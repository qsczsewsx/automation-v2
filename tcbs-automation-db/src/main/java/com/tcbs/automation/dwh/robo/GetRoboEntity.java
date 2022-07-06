package com.tcbs.automation.dwh.robo;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetRoboEntity {

  @Step("Get data")
  public static HashMap<String, Object> getSuggestRateProc(String tradingCodeList, String voucherDiscount, String priceType) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("EXEC [dbo].[getSuggestRate_robo] :tradingCodeList, :voucherDiscount, :priceType ");

    try {
      List<HashMap<String, Object>> rs = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("tradingCodeList", tradingCodeList)
        .setParameter("voucherDiscount", voucherDiscount)
        .setParameter("priceType", priceType)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

      if (!rs.isEmpty()) {
        return rs.get(0);
      }
    } catch (Exception ex) {
//      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
      HashMap<String, Object> rs = new HashMap<>();
      rs.put("message", "Data is not up to date");
      return rs;
    }
    return new HashMap<>();
  }

  @Step("Get data")
  public static List<HashMap<String, Object>> getBuyBackIcnProc() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("EXEC getbuyBackICN_robo2 ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
