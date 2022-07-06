package com.tcbs.automation.dwh.businessfinance;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetMarginLendingEntity {

  @Step("Get Data")
  public static List<HashMap<String, Object>> getMarginLending(String reportDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM (select  'MARGIN' as TYPE, case when vip.CustodyCD is not NULL then 'VIP' else 'MASS' end as CustomerType ");
    queryBuilder.append(
      " ,  CONVERT(varchar, DATEREPORT,23) as DATEREPORT, m.CUSTODYCD, LNSCHDID as Ma_khe_uoc, convert(varchar, RLSDATE, 23) as Ngay_giai_ngan, RLSPRIN as So_tien_giai_ngan, LNPRIN as So_tien_Outstanding, convert(varchar, OVERDUEDATE, 23) as Ngay_het_han ");
    queryBuilder.append(" from DailyPort_MarginBal m ");
    queryBuilder.append(" left join off_iw_CustodyCDVIP vip on m.CUSTODYCD = vip.CustodyCD where DATEREPORT = :reportDate ");
    queryBuilder.append(" union all ");
    queryBuilder.append(" select 'UTTB' as TYPE,  case when vip.CustodyCD is not NULL then 'VIP' else 'MASS' end as CustomerType ");
    queryBuilder.append(" , DATE_TRANS, u.CUSTODYCD, NULL, NULL, AMT, NULL, NULL ");
    queryBuilder.append(" from DailyPort_UTTB u left join off_iw_CustodyCDVIP vip on u.CUSTODYCD = vip.CustodyCD where DATE_TRANS = :reportDate ) a order by type, CustomerType, CustodyCD");
    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("reportDate", reportDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
