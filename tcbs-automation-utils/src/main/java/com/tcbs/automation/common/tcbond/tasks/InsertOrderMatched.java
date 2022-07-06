package com.tcbs.automation.common.tcbond.tasks;

import com.tcbs.automation.tcbond.OrderMatched;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class InsertOrderMatched implements Task {
  private OrderMatched orderMatchedData;
  private String strOrderMatchedData;

  public InsertOrderMatched(OrderMatched orderMatchedData) {
    this.orderMatchedData = orderMatchedData;
    this.strOrderMatchedData = orderMatchedData.toString();
  }

  public static InsertOrderMatched withInfo(OrderMatched orderMarketData) {
    return instrumented(InsertOrderMatched.class, orderMarketData);
  }

  @Override
  @Step("[Database] {0} insert ORDER_MATCHED data with info: #strOrderMatchedData")
  public <T extends Actor> void performAs(T t) {
    this.orderMatchedData.insert();
  }
}
