package com.tcbs.automation.config.pricingreference;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

/**
 * @author Nguyen Ngoc Tien
 * @created 15/09/2020 - 16:01
 */
public class PricingReferenceConfig {
  private static final Config conf = new ConfigImpl("pricingreference").getConf();

  public static final String PR_PRICING_VERIFY = conf.getString("pricing-reference.pricing-verify");
  public static final String PR_SYNC_FILES = conf.getString("pricing-reference.sync-files");
  public static final String PR_SYNC_FILE = conf.getString("pricing-reference.sync-file");
  public static final String PR_PRICING_AUDIT = conf.getString("pricing-reference.pricing-audit");
}
