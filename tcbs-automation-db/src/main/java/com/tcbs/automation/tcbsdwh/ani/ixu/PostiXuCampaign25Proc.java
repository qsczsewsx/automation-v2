package com.tcbs.automation.tcbsdwh.ani.ixu;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostiXuCampaign25Proc {
  public static final HibernateEdition redShiftDb = Database.TCBS_DWH.getConnection();

  @Step(" Get data from Proc")
  public static List<HashMap<String, Object>> getPostiXuCampaign25FromProc(String proc, Integer param, String table) {
    String queryBuilder = new StringBuilder()
      .append(" commit; call  ")
      .append(proc).append(" ( ")
      .append(param)
      .append(" ); ").toString();
    HibernateEdition stagingDb = Database.TCBS_DWH_STAGING.getConnection();
    stagingDb.closeSession();
    stagingDb.openSession();
    Session session = stagingDb.getSession();
    try {
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      session.createNativeQuery(queryBuilder).executeUpdate();
      String query = new StringBuilder()
        .append("select * from ")
        .append(table)
        .append(" where busdate = ")
        .append("cast(cast( ")
        .append(param)
        .append("as varchar) as date)  ").toString();
      List<HashMap<String, Object>> res = session.createNativeQuery(query)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return res;
    } catch (Exception ex) {
      throw ex;
    } finally {
      stagingDb.closeSession();
      stagingDb.shutdown();
    }
  }

  @Step("Get data from sql")

  public static List<HashMap<String, Object>> getPostiXuCampaign25FromSql(Integer tradingdate) {
    Session session = Tcbsdwh.tcbsStagingDwhDbConnection.getSession();
    session.clear();

    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" with rank_ixu as ( ");
    queryBuilder.append(" select tcbsid, custodycd,membership_type_id from ( ");
    queryBuilder.append(" 	select id,m.tcbsid,ca.custodycd, ");
    queryBuilder.append(" 	membership_type_id, ");
    queryBuilder.append(" 	applied_date, ");
    queryBuilder.append(" 	expired_date, ");
    queryBuilder.append(" 	row_number() over(PARTITION BY m.tcbsid ORDER BY m.applied_date desc) AS rn ");
    queryBuilder.append(" 	from staging.stg_ixu_ixu_membership_history m ");
    queryBuilder.append(" 	left join dwh.smy_dwh_cas_alluserview ca on m.tcbsid = ca.tcbsid ");
    queryBuilder.append(" 	where m.applied_date <= cast(cast(:tradingdate as varchar) as date) ) ");
    queryBuilder.append(" 	where rn = 1 ), ");
    queryBuilder.append(" fee as ( ");
    queryBuilder.append(" 	select custodycd, ");
    queryBuilder.append(" 		txdate, ");
    queryBuilder.append(" 		sum(nvl(fee_amt_detail,0) + nvl(feetax_amt_detail,0)) as feetax ");
    queryBuilder.append(" 	from dwh.prc_flx_stock_tradingfee  ");
    queryBuilder.append(" 	where sectype_name in ('Cổ phiếu thường', 'Chứng quyền', 'Chứng chỉ quỹ') ");
    queryBuilder.append(" 	and txdate = cast(cast(:tradingdate as varchar) as date) ");
    queryBuilder.append(" 	group by txdate, custodycd ), ");
    queryBuilder.append(" amt as ( ");
    queryBuilder.append(" 	select fa.busdate,fa.custodycd, ca.tcbsid,  ");
    queryBuilder.append(" 		sum(nvl(fa.buyamt,0)) + sum(nvl(fa.sellamt,0)) amt, ");
    queryBuilder.append(" 		nvl(f.feetax,0) as feetax_amt, ");
    queryBuilder.append(" 		nvl(amt,0) + nvl(feetax,0) as total_amount ");
    queryBuilder.append(" 	from dwh.smy_dwh_flx_allstocktxn fa ");
    queryBuilder.append(" 		left join fee f on fa.custodycd = f.custodycd and f.txdate = fa.busdate  ");
    queryBuilder.append(" 		left join dwh.smy_dwh_cas_alluserview ca on ca.custodycd = fa.custodycd  ");
    queryBuilder.append(" 		left join (select custodycd from dwh.smy_dwh_iber_direct_customers) dc  ");
    queryBuilder.append(" 			on fa.custodycd = dc.custodycd  ");
    queryBuilder.append(" 		left join (select tcbs_id from stg_ipartner_wp ");
    queryBuilder.append(
      " 					where status = 'ACTIVE' or (end_date is not null and cast(cast(:tradingdate as varchar) as date) >= trunc(created) and cast(cast(:tradingdate as varchar) as date) < trunc(end_date))) wp ");
    queryBuilder.append(" 			on wp.tcbs_id = ca.tcbsid  ");
    queryBuilder.append(" 		left join (select subscriber_id from staging.stg_ipartner_wp_relation  ");
    queryBuilder.append(
      " 					where status = 'ACTIVE' or (ineffective_date is not null and cast(cast(:tradingdate as varchar) as date) >= trunc(created_date) and cast(cast(:tradingdate as varchar) as date) < trunc(ineffective_date))) wpr ");
    queryBuilder.append(" 			on wpr.subscriber_id = ca.tcbsid ");
    queryBuilder.append(" 		left join (select code_105c from staging.stg_ixu_blacklist ");
    queryBuilder.append(
      " 					where campaign_id = 25 and (deleted = 0 or (updated_date is not null and cast(cast(:tradingdate as varchar) as date) >= trunc(created_date) and cast(cast(:tradingdate as varchar) as date) < trunc(updated_date)))) ib ");
    queryBuilder.append(" 			on ib.code_105c = fa.custodycd  ");
    queryBuilder.append(" 	where sectype = 'Stock' ");
    queryBuilder.append(" 		and field in ('Mua','Bán') ");
    queryBuilder.append(" 		and busdate = cast(cast(:tradingdate as varchar) as date) ");
    queryBuilder.append(" 		and dc.custodycd is null ");
    queryBuilder.append(" 		and wp.tcbs_id is null  ");
    queryBuilder.append(" 		and wpr.subscriber_id is null ");
    queryBuilder.append(" 		and ib.code_105c is null ");
    queryBuilder.append(" 	group by fa.busdate,fa.custodycd,ca.tcbsid,f.feetax ) ");
    queryBuilder.append(" select a.tcbsid, a.custodycd,a.total_amount, a.feetax_amt,r.membership_type_id, ");
    queryBuilder.append(" 	case when r.membership_type_id = 1 then round(total_amount/1000000000*50,0) ");
    queryBuilder.append(" 		when r.membership_type_id = 2 then round(total_amount/1000000000*75,0) ");
    queryBuilder.append(" 		when r.membership_type_id = 3 then round(total_amount/1000000000*150,0) ");
    queryBuilder.append(" 		else 0 end as Point ");
    queryBuilder.append(" from amt a ");
    queryBuilder.append(" 	left join rank_ixu r on a.tcbsid = r.tcbsid  order by a.custodycd");

    try {
      List<HashMap<String, Object>> resultList = Tcbsdwh.tcbsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("tradingdate", tradingdate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    } finally {
      Tcbsdwh.tcbsStagingDwhDbConnection.closeSession();
    }
    return new ArrayList<>();
  }
}

