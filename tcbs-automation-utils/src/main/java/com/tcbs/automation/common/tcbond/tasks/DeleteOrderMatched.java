package com.tcbs.automation.common.tcbond.tasks;

import com.tcbs.automation.tcbond.OrderMatched;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class DeleteOrderMatched implements Task {
  private OrderMatched orderMatchedData;
  private String strOrderMatchedData;

  public DeleteOrderMatched(OrderMatched orderMatchedData) {
    this.orderMatchedData = orderMatchedData;
    this.strOrderMatchedData = orderMatchedData.toString();
  }

  public static DeleteOrderMatched withInfo(OrderMatched orderMarketData) {
    return instrumented(DeleteOrderMatched.class, orderMarketData);
  }

  @Override
  @Step("[Database] {0} deletes ORDER_MARKET data with info: #strOrderMatchedData")
  public <T extends Actor> void performAs(T t) {
    this.orderMatchedData.deleteByMatchedId();
  }
}
