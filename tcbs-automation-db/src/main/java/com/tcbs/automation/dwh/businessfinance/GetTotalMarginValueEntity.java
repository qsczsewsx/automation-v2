package com.tcbs.automation.dwh.businessfinance;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetTotalMarginValueEntity {

  @Step("Get margin value")
  public static List<HashMap<String, Object>> getMarginValue(String from, String to) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" select 'Margin' as product, FROM_DATE as reportDate , case when v.custodycd is not NULL then 'VIP' else 'MASS' end as cusType ");
    queryBuilder.append(" , sum(case when RLSDATE = FROM_DATE then INTBAL else 0 end) as inValue ");
    queryBuilder.append(" , sum(INTBAL) as marginOutstanding, sum(INTAMT) as interestAmount ");
    queryBuilder.append(" from Stg_flx_VW_DAILY_MARGIN_BYCUS m ");
    queryBuilder.append(" left join off_iw_custodycdvip v on m.CustodyCD = v.custodycd ");
    queryBuilder.append(" where FROM_DATE between :from and :to ");
    queryBuilder.append(" group by FROM_DATE, ( case when v.custodycd is not NULL then 'VIP' else 'MASS' end) ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("from", from)
        .setParameter("to", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get out value info")
  public static List<HashMap<String, Object>> getOutValue(String from, String to) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select TXDATE, case when v.custodycd is not NULL then 'VIP' else 'MASS' end as CusType,sum(ORGPAIDAMT) as OutValue ");
    queryBuilder.append(" from Stg_flx_VW_DEBT_PAY_BYCUS d left join off_iw_custodycdvip v on d.CustodyCD = v.custodycd ");
    queryBuilder.append(" where TXDATE between :from and :to group by TXDATE, ( case when v.custodycd is not NULL then 'VIP' else 'MASS' end) ");
    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("from", from)
        .setParameter("to", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
