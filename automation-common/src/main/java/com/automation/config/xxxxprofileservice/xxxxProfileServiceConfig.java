package com.automation.config.xxxxprofileservice;

import com.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class xxxxProfileServiceConfig {
  private static final Config conf = new ConfigImpl("xxxxprofileservice").getConf();

  public static final String xxxxPROFILE_DOMAIN = conf.getString("xxxxprofile.internal-api-v1");
  public static final String xxxxPROFILE_DEV_DOMAIN = conf.getString("xxxxprofile.dev.domain");

  public static final String xxxxPROFILE_PUB_API = conf.getString("xxxxprofile.pub-api");
  public static final String xxxxPROFILE_OLD_REGISTER = conf.getString("xxxxprofile.oldRegister");
  public static final String xxxxPROFILE_API = conf.getString("xxxxprofile.api");
  public static final String xxxxPROFILE_REGISTER = conf.getString("xxxxprofile.register");
  public static final String xxxxPROFILE_QUEUE_UPDATE = conf.getString("xxxxprofile.queueUpdate");
  public static final String xxxxPROFILE_PENDING = conf.getString("xxxxprofile.pending");
  public static final String xxxxPROFILE_ACTIVATE = conf.getString("xxxxprofile.activate");
  public static final String xxxxPROFILE_AUTO_RETRY = conf.getString("xxxxprofile.autoRetry");
  public static final String ASSIGN_TASK_TO_MAKER_OR_CHECKER = conf.getString("xxxxprofile.assignTask");
  public static final String ASSIGN_TASK_TO_MAKER_KEY = conf.getString("xxxxprofile.assignTaskToMakerKey");
  public static final String ASSIGN_TASK_TO_CHECKER_KEY = conf.getString("xxxxprofile.assignTaskToCheckerKey");
  public static final String xxxxPROFILE_INQUIRYGROUPINFOKEY = conf.getString("xxxxprofile.inquiryGroupInfoKey");
  public static final String xxxxPROFILE_BACKENDWBLKEY = conf.getString("xxxxprofile.backEndWblKey");
  public static final String xxxxPROFILE_TOKENKEY = conf.getString("xxxxprofile.tokenKey");
  public static final String xxxxPROFILE_AUTHORIZATION = conf.getString("xxxxprofile.authorization");
  public static final String xxxxPROFILE_KYC_HISTORY = conf.getString("xxxxprofile.getKycHistory");
  public static final String xxxxPROFILE_GET_TASK_KYC = conf.getString("xxxxprofile.getTaskKYC");
  public static final String xxxxPROFILE_GET_DETAIL_TASK = conf.getString("xxxxprofile.getDetailTask");
  public static final String xxxxPROFILE_BACKENDCAMPAIGN = conf.getString("xxxxprofile.backEndCampaign");
  public static final String xxxxPROFILE_GET_TASK_HISTORY = conf.getString("xxxxprofile.getTaskHistory");
  public static final String xxxxPROFILE_UPDATE_BANK_ACCOUNT = conf.getString("xxxxprofile.updateBankAccount");
  public static final String xxxxPROFILE_OPEN_ACCOUNT_EXTERNAL = conf.getString("xxxxprofile.openAccountExternal");
  public static final String xxxxPROFILE_COMPLETE_INFO_IA = conf.getString("xxxxprofile.completeInfoIA");
  public static final String GET_CAMPAIGN_LIST = conf.getString("xxxxprofile.getCampaignList");
  public static final String GEN_CHANGE_INFO_REPORT = conf.getString("xxxxprofile.genChangeInfoReport");
  public static final String RM_PRINT_CONTRACT = conf.getString("xxxxprofile.rmPrintContract");
  public static final String RM_DOWNLOAD_SINGED_CONTRACT = conf.getString("xxxxprofile.rmDownloadSignedContract");
  public static final String CHECK_BANK_ACCOUNT_OWNER = conf.getString("xxxxprofile.checkBankAccountOwner");
  public static final String GET_STATUS_CHECK_BANK_ACCOUNT_OWNER = conf.getString("xxxxprofile.getStatusCheckBankAccountOwner");
  public static final String GET_BANK_LIST = conf.getString("xxxxprofile.getBankList");
  public static final String BANK_SYNC_H2H = conf.getString("xxxxprofile.bankSyncH2H");
  public static final String INQUIRY_REFERRAL_CODE = conf.getString("xxxxprofile.inquiryReferralCode");
  public static final String FORCE_SYNC_DERIVATIVE = conf.getString("xxxxprofile.forceSyncDerivative");
  public static final String CHECK_AM_LOCK = conf.getString("xxxxprofile.checkAMLock");
  public static final String GET_AMLOCK_HISTORY = conf.getString("xxxxprofile.getAMLockHistory");
  public static final String AMLOCK_KEY = conf.getString("xxxxprofile.amLock-key");
  public static final String GEN_MBLK5_WITH_QRCODE = conf.getString("xxxxprofile.genMblk5WithQrCode");
  public static final String CLOSE_ACCOUNT_UPLOAD_DOC = conf.getString("xxxxprofile.accountCloseUploadDocument");
  public static final String CLOSE_ACCOUNT_GET_DOC = conf.getString("xxxxprofile.accountCloseGetDocument");
  public static final String CLOSE_ACCOUNT_DELETE_DOC = conf.getString("xxxxprofile.accountCloseDeleteDocument");
  public static final String CLOSE_ACCOUNT_DOWNLOAD_DOC = conf.getString("xxxxprofile.accountCloseDownloadDocument");
  public static final String BACK_ADD_TO_BLACK_LIST = conf.getString("xxxxprofile.backAddToBlackList");
  public static final String MULTI_GET_BY_xxxxID = conf.getString("xxxxprofile.multiGetByxxxxId");
  public static final String MULTI_GET_BY_USERNAME = conf.getString("xxxxprofile.multiGetByUsername");
  public static final String MULTI_GET_ON_PROFILE_R = conf.getString("xxxxprofile.multiGetOnProfileR");
  public static final String GET_BANK_LIST_KYC_TASK = conf.getString("xxxxprofile.getBankListKycTask");

  public static final String GEN_CONTRACT_API = conf.getString("xxxxprofile.genContract");
  public static final String SIGN_CONTRACT_API = conf.getString("xxxxprofile.signContract");
  public static final String GET_ACCOUNT_STATUS_API = conf.getString("xxxxprofile.getAccountStatus");
  public static final String GET_CUSTOMER_INFO = conf.getString("xxxxprofile.getCustInfo");
  public static final String API_KEY = conf.getString("xxxxprofile.api-key");
  public static final String GEN_FINAL_CONTRACT = conf.getString("xxxxprofile.genFinalContract");
  public static final String VIEW_CLOSE_ACCOUNT_CONTRACT = conf.getString("xxxxprofile.viewCloseAccountContract");
  public static final String SIGN_CLOSE_ACCOUNT_CONTRACT = conf.getString("xxxxprofile.signCloseAccountContract");

  public static final String MAKER_MODIFY = conf.getString("xxxxprofile.makerModify");
  public static final String MAKER_REJECT = conf.getString("xxxxprofile.makerReject");
  public static final String MAKER_SEND_APPROVE = conf.getString("xxxxprofile.makerSendApprove");
  public static final String CHECKER_APPROVE = conf.getString("xxxxprofile.checkerApprove");
  public static final String CHECKER_REJECT = conf.getString("xxxxprofile.checkerReject");
  public static final String UPLOAD_DOC = conf.getString("xxxxprofile.uploadDocFile");
  public static final String GET_FILE_LIST = conf.getString("xxxxprofile.getFileList");
  public static final String DELETE_DOC = conf.getString("xxxxprofile.deleteFile");
  public static final String UPLOAD_IDENTITY = conf.getString("xxxxprofile.uploadIdentity");
  public static final String CHECK_EXPIRY_DATE = conf.getString("xxxxprofile.checkExpiryDate");
  public static final String GET_ID_PLACE = conf.getString("xxxxprofile.getIdPlace");
  public static final String UPLOAD_STOCK_VSD = conf.getString("xxxxprofile.uploadStockVSD");
  public static final String UPLOAD_FUND_VSD = conf.getString("xxxxprofile.uploadFundVSD");
  public static final String SEARCH_CUSTOMER_INFO_BE = conf.getString("xxxxprofile.searchCustomerInfoBE");
  public static final String UPLOAD_DOC_CLOSE = conf.getString("xxxxprofile.uploadDocFileClose");
  public static final String GET_DOC_CLOSE = conf.getString("xxxxprofile.getDocFileClose");
  public static final String DELETE_DOC_CLOSE = conf.getString("xxxxprofile.deleteDocFileClose");
  public static final String DOWNLOAD_DOC_CLOSE = conf.getString("xxxxprofile.downloadDocFileClose");
  public static final String ADD_BLACK_LIST = conf.getString("xxxxprofile.addBlackList");
  public static final String VALIDATION_TOKEN_BLACK_LIST = conf.getString("xxxxprofile.validationToken");
  public static final String EXT_ADD_BLACK_LIST = conf.getString("xxxxprofile.extAddBlackList");
  public static final String BACK_ADD_BLACK_LIST = conf.getString("xxxxprofile.backAddBlackList");
  public static final String EXT_GET_SESSION_LIST = conf.getString("xxxxprofile.extGetSessionList");
  public static final String BACK_GET_SESSION_LIST = conf.getString("xxxxprofile.backGetSessionList");

  public static final String FMB_CHECK_ACCOUNT_EXIST = conf.getString("xxxxprofile.fmbCheckAccountExist");
  public static final String FMB_REGISTER_BASIC = conf.getString("xxxxprofile.fmbCreateBetaUser");
  public static final String FMB_UPGRADE_ADVANCED = conf.getString("xxxxprofile.fmbUpgradeAdvanced");
  public static final String FMB_VIEW_CONTRACT = conf.getString("xxxxprofile.fmbViewContract");
  public static final String FMB_SIGN_CONTRACT = conf.getString("xxxxprofile.fmbSignContract");
  public static final String CLEAR_CACHE_REDIS = conf.getString("xxxxprofile.clearCacheRedis");
  public static final String PROD_FIND_CUSTOMER_OPS = conf.getString("xxxxprofile.findCustomerOps");
  public static final String FMB_X_API_KEY = conf.getString("xxxxprofile.x-api-key");
  public static final String FMB_LIST_PROVINCE = conf.getString("xxxxprofile.fmbListProvinces");
  public static final String FMB_REGISTER_IA = conf.getString("xxxxprofile.fmbRegisterIA");
  public static final String BANKLIST_X_API_KEY = conf.getString("xxxxprofile.x-api-key-bankList");
  public static final String PROFILE_X_API_KEY = conf.getString("xxxxprofile.profile-x-api-key");
  public static final String LIST_PROVINCE_BE = conf.getString("xxxxprofile.listProvincesBE");
  public static final String LIST_PROVINCE_INT = conf.getString("xxxxprofile.listProvincesInt");
  public static final String ADD_BLACKLIST_X_API_KEY = conf.getString("xxxxprofile.blacklist-x-api-key");
  public static final String VALIDATION_TOKEN_X_API_KEY = conf.getString("xxxxprofile.validation-token-x-api-key");
  public static final String MULTIIA_xxxxID_X_API_KEY = conf.getString("xxxxprofile.multi-xxxxid-x-api-key");
  public static final String MULTIIA_USERNAME_X_API_KEY = conf.getString("xxxxprofile.multi-username-x-api-key");
  public static final String MULTIIA_PROFILE_R_X_API_KEY = conf.getString("xxxxprofile.multi-on-profile-r-x-api-key");
  public static final String CORPORATE_CHECK_ACCOUNT_EXIST = conf.getString("xxxxprofile.corpCheckAccountExist");

  public static final String OPEN_DERIVATIVE_ACCOUNT = conf.getString("xxxxprofile.openDerivativeAccount");
  public static final String STATUS_DERIVATIVE_ACCOUNT = conf.getString("xxxxprofile.statusDerivativeAccount");
  public static final String CHECK_CLOSE_DERIVATIVE_ACCOUNT = conf.getString("xxxxprofile.checkCloseDerivativeAccount");
  public static final String CLOSE_DERIVATIVE_ACCOUNT = conf.getString("xxxxprofile.closeDerivativeAccount");
  public static final String GET_LIST_DERIVATIVE_STATUS = conf.getString("xxxxprofile.getListDerivativeStatus");
  public static final String GET_MSG_SENT_TO_DERIVATIVE = conf.getString("xxxxprofile.getMsgSentToDerivative");
  public static final String SEND_CUSTOM_MSG_TO_DERIVATIVE = conf.getString("xxxxprofile.sendCustomMsgToDerivative");
  public static final String STOP_SEND_MSG_TO_DERIVATIVE = conf.getString("xxxxprofile.stopSendMsgToDerivative");
  public static final String SMS_TO_CUS_STOP_SEND_MSG_TO_DERIVATIVE = conf.getString("xxxxprofile.smsToCusStopSendMsgToDerivative");

  public static final String OCR_WEBHOOK = conf.getString("xxxxprofile.ocrWebHook");
  public static final String OCR_SIGNATURE_TOKEN = conf.getString("xxxxprofile.ocrSignatureToken");
  public static final String CHECKER_OCR_MODIFY_TASK = conf.getString("xxxxprofile.checkerOcrModifyTask");

  public static final String xxxxPROFILE_RM_OPEN_ACCOUNT = conf.getString("xxxxprofile.rmOpenAccount");
  public static final String xxxxPROFILE_RM_CONFIRM_ACCOUNT = conf.getString("xxxxprofile.rmConfirmAccount");
  public static final String DELETE_CACHE = conf.getString("xxxxprofile.deleteCache");
  public static final String HANDLE_DUPLICATE_RM_EMAIL = conf.getString("xxxxprofile.handleDuplicateRmEmail");

  public static final String ADD_REPORT_DOC_INT = conf.getString("xxxxprofile.addReportDocInt");
  public static final String GET_ACTION_DOC_INT = conf.getString("xxxxprofile.getActionDocInt");
  public static final String GET_WBL_BY_xxxxID = conf.getString("xxxxprofile.getWblByxxxxId");
  public static final String GET_WBL_BY_POLICYCODE_INT = conf.getString("xxxxprofile.getWblByPolicyCodeInt");
  public static final String ADD_USER_TO_WBL_LIST = conf.getString("xxxxprofile.addUserToWblList");
  public static final String DELETE_USER_FROM_WBL_LIST = conf.getString("xxxxprofile.deleteUserFromWblList");
  public static final String UPDATE_USER_TO_WBL_LIST = conf.getString("xxxxprofile.updateUserToWblList");
  public static final String GET_BUSINESS_TITLE_LIST = conf.getString("xxxxprofile.getBusinessTitleList");
  public static final String GET_ACTION_ORG_LIST = conf.getString("xxxxprofile.getActionOrgList");
  public static final String GET_WBL_USER_DETAIL = conf.getString("xxxxprofile.getWblUserDetail");
  public static final String GET_WBL_USER_LIST = conf.getString("xxxxprofile.getWblUserList");
  public static final String ADD_USER_TO_WBL_LIST_FUND = conf.getString("xxxxprofile.addUserToWblListFund");
  public static final String DELETE_USER_FROM_WBL_LIST_FUND = conf.getString("xxxxprofile.deleteUserFromWblListFund");
  public static final String UPDATE_USER_TO_WBL_LIST_FUND = conf.getString("xxxxprofile.updateUserToWblListFund");

  public static final String xxxxPROFILE_SPECIALWBLKEY = conf.getString("xxxxprofile.specialWblKey");
  public static final String xxxxPROFILE_NORMALWBLKEY = conf.getString("xxxxprofile.normalWblKey");
  public static final String xxxxPROFILE_CUSTODY_MAKER = conf.getString("xxxxprofile.custodyMaker");

  public static final String CALCULATE_SLA_FOR_OB = conf.getString("xxxxprofile.calculateSlaForOb");
  public static final String GET_REJECT_REASON_BY_USER_ID = conf.getString("xxxxprofile.getRejectReasonByUserId");

  public static final String SUGGEST_FANCY_105C = conf.getString("xxxxprofile.suggestFancy105C");
  public static final String CONFIRM_BOOKING_FANCY_105C = conf.getString("xxxxprofile.confirmBookingFancy105C");

  public static final String GET_BANK_SUBACCOUNT_BY_USERNAME = conf.getString("xxxxprofile.getBankSubAccountByUserName");
  public static final String CLEAR_BANK_SUBACCOUNT_STATUS_BY_USERNAME = conf.getString("xxxxprofile.clearBankSubAccountStatusByUserName");
  public static final String CREATE_NEW_PROFILE_DOCUMENT = conf.getString("xxxxprofile.createNewProfileDocument");
  public static final String UPDATE_PROFILE_DOCUMENT_BY_ID = conf.getString("xxxxprofile.updateProfileDocumentById");
  public static final String PRO_TRADER_DOCUMENT = conf.getString("xxxxprofile.proTraderDocument");
  public static final String CHECK_DUPLICATE_BANK_INFO = conf.getString("xxxxprofile.checkDuplicateBankInfo");
  public static final String REGISTER_VERIFICATION = conf.getString("xxxxprofile.registerVerification");
  public static final String UPDATE_PROFILE_TCI3 = conf.getString("xxxxprofile.updateProfileTci3");

  public static final String MULTI_CONNECT_IA_ISAVE = conf.getString("xxxxprofile.multiConnectIsiSave");
  public static final String MULTI_DISCONNECT_IA_ISAVE = conf.getString("xxxxprofile.multiDisconnectIsiSave");

  //thirty party
  public static final String FSS_SERVICE = conf.getString("xxxxprofile.fssService");

  // regIA CTG
  public static final String INQUIRY_DISCONNECT_IA_CTG = conf.getString("xxxxprofile.inquiryDisconnectIa");
  public static final String AUTO_RETRY_TASK = conf.getString("xxxxprofile.autoRetryTask");
  public static final String FLEX_ACCOUNT_INFO = conf.getString("xxxxprofile.flexAccountInfo");
  public static final String GET_INFO_IA_TCI3 = conf.getString("xxxxprofile.getInfoIaTci3");
  public static final String DISCONNECT_IA_TCI3 = conf.getString("xxxxprofile.disconnectIaTci3");
  public static final String UPDATE_IA_TCI3 = conf.getString("xxxxprofile.updateIaTci3");
  public static final String GET_INFO_IA_BATCH = conf.getString("xxxxprofile.getInfoIaBatch");
  public static final String GET_INFO_IA_SINGLE_EXT = conf.getString("xxxxprofile.getInfoIaSingleExt");
  public static final String RETRY_OTP_REGISTER_IA = conf.getString("xxxxprofile.retryOtpRegisterIa");

  // iVoucher domain
  public static final String IVOUCHER_DOMAIN = conf.getString("xxxxprofile.ivoucher-api-tool");

  public static final String ASSIGN_TASK_TO_AMOPS_MAKER = conf.getString("xxxxprofile.assignTaskToAMOPsMaker");
  public static final String INTERNAL_PROINVESTOR = conf.getString("xxxxprofile.internalProinvestorToken");

  //TCCChecker Removal
  public static final String MAKER_APPROVE_AFTER_OCR_SCAN = conf.getString("xxxxprofile.makerApproveOCR");

  //Proinvestor
  public static final String AMOPS_DEACTIVATE_PRO_INVESTOR = conf.getString("xxxxprofile.amopsDeacivateProinvestor");
  public static final String GET_PROINVESTOR_PROFILE_BY_xxxxID = conf.getString("xxxxprofile.getProinvestorProfileByxxxxid");
  public static final String GET_PROINVESTOR_PROFILE_BY_105C = conf.getString("xxxxprofile.getProinvestorprofileBy105code");
  public static final String GET_PROINVESTOR_END_DATE = conf.getString("xxxxprofile.getProInvestorEndDate");
  public static final String PRO_TRADER_CHECK = conf.getString("xxxxprofile.proTraderCheck");
  public static final String SYNC_PROINVESTOR_FROM_ANI = conf.getString("xxxxprofile.syncProInvestorFromAni");


  //RMRBO
  public static final String xxxx_USER_TOKEN = conf.getString("xxxxprofile.user_token1");
  public static final String SET_VIEW_ASSET_STATUS = conf.getString("xxxxprofile.setStatusViewAsset");
  public static final String GET_VIEW_ASSET_STATUS = conf.getString("xxxxprofile.getViewStatusAssetStatus");
  public static final String GET_RM_RBO_LIST_WITH_PAGINATION = conf.getString("xxxxprofile.getRMRBOList");
  public static final String GET_RM_RBO_IDENTIFICATION_INT = conf.getString("xxxxprofile.getRMRBOIdentificationInt");
  public static final String GET_RM_RBO_IDENTIFICATION_EXT = conf.getString("xxxxprofile.getRMRBOIdentificationExt");
  public static final String GET_IDENTIFICATION_BATCH_BY_CUSTODYCD = conf.getString("xxxxprofile.getIdentificationBatchByCustodyCd");
  public static final String GET_IDENTIFICATION_BATCH_BY_xxxxID = conf.getString("xxxxprofile.getIdentificationBatchByxxxxId");
  public static final String GET_CUSTOMER_VIEW_ASSET = conf.getString("xxxxprofile.getCustomerViewAssetData");

  //authen login
  public static final String LOGIN_TO_TCI3 = conf.getString("xxxxprofile.loginToTci3");
  public static final String AUTHEN_GET_CLIENT_KEY = conf.getString("xxxxprofile.authenGetClientKey");
  public static final String LOGIN_FROM_THIRD_PARTY = conf.getString("xxxxprofile.loginFromThirdParty");
  public static final String ADD_OTP = conf.getString("xxxxprofile.addOtp");
  public static final String GEN_AUTHEN_KEY = conf.getString("xxxxprofile.genAuthenKey");
  public static final String GEN_LOGIN_KEY = conf.getString("xxxxprofile.genLoginKey");
  public static final String OAUTH2_GET_AUTHORIZATION_CODE = conf.getString("xxxxprofile.authen.oauth2.getAuthorizationCode");
  public static final String OAUTH2_GET_ACCESS_TOKEN_FROM_AUTHORIZATION_CODE = conf.getString("xxxxprofile.authen.oauth2.getAccessTokenFromAuthorizationCode");
  public static final String OAUTH2_GET_ACCESS_TOKEN_FROM_REFRESH_TOKEN = conf.getString("xxxxprofile.authen.oauth2.getAccessTokenFromRefreshToken");
  public static final String OAUTH2_GET_SERVICE_TOKEN = conf.getString("xxxxprofile.authen.oauth2.getServiceToken");

  //author
  public static final String LOGIN_TO_ISQUARE = conf.getString("xxxxprofile.loginToIsquare");
  public static final String UPDATE_SESSION = conf.getString("xxxxprofile.updateSession");
  public static final String UPDATE_SESSION_HB = conf.getString("xxxxprofile.updateSessionHB");
  public static final String UPDATE_SESSION_HB_TOKEN = conf.getString("xxxxprofile.authen.hb.token");
  public static final String UPDATE_SESSION_WT = conf.getString("xxxxprofile.updateSessionWT");
  public static final String UPDATE_SESSION_WT_TOKEN = conf.getString("xxxxprofile.authen.wt.token");

  // author
  public static final String AUTHOR_VALIDATION = conf.getString("xxxxprofile.authorValidation");
  public static final String OTP_HB_DOMAIN = conf.getString("xxxxprofile.otpHbDomain");
  public static final String OTP_WT_DOMAIN = conf.getString("xxxxprofile.otpWtDomain");

  //DerivativeAccountForOrganization
  public static final String OPEN_DERIVATIVE_ACCOUNT_FOR_ORGANIZATION = conf.getString("xxxxprofile.openDerivativeAccountForOrganization");

  //BPM GetCustomerInformation
  public static final String GET_CUSTOMER_INFORMATION = conf.getString("xxxxprofile.getCustomerInformation");
  public static final String TRIGGER_UPDATE_INFO = conf.getString("xxxxprofile.triggerUpdateInfo");
  public static final String START_TASK_CUS_CHANGE_INFOR = conf.getString("xxxxprofile.startTaskCusChangeInfor");

  //Profile Upload File
  public static final String UPLOAD_FILE = conf.getString("xxxxprofile.uploadFile");

  //Profile admin
  public static final String TRACKING_POINT_CHANGE_INFO_PROCESS = conf.getString("xxxxprofile.trackingPointChangeInfoProcess");

  //Istock sync subaccounts
  public static final String CALL_ISTOCK_GET_SUBACCOUNT = conf.getString("xxxxprofile.callIstockToGetSubaccount");
  public static final String SYNC_SUBACCOUNT_AND_CONNECT_IA = conf.getString("xxxxprofile.syncSubaccountAndConnectIA");

  //STPFund
  public static final String GET_MODE_STP_FUND = conf.getString("stpfund.get-mode-stp-fund");
  public static final String SET_MODE_STP_FUND = conf.getString("stpfund.set-mode-stp-fund");
  public static final String STP_AUTHORIZATION_KEY = conf.getString("xxxxprofile.stp-fund-token");
  public static final String GET_STP_TRANSACTION_LIST = conf.getString("stpfund.get-stp-transaction-list");
  public static final String CANCEL_STP_TRANSACTION = conf.getString("stpfund.cancel-stp-transaction");
  public static final String UPDATE_VSD_OF_STP_TRANSACTION = conf.getString("stpfund.update-vsd-of-stp-transaction");
  public static final String IMPORT_FILE_UPDATE_VSD = conf.getString("stpfund.import-file-update-vsd");
  public static final String RETRY_STP_TRANSACTION = conf.getString("stpfund.retry-stp-transaction");
  public static final String RETRY_MANY_TRANSACTIONS = conf.getString("stpfund.retry-many-transactions");
  public static final String GET_STP_TRANSACTION_DETAILS = conf.getString("stpfund.get-stp-transaction-details");
  public static final String SUPPORT_BY_ACTION = conf.getString("stpfund.complete-online-authentication");
  public static final String STP_FUND_CLOSE_ACCOUNT = conf.getString("stpfund.stpfund-close-account");
  public static final String STP_X_API_KEY = conf.getString("xxxxprofile.stp-fund-api-key");
  public static final String GET_ACCOUNT_ACTIVE_VSD = conf.getString("stpfund.get-account-active-vsd");


  //Change Personal Info
  public static final String CHANGE_PERSONAL_INFO = conf.getString("xxxxprofile.changePersonalInfo");
  public static final String COOKIE = conf.getString("xxxxprofile.cookie");
  public static final String COOKIE_NNB = conf.getString("xxxxprofile.cookie_nnb");

  //Resign Contract
  public static final String GET_CUSTOMER_LIST = conf.getString("xxxxprofile.getCustomerList");
  public static final String GET_CUSTOMER_PROFILE = conf.getString("xxxxprofile.getProfileOfCustomer");
  public static final String ADD_CUSTOMER_TO_LIST = conf.getString("xxxxprofile.addCustomerToList");
  public static final String REMOVE_CUSTOMER_FROM_LIST = conf.getString("xxxxprofile.removeCustomerFromList");
  public static final String HANDLE_AFTER_CHANGE_INFO = conf.getString("xxxxprofile.handleAfterChangeInfo");

  //SLA Caculating Logic
  public static final String SLA_CACULATE_LOGIC = conf.getString("xxxxprofile.slaCaculatingLogic");

  //Reminder
  public static final String TOKEN_REMINDER = conf.getString("xxxxprofile.tokenReminder");
  public static final String GET_REMINDER_SOURCE = conf.getString("xxxxprofile.getReminderSource");
  public static final String UPDATE_REMINDER_SOURCE = conf.getString("xxxxprofile.updateReminderSource");
  public static final String EXECUTE_REMINDER = conf.getString("xxxxprofile.executeReminder");

  //SSO
  public static final String LOGIN_GETKEY = conf.getString("xxxxprofile.loginGetKey");
  public static final String LOGIN_USEKEY = conf.getString("xxxxprofile.loginUsedKey");

  //iAngel
  public static final String CREATE_NEW_RELATION = conf.getString("xxxxprofile.createNewRelation");

  public static final String INTERNAL_GET_CUSTOMER_INFO = conf.getString("xxxxprofile.internalGetCusInfo");

  // Hold 105C
  public static final String GET_INFO_HOLD_105C = conf.getString("xxxxprofile.getInfoHold105C");
  public static final String VALIDATE_HOLD_105C = conf.getString("xxxxprofile.validateHold105C");
  public static final String CREATE_HOLD_105C = conf.getString("xxxxprofile.createHold105C");
  public static final String GET_INFO_REFER_CODE = conf.getString("xxxxprofile.getInfoReferCode");

  // RM/RBO
  public static final String GET_R3RD_USER_LIST = conf.getString("rmrbo.getR3rdUserList");
  public static final String RECEIVE_DATA_FROM_IANGELS = conf.getString("rmrbo.receiveFromIAngels");
  public static final String RMRBO_API_KEY = conf.getString("xxxxprofile.rmrbo-x-api-key");
  public static final String UPDATE_STATUS_ELEARNING = conf.getString("rmrbo.updateStatusELearning");
  public static final String ACTIVE_ROLE_BY_USERNAME = conf.getString("rmrbo.activeRoleByUsername");
  public static final String RMRBO_CHECKER_AUTHORIZATION_KEY = conf.getString("xxxxprofile.rmrbo-checker-authorization-token");
  public static final String RMRBO_MAKER_AUTHORIZATION_KEY = conf.getString("xxxxprofile.rmrbo-maker-authorization-token");
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
  public static final String RMRBO_VIEW_TOKEN = conf.getString("xxxxprofile.rmrbo-view-token");
  public static final String RMRBO_GET_AGENCY_INFO = conf.getString("rmrbo.getAgencyInfo");
  public static final String RMRBO_BACKEND_UPDATE_ROLE = conf.getString("rmrbo.backendUpdateRole");
  public static final String EXPORT_ROLE_RM_RBO = conf.getString("rmrbo.exportRole");
  public static final String EXPORT_ROLE_RM_RBO_TOKEN = conf.getString("xxxxprofile.exportRole-token");
  public static final String RM_RBO_HISTORY_VIEW = conf.getString("rmrbo.rmrbo-history-view");
  public static final String GET_NNB_BY_FUND = conf.getString("xxxxprofile.getNnbByFundList");
  public static final String GET_CUSTOMER_BY_RBO = conf.getString("rmrbo.getCustomerByRbo");
  public static final String GET_HISTORY_ACTIVE_INACTIVE = conf.getString("rmrbo.getHistoryActiveInactive");
  public static final String ADD_HISTORY_ACTIVE_INACTIVE_IWP = conf.getString("rmrbo.addHistoryActiveInactiveIwp");

  //FORGOT PASSWORD
  public static final String FORGOT_PASSWORD_PHONE = conf.getString("xxxxprofile.forgotPasswordPhone");
  public static final String FORGOT_PASSWORD_EMAIL = conf.getString("xxxxprofile.forgotPasswordEmail");
  public static final String FORGOT_PASSWORD_NOTIFY = conf.getString("xxxxprofile.forgotPasswordNotify");
  public static final String FORGOT_PASSWORD_CONFIRM_PHONE = conf.getString("xxxxprofile.forgotPasswordConfirmPhone");
  public static final String FORGOT_PASSWORD_VALIDATE = conf.getString("xxxxprofile.forgotPasswordValidate");

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
  public static final String PARTNERSHIP_X_API_KEY = conf.getString("xxxxprofile.partnerShip-x-api-key");
  public static final String PARTNERSHIP_ACCOUNT_UNLINK = conf.getString("newonboardingPartnerShip.partnerShipAccountUnLink");
  public static final String PARTNERSHIP_VIEW_CONTRACT = conf.getString("newonboardingPartnerShip.partnerShipViewContract");
  public static final String PARTNERSHIP_SIGN_CONTRACT = conf.getString("newonboardingPartnerShip.partnerShipSignContract");
  public static final String PARTNERSHIP_CONFIRM = conf.getString("newonboardingPartnerShip.partnerShipConfirm");
  public static final String GET_INFO_BY_USERNAME = conf.getString("newonboardingPartnerShip.getInfoByUsername");
  public static final String RETURN_INFO_SOCIAS = conf.getString("newonboardingPartnerShip.returnInfoForSocias");
  public static final String PARTNERSHIP_GET_HISTORY_IA = conf.getString("newonboardingPartnerShip.partnerShipGetHistoryIa");
  public static final String PARTNERSHIP_GET_DETAIL = conf.getString("newonboardingPartnerShip.partnerShipGetDetail");
  public static final String xxxx_ACCOUNT_CONFIRM = conf.getString("newonboardingPartnerShip.xxxxConfirmId");
  public static final String xxxx_ACCOUNT_LINK = conf.getString("newonboardingPartnerShip.xxxxAccountLink");
  public static final String OPEN_ACCOUNT_PARTNER_VIEW_CONTRACT = conf.getString("newonboardingPartnerShip.openAccountPartnerViewContract");
  public static final String OPEN_ACCOUNT_PARTNER_SIGN_CONTRACT = conf.getString("newonboardingPartnerShip.openAccountPartnerSignContract");
  public static final String OPEN_ACCOUNT_PARTNER_REGISTER = conf.getString("newonboardingPartnerShip.openAccountPartnerRegister");
  public static final String OPEN_ACCOUNT_PARTNER_SEND_OTP = conf.getString("newonboardingPartnerShip.openAccountPartnerSendOtp");
  public static final String xxxx_ACCOUNT_UNLINK = conf.getString("newonboardingPartnerShip.xxxxAccountUnLink");

  //Additional API
  public static final String SUGGEST_ID_PLACE = conf.getString("xxxxprofile.suggestIdPlace");
  public static final String GET_LIST_COUNTRY = conf.getString("xxxxprofile.getListCountry");
  public static final String GET_LIST_PROVINCE_BY_COUNTRY = conf.getString("xxxxprofile.getListProvinceByCountryCode");
  public static final String GET_LIST_BANK_INFO = conf.getString("xxxxprofile.getListBankInfo");
  public static final String SEARCH_BY_CONDITION = conf.getString("xxxxprofile.searchByCondition");
  public static final String GET_INFO_BY_CONDITION = conf.getString("xxxxprofile.getInfoByCondition");
  public static final String UPDATE_INFO_CUSTOMER = conf.getString("xxxxprofile.updateInfoCustomer");

  //BAU Tool
  public static final String BAU_AUTHORIZATION_TOKEN = conf.getString("xxxxprofile.bau-authorization-token");
  public static final String BAU_SEARCH_BANK_IA_INFO = conf.getString("bauTool.searchBankIaInfo");
  public static final String GET_TOOL_BAU_LIST = conf.getString("bauTool.getToolBauList");
  public static final String SEARCH_BAU_HISTORY = conf.getString("bauTool.searchBauHistory");
  public static final String ACTION_TOOL_BAU = conf.getString("bauTool.actionToolBau");

  // check contract
  public static final String JOB_CHECK_CONTRACT = conf.getString("xxxxprofile.jobCheckContract");

  // corporation
  public static final String CORPORATE_X_API_KEY = conf.getString("xxxxprofile.corporate-x-api-key");
  public static final String CORPORATE_REGISTER_BETA = conf.getString("xxxxprofile.corporateRegisterBeta");

}
