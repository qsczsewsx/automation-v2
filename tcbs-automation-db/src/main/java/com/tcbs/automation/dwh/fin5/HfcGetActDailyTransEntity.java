package com.tcbs.automation.dwh.fin5;

import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HfcGetActDailyTransEntity {

  @Step("Get data")
  public static List<HashMap<String, Object>> getActTransInfo(String fromDate, String toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" select accountant_time as accountanttime, underlying_code as underlyingcode, b.Price par, open_bl_amount as openblamount ")
      .append(" , sell_price as sellprice, buy_principal as buyprincipal, open_bl_quantity as openblquantity, sell_quantity as sellquantity ")
      .append(" , buy_quantity as buyquantity, total_profit as totalprofit, total_loss as totalloss, accrued_interest as accruedinterest ")
      .append(" , close_unit_price as closeUnitPrice, total_invest as totalInvest, delta_accrued_interest as deltaAccruedInterest ")
      .append(" , open_total_accrued as openTotalAccrued, close_total_accrued as closeTotalAccrued ")
      .append(" , net_coupon_this_month + delta_accrued_interest + minus_delta_accrued_interest AS accrued_coupon ")
      .append(" from staging.stg_tca_act_daily_transaction adt left join staging.stg_tcb_bond b on adt.UNDERLYING_CODE = b.Code ")
      .append(" where accountanttime >= :fromDate and accountanttime <= :toDate order by accountanttime, underlyingcode, openblquantity, accruedinterest asc ");
    try {
      return Tcbsdwh.tcbsDwhDbConnection.getSession()
        .createNativeQuery(queryStringBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data")
  public static Boolean checkEtlDate() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select case when max(etlcurdate) <> to_char(cast(getdate() - 1 as date), 'yyyyMMdd') then 'true' else 'false' end as check from staging.stg_tca_act_daily_transaction ");
    try {
      List<HashMap<String, Object>> rs = Tcbsdwh.tcbsDwhDbConnection.getSession()
        .createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return Boolean.valueOf(String.valueOf(rs.get(0).get("check")));
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return Boolean.FALSE;
  }

}
