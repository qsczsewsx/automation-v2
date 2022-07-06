package com.tcbs.automation.stoxplus.dialect;

import org.hibernate.dialect.SQLServer2012Dialect;
import org.hibernate.type.StandardBasicTypes;

import java.sql.Types;

public class StoxPlusDialect extends SQLServer2012Dialect {
  public StoxPlusDialect() {
    super();
    registerHibernateType(Types.LONGNVARCHAR, StandardBasicTypes.TEXT.getName());
    registerHibernateType(Types.NCHAR, StandardBasicTypes.TEXT.getName());
  }
}
