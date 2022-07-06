package com.tcbs.automation.config.rubik;

import com.tcbs.automation.config.ConfigImpl;
import com.typesafe.config.Config;

public class H2hConfig {
  private static final Config conf = new ConfigImpl("rubik").getConf();

  public static final String GET_HEALTH_CHECK = conf.getString("api.health-check");
  public static final String GET_BANK_ACCOUNT = conf.getString("api.bank-account");
  public static final String GET_LIST_REVIEWER = conf.getString("api.list-reviewer");
  public static final String FUND_TRANSFER = conf.getString("api.fundTransfer");
  public static final String FUND_TRANSFER_RETRY = conf.getString("api.fundTransferRetry");
  public static final String GET_LOG = conf.getString("api.inquiry");
  public static final String GET_LIST_CONFIG = conf.getString("api.list-config");
  public static final String GET_LIST_LABEL = conf.getString("api.list-label");
  public static final String GET_BANK_INFO = conf.getString("api.bankInfo");
  public static final String GET_BANK_INFO_PUBLIC = conf.getString("api.publicBankInfo");
  public static final String GET_LIST_FILE_FTP = conf.getString("api.listFtp");
  public static final String GET_RAW_FILE_FTP = conf.getString("api.rawFtp");
  public static final String SYNC_FTP = conf.getString("api.syncFtp");
  public static final String FE_TRANSFER = conf.getString("api.feFundTransfer");
  public static final String FE_REVIEW = conf.getString("api.review");
  public static final String FE_REVIEW_FT = conf.getString("api.reviewFT");
  public static final String RECONCILE_TXN = conf.getString("api.reconcileTxn");
  public static final String RECONCILE_LIST = conf.getString("api.reconcileList");
  public static final String TRANSACTION_LIST = conf.getString("api.feTransaction");
  public static final String INTERNAL_FUND_TRANSFE_REAL_TIME = conf.getString("api.internalFundTransferRealTime");
  public static final String RETRY_INTERNAL_FUND_TRANSFE_REAL_TIME = conf.getString("api.retryInternalFundTransferRealTime");

  public static final String BATCH_FUND_TRANSFER = conf.getString("api.batchFT");
  public static final String BATCH_FUND_TRANSFER_RETRY = conf.getString("api.batchRetry");
  public static final String BATCH_TRANSFER_INQUIRY = conf.getString("api.batchTransferInquiry");
  public static final String FUND_TRANSFER_INQUIRY = conf.getString("api.fundTransferInquiry");

  public static final String BATCH_TRANSFER_INQUIRY_BY_GW_ID = conf.getString("api.batchTransferInquiryByGWid");
  public static final String FUND_TRANSFER_INQUIRY_BY_GW_ID = conf.getString("api.fundTransferInquiryByGWid");

  public static final String FAST_TRANSFER = conf.getString("api.fastTransfer");
  public static final String RECONCILE_EXPORT = conf.getString("api.reconcileExport");
  public static final String TRANSACTION_EXPORT = conf.getString("api.transactionExport");

  public static final String X_API_KEY = conf.getString("x-api-key");
  public static final String X_API_KEY_INBOX = conf.getString("x-api-key-inbox");
  public static final String X_CLIENT_KEY = conf.getString("x-client-key");
  public static final String AUTHOR_BY_PASS_OTP = conf.getString("author-by-pass-otp");

  public static final String ADD_CONFIG = conf.getString("api.addConfig");
  public static final String UPDATE_CONFIG = conf.getString("api.updateConfig");
  public static final String CLEAN_CACHE = conf.getString("api.cleanCache");
  public static final String BBS_INQUIRY = conf.getString("api.bbsInquiry");
  public static final String TXN_SYNC = conf.getString("api.txnSync");

  public static final String PAYMENT_STATUS = conf.getString("api.paymentStatus");

  public static final String STOCK_RECONCILE = conf.getString("api.stockReconcile");
  public static final String RERUN_INQUIRY_BY_TXNID = conf.getString("api.rerunInquiryByTxnId");
  public static final String RECONCILE_CONTENT = conf.getString("api.reconcileContent");
  public static final String RECONCILE_SUMMARY = conf.getString("api.reconcileSummary");
  public static final String LIST_RECONCILE_IN_FLEX = conf.getString("api.listReconcileInFlex");
  public static final String GET_LIST_TRANSACTION_PAY_COUPON = conf.getString("api.getListTransactionPayCoupon");
  public static final String SEARCH_FOR_MONEY_TRANSFER_SELL_TO_CUSTOMERS = conf.getString("api.searchForMoneyTransferSellToCustomers");
  public static final String ANI_BATCH_TRANSFER = conf.getString("api.aniBatch");
  public static final String ANI_BATCH_TRANSFER_RETRY = conf.getString("api.aniBatchRetry");
  public static final String ANI_BATCH_TRANSFER_AUTO_RETRY = conf.getString("api.aniBatchAutoRetry");
  public static final String REINQUIRY_STATUS_WITH_TRANSACTION_LIST = conf.getString("api.reInquiryStatusWithTransactionList");
  public static final String CHANGE_STATUS_ERROR_TRANSACTION = conf.getString("api.changeStatusErrorTransaction");
  public static final String BBK_ADD_PATTERN = conf.getString("api.bbkAddPattern");
  public static final String BBK_VERIFY_PATTERN = conf.getString("api.bbkVerifyPattern");
  public static final String BBK_GET_BY_PATTERN = conf.getString("api.bbkGetByPattern");
  public static final String BBK_INQUIRY_LIST = conf.getString("api.bbkInquiryList");

  public static final String GET_LIST_TYPE = conf.getString("api.list-type");
  public static final String GET_CONFIG_BY_ID = conf.getString("api.getConfigById");
  public static final String GET_ACCOUNT_INFOR_DWH = conf.getString("api.getAccountInforDWH");
  public static final String GET_ACCOUNT_INFOR_BANK = conf.getString("api.getAccountInforBank");
  public static final String TRUSTED_ACCOUNT_WITH_CREATE_ACCOUNT = conf.getString("api.trustedAccountWithCreateAccount");
  public static final String TRUSTED_ACCOUNT_WITH_APPROVE_ACCOUNT = conf.getString("api.trustedAccountWithApproveAccount");
  public static final String TRUSTED_ACCOUNT_WITH_CHANGE_ACCOUNT = conf.getString("api.trustedAccountWithChangeAccount");
  public static final String TRUSTED_ACCOUNT_WITH_CANCEL_ACCOUNT = conf.getString("api.trustedAccountWithCancelAccount");
  public static final String TRANSFER_MONEY_FROM_FUTURE_OUT = conf.getString("api.transferMoneyFromFutureOut");
  public static final String TRANSFER_MONEY_PAYMENT_INTERNAL_FROM_CAPITAL_TO_OUT = conf.getString("api.transferMoneyPaymentInternalFromCapitalToOut");
  public static final String TRANSFER_MONEY_PAYMENT_TCI3_FROM_DERIVATIVE_TO_OUT = conf.getString("api.transferMoneyPaymentTci3FromDerivativeToOut");
  public static final String TRANSFER_MONEY_FROM_FUTURE_TO_CAPITAL_MARKET = conf.getString("api.transferMoneyFromDerivativeToCapitalMarket");
  public static final String INQUIRY_TRANSACTION_FORFLOW_EXTENAL_FUTURE_AND_CAPITAL_MARKET = conf.getString("api.inquiryTransactionForFlowExtenalFutureAndCapitalMarket");
  public static final String HOLD_MONEY_OF_CAPITAL_MARKET = conf.getString("api.holdMoneyOfCapitalMarket");
  public static final String CHECK_CASH_OUT_AMOUNT = conf.getString("api.checkCashOutAmount");
  public static final String TRANSFER_MONEY_INTERNAL_FROM_FUTURE_TO_CAPITAL_MARKET = conf.getString("api.transferMoneyInternalFromFutureToCapitalMarket");
  public static final String TRANSFER_MONEY_INTERNAL_FROM_CAPITAL_MARKET_TO_FUTURE = conf.getString("api.transferMoneyInternalFromCapitalMarketToFuture");

  public static final String INQUIRY_STOCK_RECONCILE = conf.getString("api.inquiryStockReconcile");
  public static final String UPDATE_STOCK_RECONCILE = conf.getString("api.updateStockReconcile");
  public static final String RE_PUSH_STOCK_RECONCILE = conf.getString("api.rePushStockReconcile");

  public static final String ISOCIAS_RECONCILE_SEARCH = conf.getString("api.isociasReconcileSearch");
  public static final String ISOCIAS_RECONCILE_SUMMARY = conf.getString("api.isociasReconcileSummary");
  public static final String ISOCIAS_RECONCILE_EXPORT = conf.getString("api.isociasReconcileExport");
  public static final String UPDATE_RESULT_RECONCILE = conf.getString("api.updateResultReconcile");
  public static final String ADD_TRANSACTION_RECONCILE = conf.getString("api.addTransactionReconcile");

  public static final String BBS_RECONCILE_SEARCH = conf.getString("api.bbsReconcileSearch");
  public static final String BBS_RECONCILE_EXPORT = conf.getString("api.bbsReconcileExport");
  public static final String BBS_PATTERN_EXPORT = conf.getString("api.bbsPatternExport");
  public static final String BBS_BATCH_REALTIME = conf.getString("api.bbkBatchRealtime");

  public static final String BLOCKADE_INQUIRY_TRANSACTION = conf.getString("api.blockadeInquiryTransaction");
  public static final String BLOCKADE_INQUIRY_LIST_TRANSACTION = conf.getString("api.blockadeInquiryListTransaction");
  public static final String BATCH_TRANSFER_ISAVE = conf.getString("api.batchTransferISave");
  public static final String BATCH_TRANSFER_ANI_HB = conf.getString("api.transactionBatchANIHB");
  public static final String TRANSACTION_BATCH_ACH_TCS = conf.getString("api.transactionBatchACH_TCS");
  public static final String BATCH_TRANSFER_ANI_SELL_BOND_HB = conf.getString("api.transactionBatchForAniSellBondHb");
  public static final String RETRY_BATCH_TRANSFER_ANI_SELL_BOND_HB = conf.getString("api.retryTransactionBatchForAniBondHb");
  public static final String JOB_SYNC_STATUS_FROM_RECONCILE = conf.getString("api.jobSyncStatusFromReconcile");

  /**
   * API of silk-gate service
   */
  public static final String BANK_ACCOUNT_BALANCE = conf.getString("api.bankAccountBalance");
  public static final String BANK_ACCOUNT_BALANCE_INTERNAL = conf.getString("api.bankAccountBalanceInternal");
  public static final String HOLD_MONEY = conf.getString("api.holdMoney");
  public static final String UNHOLD_MONEY = conf.getString("api.unholdMoney");
  public static final String HOLD_MONEY_STATUS = conf.getString("api.holdMoneyStatus");
  public static final String REMITTANCE_ADD_MONEY = conf.getString("api.remitAdd");
  public static final String REMITTANCE_INQUIRY_MONEY = conf.getString("api.remitInquiry");
  /**
   * API of CASH service
   */
  public static final String INFORMATION_CLIENT_FROM_CASH = conf.getString("api.informationClientFromCash");
  public static final String SILK_GATE_SCAN_RECONCILE = conf.getString("api.silkGateScanReconcile");
  public static final String CUT_PAYMENT_FEE_BPM = conf.getString("api.cutPaymentFeeBPM");
  public static final String INQUIRY_CUT_PAYMENT_FEE_BPM = conf.getString("api.inquiryCutPaymentFeeBPM");
  public static final String INQUIRY_LOAN_HOLDING_BUY_BOND = conf.getString("api.inquiryLoanHoldingBuyBond");
  public static final String HOLDING_MONEY_ACCOUNT_CONNECT_AI = conf.getString("api.holdingMoneyAccountConnectAI");
  public static final String UNHOLDING_MONEY_ACCOUNT_CONNECT_AI = conf.getString("api.unholdingMoneyAccountConnectAI");
  public static final String CUT_ON_HOLD_MONEY_ACCOUNT_CONNECT_IA = conf.getString("api.cutOnHoldMoneyAccountConnectIA");
  public static final String CUT_ON_HOLD_MONEY_BATCH_ACCOUNT_CONNECT_IA = conf.getString("api.cutOnHoldMoneyBatchAccountConnectIA");
  public static final String INQUIRY_HOLD_AND_UNHOLDING_MONEY_ACCOUNT_CONNECT_AI = conf.getString("api.inquiryHoldAndUnholdMoneyAccountConnectAIApi");
  public static final String RECEIVE_NOTIFY_BOND_SALE_ORDER = conf.getString("api.receiveNotifyBondSaleOrder");
  public static final String RECEIVE_NOTIFY_FLEX_SALE_ORDER = conf.getString("api.receiveNotifyFlexSaleOrder");
  public static final String MAKER_VIEW_TRANSACTION_MANUAL_IMPACT_HISTORY = conf.getString("api.makerViewTransactionManualImpactHistory");
  public static final String ADDITIONAL_HOLD_FOR_BOND_LOAN = conf.getString("api.additionalHoldForBondLoan");
  public static final String GET_LIST_TRANSACTION_LOAN = conf.getString("api.getListTransactionLoan");
  public static final String INQUIRY_RESULT_COLLECTION = conf.getString("api.inquiryResultCollection");
  public static final String RE_INQUIRY_STATUS_WITH_COLLECTION_LIST = conf.getString("api.reInquiryStatusWithCollectionList");
  public static final String BACK_THE_LOAN_WHEN_NOT_IN_USE = conf.getString("api.backTheLoanWhenNotInUse");
  public static final String INQUIRY_BACK_THE_LOAN_WHEN_NOT_IN_USE = conf.getString("api.inquiryBackTheLoanWhenNotInUse");
  public static final String INQUIRY_LIST_SUBACCOUNTTRANSACTION = conf.getString("api.inquiryListSubAccountTransaction");
  public static final String INQUIRY_LIST_SUBACCOUNT_TRANSACTION_LOG = conf.getString("api.inquiryListSubAccountTransactionLog");
  public static final String INCREASE_SUBACCOUNT_FROM_IVOUCHER = conf.getString("api.increaseSubAccountFromIVoucher");
  public static final String DECREASE_SUBACCOUNT_FROM_IVOUCHER = conf.getString("api.decreaseSubAccountFromIVoucher");
  public static final String INCREASE_SUBACCOUNT_FROM_ISAVE = conf.getString("api.increaseSubAccountFromISave");
  public static final String DECREASE_SUBACCOUNT_FROM_ISAVE = conf.getString("api.decreaseSubAccountFromISave");
  public static final String INQUIRY_INCREASE_DECREASE_ISAVE = conf.getString("api.inquiryIncreaseDecreaseISave");
  public static final String INQUIRY_RESULT_ISAVE_IN_FLEX = conf.getString("api.inquiryResultISaveInFlex");
  public static final String BATCH_TRANSFER_INTEREST_PAYMENT_ISAVE = conf.getString("api.batchTransferInterestPaymentISave");
  public static final String BATCH_TRANSFER_DECREASE_ISAVE = conf.getString("api.batchTransferDecreaseISave");
  public static final String BATCH_INQUIRY_BALANCE_ACCOUNT = conf.getString("api.batchInquiryBalanceAccount");
  public static final String BLOCKADE_GET_BALANCE = conf.getString("api.blockadeGetBalance");


  /**
   * API cho phái sinh
   */
  public static final String IF_CALCULATE_FEE = conf.getString("api.IFCalculateFee");

  //API Reconcile
  public static final String H2H_RECONCILE_STOCK_TXN = conf.getString("api.stockTXNReconcile");

  //API Manage Hold/UnHold TK IA
  public static final String BLOCKADE_GET_TRANSACTION = conf.getString("api.getTransactionBlockade");
  public static final String BLOCKADE_GET_TRANSACTION_EXPORT = conf.getString("api.getTransactionBlockadeExport");

  //API buy bond
  public static final String BLOCKADE_NOTIFY_ORDER_MATCHING = conf.getString("api.notifyOrderMatching");
  public static final String BLOCKADE_NOTIFY_BOND_SALE_MONEY_RECEIVED = conf.getString("api.notifyBondSaleMoneyReceived");

  //API notify paid loan
  public static final String BLOCKADE_PAID_LOAN = conf.getString("api.paidLoan");
  public static final String BLOCKADE_INQUIRY_PAID_LOAN = conf.getString("api.inquiryPaidLoan");
  public static final String BLOCKADE_UNHOLD_PAID_LOAN = conf.getString("api.unholdPaidLoan");

  //API inquiry transaction fund
  public static final String INQUIRY_TRANSACTION_FUND = conf.getString("api.inquiryTransactionFund");
  public static final String TRANSFER_FUND_BATCH = conf.getString("api.transferFundBatch");

  //API get list collection transaction
  public static final String GET_LIST_COLLECTION_TRANSACTION = conf.getString("api.getListCollectionTransaction");
  public static final String MAKER_MANUAL_BOOKING = conf.getString("api.makerManualBooking");
  public static final String INVEST_COLLECTION_TRANSACTION = conf.getString("api.investCollectionTransaction");
  public static final String EXPORT_COLLECTION_TRANSACTION_REPORT = conf.getString("api.exportCollectionTransactionReport");
  public static final String GET_TOATAL_COLLECTION_TRANSACTION = conf.getString("api.getTotalCollectionTransaction");

  //API phe duyet giao dich man hinh thu ho
  public static final String LIST_APPROVE_STOCK_RECONCILE = conf.getString("api.listApproveStockReconcile");
  public static final String LIST_APPROVE_STOCK_RECONCILE_EXPORT = conf.getString("api.listApproveStockReconcileExport");

  // API Verify thong tin acc
  public static final String CUSTOMER_BANK_ACC_CHECK = conf.getString("api.customerBankAccCheck");
  public static final String CUSTOMER_BANK_ACC_DETAIL = conf.getString("api.customerBankAccGetDetail");
  public static final String RESET_TIME_CALL_API_CHECK = conf.getString("api.resetCallApicCheck");

  // API danh dau giao dich cho hoan tra va thuc hien hoan tra
  public static final String MAKE_REFUND_STOCK_RECONCILE = conf.getString("api.makeRefundStockReconcile");
  public static final String EXPORT_WAITING_REFUND_RECONCILE = conf.getString("api.exportWaitingRefundReconcile");


  //API nhận diện diễn giải thu hộ
  public static final String IDENTIFY_DESCRIPTION = conf.getString("api.identifyDescription");

  //API list TK tổng TCBS theo đối tác
  public static final String GET_LIST_SOURCE_ACCOUNT_NUMBER_BY_PARTNER = conf.getString("api.getListSourceAccountNumberByPartner");

  // API tra soát
  public static final String MAKER_INVEST_RECONCILE = conf.getString("api.makerInvestReconcile");
  public static final String MAKER_UPDATE_INVEST_RECONCILE = conf.getString("api.makerUpdateInvestReconcile");
  public static final String MAKER_NOTE_STOCK_RECONCILE = conf.getString("api.makerNoteStockReconcile");
  public static final String CHECKER_ACTION_STOCK_RECONCILE = conf.getString("api.checkerActionStockReconcile");

  public static final String TRANSFER_FEE_PRICING = conf.getString("api.transferFeePricing");
  public static final String REPROCESS_IDENTIFY = conf.getString("api.reprocessIdentify");

  //API ISAVE INQUIRY KẾT QUẢ CHUYỂN TIỀN BATCH NETOFF
  public static final String INQUIRY_BATCH_NETOFF = conf.getString("api.inquiryBatchNetoff");
  public static final String GET_SUB_ACC_BALANCE = conf.getString("api.getSubAccBalance");


  public static final String TRANSFER_IPI_HOA_BINH = conf.getString("api.transferIpi");


  //API list giao dịch mua, bán BOND LOAN
  public static final String GET_LIST_SELL_BUY_BOND = conf.getString("api.getListSellBuyBond");

  //API list giao dịch giải ngân/thu nợ của HOA BINH
  public static final String GET_LIST_DISBURSEMENT_DEBT_HB = conf.getString("api.getListDisbursementDebtHB");

  //API update trạng thái giao dịch H2H_TRANS
  public static final String UPDATE_STATUS_H2H_TRANS = conf.getString("api.updateStatusH2HTrans");

  //API Truy vấn thông tin sức mua physical TKCK
  public static final String GET_PURCHASING_POWER_STOCK_ACCOUNT = conf.getString("api.getPurchasingPowerStockAccount");

  //API Truy vấn thông tin sức mua physical TKCK
  public static final String LOOK_UP_MONEY_FROM_TCB_ACCOUNT = conf.getString("api.lookUpMoneyFromTCBAccount");

  //API silkgate
  //API lấy list config
  public static final String GET_LIST_CONFIG_DB_SILKGATE = conf.getString("api.getListConfigDBSilkGate");
  //API add config
  public static final String ADD_CONFIG_DB_SILKGATE = conf.getString("api.addConfigDBSilkGate");
  //API update config
  public static final String UPDATE_CONFIG_DB_SILKGATE = conf.getString("api.updateConfigDBSilkGate");


  //API lấy giao dịch hold/unhold/bảng kê
  public static final String GET_LIST_HOLD_UNHOLD_CUTONHOLD = conf.getString("api.getListHoldUnholdCutonhold");

  //API truy vấn trạng thái GD hold/unhold/changehold/thanh toán
  public static final String QUERY_HOLD_UNHOLD_CHANGEHOLD_PAYMENT = conf.getString("api.queryHoldUnholdChangeHoldPaymentTrans");

  //API inquiry số dư tiểu khoản có lưu cache
  public static final String INQUIRY_SUB_ACC_BALANCE_SAVE_CACHE = conf.getString("api.inquirySubAccBalanceSaveCache");

  //thu hộ trực tiếp
  public static final String ISOCIAS_DIRECT_COLLECTION_TRANS = conf.getString("api.directCollectionTrans");

}
