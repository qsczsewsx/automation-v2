package com.tcbs.automation.dwh.focus;

import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetDataCouponOtcEntity {

  @Step("get data")
  public static List<HashMap<String, Object>> getDataCouponOtc(String custody){
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(" select custodycode , bondcode as bondCode, cfidate  as cpPayDate, sum(round(cfi,0)) as cpVal from dwh.smy_dwh_tcb_bondcontractcfi ")
      .append(" where \"Tag\" = 'Coupon Payment' and exdate <= cast(getdate() as date) and cfidate >= cast(getdate() as date) ")
      .append(" and custodycode  = :custody ")
      .append(" group by custodycode , bondcode , cfidate ");
    try {
      return Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(stringBuilder.toString())
        .setParameter("custody", custody)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();

  }

}
