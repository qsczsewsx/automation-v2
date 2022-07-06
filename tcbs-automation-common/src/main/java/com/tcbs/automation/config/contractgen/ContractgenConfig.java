package com.tcbs.automation.config.contractgen;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class ContractgenConfig {
  private static final Config conf = new ConfigImpl("contractgen").getConf();
  public static final String BOND_CONTRACT_GEN_GET_CONTRACT_V2 = conf.getString("contract-generate.getContractV2");
}
