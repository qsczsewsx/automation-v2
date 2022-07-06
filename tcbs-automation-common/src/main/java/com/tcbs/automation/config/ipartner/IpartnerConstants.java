package com.tcbs.automation.config.ipartner;

public class IpartnerConstants {
  public static final String MAP_IMPORT_TCBS_REFERRAL = "/src/test/resources/referrals/jobupdatestatus/sampleDataBondOrderReferral.csv";
  public static final String MAP_IMPORT_SAMPLE_ROW_TCBSR_EFERRAL = "/src/test/resources/referrals/jobupdatestatus/sampleRowTcbsReferral.csv";
  public static final String MAP_CANCELKHIWP_PREPAREDATA = "/src/test/resources/cancelrelationkhiwp/prepareData.csv";
  public static final String IWP_IMPORT_DS_IWP_TCBSID_04 = "0001000004";
  public static final String IWP_IMPORT_DS_IWP_TCBSID_05 = "0001000005";
  public static final String TCBSID_TEXT = "tcbsId";
  public static final String DATA_REFERRAL_ID_TEXT = "dataReferralId";
  public static final String CONTENT_TEXT = "content";
  public static final String USERNAME_TEXT = "username";
  public static final String DELETED_TEXT = "DELETED";
  public static final String PARTNER_ID_TEXT = "partnerId";
  public static final String SUBSCRIBER_ID_TEXT = "subscriberId";
  public static final String DATA_REFERRAL_TYPE_TEXT = "referralType";
  public static final String DATA_REFERREE_TCBS_ID_TEXT = "referreeTcbsId";
  public static final String DATA_REFERRER_TCBS_ID_TEXT = "referrerTcbsId";
  public static final String MAP_CONFIRM_REGISTER_IWP_PREPAREDATA = "/src/test/resources/iwp/confirmregisteriwp/prepareData.txt";
  public static final String QUERY_CLEANUP_IAG_T1544 = "INSERT INTO TCBS_USER (ID, USERNAME, PASSWORD, EMAIL, FIRSTNAME, LASTNAME, GENDER, BIRTHDAY, RELATIONSHIP, CREATED_DATE, UPDATED_DATE, STATUS, PROFILE_PICTURE, HONORIFIC, PHONE, TCBSID, VSD_STATUS, OTP_CHANNEL, CUSTYPE, TRANSFER_STATUS, NAME_ACRONYN, MST, FLOW_OPEN_ACCOUNT, SYS_USER_TYPE, SYSTEM_USER, ENVELOPE_ID, DOCUSIGN_STATUS, ACCOUNT_STATUS, AVATAR_DATA, AVATAR_HEADER, FIRSTTIME_LOGIN, EDITABLE, ONBOARDING_DATA, BPM_EKYC_STATUS, BPM_EKYC_DENY_REASON, BPM_EKYC_DENY_CONTENT, CLIENTKEY, CONTRACT_PAYLOAD, HAS_PERM_BOND_PT) VALUES (5187938, '105C888311', '14ca977e4e1cca34e82538e969fbd41fcf6c52e17f95cad3a29ecf0d52ca4d0f', 'no_email_12107771@tcbs.com.vn', 'TCBS41463262', 'test', 0, TO_DATE('1984-11-11 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), null, TO_DATE('2017-07-19 13:47:11', 'YYYY-MM-DD HH24:MI:SS'), TO_DATE('2018-11-26 19:31:15', 'YYYY-MM-DD HH24:MI:SS'), 1, null, -1, '0117518558', '0001888311', 1, 1, 0, 1, null, null, 1, 0, 0, null, null, 1, null, null, null, 0, null, null, null, null, null, null, 0)";
  public static final String IAG_T1552_USERNAME = "105C888311";
  public static final String IAG_T1552_FULLNAME = "test TCBS41463262";
  public static final String IAG_T1552_TCBSID = "0001888311";

  public static final String RESULT_FLEX_GET_ACCOUNT = "RESULT_FLEX_GET_ACCOUNT";
  public static final String RESULT_FLEX_UPDATE_AFTYPE = "RESULT_FLEX_UPDATE_AFTYPE";
  public static final String BEARER_KEY = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzaXRfYXV0aGVuX3NlcnZpY2UiLCJleHAiOjE1OTM3NTk5MDMsImp0aSI6IiIsImlhdCI6MTU5Mzc0NTUwMywic3ViIjoiMDAwMTIwMTk4OCIsImN1c3RvZHlJRCI6IjEwNUM5NDE4ODgiLCJlbWFpbCI6Im5vX2VtYWlsXzU3MzI2MTc3QHRjYnMuY29tLnZuIiwicm9sZXMiOlsiY3VzdG9tZXIiXSwic3RlcHVwX2V4cCI6MCwic290cF9zaWduIjoiIiwiY2xpZW50X2tleSI6ImVXWktmbzR0NmQ1OGFVdnIyOHZtdmVkQVpONmlHNThnIn0.Q1FybA3FLczCq2fseLE6LreOB0HKYBvi4R7CuBOCeNWMbDLtMpsCWx11Fmj8NuQjRevXpmZzuqcWfReOY07fu7mi1diWG4wrAIaESgYTSEtwoXDEhJ6Dpzz8j21ziOyyEJcIIBIbIMw1lmyuLYfRKEIpEyKFZlpHXMXTIjhtw0wybeHgeyTZ2YUkk2SjlBUyAjPLGtAmoofNdGlN9fKYo9D0atwhEGvU1umnTwvKLL6pJUiipaFLXvLiTSZxEJ1DzzmuWxUJ4JxuTE_wB6vW4en0aloEskRiSHWkMmw9Nclc4kSG0BEQd4EShHkPTEgGAJZxyupcb85HwW01IB8y4w";
  public static final String BEFORE_AFTYPE = "BEFORE_AFTYPE";

  public static final String COMMON_X_API_KEY = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzaXRfYXV0aGVuX3NlcnZpY2UiLCJleHAiOjE1NDY5MTYwNTUsImp0aSI6IiIsImlhdCI6MTU0NjgyOTY1NSwic3ViIjoiMDAwMTEwMDE3MSIsImN1c3RvZHlJRCI6IjEwNUMxMDAxNzEiLCJlbWFpbCI6ImFidmlldGR2LmNtY0B0Y2JzLmNvbS52biIsInJvbGVzIjpbImN1c3RvbWVyIl0sInN0ZXB1cF9leHAiOjAsInNvdHBfc2lnbiI6IiJ9.QYzxqOWs_C10FQHFsjAhHg7i0GASLvYKyw2pFG7Re3auHv6s6sAjZeDsYqpUrSYUQFO5q6I9U4ipqnn27mTHyPqcgm5KYl-H7HL8sBA8r-hhGz3W9g8MkF-Gp3kEYJBCFpDWhlZEO_QbJGejw8fFLTddsJ6NIgAXWpFWtjD4BuIpGWFalJiHDN_vjXeAIld2-LimzgaJcO3hna5bUiZ9pQyEGZc4GX9vR1t_uAbKl7tDMpRbY8k9jFzHe3E8isV2TE4oDyXBpUHv6rD8d2MIP6s98vyv-Os0zLLuSVC_egh1jSxzBtaY3hBZCAuHT3wuHhx10Fz4_A5cfTjJG3o_Qw";
  public static final String MAP_CHANGE_AFTYPE_IWP_REACTIVE_PREPAREDATA = "/src/test/resources/aftype/changeaftypeiwpreactive/prepareData.txt";
  public static final String MAP_GET_REMAIN_INCENTIVE_PREPARE_DATA = "/src/test/resources/referrals/getstockremainincentive/prepareData.txt";
  public static final String MAP_GET_STOCK_FIRST_ORDER_PREPARE_DATA = "/src/test/resources/referrals/getstockfirstorder/prepareData.txt";
  public static final String MAP_SEND_NET_INCENTIVE_TO_IXU_PREPARE_DATA = "/src/test/resources/referrals/netincentivetoixu/prepareData.txt";
  public static final String MAP_SEND_NET_INCENTIVE_TO_IXU_PREPARE_DATA_BOND_TABLE = "/src/test/resources/referrals/netincentivetoixu/prepareDataBondTrading.txt";
  public static final String MAP_SEND_NET_INCENTIVE_TO_IXU_PREPARE_DATA_TCINVEST_TABLE = "/src/test/resources/referrals/netincentivetoixu/prepareDataFundTrading.txt";
  public static final String MAP_SEND_NET_INCENTIVE_TO_IXU_PREPARE_DATA_IXU_REFERRAL_TRANSACTION_TABLE = "/src/test/resources/referrals/netincentivetoixu/prepareDataIxuReferralTransaction.txt";
  public static final String MAP_GET_REMAIN_INCENTIVE_PREPARE_DATA_CHECK_TOTAL_INCENTIVE = "/src/test/resources/referrals/getstockremainincentive/prepareData_check_total_incentive.txt";
  public static final String TCBSID_PARAM_KEY = "tcbsId";
  public static final String TOP_PARAM_KEY = "top";
  public static final String MAP_SEND_NET_INCENTIVE_TO_IXU_IAG486_PREPARE_DATA = "/src/test/resources/referrals/netincentivetoixuiag486/prepareData.txt";
  public static final Object ACCOUNT_X_API_KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiAiaHR0cHM6Ly9kYXRhcG93ZXIudGNicy5jb20udm4iLCAic3ViIjogInRjYnMtYWNjb3VudCIsICJpYXQiOiAxNjAyNjU4NzgwLCAiZXhwIjogMTkxODAxODc4MH0.dgpMqxGoOkfnnUi9HcSq1ukR2vLEpRAx7s842q02kJv6d6hx2s9uGFWvHf93zmMAQG5H2AzhKqGzswTK8FJA2h11XL1ngVYfFLRP1hwDvukGHm-7UQ4DV6CCo3ZCZW0UKNWkZKmrvCJOPqxjBf_GJkRDph-vIbSKBtQZ4tpA9OM-sl9FXj1eG_eKIayt6PNupi9OBqzxlPF6pzmjk9ydMj7y0Au65wreeNrArCkfbvogqNYMXUHM1yW5G3FRD8ENH-nJZqUpr5kzghEk8v_k6FR82m15FBqgq08G7hM3L0l54xaBj40A06nP4Pd3-HbC-upr5b_aWFfQEtg9YxviVA";
  public static final Object IPARTNER_X_API_KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJpc3MiOiAiaHR0cHM6Ly9kYXRhcG93ZXIudGNicy5jb20udm4iLCAic3ViIjogImlwYXJ0bmVyIiwgImlhdCI6IDE2MDI1NzkwNjgsICJleHAiOiAxOTE3OTM5MDY4fQ.RGqE9qjxerNGyOxmt0Lj5h_-LymB7jsM5x-ilSfsIxx1ufjAHXEjTsuKgtInLe39vzFQrRfJnw3nkZa8PswbLoP1wClrcmmltYhIUQnSsM2aATJ4chFg-OkKSqTyRA4D8j0jmL2nsW7vLsDMJuVdbxtOfS-p4aqqWIf2lNUTYHqQQhcrfbl5aw2QUC2WKdF5f7KLKrrHH5mFF2ZpXABAXONc6FU0mCZm-ZziHJs_pbTzRBVTZor2vcA_BRv8zraEUcureHD-cfLfy3QlC-Blkr1v-BOtADyXxIBweuqvUkXec_YjT6H9IlKPdSMnw34HHAkK1r3M7GWfM4MQ4EL0wg";
}
