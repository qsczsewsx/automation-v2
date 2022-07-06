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
import java.util.HashMap;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TotalTransactionValueEntity {
  @Id
  String tcbsId;
  String custodyCd;
  Double tradingValue;
  String productCategory;
  String assetType;

  @Step("Get total transaction value of customer")
  public static List<HashMap<String, Object>> getTotalTransByCondition(String fromDate, String toDate, List<String> custodyCdList, List<String> bondProductList,
                                                                       List<String> fundProductList, String isIWealthPartner, List<String> marketStatus) {
    StringBuilder query = new StringBuilder();

    query.append(" select CustomerTcbsId as CustomerTcbsId, CustomerCustodyCD as CustodyCd, sum(Principal) as TradingValue, BondCategory as ProductCategory, 'Bond' as AssetType ");
    query.append(" from Smy_dwh_tcb_Bond_Trading_Details bt ");
    query.append(" where EtlCurDate = (Select max(etlCurDate) from Smy_dwh_tcb_Bond_Trading_Details) ");
    query.append(" and AgencyStatusDate >= :fromDate ");
    query.append(" and AgencyStatusDate < :toDate ");
    query.append(" and [Action] = 5 ");
    query.append(" and AgencyStatus = 1 ");
    query.append(" and MarketStatus in :marketStatus and BondCategory in :bondProductList ");
    query.append(" and CustomerCustodyCD in :custodyCdList ");
    query.append(" group by CustomerTcbsId, CustomerCustodyCD, BondCategory ");
    query.append(" UNION ALL ");
    query.append(" select ACCOUNT_ID as CustomerTcbsId, CustodyCd, sum(TRANSACTION_VALUE) as TradingValue, PRODUCT_CODE as ProductCategory, 'Fund' as AssetType ");
    query.append(" from Smy_dwh_tci_Fund_Trading_Details ");
    query.append(" where EtlCurDate = (Select max(etlCurDate) from Smy_dwh_tci_Fund_Trading_Details) ");
    query.append(" and TRADING_TIMESTAMP >= :fromDate ");
    query.append(" and TRADING_TIMESTAMP < :toDate ");
    query.append(" and ACTION_ID = 1 ");
    query.append(" and [STATUS] = 'MATCHED' ");
    query.append(" and PRODUCT_CODE in :fundProductList ");
    query.append(" and CustodyCd in :custodyCdList ");
    query.append(" group by ACCOUNT_ID, CustodyCd, PRODUCT_CODE ");
    query.append(" UNION ALL ");
    query.append(" select TCBSID as CustomerTcbsId, CustodyCd, sum(TotalAmt) as TradingValue, 'Stock' as ProductCategory, 'Stock' as AssetType ");
    query.append(" from Prc_Stock_TxnValueByDate ");
    query.append(" where BusDate >= :fromDate ");
    query.append(" and BusDate < :toDate ");
    query.append(" and isIWP = :isIWealthPartner ");
    query.append(" and CustodyCd in :custodyCdList ");
    query.append(" group by TCBSID, CustodyCD ");
    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("custodyCdList", custodyCdList)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setParameter("fundProductList", fundProductList)
        .setParameter("bondProductList", bondProductList)
        .setParameter("isIWealthPartner", isIWealthPartner)
        .setParameter("marketStatus", marketStatus)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
