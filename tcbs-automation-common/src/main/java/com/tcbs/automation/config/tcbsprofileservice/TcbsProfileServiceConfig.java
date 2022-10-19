package com.tcbs.automation.config.tcbsprofileservice;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class TcbsProfileServiceConfig {
  private static final Config conf = new ConfigImpl("tcbsprofileservice").getConf();

  public static final String TCBSPROFILE_DOMAIN = conf.getString("tcbsprofile.internal-api-v1");
  public static final String TCBSPROFILE_DEV_DOMAIN = conf.getString("tcbsprofile.dev.domain");

  public static final String TCBSPROFILE_PUB_API = conf.getString("tcbsprofile.pub-api");
  public static final String TCBSPROFILE_OLD_REGISTER = conf.getString("tcbsprofile.oldRegister");
  public static final String TCBSPROFILE_API = conf.getString("tcbsprofile.api");
  public static final String TCBSPROFILE_REGISTER = conf.getString("tcbsprofile.register");
  public static final String TCBSPROFILE_QUEUE_UPDATE = conf.getString("tcbsprofile.queueUpdate");
  public static final String TCBSPROFILE_PENDING = conf.getString("tcbsprofile.pending");
  public static final String TCBSPROFILE_ACTIVATE = conf.getString("tcbsprofile.activate");
  public static final String TCBSPROFILE_AUTO_RETRY = conf.getString("tcbsprofile.autoRetry");
  public static final String ASSIGN_TASK_TO_MAKER_OR_CHECKER = conf.getString("tcbsprofile.assignTask");
  public static final String ASSIGN_TASK_TO_MAKER_KEY = conf.getString("tcbsprofile.assignTaskToMakerKey");
  public static final String ASSIGN_TASK_TO_CHECKER_KEY = conf.getString("tcbsprofile.assignTaskToCheckerKey");
  public static final String TCBSPROFILE_INQUIRYGROUPINFOKEY = conf.getString("tcbsprofile.inquiryGroupInfoKey");
  public static final String TCBSPROFILE_BACKENDWBLKEY = conf.getString("tcbsprofile.backEndWblKey");
  public static final String TCBSPROFILE_TOKENKEY = conf.getString("tcbsprofile.tokenKey");
  public static final String TCBSPROFILE_AUTHORIZATION = conf.getString("tcbsprofile.authorization");
  public static final String TCBSPROFILE_KYC_HISTORY = conf.getString("tcbsprofile.getKycHistory");
  public static final String TCBSPROFILE_GET_TASK_KYC = conf.getString("tcbsprofile.getTaskKYC");
  public static final String TCBSPROFILE_GET_DETAIL_TASK = conf.getString("tcbsprofile.getDetailTask");
  public static final String TCBSPROFILE_BACKENDCAMPAIGN = conf.getString("tcbsprofile.backEndCampaign");
  public static final String TCBSPROFILE_GET_TASK_HISTORY = conf.getString("tcbsprofile.getTaskHistory");
  public static final String TCBSPROFILE_UPDATE_BANK_ACCOUNT = conf.getString("tcbsprofile.updateBankAccount");
  public static final String TCBSPROFILE_OPEN_ACCOUNT_EXTERNAL = conf.getString("tcbsprofile.openAccountExternal");
  public static final String TCBSPROFILE_COMPLETE_INFO_IA = conf.getString("tcbsprofile.completeInfoIA");
  public static final String GET_CAMPAIGN_LIST = conf.getString("tcbsprofile.getCampaignList");
  public static final String GEN_CHANGE_INFO_REPORT = conf.getString("tcbsprofile.genChangeInfoReport");
  public static final String RM_PRINT_CONTRACT = conf.getString("tcbsprofile.rmPrintContract");
  public static final String RM_DOWNLOAD_SINGED_CONTRACT = conf.getString("tcbsprofile.rmDownloadSignedContract");
  public static final String CHECK_BANK_ACCOUNT_OWNER = conf.getString("tcbsprofile.checkBankAccountOwner");
  public static final String GET_STATUS_CHECK_BANK_ACCOUNT_OWNER = conf.getString("tcbsprofile.getStatusCheckBankAccountOwner");
  public static final String GET_BANK_LIST = conf.getString("tcbsprofile.getBankList");
  public static final String BANK_SYNC_H2H = conf.getString("tcbsprofile.bankSyncH2H");
  public static final String INQUIRY_REFERRAL_CODE = conf.getString("tcbsprofile.inquiryReferralCode");
  public static final String FORCE_SYNC_DERIVATIVE = conf.getString("tcbsprofile.forceSyncDerivative");
  public static final String CHECK_AM_LOCK = conf.getString("tcbsprofile.checkAMLock");
  public static final String GET_AMLOCK_HISTORY = conf.getString("tcbsprofile.getAMLockHistory");
  public static final String AMLOCK_KEY = conf.getString("tcbsprofile.amLock-key");
  public static final String GEN_MBLK5_WITH_QRCODE = conf.getString("tcbsprofile.genMblk5WithQrCode");
  public static final String CLOSE_ACCOUNT_UPLOAD_DOC = conf.getString("tcbsprofile.accountCloseUploadDocument");
  public static final String CLOSE_ACCOUNT_GET_DOC = conf.getString("tcbsprofile.accountCloseGetDocument");
  public static final String CLOSE_ACCOUNT_DELETE_DOC = conf.getString("tcbsprofile.accountCloseDeleteDocument");
  public static final String CLOSE_ACCOUNT_DOWNLOAD_DOC = conf.getString("tcbsprofile.accountCloseDownloadDocument");
  public static final String BACK_ADD_TO_BLACK_LIST = conf.getString("tcbsprofile.backAddToBlackList");
  public static final String MULTI_GET_BY_TCBSID = conf.getString("tcbsprofile.multiGetByTcbsId");
  public static final String MULTI_GET_BY_USERNAME = conf.getString("tcbsprofile.multiGetByUsername");
  public static final String MULTI_GET_ON_PROFILE_R = conf.getString("tcbsprofile.multiGetOnProfileR");
  public static final String GET_BANK_LIST_KYC_TASK = conf.getString("tcbsprofile.getBankListKycTask");

  public static final String GEN_CONTRACT_API = conf.getString("tcbsprofile.genContract");
  public static final String SIGN_CONTRACT_API = conf.getString("tcbsprofile.signContract");
  public static final String GET_ACCOUNT_STATUS_API = conf.getString("tcbsprofile.getAccountStatus");
  public static final String GET_CUSTOMER_INFO = conf.getString("tcbsprofile.getCustInfo");
  public static final String API_KEY = conf.getString("tcbsprofile.api-key");
  public static final String GEN_FINAL_CONTRACT = conf.getString("tcbsprofile.genFinalContract");
  public static final String VIEW_CLOSE_ACCOUNT_CONTRACT = conf.getString("tcbsprofile.viewCloseAccountContract");
  public static final String SIGN_CLOSE_ACCOUNT_CONTRACT = conf.getString("tcbsprofile.signCloseAccountContract");

  public static final String MAKER_MODIFY = conf.getString("tcbsprofile.makerModify");
  public static final String MAKER_REJECT = conf.getString("tcbsprofile.makerReject");
  public static final String MAKER_SEND_APPROVE = conf.getString("tcbsprofile.makerSendApprove");
  public static final String CHECKER_APPROVE = conf.getString("tcbsprofile.checkerApprove");
  public static final String CHECKER_REJECT = conf.getString("tcbsprofile.checkerReject");
  public static final String UPLOAD_DOC = conf.getString("tcbsprofile.uploadDocFile");
  public static final String GET_FILE_LIST = conf.getString("tcbsprofile.getFileList");
  public static final String DELETE_DOC = conf.getString("tcbsprofile.deleteFile");
  public static final String UPLOAD_IDENTITY = conf.getString("tcbsprofile.uploadIdentity");
  public static final String CHECK_EXPIRY_DATE = conf.getString("tcbsprofile.checkExpiryDate");
  public static final String GET_ID_PLACE = conf.getString("tcbsprofile.getIdPlace");
  public static final String UPLOAD_STOCK_VSD = conf.getString("tcbsprofile.uploadStockVSD");
  public static final String UPLOAD_FUND_VSD = conf.getString("tcbsprofile.uploadFundVSD");
  public static final String SEARCH_CUSTOMER_INFO_BE = conf.getString("tcbsprofile.searchCustomerInfoBE");
  public static final String UPLOAD_DOC_CLOSE = conf.getString("tcbsprofile.uploadDocFileClose");
  public static final String GET_DOC_CLOSE = conf.getString("tcbsprofile.getDocFileClose");
  public static final String DELETE_DOC_CLOSE = conf.getString("tcbsprofile.deleteDocFileClose");
  public static final String DOWNLOAD_DOC_CLOSE = conf.getString("tcbsprofile.downloadDocFileClose");
  public static final String ADD_BLACK_LIST = conf.getString("tcbsprofile.addBlackList");
  public static final String VALIDATION_TOKEN_BLACK_LIST = conf.getString("tcbsprofile.validationToken");
  public static final String EXT_ADD_BLACK_LIST = conf.getString("tcbsprofile.extAddBlackList");
  public static final String BACK_ADD_BLACK_LIST = conf.getString("tcbsprofile.backAddBlackList");
  public static final String EXT_GET_SESSION_LIST = conf.getString("tcbsprofile.extGetSessionList");
  public static final String BACK_GET_SESSION_LIST = conf.getString("tcbsprofile.backGetSessionList");

  public static final String FMB_CHECK_ACCOUNT_EXIST = conf.getString("tcbsprofile.fmbCheckAccountExist");
  public static final String FMB_REGISTER_BASIC = conf.getString("tcbsprofile.fmbCreateBetaUser");
  public static final String FMB_UPGRADE_ADVANCED = conf.getString("tcbsprofile.fmbUpgradeAdvanced");
  public static final String FMB_VIEW_CONTRACT = conf.getString("tcbsprofile.fmbViewContract");
  public static final String FMB_SIGN_CONTRACT = conf.getString("tcbsprofile.fmbSignContract");
  public static final String CLEAR_CACHE_REDIS = conf.getString("tcbsprofile.clearCacheRedis");
  public static final String PROD_FIND_CUSTOMER_OPS = conf.getString("tcbsprofile.findCustomerOps");
  public static final String FMB_X_API_KEY = conf.getString("tcbsprofile.x-api-key");
  public static final String FMB_LIST_PROVINCE = conf.getString("tcbsprofile.fmbListProvinces");
  public static final String FMB_REGISTER_IA = conf.getString("tcbsprofile.fmbRegisterIA");
  public static final String BANKLIST_X_API_KEY = conf.getString("tcbsprofile.x-api-key-bankList");
  public static final String PROFILE_X_API_KEY = conf.getString("tcbsprofile.profile-x-api-key");
  public static final String LIST_PROVINCE_BE = conf.getString("tcbsprofile.listProvincesBE");
  public static final String LIST_PROVINCE_INT = conf.getString("tcbsprofile.listProvincesInt");
  public static final String ADD_BLACKLIST_X_API_KEY = conf.getString("tcbsprofile.blacklist-x-api-key");
  public static final String VALIDATION_TOKEN_X_API_KEY = conf.getString("tcbsprofile.validation-token-x-api-key");
  public static final String MULTIIA_TCBSID_X_API_KEY = conf.getString("tcbsprofile.multi-tcbsid-x-api-key");
  public static final String MULTIIA_USERNAME_X_API_KEY = conf.getString("tcbsprofile.multi-username-x-api-key");
  public static final String MULTIIA_PROFILE_R_X_API_KEY = conf.getString("tcbsprofile.multi-on-profile-r-x-api-key");
  public static final String CORPORATE_CHECK_ACCOUNT_EXIST = conf.getString("tcbsprofile.corpCheckAccountExist");

  public static final String OPEN_DERIVATIVE_ACCOUNT = conf.getString("tcbsprofile.openDerivativeAccount");
  public static final String STATUS_DERIVATIVE_ACCOUNT = conf.getString("tcbsprofile.statusDerivativeAccount");
  public static final String CHECK_CLOSE_DERIVATIVE_ACCOUNT = conf.getString("tcbsprofile.checkCloseDerivativeAccount");
  public static final String CLOSE_DERIVATIVE_ACCOUNT = conf.getString("tcbsprofile.closeDerivativeAccount");
  public static final String GET_LIST_DERIVATIVE_STATUS = conf.getString("tcbsprofile.getListDerivativeStatus");
  public static final String GET_MSG_SENT_TO_DERIVATIVE = conf.getString("tcbsprofile.getMsgSentToDerivative");
  public static final String SEND_CUSTOM_MSG_TO_DERIVATIVE = conf.getString("tcbsprofile.sendCustomMsgToDerivative");
  public static final String STOP_SEND_MSG_TO_DERIVATIVE = conf.getString("tcbsprofile.stopSendMsgToDerivative");
  public static final String SMS_TO_CUS_STOP_SEND_MSG_TO_DERIVATIVE = conf.getString("tcbsprofile.smsToCusStopSendMsgToDerivative");

  public static final String OCR_WEBHOOK = conf.getString("tcbsprofile.ocrWebHook");
  public static final String OCR_SIGNATURE_TOKEN = conf.getString("tcbsprofile.ocrSignatureToken");
  public static final String CHECKER_OCR_MODIFY_TASK = conf.getString("tcbsprofile.checkerOcrModifyTask");

  public static final String TCBSPROFILE_RM_OPEN_ACCOUNT = conf.getString("tcbsprofile.rmOpenAccount");
  public static final String TCBSPROFILE_RM_CONFIRM_ACCOUNT = conf.getString("tcbsprofile.rmConfirmAccount");
  public static final String DELETE_CACHE = conf.getString("tcbsprofile.deleteCache");
  public static final String HANDLE_DUPLICATE_RM_EMAIL = conf.getString("tcbsprofile.handleDuplicateRmEmail");

  public static final String ADD_REPORT_DOC_INT = conf.getString("tcbsprofile.addReportDocInt");
  public static final String GET_ACTION_DOC_INT = conf.getString("tcbsprofile.getActionDocInt");
  public static final String GET_WBL_BY_TCBSID = conf.getString("tcbsprofile.getWblByTcbsId");
  public static final String GET_WBL_BY_POLICYCODE_INT = conf.getString("tcbsprofile.getWblByPolicyCodeInt");
  public static final String ADD_USER_TO_WBL_LIST = conf.getString("tcbsprofile.addUserToWblList");
  public static final String DELETE_USER_FROM_WBL_LIST = conf.getString("tcbsprofile.deleteUserFromWblList");
  public static final String UPDATE_USER_TO_WBL_LIST = conf.getString("tcbsprofile.updateUserToWblList");
  public static final String GET_BUSINESS_TITLE_LIST = conf.getString("tcbsprofile.getBusinessTitleList");
  public static final String GET_ACTION_ORG_LIST = conf.getString("tcbsprofile.getActionOrgList");
  public static final String GET_WBL_USER_DETAIL = conf.getString("tcbsprofile.getWblUserDetail");
  public static final String GET_WBL_USER_LIST = conf.getString("tcbsprofile.getWblUserList");
  public static final String ADD_USER_TO_WBL_LIST_FUND = conf.getString("tcbsprofile.addUserToWblListFund");
  public static final String DELETE_USER_FROM_WBL_LIST_FUND = conf.getString("tcbsprofile.deleteUserFromWblListFund");
  public static final String UPDATE_USER_TO_WBL_LIST_FUND = conf.getString("tcbsprofile.updateUserToWblListFund");

  public static final String TCBSPROFILE_SPECIALWBLKEY = conf.getString("tcbsprofile.specialWblKey");
  public static final String TCBSPROFILE_NORMALWBLKEY = conf.getString("tcbsprofile.normalWblKey");
  public static final String TCBSPROFILE_CUSTODY_MAKER = conf.getString("tcbsprofile.custodyMaker");

  public static final String CALCULATE_SLA_FOR_OB = conf.getString("tcbsprofile.calculateSlaForOb");
  public static final String GET_REJECT_REASON_BY_USER_ID = conf.getString("tcbsprofile.getRejectReasonByUserId");

  public static final String SUGGEST_FANCY_105C = conf.getString("tcbsprofile.suggestFancy105C");
  public static final String CONFIRM_BOOKING_FANCY_105C = conf.getString("tcbsprofile.confirmBookingFancy105C");

  public static final String GET_BANK_SUBACCOUNT_BY_USERNAME = conf.getString("tcbsprofile.getBankSubAccountByUserName");
  public static final String CLEAR_BANK_SUBACCOUNT_STATUS_BY_USERNAME = conf.getString("tcbsprofile.clearBankSubAccountStatusByUserName");
  public static final String CREATE_NEW_PROFILE_DOCUMENT = conf.getString("tcbsprofile.createNewProfileDocument");
  public static final String UPDATE_PROFILE_DOCUMENT_BY_ID = conf.getString("tcbsprofile.updateProfileDocumentById");
  public static final String PRO_TRADER_DOCUMENT = conf.getString("tcbsprofile.proTraderDocument");
  public static final String CHECK_DUPLICATE_BANK_INFO = conf.getString("tcbsprofile.checkDuplicateBankInfo");
  public static final String REGISTER_VERIFICATION = conf.getString("tcbsprofile.registerVerification");
  public static final String UPDATE_PROFILE_TCI3 = conf.getString("tcbsprofile.updateProfileTci3");

  public static final String MULTI_CONNECT_IA_ISAVE = conf.getString("tcbsprofile.multiConnectIsiSave");
  public static final String MULTI_DISCONNECT_IA_ISAVE = conf.getString("tcbsprofile.multiDisconnectIsiSave");

  //thirty party
  public static final String FSS_SERVICE = conf.getString("tcbsprofile.fssService");

  // regIA CTG
  public static final String INQUIRY_DISCONNECT_IA_CTG = conf.getString("tcbsprofile.inquiryDisconnectIa");
  public static final String AUTO_RETRY_TASK = conf.getString("tcbsprofile.autoRetryTask");
  public static final String FLEX_ACCOUNT_INFO = conf.getString("tcbsprofile.flexAccountInfo");
  public static final String GET_INFO_IA_TCI3 = conf.getString("tcbsprofile.getInfoIaTci3");
  public static final String DISCONNECT_IA_TCI3 = conf.getString("tcbsprofile.disconnectIaTci3");
  public static final String UPDATE_IA_TCI3 = conf.getString("tcbsprofile.updateIaTci3");
  public static final String GET_INFO_IA_BATCH = conf.getString("tcbsprofile.getInfoIaBatch");
  public static final String GET_INFO_IA_SINGLE_EXT = conf.getString("tcbsprofile.getInfoIaSingleExt");
  public static final String RETRY_OTP_REGISTER_IA = conf.getString("tcbsprofile.retryOtpRegisterIa");

  // iVoucher domain
  public static final String IVOUCHER_DOMAIN = conf.getString("tcbsprofile.ivoucher-api-tool");

  public static final String ASSIGN_TASK_TO_AMOPS_MAKER = conf.getString("tcbsprofile.assignTaskToAMOPsMaker");
  public static final String INTERNAL_PROINVESTOR = conf.getString("tcbsprofile.internalProinvestorToken");

  //TCCChecker Removal
  public static final String MAKER_APPROVE_AFTER_OCR_SCAN = conf.getString("tcbsprofile.makerApproveOCR");

  //Proinvestor
  public static final String AMOPS_DEACTIVATE_PRO_INVESTOR = conf.getString("tcbsprofile.amopsDeacivateProinvestor");
  public static final String GET_PROINVESTOR_PROFILE_BY_TCBSID = conf.getString("tcbsprofile.getProinvestorProfileByTcbsid");
  public static final String GET_PROINVESTOR_PROFILE_BY_105C = conf.getString("tcbsprofile.getProinvestorprofileBy105code");
  public static final String GET_PROINVESTOR_END_DATE = conf.getString("tcbsprofile.getProInvestorEndDate");
  public static final String PRO_TRADER_CHECK = conf.getString("tcbsprofile.proTraderCheck");
  public static final String SYNC_PROINVESTOR_FROM_ANI = conf.getString("tcbsprofile.syncProInvestorFromAni");


  //RMRBO
  public static final String TCBS_USER_TOKEN = conf.getString("tcbsprofile.user_token1");
  public static final String SET_VIEW_ASSET_STATUS = conf.getString("tcbsprofile.setStatusViewAsset");
  public static final String GET_VIEW_ASSET_STATUS = conf.getString("tcbsprofile.getViewStatusAssetStatus");
  public static final String GET_RM_RBO_LIST_WITH_PAGINATION = conf.getString("tcbsprofile.getRMRBOList");
  public static final String GET_RM_RBO_IDENTIFICATION_INT = conf.getString("tcbsprofile.getRMRBOIdentificationInt");
  public static final String GET_RM_RBO_IDENTIFICATION_EXT = conf.getString("tcbsprofile.getRMRBOIdentificationExt");
  public static final String GET_IDENTIFICATION_BATCH_BY_CUSTODYCD = conf.getString("tcbsprofile.getIdentificationBatchByCustodyCd");
  public static final String GET_IDENTIFICATION_BATCH_BY_TCBSID = conf.getString("tcbsprofile.getIdentificationBatchByTcbsId");
  public static final String GET_CUSTOMER_VIEW_ASSET = conf.getString("tcbsprofile.getCustomerViewAssetData");

  //authen login
  public static final String LOGIN_TO_TCI3 = conf.getString("tcbsprofile.loginToTci3");
  public static final String AUTHEN_GET_CLIENT_KEY = conf.getString("tcbsprofile.authenGetClientKey");
  public static final String LOGIN_FROM_THIRD_PARTY = conf.getString("tcbsprofile.loginFromThirdParty");
  public static final String ADD_OTP = conf.getString("tcbsprofile.addOtp");
  public static final String GEN_AUTHEN_KEY = conf.getString("tcbsprofile.genAuthenKey");
  public static final String GEN_LOGIN_KEY = conf.getString("tcbsprofile.genLoginKey");
  public static final String OAUTH2_GET_AUTHORIZATION_CODE = conf.getString("tcbsprofile.authen.oauth2.getAuthorizationCode");
  public static final String OAUTH2_GET_ACCESS_TOKEN_FROM_AUTHORIZATION_CODE = conf.getString("tcbsprofile.authen.oauth2.getAccessTokenFromAuthorizationCode");
  public static final String OAUTH2_GET_ACCESS_TOKEN_FROM_REFRESH_TOKEN = conf.getString("tcbsprofile.authen.oauth2.getAccessTokenFromRefreshToken");
  public static final String OAUTH2_GET_SERVICE_TOKEN = conf.getString("tcbsprofile.authen.oauth2.getServiceToken");

  //author
  public static final String LOGIN_TO_ISQUARE = conf.getString("tcbsprofile.loginToIsquare");
  public static final String UPDATE_SESSION = conf.getString("tcbsprofile.updateSession");
  public static final String UPDATE_SESSION_HB = conf.getString("tcbsprofile.updateSessionHB");
  public static final String UPDATE_SESSION_HB_TOKEN = conf.getString("tcbsprofile.authen.hb.token");
  public static final String UPDATE_SESSION_WT = conf.getString("tcbsprofile.updateSessionWT");
  public static final String UPDATE_SESSION_WT_TOKEN = conf.getString("tcbsprofile.authen.wt.token");

  // author
  public static final String AUTHOR_VALIDATION = conf.getString("tcbsprofile.authorValidation");
  public static final String OTP_HB_DOMAIN = conf.getString("tcbsprofile.otpHbDomain");
  public static final String OTP_WT_DOMAIN = conf.getString("tcbsprofile.otpWtDomain");

  //DerivativeAccountForOrganization
  public static final String OPEN_DERIVATIVE_ACCOUNT_FOR_ORGANIZATION = conf.getString("tcbsprofile.openDerivativeAccountForOrganization");

  //BPM GetCustomerInformation
  public static final String GET_CUSTOMER_INFORMATION = conf.getString("tcbsprofile.getCustomerInformation");
  public static final String TRIGGER_UPDATE_INFO = conf.getString("tcbsprofile.triggerUpdateInfo");
  public static final String START_TASK_CUS_CHANGE_INFOR = conf.getString("tcbsprofile.startTaskCusChangeInfor");

  //Profile Upload File
  public static final String UPLOAD_FILE = conf.getString("tcbsprofile.uploadFile");

  //Profile admin
  public static final String TRACKING_POINT_CHANGE_INFO_PROCESS = conf.getString("tcbsprofile.trackingPointChangeInfoProcess");

  //Istock sync subaccounts
  public static final String CALL_ISTOCK_GET_SUBACCOUNT = conf.getString("tcbsprofile.callIstockToGetSubaccount");
  public static final String SYNC_SUBACCOUNT_AND_CONNECT_IA = conf.getString("tcbsprofile.syncSubaccountAndConnectIA");

  //STPFund
  public static final String GET_MODE_STP_FUND = conf.getString("stpfund.get-mode-stp-fund");
  public static final String SET_MODE_STP_FUND = conf.getString("stpfund.set-mode-stp-fund");
  public static final String STP_AUTHORIZATION_KEY = conf.getString("tcbsprofile.stp-fund-token");
  public static final String GET_STP_TRANSACTION_LIST = conf.getString("stpfund.get-stp-transaction-list");
  public static final String CANCEL_STP_TRANSACTION = conf.getString("stpfund.cancel-stp-transaction");
  public static final String UPDATE_VSD_OF_STP_TRANSACTION = conf.getString("stpfund.update-vsd-of-stp-transaction");
  public static final String IMPORT_FILE_UPDATE_VSD = conf.getString("stpfund.import-file-update-vsd");
  public static final String RETRY_STP_TRANSACTION = conf.getString("stpfund.retry-stp-transaction");
  public static final String RETRY_MANY_TRANSACTIONS = conf.getString("stpfund.retry-many-transactions");
  public static final String GET_STP_TRANSACTION_DETAILS = conf.getString("stpfund.get-stp-transaction-details");
  public static final String SUPPORT_BY_ACTION = conf.getString("stpfund.complete-online-authentication");
  public static final String STP_FUND_CLOSE_ACCOUNT = conf.getString("stpfund.stpfund-close-account");
  public static final String STP_X_API_KEY = conf.getString("tcbsprofile.stp-fund-api-key");
  public static final String GET_ACCOUNT_ACTIVE_VSD = conf.getString("stpfund.get-account-active-vsd");


  //Change Personal Info
  public static final String CHANGE_PERSONAL_INFO = conf.getString("tcbsprofile.changePersonalInfo");
  public static final String COOKIE = conf.getString("tcbsprofile.cookie");
  public static final String COOKIE_NNB = conf.getString("tcbsprofile.cookie_nnb");

  //Resign Contract
  public static final String GET_CUSTOMER_LIST = conf.getString("tcbsprofile.getCustomerList");
  public static final String GET_CUSTOMER_PROFILE = conf.getString("tcbsprofile.getProfileOfCustomer");
  public static final String ADD_CUSTOMER_TO_LIST = conf.getString("tcbsprofile.addCustomerToList");
  public static final String REMOVE_CUSTOMER_FROM_LIST = conf.getString("tcbsprofile.removeCustomerFromList");
  public static final String HANDLE_AFTER_CHANGE_INFO = conf.getString("tcbsprofile.handleAfterChangeInfo");

  //SLA Caculating Logic
  public static final String SLA_CACULATE_LOGIC = conf.getString("tcbsprofile.slaCaculatingLogic");

  //Reminder
  public static final String TOKEN_REMINDER = conf.getString("tcbsprofile.tokenReminder");
  public static final String GET_REMINDER_SOURCE = conf.getString("tcbsprofile.getReminderSource");
  public static final String UPDATE_REMINDER_SOURCE = conf.getString("tcbsprofile.updateReminderSource");
  public static final String EXECUTE_REMINDER = conf.getString("tcbsprofile.executeReminder");

  //SSO
  public static final String LOGIN_GETKEY = conf.getString("tcbsprofile.loginGetKey");
  public static final String LOGIN_USEKEY = conf.getString("tcbsprofile.loginUsedKey");

  //iAngel
  public static final String CREATE_NEW_RELATION = conf.getString("tcbsprofile.createNewRelation");

  public static final String INTERNAL_GET_CUSTOMER_INFO = conf.getString("tcbsprofile.internalGetCusInfo");

  // Hold 105C
  public static final String GET_INFO_HOLD_105C = conf.getString("tcbsprofile.getInfoHold105C");
  public static final String VALIDATE_HOLD_105C = conf.getString("tcbsprofile.validateHold105C");
  public static final String CREATE_HOLD_105C = conf.getString("tcbsprofile.createHold105C");
  public static final String GET_INFO_REFER_CODE = conf.getString("tcbsprofile.getInfoReferCode");

  // RM/RBO
  public static final String GET_R3RD_USER_LIST = conf.getString("rmrbo.getR3rdUserList");
  public static final String RECEIVE_DATA_FROM_IANGELS = conf.getString("rmrbo.receiveFromIAngels");
  public static final String RMRBO_API_KEY = conf.getString("tcbsprofile.rmrbo-x-api-key");
  public static final String UPDATE_STATUS_ELEARNING = conf.getString("rmrbo.updateStatusELearning");
  public static final String ACTIVE_ROLE_BY_USERNAME = conf.getString("rmrbo.activeRoleByUsername");
  public static final String RMRBO_CHECKER_AUTHORIZATION_KEY = conf.getString("tcbsprofile.rmrbo-checker-authorization-token");
  public static final String RMRBO_MAKER_AUTHORIZATION_KEY = conf.getString("tcbsprofile.rmrbo-maker-authorization-token");
  public static final String VALIDATE_AND_EXPIRED = conf.getString("rmrbo.validateAndExpired");
  public static final String UPDATE_ROLE_VALIDATE_ROLE_INTO_WSO2 = conf.getString("rmrbo.updateRoleValidateRoleIntoWso2");
  public static final String UPDATE_USER_VALIDATE_ROLE_INTO_WSO2 = conf.getString("rmrbo.updateUserValidateRoleIntoWso2");
  public static final String GET_ALL_PARTNER = conf.getString("rmrbo.getAllPartner");
  public static final String GET_ROLE_BY_PARTNER = conf.getString("rmrbo.getRoleByPartner");
  public static final String SYNC_DATA_FROM_ANI = conf.getString("rmrbo.syncDataFromAnI");
  public static final String API_ANI_V2_DOMAIN = conf.getString("rmrbo.apiAniV2Domain");
  public static final String API_KEY_ANI_V2_GET_RM = conf.getString("rmrbo.apiKeyAniV2GetRM");
  public static final String API_KEY_ANI_V2_GET_RBO = conf.getString("rmrbo.apiKeyAniV2GetRBO");
  public static final String SEARCH_ROLE_RM_RBO = conf.getString("rmrbo.searchRole");
  public static final String VALIDATE_AND_ACTIVE_ROLE = conf.getString("rmrbo.validateAndActiveRole");
  public static final String WSO2_GET_LIST_ROLE_BY_USERNAME = conf.getString("wso2.getListRoleByUsername");
  public static final String GET_COURSE_CODE = conf.getString("rmrbo.getCourseCode");
  public static final String WSO2_UPDATE_ROLE_OF_USER = conf.getString("wso2.updateRoleOfUser");
  public static final String VIEW_ROLE_DETAIL = conf.getString("rmrbo.viewRoleDetail");
  public static final String VIEW_ELEARNING_DETAIL = conf.getString("rmrbo.viewElearningDetail");
  public static final String RMRBO_VIEW_TOKEN = conf.getString("tcbsprofile.rmrbo-view-token");
  public static final String RMRBO_GET_AGENCY_INFO = conf.getString("rmrbo.getAgencyInfo");
  public static final String RMRBO_BACKEND_UPDATE_ROLE = conf.getString("rmrbo.backendUpdateRole");
  public static final String EXPORT_ROLE_RM_RBO = conf.getString("rmrbo.exportRole");
  public static final String EXPORT_ROLE_RM_RBO_TOKEN = conf.getString("tcbsprofile.exportRole-token");
  public static final String RM_RBO_HISTORY_VIEW = conf.getString("rmrbo.rmrbo-history-view");
  public static final String GET_NNB_BY_FUND = conf.getString("tcbsprofile.getNnbByFundList");
  public static final String GET_CUSTOMER_BY_RBO = conf.getString("rmrbo.getCustomerByRbo");
  public static final String GET_HISTORY_ACTIVE_INACTIVE = conf.getString("rmrbo.getHistoryActiveInactive");
  public static final String ADD_HISTORY_ACTIVE_INACTIVE_IWP = conf.getString("rmrbo.addHistoryActiveInactiveIwp");

  //FORGOT PASSWORD
  public static final String FORGOT_PASSWORD_PHONE = conf.getString("tcbsprofile.forgotPasswordPhone");
  public static final String FORGOT_PASSWORD_EMAIL = conf.getString("tcbsprofile.forgotPasswordEmail");
  public static final String FORGOT_PASSWORD_NOTIFY = conf.getString("tcbsprofile.forgotPasswordNotify");
  public static final String FORGOT_PASSWORD_CONFIRM_PHONE = conf.getString("tcbsprofile.forgotPasswordConfirmPhone");
  public static final String FORGOT_PASSWORD_VALIDATE = conf.getString("tcbsprofile.forgotPasswordValidate");

  //OTP Global Phone
  public static final String OTP_GET_LIST_PHONE_CODE = conf.getString("otpGlobalPhone.getListPhoneCode");

  //new onboarding 2022
  public static final String API_VALIDATE_PHONE_EMAIL = conf.getString("newonboarding2022.validatePhoneEmail");
  public static final String REGISTER_CONFIRM_PHONE = conf.getString("newonboarding2022.registerConfirmPhone");
  public static final String REGISTER_OCR_GET_DATA = conf.getString("newonboarding2022.registerOcrGetData");
  public static final String REGISTER_VALIDATE_PASSWORD = conf.getString("newonboarding2022.registerValidatePassWord");
  public static final String REGISTER_VALIDATE_BANK_INFO = conf.getString("newonboarding2022.registerValidateBankInfo");
  public static final String REGISTER_VALIDATE_IDENTITY_INFO = conf.getString("newonboarding2022.registerValidateIdentityInfo");
  public static final String REGISTER_UPLOAD_IDENTIFY = conf.getString("newonboarding2022.registerUploadIdentify");
  public static final String REGISTER_UPLOAD_OTHER = conf.getString("newonboarding2022.registerUploadOther");
  public static final String OB_REGISTER = conf.getString("newonboarding2022.obRegister");


  //new onboarding partership
  public static final String PARTNER_CHECK_ACCOUNT_EXIST = conf.getString("newonboardingPartnerShip.checkAccountExist");
  public static final String PARTNERSHIP_ACCOUNT_LINK = conf.getString("newonboardingPartnerShip.partnerShipAccountLink");
  public static final String PARTNERSHIP_X_API_KEY = conf.getString("tcbsprofile.partnerShip-x-api-key");
  public static final String PARTNERSHIP_ACCOUNT_UNLINK = conf.getString("newonboardingPartnerShip.partnerShipAccountUnLink");
  public static final String PARTNERSHIP_VIEW_CONTRACT = conf.getString("newonboardingPartnerShip.partnerShipViewContract");
  public static final String PARTNERSHIP_SIGN_CONTRACT = conf.getString("newonboardingPartnerShip.partnerShipSignContract");
  public static final String PARTNERSHIP_CONFIRM = conf.getString("newonboardingPartnerShip.partnerShipConfirm");
  public static final String GET_INFO_BY_USERNAME = conf.getString("newonboardingPartnerShip.getInfoByUsername");
  public static final String RETURN_INFO_SOCIAS = conf.getString("newonboardingPartnerShip.returnInfoForSocias");
  public static final String PARTNERSHIP_GET_HISTORY_IA = conf.getString("newonboardingPartnerShip.partnerShipGetHistoryIa");
  public static final String PARTNERSHIP_GET_DETAIL = conf.getString("newonboardingPartnerShip.partnerShipGetDetail");
  public static final String TCBS_ACCOUNT_CONFIRM = conf.getString("newonboardingPartnerShip.tcbsConfirmId");
  public static final String TCBS_ACCOUNT_LINK = conf.getString("newonboardingPartnerShip.tcbsAccountLink");
  public static final String OPEN_ACCOUNT_PARTNER_VIEW_CONTRACT = conf.getString("newonboardingPartnerShip.openAccountPartnerViewContract");
  public static final String OPEN_ACCOUNT_PARTNER_SIGN_CONTRACT = conf.getString("newonboardingPartnerShip.openAccountPartnerSignContract");
  public static final String OPEN_ACCOUNT_PARTNER_REGISTER = conf.getString("newonboardingPartnerShip.openAccountPartnerRegister");
  public static final String OPEN_ACCOUNT_PARTNER_SEND_OTP = conf.getString("newonboardingPartnerShip.openAccountPartnerSendOtp");

  //Additional API
  public static final String SUGGEST_ID_PLACE = conf.getString("tcbsprofile.suggestIdPlace");
  public static final String GET_LIST_COUNTRY = conf.getString("tcbsprofile.getListCountry");
  public static final String GET_LIST_PROVINCE_BY_COUNTRY = conf.getString("tcbsprofile.getListProvinceByCountryCode");
  public static final String GET_LIST_BANK_INFO = conf.getString("tcbsprofile.getListBankInfo");
  public static final String SEARCH_BY_CONDITION = conf.getString("tcbsprofile.searchByCondition");
  public static final String GET_INFO_BY_CONDITION = conf.getString("tcbsprofile.getInfoByCondition");
  public static final String UPDATE_INFO_CUSTOMER = conf.getString("tcbsprofile.updateInfoCustomer");

  //BAU Tool
  public static final String BAU_AUTHORIZATION_TOKEN = conf.getString("tcbsprofile.bau-authorization-token");
  public static final String BAU_SEARCH_BANK_IA_INFO = conf.getString("bauTool.searchBankIaInfo");
  public static final String GET_TOOL_BAU_LIST = conf.getString("bauTool.getToolBauList");
  public static final String SEARCH_BAU_HISTORY = conf.getString("bauTool.searchBauHistory");
  public static final String ACTION_TOOL_BAU = conf.getString("bauTool.actionToolBau");

  // check contract
  public static final String JOB_CHECK_CONTRACT = conf.getString("tcbsprofile.jobCheckContract");

  // corporation
  public static final String CORPORATE_X_API_KEY = conf.getString("tcbsprofile.corporate-x-api-key");
  public static final String CORPORATE_REGISTER_BETA = conf.getString("tcbsprofile.corporateRegisterBeta");

}
