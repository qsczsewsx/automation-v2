package com.tcbs.automation.common.tcbond.tasks;


import com.tcbs.automation.tcinvest.InvGlobalAttr;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import net.thucydides.core.steps.StepFailure;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.thucydides.core.steps.ExecutedStepDescription.withTitle;


public class CreateGlobalAttributeViaDB implements Task {
  private InvGlobalAttr globalAttr;
  private String strGlobalAtt;

  public CreateGlobalAttributeViaDB(InvGlobalAttr globalAttr) {
    this.globalAttr = globalAttr;
    this.strGlobalAtt = globalAttr.toString();
  }

  public static CreateGlobalAttributeViaDB withInfo(InvGlobalAttr globalAttr) {
    return instrumented(CreateGlobalAttributeViaDB.class, globalAttr);
  }

  @Override
  @Step("[Database] {0} create global attributes with info: #strGlobalAtt")
  public <T extends Actor> void performAs(T t) {
    if (!this.globalAttr.getName().isEmpty() && !this.globalAttr.getValue().isEmpty()) {
      try {
        this.globalAttr.insert();
      } catch (Exception e) {
        StepEventBus.getEventBus().stepFailed(new StepFailure(withTitle("Unable to insert new global attribute"), e));
      }
    }
  }
}
