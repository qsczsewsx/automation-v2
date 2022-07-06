package com.tcbs.automation.config.idataicalendar;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class IDataIcalendarConfig {
  private static final Config conf = new ConfigImpl("idataicalendar").getConf();
  // market event
  public static final String ICALENDAR_EVENT_MARKET_LIST_OPTION_URL = conf.getString("icalendar.event.eventMarketListOptionUrl");
  public static final String ICALENDAR_EVENT_MARKET_LIST_SHORT_URL = conf.getString("icalendar.event.eventMarketListShortUrl");
  public static final String ICALENDAR_EVENT_MARKET_LIST_FULL_URL = conf.getString("icalendar.event.eventMarketListFullUrl");

  // personal event
  public static final String ICALENDAR_EVENT_PERSONAL_LIST_OPTION_URL = conf.getString("icalendar.event.eventPersonalListOptionUrl");
  public static final String ICALENDAR_EVENT_PERSONAL_LIST_FULL_URL = conf.getString("icalendar.event.eventPersonalListFullUrl");

  // notification - api
  public static final String ICALENDAR_NOTI_GET_CONFIG_NOTI_URL = conf.getString("icalendar.noti.notificationConfigUrl");
  public static final String ICALENDAR_NOTI_GET_EVENT_INFO_URL = conf.getString("icalendar.noti.getDynamicEventInfoUrl");
  public static final String ICALENDAR_NOTI_GET_DYNAMIC_DATA_NOTI = conf.getString("icalendar.noti.getDynamicDataNotiUrl");

  // notification - eventhub
  public static final String ICALENDAR_NOTI_ACTIVE_CHEAT_EVENTHUB = conf.getString("icalendar.noti.getActiveEventHubNotiUrl");

  // Get event config menu
  public static final String HFC_GET_EVENT_CONFIG_MENU = conf.getString("icalendar.menu.getEventConfigMenu");

  // Widget Api
  public static final String ICALENDAR_GET_EVENT_RMWIDGET_URL = conf.getString("icalendar.widget.getEventRMWigdetUrl");
  public static final String ICALENDAR_GET_EVENT_CUSWIDGET_URL = conf.getString("icalendar.widget.getEventCusWigdetUrl");
  public static final String ICALENDAR_GET_PER_RMWIDGET_URL = conf.getString("icalendar.widget.getPerRMWigdetUrl");
  public static final String ICALENDAR_GET_PER_CUSWIDGET_URL = conf.getString("icalendar.widget.getPerCusWigdetUrl");



}
