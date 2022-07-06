package com.tcbs.automation.config.coman;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class ComanConfig {
  private static final Config conf = new ConfigImpl("coman").getConf();

  // COMAN
  public static final String DOMAIN = conf.getString("coman.domain");
  public static final String DOMAIN_BACK = conf.getString("coman.domainback");
  public static final String DOMAIN_BOND = conf.getString("coman.domainbond");
  public static final String DOMAIN_INT = conf.getString("coman.domaininternal");
  public static final String DOMAIN_PRODUCT = conf.getString("coman.domain_product");
  public static final String DOMAIN_DCM = conf.getString("coman.domain_dcm");
  public static final String DOMAIN_GLOBAL = conf.getString("coman.domain_global");
  public static final String DOMAIN_CAS = conf.getString("coman.domain_cas");
  public static final String DOMAIN_EXCEL = conf.getString("coman.domain_excel");
  public static final String DOMAIN_BACK_PRODUCT = conf.getString("coman.domainbackproduct");
  public static final String DOMAIN_INT_PRODUCT = conf.getString("coman.domainintproduct");
  public static final String DOMAIN_INT_BOND = conf.getString("coman.domain_int");
  public static final String ADD_INTEREST = conf.getString("coman.insertInterest");
  public static final String VALIDATION_INTEREST = conf.getString("coman.validationInsertInterest");
  public static final String VALIDATION_REMOVE_INTEREST = conf.getString("coman.validateRemoveInterest");
  public static final String REFERENCE_RATE = conf.getString("coman.referenceRate");
  public static final String VALIDATION_REFERENCE_RATE = conf.getString("coman.validateReferenceRate");
  public static final String REFERENCE_RATE_ALL = conf.getString("coman.referenceRateAll");
  public static final String REFERENCE_RATE_DETAIL_INTERNAL = conf.getString("coman.getReferenceRateDetailInternal");
  public static final String ADD_APPLIED_BOND = conf.getString("coman.addAppliedBond");
  public static final String VALIDATION_APPLIED_BOND = conf.getString("coman.validateAppliedBond");
  public static final String REMOVE_INTEREST = conf.getString("coman.removeInterest");
  public static final String GENBONDTIMELINE = conf.getString("coman.generateBondTimeLine");
  public static final String BONDTIMELINE = conf.getString("coman.bondTimeLine");
  public static final String AUTHORIZATION = conf.getString("coman.authorization");
  public static final String COMMAN_I_V1 = conf.getString("coman.comnanIv1");
  public static final String BOND_COUPON_API = conf.getString("coman.bondCoupon");
  public static final String MANUALTIMELINE = conf.getString("coman.manual-timeline");
  public static final String VALIDATION = conf.getString("coman.validation");
  public static final String ZERO_PERIOD = conf.getString("coman.zeroPeriod");
  public static final String LSGD = conf.getString("coman.lsgd");
  public static final String EXPORT = conf.getString("coman.export");
  public static final String TIMELINE = conf.getString("coman.timeline");
  public static final String JOB = conf.getString("coman.job");
  public static final String GEN_NAME = conf.getString("coman.genName");
  public static final String GEN_DOC = conf.getString("coman.genDoc");
  public static final String DELETE_DRAFT = conf.getString("coman.deleteDraft");
  public static final String GET_GROUP_BANK = conf.getString("coman.getGroupBank");
  public static final String GET_DEFINITION_RULE = conf.getString("coman.getDefRule");
  public static final String GET_REFER_BASE = conf.getString("coman.getReferBase");
  public static final String UPDATE_CONSTANT = conf.getString("coman.updateConstant");
  public static final String AFFECTED_BOND = conf.getString("coman.affectedBond");
  public static final String DOMAIN_BANK = conf.getString("coman.domainbank");
  public static final String INTERNAL_BANKINFOS = conf.getString("coman.internalBankInfos");
  public static final String DOMAIN_BOND_BACK = conf.getString("coman.domainbondback");
  public static final String DOMAIN_ATTR = conf.getString("coman.domainattribute");
  public static final String ATTR_PARAM = conf.getString("coman.attributeParamRule");
  public static final String UPDATE_CONFIRM_DATE = conf.getString("coman.updateConfirmDate");
  public static final String GENERATE = conf.getString("coman.generate");
  public static final String UPDATE_STATUS = conf.getString("coman.updateStatus");
  public static final String QUEUE_DEROSIE = conf.getString("derosiequeue.queue");
  public static final String BOND_LISTING_TIMELINE = conf.getString("coman.bond_listing_timeline");
  public static final String PUBLIC_APIS_DETAIL = conf.getString("coman.public_apis_detail");
  public static final String BOND_BONDTEMP_DETAIL = conf.getString("coman.bond_bondtemp_detail");
  public static final String COMPANY = conf.getString("coman.company");
  public static final String DETAIL = conf.getString("coman.detail");
  public static final String GET_PROFILE = conf.getString("coman.get_profile");
  public static final String BY_TCBSID = conf.getString("coman.by_tcbsid");
  public static final String BATCH = conf.getString("coman.batch");
  public static final String BOND = conf.getString("coman.bond");
  public static final String GET = conf.getString("coman.get");
  public static final String BOND_CREATING = conf.getString("coman.bond_creating");
  public static final String BOND_EDITING = conf.getString("coman.bond_editing");
  public static final String BOND_MIGRATE = conf.getString("coman.migration");
  public static final String BOND_TCBSID = conf.getString("coman.tcbsId");
  public static final String BY_IDNUMBER = conf.getString("coman.by_idnumber");
  public static final String MIGRATE = conf.getString("coman.migrate");
  public static final String COUPON_FREQ = conf.getString("coman.couponFreq");


}
