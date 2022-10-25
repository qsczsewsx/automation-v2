package com.automation.common.tcbond.actions;

import com.automation.common.tcbond.tasks.CreateBondAttributeViaDB;
import com.automation.common.tcbond.tasks.DeleteBondAttributeViaDB;
import com.automation.common.tcbond.tasks.UpdateBondAttributesViaDB;
import com.automation.tcinvest.InvBondAttr;
import net.serenitybdd.screenplay.Task;

public class WorkOnBondAttributes {

  public static Task toCreateNewOne(InvBondAttr bondAttr) {
    return Task.where("{0} creates new bond attribute",
      CreateBondAttributeViaDB.withInfo(bondAttr)).with("bondAttribute").of(bondAttr);
  }

  public static Task toUpdateExistingOne(InvBondAttr bondAttr) {
    return Task.where("{0} updates bond attribute",
      UpdateBondAttributesViaDB.withInfo(bondAttr.getName(), bondAttr.getValue(), bondAttr.getBondStaticId()));
  }

  public static Task toDelete(InvBondAttr bondAttr) {
    return Task.where("{0} deletes bond attribute",
      DeleteBondAttributeViaDB.withInfo(bondAttr));
  }
}