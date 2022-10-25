package com.automation.common.tcbond.tasks;


import com.automation.tcinvest.InvProductAttr;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import net.thucydides.core.steps.StepFailure;

import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.thucydides.core.steps.ExecutedStepDescription.withTitle;


public class UpdateProductAttributesViaDB implements Task {
  private String name;
  private String value;
  private Long productId;

  public UpdateProductAttributesViaDB(String name, String value, Long productId) {
    this.name = name;
    this.value = value;
    this.productId = productId;
  }

  public static UpdateProductAttributesViaDB withInfo(String name, String value, Integer productId) {
    return instrumented(UpdateProductAttributesViaDB.class, name, value, productId);
  }

  @Override
  @Step("[Database] {0} updates product attributes name: #name with values: #value for product: #productId")
  public <T extends Actor> void performAs(T t) {
    if (name.isEmpty() && value.isEmpty()) {
      StepEventBus.getEventBus().stepFailed(new StepFailure(withTitle("Unable to update product attributes"), new RuntimeException("Name or Value is not defined")));
    } else {
      InvProductAttr productAttr = new InvProductAttr();
      productAttr.updateAttribute_value_byName(name, value, productId);
    }
  }
}
