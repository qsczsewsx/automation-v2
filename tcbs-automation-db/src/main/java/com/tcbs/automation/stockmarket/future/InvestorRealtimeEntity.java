package com.tcbs.automation.stockmarket.future;

import com.tcbs.automation.stockmarket.FuturesMarket;
import com.tcbs.automation.stockmarket.Stockmarket;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InvestorRealtimeEntity {

  @Step("get derivative buy trans from db")
  public static List<HashMap<String, Object>> getDerivativeBuyTrans(String ticker) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT FROM_UNIXTIME(seq_time), ticker , seq_time, p, v, cp, FORMAT(cast(rcp as DECIMAL), 1) as rcp, a, ba, sa, hl, pcp ");
    queryStringBuilder.append("FROM ( ");
    queryStringBuilder.append("SELECT ticker , seq_time , close_price as p , close_vol  as v,  ");
    queryStringBuilder.append("IF(close_vol > 100, 'true', 'false') as hl, `action` as a, change_price as cp, change_price/ref_price as rcp, ");
    queryStringBuilder.append("if(@isFirst = 0, 0, close_price - @preP) as pcp, ");
    queryStringBuilder.append("@ba \\:= IF(`action` = 'BU', @ba + close_vol, @ba) as ba, ");
    queryStringBuilder.append("@sa \\:= IF(`action` = 'SD', @sa + close_vol, @sa) as sa, ");
    queryStringBuilder.append("@preP \\:= close_price, @isFirst \\:= 1 ");
    queryStringBuilder.append("from buysellactive_trans a ");
    queryStringBuilder.append("cross join (SELECT @ba \\:= 0, @sa \\:= 0, @preP \\:= 0 , @isFirst \\:= 0) params ");
    queryStringBuilder.append("WHERE ticker =:ticker and seq_time >= (FLOOR(UNIX_TIMESTAMP()/86400) *86400 ) ");
    queryStringBuilder.append("order by seq_time asc ");
    queryStringBuilder.append(")tbl ");

    try {
      return FuturesMarket.futuresMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get derivative buy trans from db")
  public static List<HashMap<String, Object>> getDerivativeBuyTrans(String ticker, Long from) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT FROM_UNIXTIME(seq_time), ticker , seq_time, p, v ");
    queryStringBuilder.append("FROM ( ");
    queryStringBuilder.append("SELECT ticker , seq_time , close_price as p , close_vol  as v  ");
    queryStringBuilder.append("from buysellactive_trans a ");
    queryStringBuilder.append("WHERE ticker =:ticker and seq_time >= :from ");
    queryStringBuilder.append("order by seq_time asc ");
    queryStringBuilder.append(")tbl ");

    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("from", from)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
