package com.automation.common.tcbond.tasks;


import com.automation.tcinvest.InvBondAttr;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;


public class CreateBondAttributeViaDB implements Task {
  private InvBondAttr bondAttr;
  private String strBondAttr;

  public CreateBondAttributeViaDB(InvBondAttr bondAttr) {
    this.bondAttr = bondAttr;
    this.strBondAttr = bondAttr.toString();
  }

  public static CreateBondAttributeViaDB withInfo(InvBondAttr bondAttr) {
    return instrumented(CreateBondAttributeViaDB.class, bondAttr);
  }

  @Override
  @Step("[Database] {0} create bond attributes with info: #strBondAttr")
  public <T extends Actor> void performAs(T t) {
    //todo: check bond id
    if (!this.bondAttr.getName().isEmpty() && !this.bondAttr.getValue().isEmpty()) {
      this.bondAttr.insert();
    }
  }
}
