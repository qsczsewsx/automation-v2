package com.tcbs.automation.common.tcbond.tasks;


import com.tcbs.automation.tcinvest.InvBondAttr;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;

import static net.serenitybdd.screenplay.Tasks.instrumented;


public class DeleteBondAttributeViaDB implements Task {
  private InvBondAttr bondAttr;
  private String strBondAttr;

  public DeleteBondAttributeViaDB(InvBondAttr bondAttr) {
    this.bondAttr = bondAttr;
    this.strBondAttr = bondAttr.toString();
  }

  public static DeleteBondAttributeViaDB withInfo(InvBondAttr bondAttr) {
    return instrumented(DeleteBondAttributeViaDB.class, bondAttr);
  }

  @Override
  @Step("[Database] {0} deletes bond attributes with info: #strBondAttr")
  public <T extends Actor> void performAs(T t) {
    if (!this.bondAttr.getName().isEmpty() && !this.bondAttr.getValue().isEmpty()) {
      this.bondAttr.delete();
    }
  }
}
