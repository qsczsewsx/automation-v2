package common;

import com.automation.cas.*;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.automation.login.LoginApi;
import com.automation.login.TheUserInfo;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.screenplay.Actor;
import org.jetbrains.annotations.NotNull;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.automation.config.iotp.IOTPConfig.IOTP_AUTHEN;
import static com.automation.config.iotp.IOTPConfig.IOTP_DATAPOWER_DOMAIN;
import static com.automation.tools.ConvertUtils.fileTxtToString;
import static com.automation.tools.FormatUtils.syncData;
import static common.DatesUtils.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hibernate.internal.CoreLogging.logger;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CommonUtils {
  private static final String ONLINE = "ONLINE";
  private static final String FULL_NAME = "fullName";
  private static final String PHONE_NUMBER = "phoneNumber";
  private static final String ID_NUMBER = "idNumber";
  private static final String EMAIL = "email";
  private static final String BIRTHDAY = "birthday";
  private static final String IDENTITY_CARD = "identityCard";
  private static final String BANK_ACCOUNTS = "bankAccounts";
  private static final String ACCOUNT_NO = "accountNo";
  private static final String ACCOUNT_NAME = "accountName";
  private static final String PERSONAL_INFO = "personalInfo";
  private static final String USERNAME = "105C300126";
  private static final String AUTHORIZATION = "Authorization";
  private static final String BEARER = "Bearer ";
  private static final String xxxxID = "xxxxId";
  private static final String PROCESSING = "PROCESSING";
  private static final String FOCUS_AMOPS_MAKER = "FOCUS_AMOPS_MAKER";
  private static final String PHONE = "phone";
  private static final String USERID = "userId";
  private static final String BANK_CODE = "bankCode";
  private static final String BANK_NAME = "bankName";
  private static final String BANK_PROVINCE = "bankProvince";
  private static final String BRANCH_CODE = "branchCode";
  private static final String CODE105C = "code105C";
  private static final String END_DATETIME = "endDatetime";
  private static final String BE_ACTION = "beAction";
  private static final String STR_CREATE = "CREATE";
  private static final String STATUS = "status";
  private static final String ACCOUNT_TYPE = "accountType";
  private static final String ACCOUNT ="ACCOUNT";
  private static final String START_DATETIME = "startDatetime";
  private static final String ADDRESS = "address";
  private static final String ACTOR = "actor";

  private static final ObTask obTask = new ObTask();
  private static Random rand = null;  // SecureRandom is preferred to Random

  static {
    try {
      rand = SecureRandom.getInstanceStrong();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
  }

  public static String getUserType(String value) {
    String type;
    if (value.contains("0")) {
      type = "INDIVIDUAL";
    } else {
      type = "ORGANIZATION";
    }
    return type;
  }

  public static String getUserStatus(String value) {
    String status;
    if (value.contains("1")) {
      status = "ACTIVE";
    } else if (value.contains("-1")) {
      status = "INACTIVE";
    } else {
      status = "LOCKED";
    }
    return status;
  }

  public static String getAccountType(String value) {
    String accountType;
    if (value != null) {
      accountType = "ADVANCED";
    } else {
      accountType = "BETA";
    }
    return accountType;
  }

  public static String getGender(String genderId) {
    String gender;
    if (genderId.equalsIgnoreCase("0")) {
      gender = "FEMALE";
    } else {
      gender = "MALE";
    }
    return gender;
  }

  public static String getUserId(String key, String value) {

    String userId;
    switch (key.toLowerCase()) {
      case "xxxxid":
        userId = xxxxUser.getByxxxxId(value).getId().toString();
        break;
      case CODE105C:
        userId = xxxxUser.getByUserName(value).getId().toString();
        break;
      case PHONE:
        userId = xxxxUser.getByPhoneNumber(value).getId().toString();
        break;
      case EMAIL:
        userId = xxxxUser.getByEmail(value).getId().toString();
        break;
      case ID_NUMBER:
        userId = xxxxIdentification.getByIdNumber(value).getUserId().toString();
        break;
      default:
        logger("Invalid key : " + key);
        userId = "";
        break;
    }
    return userId;
  }

  public static String getChannel(String channelValue) {
    String channel;
    if (channelValue.equalsIgnoreCase("0")) {
      channel = ONLINE;
    } else if (channelValue.equalsIgnoreCase("1")) {
      channel = "RM";
    } else {
      channel = "IA";
    }
    return channel;
  }

  public static String checkDataIsNullOrNot(Object object) {
    String data;
    if (object != null) {
      data = object.toString();
    } else {
      data = "";
    }
    return data;
  }

  public static String checkUserIdInput(String key, String value) {
    String userId;
    if (key.equalsIgnoreCase(USERID)) {
      userId = value;
    } else {
      userId = getUserId(key, value);
    }
    return userId;
  }

  public static String getDesiredData(String testCaseName, String containsText, String desiredData, String actualData) {
    String getData;
    if (testCaseName.contains(containsText)) {
      getData = desiredData;
    } else {
      getData = actualData;
    }
    return getData;
  }

  public static String getDesiredxxxxId(String xxxxId, String getxxxxId) {
    if (xxxxId.equalsIgnoreCase("getxxxxId")) {
      xxxxId = getxxxxId;
    } else {
      xxxxId = syncData(xxxxId);
    }
    return xxxxId;
  }

  public static String getPhoneOrCode105C(String key, Object value, String phonedata) {
    String data;
    String prepareData = String.valueOf(new Date().getTime());
    if (value != null) {
      if (key.equalsIgnoreCase(PHONE)) {
        if (value.toString().equalsIgnoreCase("valid")) {
          data = prepareData.substring(1, 13);
        } else if (value.toString().equalsIgnoreCase("redis")) {
          data = phonedata.substring(1, 13);
        } else {
          data = value.toString();
        }
      } else {
        if (value.toString().equalsIgnoreCase("valid")) {
          data = "105C" + prepareData.substring(7, 13);
        } else if (value.toString().equalsIgnoreCase("redis")) {
          data = "105C" + phonedata.substring(7, 13);
        } else {
          data = value.toString();
        }
      }
    } else {
      data = "";
    }
    return data;
  }

  public static String getDerivativeActivationStatus(String value) {
    String status;
    switch (value) {
      case "NOT_VSD_ACTIVATE_YET":
        status = "0";
        break;
      case "VSD_ACTIVATED":
        status = "1";
        break;
      case "NOT_SEND_VSD_OPEN_REQUEST_YET":
        status = "2";
        break;
      case "SENT_VSD_OPEN_REQUEST":
        status = "3";
        break;
      case "VSD_REJECT_OPEN":
        status = "4";
        break;
      case "OPS_REJECT_OPEN_REQUEST":
        status = "7";
        break;
      case "CANCEL_OPEN_REQUEST":
        status = "8";
        break;
      case "SEND_OPEN_REQUEST_ERROR":
        status = "9";
        break;
      case "CLOSED":
        status = "10";
        break;
      case "NOT_SEND_VSD_CLOSE_REQUEST_YET":
        status = "20";
        break;
      case "SENT_VSD_CLOSE_REQUEST":
        status = "30";
        break;
      case "VSD_REJECT_CLOSE":
        status = "40";
        break;
      default:
        status = null;
    }
    return status;
  }


  public static String getFileContents(String file) {
    String fileContents;
    if (file.equalsIgnoreCase("front")) {
      fileContents = fileTxtToString("src/test/resources/data/newfastmobile/frontIdentity");
    } else if (file.equalsIgnoreCase("back")) {
      fileContents = fileTxtToString("src/test/resources/data/newfastmobile/backIdentity");

    } else {
      fileContents = file;
    }
    return fileContents;
  }

  public static LinkedHashMap<String, Object> getFMBRegisterBetaBody(String idNumber) {

    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    LinkedHashMap<String, Object> personalInfo = new LinkedHashMap<>();
    LinkedHashMap<String, Object> identityCard = new LinkedHashMap<>();
    List<LinkedHashMap<String, Object>> bankAccounts = new ArrayList<>();

    identityCard.put(ID_NUMBER, idNumber);
    identityCard.put("idPlace", "CA. Thái Nguyên");
    identityCard.put("idDate", "2009-01-01T00:00:00.000Z");

    personalInfo.put("nationality", "VN");
    personalInfo.put(FULL_NAME, "NGUYEN VAN A");
    personalInfo.put(EMAIL, "theanh28" + idNumber.substring(6, 12) + "@gmail.com");
    personalInfo.put(PHONE_NUMBER, "03" + idNumber.substring(4, 12));
    personalInfo.put(BIRTHDAY, "1990-01-01T00:00:00.000Z");
    personalInfo.put(IDENTITY_CARD, identityCard);

    LinkedHashMap<String, Object> bankAccount = new LinkedHashMap<>();
    bankAccount.put(ACCOUNT_NO, "19" + idNumber.substring(1, 12));
    bankAccount.put(ACCOUNT_NAME, "NGUYEN VAN A");
    bankAccount.put(BANK_CODE, "01310001");
    bankAccount.put(BANK_NAME, "NH TMCP KY THUONG VN");
    bankAccount.put(BANK_PROVINCE, null);
    bankAccount.put(BRANCH_CODE, null);
    bankAccount.put("bankType", "CENTRALIZED_PAYMENT");
    bankAccounts.add(bankAccount);

    body.put(PERSONAL_INFO, personalInfo);
    body.put(BANK_ACCOUNTS, bankAccounts);

    return body;
  }

  public static LinkedHashMap<String, Object> getUpgradeAdvancedBody() {

    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    LinkedHashMap<String, Object> personalInfo = new LinkedHashMap<>();
    LinkedHashMap<String, Object> identityCard = new LinkedHashMap<>();
    String frontIdentity = getFileContents("front");
    String backIdentity = getFileContents("back");
    identityCard.put("frontIdentityFilePath", "");
    identityCard.put("backIdentityFilePath", "");
    identityCard.put("frontIdentity", frontIdentity);
    identityCard.put("backIdentity", backIdentity);

    personalInfo.put("contactAddress", "219 Trung Kính, Cầy Giấy, Hà Nội");
    personalInfo.put("province", "HA-NOI");
    personalInfo.put(IDENTITY_CARD, identityCard);
    body.put(PERSONAL_INFO, personalInfo);

    return body;
  }

  public static void deleteFMBRegisterBetaData(String phoneNumber, String idNumber, String email) {

    if (!phoneNumber.equalsIgnoreCase("0253652563")) {
      if (!xxxxUser.getListByPhoneNumber(phoneNumber).isEmpty()) {
        String userId = xxxxUser.getByPhoneNumber(phoneNumber).getId().toString();
        xxxxIdentification.deleteByIdNumber(idNumber);
        xxxxNewOnboardingStatus.deleteByUserId(userId);
        xxxxUser.deleteByPhone(phoneNumber);
        xxxxUserOpenAccountQueue.deleteByPhone(phoneNumber);
      }
    }

    if (!idNumber.equalsIgnoreCase("0253652563")) {
      if (!xxxxIdentification.getListByIdNumber(idNumber).isEmpty()) {
        String userId = xxxxIdentification.getByIdNumber(idNumber).getUserId().toString();
        String phone = xxxxUser.getById(new BigDecimal(userId)).getPhone();
        xxxxIdentification.deleteByIdNumber(idNumber);
        xxxxNewOnboardingStatus.deleteByUserId(userId);
        xxxxUser.deleteByPhone(phone);
        xxxxUserOpenAccountQueue.deleteByPhone(phone);
      }
    }

    if (!email.equalsIgnoreCase("nguyenvanaaa@gmail.com")) {
      if (!xxxxUser.getListByEmail(email).isEmpty()) {
        String userId = xxxxUser.getByEmail(email).getId().toString();
        xxxxIdentification.deleteByIdNumber(idNumber);
        xxxxNewOnboardingStatus.deleteByUserId(userId);
        xxxxUser.deleteByPhone(phoneNumber);
        xxxxUserOpenAccountQueue.deleteByPhone(phoneNumber);
      }

    }
  }

  public static void verifyFMBBasicInfo(Response response, String responsePath, String key, String value) {

    Map<String, Object> getResponse = response.jsonPath().getMap(responsePath);
    String userId = checkUserIdInput(key, value);
    xxxxUser xxxxUser = xxxxUser.getById(new BigDecimal(userId));
    assertEquals(xxxxUser.getxxxxid(), getResponse.get(xxxxID));
    assertEquals(xxxxUser.getUsername(), getResponse.get(CODE105C));
    String status = getUserStatus(xxxxUser.getAccountStatus().toString());
    assertEquals(status, getResponse.get(STATUS));
    String type = getUserType(xxxxUser.getCustype().toString());
    assertEquals(type, getResponse.get("type"));
    String accountType = getAccountType(xxxxUser.getUsername());
    assertEquals(accountType, getResponse.get(ACCOUNT_TYPE));
  }

  public static void verifyOnboardingBasicInfo(Response response, String responsePath, String key, String value) {

    Map<String, Object> getResponse = response.jsonPath().getMap(responsePath);
    String userId = checkUserIdInput(key, value);
    xxxxUser xxxxUser = xxxxUser.getById(new BigDecimal(userId));
    assertEquals(xxxxUser.getxxxxid(), getResponse.get(xxxxID));
    assertEquals(xxxxUser.getUsername(), getResponse.get(CODE105C));
    String status = getUserStatus(xxxxUser.getAccountStatus().toString());
    assertEquals(status, getResponse.get(STATUS));
    String type = getUserType(xxxxUser.getCustype().toString());
    assertEquals(type, getResponse.get("type"));
  }

  public static void verifyFMBOnboadingStatusValue(List<xxxxNewOnboardingStatus> xxxxNewOnboardingStatusList, int i, Map<String, Object> getResponse) {

    if (getResponse != null) {
      assertEquals(xxxxNewOnboardingStatusList.get(i).getStatusValue(), getResponse.get("value"));
      assertEquals(xxxxNewOnboardingStatusList.get(i).getRejectReason(), getResponse.get("rejectReason"));
      assertEquals(xxxxNewOnboardingStatusList.get(i).getRejectContent(), getResponse.get("rejectContent"));
      assertEquals(xxxxNewOnboardingStatusList.get(i).getRejectPerson(), getResponse.get("rejectPerson"));
    }
  }

  public static void verifyFMBRegIAStatus(Response response, String userId) {
    Map<String, Object> getResponse = response.jsonPath().getMap("accountStatus.iaStatus");
    xxxxBankIaaccount xxxxBankIaaccount = xxxxBankIaaccount.getBank(userId);
    assertEquals(xxxxBankIaaccount.getAutoTransfer(), getResponse.get("autoTransfer"));
    assertEquals(xxxxBankIaaccount.getIsIaPaid(), getResponse.get("isIAPaid"));
  }

  public static void verifyFMBOnboardingStatus(Response response, String responsePath, String key, String value) {
    Map<String, Object> getResponse;
    String userId = checkUserIdInput(key, value);
    List<xxxxNewOnboardingStatus> xxxxNewOnboardingStatusList = xxxxNewOnboardingStatus.getByUserId(new BigDecimal(userId));
    if (!xxxxNewOnboardingStatusList.isEmpty()) {
      for (int i = 0; i < xxxxNewOnboardingStatusList.size(); i++) {
        String statusKey = xxxxNewOnboardingStatusList.get(i).getStatusKey();
        switch (statusKey) {
          case "ACTIVATED_STATUS":
            getResponse = response.jsonPath().getMap(responsePath + "activatedStatus");
            verifyFMBOnboadingStatusValue(xxxxNewOnboardingStatusList, i, getResponse);
            break;
          case "GEN_105C":
            getResponse = response.jsonPath().getMap(responsePath + "gen105c");
            verifyFMBOnboadingStatusValue(xxxxNewOnboardingStatusList, i, getResponse);
            break;
          case "PREFER_ACTIVATION_CHANNEL":
            getResponse = response.jsonPath().getMap(responsePath + "preferActivationChannelStatus");
            verifyFMBOnboadingStatusValue(xxxxNewOnboardingStatusList, i, getResponse);
            break;
          case "ID_STATUS":
            getResponse = response.jsonPath().getMap(responsePath + "idStatus");
            verifyFMBOnboadingStatusValue(xxxxNewOnboardingStatusList, i, getResponse);
            break;
          case "BANK_INFO_STATUS":
            getResponse = response.jsonPath().getMap(responsePath + "bankInfoStatus");
            verifyFMBOnboadingStatusValue(xxxxNewOnboardingStatusList, i, getResponse);
            break;
          case "COUNTER_KYC_STATUS":
            getResponse = response.jsonPath().getMap(responsePath + "counterKycStatus");
            verifyFMBOnboadingStatusValue(xxxxNewOnboardingStatusList, i, getResponse);
            break;
          case "DOCUMENT_STATUS":
            getResponse = response.jsonPath().getMap(responsePath + "documentStatus");
            verifyFMBOnboadingStatusValue(xxxxNewOnboardingStatusList, i, getResponse);
            break;
          case "EKYC_STATUS":
            getResponse = response.jsonPath().getMap(responsePath + "ekycStatus");
            verifyFMBOnboadingStatusValue(xxxxNewOnboardingStatusList, i, getResponse);
            break;
          case "ECONTRACT_STATUS":
            getResponse = response.jsonPath().getMap(responsePath + "econtractStatus");
            verifyFMBOnboadingStatusValue(xxxxNewOnboardingStatusList, i, getResponse);
            break;
          case "PRIVATE_ACCOUNT_STATUS":
            getResponse = response.jsonPath().getMap(responsePath + "privateAccountStatus");
            verifyFMBOnboadingStatusValue(xxxxNewOnboardingStatusList, i, getResponse);
            break;
          default:
            logger("Can not find : " + statusKey);
        }
      }
    }

  }

  public static void verifyFMBankInfo(Response response, String responsePath, String key, String value) {
    Map<String, Object> getResponse = response.jsonPath().getMap(responsePath);
    if (getResponse != null) {
      String userId = checkUserIdInput(key, value);
      xxxxBankSubaccount xxxxBankSubaccount = xxxxBankSubaccount.getBank(userId);
      assertEquals(xxxxBankSubaccount.getId().toString(), getResponse.get("bankAccountId").toString());
      assertEquals(xxxxBankSubaccount.getAccountNo(), getResponse.get(ACCOUNT_NO));
      assertEquals(xxxxBankSubaccount.getAccountName(), getResponse.get(ACCOUNT_NAME));
      assertEquals(xxxxBankSubaccount.getBankCode(), getResponse.get(BANK_CODE));
      assertEquals(xxxxBankSubaccount.getBankName(), getResponse.get(BANK_NAME));
      assertEquals(xxxxBankSubaccount.getBankprovince(), getResponse.get(BANK_PROVINCE));
      assertEquals(xxxxBankSubaccount.getBankBranch(), getResponse.get(BRANCH_CODE));
      assertEquals(xxxxBankSubaccount.getAccountType(), getResponse.get(ACCOUNT_TYPE));
    }

  }

  public static void verifyFMBankIaInfo(Response response, String responsePath, String key, String value) {
    Map<String, Object> getResponse = response.jsonPath().getMap(responsePath);
    if (getResponse != null) {
      String userId = checkUserIdInput(key, value);
      xxxxBankIaaccount xxxxBankIaaccount = xxxxBankIaaccount.getBank(userId);
      assertEquals(xxxxBankIaaccount.getAccountNo(), getResponse.get(ACCOUNT_NO));
      assertEquals(xxxxBankIaaccount.getAccountName(), getResponse.get(ACCOUNT_NAME));
      assertEquals(xxxxBankIaaccount.getBankCode(), getResponse.get(BANK_CODE));
      assertEquals(xxxxBankIaaccount.getBankName(), getResponse.get(BANK_NAME));
      assertEquals(xxxxBankIaaccount.getBankprovince(), getResponse.get(BANK_PROVINCE));
      assertEquals(xxxxBankIaaccount.getBankBranch(), getResponse.get(BRANCH_CODE));
      assertEquals(xxxxBankIaaccount.getBankSource(), getResponse.get("bankSource"));
    }

  }

  public static void verifyBankSource(Response response, String responsePath, String key, String value) {
    Map<String, Object> getResponse = response.jsonPath().getMap(responsePath);
    if (getResponse != null) {
      String userId = checkUserIdInput(key, value);
      xxxxBankIaaccount xxxxBankIaaccount = xxxxBankIaaccount.getBank(userId);
      assertThat(getResponse.get("bankSource"), is(xxxxBankIaaccount.getBankSource()));
    }
  }

  public static void verifyPRODPersonalInfo(Response response, String responsePath, String key, String value) {

    Map<String, Object> getResponse = response.jsonPath().getMap(responsePath);
    if (getResponse != null) {
      String userId = checkUserIdInput(key, value);
      xxxxUser xxxxUser = xxxxUser.getById(new BigDecimal(userId));
      assertEquals(xxxxUser.getLastname() + " " + xxxxUser.getFirstname(), getResponse.get(FULL_NAME));
      assertEquals(xxxxUser.getFirstname(), getResponse.get("firstName"));
      assertEquals(xxxxUser.getLastname(), getResponse.get("lastName"));
      assertEquals(xxxxUser.getEmail(), getResponse.get(EMAIL));
      assertEquals(xxxxUser.getPhone(), getResponse.get(PHONE_NUMBER));
      String gender = getGender(String.valueOf(xxxxUser.getGender()));
      assertEquals(gender, getResponse.get("gender"));
      assertEquals(covertDateToString(xxxxUser.getBirthday()), convertTimestampToString((getResponse.get(BIRTHDAY).toString())));
      assertEquals(xxxxUser.getId().toString(), getResponse.get(USERID).toString());
      assertEquals(covertDateToString(xxxxUser.getCreatedDate()), convertTimestampToString(getResponse.get("createdDate").toString()));
      assertEquals(covertDateToString(xxxxUser.getUpdatedDate()), convertTimestampToString(getResponse.get("updatedDate").toString()));
      if (getResponse.get("honorific") != null) {
        assertEquals(xxxxUser.getHonorific().toString(), getResponse.get("honorific").toString());
      }

    } else {
      Map<String, Object> getProfile = response.jsonPath().getMap("profileDto");
      assertTrue(getProfile.containsKey(PERSONAL_INFO));
    }
  }

  public static void verifyOnboaringPersonalInfo(Response response, String responsePath, String key, String value) {

    Map<String, Object> getResponse = response.jsonPath().getMap(responsePath);
    if (getResponse != null) {
      String userId = checkUserIdInput(key, value);
      xxxxUser xxxxUser = xxxxUser.getById(new BigDecimal(userId));
      xxxxIdentification xxxxIdentification = xxxxIdentification.getByUserId(userId);
      assertEquals(xxxxUser.getFirstname(), getResponse.get("firstName"));
      assertEquals(xxxxUser.getLastname(), getResponse.get("lastName"));
      assertEquals(xxxxUser.getLastname() + " " + xxxxUser.getFirstname(), getResponse.get(FULL_NAME));
      assertEquals(xxxxUser.getEmail(), getResponse.get(EMAIL));
      assertEquals(xxxxUser.getPhone(), getResponse.get(PHONE_NUMBER));
      assertEquals(xxxxIdentification.getIdNumber(), response.jsonPath().get(responsePath + ".identityCard.idNumber"));
    }
  }

  public static void verifyBankSubAccountsInfo(Response response, String responsepath, String key, String value) {

    String userId = checkUserIdInput(key, value);
    List<HashMap<String, Object>> getResponses = response.jsonPath().getList(responsepath);
    if (!getResponses.isEmpty()) {
      for (HashMap<String, Object> getRes : getResponses) {
        xxxxBankSubaccount xxxxBankSubaccount = xxxxBankSubaccount.getBankByAccountNoAndUserId(getRes.get(ACCOUNT_NO).toString(), userId).get(0);
        assertEquals(xxxxBankSubaccount.getAccountName(), getRes.get(ACCOUNT_NAME));
        assertEquals(xxxxBankSubaccount.getBankCode(), getRes.get(BANK_CODE));
        if (xxxxBankSubaccount.getUserType().equalsIgnoreCase("NORMAL")) {
          assertEquals(xxxxBankSubaccount.getAccountType(), getRes.get(ACCOUNT_TYPE));
        } else {
          assertEquals(xxxxBankSubaccount.getUserType() + "_" + xxxxBankSubaccount.getAccountType(), getRes.get(ACCOUNT_TYPE));
        }

        assertEquals(xxxxBankSubaccount.getStatus().toString(), getRes.get(STATUS).toString());
      }
    }
  }

  public static void verifyPRODIdentityCard(Response response, String responsePath, String key, String value) {

    Map<String, Object> getResponse = response.jsonPath().getMap(responsePath);
    if (getResponse != null) {
      String userId = checkUserIdInput(key, value);
      xxxxIdentification xxxxIdentification = xxxxIdentification.getByUserId(userId);
      assertEquals(xxxxIdentification.getIdNumber(), getResponse.get(ID_NUMBER));
      assertEquals(xxxxIdentification.getIdPlace(), getResponse.get("idPlace"));
      assertEquals(covertDateToString(xxxxIdentification.getIdDate()), convertTimestampToString((getResponse.get("idDate").toString())));
    } else {
      Map<String, Object> getProfile = response.jsonPath().getMap("profileDto.personalInfo");
      assertTrue(getProfile.containsKey(IDENTITY_CARD));
    }
  }

  public static void verifyPRODScanIdFiles(Response response, String responsePath, String key, String value) {

    List<HashMap<String, Object>> getResponseList = response.jsonPath().getList(responsePath);
    if (!getResponseList.isEmpty()) {
      String userId = checkUserIdInput(key, value);
      List<xxxxUserUpload> xxxxUserUpload = xxxxUserUpload.getByUserId(userId);
      for (int i = 0; i < xxxxUserUpload.size(); i++) {
        assertEquals(xxxxUserUpload.get(i).getOriginalName(), getResponseList.get(i).get("fileName").toString());
        assertEquals(xxxxUserUpload.get(i).getFileAlias(), getResponseList.get(i).get("fileAlias").toString());
        assertEquals(xxxxUserUpload.get(i).getFileType(), getResponseList.get(i).get("fileType").toString());
        String expected = covertDateToString((xxxxUserUpload.get(i).getSince()));
        String actual = convertTimestampToString(getResponseList.get(i).get("uploadDate").toString());
        assertEquals(expected, actual);
        assertEquals(xxxxUserUpload.get(i).getId().toString(), getResponseList.get(i).get("fileUploadId").toString());
        assertEquals(xxxxUserUpload.get(i).getObjectId(), getResponseList.get(i).get("objectId").toString());
      }

    }
  }

  public static void verifyPRODBankInfo(Response response, String responsePath, String key, String value) {
    List<HashMap<String, Object>> getResponseList = response.jsonPath().getList(responsePath);
    if (!getResponseList.isEmpty()) {
      String userId = checkUserIdInput(key, value);
      List<xxxxBankAccount> xxxxBankAccountList = xxxxBankAccount.getListBanks(userId);
      for (int i = 0; i < getResponseList.size(); i++) {
        assertEquals(xxxxBankAccountList.get(i).getId().toString(), getResponseList.get(i).get("bankAccountId").toString());
        assertEquals(xxxxBankAccountList.get(i).getBankAccountNo(), getResponseList.get(i).get(ACCOUNT_NO));
        assertEquals(xxxxBankAccountList.get(i).getBankAccountName(), getResponseList.get(i).get(ACCOUNT_NAME));
        assertEquals(xxxxBankAccountList.get(i).getBankCode(), getResponseList.get(i).get(BANK_CODE));
        assertEquals(xxxxBankAccountList.get(i).getBankName(), getResponseList.get(i).get(BANK_NAME));
        assertEquals(xxxxBankAccountList.get(i).getBankprovince(), getResponseList.get(i).get(BANK_PROVINCE));
        assertEquals(xxxxBankAccountList.get(i).getBankBranch(), getResponseList.get(i).get(BRANCH_CODE));
      }
    }

  }

  public static void verifyPRODContractTrust(Response response, String responsePath, String key, String value) {

    Map<String, Object> getResponse = response.jsonPath().getMap(responsePath);
    if (getResponse != null) {
      String userId = checkUserIdInput(key, value);
      String xxxxId = xxxxUser.getById(new BigDecimal(userId)).getxxxxid();
      xxxxContractTrust xxxxContractTrust = xxxxContractTrust.getByxxxxId(xxxxId);
      assertEquals(xxxxContractTrust.getxxxxid(), getResponse.get(xxxxID));
      assertEquals(xxxxContractTrust.getFullName(), getResponse.get(FULL_NAME));
      assertEquals(xxxxContractTrust.getBirthday(), getResponse.get(BIRTHDAY));
      assertEquals(xxxxContractTrust.getNationnality(), getResponse.get("nationnality"));
      assertEquals(xxxxContractTrust.getPassportNo(), getResponse.get("passportNo"));
      assertEquals(xxxxContractTrust.getIssueDate(), getResponse.get("issueDate"));
      assertEquals(xxxxContractTrust.getIssuePlace(), getResponse.get("issuePlace"));
      assertEquals(xxxxContractTrust.getType(), getResponse.get("type"));
    }

  }

  public static void verifyPRODKycHistory(Response response, String responsePath, String key, String value) {
    List<HashMap<String, Object>> getResponseList = response.jsonPath().getList(responsePath);
    if (!getResponseList.isEmpty()) {
      String userId = checkUserIdInput(key, value);
      List<ObTask> obTaskList = ObTask.getListByUserId(new BigDecimal(userId));
      for (int i = 0; i < getResponseList.size(); i++) {
        assertEquals(obTaskList.get(i).getId().toString(), getResponseList.get(i).get("taskId").toString());
        assertEquals(obTaskList.get(i).getUserId().toString(), getResponseList.get(i).get(USERID).toString());
        assertEquals(covertDateToString(obTaskList.get(i).getCreatedDatetime()), convertTimestampToString(getResponseList.get(i).get("createdDatetime").toString()));
        assertEquals(obTaskList.get(i).getStatus(), getResponseList.get(i).get(STATUS));
        assertEquals(obTaskList.get(i).getKycStatus(), getResponseList.get(i).get("kycStatus"));
        assertEquals(obTaskList.get(i).getActor(), getResponseList.get(i).get(ACTOR));
        assertEquals(covertDateToString(obTaskList.get(i).getStartDatetime()), convertTimestampToString(getResponseList.get(i).get(START_DATETIME).toString()));
        if (getResponseList.get(i).get(END_DATETIME) != null) {
          assertEquals(covertTimeStampToStringDate(obTaskList.get(i).getEndDatetime()), convertTimestampToStringWithFormat(getResponseList.get(i).get(END_DATETIME), "dd/MM/yyyy HH:mm:ss"));
        }
      }
    }
  }

  public static String getTaskId(String taskInput, String taskId) {
    String getTask;
    if (taskInput.equalsIgnoreCase("task")) {
      getTask = taskId;
    } else {
      getTask = taskInput;
    }
    return getTask;
  }

  public static String getAuthorization(String actor) {
    String getAuthor;
    if (actor.contains("checker")) {
      getAuthor = ASSIGN_TASK_TO_CHECKER_KEY;
    } else {
      getAuthor = ASSIGN_TASK_TO_MAKER_KEY;
    }
    return getAuthor;
  }

  public static void verifyRejectReasonsListField(List<HashMap<String, Object>> responseList, List<ObReason> obReasons) {

    for (int i = 0; i < responseList.size(); i++) {
      assertEquals(obReasons.get(i).getId().toString(), responseList.get(i).get("id").toString());
      assertEquals(obReasons.get(i).getReasonCode(), responseList.get(i).get("reasonCode"));
      assertEquals(obReasons.get(i).getReasonName(), responseList.get(i).get("reasonName"));
      assertEquals(obReasons.get(i).getActionCode(), responseList.get(i).get("actionCode"));
      assertEquals(obReasons.get(i).getReasonGroup(), responseList.get(i).get("reasonGroup"));
      assertEquals(obReasons.get(i).getDescription(), responseList.get(i).get("description"));
      assertEquals(obReasons.get(i).getChannelCode(), responseList.get(i).get("channelCode"));

    }
  }

  public static void verifyRejectReasonField(HashMap<String, Object> response, ObReason obReason) {

    assertEquals(obReason.getId().toString(), response.get("id").toString());
    assertEquals(obReason.getReasonCode(), response.get("reasonCode"));
    assertEquals(obReason.getReasonName(), response.get("reasonName"));
    assertEquals(obReason.getActionCode(), response.get("actionCode"));
    assertEquals(obReason.getReasonGroup(), response.get("reasonGroup"));
    assertEquals(obReason.getDescription(), response.get("description"));
  }

  public static List<ObReason> getListRejectReasons(String userId) {
    ObTask obTask = ObTask.getByUserIdAndKycStatus(userId, "REJECT").get(0);
    List<ObTaskReason> obTaskReasons = ObTaskReason.getListByTaskId(obTask.getId().toString());
    List<ObReason> obReasons = new ArrayList<>();
    for (ObTaskReason obTaskReason : obTaskReasons) {
      ObReason obReason = ObReason.getById(obTaskReason.getReasonId().toString());
      obReasons.add(obReason);
    }
    return obReasons;
  }

  public static List<?> convertObjectToList(Object obj) {
    List<?> list = new ArrayList<>();
    if (obj.getClass().isArray()) {
      list = Arrays.asList((Object[]) obj);
    } else if (obj instanceof Collection) {
      list = new ArrayList<>((Collection<?>) obj);
    }
    return list;
  }

  public static HashMap<String, Object> prepareDataAddUserToWbl(String testCaseName, String prepareValue, String idNumber,
                                                                String startDatetime, String endDatetime, String type, String policyCode,
                                                                String refIdNumber, String fullName, String address) {

    List<HashMap<String, Object>> identifications = new ArrayList<>();
    LinkedHashMap<String, Object> identification1 = new LinkedHashMap<>();
    identification1.put(ID_NUMBER, idNumber);
    identification1.put(BE_ACTION, STR_CREATE);
    identifications.add(identification1);

    List<HashMap<String, Object>> actions = new ArrayList<>();
    LinkedHashMap<String, Object> action1 = new LinkedHashMap<>();
    action1.put("code", "DISCLOSURE_INFO");
    actions.add(action1);

    List<HashMap<String, Object>> policyItems = new ArrayList<>();
    LinkedHashMap<String, Object> policyItem1 = new LinkedHashMap<>();
    policyItem1.put("businessTitle", "Giám đốc điều hành");
    policyItem1.put(START_DATETIME, startDatetime);
    policyItem1.put(END_DATETIME, endDatetime);
    policyItem1.put("actions", actions);
    policyItem1.put(BE_ACTION, STR_CREATE);
    policyItems.add(policyItem1);

    List<HashMap<String, Object>> policies = new ArrayList<>();
    LinkedHashMap<String, Object> policy1 = new LinkedHashMap<>();
    policy1.put("type", type);
    policy1.put("policyCode", policyCode);
    policy1.put("note", "note ghi chú");
    policy1.put("policyItems", policyItems);
    if (testCaseName.contains("type as NLQ")) {
      policy1.put("refIdNumber", refIdNumber);
    }
    policies.add(policy1);

    List<HashMap<String, Object>> stakeHolders = new ArrayList<>();
    LinkedHashMap<String, Object> stakeHolder1 = new LinkedHashMap<>();
    stakeHolder1.put("companyName", "Công ty chứng khoán" + prepareValue.substring(4));
    stakeHolder1.put("companyIdNumber", "GPKD" + prepareValue);
    stakeHolder1.put(ADDRESS, "312 Bà Triệu, HBT");
    stakeHolder1.put("capitalRatio", "51%");
    stakeHolder1.put("fromDate", "2021-08-30");
    stakeHolder1.put(BE_ACTION, STR_CREATE);
    stakeHolders.add(stakeHolder1);

    HashMap<String, Object> hashMapBody = new HashMap<>();
    hashMapBody.put(FULL_NAME, fullName);
    hashMapBody.put(ADDRESS, address);
    if (!testCaseName.contains("missing param identifications")) {
      hashMapBody.put("identifications", identifications);
    }
    if (!testCaseName.contains("missing param policies")) {
      hashMapBody.put("policies", policies);
    }
    if (!testCaseName.contains("missing param stakeholders")) {
      hashMapBody.put("stakeHolders", stakeHolders);
    }
    return hashMapBody;
  }

  public static LinkedHashMap<String, Object> getRegisterIABody(String testCaseName, String accountNo, String accountName,
                                                                String bankCode, String bankName, String bankType,
                                                                String autoTransfer, String isIAPaid, String action, String isIa) {

    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    LinkedHashMap<String, Object> regIA = new LinkedHashMap<>();
    List<LinkedHashMap<String, Object>> bankAccounts = new ArrayList<>();

    if (testCaseName.contains("missing Body")) {
      body = new LinkedHashMap<>();
    } else {
      LinkedHashMap<String, Object> bankAccount = new LinkedHashMap<>();
      bankAccount.put(ACCOUNT_NO, getAccountNoData(testCaseName, accountNo));
      bankAccount.put(ACCOUNT_NAME, accountName);
      bankAccount.put(BANK_CODE, bankCode);
      bankAccount.put(BANK_NAME, bankName);
      bankAccount.put(BANK_PROVINCE, null);
      bankAccount.put(BRANCH_CODE, null);
      bankAccount.put("bankType", bankType);
      bankAccounts.add(bankAccount);

      regIA.put("autoTransfer", autoTransfer);
      regIA.put("isIAPaid", isIAPaid);
      regIA.put("isIA", isIa);
      body.put("iaBankAccount", bankAccounts);
      body.put("action", action);
      body.put("iaStatus", regIA);
    }
    return body;
  }

  public static String getAccountNoData(String testCaseName, String accountNo) {
    String idNumberVal = String.valueOf(new Date().getTime());
    String getData;
    if (testCaseName.contains("accountNo equal 40 character")) {
      getData = "0" + idNumberVal + idNumberVal + idNumberVal;
    } else {
      getData = accountNo;
    }
    return getData;
  }

  public static HashMap<String, Object> getConfirmBookingFancy105CBody(String testCaseName, String phone, String code105C) {

    HashMap<String, Object> body = new HashMap<>();
    if (testCaseName.contains("missing BODY")) {
      body = new HashMap<>();
    } else if (testCaseName.contains("missing param phone")) {
      body.put(CODE105C, code105C);
    } else if (testCaseName.contains("missing param code105C")) {
      body.put(PHONE, phone);
    } else {
      body.put(PHONE, phone);
      body.put(CODE105C, code105C);
    }
    return body;
  }

  public static List<HashMap<String, Object>> convertProvincesListJsonToHashmapList(String filePath) {

    List<HashMap<String, Object>> hashMapList = new ArrayList<>();
    JSONParser parser = new JSONParser();
    Map<String, List<Object>> div = null;
    try {
      div = (Map<String, List<Object>>) parser.parse(new FileReader(filePath));
    } catch (IOException | ParseException e) {
      e.printStackTrace();
    }
    assert div != null;
    List<Object> objectList = div.get("provinces");
    for (Object obj : objectList) {
      hashMapList.add((HashMap) obj);
    }
    return hashMapList;
  }

  public static String getBookId(String bookInput, String bookId) {
    String getBook;
    if (bookInput.equalsIgnoreCase("bookId")) {
      getBook = bookId;
    } else {
      getBook = bookInput;
    }
    return getBook;
  }

  public static xxxxUser getxxxxUserByUserName(String username) {
    return xxxxUser.getByUserName(username);
  }

  public static Map<String, Object> decodeToken(String token) {
    String[] pieces = token.split("\\.");
    String b64payload = pieces[1];
    String jsonString = new String(Base64.getDecoder().decode(b64payload));
    return new Gson().fromJson(
      jsonString, new TypeToken<HashMap<String, Object>>() {
      }.getType()
    );
  }

  public static String authenOTP(String xxxxId, String token) {
    HashMap<String, Object> authenInfo = new HashMap<>();
    authenInfo.put("otp", "111111");
    authenInfo.put("otpTypeName", "TOTP");
    authenInfo.put(xxxxID, xxxxId);
    Response resp = given()
      .baseUri(IOTP_DATAPOWER_DOMAIN + IOTP_AUTHEN)
      .contentType(ContentType.JSON)
      .header(AUTHORIZATION, BEARER + token)
      .body(authenInfo)
      .post();

    return resp.jsonPath().get("token").toString();
  }

  public static BigDecimal createTaskCloseAccount() {
    ObTaskClose obTaskClose = new ObTaskClose();
    obTaskClose.setType(ONLINE);
    obTaskClose.setCreatedDatetime(new Timestamp(System.currentTimeMillis()));
    obTaskClose.setUpdatedDatetime(new Timestamp(System.currentTimeMillis()));
    obTaskClose.setStatus(PROCESSING);
    obTaskClose.setPriority(new BigDecimal(1));
    obTaskClose.setActor(FOCUS_AMOPS_MAKER);
    obTaskClose.setCreatedByUser(ONLINE);

    obTaskClose.insert();
    BigDecimal taskId = ObTaskClose.getLatestTaskId(PROCESSING, FOCUS_AMOPS_MAKER).get(0).getId();

    ObUserClose obUserClose = new ObUserClose();
    obUserClose.setUserId(new BigDecimal(736));
    obUserClose.setStatus("1");
    obUserClose.setReason(null);
    obUserClose.setCreatedDate(new Timestamp(System.currentTimeMillis()));
    obUserClose.setCreatedBy("CREATOR");
    obUserClose.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
    obUserClose.setUpdatedBy("UPDATOR");
    obUserClose.setTaskCloseId(taskId);
    obUserClose.setBookId(null);

    obUserClose.insert();
    return taskId;
  }

  public static void deleteTaskCloseAccount(BigDecimal taskId) {
    ObTaskClose.deleteByObTaskCloseId(taskId);
    ObUserClose.deleteByObTaskCloseId(taskId);
  }

  public static String checkTaskExist(String userId) {

    String taskId = "emptyTask";
    int size = ObTask.getListByStatusNotDone(new BigDecimal(userId)).size();
    if (size != 0) {
      List<ObTask> obTaskList = ObTask.getListByStatusNotDone(new BigDecimal(userId));//
      for (ObTask obTask : obTaskList) {
        taskId = obTask.getId().toString();
      }
    }
    return taskId;
  }

  public static String createTaskNewOBAndAssignToMaker(String xxxxId) {
    String taskId;
    String userId = xxxxUser.getByxxxxId(xxxxId).getId().toString();
    if (!checkTaskExist(userId).equalsIgnoreCase("emptyTask")) {
      taskId = checkTaskExist(userId);
    } else {
      taskId = CommonUtils.createTaskOb(userId).toString();
    }
    return taskId;
  }

  public static BigDecimal createTaskOb(String userId) {
    obTask.setType(ONLINE);
    obTask.setChannelId(new BigDecimal(0));
    obTask.setActionId(new BigDecimal(8));
    obTask.setCreatedDatetime(new Timestamp(System.currentTimeMillis()));
    obTask.setUpdatedDatetime(new Timestamp(System.currentTimeMillis()));
    obTask.setStartDatetime(new Timestamp(System.currentTimeMillis()));
    obTask.setKycGroupDatetime(new Timestamp(System.currentTimeMillis()));
    obTask.setStatus(PROCESSING);
    obTask.setPriority(new BigDecimal(1));
    obTask.setActor(FOCUS_AMOPS_MAKER);
    obTask.setUserId(new BigDecimal(userId));
    obTask.setKycStatus("ATM");
    obTask.setKycFlow("NORMAL");

    obTask.insert();

    return ObTask.getByUserIdAndStatus(userId, PROCESSING).getId();
  }

  public static BigDecimal createTaskResignContract() {
    obTask.setType(ONLINE);
    obTask.setChannelId(new BigDecimal(0));
    obTask.setActionId(new BigDecimal(8));
    obTask.setCreatedDatetime(new Timestamp(System.currentTimeMillis()));
    obTask.setUpdatedDatetime(new Timestamp(System.currentTimeMillis()));
    obTask.setStartDatetime(new Timestamp(System.currentTimeMillis()));
    obTask.setKycGroupDatetime(new Timestamp(System.currentTimeMillis()));
    obTask.setStatus(PROCESSING);
    obTask.setPriority(new BigDecimal(1));
    obTask.setActor(FOCUS_AMOPS_MAKER);
    obTask.setUserId(new BigDecimal(5285560));
    obTask.setKycStatus("ATM");
    obTask.setKycFlow("RESIGN_CONTRACT");

    obTask.insert();

    return ObTask.getByUserIdAndStatus("5285560", PROCESSING).getId();
  }

  public static BigDecimal uploadCloseDocs(String taskId) {

    String fileContents = fileTxtToString("src/test/resources/requestBody/CloseAccountContent");
    String fileName = "uploadDoc.xlsx";

    HashMap<String, Object> body = new LinkedHashMap<>();
    body.put("taskId", taskId);
    body.put("fileName", fileName);
    body.put("fileContents", fileContents);

    given()
      .baseUri(UPLOAD_DOC_CLOSE)
      .header(AUTHORIZATION, BEARER + CLOSE_ACCOUNT_UPLOAD_DOC)
      .contentType("application/json")
      .body(body)
      .post();

    return ObUserCloseDocs.getCloseDocs(taskId).get(0).getId();
  }

  public static void verifyGetMultiIA(Response response, String condition) {
    if (condition.equalsIgnoreCase("abcdefgh")) {
      assertThat("verify get null", response.jsonPath().get("basicInfo"), is(nullValue()));
    } else {
      for (String item : condition.split(",", -1)) {
        assertThat(response.jsonPath().getMap(""), hasKey(item));
      }
    }
    if (response.jsonPath().get(PERSONAL_INFO) != null) {
      assertThat("verify fullName",
        response.jsonPath().get("personalInfo.fullName"), is(notNullValue())
      );
    }
    if (response.jsonPath().get(BANK_ACCOUNTS) != null) {
      List<HashMap<String, Object>> bankAccounts = response.jsonPath().getList(BANK_ACCOUNTS);
      for (HashMap<String, Object> bankAccount : bankAccounts) {
        assertThat(bankAccount.get(ACCOUNT_NAME), is(notNullValue()));
      }
    }
    if (response.jsonPath().get("iaBankAccount") != null) {
      assertThat("verify iaBankAccount",
        response.jsonPath().get("iaBankAccount.isIaPaid"), is(notNullValue()));
    }
    if (response.jsonPath().get("userSetting") != null) {
      assertThat("verify prodIaPriority",
        response.jsonPath().get("userSetting.prodIaPriority"), is(notNullValue())
      );
    }
  }

  public static void verifyGetListSession(Response response) {
    String userId = xxxxUser.getByUserName(USERNAME).getId().toString();
    List<HashMap<String, Object>> taskSession = response.jsonPath().getList("");
    xxxxAuthenInfo xxxxAuthenInfo = xxxxAuthenInfo.getSessionByUserId(userId).get(0);
    for (HashMap<String, Object> session : taskSession) {
      if (session.get("sessionId").toString().equalsIgnoreCase(xxxxAuthenInfo.getSessionId())) {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        DateFormat df = new SimpleDateFormat(pattern);
        assertThat(session.get("loginTime"), is(df.format(xxxxAuthenInfo.getLoginTime())));
        assertTrue(session.get("ip").toString().contains(xxxxAuthenInfo.getIp()));
        assertThat(session.get("device"), is(xxxxAuthenInfo.getDevice()));
        assertThat(session.get(USERID), is(xxxxAuthenInfo.getUserId()));
      }
    }
  }

  public static List<String> getSessionID(String sessionID) {
    String token = getTokenAuthenSOtp("10000025745");
    return getStrings(sessionID, token);
  }

  public static String getToken(String username) {
    Actor actor = Actor.named("linhntm");
    LoginApi.withCredentials(username, "abc123").performAs(actor);
    return TheUserInfo.aboutLoginData().answeredBy(actor).getToken();
  }

  public static String getTokenAuthenSOtp(String xxxxId) {
    String token = CommonUtils.getToken(USERNAME);
    String duration = "0";
    String otp = "111111";
    String otpTypeName = "SMS";
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("duration", duration);
    body.put("otp", otp);
    body.put("otpTypeName", otpTypeName);
    body.put(xxxxID, xxxxId);

    Response response = given()
      .baseUri(ADD_OTP)
      .header(AUTHORIZATION, BEARER + token)
      .contentType("application/json")
      .body(body)
      .post();
    return response.jsonPath().get("token");
  }

  public static List<String> getSessionIDFromLogin(String sessionID) {
    String token = getToken(USERNAME);
    return getStrings(sessionID, token);
  }

  @NotNull
  private static ArrayList<String> getStrings(String sessionID, String token) {
    Map<String, Object> claims = CommonUtils.decodeToken(token);
    if (sessionID.equalsIgnoreCase("sessionID")) {
      sessionID = (String) claims.get("sessionID");
    } else {
      sessionID = syncData(sessionID);
    }
    return new ArrayList<>(Arrays.asList(sessionID.split(",")));
  }

  public static String createxxxxIdLearning(String xxxxId) {
    R3RdElearning r3rdElearning = new R3RdElearning();
    r3rdElearning.setCourseCode("EL0123123");
    r3rdElearning.setxxxxId("0001100103");
    r3rdElearning.setFullName("Lê Phương Mai");
    r3rdElearning.setCourseStatus(new BigDecimal("1"));
    r3rdElearning.setCreatedDate(new Timestamp(System.currentTimeMillis()));
    r3rdElearning.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
    r3rdElearning.setUsername("105C100103");
    r3rdElearning.setRole("RM");
    r3rdElearning.setBankCode("ACB");
    r3rdElearning.insert();
    return xxxxId;
  }

  public static String genPhoneNumberByDateTime() {
    List<String> givenList = Arrays.asList("3", "5", "7", "8", "9");
    String randomElement = givenList.get(rand.nextInt(givenList.size()));
    String prepareValue = String.valueOf(new Date().getTime());
    return randomElement + prepareValue.substring(5);
  }

  public static String creatConfirmID(String partnerId, String partnerAccountId, String code105C, String idNumber, String birthday) {
    LinkedHashMap<String, Object> bodyLink = new LinkedHashMap<>();
    List<String> listLinkType = new ArrayList<>(Collections.emptyList());
    listLinkType.add(ACCOUNT);

    bodyLink.put("partnerId", partnerId);
    bodyLink.put("partnerAccountId", partnerAccountId);
    bodyLink.put(CODE105C, code105C);
    bodyLink.put(ID_NUMBER, idNumber);
    bodyLink.put(BIRTHDAY, birthday);
    bodyLink.put("linkType", listLinkType);

    given()
      .baseUri(PARTNERSHIP_ACCOUNT_LINK)
      .header("x-api-key", PARTNERSHIP_X_API_KEY)
      .body(bodyLink)
      .post();

    return xxxxPartnerShip.getPartnerShip(partnerAccountId).getConfirmId();
  }

  public static String xxxxCreateConfirmID(String partnerId, String partnerAccountId) {
    LinkedHashMap<String, Object> bodyLink = new LinkedHashMap<>();
    List<String> listLinkType = new ArrayList<>(Collections.emptyList());
    listLinkType.add(ACCOUNT);

    bodyLink.put("partnerId", partnerId);
    bodyLink.put("partnerAccountId", partnerAccountId);
    bodyLink.put("linkType", listLinkType);

    Actor actor = Actor.named("logintoken");
    LoginApi.withCredentials("105C066114", "abc123").performAs(actor);
    String token = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();

    given()
      .baseUri(xxxx_ACCOUNT_LINK)
      .header(AUTHORIZATION, BEARER + token)
      .body(bodyLink)
      .post();

    return xxxxPartnerShipConfirm.getConfirmIdByPartnerAndType(xxxxPartnerShip.getPartnerShip(partnerAccountId).getId(),ACCOUNT).getValue();
  }

  public static String getStatusBankIA(String value) {
    String status;
    switch (value) {
      case "0":
        status = "CLOSE";
        break;
      case "1":
        status = "ACTIVE";
        break;
      case "2":
        status = "WAIT_APPROVE";
        break;
      case "3":
        status = "WAIT_KYC";
        break;
      case "4":
        status = "WAIT_CLOSE";
        break;
      case "5":
        status = "WAIT_CHANGE";
        break;
      default:
        status = null;
    }
    return status;
  }
  public static HashMap<String, Object> prepareDataAddUserToWblByFund(String fullName,
                                                                      String address, String idNumber, String fundCode, String note,
                                                                      String actor, String startDatetime, String endDatetime) {

    List<HashMap<String, Object>> wblUsers = new ArrayList<>();
    LinkedHashMap<String, Object> wblUser1 = new LinkedHashMap<>();
    wblUser1.put(FULL_NAME, fullName);
    wblUser1.put(ADDRESS, address);
    wblUser1.put(ID_NUMBER, idNumber);
    wblUser1.put("fundCode", fundCode);
    wblUser1.put("note", note);
    wblUser1.put(START_DATETIME, startDatetime);
    wblUser1.put(END_DATETIME, endDatetime);
    wblUsers.add(wblUser1);

    HashMap<String, Object> hashMapBody = new HashMap<>();
    hashMapBody.put(ACTOR, actor);
    hashMapBody.put("wblUsers", wblUsers);

    return hashMapBody;
  }

  public static HashMap<String, Object> prepareDataUpdateUserToWblByFund(String fullName,
                                                                         String address, String idNumber, String fundCode, String note,
                                                                         String actor, String startDatetime, String endDatetime, String wblUserId) {

    List<HashMap<String, Object>> wblUsers = new ArrayList<>();
    LinkedHashMap<String, Object> wblUser1 = new LinkedHashMap<>();
    wblUser1.put(FULL_NAME, fullName);
    wblUser1.put(ADDRESS, address);
    wblUser1.put(ID_NUMBER, idNumber);
    wblUser1.put("fundCode", fundCode);
    wblUser1.put("note", note);
    wblUser1.put(START_DATETIME, startDatetime);
    wblUser1.put(END_DATETIME, endDatetime);
    wblUser1.put("wblUserId", wblUserId);
    wblUsers.add(wblUser1);

    HashMap<String, Object> hashMapBody = new HashMap<>();
    hashMapBody.put(ACTOR, actor);
    hashMapBody.put("wblUsers", wblUsers);

    return hashMapBody;
  }
}
