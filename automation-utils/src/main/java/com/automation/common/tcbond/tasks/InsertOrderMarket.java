package com.automation.common.tcbond.tasks;

import com.automation.tcbond.OrderMarket;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class InsertOrderMarket implements Task {
  private OrderMarket orderMarketData;
  private String strOrderMarketData;
  private String keyToRememberOrder;
  private String keyToRememberId;


  public InsertOrderMarket(OrderMarket orderMarketData) {
    this.orderMarketData = orderMarketData;
    this.strOrderMarketData = orderMarketData.toString();
  }

  public static InsertOrderMarket withInfo(OrderMarket orderMarketData) {
    return instrumented(InsertOrderMarket.class, orderMarketData);
  }

  public InsertOrderMarket andRememberAs(String key) {
    this.keyToRememberOrder = key;
    return this;
  }

  public InsertOrderMarket andRememberOrderId(String key) {
    this.keyToRememberId = key;
    return this;
  }

  @Override
  @Step("[Database] {0} insert ORDER_MARKET data with info: #strOrderMarketData")
  public <T extends Actor> void performAs(T actor) {
    this.orderMarketData.insert();
    actor.remember(keyToRememberOrder, orderMarketData);
    actor.remember(keyToRememberId, orderMarketData.orderId);
  }
}
