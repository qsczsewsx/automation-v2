package com.automation.common.tcbond.tasks;

import com.automation.tcbond.Intradaylimit;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class DeleteIntradayLimitDataViaDB implements Task {
  private Intradaylimit intradaylimitData;
  private String strIntradayLimitData;

  public DeleteIntradayLimitDataViaDB(Intradaylimit intradaylimitData) {
    this.intradaylimitData = intradaylimitData;
    this.strIntradayLimitData = intradaylimitData.toString();
  }

  public static DeleteIntradayLimitDataViaDB withInfo(Intradaylimit intradaylimitData) {
    return instrumented(DeleteIntradayLimitDataViaDB.class, intradaylimitData);
  }

  @Override
  @Step("[Database] {0} deletes intraday limit data with info: #intradaylimitData")
  public <T extends Actor> void performAs(T t) {
    this.intradaylimitData.deleteById();
  }

}
