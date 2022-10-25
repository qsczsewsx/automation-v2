package com.automation.common.tcbond.tasks;

import com.automation.tcbond.Trading;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class UpdateTradingBD implements Task {
  private Integer referenceStatus;
  private Integer id;

  public UpdateTradingBD(Integer referenceStatus, Integer id) {
    this.referenceStatus = referenceStatus;
    this.id = id;
  }

  public static UpdateTradingBD with(Integer referenceStatus, Integer id) {
    return instrumented(UpdateTradingBD.class, referenceStatus, id);
  }

  @Override
  @Step("[Database] {0} updates Trading with referenceStatus: #referenceStatus")
  public <T extends Actor> void performAs(T user) {
    new Trading().updateReferenceStatusValueById(referenceStatus, id);

  }
}
