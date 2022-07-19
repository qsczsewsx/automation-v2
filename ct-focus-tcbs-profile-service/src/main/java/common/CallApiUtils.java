package common;

import com.google.gson.Gson;
import com.tcbs.automation.cas.ObTask;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;

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

    assertEquals(response.statusCode(), 200);
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
    CallApiUtils.clearCache(CLEAR_CACHE, X_API_KEY, API_KEY);

    LinkedHashMap<String, Object> bodyBeta = getFMBRegisterBetaBody(idNumberVal);
    Response response1 = getFMBRegisterBetaResponse(bodyBeta);
    String tcbsId = response1.jsonPath().getString("basicInfo.tcbsId");
    CallApiUtils.clearCache(CLEAR_CACHE_REDIS.replace("{phoneNumber}", getPhoneNumber), X_API_KEY, TOKEN);

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
    body.put("phoneNumber", phoneNumber);
    body.put("phoneCode", phoneCode);

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
      .replace("#phoneCode#", referenceId.substring(0,3))
      .replace("#phoneNumber#", referenceId.substring(3,12))
      .replace("#referenceId#", referenceId);
    Response response = given()
      .baseUri(REGISTER_CONFIRM_PHONE)
      .contentType(APPLICATION_JSON)
      .body(body)
      .post();
    assertThat(response.statusCode(), is(200));
    return response.jsonPath().get("authenKey").toString();
  }


}
