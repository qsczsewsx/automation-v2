package com.tcbs.automation.dwh.businessfinance;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetActTransEntity {

  @Step("Get Data")
  public static List<HashMap<String, Object>> getActTransInfo(String fromDate, String toDate){
    StringBuilder query = new StringBuilder();
    query.append(" select TRADING_ID, SALE_TYPE, trading_code as tradingcode, quantity, accountant_time as accountanttime, cast(trading_date as date) as tradingdate ");
    query.append(" , underlying_code as underlyingcode ");
    query.append(" , case\twhen SALE_TYPE = 'RETAILS' then case when custody_code in ('0001011809','TR96877551') then  action ");
    query.append(" when counterparty_code in ('0001011809','TR96877551') then 12 - action else action end ");
    query.append(" when SALE_TYPE = 'WHOLESALES' then case\twhen custody_code in ('0001011809','TR96877551') then  action ");
    query.append(" when counterparty_code in ('0001011809','TR96877551') then 3 - action else action end end as [action] ");
    query.append(" , price, principal, cost_good as costgood, pnl, pnl_type as pnltype, accrued_interest as accruedinterest, agency_dept as agencyDept ");
    query.append(" from Stg_tca_ACT_TRANSACTION a ");
    query.append(" where accountant_time >= :fromDate and accountant_time <= :toDate ");
    query.append(" and act_type in ('1AC1AG','1AC2AG','1AG','1AG1AC') order by tradingcode ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
