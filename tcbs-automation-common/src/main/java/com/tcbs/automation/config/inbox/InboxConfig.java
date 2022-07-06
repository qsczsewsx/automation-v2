package com.tcbs.automation.config.inbox;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class InboxConfig {
  private static final Config conf = new ConfigImpl("inbox").getConf();

  //INBOX
  public static final String INBOX_DOMAIN = conf.getString("inbox.domain");
  public static final String INBOX_FORWARDER_INTERNAL = conf.getString("inbox.forwarder-internal");
  public static final String INBOX_WORKER_EXTERNAL = conf.getString("inbox.worker-external");
  public static final String INBOX_WORKER_INTERNAL = conf.getString("inbox.worker-internal");
  public static final String INBOX_API_KEY = conf.getString("inbox.api-key");
  public static final String INBOX_WORKER_BACK = conf.getString("inbox.worker-back");
  public static final String INBOX_INTERNAL = conf.getString("inbox.internal-worker");
  public static final String WORKER_BACK = conf.getString("inbox.worker-back");
  public static final String INBOX_DOMAIN_NEW = conf.getString("inbox.domain-new");
  public static final String INBOX_DOMAIN_THIRDPARTY = conf.getString("inbox.thirdparty");
  public static final String INBOX_DOMAIN_PUBLIC = conf.getString("inbox.public-inbox");

  // ACTION
  public static final String INBOX_ACCOUNT = conf.getString("inbox.account");
  public static final String INBOX_MESSAGE = conf.getString("inbox.message");
  public static final String INBOX_UNICAST = conf.getString("inbox.unicast");
  public static final String INBOX_MULTICAST_V1 = conf.getString("inbox.multicast-v1");
  public static final String INBOX_BROADCAST = conf.getString("inbox.broadcast");
  public static final String INBOX_MULTICAST_V2 = conf.getString("inbox.multicast-v2");
  public static final String INBOX_VIEW_ALL = conf.getString("inbox.viewall");
  public static final String INBOX_READ_ALL = conf.getString("inbox.readall");
  public static final String INBOX_UNREAD = conf.getString("inbox.unread");
  public static final String INBOX_READ = conf.getString("inbox.read");
  public static final String INBOX_DELETE_ALL = conf.getString("inbox.deleteall");
  public static final String INBOX_DELETE = conf.getString("inbox.delete");
  public static final String INBOX_NOTIFICATION = conf.getString("inbox.notification");
  public static final String INBOX_RECEIVER = conf.getString("inbox.receiver");
  public static final String INBOX_NUMBER = conf.getString("inbox.number");
  public static final String INBOX_UPDATE_TIME = conf.getString("inbox.update-time");
  public static final String INBOX_ARCHIVE = conf.getString("inbox.archive");
  public static final String INBOX_TCBS_ID = conf.getString("inbox.tcbs-id");
  public static final String INBOX_VALIDATE = conf.getString("inbox.validate");
  public static final String INBOX_TOTAL_USER = conf.getString("inbox.total-user");
  public static final String INBOX_MARKETING = conf.getString("inbox.marketing");
  public static final String INBOX_ADD = conf.getString("inbox.add");
  public static final String INBOX_SYNC = conf.getString("inbox.sync");
}
