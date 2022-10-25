package com.automation.common.tcbond.tasks;

import com.automation.tcbond.OrderMarket;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class DeleteOrderMarket implements Task {
  private OrderMarket orderMarketData;
  private String strOrderMarketData;

  public DeleteOrderMarket(OrderMarket orderMarketData) {
    this.orderMarketData = orderMarketData;
    this.strOrderMarketData = orderMarketData.toString();
  }

  public static DeleteOrderMarket withInfo(OrderMarket orderMarketData) {
    return instrumented(DeleteOrderMarket.class, orderMarketData);
  }

  @Override
  @Step("[Database] {0} deletes ORDER_MARKET data with info: #strOrderMarketData")
  public <T extends Actor> void performAs(T t) {
    this.orderMarketData.deleteByOrderId();
  }
}
