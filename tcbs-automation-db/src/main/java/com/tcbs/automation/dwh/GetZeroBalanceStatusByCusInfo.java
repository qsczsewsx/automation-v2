package com.tcbs.automation.dwh;

import lombok.Data;
import net.thucydides.core.steps.StepEventBus;

import java.util.ArrayList;
import java.util.List;

@Data
public class GetZeroBalanceStatusByCusInfo {
  public static List<Object> getByCustodyCd(String custodyCd, String fromDate, String toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" select distinct a.custodycd from (  ");
    queryStringBuilder.append("   select custodycd from vw_DP_FundBal where custodycd = :custodyCd and cast(datereport as date) between :fromDate and :toDate  ");
    queryStringBuilder.append(" union all  ");
    queryStringBuilder.append(" select custodycd from vw_DP_BondBal where custodycd = :custodyCd and cast(datereport as date) between :fromDate and :toDate) a  ");
    queryStringBuilder.append(" where a.custodycd = ( select distinct b.CustodyCD from (  ");
    queryStringBuilder.append("   select  CustomerCustodyCD as CustodyCD from Smy_dwh_tcb_Bond_Trading_details  ");
    queryStringBuilder.append(" where WaitingStatus = 3 and AccountantStatus = 1 and EtlCurDate = (select max(EtlCurDate) from Smy_dwh_tcb_Bond_Trading_details)  ");
    queryStringBuilder.append(" and CustomerCustodyCD = :custodyCd  ");
    queryStringBuilder.append(" union all  ");
    queryStringBuilder.append(" select  CustodyCD from Smy_dwh_tci_Fund_Trading_Details where STATUS = 'MATCHED' and EtlCurDate = (select max(EtlCurDate) from Smy_dwh_tci_Fund_Trading_Details)  ");
    queryStringBuilder.append(" and CustodyCD = :custodyCd ) b )  ");

    try {
      List<Object> result = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("custodyCd", custodyCd)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .getResultList();

      return result;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static boolean checkByCustodyCd(String custodyCd) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select custodycd from smy_dwh_cas_alluserview where EtlCurDate = (select max(EtlCurDate) from smy_dwh_cas_alluserview) and tcbsid = :tcbsid and status = 'active';");

    try {
      List<Object> rs = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("tcbsid", custodyCd)
        .getResultList();
      return rs.isEmpty();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return false;
  }

}
