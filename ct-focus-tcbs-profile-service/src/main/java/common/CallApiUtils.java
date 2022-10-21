package common;

import com.google.gson.Gson;
import com.tcbs.automation.cas.ObTask;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static common.CommonUtils.getFMBRegisterBetaBody;
import static common.CommonUtils.getUpgradeAdvancedBody;
import static common.ProfileTools.TOKEN;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class CallApiUtils {
  private static final String APPLICATION_JSON = "application/json";
  private static final String BEARER = "Bearer ";
  private static final String AUTHORIZATION = "Authorization";
  private static final String X_API_KEY = "x-api-key";
  private static final String TASK_ID = "#taskId#";
  private static final String PHONE_CODE = "phoneCode";
  private static final String PHONE_NUMBER = "phoneNumber";


  public static void clearCache(String baseUri, String headerKey, String headerValue) {
    given().baseUri(baseUri).headers(headerKey, headerValue).contentType(APPLICATION_JSON).when().delete();
  }

  public static Response callPRODFindCustOpsApi(String testCaseName, String containsText, String keyParams, String valueParams) {
    Response response;
    RequestSpecification request = given()
      .baseUri(PROD_FIND_CUSTOMER_OPS)
      .header(AUTHORIZATION, BEARER + TOKEN)
      .contentType(APPLICATION_JSON);

    if (testCaseName.contains(containsText)) {
      response = request
        .when()
        .get();
    } else {
      response = request
        .param(keyParams, valueParams)
        .when()
        .get();
    }
    return response;
  }

  public static String callNewOBMakerSendApproveToCheckerApiAndGetTaskId(String taskId) {
    String bodySendApproveTask = fileTxtToString("src/test/resources/requestBody/MakerSendApproveTaskBody.json")
      .replace(TASK_ID, taskId);
    Response response = given()
      .baseUri(MAKER_SEND_APPROVE)
      .header(AUTHORIZATION, BEARER + ASSIGN_TASK_TO_MAKER_KEY)
      .contentType(APPLICATION_JSON)
      .body(bodySendApproveTask)
      .when()
      .post();

    assertThat(response.statusCode(), is(200));
    ObTask obTask = ObTask.getByTaskRefId(new BigDecimal(taskId));
    return obTask.getId().toString();
  }

  public static void callNewOBAssignTaskToCheckerApi(String taskId) {

    Response response = given()
      .baseUri(ASSIGN_TASK_TO_MAKER_OR_CHECKER.replace("#taskID#", taskId))
      .header(AUTHORIZATION, BEARER + ASSIGN_TASK_TO_CHECKER_KEY)
      .when()
      .post();
    assertEquals(response.statusCode(), 200);
  }

  public static void callNewOBMakerRejectApi(String taskId) {
    String body = fileTxtToString("src/test/resources/requestBody/RejectTaskBody.json")
      .replace(TASK_ID, taskId);
    Response response = given()
      .baseUri(MAKER_REJECT)
      .header(AUTHORIZATION, BEARER + ASSIGN_TASK_TO_MAKER_KEY)
      .contentType(APPLICATION_JSON)
      .body(body)
      .when()
      .post();
    assertEquals(response.statusCode(), 200);
  }

  public static String callNewOBUploadDocAndGetEcmId(String taskId) {
    String fileContents = fileTxtToString("src/test/resources/requestBody/DocFileContent");
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("taskId", taskId);
    body.put("documentGroup", "REGISTER_INFORMATION");
    body.put("fileName", "Mau-20-DK-NPT+dang+ky+GTGC.doc");
    body.put("fileContents", fileContents);

    Response response = given()
      .baseUri(UPLOAD_DOC)
      .header(AUTHORIZATION, BEARER + ASSIGN_TASK_TO_MAKER_KEY)
      .contentType(APPLICATION_JSON)
      .body(body)
      .when()
      .post();
    return response.jsonPath().getString("objectId");
  }

  public static void callNewOBCheckerApproveApi(String taskId) {

    String body = fileTxtToString("src/test/resources/requestBody/CheckerApproveTaskBody.json")
      .replace(TASK_ID, taskId);
    Response response = given()
      .baseUri(CHECKER_APPROVE)
      .header(AUTHORIZATION, BEARER + ASSIGN_TASK_TO_CHECKER_KEY)
      .contentType(APPLICATION_JSON)
      .body(body)
      .when()
      .put();
    assertEquals(response.statusCode(), 200);
  }

  public static Response callNewOBUploadVsdApi(String fileKey, String filePath, String fileName, RequestSpecification request) {
    switch (fileKey) {
      case "files":
        request.multiPart(fileKey, new File(filePath + fileName));
        break;
      case "filesNull":
        request.multiPart(fileKey, "");
        break;
      default:
        break;
    }
    return request.when().post();
  }

  public static RequestSpecification getNewOBUploadVsdApiRequest(String baseUrl) {
    return given()
      .baseUri(baseUrl)
      .headers(AUTHORIZATION, BEARER + TOKEN)
      .contentType("multipart/form-data");
  }

  public static Response callPostApiHasBody(String baseUri, String headerKey, String headerValue, Object body) {
    Gson gson = new Gson();
    return given()
      .baseUri(baseUri)
      .headers(headerKey, headerValue)
      .contentType(APPLICATION_JSON)
      .body(gson.toJson(body))
      .when()
      .post();
  }

  public static Response callGetApiHasParams(String baseUri, String headerKey, String headerValue, HashMap<String, Object> params) {
    return given()
      .baseUri(baseUri)
      .headers(headerKey, headerValue)
      .contentType(APPLICATION_JSON)
      .params(params)
      .when()
      .get();
  }

  public static void callOcrWebhookApi(String requestId, String fullName, String birthday, String idNumber, String idDate, String idPlace, String expireDate) {
    String body = fileTxtToString("src/test/resources/requestBody/CreateOcrTask.json")
      .replace("#requestId#", requestId)
      .replace("#fullName#", fullName).replace("#birthday#", birthday)
      .replace("#idNumber#", idNumber).replace("#idDate#", idDate)
      .replace("#idPlace#", idPlace).replace("#expireDate#", expireDate);
    Response responseCreate = given()
      .baseUri(OCR_WEBHOOK)
      .header("Is-Signature", OCR_SIGNATURE_TOKEN)
      .contentType(APPLICATION_JSON)
      .body(body)
      .when()
      .get();
    assertThat(responseCreate.getStatusCode(), is(200));
  }

  public static void callRmOpenAccountApi(String fullName, String birthday, String idNumber, String email, String phone, String contactAddress, String idDate, String idPlace) {
    String body = fileTxtToString("src/test/resources/requestBody/RmOpenAccount.txt")
      .replace("#fullName#", fullName).replace("#birthday#", birthday).replace("#idNumber#", idNumber)
      .replace("#email#", email).replace("#phone#", phone)
      .replace("#contactAddress#", contactAddress).replace("#idDate#", idDate).replace("#idPlace#", idPlace);
    Response response = given()
      .baseUri(TCBSPROFILE_RM_OPEN_ACCOUNT)
      .cookie("JSESSIONID=53DE4D703500CBB4AD5C0B08E2E32A66")
      .contentType("application/x-www-form-urlencoded; charset=UTF-8")
      .body(body)
      .post();
    assertThat(response.jsonPath().get("status"), is(0));
  }

  public static Response getFMBRegisterBetaResponse(LinkedHashMap<String, Object> body) {
    Gson gson = new Gson();
    return given()
      .baseUri(FMB_REGISTER_BASIC)
      .header(X_API_KEY, FMB_X_API_KEY)
      .contentType(APPLICATION_JSON)
      .body(gson.toJson(body))
      .when()
      .post();
  }

  public static void callAddUserToWblListApi(String testCaseName, String prepareValue, String idNumber,
                                             String startDatetime, String endDatetime, String type, String policyCode,
                                             String refIdNumber, String fullName, String address) {

    HashMap<String, Object> body = CommonUtils.prepareDataAddUserToWbl(testCaseName, prepareValue, idNumber, startDatetime, endDatetime, type,
      policyCode, refIdNumber, fullName, address);

    Response response = given()
      .baseUri(ADD_USER_TO_WBL_LIST)
      .contentType(APPLICATION_JSON)
      .header(AUTHORIZATION, BEARER + TCBSPROFILE_SPECIALWBLKEY)
      .body(body)
      .post();

    assertThat(response.getStatusCode(), is(200));
  }

  public static void callDeleteUserFromWblListApi(String wblUserId) {

    Response response = given()
      .baseUri(DELETE_USER_FROM_WBL_LIST.replace("#wblUserId#", wblUserId))
      .contentType(APPLICATION_JSON)
      .header(AUTHORIZATION, BEARER + TCBSPROFILE_SPECIALWBLKEY)
      .delete();

    assertThat("verify status code", response.getStatusCode(), is(200));
  }

  public static void callPrepareUserToWblListApi() {
    String body = fileTxtToString("src/test/resources/requestBody/PrepareUpdateUserToWblList.json");

    Response response = given()
      .baseUri(ADD_USER_TO_WBL_LIST)
      .contentType(APPLICATION_JSON)
      .header(AUTHORIZATION, BEARER + TCBSPROFILE_SPECIALWBLKEY)
      .body(body)
      .post();

    assertThat(response.getStatusCode(), is(200));
  }

  public static Response callGetApiHasNoParams(String baseUri, String headerKey, String headerValue) {
    return given()
      .baseUri(baseUri)
      .headers(headerKey, headerValue)
      .contentType(APPLICATION_JSON)
      .when()
      .get();
  }

  public static Response callDeleteApiHasNoParams(String baseUri, String headerKey, String headerValue) {
    return given()
      .baseUri(baseUri)
      .headers(headerKey, headerValue)
      .contentType(APPLICATION_JSON)
      .when()
      .delete();
  }

  public static Response callProfileRegisterApi(String prepareValue, String suffixPhone, String phoneCode, String code105C, String accountNumber) {
    String body = fileTxtToString("src/test/resources/requestBody/RegisterInfoBody.json")
      .replace("#value#", prepareValue).replace("#phone#", suffixPhone)
      .replace("#phoneCode#", phoneCode)
      .replace("#code105C#", code105C).replace("#accountNumber#", accountNumber);

    Response response = given()
      .baseUri(TCBSPROFILE_PUB_API + TCBSPROFILE_REGISTER)
      .contentType(APPLICATION_JSON)
      .body(body)
      .post();

    assertThat(response.statusCode(), is(200));
    return response;
  }

  public static Response getFMBUpgradeAdvanceResponse(LinkedHashMap<String, Object> body, String tcbsId) {
    Gson gson = new Gson();
    return given()
      .baseUri(FMB_UPGRADE_ADVANCED.replace("{tcbsId}", tcbsId))
      .header(X_API_KEY, FMB_X_API_KEY)
      .contentType(APPLICATION_JSON)
      .body(gson.toJson(body))
      .when()
      .post();
  }

  public static String prepareRegIA(String idNumberVal, String getPhoneNumber) {
    CallApiUtils.clearCache(DELETE_CACHE, X_API_KEY, API_KEY);

    LinkedHashMap<String, Object> bodyBeta = getFMBRegisterBetaBody(idNumberVal);
    Response response1 = getFMBRegisterBetaResponse(bodyBeta);
    String tcbsId = response1.jsonPath().getString("basicInfo.tcbsId");
    CallApiUtils.clearCache(CLEAR_CACHE_REDIS.replace("{phoneNumber}", getPhoneNumber), X_API_KEY, API_KEY);

    LinkedHashMap<String, Object> bodyAdvance = getUpgradeAdvancedBody();
    getFMBUpgradeAdvanceResponse(bodyAdvance, tcbsId);
    return tcbsId;
  }

  public static Response callListProvincesApi(String testCaseName, String countryCode, RequestSpecification request) {

    Response response;
    if (testCaseName.contains("missing param countryCode")) {
      response = request
        .when()
        .get();
    } else {
      response = request
        .param("countryCode", countryCode)
        .when()
        .get();
    }
    return response;
  }

  public static Response callCreateNewProfileDocumentApi(String tcbsId, String bookType) {
    String body = fileTxtToString("src/test/resources/requestBody/CreateNewProfileDocument.json")
      .replace("#bookType#", bookType);

    Response response = given()
      .baseUri(CREATE_NEW_PROFILE_DOCUMENT.replace("#tcbsId#", tcbsId))
      .contentType(APPLICATION_JSON)
      .header(X_API_KEY, API_KEY)
      .body(body)
      .post();

    assertThat(response.statusCode(), is(200));
    return response;
  }

  public static Response registerValidation(String fullName, String email, String phoneNumber, String phoneCode) {
    LinkedHashMap<String, Object> body = new LinkedHashMap<>();
    body.put("fullName", fullName);
    body.put("email", email);
    body.put(PHONE_NUMBER, phoneNumber);
    body.put(PHONE_CODE, phoneCode);

    Gson gson = new Gson();
    return given()
      .baseUri(REGISTER_VERIFICATION)
      .header(X_API_KEY, FMB_X_API_KEY)
      .contentType(APPLICATION_JSON)
      .body(gson.toJson(body))
      .when()
      .post();
  }

  public static Response callGenAuthenKeyApi() {
    String body = fileTxtToString("src/test/resources/requestBody/GenAuthenKey.json");
    Response response = given()
      .baseUri(GEN_AUTHEN_KEY)
      .contentType(APPLICATION_JSON)
      .header(X_API_KEY, API_KEY)
      .body(body)
      .post();
    assertThat(response.statusCode(), is(200));
    return response;
  }

  public static Response callLoginApi(String loginKey) {
    HashMap<String, Object> body = new HashMap<>();
    body.put("login_key", loginKey);
    Response response = given()
      .baseUri(LOGIN_TO_TCI3)
      .contentType(APPLICATION_JSON)
      .body(body)
      .post();
    assertThat(response.statusCode(), is(200));
    return response;
  }

  public static String callRegisterConfirmPhoneApi(String referenceId) {
    String body = fileTxtToString("src/test/resources/requestBody/RegisterConfirmPhone.json")
      .replace("#phoneCode#", referenceId.substring(0, 3))
      .replace("#phoneNumber#", referenceId.substring(3, 12))
      .replace("#referenceId#", referenceId);
    Response response = given()
      .baseUri(REGISTER_CONFIRM_PHONE)
      .contentType(APPLICATION_JSON)
      .body(body)
      .post();
    assertThat(response.statusCode(), is(200));
    return response.jsonPath().get("authenKey").toString();
  }

  public static Response callGenRefIdAndConfirmPhoneApi(String phoneCode, String phoneNumber) {
    HashMap<String, Object> body = new HashMap<>();
    body.put(PHONE_NUMBER, phoneNumber);
    body.put(PHONE_CODE, phoneCode);
    body.put("otpId", "4971c9bb-09c6-4a5d-94fc-b93c64209c3b");
    body.put("otp", "111111");
    Response response = given()
      .baseUri(REGISTER_CONFIRM_PHONE)
      .contentType(APPLICATION_JSON)
      .body(body)
      .post();
    assertThat(response.statusCode(), is(200));
    return response;
  }

  public static Response callForgotPasswordPhoneApi(String code105C, String birthday) {
    HashMap<String, Object> body = new HashMap<>();
    body.put("code105C", code105C);
    body.put("birthday", birthday);
    Response response = given()
      .baseUri(FORGOT_PASSWORD_PHONE)
      .contentType(APPLICATION_JSON)
      .body(body)
      .post();
    assertThat(response.statusCode(), is(200));
    return response;
  }

  public static Response callForgotPasswordNotifyApi(String transactionId, String token) {
    HashMap<String, Object> body = new HashMap<>();
    body.put("transactionId", transactionId);
    body.put("token", token);
    Response response = given()
      .baseUri(FORGOT_PASSWORD_NOTIFY)
      .contentType(APPLICATION_JSON)
      .body(body)
      .post();
    assertThat(response.statusCode(), is(200));
    return response;
  }

  public static void callDeleteUserFromWblByFundApi(String wblUserId) {
    HashMap<String, Object> body = new HashMap<>();
    List<HashMap<String, Object>> wblUsers = new ArrayList<>();
    LinkedHashMap<String, Object> wblUserId1 = new LinkedHashMap<>();
    wblUserId1.put("wblUserId", wblUserId);
    wblUsers.add(wblUserId1);
    body.put("actor", "test");
    body.put("wblUsers", wblUsers);
    Response response = given()
      .baseUri(DELETE_USER_FROM_WBL_LIST_FUND)
      .contentType(APPLICATION_JSON)
      .header(X_API_KEY, STP_X_API_KEY)
      .body(body)
      .post();

    assertThat("verify status code", response.getStatusCode(), is(200));
  }

  public static void callAddUserToWblListFundApi(String fullName,
                                                 String address, String idNumber, String fundCode, String note,
                                                 String actor, String startDatetime, String endDatetime) {

    HashMap<String, Object> body = CommonUtils.prepareDataAddUserToWblByFund(fullName, address, idNumber, fundCode,
      note, actor, startDatetime, endDatetime);

    Response response = given()
      .baseUri(ADD_USER_TO_WBL_LIST_FUND)
      .contentType(APPLICATION_JSON)
      .header(X_API_KEY, STP_X_API_KEY)
      .body(body)
      .post();

    assertThat(response.getStatusCode(), is(200));
  }

  public static void callPrepareUserToWblListFundApi() {
    String body = fileTxtToString("src/test/resources/requestBody/PrepareUpdateUserToWblListFund.json");

    Response response = given()
      .baseUri(ADD_USER_TO_WBL_LIST_FUND)
      .contentType(APPLICATION_JSON)
      .header(X_API_KEY, STP_X_API_KEY)
      .body(body)
      .post();

    assertThat(response.getStatusCode(), is(200));
  }

  public static Response callRegisterPartnership(List<String> linkType, String autoTransfer,
                                                 String isIAPaid, String iaBankAccount, String partnerAccountId) {

    String prepareValue = String.valueOf(new Date().getTime() / 10);
    String phoneNumber = CommonUtils.genPhoneNumberByDateTime();
    String email = "tcbs.customer" + prepareValue + "@gmail.com";

    HashMap<String, Object> identityCard = new HashMap<>();
    identityCard.put("idNumber", prepareValue);
    identityCard.put("idPlace", "Cục CS QLHC về TTXH");
    identityCard.put("idDate", "15/05/2021");
    identityCard.put("frontIdentity", "data:image/jpeg;base64,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");
    identityCard.put("backIdentity", "data:image/jpeg;base64,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");

    HashMap<String, Object> personalInfo = new HashMap<>();
    personalInfo.put("fullName", "Trương Thị Phượng");
    personalInfo.put("email", email);
    personalInfo.put(PHONE_NUMBER, phoneNumber);
    personalInfo.put(PHONE_CODE, "+84");
    personalInfo.put("gender", "FEMALE");
    personalInfo.put("birthday", "25/03/1995");
    personalInfo.put("contactAddress", "119 Bà Triệu, Hai Bà Trưng, Hà Nội");
    personalInfo.put("permanentAddress", "119, Trần Duy Hưng, Hà Nội");
    personalInfo.put("nationality", "VN");
    personalInfo.put("province", "HA-NOI");
    personalInfo.put("identityCard", identityCard);

    List<HashMap<String, Object>> bankAccounts = new ArrayList<>();
    HashMap<String, Object> bankAccount1 = new HashMap<>();
    bankAccount1.put("accountNo", "10" + prepareValue);
    bankAccount1.put("accountName", "Trương Thị Phượng");
    bankAccount1.put("bankCode", "01201001");
    bankAccount1.put("bankName", "NH TMCP CONG THUONG VN (VIETINBANK)");
    bankAccount1.put("bankProvince", null);
    bankAccount1.put("branchCode", null);
    bankAccount1.put("bankType", "CENTRALIZED_PAYMENT");
    bankAccounts.add(bankAccount1);

    HashMap<String, Object> connect = new HashMap<>();
    connect.put("linkType",linkType);
    connect.put("autoTransfer", autoTransfer);
    connect.put("isIAPaid", isIAPaid);
    connect.put("iaBankAccount", iaBankAccount);

    HashMap<String, Object> partnerInfo = new HashMap<>();
    partnerInfo.put("partnerId", "MBB");
    partnerInfo.put("kycLevel", "0");
    partnerInfo.put("partnerAccountId", partnerAccountId);
    partnerInfo.put("connect", connect);

    HashMap<String, Object> body = new HashMap<>();
    body.put("personalInfo", personalInfo);
    body.put("bankAccounts", bankAccounts);
    body.put("partnerInfo", partnerInfo);

    Response response = given()
      .baseUri(OPEN_ACCOUNT_PARTNER_REGISTER)
      .header(X_API_KEY, PARTNERSHIP_X_API_KEY)
      .contentType(APPLICATION_JSON)
      .body(body)
      .post();
    assertThat(response.statusCode(), is(200));
    return response;
  }
}
