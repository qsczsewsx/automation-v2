package com.tcbs.automation.common.tcbond.tasks;

import com.tcbs.automation.tcbond.Trading;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class UpdateStatusInTradingDB implements Task {
  private Trading trading;

  public UpdateStatusInTradingDB(Trading trading) {
    this.trading = trading;
  }

  public static UpdateStatusInTradingDB with(Trading trading) {
    return instrumented(UpdateStatusInTradingDB.class, trading);

  }

  @Override
  public <T extends Actor> void performAs(T t) {
    trading.updateTrading();
  }
}
