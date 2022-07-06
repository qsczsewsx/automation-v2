package com.tcbs.automation.tcbsdwh.ani.monthlystatement;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MonthlyStatementProc7Entity {
  public static final HibernateEdition redShiftDb = Database.TCBS_DWH.getConnection();

  @Step("Get data from sql")
  public static List<HashMap<String, Object>> getSaoKeThangSql(String endmonth) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" with ixuName as ( ");
    queryBuilder.append(" select mh.tcbsid ");
    queryBuilder.append(" ,case when mh.expired_date >=cast (:endmonth as timestamp) then mt.name else 'Silver' END AS name ");
    queryBuilder.append(" from (select tcbsid,membership_type_id,created_date ,expired_date ");
    queryBuilder.append(" ,row_number() over(PARTITION BY tcbsid ORDER BY created_date DESC) AS rn ");
    queryBuilder.append(" from staging.stg_ixu_ixu_membership_history) mh ");
    queryBuilder.append(" left join staging.stg_ixu_ixu_membership_type mt ON mh.membership_type_id = mt.id ");
    queryBuilder.append(" where mh.rn=1 ");
    queryBuilder.append(" )  ");
    queryBuilder.append(" , ");
    queryBuilder.append(" ixuPoint as ( ");
    queryBuilder.append(" SELECT tcbsid ");
    queryBuilder.append(" , sum(CASE WHEN lower(award_type) = 'ranking' THEN point END) AS ranking_point ");
    queryBuilder.append(" FROM staging.stg_ixu_ixu_transaction_history ");
    queryBuilder.append(" WHERE (issued_date <= cast (:endmonth as timestamp) AND cast (:endmonth as timestamp) < expired_date) ");
    queryBuilder.append(" GROUP BY tcbsid ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" , ");
    queryBuilder.append(" outs as ( ");
    queryBuilder.append(" select * from ( ");
    queryBuilder.append(" SELECT tcbsid, outstanding ");
    queryBuilder.append(" , row_number() OVER(PARTITION BY tcbsid ORDER BY created_date DESC, ID DESC) AS rn ");
    queryBuilder.append(" FROM staging.stg_ixu_ixu_transaction_history ");
    queryBuilder.append(" WHERE created_date <= cast (:endmonth as timestamp)) ");
    queryBuilder.append(" WHERE rn = 1 ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" , ");
    queryBuilder.append(" ixu_convert as ( ");
    queryBuilder.append(" select tcbs_id ");
    queryBuilder.append(" ,sum(case when gt.campaign_id = 18 then point end) as money_total ");
    queryBuilder.append(" ,sum(case when gt.campaign_id = 12 then point end) as VinID_total ");
    queryBuilder.append(" ,sum(case when gt.campaign_id = 62 then point end) as ccq ");
    queryBuilder.append(" ,sum(case when gt.campaign_id = 71 then point end) as gift ");
    queryBuilder.append(" from staging.stg_ixu_general_transaction gt ");
    queryBuilder.append(" where gt.STATUS = 'SUCCESS' ");
    queryBuilder.append(" and issue_date >= date_trunc ('month',cast(:endmonth as timestamp) ) and issue_date <= cast (:endmonth as timestamp) ");
    queryBuilder.append(" group by tcbs_id ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" select top 50000 i.tcbsid, ");
    queryBuilder.append(" cast (:endmonth as timestamp) as monthreport, ");
    queryBuilder.append(" i.name as membership_type, ");
    queryBuilder.append(" isnull(ip.ranking_point,0) as rankingpoint, ");
    queryBuilder.append(" isnull(o.outstanding,0) as RedeemablePoint, ");
    queryBuilder.append(" isnull(ic.money_total,0) as cash_redeem, ");
    queryBuilder.append(" isnull(ic.VinID_total,0) as vinid_redeem, ");
    queryBuilder.append(" isnull(ic.ccq,0) as fund_redeem, ");
    queryBuilder.append(" isnull(ic.gift,0) as gift_redeem ");
    queryBuilder.append(" from ixuName i ");
    queryBuilder.append(" left join ixuPoint ip on i.tcbsid = ip.tcbsid ");
    queryBuilder.append(" left join outs o on i.tcbsid = o.tcbsid ");
    queryBuilder.append(" left join ixu_convert ic on i.tcbsid = ic.tcbs_id order by tcbsid,i.name,ic.money_total");


    try {
      redShiftDb.closeSession();
      redShiftDb.openSession();
      List<HashMap<String, Object>> data = redShiftDb.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("endmonth", endmonth)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return data;


    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    redShiftDb.closeSession();
    return new ArrayList<>();
  }

  @Step("Get data from sql")
  public static List<HashMap<String, Object>> getSaoKeThangProcSql() {
    StringBuilder queryBuilderProc = new StringBuilder();

    queryBuilderProc.append(" select top 50000 * from dwh.smy_dwh_monthlystatement_ixugeneral order by tcbsid,membership_type,redeemablepoint ");

    try {
      redShiftDb.closeSession();
      redShiftDb.openSession();
      return redShiftDb.getSession().createNativeQuery(queryBuilderProc.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    redShiftDb.closeSession();
    return new ArrayList<>();
  }

}
