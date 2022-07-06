package com.tcbs.automation.config.profileservice;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class ProfileServiceConfig {
  private static final Config conf = new ConfigImpl("profileservice").getConf();

  public static final String TCI3PROFILE_DOMAIN = conf.getString("tci3profile.domain");
  public static final String TCI3PROFILE_CURRENT_DOMAIN = conf
    .getString("tci3profile.current.domain");

  public static final String TCI3PROFILE_API_KEY = conf.getString("tci3profile.api-key");

  public static final String TCI3PROFILE_API = conf.getString("tci3profile.api");
  public static final String TCI3PROFILE_SEARCH = conf.getString("tci3profile.search");
  public static final String TCI3PROFILE_PASSWORD = conf.getString("tci3profile.password");
  public static final String TCI3PROFILE_FORGOT = conf.getString("tci3profile.forgot");
  public static final String TCI3PROFILE_REGISTRY = conf.getString("tci3profile.registry");
  public static final String TCI3PROFILE_CONFIRM = conf.getString("tci3profile.confirm");
  public static final String TCI3PROFILE_REGISTER = conf.getString("tci3profile.register");
  public static final String TCI3PROFILE_PENDING = conf.getString("tci3profile.pending");
  public static final String TCI3PROFILE_ACTIVATION_LINKS = conf
    .getString("tci3profile.activationLinks");
  public static final String TCI3PROFILE_TOKEN = conf.getString("tci3profile.token");
  public static final String TCI3PROFILE_VERIFY = conf.getString("tci3profile.verify");
  public static final String TCI3PROFILE_ACTIVATE = conf.getString("tci3profile.activate");
  public static final String TCI3PROFILE_VALIDATE = conf.getString("tci3profile.validate");
  public static final String TCI3PROFILE_CHANGE_PASSWORD = conf
    .getString("tci3profile.changePassword");
  public static final String TCI3PROFILE_SEARCH_BY_CONDITION = conf
    .getString("tci3profile.searchByCondition");
  public static final String TCI3PROFILE_PROFILE_BY_USERNAME = conf
    .getString("tci3profile.profileByUsername");
  public static final String TCI3PROFILE_CACHE = conf
    .getString("tci3profile.cache");

  public static final String NEW_BACKEND_API = conf.getString("new-backend.api");
  public static final String NEW_BACKEND_SEARCH = conf.getString("new-backend.search");

  public static final String IAGENT_API = conf.getString("iagent.api");
  public static final String IAGENT_SEARCH = conf.getString("iagent.search");

  public static final String VIDEOASK_API = conf.getString("videoask.api");
  public static final String VIDEOASK_SEARCH = conf.getString("videoask.search");
}
