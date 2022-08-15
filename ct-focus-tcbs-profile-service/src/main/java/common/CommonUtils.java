package common;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.tcbs.automation.cas.*;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
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

import static com.tcbs.automation.config.iotp.IOTPConfig.IOTP_AUTHEN;
import static com.tcbs.automation.config.iotp.IOTPConfig.IOTP_DATAPOWER_DOMAIN;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static com.tcbs.automation.tools.FormatUtils.syncData;
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
  private static final String TCBSID = "tcbsId";
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
      case "tcbsid":
        userId = TcbsUser.getByTcbsId(value).getId().toString();
        break;
      case CODE105C:
        userId = TcbsUser.getByUserName(value).getId().toString();
        break;
      case PHONE:
        userId = TcbsUser.getByPhoneNumber(value).getId().toString();
        break;
      case EMAIL:
        userId = TcbsUser.getByEmail(value).getId().toString();
        break;
      case ID_NUMBER:
        userId = TcbsIdentification.getByIdNumber(value).getUserId().toString();
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
    personalInfo.put(EMAIL, "anhbui" + idNumber.substring(6, 12) + "@gmail.com");
    personalInfo.put(PHONE_NUMBER, "0" + idNumber.substring(3, 12));
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
      if (!TcbsUser.getListByPhoneNumber(phoneNumber).isEmpty()) {
        String userId = TcbsUser.getByPhoneNumber(phoneNumber).getId().toString();
        TcbsIdentification.deleteByIdNumber(idNumber);
        TcbsNewOnboardingStatus.deleteByUserId(userId);
        TcbsUser.deleteByPhone(phoneNumber);
        TcbsUserOpenAccountQueue.deleteByPhone(phoneNumber);
      }
    }

    if (!idNumber.equalsIgnoreCase("0253652563")) {
      if (!TcbsIdentification.getListByIdNumber(idNumber).isEmpty()) {
        String userId = TcbsIdentification.getByIdNumber(idNumber).getUserId().toString();
        String phone = TcbsUser.getById(new BigDecimal(userId)).getPhone();
        TcbsIdentification.deleteByIdNumber(idNumber);
        TcbsNewOnboardingStatus.deleteByUserId(userId);
        TcbsUser.deleteByPhone(phone);
        TcbsUserOpenAccountQueue.deleteByPhone(phone);
      }
    }

    if (!email.equalsIgnoreCase("nguyenvanaaa@gmail.com")) {
      if (!TcbsUser.getListByEmail(email).isEmpty()) {
        String userId = TcbsUser.getByEmail(email).getId().toString();
        TcbsIdentification.deleteByIdNumber(idNumber);
        TcbsNewOnboardingStatus.deleteByUserId(userId);
        TcbsUser.deleteByPhone(phoneNumber);
        TcbsUserOpenAccountQueue.deleteByPhone(phoneNumber);
      }

    }
  }

  public static void verifyFMBBasicInfo(Response response, String responsePath, String key, String value) {

    Map<String, Object> getResponse = response.jsonPath().getMap(responsePath);
    String userId = checkUserIdInput(key, value);
    TcbsUser tcbsUser = TcbsUser.getById(new BigDecimal(userId));
    assertEquals(tcbsUser.getTcbsid(), getResponse.get(TCBSID));
    assertEquals(tcbsUser.getUsername(), getResponse.get(CODE105C));
    String status = getUserStatus(tcbsUser.getAccountStatus().toString());
    assertEquals(status, getResponse.get(STATUS));
    String type = getUserType(tcbsUser.getCustype().toString());
    assertEquals(type, getResponse.get("type"));
    String accountType = getAccountType(tcbsUser.getUsername());
    assertEquals(accountType, getResponse.get(ACCOUNT_TYPE));
  }

  public static void verifyOnboardingBasicInfo(Response response, String responsePath, String key, String value) {

    Map<String, Object> getResponse = response.jsonPath().getMap(responsePath);
    String userId = checkUserIdInput(key, value);
    TcbsUser tcbsUser = TcbsUser.getById(new BigDecimal(userId));
    assertEquals(tcbsUser.getTcbsid(), getResponse.get(TCBSID));
    assertEquals(tcbsUser.getUsername(), getResponse.get(CODE105C));
    String status = getUserStatus(tcbsUser.getAccountStatus().toString());
    assertEquals(status, getResponse.get(STATUS));
    String type = getUserType(tcbsUser.getCustype().toString());
    assertEquals(type, getResponse.get("type"));
  }

  public static void verifyFMBOnboadingStatusValue(List<TcbsNewOnboardingStatus> tcbsNewOnboardingStatusList, int i, Map<String, Object> getResponse) {

    if (getResponse != null) {
      assertEquals(tcbsNewOnboardingStatusList.get(i).getStatusValue(), getResponse.get("value"));
      assertEquals(tcbsNewOnboardingStatusList.get(i).getRejectReason(), getResponse.get("rejectReason"));
      assertEquals(tcbsNewOnboardingStatusList.get(i).getRejectContent(), getResponse.get("rejectContent"));
      assertEquals(tcbsNewOnboardingStatusList.get(i).getRejectPerson(), getResponse.get("rejectPerson"));
    }
  }

  public static void verifyFMBRegIAStatus(Response response, String userId) {
    Map<String, Object> getResponse = response.jsonPath().getMap("accountStatus.iaStatus");
    TcbsBankIaaccount tcbsBankIaaccount = TcbsBankIaaccount.getBank(userId);
    assertEquals(tcbsBankIaaccount.getAutoTransfer(), getResponse.get("autoTransfer"));
    assertEquals(tcbsBankIaaccount.getIsIaPaid(), getResponse.get("isIAPaid"));
  }

  public static void verifyFMBOnboardingStatus(Response response, String responsePath, String key, String value) {
    Map<String, Object> getResponse;
    String userId = checkUserIdInput(key, value);
    List<TcbsNewOnboardingStatus> tcbsNewOnboardingStatusList = TcbsNewOnboardingStatus.getByUserId(new BigDecimal(userId));
    if (!tcbsNewOnboardingStatusList.isEmpty()) {
      for (int i = 0; i < tcbsNewOnboardingStatusList.size(); i++) {
        String statusKey = tcbsNewOnboardingStatusList.get(i).getStatusKey();
        switch (statusKey) {
          case "ACTIVATED_STATUS":
            getResponse = response.jsonPath().getMap(responsePath + "activatedStatus");
            verifyFMBOnboadingStatusValue(tcbsNewOnboardingStatusList, i, getResponse);
            break;
          case "GEN_105C":
            getResponse = response.jsonPath().getMap(responsePath + "gen105c");
            verifyFMBOnboadingStatusValue(tcbsNewOnboardingStatusList, i, getResponse);
            break;
          case "PREFER_ACTIVATION_CHANNEL":
            getResponse = response.jsonPath().getMap(responsePath + "preferActivationChannelStatus");
            verifyFMBOnboadingStatusValue(tcbsNewOnboardingStatusList, i, getResponse);
            break;
          case "ID_STATUS":
            getResponse = response.jsonPath().getMap(responsePath + "idStatus");
            verifyFMBOnboadingStatusValue(tcbsNewOnboardingStatusList, i, getResponse);
            break;
          case "BANK_INFO_STATUS":
            getResponse = response.jsonPath().getMap(responsePath + "bankInfoStatus");
            verifyFMBOnboadingStatusValue(tcbsNewOnboardingStatusList, i, getResponse);
            break;
          case "COUNTER_KYC_STATUS":
            getResponse = response.jsonPath().getMap(responsePath + "counterKycStatus");
            verifyFMBOnboadingStatusValue(tcbsNewOnboardingStatusList, i, getResponse);
            break;
          case "DOCUMENT_STATUS":
            getResponse = response.jsonPath().getMap(responsePath + "documentStatus");
            verifyFMBOnboadingStatusValue(tcbsNewOnboardingStatusList, i, getResponse);
            break;
          case "EKYC_STATUS":
            getResponse = response.jsonPath().getMap(responsePath + "ekycStatus");
            verifyFMBOnboadingStatusValue(tcbsNewOnboardingStatusList, i, getResponse);
            break;
          case "ECONTRACT_STATUS":
            getResponse = response.jsonPath().getMap(responsePath + "econtractStatus");
            verifyFMBOnboadingStatusValue(tcbsNewOnboardingStatusList, i, getResponse);
            break;
          case "PRIVATE_ACCOUNT_STATUS":
            getResponse = response.jsonPath().getMap(responsePath + "privateAccountStatus");
            verifyFMBOnboadingStatusValue(tcbsNewOnboardingStatusList, i, getResponse);
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
      TcbsBankSubaccount tcbsBankSubaccount = TcbsBankSubaccount.getBank(userId);
      assertEquals(tcbsBankSubaccount.getId().toString(), getResponse.get("bankAccountId").toString());
      assertEquals(tcbsBankSubaccount.getAccountNo(), getResponse.get(ACCOUNT_NO));
      assertEquals(tcbsBankSubaccount.getAccountName(), getResponse.get(ACCOUNT_NAME));
      assertEquals(tcbsBankSubaccount.getBankCode(), getResponse.get(BANK_CODE));
      assertEquals(tcbsBankSubaccount.getBankName(), getResponse.get(BANK_NAME));
      assertEquals(tcbsBankSubaccount.getBankprovince(), getResponse.get(BANK_PROVINCE));
      assertEquals(tcbsBankSubaccount.getBankBranch(), getResponse.get(BRANCH_CODE));
      assertEquals(tcbsBankSubaccount.getAccountType(), getResponse.get(ACCOUNT_TYPE));
    }

  }

  public static void verifyFMBankIaInfo(Response response, String responsePath, String key, String value) {
    Map<String, Object> getResponse = response.jsonPath().getMap(responsePath);
    if (getResponse != null) {
      String userId = checkUserIdInput(key, value);
      TcbsBankIaaccount tcbsBankIaaccount = TcbsBankIaaccount.getBank(userId);
      assertEquals(tcbsBankIaaccount.getAccountNo(), getResponse.get(ACCOUNT_NO));
      assertEquals(tcbsBankIaaccount.getAccountName(), getResponse.get(ACCOUNT_NAME));
      assertEquals(tcbsBankIaaccount.getBankCode(), getResponse.get(BANK_CODE));
      assertEquals(tcbsBankIaaccount.getBankName(), getResponse.get(BANK_NAME));
      assertEquals(tcbsBankIaaccount.getBankprovince(), getResponse.get(BANK_PROVINCE));
      assertEquals(tcbsBankIaaccount.getBankBranch(), getResponse.get(BRANCH_CODE));
      assertEquals(tcbsBankIaaccount.getBankSource(), getResponse.get("bankSource"));
    }

  }

  public static void verifyBankSource(Response response, String responsePath, String key, String value) {
    Map<String, Object> getResponse = response.jsonPath().getMap(responsePath);
    if (getResponse != null) {
      String userId = checkUserIdInput(key, value);
      TcbsBankIaaccount tcbsBankIaaccount = TcbsBankIaaccount.getBank(userId);
      assertThat(getResponse.get("bankSource"), is(tcbsBankIaaccount.getBankSource()));
    }
  }

  public static void verifyPRODPersonalInfo(Response response, String responsePath, String key, String value) {

    Map<String, Object> getResponse = response.jsonPath().getMap(responsePath);
    if (getResponse != null) {
      String userId = checkUserIdInput(key, value);
      TcbsUser tcbsUser = TcbsUser.getById(new BigDecimal(userId));
      assertEquals(tcbsUser.getLastname() + " " + tcbsUser.getFirstname(), getResponse.get(FULL_NAME));
      assertEquals(tcbsUser.getFirstname(), getResponse.get("firstName"));
      assertEquals(tcbsUser.getLastname(), getResponse.get("lastName"));
      assertEquals(tcbsUser.getEmail(), getResponse.get(EMAIL));
      assertEquals(tcbsUser.getPhone(), getResponse.get(PHONE_NUMBER));
      String gender = getGender(String.valueOf(tcbsUser.getGender()));
      assertEquals(gender, getResponse.get("gender"));
      assertEquals(covertDateToString(tcbsUser.getBirthday()), convertTimestampToString((getResponse.get(BIRTHDAY).toString())));
      assertEquals(tcbsUser.getId().toString(), getResponse.get(USERID).toString());
      assertEquals(covertDateToString(tcbsUser.getCreatedDate()), convertTimestampToString(getResponse.get("createdDate").toString()));
      assertEquals(covertDateToString(tcbsUser.getUpdatedDate()), convertTimestampToString(getResponse.get("updatedDate").toString()));
      if (getResponse.get("honorific") != null) {
        assertEquals(tcbsUser.getHonorific().toString(), getResponse.get("honorific").toString());
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
      TcbsUser tcbsUser = TcbsUser.getById(new BigDecimal(userId));
      TcbsIdentification tcbsIdentification = TcbsIdentification.getByUserId(userId);
      assertEquals(tcbsUser.getFirstname(), getResponse.get("firstName"));
      assertEquals(tcbsUser.getLastname(), getResponse.get("lastName"));
      assertEquals(tcbsUser.getLastname() + " " + tcbsUser.getFirstname(), getResponse.get(FULL_NAME));
      assertEquals(tcbsUser.getEmail(), getResponse.get(EMAIL));
      assertEquals(tcbsUser.getPhone(), getResponse.get(PHONE_NUMBER));
      assertEquals(tcbsIdentification.getIdNumber(), response.jsonPath().get(responsePath + ".identityCard.idNumber"));
    }
  }

  public static void verifyBankSubAccountsInfo(Response response, String responsepath, String key, String value) {

    String userId = checkUserIdInput(key, value);
    List<HashMap<String, Object>> getResponses = response.jsonPath().getList(responsepath);
    if (!getResponses.isEmpty()) {
      for (HashMap<String, Object> getRes : getResponses) {
        TcbsBankSubaccount tcbsBankSubaccount = TcbsBankSubaccount.getBankByAccountNoAndUserId(getRes.get(ACCOUNT_NO).toString(), userId).get(0);
        assertEquals(tcbsBankSubaccount.getAccountName(), getRes.get(ACCOUNT_NAME));
        assertEquals(tcbsBankSubaccount.getBankCode(), getRes.get(BANK_CODE));
        if (tcbsBankSubaccount.getUserType().equalsIgnoreCase("NORMAL")) {
          assertEquals(tcbsBankSubaccount.getAccountType(), getRes.get(ACCOUNT_TYPE));
        } else {
          assertEquals(tcbsBankSubaccount.getUserType() + "_" + tcbsBankSubaccount.getAccountType(), getRes.get(ACCOUNT_TYPE));
        }

        assertEquals(tcbsBankSubaccount.getStatus().toString(), getRes.get(STATUS).toString());
      }
    }
  }

  public static void verifyPRODIdentityCard(Response response, String responsePath, String key, String value) {

    Map<String, Object> getResponse = response.jsonPath().getMap(responsePath);
    if (getResponse != null) {
      String userId = checkUserIdInput(key, value);
      TcbsIdentification tcbsIdentification = TcbsIdentification.getByUserId(userId);
      assertEquals(tcbsIdentification.getIdNumber(), getResponse.get(ID_NUMBER));
      assertEquals(tcbsIdentification.getIdPlace(), getResponse.get("idPlace"));
      assertEquals(covertDateToString(tcbsIdentification.getIdDate()), convertTimestampToString((getResponse.get("idDate").toString())));
    } else {
      Map<String, Object> getProfile = response.jsonPath().getMap("profileDto.personalInfo");
      assertTrue(getProfile.containsKey(IDENTITY_CARD));
    }
  }

  public static void verifyPRODScanIdFiles(Response response, String responsePath, String key, String value) {

    List<HashMap<String, Object>> getResponseList = response.jsonPath().getList(responsePath);
    if (!getResponseList.isEmpty()) {
      String userId = checkUserIdInput(key, value);
      List<TcbsUserUpload> tcbsUserUpload = TcbsUserUpload.getByUserId(userId);
      for (int i = 0; i < tcbsUserUpload.size(); i++) {
        assertEquals(tcbsUserUpload.get(i).getOriginalName(), getResponseList.get(i).get("fileName").toString());
        assertEquals(tcbsUserUpload.get(i).getFileAlias(), getResponseList.get(i).get("fileAlias").toString());
        assertEquals(tcbsUserUpload.get(i).getFileType(), getResponseList.get(i).get("fileType").toString());
        String expected = covertDateToString((tcbsUserUpload.get(i).getSince()));
        String actual = convertTimestampToString(getResponseList.get(i).get("uploadDate").toString());
        assertEquals(expected, actual);
        assertEquals(tcbsUserUpload.get(i).getId().toString(), getResponseList.get(i).get("fileUploadId").toString());
        assertEquals(tcbsUserUpload.get(i).getObjectId(), getResponseList.get(i).get("objectId").toString());
      }

    }
  }

  public static void verifyPRODBankInfo(Response response, String responsePath, String key, String value) {
    List<HashMap<String, Object>> getResponseList = response.jsonPath().getList(responsePath);
    if (!getResponseList.isEmpty()) {
      String userId = checkUserIdInput(key, value);
      List<TcbsBankAccount> tcbsBankAccountList = TcbsBankAccount.getListBanks(userId);
      for (int i = 0; i < getResponseList.size(); i++) {
        assertEquals(tcbsBankAccountList.get(i).getId().toString(), getResponseList.get(i).get("bankAccountId").toString());
        assertEquals(tcbsBankAccountList.get(i).getBankAccountNo(), getResponseList.get(i).get(ACCOUNT_NO));
        assertEquals(tcbsBankAccountList.get(i).getBankAccountName(), getResponseList.get(i).get(ACCOUNT_NAME));
        assertEquals(tcbsBankAccountList.get(i).getBankCode(), getResponseList.get(i).get(BANK_CODE));
        assertEquals(tcbsBankAccountList.get(i).getBankName(), getResponseList.get(i).get(BANK_NAME));
        assertEquals(tcbsBankAccountList.get(i).getBankprovince(), getResponseList.get(i).get(BANK_PROVINCE));
        assertEquals(tcbsBankAccountList.get(i).getBankBranch(), getResponseList.get(i).get(BRANCH_CODE));
      }
    }

  }

  public static void verifyPRODContractTrust(Response response, String responsePath, String key, String value) {

    Map<String, Object> getResponse = response.jsonPath().getMap(responsePath);
    if (getResponse != null) {
      String userId = checkUserIdInput(key, value);
      String tcbsId = TcbsUser.getById(new BigDecimal(userId)).getTcbsid();
      TcbsContractTrust tcbsContractTrust = TcbsContractTrust.getByTcbsId(tcbsId);
      assertEquals(tcbsContractTrust.getTcbsid(), getResponse.get(TCBSID));
      assertEquals(tcbsContractTrust.getFullName(), getResponse.get(FULL_NAME));
      assertEquals(tcbsContractTrust.getBirthday(), getResponse.get(BIRTHDAY));
      assertEquals(tcbsContractTrust.getNationnality(), getResponse.get("nationnality"));
      assertEquals(tcbsContractTrust.getPassportNo(), getResponse.get("passportNo"));
      assertEquals(tcbsContractTrust.getIssueDate(), getResponse.get("issueDate"));
      assertEquals(tcbsContractTrust.getIssuePlace(), getResponse.get("issuePlace"));
      assertEquals(tcbsContractTrust.getType(), getResponse.get("type"));
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
        assertEquals(obTaskList.get(i).getActor(), getResponseList.get(i).get("actor"));
        assertEquals(covertDateToString(obTaskList.get(i).getStartDatetime()), convertTimestampToString(getResponseList.get(i).get("startDatetime").toString()));
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
    policyItem1.put("startDatetime", startDatetime);
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
    stakeHolder1.put("address", "312 Bà Triệu, HBT");
    stakeHolder1.put("capitalRatio", "51%");
    stakeHolder1.put("fromDate", "2021-08-30");
    stakeHolder1.put(BE_ACTION, STR_CREATE);
    stakeHolders.add(stakeHolder1);

    HashMap<String, Object> hashMapBody = new HashMap<>();
    hashMapBody.put(FULL_NAME, fullName);
    hashMapBody.put("address", address);
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

  public static TcbsUser getTcbsUserByUserName(String username) {
    return TcbsUser.getByUserName(username);
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

  public static String authenOTP(String tcbsId, String token) {
    HashMap<String, Object> authenInfo = new HashMap<>();
    authenInfo.put("otp", "111111");
    authenInfo.put("otpTypeName", "TOTP");
    authenInfo.put(TCBSID, tcbsId);
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

  public static String createTaskNewOBAndAssignToMaker(String tcbsId) {
    String taskId;
    String userId = TcbsUser.getByTcbsId(tcbsId).getId().toString();
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
    String userId = TcbsUser.getByUserName(USERNAME).getId().toString();
    List<HashMap<String, Object>> taskSession = response.jsonPath().getList("");
    TcbsAuthenInfo tcbsAuthenInfo = TcbsAuthenInfo.getSessionByUserId(userId).get(0);
    for (HashMap<String, Object> session : taskSession) {
      if (session.get("sessionId").toString().equalsIgnoreCase(tcbsAuthenInfo.getSessionId())) {
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
        DateFormat df = new SimpleDateFormat(pattern);
        assertThat(session.get("loginTime"), is(df.format(tcbsAuthenInfo.getLoginTime())));
        assertTrue(session.get("ip").toString().contains(tcbsAuthenInfo.getIp()));
        assertThat(session.get("device"), is(tcbsAuthenInfo.getDevice()));
        assertThat(session.get(USERID), is(tcbsAuthenInfo.getUserId()));
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

  public static String getTokenAuthenSOtp(String tcbsId) {
    String token = CommonUtils.getToken(USERNAME);
    String duration = "0";
    String otp = "111111";
    String otpTypeName = "SMS";
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("duration", duration);
    body.put("otp", otp);
    body.put("otpTypeName", otpTypeName);
    body.put(TCBSID, tcbsId);

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

  public static String createTcbsIdLearning(String tcbsId) {
    R3RdElearning r3rdElearning = new R3RdElearning();
    r3rdElearning.setCourseCode("EL0123123");
    r3rdElearning.setTcbsId("0001100103");
    r3rdElearning.setFullName("Lê Phương Mai");
    r3rdElearning.setCourseStatus(new BigDecimal("1"));
    r3rdElearning.setCreatedDate(new Timestamp(System.currentTimeMillis()));
    r3rdElearning.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
    r3rdElearning.setUsername("105C100103");
    r3rdElearning.setRole("RM");
    r3rdElearning.setBankCode("ACB");
    r3rdElearning.insert();
    return tcbsId;
  }

  public static String genPhoneNumberByDateTime() {
    List<String> givenList = Arrays.asList("3", "5", "7", "8", "9");
    String randomElement = givenList.get(rand.nextInt(givenList.size()));
    String prepareValue = String.valueOf(new Date().getTime());
    return randomElement + prepareValue.substring(5);
  }

}
