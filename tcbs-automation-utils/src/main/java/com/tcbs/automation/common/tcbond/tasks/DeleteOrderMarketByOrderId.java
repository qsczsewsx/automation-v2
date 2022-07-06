package com.tcbs.automation.common.tcbond.tasks;

import com.tcbs.automation.tcbond.OrderMarket;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class DeleteOrderMarketByOrderId {

  private final String orderId;
  
  public DeleteOrderMarketByOrderId(String orderId) {
    this.orderId = orderId;
  }

}
