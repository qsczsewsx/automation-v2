package com.tcbs.automation.tcbsdwh.ani.getsubaumforiwp;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetSubaumForiwpEntity {
  public static final HibernateEdition redShiftDb = Database.TCBS_DWH_STAGING.getConnection();

  @Step("Get data from sql")
  public static List<HashMap<String, Object>> getSubaumForiwpSql(String idiwp, String fromdate) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" with SUB as( ");
    queryBuilder.append(" select partner_id,subscriber_id ");
    queryBuilder.append(" ,cast(created_date as date) created_date ");
    queryBuilder.append(" ,isnull(cast(ineffective_date as date), date '2100-01-01') ineffective_date ");
    queryBuilder.append(" , custodycode ");
    queryBuilder.append(" from staging.stg_ipartner_wp_relation r ");
    queryBuilder.append(" left join staging.stg_tcb_customer c  ");
    queryBuilder.append(" on r.subscriber_id = c.tcbsid  ");
    queryBuilder.append(" and c.active = 1 ");
    queryBuilder.append(" where (upper(status) = 'ACTIVE'  ");
    queryBuilder.append(" or (upper(status) = 'DELETED' and ineffective_date is not NULL)) ");
    queryBuilder.append(" and partner_id is not null ");
    queryBuilder.append(" and  cast(:fromdate as date) >= cast(created_date as Date) ");
    queryBuilder.append(" and  cast(:fromdate as date) <isnull(cast(ineffective_date as date), date '2100-01-01')  ");
    queryBuilder.append(" )  ");
    queryBuilder.append(" ,STOCK_BAL AS( ");
    queryBuilder.append(" select dlp.custodycd,nvl(sum(case when subaccounttype in ('Thường','Margin','iCopy Trader', 'iCopy Copier') then mktamt else 0 end), 0) stock_bal ");
    queryBuilder.append(" ,cast(dlp.datereport as date) date_report ");
    queryBuilder.append(" from dwh.Dailyport_StockBal dlp ");
    queryBuilder.append(" left join (select distinct custodycd ,afacctno ,subaccounttype from staging.vw_flx_accounttype)u ");
    queryBuilder.append(" on dlp.afacctno = u.afacctno ");
    queryBuilder.append(" where 1=1 and dlp.custodycd  in (select distinct custodycode from  SUB) ");
    queryBuilder.append(" group by dlp.custodycd , datereport ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" ,PS_BAL AS( ");
    queryBuilder.append(" select ba.c_dcterm_code as custodycd  ");
    queryBuilder.append(" ,trunc(bcbh.c_transaction_date) as c_transaction_date             ");
    queryBuilder.append(" ,sum(nvl(bcbh.c_cash_balance, 0) + nvl(bcbh.c_cash_day_in, 0) - nvl(bcbh.c_cash_day_out, 0)) as dercashbal    ");
    queryBuilder.append(" ,sum(nvl(bcbhd.c_cash_balance, 0) + nvl(bcbhd.c_cash_day_in, 0) - nvl(bcbhd.c_cash_day_out, 0)) as dervsdcashbal ");
    queryBuilder.append(" from staging.stg_psbo_t_back_account ba   ");
    queryBuilder.append("  ");
    queryBuilder.append(" left join staging.stg_psbo_t_back_cash_balance_history bcbh  ");
    queryBuilder.append(" on bcbh.c_account_code = ba.c_account_code  ");
    queryBuilder.append(" left join staging.stg_psbo_t_back_cash_balance_his_de bcbhd  ");
    queryBuilder.append(" on bcbhd.c_account_code = ba.c_account_code  ");
    queryBuilder.append(" where ba.c_dcterm_code in (select distinct custodycode from  SUB) ");
    queryBuilder.append(" group by ba.c_dcterm_code ,trunc(bcbh.c_transaction_date) ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" ,BOND_BAL as ( ");
    queryBuilder.append(" select custodycd,nvl(sum(principal), 0) bond_bal ");
    queryBuilder.append(" ,cast(datereport as date) date_report ");
    queryBuilder.append(" from dwh.Dailyport_BondBal bal  ");
    queryBuilder.append(" where 1=1 and custodycd in (select distinct custodycode from SUB) ");
    queryBuilder.append(" group by custodycd,datereport ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" ,FUND_BAL as( ");
    queryBuilder.append(" select custodycd,nvl(sum(curramt), 0) fund_bal ");
    queryBuilder.append(" ,cast(datereport as date) date_report ");
    queryBuilder.append(" from dwh.Dailyport_FundBal ");
    queryBuilder.append(" where 1=1 and custodycd in (select distinct custodycode from  SUB) ");
    queryBuilder.append(" group by custodycd,datereport ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" ,CASH_BAL AS( ");
    queryBuilder.append(" select cb.custodycd ");
    queryBuilder.append(" ,nvl(sum(case when tk.subaccounttype in ('Thường','iCopy Trader','Margin') then cb.totalbalance else 0 end) ,0) as cash_bal  ");
    queryBuilder.append(" ,nvl(sum(case when tk.subaccounttype = 'iCopy Copier' then nvl(cb.totalbalance, 0) else 0 end), 0) as icopycash_bal ");
    queryBuilder.append(" ,cast (cb.txdate as date) date_report ");
    queryBuilder.append(" from dwh.Dailyport_CashBal cb ");
    queryBuilder.append(" left join (select distinct custodycd ,afacctno ,subaccounttype from staging.vw_flx_accounttype) tk  ");
    queryBuilder.append(" on cb.afacctno = tk.afacctno ");
    queryBuilder.append(" where 1=1 and cb.custodycd in (select distinct custodycode from  SUB) ");
    queryBuilder.append(" group by cb.custodycd,cb.txdate ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" ,ISAVE_CASH_BAL as ( ");
    queryBuilder.append(" select tcbs_id,cast(reportdate as date) date_report ");
    queryBuilder.append(" ,nvl(sum(balance), 0) isave_bal ");
    queryBuilder.append(" from dwh.smy_dwh_isave_dailybalance ");
    queryBuilder.append(" where 1=1 ");
    queryBuilder.append(" and tcbs_id in (select distinct subscriber_id from  SUB) ");
    queryBuilder.append(" group by tcbs_id,reportdate ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" ,TB as(  ");
    queryBuilder.append(" select sub.partner_id ");
    queryBuilder.append(" ,sub.subscriber_id  ");
    queryBuilder.append(" ,sub.created_date  ");
    queryBuilder.append(" ,sub.ineffective_date  ");
    queryBuilder.append(" ,sub.custodycode  ");
    queryBuilder.append(" ,nvl(stk.date_report, bnd.date_report, fnd.date_report, csh.date_report, cis.date_report , cast(:fromdate as date) ) date_report  ");
    queryBuilder.append(" ,nvl(stk.stock_bal+ps.dercashbal+ps.dervsdcashbal, 0) stock_bal ");
    queryBuilder.append(" ,nvl(bnd.bond_bal, 0) bond_bal ");
    queryBuilder.append(" ,nvl(fnd.fund_bal, 0) fund_bal ");
    queryBuilder.append(" ,nvl(csh.cash_bal, 0) cash_bal ");
    queryBuilder.append(" ,nvl(csh.icopycash_bal, 0) icopycash_bal ");
    queryBuilder.append(" ,nvl(cis.isave_bal, 0) isave_bal ");
    queryBuilder.append(" from sub ");
    queryBuilder.append(" left join STOCK_BAL stk on sub.custodycode = stk.custodycd ");
    queryBuilder.append(" left join PS_BAL ps on sub.custodycode = ps.custodycd ");
    queryBuilder.append(" left join bond_bal bnd on sub.custodycode = bnd.custodycd ");
    queryBuilder.append(" left join fund_bal fnd on sub.custodycode = fnd.custodycd ");
    queryBuilder.append(" left join cash_bal csh on sub.custodycode = csh.custodycd ");
    queryBuilder.append(" left join isave_cash_bal cis on sub.subscriber_id = cis.tcbs_id) ");
    queryBuilder.append(" select * from ( ");
    queryBuilder.append(" select  partner_id  ");
    queryBuilder.append(" ,date_report ");
    queryBuilder.append(" ,round(sum(nvl(stock_bal, 0)+nvl(icopycash_bal, 0)), 0) stock_bal ");
    queryBuilder.append(" ,round(sum(nvl(bond_bal, 0)), 0) bond_bal ");
    queryBuilder.append(" ,round(sum(nvl(fund_bal, 0)), 0) fund_bal ");
    queryBuilder.append(" ,round(sum(nvl(cash_bal, 0)+nvl(isave_bal, 0)), 0) cash_bal ");
    queryBuilder.append(" from TB ");
    queryBuilder.append(" group by partner_id,date_report  ");
    queryBuilder.append(" having partner_id = :idiwp and date_report = cast(:fromdate as date) ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" union  ");
    queryBuilder.append(" select  ");
    queryBuilder.append(" case when :idiwp not in (select partner_id from TB) then null ");
    queryBuilder.append(" when (select count(*) from tb where partner_id = :idiwp  ");
    queryBuilder.append(" and date_report = cast(:fromdate as date)) = 0 then :idiwp ");
    queryBuilder.append(" else null end as partner_id ");
    queryBuilder.append(" ,case when  ");
    queryBuilder.append(" (select count(*) from tb where partner_id = :idiwp  ");
    queryBuilder.append(" and date_report = cast(:fromdate as date)) = 0 then cast(:fromdate as date) ");
    queryBuilder.append(" else null end as date_report ");
    queryBuilder.append(" ,'0' as stock_bal ");
    queryBuilder.append(" ,'0' as bond_bal ");
    queryBuilder.append(" ,'0' as fund_bal ");
    queryBuilder.append(" ,'0' as cash_bal ");
    queryBuilder.append(" from dual ");
    queryBuilder.append(" where partner_id is not null ");


    try {
      redShiftDb.closeSession();
      redShiftDb.openSession();
      List<HashMap<String, Object>> data = redShiftDb.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("idiwp", idiwp)
        .setParameter("fromdate", fromdate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return data;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    redShiftDb.closeSession();
    return new ArrayList<>();
  }

  public static void deleteData(String idiwp) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("delete from dwh.smy_dwh_subaum_foriwp where partner_id  = '" + idiwp + "'");
    executeQuery(queryBuilder);
  }

  public static void insertData(Integer stagingdate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("CALL staging.smy_iag_get_subaum_foriwp(" + stagingdate + ")");
    executeQuery(queryBuilder);
  }

  public static void executeQuery(StringBuilder sql) {
    Session session = redShiftDb.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createNativeQuery(sql.toString());
    try {
      query.executeUpdate();
      session.getTransaction().commit();
      redShiftDb.closeSession();
    } catch (Exception e) {
      redShiftDb.closeSession();
      throw e;
    }
  }

}
