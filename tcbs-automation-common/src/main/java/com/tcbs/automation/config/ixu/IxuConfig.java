package com.tcbs.automation.config.ixu;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class IxuConfig {
  //File Path
  public static final String ODIN_ICONNECT_FILE_PATH = "/src/test/resources/odin/iconnect/";
  public static final String ODIN_HOLDINGFUND_FILE_PATH = "/src/test/resources/odin/inactivecampaign/holdingfund/";
  public static final String ODIN_PROINVESTOR_FILE_PATH = "/src/test/resources/odin/professionalinvestor/";
  public static final String MESSAGES_FILE_NAME = "IConnect100Messages.txt";
  public static final String VALID_MESSAGE_FILE_NAME = "IConnectMessage.txt";
  public static final String ICONNECT_SPLIT_KEY = "iconnect100Messages";
  public static final String ICONNECT_MAIN_TABLE_NAME = "IConnectJob100MessagesMainTable.txt";
  private static final Config conf = new ConfigImpl("ixu").getConf();
  /*
   * jarvis
   */
  public static final String JARVIS_SCAN_AND_PUSH_AWARDING_POINT = conf.getString("jarvis.scanAndPushInbox");
  public static final String JARVIS_PUSH_AUDIT_FILE = conf.getString("jarvis.pushAuditFile");
  public static final String JARVIS_GET_RETURN_AUDIT_FILE = conf.getString("jarvis.getReturnAuditFile");
  public static final String HULK_GET_RETURN_AUDIT_FILE_URL = conf.getString("jarvis.hulkGetReturnAuditFile");
  public static final String JARVIS_INBOX_X_API_KEY = conf.getString("jarvis.x-api-key");
  public static final String JARVIS_INSPECT_TRANSACTION = conf.getString("jarvis.inspectData");
  public static final String JARVIS_REDIS_URL = conf.getString("jarvis.redis-url");
  public static final String JARVIS_REFUND_URL = conf.getString("jarvis.refund");
  public static final String JARVIS_TAX_URL = conf.getString("jarvis.tax");
  // hulk
  public static final String HULK_URL_GET_FILE = conf.getString("hulk.getFileURL");
  public static final String HULK_URL_GET_FOLDER = conf.getString("hulk.getDirURl");
  public static final String HULK_URL_POST_FILE = conf.getString("hulk.postFileURL");
  public static final String HULK_URL_DELETE_FILE = conf.getString("hulk.deleteFileURL");
  public static final String HULK_X_API_KEY = conf.getString("hulk.x-api-key");
  //odin
  public static final String ODIN_RATE_FOR_HOLDING_FUND = conf.getString("odin.holdingFund");
  public static final String ODIN_CACULATING_IXU_FOR_HOLDING_FUND = conf.getString("odin.calculateHoldingFund");
  public static final String ODIN_GET_HOLDING_FUND_CUSTOMER_FAKE = conf.getString("odin.fakeHoldingFundCustomer");
  public static final String ODIN_CALCULATE_PRICING = conf.getString("odin.calculatePricing");
  public static final String CREATE_BATCH_MANUAL_REQUEST = conf.getString("odin.createBatchManualRequest");
  public static final String CREATE_MANUAL_REQUEST = conf.getString("odin.createManualRequest");
  public static final String UPDATE_MANUAL_REQUEST = conf.getString("odin.updateManualRequest");
  public static final String UPDATE_MANUAL_REQUEST_STATUS = conf.getString("odin.updateManualRequestStatus");
  public static final String GET_MANUAL_REQUEST_DETAIL = conf.getString("odin.getManualRequestDetail");
  public static final String EXECUTE_MANUAL_REQUEST_DAILY = conf.getString("odin.executeManualRequestDaily");
  public static final String SEARCH_MANUAL_REQUEST = conf.getString("odin.searchManualRequest");
  public static final String UPDATE_POINT_TRANSACTION_URL = conf.getString("odin.updatePointTransaction");
  public static final String GET_ALL_CAMPAIGNS = conf.getString("odin.getAllCampaigns");
  public static final String SEARCH_FILE_STATUS = conf.getString("odin.searchFileStatus");
  public static final String X_API_KEY_ODIN = conf.getString("odin.x-api-key");
  public static final String ODIN_BIRTHDAY_JOB = conf.getString("odin.birthdayJob");
  public static final String ODIN_CALCULATOR_LIMIT_JOB = conf.getString("odin.calculatorLimitJob");
  public static final String ODIN_GET_TOP_REFERRAL = conf.getString("odin.gettopreferral");
  public static final String GET_MEMBERSHIP_BY_105C = conf.getString("odin.getMembershipBy105c");
  public static final String ODIN_MARGIN_ACCUMULATION_JOB = conf.getString("odin.marginAccumulationJob");
  public static final String ODIN_MARGIN_CONSUME_JOB = conf.getString("odin.marginConsumeJob");
  public static final String ODIN_GET_REFERRAL_TRANSACTION = conf.getString("odin.getReferralTransaction");
  public static final String ODIN_STOCK_JOB = conf.getString("odin.stockJob");
  public static final String ODIN_PUBLISH_MESSAGE_HISTORY = conf.getString("odin.publishMessageHistory");
  public static final String ODIN_STOCK_BACK_DATE_JOB = conf.getString("odin.stockBackdateJob");
  public static final String ODIN_TOTAL_STOCK_TRANSACTION_URL = conf.getString("odin.dwhTotalStockTransaction");
  public static final String ODIN_HOLDING_TCFF_JOB = conf.getString("odin.holdingtcff");
  public static final String ODIN_HOLDING_TCEF_JOB = conf.getString("odin.holdingtcef");
  public static final String ODIN_HOLDING_TCBF_JOB = conf.getString("odin.holdingtcbf");
  public static final String ODIN_CASA_JOB = conf.getString("odin.casa");
  public static final String ODIN_SCAN_PENDING_JOB = conf.getString("odin.pedingjob");
  public static final String ODIN_EXPIRED_BLOCK_TRANSACTION_JOB = conf.getString("odin.expiredBlockTransactionJob");
  public static final String ODIN_HOLDING_FUND_JOB = conf.getString("odin.holdingFundJob");
  public static final String ODIN_SEARCH_TAX_TRANSACTION = conf.getString("odin.searchTaxTransaction");
  public static final String ODIN_REFUND_TAX_TRANSACTION = conf.getString("odin.refundTaxTransaction");
  public static final String ODIN_PROFESSIONAL_INVESTOR = conf.getString("odin.professionalInvestorJob");
  public static final String ODIN_DOCUMENT_GET_BY_ID = conf.getString("odin.documentGetById");
  public static final String ODIN_DOCUMENT_GET_LIST = conf.getString("odin.documentGetList");
  public static final String ODIN_FRACTIONAL_SHARE_TRADING_JOB = conf.getString("odin.fractionalShareTradingJob");
  public static final String ODIN_REFUND_ICONNECT_CALC = conf.getString("odin.refundIConnectCalculator");
  public static final String ODIN_CALCULATING_TRANSACTION = conf.getString("odin.calculatingTransactionJob");
  public static final String ODIN_VINID_JOB = conf.getString("odin.vinidjob");
  public static final String ODIN_EKYC_TCB_JOB = conf.getString("odin.syncEkycTcbJob");
  public static final String ODIN_RETRY_EKYC_TCB_JOB = conf.getString("odin.retrySyncEkycTcbJob");
  public static final String ODIN_RETRY_EKYC_CAMPAIGN = conf.getString("odin.retrySyncEkycCampaign");
  public static final String ODIN_DERIVATIVE_JOB = conf.getString("odin.derivativeJob");
  public static final String ODIN_BOND_PP_JOB = conf.getString("odin.syncBondPpJob");
  public static final String ODIN_GET_CATEGORY_POINT_LIMIT = conf.getString("odin.getCategoryPointLimit");
  public static final String ODIN_VALIDATE_LIMIT = conf.getString("odin.validateLimit");
  public static final String ODIN_REVERT_TRANSACTION = conf.getString("odin.revertTransaction");
  public static final String ODIN_HOLDING_BOND_PRO_JOB = conf.getString("odin.holdingBondPro");
  public static final String ODIN_CLOSE_ACCOUNT = conf.getString("odin.closeAccount");
  public static final String ODIN_RECALL_IFUND_ORDERS = conf.getString("odin.recallIFundOrders");
  public static final String ODIN_GET_PAYMENT_PERIOD = conf.getString("odin.getPaymentPeriod");

  //wanda
  public static final String WANDA_X_API_KEY = conf.getString("wanda.x-api-key");
  public static final String WANDA_ADD_BLACKLIST = conf.getString("wanda.addBlacklist");
  public static final String WANDA_DELETE_BLACKLIST = conf.getString("wanda.deleteBlacklist");
  public static final String WANDA_GET_BLACKLIST = conf.getString("wanda.getBlacklist");
  public static final String WANDA_GET_BLACKLIST_PRODUCT = conf.getString("wanda.getBlacklistProduct");
  public static final String WANDA_GET_MAX_DISCOUNT = conf.getString("wanda.getMaxDiscount");
  public static final String WANDA_GENERAL_TRANSACTION_SEARCH = conf.getString("wanda.searchGeneralTransaction");
  public static final String WANDA_CUSTOMER_TRANSACTION_SEARCH = conf.getString("wanda.searchCustomerTransaction");
  public static final String WANDA_GENERAL_TRANSACTION_CANCEL = conf.getString("wanda.transactionServiceCancel");
  public static final String WANDA_GENERAL_TRANSACTION_CHANGE_STATUS = conf.getString("wanda.transactionServiceChangeStatus");
  public static final String WANDA_GENERAL_TRANSACTION_OVER_LIMIT_ACTION = conf.getString("wanda.overLimitAction");
  public static final String WANDA_GENERAL_TRANSACTION_LIMIT_HISTORY = conf.getString("wanda.limitHistory");
  public static final String WANDA_GENERAL_TRANSACTION_LIMIT_SEARCH = conf.getString("wanda.limitSearch");
  public static final String WANDA_GENERAL_TRANSACTION_LIMIT_UPDATE = conf.getString("wanda.limitUpdate");
  public static final String WANDA_GENERAL_TRANSACTION_RETRY = conf.getString("wanda.transactionServiceRetry");
  public static final String WANDA_GENERAL_LEDGER_SEARCH = conf.getString("wanda.searchGeneralLedger");
  public static final String WANDA_DETAIL_IXU_BY_TCBSID = conf.getString("wanda.getDetailIxuByTcbsId");
  public static final String WANDA_DETAIL_GENERAL_TX_BY_ID = conf.getString("wanda.getDetailGeneralTxById");
  public static final String WANDA_IXU_TX_HISTORY_BY_REFID = conf.getString("wanda.getIxuTxHistoryByRefId");
  public static final String WANDA_UPDATE_IBER_LIST = conf.getString("wanda.updateIberList");
  public static final String WANDA_CHECK_IBER = conf.getString("wanda.checkIber");
  public static final String WANDA_CHECK_VIP = conf.getString("wanda.checkVip");
  public static final String WANDA_RESET_LIMITED_DAILY = conf.getString("wanda.resetLimitedDaily");
  public static final String WANDA_EXE_PENDING_TRANS = conf.getString("wanda.exePendingTransaction");
  public static final String WANDA_CHANGE_LIMITED_DAILY_POINT = conf.getString("wanda.changeLimitedDailyPoint");
  public static final String WANDA_NOTIFY_NEW_DEPLOYMENT = conf.getString("wanda.notifyNewDeployment");
  public static final String WANDA_ENQUIRY_IXU_DYNAMIC_MARGIN = conf.getString("wanda.dynamicMargin");
  public static final String WANDA_CALCULATING_TRANSACTION = conf.getString("wanda.calculatingTransactionJob");

  //queue
  public static final String ICONNECT_QUEUE = conf.getString("odin.IConnectQueue");
  public static final String IXU_CASH_QUEUE = conf.getString("ironman.IxuCashQueue");
  public static final String AUM_QUEUE = conf.getString("odin.aumQueue");
  public static final String REFUND_IWP_QUEUE = conf.getString("odin.refundiwpQueue");
  public static final String COMMISSION_IWP_QUEUE = conf.getString("odin.commissioniwpQueue");
  public static final String RETRY_PRO_INVESTOR_QUEUE = conf.getString("odin.proInvestorQueue");
  public static final String IBOND_QUEUE = conf.getString("odin.ibondQueue");
  public static final String FUND_QUEUE = conf.getString("odin.fundQueue");
  public static final String OPEN_ACCOUNT_QUEUE = conf.getString("odin.openAccountQueue");
  public static final String REF_BOND_QUEUE = conf.getString("odin.refBondFundQueue");
  public static final String REF_STOCK_QUEUE = conf.getString("odin.refStockQueue");
  public static final String REF_STOCK_QUEUE_FOR_RETRY = conf.getString("odin.refStockQueueForRetry");
  public static final String COFFEE_QUEUE = conf.getString("odin.coffeeQueue");
  public static final String DYNAMIC_MARGIN_QUEUE = conf.getString("odin.dynamicMarginQueue");
  public static final String TCBF_REFUND_QUEUE = conf.getString("odin.tcbfRefundQueue");
  public static final String FRACTION_SHARE_QUEUE = conf.getString("odin.fractionShareQueue");
  public static final String VINID_CONFIRM_FOCUS_QUEUE = conf.getString("odin.vinidConfirmFocusQueue");
  public static final String VINID_FUND_QUEUE = conf.getString("odin.vinidfund");
  public static final String VINID_BOND_REFUND_QUEUE = conf.getString("odin.vinidBondRefundQueue");
  public static final String VINID_BOND_FILTER_QUEUE = conf.getString("odin.ibondQueue");
  public static final String REFERRAL_DERIVATIVE_QUEUE = conf.getString("odin.referralExternal");
  //public static final String DERIVATIVE_JOB_QUEUE = conf.getString("odin.derivativeJobQueue");
  public static final String MONTHLY_CASHBACK_QUEUE = conf.getString("odin.monthlyCashbackQueue");

  //iconnect
  public static final String ICONNECT_GET_TCBSID_BY_CUSTOMERID = conf.getString("odin.getTcbsIDByCustomerID");
  //IRONMAN
  public static final String IRONMAN_VALIDATE_ORDER = conf.getString("ironman.validateorder");
  public static final String IRONMAN_SETTLE_ORDER = conf.getString("ironman.settleorder");
  public static final String IRONMAN_SEARCH_PROFILE = conf.getString("ironman.searchprofile");
  public static final String IRONMAN_SALE_TRANSACTION = conf.getString("ironman.saletransaction");
  public static final String IRONMAN_SALE_TRANSACTION_V2 = conf.getString("ironman.saletransactionv2");
  public static final String IRONMAN_X_API_KEY = conf.getString("ironman.x-api-key");
  public static final String IRONMAN_GL_ENTRY = conf.getString("ironman.gl");
  public static final String IRONMAN_GL_X_API_KEY = conf.getString("ironman.gl-x-api-key");
  public static final String IRONMAN_EXCHANGE_IXU_TO_CASH = conf.getString("ironman.exchangeixutocash");
  public static final String IRONMAN_CREATE_PLAN = conf.getString("ironman.createplan");
  public static final String IRONMAN_CHANGE_STATUS_PLAN = conf.getString("ironman.changestatusplan");
  public static final String IRONMAN_GET_INFO_PLAN = conf.getString("ironman.getinfoplan");
  public static final String IRONMAN_RETRY_IXU_TO_CASH = conf.getString("ironman.retryixutocash");
  public static final String IRONMAN_RETRY_IXU_TO_VINID = conf.getString("ironman.retryixutovinid");
  public static final String IRONMAN_CANCEL_IXU_TO_CASH = conf.getString("ironman.cancelixutocash");
  public static final String IRONMAN_TRANSFER_IXU = conf.getString("ironman.transferixu");
  public static final String IRONMAN_TRANSFER_IXU_UPDATE_PRIORITY = conf.getString("ironman.updatepriority");
  public static final String IRONMAN_TRANSFER_IXU_SCAN_NOTIFY = conf.getString("ironman.scannotify");
  public static final String IRONMAN_TRANSFER_IXU_GET_TEMPLATE = conf.getString("ironman.gettemplate");
  public static final String IRONMAN_TRANSFER_IXU_GET_CONTENT = conf.getString("ironman.getcontent");
  public static final String IRONMAN_TRANSFER_IXU_VERIFY_USER = conf.getString("ironman.verifyuser");
  public static final String IRONMAN_TRANSFER_IXU_CUSTOMER_VERIFICATION = conf.getString("ironman.customer-verification");
  public static final String IRONMAN_ISTOCK_PPSE = conf.getString("ironman.istock");
  public static final String IRONMAN_ORDER_FUND_GET_CUSTOMERS_URL = conf.getString("ironman.fund-order-get-customers-url");
  public static final String REQUEST_ORDER_FUND_URL = conf.getString("ironman.fund-order-buy-url");
  public static final String CANCEL_ORDER_FUND_URL = conf.getString("ironman.fund-order-cancel-url");
  public static final String RETRY_ORDER_FUND_URL = conf.getString("ironman.fund-order-retry-url");
  public static final String UPDATE_ORDER_FUND_STATUS_URL = conf.getString("ironman.fund-order-status-update-url");
  // HEIMDALL
  public static final String HEIMDALL_URL_BALANCES = conf.getString("heimdall.getbalances");
  public static final String HEIMDALL_URL_CREATED_IXU = conf.getString("heimdall.createIxu");
  public static final String HEIMDALLL_GET_CURRENT_BALANCE = conf.getString("heimdall.getcurrentbalance");
  public static final String HEIMDALLL_UPDATE_CACHE_BALANCE = conf.getString("heimdall.updateCacheBalance");
  public static final String HEIMDALLL_TRANSACTION_IXU = conf.getString("heimdall.transaction");
  public static final String HEIMDALL_BATCH_TRANSACTION_IXU = conf.getString("heimdall.batch-transaction");
  public static final String HEIMDALLL_X_API_KEY = conf.getString("heimdall.x-api-key");
  public static final String HEIMDALLL_SECRET_KEY = conf.getString("heimdall.secret-key");
  public static final String HEIMDALLL_GET_TRANSACTION_HISTORY = conf.getString("heimdall.gettransactionhistory");
  public static final String HEIMDALLL_GET_MEMBERSHIP_LIST = conf.getString("heimdall.getmembershiplist");
  public static final String HEIMDALL_GET_MEMBERSHIP_LIST_WITH_BACK_DATE = conf.getString("heimdall.getmembershiplistwithBackDate");
  public static final String HEIMDALLL_UPDATE_MEMBERSHIP = conf.getString("heimdall.updatemembership");
  public static final String HEIMDALLL_UPDATE_MEMBERSHIP_V2 = conf.getString("heimdall.updateMemberShipV2");
  public static final String HEIMDALLL_UPDATE_RANKING_DEBIT = conf.getString("heimdall.updateMemberDebit");
  public static final String HEIMDALLL_REDIS_URL = conf.getString("heimdall.redis-url");
  public static final String HEIMDALLL_BLOCK_IXU = conf.getString("heimdall.block");
  public static final String HEIMDALLL_UNBLOCK_IXU = conf.getString("heimdall.unblock");
  public static final String HEIMDALLL_EXPIRED_BLOCK_TRANSACTION = conf.getString("heimdall.expiredBlockTransaction");
  public static final String HEIMDALL_CHECK_UPDATE_RANKING = conf.getString("heimdall.checkUpdateRanking");
  public static final String HEIMDALL_UPDATE_CODE105C_IGL_JOB = conf.getString("heimdall.updateCode105CIGLJob");
  public static final String HEIMDALL_CHECK_ASSETS = conf.getString("heimdall.checkAssets");
  public static final String HEIMDALL_GET_BLOCK_TRANSACTIONS = conf.getString("heimdall.getBlockTransaction");
  public static final String HEIMDALL_CLEAR_RANKING = conf.getString("heimdall.clearRanking");

  // HAWKEYE
  public static final String HAWKEYE_GET_BALANCE = conf.getString("hawkeye.getbalance");

  //INBOX
  public static final String INBOX_DOMAIN = conf.getString("inbox.domain");
  public static final String INBOX_API_KEY = conf.getString("inbox.api-key");
  public static final String INBOX_TCBS_ID = conf.getString("inbox.tcbs-id");
  //MOUNTEBANK
  public static final String MOUNTEBANK_BIRTH_DAY_API = conf.getString("mountebank.birthday");
  public static final String MOUNTEBANK_SET_STATE_IBER = conf.getString("mountebank.setStateIber");
  //WSO2IS
  public static final String LOGIN_WSO2IS_API = conf.getString("wso2is.login");
  // queue
  public static final String REMOVE_MESSAGE_QUEUE_URL = conf.getString("rabbitmq.clear-queue");
  // ivoucher
  public static final String IVOUCHER_USE_VINID_CODE_URL = conf.getString("ivoucher.use_vinid_code");
  public static final String IVOUCHER_CREATE_VINID_CODE_URL = conf.getString("ivoucher.create_vinid_code");
  public static final String IVOUCHER_SEARCH_VINID_CODE_URL = conf.getString("ivoucher.search_vinid_code");
  // kafka
  public static final String KAFKA_DERIVATIVE_JOB_TOPIC = conf.getString("kafka.derivative-job-topic");
}
