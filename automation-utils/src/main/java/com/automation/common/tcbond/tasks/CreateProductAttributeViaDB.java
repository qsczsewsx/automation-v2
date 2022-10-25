package com.automation.common.tcbond.tasks;


import com.automation.tcinvest.InvProductAttr;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;


public class CreateProductAttributeViaDB implements Task {
  private InvProductAttr productAttr;
  private String strProductAttr;

  public CreateProductAttributeViaDB(InvProductAttr productAttr) {
    this.productAttr = productAttr;
    this.strProductAttr = productAttr.toString();
  }

  public static CreateProductAttributeViaDB withInfo(InvProductAttr productAttr) {
    return instrumented(CreateProductAttributeViaDB.class, productAttr);
  }

  @Override
  @Step("[Database] {0} create product attributes with info: #strProductAttr")
  public <T extends Actor> void performAs(T t) {
    //todo: check product id
    if (!this.productAttr.getName().isEmpty() && !this.productAttr.getValue().isEmpty()) {
      this.productAttr.insert();
    }
  }
}
