package com.tcbs.automation.stockmarket.bondorion;

import com.tcbs.automation.stockmarket.Stockmarket;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetAllListedBondEntity {

  @Step("get data")
  public static List<HashMap<String, Object>> getDataFromDb(String floor, String from, String to, List<String> bondCodes) {
    StringBuilder stringBuilder = new StringBuilder();
    StringBuilder queryBuilder = new StringBuilder();

    switch (floor) {
      case "HOSE":
        stringBuilder.append(" SELECT bondCode, DATE_FORMAT(TradingDate, '%Y-%m-%d') as tradingDate, SUM(Volume) as volume, SUM(Value) as value FROM TCS_HOSE_Negotiate_Trading ")
          .append(" WHERE DATE_FORMAT(TradingDate, '%Y%m%d') BETWEEN '")
          .append(from).append("' AND '").append(to).append("' ").append(" AND BondCode in ( :p_bond_code ) GROUP BY BondCode, DATE_FORMAT(TradingDate, '%Y-%m-%d') ");
        queryBuilder.append(" SELECT bondCode, DATE_FORMAT(TradingDate, '%Y-%m-%d') as tradingDate, SUM(Volume) as volume, SUM(Value) as value FROM TCS_HOSE_Negotiate_Trading ")
          .append(" WHERE DATE_FORMAT(TradingDate, '%Y%m%d') BETWEEN '")
          .append(from).append("' AND '").append(to).append("' ").append(" GROUP BY BondCode, DATE_FORMAT(TradingDate, '%Y-%m-%d') ");
        break;
      case "HNX":
        stringBuilder.append(" SELECT bondCode, DATE_FORMAT(TradingDate, '%Y-%m-%d') as tradingDate, SUM(Volume) as volume, SUM(Value) as value FROM TCS_HNX_Negotiate_Trading ")
          .append(" WHERE DATE_FORMAT(TradingDate, '%Y%m%d') BETWEEN '")
          .append(from).append("' AND '").append(to).append("' ").append(" AND BondCode in ( :p_bond_code ) GROUP BY BondCode, DATE_FORMAT(TradingDate, '%Y-%m-%d') ");
        queryBuilder.append(" SELECT bondCode, DATE_FORMAT(TradingDate, '%Y-%m-%d') as tradingDate, SUM(Volume) as volume, SUM(Value) as value FROM TCS_HNX_Negotiate_Trading ")
          .append(" WHERE DATE_FORMAT(TradingDate, '%Y%m%d') BETWEEN '")
          .append(from).append("' AND '").append(to).append("' ").append(" GROUP BY BondCode, DATE_FORMAT(TradingDate, '%Y-%m-%d') ");
      default:
        break;
    }

    try {
      if ((bondCodes.get(0).isEmpty() || bondCodes.get(0).equals("NULL"))) {
        return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryBuilder.toString())
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();
      } else {
        return Stockmarket.stockMarketConnection.getSession().createNativeQuery(stringBuilder.toString())
          .setParameter("p_bond_code", bondCodes)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();
      }

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
