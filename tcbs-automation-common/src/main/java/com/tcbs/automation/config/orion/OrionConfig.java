package com.tcbs.automation.config.orion;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class OrionConfig {
  private static final Config conf = new ConfigImpl("orion").getConf();

  /*
   * jarvis
   */
  public static final String X_API_KEY = conf.getString("hercules.x-api-key");
  public static final String INIT_BLOCK_URL = conf.getString("hercules.url");
  public static final String INTRODUCE_PLEDGE_URL = conf.getString("hercules.urlIntroducePledge");
  public static final String INTRODUCED_PLEDGE_URL = conf.getString("hercules.urlIntroducedPledge");
  public static final String INTRODUCE_UNBLOCK_URL = conf.getString("hercules.urlIntroduceUnblock");
  public static final String UPDATE_PLEDGE_URL = conf.getString("hercules.urlUpdatePledge");
  public static final String UPLOAD_ECM_URL = conf.getString("hercules.uploadEcm");
  public static final String GET_LIMIT_LOAN_BOND_URL = conf.getString("hercules.urlBondPrice");
  public static final String GET_BANK_INFOS_URL = conf.getString("hercules.urlBankInfos");
  public static final String ADVANCED_SEARCH_URL = conf.getString("hercules.urlAdvancedSearch");
  public static final String GET_PROFILE_URL = conf.getString("tcbs-profile.urlGetProfile");
  public static final String GET_BANK_INFOS_PROFILE_URL = conf.getString("hercules.urlBankInfosProfile");
  public static final String GET_ASSET_CUS_RM_URL = conf.getString("hercules.urlRMAsset");
  public static final String GET_ASSET_IBOND_URL = conf.getString("hercules.urlAssetBondTradingIBond");
  public static final String GET_ASSET_ICONNECT_URL = conf.getString("hercules.urlAssetBondTradingIConnect");
  public static final String GET_DWH_BP_URL = conf.getString("hercules.urlDwhServiceBP");
  public static final String GET_PRODUCT_SERVICE_URL = conf.getString("hercules.urlProductService");
  public static final String ADD_PLEDGE_URL = conf.getString("hercules.urlAddPledge");
  public static final String SEARCH_PROFILE_URL = conf.getString("tcbs-profile.urlProfileSearch");
  public static final String SEND_MAIL_URL = conf.getString("hercules.urlSendMail");
  public static final String SUBMIT_BLOCK_APPROVAL = conf.getString("hercules.urlSubmitBlockApproval");
  public static final String SUBMIT_UNBLOCK_APPROVAL = conf.getString("hercules.urlSubmitUnBlockApproval");
  public static final String JWT = conf.getString("hercules.jwt");
  public static final String GET_UNBLOCK_CONTRACT_BY_RM_URL = conf.getString("hercules.urlUnblockContractByRM");
  public static final String CUS_INFO_BY_RMDD_URL = conf.getString("hercules.cusInfoByRMDD");
  public static final String GET_ALL_PLEDGE_CONTRACT_URL = conf.getString("hercules.allPledgeContractInfor");
  public static final String UPDATE_BOND_LISTED = conf.getString("hercules.updateBondListed");
  public static final String CANCEL_PROCESS = conf.getString("hercules.cancelProcess");
  public static final String PACK_INIT_RECORD = conf.getString("hercules.packInitRecord");
  public static final String UPDATE_LISTED_STS_REASON = conf.getString("hercules.updateListedUpdatedStatus");
  public static final String OBTAIN_BOND_BLOCK = conf.getString("hercules.obtainBondBlock");

  public static final String HERCULES_X_API_KEY = conf.getString("hercules.x-api-key");

}
