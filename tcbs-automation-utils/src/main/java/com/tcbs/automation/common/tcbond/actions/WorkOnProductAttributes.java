package com.tcbs.automation.common.tcbond.actions;

import com.tcbs.automation.common.tcbond.tasks.CreateProductAttributeViaDB;
import com.tcbs.automation.common.tcbond.tasks.DeleteProductAttributeViaDB;
import com.tcbs.automation.common.tcbond.tasks.UpdateProductAttributesViaDB;
import com.tcbs.automation.tcinvest.InvProductAttr;
import net.serenitybdd.screenplay.Task;

public class WorkOnProductAttributes {

  public static Task toCreateNewOne(InvProductAttr productAttr) {
    return toCreateNewOne("{0} creates new global attribute", productAttr);
  }

  public static Task toCreateNewOne(String stepDescription, InvProductAttr productAttr) {
    return Task.where(stepDescription,
      CreateProductAttributeViaDB.withInfo(productAttr)).with("globalAttribute").of(productAttr);
  }

  public static Task toUpdateExistingOne(InvProductAttr productAttr) {
    return Task.where("{0} updates global attribute",
      UpdateProductAttributesViaDB.withInfo(productAttr.getName(), productAttr.getValue(), productAttr.getProductId()));
  }

  public static Task toDelete(InvProductAttr productAttr) {
    return Task.where("{0} deletes global attribute",
      DeleteProductAttributeViaDB.withInfo(productAttr));
  }
}