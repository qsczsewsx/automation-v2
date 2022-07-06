package com.tcbs.automation.dwh.businessfinance;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetActOpexTransEntity {

  @Step("Get Data")
  public static List<HashMap<String, Object>> getActOpexTrans(String from, String to, List<String> company){
    StringBuilder query = new StringBuilder();
    query.append(" select *, concat(description,deptCode) as uKey from ");
    query.append(" (select fiscalYear,cast(docDate as date) as docDate,description,account,cast(debitAmount as bigint) as debitAmount, ");
    query.append(" cast(creditAmount as bigint) as creditAmount,crspAccount,deptCode,expenseCatgCode,account1,firm ");
    query.append(" from smy_dwh_brv_GeneralLedger ");
    query.append(" where etlcurdate = (select max(etlcurdate) from smy_dwh_brv_GeneralLedger) ");
    query.append(" and firm like '%TCS%' and account not like '91%' and (crspaccount like '64%' or crspaccount like '811%' or crspaccount like '711%') ");
    query.append(" union ");
    query.append(" select fiscalYear,cast(docDate as date) as docDate,description,account,cast(debitAmount as bigint) as debitAmount, ");
    query.append(" cast(creditAmount as bigint) as creditAmount,crspAccount,deptCode,expenseCatgCode,account1,firm ");
    query.append(" from smy_dwh_brv_GeneralLedger ");
    query.append(" where etlcurdate = (select max(etlcurdate) from smy_dwh_brv_GeneralLedger) ");
    query.append(" and firm like '%TCC%' and account not like '91%' and (crspaccount like '64%' or crspaccount like '811%' or crspaccount like '711%')) gl ");
    query.append(" where docdate >= :from and docdate <= :to and Firm in ( :company ) ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("from", from)
        .setParameter("to", to)
        .setParameter("company", company)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();

  }
}
