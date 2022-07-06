package com.tcbs.automation.dwh;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IWPTransactionAmountEntity {

  @Step("Get transaction amount of Iwp")
  public static HashMap<String, Object> getFundBondIwpTrans(String tcbsId, String fromDate, String toDate,
                                                            String type) {
    StringBuilder queryBuilder = new StringBuilder();
    if (type.equalsIgnoreCase("bond")) {
      queryBuilder.append("  select iwp.tcbs_id, sum(IIF(bd.[Action] = 5, bd.principal, 0)) totalBondIwpBuy,  ");
      queryBuilder.append("  sum(IIF(bd.[Action] = 7, bd.principal, 0)) totalBondIwpSell , count(bd.[Action]) totalTransIwpBond ");
      queryBuilder.append("  from ( select iwp.tcbs_id, iwp.status, iwp.start_date ");
      queryBuilder.append("         from ( select iwpTemp.tcbs_id, iwpTemp.status, iwpTemp.start_date, ");
      queryBuilder.append("                   ROW_NUMBER() OVER(PARTITION BY tcbs_id ORDER BY start_date DESC) rn ");
      queryBuilder.append("               from Stg_ipartner_wp iwpTemp ");
      queryBuilder.append("               where iwpTemp.tcbs_id = :tcbsId) iwp ");
      queryBuilder.append("           where rn = 1) iwp ");
      queryBuilder.append("  left join [tcbs-dwh].dbo.vw_Bond_Trading_Details bd on iwp.tcbs_id = bd.CustomerTcbsId ");
      queryBuilder.append("  where bd.TradingDate between :fromDate and :toDate ");
      queryBuilder.append("  and bd.TradingDate >= iwp.start_date ");
      queryBuilder.append("  and bd.MatchingDate is not null ");
      queryBuilder.append("  group by iwp.tcbs_id ");
    } else if (type.equalsIgnoreCase("fund")) {
      queryBuilder.append("  select iwp.tcbs_id, sum(IIF(fd.ACTION_ID = 1,fd.TRANSACTION_VALUE,0)) totalFundIwpBuy,  ");
      queryBuilder.append("  sum(IIF(fd.ACTION_ID = 2,fd.TRANSACTION_VALUE,0)) totalFundIwpSell , count(fd.ACTION_ID) totalTransIwpFund ");
      queryBuilder.append("  from ( select iwp.tcbs_id, iwp.status, iwp.start_date ");
      queryBuilder.append("         from ( select iwpTemp.tcbs_id, iwpTemp.status, iwpTemp.start_date, ");
      queryBuilder.append("                   ROW_NUMBER() OVER(PARTITION BY tcbs_id ORDER BY start_date DESC) rn ");
      queryBuilder.append("               from Stg_ipartner_wp iwpTemp ");
      queryBuilder.append("               where iwpTemp.tcbs_id = :tcbsId) iwp ");
      queryBuilder.append("           where rn = 1) iwp ");
      queryBuilder.append("  left join [tcbs-dwh].dbo.smy_dwh_tci_fund_trading_details fd on iwp.tcbs_id = fd.ACCOUNT_ID ");
      queryBuilder.append("  where EtlCurDate = (select max(etlcurdate) from [tcbs-dwh].dbo.smy_dwh_tci_fund_trading_details) and fd.STATUS = 'MATCHED' ");
      queryBuilder.append("  and fd.MATCHED_TIMESTAMP between :fromDate and :toDate ");
      queryBuilder.append("  and fd.MATCHED_TIMESTAMP >= iwp.start_date ");
      queryBuilder.append("  group by iwp.tcbs_id ");
    }

    try {
      List<HashMap<String, Object>> result = Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("tcbsId", tcbsId)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      if (result.size() > 0) {
        return result.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  @Step("Get transaction amount of Sub")
  public static HashMap<String, Object> getFundBondSubTrans(String tcbsId, String fromDate, String toDate,
                                                            String type) {
    StringBuilder queryBuilder = new StringBuilder();
    if (type.equalsIgnoreCase("bond")) {
      queryBuilder.append("  select sub.partner_id, sum(IIF(bd.[Action] = 5, bd.principal, 0)) totalBondBuy,  ");
      queryBuilder.append("  sum(IIF(bd.[Action] = 7, bd.principal, 0)) totalBondSell , count(bd.[Action]) totalTransBond ");
      queryBuilder.append("  from (	select partner_id, subscriber_id, wp.created_date, ineffective_date, uv.CUSTODYCD as subCustodyCd ");
      queryBuilder.append("           from [tcbs-dwh].dbo.Stg_ipartner_wp_relation wp left join [tcbs-dwh].dbo.smy_dwh_cas_alluserview uv on wp.subscriber_id = uv.TCBSID ");
      queryBuilder.append("           where uv.EtlCurDate = (select max(etlcurdate) from [tcbs-dwh].dbo.smy_dwh_cas_alluserview) ");
      queryBuilder.append("           and wp.status in ('ACTIVE','PENDING') ");
      queryBuilder.append("           and partner_id = :tcbsId) sub ");
      queryBuilder.append("  left join [tcbs-dwh].dbo.vw_Bond_Trading_Details bd on sub.subscriber_id = bd.CustomerTcbsId ");
      queryBuilder.append("  where bd.TradingDate >= sub.created_date and ((bd.TradingDate <= sub.ineffective_date) or (sub.ineffective_date is null)) ");
      queryBuilder.append("  and bd.TradingDate between :fromDate and :toDate ");
      queryBuilder.append("  and bd.MatchingDate is not null ");
      queryBuilder.append("  group by sub.partner_id ");
    } else if (type.equalsIgnoreCase("fund")) {
      queryBuilder.append("  select partner_id, sum(IIF(fd.ACTION_ID = 1,fd.TRANSACTION_VALUE,0)) totalFundBuy,  ");
      queryBuilder.append("  sum(IIF(fd.ACTION_ID = 2,fd.TRANSACTION_VALUE,0)) totalFundSell , count(fd.ACTION_ID) totalTransFund ");
      queryBuilder.append("  from (	select partner_id, subscriber_id, wp.created_date, ineffective_date, uv.CUSTODYCD as subCustodyCd ");
      queryBuilder.append("           from [tcbs-dwh].dbo.Stg_ipartner_wp_relation wp ");
      queryBuilder.append("           left join [tcbs-dwh].dbo.smy_dwh_cas_alluserview uv on wp.subscriber_id = uv.TCBSID ");
      queryBuilder.append("           where uv.EtlCurDate = (select max(etlcurdate) from [tcbs-dwh].dbo.smy_dwh_cas_alluserview) ");
      queryBuilder.append("           and wp.status in ('ACTIVE','PENDING') and partner_id = :tcbsId ) sub ");
      queryBuilder.append("  left join [tcbs-dwh].dbo.smy_dwh_tci_fund_trading_details fd on sub.subscriber_id = fd.ACCOUNT_ID ");
      queryBuilder.append("  where EtlCurDate = (select max(etlcurdate) from [tcbs-dwh].dbo.smy_dwh_tci_fund_trading_details) and STATUS = 'MATCHED' ");
      queryBuilder.append("  and fd.MATCHED_TIMESTAMP >= sub.created_date and ((fd.MATCHED_TIMESTAMP <= sub.ineffective_date) or (sub.ineffective_date is null)) ");
      queryBuilder.append("  and fd.MATCHED_TIMESTAMP between :fromDate and :toDate ");
      queryBuilder.append("  group by partner_id ");
    }

    try {
      List<HashMap<String, Object>> result =
        Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
          .setParameter("tcbsId", tcbsId)
          .setParameter("fromDate", fromDate)
          .setParameter("toDate", toDate)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();
      if (result.size() > 0) {
        return result.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  @Step("Get list sub")
  public static List<HashMap<String, Object>> getSubIWealthPartner(List<String> tcbsIds) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("   select partner_id, COUNT(subscriber_id) as sub ");
    queryBuilder.append("   from [tcbs-dwh].dbo.Stg_ipartner_wp_relation wp ");
    queryBuilder.append("   left join [tcbs-dwh].dbo.smy_dwh_cas_alluserview uv on wp.subscriber_id = uv.TCBSID ");
    queryBuilder.append("   where uv.EtlCurDate = (select max(etlcurdate) from [tcbs-dwh].dbo.smy_dwh_cas_alluserview) ");
    queryBuilder.append("   and wp.status in ('ACTIVE','PENDING') ");
    queryBuilder.append("   and partner_id in :tcbsIds GROUP by partner_id ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("tcbsIds", tcbsIds)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get list iwp")
  public static List<HashMap<String, Object>> getIWealthPartners(List<String> tcbsIds) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("   select iwp.tcbs_id, iwp.status, iwp.start_date ");
    queryBuilder.append("   from ( ");
    queryBuilder.append("   SELECT  iwpTemp.tcbs_id, iwpTemp.status, iwpTemp.start_date, ");
    queryBuilder.append("   ROW_NUMBER() OVER(PARTITION BY tcbs_id ORDER BY start_date DESC) rn ");
    queryBuilder.append("   FROM Stg_ipartner_wp iwpTemp ");
    queryBuilder.append("   where iwpTemp.tcbs_id in :tcbsIds) iwp ");
    queryBuilder.append("   WHERE rn = 1 ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("tcbsIds", tcbsIds)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
