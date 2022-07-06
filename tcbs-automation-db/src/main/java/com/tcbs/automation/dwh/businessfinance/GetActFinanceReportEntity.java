package com.tcbs.automation.dwh.businessfinance;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetActFinanceReportEntity {

  public static List<HashMap<String, Object>> getActFinanceReport(String from, String to, List<String> company) {
    StringBuilder query = new StringBuilder();
    query.append(" select firm,cast(docdate as date) as date,null as totalAsset,null as totalLiabilities,null as equity ");
    query.append(
      " ,cast((SUM(case when account not like '91%' and crspaccount like '5%' then DebitAmount else 0 end) - SUM(case when account not like '91%' and crspaccount like '5%' then CreditAmount else 0 end)) as bigint) as toi ");
    query.append(
      ",cast((SUM(case when account not like '91%' and (crspaccount like '64%' or crspaccount like '811%' or crspaccount like '711%') then DebitAmount else 0 end) - SUM(case when account not like '91%' and (crspaccount like '64%' or crspaccount like '811%' or crspaccount like '711%') then CreditAmount else 0 end)) as bigint) as opex ");
    query.append(" from smy_dwh_brv_GeneralLedger ");
    query.append(" where etlcurdate = (select max(etlcurdate) from smy_dwh_brv_GeneralLedger) ");
    query.append(" and docdate >= :from and docdate <= :to and Firm in ( :company ) ");
    query.append(" group by firm, docDate ");
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
