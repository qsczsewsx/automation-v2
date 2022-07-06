package com.tcbs.automation.dwh;

import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StockTransactionMicroInvestingEntity {
  public static List<HashMap<String, Object>> getByCondition(String fromDate, String toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append(" select ast.* , cas.tcbsId, mi.MI_STATUS as MIStatus ");
    queryStringBuilder.append(" from ( 	select ast.ID as transId ,BusDate as matchedDate ,custodyCD, Symbol, quantity, BuyAmt ");
    queryStringBuilder.append("         from smy_dwh_flx_allstocktxn ast ");
    queryStringBuilder.append("         where SecType = 'Stock' ");
    queryStringBuilder.append("         and EtlCurDate = (select max(EtlCurDate) from smy_dwh_flx_allstocktxn) ");
    queryStringBuilder.append(" and BusDate >= :fromDate ");
    queryStringBuilder.append(" and BusDate <  dateadd(day,1,:toDate) ");
    queryStringBuilder.append(" and Field = 'Mua') ast ");
    queryStringBuilder.append(" left join ( select USER_NAME, MI_STATUS ");
    queryStringBuilder.append(" from Stg_tcasset_ACCOUNT ");
    queryStringBuilder.append(" where MI_STATUS IS NOT NULL ");
    queryStringBuilder.append(" and EtlCurDate = (select max(EtlCurDate) from Stg_tcasset_ACCOUNT) ) mi on mi.[USER_NAME] = ast.CustodyCD ");
    queryStringBuilder.append(" left join Smy_dwh_cas_AllUserView cas on cas.CUSTODYCD = ast.CustodyCD and cas.EtlCurDate = (select max(EtlCurDate) from Smy_dwh_cas_AllUserView) ");
    queryStringBuilder.append(" where MI_STATUS = 1 order by ast.transId asc ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
