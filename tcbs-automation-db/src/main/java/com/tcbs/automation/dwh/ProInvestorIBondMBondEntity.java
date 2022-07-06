package com.tcbs.automation.dwh;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ProInvestorIBondMBondEntity {
  private String custodyCd;
  private String tcbsId;
  @Id
  private String tradingCode;
  private String bondCode;
  private String agencyStatusDate;
  private String tradingDate;
  private Float totalFaceValue;
  private Integer principal;
  private String tradingChannel;
  private String bondType;
  private Integer isLinkedToMBond;

  @Step("Get total MBond of investor")
  public static List<Map<String, Object>> getMBondByCondition(String fromDate, String toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append("   select CustomerCustodyCD, CustomerTcbsId, id as tradingId, bondCode ");
    queryStringBuilder.append("     , tradingCode , cast(agencyStatusDate as date) agencyStatusDate, cast(TradingDate as date) TradingDate ");
    queryStringBuilder.append("     , totalFaceValue, principal, MarketStatus, isLinkedToMBond ");
    queryStringBuilder.append("   from Smy_dwh_tcb_Bond_Trading_Details ");
    queryStringBuilder.append("   where [Action] = 5 ");
    queryStringBuilder.append("   and AgencyStatusDate >= :fromDate ");
    queryStringBuilder.append("   and AgencyStatusDate < :toDate ");
    queryStringBuilder.append("   and AgencyStatus = 1 ");
    queryStringBuilder.append("   and MarketStatus = 8 and EtlCurDate = (select max(EtlCurDate) from Smy_dwh_tcb_Bond_Trading_Details) ");

    try {
      List<Map<String, Object>> result = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return result;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get total iBond of investor")
  public static List<Map<String, Object>> getIBondByCondition(String fromDate, String toDate, Integer dayInterval) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("   select ib.CustomerCustodyCd as custodyCd, ib.CustomerTcbsId as tcbsId, id as tradingId, ib.BondCode ");
    queryStringBuilder.append("     , ib.tradingCode, cast(ib.agencyStatusDate as date) agencyStatusDate, cast(ib.tradingDate as date) tradingDate ");
    queryStringBuilder.append("     , ib.totalFaceValue, ib.principal, MarketStatus, ib.isLinkedToMBond ");
    queryStringBuilder.append("   from Smy_dwh_tcb_Bond_Trading_Details ib ");
    queryStringBuilder.append("   left join (	select 	CustomerCustodyCD, CustomerTcbsId, id as tradingId, BondCode ");
    queryStringBuilder.append("   , tradingCode, cast(agencyStatusDate as date) agencyStatusDate, cast(TradingDate as date) TradingDate ");
    queryStringBuilder.append("     , totalFaceValue, principal ");
    queryStringBuilder.append("   from Smy_dwh_tcb_Bond_Trading_Details ");
    queryStringBuilder.append("   where [Action] = 5 ");
    queryStringBuilder.append("   and AgencyStatusDate >= :fromDate ");
    queryStringBuilder.append("   and AgencyStatusDate < :toDate ");
    queryStringBuilder.append("   and AgencyStatus = 1 ");
    queryStringBuilder.append("   and MarketStatus = 8 and EtlCurDate = (select max(EtlCurDate) from Smy_dwh_tcb_Bond_Trading_Details)) mb on mb.CustomerCustodyCD = ib.CustomerCustodyCD ");
    queryStringBuilder.append("   and ib.tradingDate between mb.tradingDate and dbo.businessDaysAdd(:dayInterval,mb.tradingDate) ");
    queryStringBuilder.append("   where ib.[Action] = 5 ");
    queryStringBuilder.append("   and ib.AgencyStatus = 1 and MarketStatus <> 8 ");
    queryStringBuilder.append("   and mb.CustomerCustodyCD is not null and EtlCurDate = (select max(EtlCurDate) from Smy_dwh_tcb_Bond_Trading_Details) ");


    try {
      List<Map<String, Object>> result = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setParameter("dayInterval", dayInterval)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return result;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
