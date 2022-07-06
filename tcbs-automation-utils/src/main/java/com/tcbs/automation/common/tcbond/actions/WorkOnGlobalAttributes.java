package com.tcbs.automation.common.tcbond.actions;

import com.tcbs.automation.common.tcbond.tasks.CreateGlobalAttributeViaDB;
import com.tcbs.automation.common.tcbond.tasks.DeleteGlobalAttributeViaDB;
import com.tcbs.automation.common.tcbond.tasks.UpdateGlobalAttributesViaDB;
import com.tcbs.automation.tcinvest.InvGlobalAttr;
import net.serenitybdd.screenplay.Task;

public class WorkOnGlobalAttributes {

  public static Task toCreateNewOne(InvGlobalAttr globalAttr) {
    return Task.where("{0} creates new global attribute",
      CreateGlobalAttributeViaDB.withInfo(globalAttr)).with("globalAttribute").of(globalAttr);
  }

  public static Task toUpdateExistingOne(InvGlobalAttr globalAttr) {
    return Task.where("{0} updates global attribute",
      UpdateGlobalAttributesViaDB.ofAttributeName(globalAttr.getName()).withValue(globalAttr.getValue()));
  }

  public static Task toDelete(InvGlobalAttr globalAttr) {
    return Task.where("{0} deletes global attribute",
      DeleteGlobalAttributeViaDB.withInfo(globalAttr));
  }
}