package com.automation.common.tcbond.tasks;


import com.automation.tcinvest.InvGlobalAttr;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import net.thucydides.core.steps.StepFailure;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.thucydides.core.steps.ExecutedStepDescription.withTitle;


public class UpdateGlobalAttributesViaDB implements Task {
  private String value;
  private String name;

  public UpdateGlobalAttributesViaDB(String name) {
    this.name = name;
  }

  public static UpdateGlobalAttributesViaDB ofAttributeName(String attName) {
    return instrumented(UpdateGlobalAttributesViaDB.class, attName);
  }

  public UpdateGlobalAttributesViaDB withValue(String value) {
    this.value = value;
    return this;
  }

  @Override
  @Step("[Database] {0} updates global attributes name: #name with values: #value")
  public <T extends Actor> void performAs(T t) {
    if (name.isEmpty() && value.isEmpty()) {
      StepEventBus.getEventBus().stepFailed(new StepFailure(withTitle("Unable to update global attributes"), new RuntimeException("Name or Value is not defined")));
    } else {
      InvGlobalAttr globalAttr = new InvGlobalAttr();
      globalAttr.updateAttribute_value_byName(this.name, this.value);
    }
  }
}
