package com.tcbs.automation.tcbsdwh.ani.baymax;

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

public class MonthlyStatementProc {
  public static final HibernateEdition redShiftDb = Database.TCBS_DWH.getConnection();
//  public static final HibernateEdition stagingDb = Database.TCBS_DWH_STAGING.getConnection();

  @Step(" Get data from Proc")
  public static List<HashMap<String, Object>> getDataFromProc(String proc, String param, String table) {
    String queryBuilder = new StringBuilder()
      .append(" commit; call  ")
      .append(proc).append(" ( ")
      .append(param)
      .append(" ); ").toString();
    HibernateEdition stagingDb = Database.TCBS_DWH_STAGING.getConnection();
    stagingDb.closeSession();
    stagingDb.openSession();
    Session session = stagingDb.getSession();
//    session.clear();
    try {
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      session.createNativeQuery(queryBuilder).executeUpdate();
      String query = new StringBuilder()
        .append("select * from ")
        .append(table).toString();
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

  public static List<HashMap<String, Object>> getPostEomDetailsFromSql(String eomonth) {
    Session session = Tcbsdwh.tcbsStagingDwhDbConnection.getSession();
    session.clear();

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("with tmp_cash as ( ");
    queryBuilder.append(" select  ");
    queryBuilder.append(" cb.custodycd, round(sum(case when acc.subaccounttype in ('Thường' ,'iCopy Trader','Margin') ");
    queryBuilder.append("then nvl(totalbalance,0) else 0 end),0) CashTotalBal, ");
    queryBuilder.append(" round(sum(nvl(cb.balance,0) + nvl(cb.cash_receiving_t0,0) + nvl(cb.cash_receiving_t1,0) ");
    queryBuilder.append(" + nvl(cb.cash_receiving_t2,0) +nvl(cdiv.amount,0)),0) as cce, ");
    queryBuilder.append("round(sum(case when acc.subaccounttype = 'iCopy Copier' then nvl(totalbalance,0) ");
    queryBuilder.append(" else 0 end),0) as IcopyCasBal ");
    queryBuilder.append("from dwh.dailyport_cashbal cb ");
    queryBuilder.append("left join (select distinct custodycd,afacctno,subaccounttype from staging.vw_flx_accounttype) acc on cb.afacctno = acc.afacctno ");
    queryBuilder.append("left join (select custodycd, ");
    queryBuilder.append(" sum(nvl(stcv,0)) as amount ");
    queryBuilder.append("from dwh.dailyport_CashDiv ");
    queryBuilder.append("where txdate = cast(:eomonth as date) ");
    queryBuilder.append("group by custodycd) cdiv on cdiv.custodycd = cb.custodycd ");
    queryBuilder.append("where cb.txdate = cast(:eomonth as date) group by cb.custodycd ");
    queryBuilder.append("order by custodycd ");
    queryBuilder.append(")");
    queryBuilder.append(",");

    queryBuilder.append("tmp_isave as ( ");
    queryBuilder.append(" select stca.custodycd , ");
    queryBuilder.append(" round(nvl(balance,0),0) as iSaveBal, ");
    queryBuilder.append(" reportdate ");
    queryBuilder.append("from dwh.smy_dwh_isave_dailybalance sdid ");
    queryBuilder.append("left join dwh.smy_dwh_cas_alluserview stca on sdid.tcbs_id = stca.tcbsid ");
    queryBuilder.append("where reportdate = cast(:eomonth as date) ");
    queryBuilder.append(")");
    queryBuilder.append(",");

    queryBuilder.append("tmp_stockeom as ( ");
    queryBuilder.append(" select ds.custodycd, ");
    queryBuilder.append(" round(SUM(CASE WHEN subaccounttype IN ('Thường','Margin','iCopy Trader') THEN NVL(costprice*quantity,0) ");
    queryBuilder.append(" ELSE 0 END),0) AS StockCost, ");
    queryBuilder.append(" round(SUM(CASE WHEN acc.subaccounttype IN ('Thường','Margin','iCopy Trader') THEN nvl(MktAmt,0) ");
    queryBuilder.append(" ELSE 0 END),0) AS StockBal, ");
    queryBuilder.append("SUM(CASE WHEN acc.subaccounttype IN ('Thường','Margin','iCopy Trader') THEN nvl(quantity,0) ");
    queryBuilder.append(" ELSE 0 END) AS StockQuantity, ");
    queryBuilder.append("SUM(CASE WHEN acc.subaccounttype IN ('Thường','Margin','iCopy Trader') AND QttType = 'Giao dịch' THEN nvl(Quantity,0) ");
    queryBuilder.append(" ELSE 0 END) AS StockQuantityTradable, ");
    queryBuilder.append("nvl((stockbal-stockcost)/nullif(stockcost,0),0) as StockPnl_pct, ");
    queryBuilder.append("ROUND( NVL( StockBal,0) - nvl(StockCost,0),0) AS StockPnl_amt, ");
    queryBuilder.append("sum(case when acc.subaccounttype = 'iCopy Copier' then nvl(MktAmt,0) else 0 end) as Icopystockbal ");
    queryBuilder.append("from dwh.dailyport_stockbal ds ");
    queryBuilder.append("left join staging.vw_flx_accounttype acc on ds.afacctno = acc.afacctno ");
    queryBuilder.append("where datereport = cast(:eomonth as date) group by ds.custodycd ), ");


    queryBuilder.append("tmp_stocknumoftrans as ( ");
    queryBuilder.append(" select sdfa.custodycd, ");
    queryBuilder.append(" count(sdfa.*) as stocknotxn ");
    queryBuilder.append("from dwh.smy_dwh_flx_allstocktxn sdfa ");
    queryBuilder.append("left join (select distinct custodycd,afacctno,subaccounttype from staging.vw_flx_accounttype) acc on sdfa.afacctno = acc.afacctno  ");
    queryBuilder.append("where subaccounttype <> 'iCopy Copier' ");
    queryBuilder.append("and Field in ('Mua', 'Bán') ");
    queryBuilder.append("and Sectype = 'Stock' ");
    queryBuilder.append("and busdate between date_trunc('mon',cast(:eomonth as date)) and cast(:eomonth as date) ");
    queryBuilder.append("group by sdfa.custodycd ");
    queryBuilder.append(")");
    queryBuilder.append(",");

    queryBuilder.append("tmp_icopy as ( ");
    queryBuilder.append(" select ia.custody_id as custodycd , ");
    queryBuilder.append(" sum(net_invested) icopiernetinverted ");
    queryBuilder.append("from staging.stg_icopy_account ia ");
    queryBuilder.append("left join stg_icopy_copier_account ica on ia.id = ica.account_id ");
    queryBuilder.append("where ia.account_type = 'COPYER' ");
    queryBuilder.append("and (ica.status = 'ACTIVE' ");
    queryBuilder.append(" OR (ica.status = 'STOPPED' AND ica.stopped_time BETWEEN DATE_TRUNC('mon',DATEADD('mon',1,cast(:eomonth as date))) AND LAST_DAY(DATEADD('mon',1,cast(:eomonth as date))))) ");
    queryBuilder.append("group by ia.custody_id ");
    queryBuilder.append(")");
    queryBuilder.append(",");

    queryBuilder.append("tmp_der as ( ");
    queryBuilder.append(" select c_dcterm_code as custodycd, ");
    queryBuilder.append(" round(nvl(bcbh.c_cash_balance,0) + nvl(bcbh.c_cash_day_in,0) - nvl(bcbh.c_cash_day_out,0),0) AS DerCashBal, ");
    queryBuilder.append("round(nvl(bcbd.c_cash_balance,0) + nvl(bcbd.c_cash_day_in,0) - nvl(bcbd.c_cash_day_out,0),0) AS DerVsdCashBal, ");
    queryBuilder.append("DerCashBal + DerVsdCashBal as DerBal, ");
    queryBuilder.append("round(nvl(bcdi.C_DEBIT_TOTAL,0) +nvl(vsd.DerVsdFee,0) + GREATEST(0,DerVsdCashBal),0) as DerTotalDebt ");
    queryBuilder.append("from staging.stg_psbo_t_back_account ba ");
    queryBuilder.append(
      "left join staging.stg_psbo_t_back_cash_balance_history bcbh on ba.c_account_code = bcbh.c_account_code and c_transaction_date = (SELECT max(c_transaction_date) FROM staging.stg_psbo_t_back_cash_balance_history WHERE c_transaction_date <= cast(:eomonth as date)) ");
    queryBuilder.append(
      "left join staging.stg_psbo_t_back_cash_balance_his_de bcbd on ba.c_account_code = bcbd.c_account_code and c_trading_date = (SELECT max(c_trading_date) FROM staging.stg_psbo_t_back_cash_balance_his_de WHERE c_trading_date <= cast(:eomonth as date)) ");
    queryBuilder.append("left join (select c_account_code, ");
    queryBuilder.append(" nvl(c_debit_total,0)c_debit_total ");
    queryBuilder.append("from staging.stg_psbo_T_BACK_CASH_DISBURSEMENT_INTEREST ");
    queryBuilder.append(
      "where TO_DATE(c_day ,'DD/MM/YYYY') = (SELECT max(TO_DATE(c_day ,'DD/MM/YYYY')) FROM staging.stg_psbo_T_BACK_CASH_DISBURSEMENT_INTEREST bcbh WHERE TO_DATE(c_day ,'DD/MM/YYYY') <= cast(:eomonth as date))) bcdi on ba.c_account_code = bcdi.c_account_code ");
    queryBuilder.append("left join (select c_account_code, ");
    queryBuilder.append(" SUM(nvl(c_fee_value,0) - nvl(c_fee_payment,0)) AS DerVsdFee ");
    queryBuilder.append("from staging.stg_psbo_T_DE_VSD_FEE ");
    queryBuilder.append("where c_trading_date BETWEEN DATE_TRUNC('mon',cast(:eomonth as date)) AND cast(:eomonth as date) ");
    queryBuilder.append("group by c_account_code) vsd on ba.c_account_code = vsd.c_account_code ");
    queryBuilder.append(")");
    queryBuilder.append(",");

    queryBuilder.append("tmp_bond as (select custodycd, ");
    queryBuilder.append(" round(sum(nvl(principal,0)),0) as bondbal ");
    queryBuilder.append("from dwh.Dailyport_bondbal where datereport = cast(:eomonth as date) ");
    queryBuilder.append("group by custodycd), ");


    queryBuilder.append("tmp_fund as (select custodycd, ");
    queryBuilder.append(" round(sum(nvl(curramt,0)),0) as fundbal, ");
    queryBuilder.append("round(sum(nvl(costamount,0)),0) as fundcost, ");
    queryBuilder.append("nvl((fundbal - fundcost)/nullif(fundcost,0),0) as FundPnl_pct ");
    queryBuilder.append("from dwh.dailyport_fundbal df where datereport = cast(:eomonth as date) group by custodycd), ");


    queryBuilder.append("tmp_debt as ( ");
    queryBuilder.append(" select nvl(dm.custodycd,df.custodycd,du.custodycd) as custodycd, ");
    queryBuilder.append("round(nvl(marginbal,0),0) as marginbal, ");
    queryBuilder.append("round(nvl(DepositoryFee,0),0) as DepositoryFee, ");
    queryBuilder.append("round(nvl(cashadvance,0),0) as cashadvance ");
    queryBuilder.append("from (select dm.custodycd, ");
    queryBuilder.append(" round(sum(nvl(lnprin,0) + nvl(intamt,0)),0) as marginbal ");
    queryBuilder.append(" from dwh.dailyport_marginbal dm where datereport = cast(:eomonth as date) ");
    queryBuilder.append("group by dm.custodycd) dm ");
    queryBuilder.append("full outer join ");
    queryBuilder.append(" (select custodycd, ");
    queryBuilder.append(" round(sum(nvl(no_tai_t_date,0)),0) as DepositoryFee ");
    queryBuilder.append(" from dwh.smy_dwh_flx_ci0032_depositoryfee where datereport = cast(:eomonth as date) ");
    queryBuilder.append("group by custodycd) df on dm.custodycd = df.custodycd ");
    queryBuilder.append("full outer join ");
    queryBuilder.append(" (select custodycd, ");
    queryBuilder.append(" round(sum(nvl(amt,0)),0) as cashadvance ");
    queryBuilder.append(" from dwh.dailyport_uttb ");
    queryBuilder.append(" where date_trans = cast(:eomonth as date) ");
    queryBuilder.append("group by custodycd) du on nvl(df.custodycd,dm.custodycd) = du.custodycd ");
    queryBuilder.append("), ");

    queryBuilder.append(" tmp_cashbeginbal as ( ");
    queryBuilder.append(" select t.custodycd, ");
    queryBuilder.append(" round(min(ci_begin_bal),0) as CashBeginBal ");
    queryBuilder.append(" from (select custodycd, min(busdate) as Busdate ");
    queryBuilder.append(" from dwh.DailyPort_CashTxn ");
    queryBuilder.append(" where busdate between date_trunc('mon',cast (:eomonth as date)) and cast(:eomonth as date) ");
    queryBuilder.append(" group by custodycd) t ");
    queryBuilder.append(" left join dwh.DailyPort_CashTxn t2 on t.custodycd = t2.custodycd and t.Busdate = t2.busdate ");
    queryBuilder.append(" group by t.custodycd ");
    queryBuilder.append(" ), ");

    queryBuilder.append("tmp_cashtxn as ( ");
    queryBuilder.append(" select custodycd, round(sum(nvl(ci_credit_amt,0)),0) as cashtotalcredit, ");
    queryBuilder.append("round(sum(nvl(ci_debit_amt,0)),0) as cashtotaldebit ");
    queryBuilder.append("from dwh.DailyPort_CashTxn dc ");
    queryBuilder.append("where busdate between date_trunc('mon',cast (:eomonth as date)) and cast(:eomonth as date) ");
    queryBuilder.append("group by custodycd ) ");

    queryBuilder.append("select acc.custodycd, ");
    queryBuilder.append(" nvl(tmp_cash.CashTotalBal,0) as CashTotalBal, ");
    queryBuilder.append("nvl(tmp_isave.iSaveBal,0) as iSaveBal, ");
    queryBuilder.append("round(nvl(tmp_stockeom.StockBal,0),0) as StockBal, ");
    queryBuilder.append("NVL(tmp_stockeom.StockPnl_amt,0) AS StockPnl_amt, ");
    queryBuilder.append("nvl(tmp_stockeom.StockPnl_pct,0) as StockPnl_pct, ");
    queryBuilder.append("round(nvl(tmp_stockeom.StockQuantity,0),0) as StockQuantity, ");
    queryBuilder.append("round(nvl(tmp_stockeom.StockQuantityTradable,0),0) as StockQuantityTradable, ");
    queryBuilder.append("nvl(tmp_stocknumoftrans.stocknotxn,0) as stocknotxn, ");
    queryBuilder.append("nvl(tmp_cash.IcopyCasBal,0) + nvl(tmp_stockeom.Icopystockbal,0) as icopybal, ");
    queryBuilder.append("nvl(tmp_der.DerBal,0) as DerBal, ");
    queryBuilder.append("nvl(((nvl(icopybal,0) - nvl(tmp_icopy.icopiernetinverted,0))/nullif(tmp_icopy.icopiernetinverted,0)),0) as iCopyStockPnl_pct, ");
    queryBuilder.append("nvl(tmp_bond.bondbal,0) as bondbal, ");
    queryBuilder.append("nvl(tmp_fund.fundbal,0) as fundbal, ");
    queryBuilder.append("nvl(tmp_cash.cce,0) as cce,");
    queryBuilder.append(" nvl(tmp_cashbeginbal.cashbeginbal,0) as cashbeginbal, ");
    queryBuilder.append(" nvl(tmp_cashtxn.cashtotalcredit,0) as cashtotalcredit, ");
    queryBuilder.append(" nvl(tmp_cashtxn.cashtotaldebit,0) as cashtotaldebit, ");
    queryBuilder.append(" nvl(cashbeginbal,0) + nvl(cashtotalcredit,0) - nvl(cashtotaldebit,0) as CashEndBal, ");
    queryBuilder.append("nvl(tmp_fund.FundPnl_pct,0) as FundPnl_pct, ");
    queryBuilder.append("nvl(tmp_debt.marginbal,0) + nvl(tmp_debt.DepositoryFee,0) + nvl(tmp_debt.cashadvance,0) + nvl(tmp_der.DerTotalDebt,0) as TotalDebt, ");
    queryBuilder.append("nvl(tmp_cash.CashTotalBal,0) + nvl(tmp_isave.iSaveBal,0) + nvl(tmp_stockeom.StockBal,0) + nvl(icopybal,0) ");
    queryBuilder.append(" + nvl(tmp_der.DerBal,0) + nvl(tmp_bond.bondbal,0) + nvl(tmp_fund.fundbal,0) as TotalAssets, ");
    queryBuilder.append("round(nvl(TotalAssets,0) - nvl(TotalDebt,0),0) as NetAssets, ");
    queryBuilder.append(
      "NVL(CashTotalBal,0) + NVL(iSaveBal,0) + NVL(tmp_stockeom.StockCost,0) + NVL(BondBal,0) + NVL(tmp_fund.FundCost,0) + ROUND(NVL(tmp_icopy.icopiernetinverted,0),0) + NVL(DerBal,0) AS TotalAssetsCost, ");
    queryBuilder.append("nvl( (TotalAssets - TotalAssetsCost)/ NULLIF(TotalAssetsCost,0),0) AS PortfolioPnl_pct, ");
    queryBuilder.append("nvl(CashTotalBal/nullif(TotalAssets,0),0) as cashtotalbal_prop, ");
    queryBuilder.append("nvl(iSaveBal/nullif(TotalAssets,0),0) as iSaveBal_prop, ");
    queryBuilder.append("nvl(StockBal/nullif(TotalAssets,0),0) as stockbal_prop, ");
    queryBuilder.append("nvl(BondBal/nullif(TotalAssets,0),0) as bondbal_prop, ");
    queryBuilder.append("nvl(FundBal/nullif(TotalAssets,0),0) as fundbal_prop, ");
    queryBuilder.append("nvl(icopybal/nullif(TotalAssets,0),0) as icopy_prop, ");
    queryBuilder.append("nvl(DerBal/nullif(TotalAssets,0),0) as derbal_prop, ");
    queryBuilder.append(
      "nvl((nvl(tmp_cash.CashTotalBal,0) + nvl(tmp_isave.iSaveBal,0) + nvl(icopybal,0)+ nvl(tmp_der.DerBal,0) + nvl(tmp_bond.bondbal,0) + nvl(tmp_fund.fundbal,0))/nullif(TotalAssets,0),0) as otherassets_prop ");
    queryBuilder.append("from (select distinct custodycd from staging.vw_flx_accounttype) acc ");
    queryBuilder.append("left join tmp_cash on acc.custodycd = tmp_cash.custodycd ");
    queryBuilder.append("left join tmp_isave on acc.custodycd = tmp_isave.custodycd ");
    queryBuilder.append("left join tmp_stockeom on acc.custodycd = tmp_stockeom.custodycd ");
    queryBuilder.append("left join tmp_stocknumoftrans on acc.custodycd = tmp_stocknumoftrans.custodycd ");
    queryBuilder.append("left join tmp_icopy on acc.custodycd = tmp_icopy.custodycd ");
    queryBuilder.append("left join tmp_der on acc.custodycd = tmp_der.custodycd ");
    queryBuilder.append("left join tmp_bond on acc.custodycd = tmp_bond.custodycd ");
    queryBuilder.append("left join tmp_fund on acc.custodycd = tmp_fund.custodycd ");
    queryBuilder.append("left join tmp_debt on acc.custodycd = tmp_debt.custodycd ");
    queryBuilder.append("left join tmp_cashtxn on acc.custodycd = tmp_cashtxn.custodycd ");
    queryBuilder.append("left join tmp_cashbeginbal on acc.custodycd = tmp_cashbeginbal.custodycd ");


    try {
      List<HashMap<String, Object>> resultList = Tcbsdwh.tcbsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("eomonth", eomonth)
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

  public static List<HashMap<String, Object>> getPostEowFromSql(String eomonth) {

    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" with tmp_date as ( ");
    queryBuilder.append("   select distinct date, lpickenddate ");
    queryBuilder.append("   from dwh.smy_dwh_date_dim ");
    queryBuilder.append("   where date_part('d',date) in (1,7,14,21,28,date_part('d',cast(:eomonth as date) )) ");
    queryBuilder.append(" and last_day(date) = cast(:eomonth as date) ");
    queryBuilder.append(" order by date ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" , ");

    queryBuilder.append(" tmp_cash as ( ");
    queryBuilder.append("   select cb.custodycd, ");
    queryBuilder.append("   cb.txdate as reportdate, ");
    queryBuilder.append(" round(sum(case when acc.subaccounttype in ('Thường' ,'iCopy Trader','Margin') ");
    queryBuilder.append(" then nvl(totalbalance,0) else 0 end),0) CashTotalBal, ");
    queryBuilder.append("   round(sum(case when acc.subaccounttype = 'iCopy Copier' then nvl(totalbalance,0) ");
    queryBuilder.append(" 			else 0 end),0) as IcopyCasBal ");
    queryBuilder.append(" from dwh.dailyport_cashbal cb ");
    queryBuilder.append(" left join (select distinct custodycd,afacctno ,subaccounttype from staging.vw_flx_accounttype) acc on cb.afacctno  = acc.afacctno  ");
    queryBuilder.append(" where cb.txdate in (select date from tmp_date) ");
    queryBuilder.append(" group by cb.custodycd,cb.txdate ");
    queryBuilder.append(" order by custodycd ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" , ");

    queryBuilder.append(" tmp_isave as ( ");
    queryBuilder.append("   select stca.custodycd , ");
    queryBuilder.append("   reportdate, ");
    queryBuilder.append("   round(nvl(balance,0),0) as iSaveBal ");
    queryBuilder.append(" from dwh.smy_dwh_isave_dailybalance sdid ");
    queryBuilder.append(" left join dwh.smy_dwh_cas_alluserview  stca on  sdid.tcbs_id  = stca.tcbsid ");
    queryBuilder.append(" where reportdate in (select date from tmp_date) ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" , ");

    queryBuilder.append(" tmp_stock as ( ");
    queryBuilder.append("   select ds.custodycd, datereport as reportdate, ");
    queryBuilder.append(" round(SUM(CASE WHEN acc.subaccounttype IN ('Thường','Margin','iCopy Trader') THEN nvl(MktAmt,0) ");
    queryBuilder.append("   ELSE 0 END),0) AS StockBal, ");
    queryBuilder.append(" round(sum(case when acc.subaccounttype = 'iCopy Copier' then nvl(MktAmt,0) else 0 end),0) as Icopystockbal ");
    queryBuilder.append(" from dwh.dailyport_stockbal ds ");
    queryBuilder.append(" left join (select distinct custodycd,afacctno ,subaccounttype from staging.vw_flx_accounttype) acc on ds.afacctno = acc.afacctno ");
    queryBuilder.append(" where datereport in (select date from tmp_date) ");
    queryBuilder.append(" group by ds.custodycd,datereport ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" , ");

    queryBuilder.append(" tmp_der as ( ");
    queryBuilder.append("   select c_dcterm_code as custodycd, ");
    queryBuilder.append("   tmp_date.date as reportdate, ");
    queryBuilder.append(" round(nvl(bcbh.c_cash_balance,0) + nvl(bcbh.c_cash_day_in,0) - nvl(bcbh.c_cash_day_out,0),0) AS DerCashBal, ");
    queryBuilder.append(" round(nvl(bcbd.c_cash_balance,0) + nvl(bcbd.c_cash_day_in,0) - nvl(bcbd.c_cash_day_out,0),0) AS DerVsdCashBal, ");
    queryBuilder.append(" nvl(DerCashBal,0) + NVL(DerVsdCashBal,0) as DerBal ");
    queryBuilder.append(" from staging.stg_psbo_t_back_account ba ");
    queryBuilder.append(" left join staging.stg_psbo_t_back_cash_balance_history bcbh on ba.c_account_code = bcbh.c_account_code and c_transaction_date in (select lpickenddate from tmp_date) ");
    queryBuilder.append(" left join staging.stg_psbo_t_back_cash_balance_his_de bcbd on ba.c_account_code = bcbd.c_account_code and c_trading_date = c_transaction_date ");
    queryBuilder.append(" left join tmp_date on tmp_date.lpickenddate = bcbh.c_transaction_date ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" , ");

    queryBuilder.append(" tmp_bond as ( ");
    queryBuilder.append("   select custodycd, datereport as reportdate, ");
    queryBuilder.append(" round(sum(nvl(principal,0)),0) as bondbal ");
    queryBuilder.append(" from dwh.Dailyport_bondbal where datereport in (select date from tmp_date) ");
    queryBuilder.append(" group by custodycd, datereport ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" , ");
    queryBuilder.append(" tmp_fund as ( ");
    queryBuilder.append(" select custodycd, ");
    queryBuilder.append(" datereport as reportdate, ");
    queryBuilder.append(" round(sum(nvl(curramt,0)),0) as fundbal ");
    queryBuilder.append(" from dwh.dailyport_fundbal df ");
    queryBuilder.append(" where datereport in (select date from tmp_date) group by custodycd, datereport ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" , ");
    queryBuilder.append(" eow as ( ");
    queryBuilder.append("   select acc.custodycd, date as datereport, ");
    queryBuilder.append(" nvl(CashTotalBal,0) + nvl(iSaveBal,0) as CashGroup, ");
    queryBuilder.append(" nvl(StockBal,0) + nvl(IcopyCasBal,0) + nvl(Icopystockbal,0) + nvl(DerBal,0) as StockGroup, ");
    queryBuilder.append(" nvl(bondbal,0) as BondBal, ");
    queryBuilder.append(" nvl(fundbal,0) as FundBal, ");
    queryBuilder.append(" nvl(CashGroup,0) + nvl(StockGroup,0) + nvl(BondBal,0) + nvl(FundBal,0) as totalassets, ");
    queryBuilder.append(" nvl(CashGroup/nullif(totalassets,0),0) as cashgroup_prop, ");
    queryBuilder.append(" nvl(StockGroup/nullif(totalassets,0),0) as stockgroup_prop, ");
    queryBuilder.append(" nvl(BondBal/nullif(totalassets,0),0) as bondbal_prop, ");
    queryBuilder.append(" nvl(FundBal/nullif(totalassets,0),0) as fundbal_prop ");
    queryBuilder.append(" from (select distinct custodycd from staging.vw_flx_accounttype) acc ");
    queryBuilder.append(" left  join tmp_date d on 1=1 ");
    queryBuilder.append(" left join tmp_cash c on acc.custodycd = c.custodycd and c.reportdate = d.date ");
    queryBuilder.append(" left join tmp_isave i  on acc.custodycd = i.custodycd and i.reportdate = d.date ");
    queryBuilder.append(" left join tmp_stock s on acc.custodycd = s.custodycd and s.reportdate = d.date ");
    queryBuilder.append(" left join tmp_der der on acc.custodycd = der.custodycd and der.reportdate = d.date ");
    queryBuilder.append(" left join tmp_bond b on acc.custodycd  = b.custodycd and b.reportdate = d.date ");
    queryBuilder.append(" left join tmp_fund f on acc.custodycd  = f.custodycd and f.reportdate = d.date ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" select * from eow ");
    queryBuilder.append(" where custodycd in ");
    queryBuilder.append("   (select custodycd from ");
    queryBuilder.append("     (Select CustodyCD,SUM(TotalAssets) FROM eow GROUP BY CustodyCD HAVING SUM(TotalAssets)>0)) ");
    queryBuilder.append(" order by custodycd, datereport ");

    try {
      List<HashMap<String, Object>> resultList = Tcbsdwh.tcbsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("eomonth", eomonth)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}