package com.tcbs.automation.config.cogs;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class CogsConfig {
  private static final Config conf = new ConfigImpl("cogs").getConf();

  public static final String TCBS_COGS_DOMAIN = conf.getString("tcbsCogs.domain");
  public static final String TCBS_COGS_CREATE = conf.getString("tcbsCogs.create");
  public static final String TCBS_COGS_GET_LIST = conf.getString("tcbsCogs.get-list-cogs");
  public static final String TCBS_COGS_HISTORY_GET_LIST = conf.getString("tcbsCogs.get-list-cogs-history");
  public static final String TCBS_COGS_BATCH_CALCULATE = conf.getString("tcbsCogs.batch-calculate");
  public static final String TCBS_COGS_BATCH_BACK_CALCULATE = conf.getString("tcbsCogs.batch-back-calculate");
  public static final String TCBS_COGS_CALCULATE = conf.getString("tcbsCogs.calculate");
  public static final String TCBS_COGS_BULK_CALCULATE = conf.getString("tcbsCogs.bulk-calculate");
  public static final String TCBS_COGS_BULK_CREATE = conf.getString("tcbsCogs.bulk-create");
  public static final String TCBS_COGS_TRANS_ACCOUNTING_CALCULATE = conf.getString("tcbsCogs.trans-accounting-calculate");
  public static final String TCBS_COGS_BACK_CALCULATE = conf.getString("tcbsCogs.back-calculate");
  public static final String TCBS_COGS_DATA_INPUT_COGS_BOND_CODE = conf.getString("tcbsCogs.data.importCogsBondCode");
  public static final String TCBS_COGS_TRANS_GET_LIST = conf.getString("tcbsCogs.get-list-transaction");

//  public static final String COGS_POST_A_COSG = conf.getString("trading.placePartnerOrder");
}
