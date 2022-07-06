package com.tcbs.automation.common.tcbond.tasks;


import com.tcbs.automation.tcinvest.InvBondAttr;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import net.thucydides.core.steps.StepFailure;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.thucydides.core.steps.ExecutedStepDescription.withTitle;


public class UpdateBondAttributesViaDB implements Task {
  private String value;
  private String name;
  private Long bondId;

  public UpdateBondAttributesViaDB(String name, String value, Long bondId) {
    this.value = value;
    this.name = name;
    this.bondId = bondId;
  }

  public static UpdateBondAttributesViaDB withInfo(String name, String value, Long bondId) {
    return instrumented(UpdateBondAttributesViaDB.class, name, value, bondId);
  }

  //todo: bond static id
  @Override
  @Step("[Database] {0} updates bond attributes name: #name with values: #value for bond: #bondId")
  public <T extends Actor> void performAs(T t) {
    if (name.isEmpty() && value.isEmpty()) {
      StepEventBus.getEventBus().stepFailed(new StepFailure(withTitle("Unable to update bond attributes"), new RuntimeException("Name or Value is not defined")));
    } else {
      InvBondAttr bondAttr = new InvBondAttr();
      bondAttr.updateAttribute_value_byName(name, value, bondId);
    }
  }
}
