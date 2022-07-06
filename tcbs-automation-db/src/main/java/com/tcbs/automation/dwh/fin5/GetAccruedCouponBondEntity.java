package com.tcbs.automation.dwh.fin5;

import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GetAccruedCouponBondEntity {

  @Step("Get data from database")
  public static List<HashMap<String, Object>> getAccruedCouponBond(String bondCode, String fromDate, String toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT underlying_code, CAST(accountant_time AS date) AS accountant_time , book_interest_amount , close_accrued_extra_perday ")
      .append(" , close_accrued_discount_perday, buy_amount, buy_quantity, product_par ")
      .append(" , book_interest_amount - close_accrued_extra_perday + close_accrued_discount_perday + isnull(buy_amount,0) - isnull(buy_quantity,0)*product_par AS accrued_coupon ")
      .append(" FROM staging.stg_tca_act_daily_portfolio ")
      .append(" WHERE (accountant_time >= :fromDate and accountant_time <= :toDate ) ");
    if (!bondCode.equals("") && !bondCode.equals("NULL")) {
      queryStringBuilder.append(" AND underlying_code in :bondCode ");
    }
    queryStringBuilder.append(" ORDER BY accountant_time, underlying_code ASC ");
    try {
      if (bondCode.equals("") || bondCode.equals("NULL")) {
        return Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
          .setParameter("fromDate", fromDate)
          .setParameter("toDate", toDate)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();
      } else {
        return Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
          .setParameter("fromDate", fromDate)
          .setParameter("toDate", toDate)
          .setParameter("bondCode", Arrays.asList(bondCode.split(",")))
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data from database")
  public static Boolean checkEtlDateTime() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT case when max(etlcurdate) <> to_char(CAST(getdate() -1 AS date), 'yyyyMMdd') then 'true' else 'false' end as check FROM staging.stg_tca_act_daily_portfolio");

    try {
      List<HashMap<String, Object>> rs = Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return Boolean.valueOf(String.valueOf(rs.get(0).get("check")));
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return Boolean.FALSE;
  }

}
