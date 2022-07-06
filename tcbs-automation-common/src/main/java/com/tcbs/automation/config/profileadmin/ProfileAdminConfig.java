package com.tcbs.automation.config.profileadmin;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class ProfileAdminConfig {

  private static final Config conf = new ConfigImpl("profileadmin").getConf();

  public static final String PROFILEADMIN_USERS = conf.getString("profileAdmin.users");

  // using config with full info
  public static final String PROFILES_ADMIN_DOMAIN = conf.getString("profileAdmin.domain");
  public static final String PROFILES_ADMIN_SEARCHECUSTOMER = conf.getString("profileAdmin.rmSearchCustomer");
}
