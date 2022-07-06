package com.tcbs.automation.dwh.baymax;

import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetRightsInternalEntity {

  @Step("Get Data")
  public static List<HashMap<String, Object>> getRightsFromDB(String accountNo, String symbol, String type, String fromDate, String toDate) {
    StringBuilder query = new StringBuilder();
    query.append(" select 'rights' as \"object\", id, eventid, symbol, \"type\", typename, reportdate, fromdatetransfer, todatetransfer, duedate, actiondate, begindate ")
      .append(", exdate, ratio, stockdividend, cashdividend, rightsatreportdate, status, quantity, registerquantity ")
      .append(", remainquantity, price, basicprice, isincode, description, tosymbol  ")
      .append("from staging.stg_flx_vw_api_rights ")
      .append("where accountno = :p_account ")
      .append("and reportdate BETWEEN cast(:p_fromDate as timestamp) and cast(:p_toDate as timestamp) ")
      .append("and ((:p_type = 'null') or (:p_type != 'null' and \"type\" = :p_type)) ")
      .append("and (( :p_symbol= 'null'  ) or (:p_symbol != 'null' and symbol =:p_symbol)) ");
    try {
      return Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("p_type", type)
        .setParameter("p_symbol", symbol)
        .setParameter("p_account", accountNo)
        .setParameter("p_fromDate", fromDate)
        .setParameter("p_toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();

  }
}
