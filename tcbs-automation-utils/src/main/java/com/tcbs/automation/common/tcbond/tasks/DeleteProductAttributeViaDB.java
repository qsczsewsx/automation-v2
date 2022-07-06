package com.tcbs.automation.common.tcbond.tasks;


import com.tcbs.automation.tcinvest.InvProductAttr;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;


public class DeleteProductAttributeViaDB implements Task {
  private InvProductAttr productAttr;
  private String strProductAtt;

  public DeleteProductAttributeViaDB(InvProductAttr productAttr) {
    this.productAttr = productAttr;
    this.strProductAtt = productAttr.toString();
  }

  public static DeleteProductAttributeViaDB withInfo(InvProductAttr productAttr) {
    return instrumented(DeleteProductAttributeViaDB.class, productAttr);
  }

  @Override
  @Step("[Database] {0} deletes product attributes with info: #strProductAtt")
  public <T extends Actor> void performAs(T t) {
    if (!this.productAttr.getName().isEmpty() && !this.productAttr.getValue().isEmpty()) {
      this.productAttr.delete();
    }
  }
}
