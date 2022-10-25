package com.automation.common.tcbond.tasks;

import com.automation.tcbond.Intradaylimit;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class CreateNewIntradayDataViaDB implements Task {
  private Intradaylimit intradaylimitData;
  private String strIntradayLimitData;

  public CreateNewIntradayDataViaDB(Intradaylimit intradaylimitData) {
    this.intradaylimitData = intradaylimitData;
    this.strIntradayLimitData = intradaylimitData.toString();
  }

  public static CreateNewIntradayDataViaDB withInfo(Intradaylimit intradaylimitData) {
    return instrumented(CreateNewIntradayDataViaDB.class, intradaylimitData);
  }

  @Override
  @Step("[Database] {0} create intraday limit data with info: #strIntradayLimitData")
  public <T extends Actor> void performAs(T t) {
    intradaylimitData.setLimitationtype(1);
    if (this.intradaylimitData.getActualprice() != null
      && this.intradaylimitData.getIntradayDate() != null) {
      this.intradaylimitData.insert();
    }
  }
}
