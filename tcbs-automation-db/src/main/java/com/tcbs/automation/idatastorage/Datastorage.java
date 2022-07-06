package com.tcbs.automation.idatastorage;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;

public class Datastorage {
  public static final HibernateEdition DataStorageConnection = Database.DATA_STORAGE.getConnection();
}
