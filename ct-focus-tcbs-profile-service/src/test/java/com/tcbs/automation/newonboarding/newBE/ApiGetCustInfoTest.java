package com.tcbs.automation.newonboarding.newBE;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.*;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static common.CallApiUtils.callNewOBMakerRejectApi;
import static common.CallApiUtils.clearCache;
import static common.CommonUtils.createTaskNewOBAndAssignToMaker;
import static common.CommonUtils.getDerivativeActivationStatus;
import static common.ProfileTools.TOKEN;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiGetCustInfo.csv", separator = '|')
public class ApiGetCustInfoTest {
  private static String getTaskId;
  @Getter
  private String testCaseName;
  @Getter
  private String testCaseKey;
  private String taskId;
  private String fieldsVal;
  private String onboardingStatus;
  private int statusCode;
  private String errorMsg;
  private TcbsUser tcbsUser;
  private ObTask obTask;

  @BeforeClass
  public static void creatAndAssignTask() {
    getTaskId = createTaskNewOBAndAssignToMaker("10000000744");
  }

  @AfterClass
  public static void rejectTask() {
    callNewOBMakerRejectApi(getTaskId);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get inquiry group info")
  public void verifyApiGetCustInfoTest() {

    System.out.println(testCaseName);
    taskId = getsTaskId(taskId);

    clearCache(DELETE_CACHE, "x-api-key", API_KEY);

    Response response = given()
      .baseUri(GET_CUSTOMER_INFO.replace("{taskId}", taskId))
      .header("Authorization", "Bearer " + TOKEN)
      .param("fields", fieldsVal)
      .param("onboarding", onboardingStatus)
      .when()
      .get();

    assertThat(response.getStatusCode(), is(statusCode));
    if (response.statusCode() == 200) {
      if (testCaseKey.equalsIgnoreCase("missing value of param onboarding in Url") ||
        testCaseKey.equalsIgnoreCase("value of param onboarding as false") ||
        testCaseKey.equalsIgnoreCase("missing param onboarding in Url")) {

        verifyPersonalInfoField(response);
        verifyAccountStatusField(response);
        verifyBankAccountsField(response);
      } else if (testCaseKey.equalsIgnoreCase("missing fields in Url") ||
        testCaseKey.equalsIgnoreCase("value of param field as all") ||
        testCaseKey.equalsIgnoreCase("missing value in fields")) {
        verifyPersonalInfoField(response);
        verifyAccountStatusField(response);
        verifyBankAccountsField(response);
      } else if (testCaseKey.equalsIgnoreCase("value of param field as only personalInfo")) {
        verifyPersonalInfoField(response);
        String responseData = response.jsonPath().getString("originalProfile");
        assertFalse(responseData.contains("accountStatus"));
        assertFalse(responseData.contains("bankAccounts"));
      } else if (testCaseKey.equalsIgnoreCase("value of param field as only accountStatus")) {
        verifyAccountStatusField(response);
        String responseData = response.jsonPath().getString("originalProfile");
        assertFalse(responseData.contains("personalInfo"));
        assertFalse(responseData.contains("bankAccounts"));
      } else if (testCaseKey.equalsIgnoreCase("value of param field as only bankAccounts")) {
        verifyBankAccountsField(response);
        String responseData = response.jsonPath().getString("originalProfile");
        assertFalse(responseData.contains("accountStatus"));
        assertFalse(responseData.contains("personalInfo"));
      } else {
        assertNull(response.jsonPath().get("originalProfile"));

      }

    } else {
      assertEquals(errorMsg, response.jsonPath().getString("message"));
    }
  }

  public void verifyAccountStatusField(Response response) {

    String fundAccount = response.jsonPath().getString("originalProfile.accountStatus.fundAccount");
    String flexAccount = response.jsonPath().getString("originalProfile.accountStatus.flexAccount");
    String tcBondAccount = response.jsonPath().getString("originalProfile.accountStatus.tcBondAccount");
    String derivativeActivationStatus = response.jsonPath().getString("originalProfile.accountStatus.derivativeActivationStatus");
    obTask = ObTask.getByTaskId(taskId);
    tcbsUser = TcbsUser.getById(obTask.getUserId());
    if (TcbsApplicationUser.getByUserAppId(tcbsUser.getUsername(), "7").size() > 0) {
      TcbsApplicationUser tcbsApplicationUser = TcbsApplicationUser.getByUserAppId(tcbsUser.getUsername(), "7").get(0);
      assertThat(tcbsApplicationUser.getStatus(), is(getDerivativeActivationStatus(derivativeActivationStatus)));
    } else {
      assertEquals("NOT_VSD_ACTIVATE_YET", derivativeActivationStatus);
    }

    assertEquals(tcbsUser.getUsername(), fundAccount);
    assertEquals(tcbsUser.getUsername(), flexAccount);
    assertEquals(tcbsUser.getTcbsid(), tcBondAccount);
    List<TcbsNewOnboardingStatus> tcbsNewOnboardingStatusList = TcbsNewOnboardingStatus.getByUserId(obTask.getUserId());
    for (TcbsNewOnboardingStatus tcbsNewOnboardingStatus : tcbsNewOnboardingStatusList) {
      String statusKey = tcbsNewOnboardingStatus.getStatusKey();
      switch (statusKey) {
        case "ECONTRACT_STATUS":
          String econtractStatus = response.jsonPath().getString("originalProfile.accountStatus.onboardingStatus.econtractStatus.value");
          assertEquals(tcbsNewOnboardingStatus.getStatusValue(), econtractStatus);
          break;
        case "DOCUMENT_STATUS":
          String documentStatus = response.jsonPath().getString("originalProfile.accountStatus.onboardingStatus.documentStatus.value");
          assertEquals(tcbsNewOnboardingStatus.getStatusValue(), documentStatus);
          break;
        case "ID_STATUS":
          String idStatus = response.jsonPath().getString("originalProfile.accountStatus.onboardingStatus.idStatus.value");
          assertEquals(tcbsNewOnboardingStatus.getStatusValue(), idStatus);
          break;
        case "ACTIVATED_STATUS":
          String activatedStatus = response.jsonPath().getString("originalProfile.accountStatus.onboardingStatus.activatedStatus.value");
          assertEquals(tcbsNewOnboardingStatus.getStatusValue(), activatedStatus);
          break;
        case "COUNTER_KYC_STATUS":
          String counterKycStatus = response.jsonPath().getString("originalProfile.accountStatus.onboardingStatus.counterKycStatus.value");
          assertEquals(tcbsNewOnboardingStatus.getStatusValue(), counterKycStatus);
          break;
        case "PRIVATE_ACCOUNT_STATUS":
          String privateAccountStatus = response.jsonPath().getString("originalProfile.accountStatus.onboardingStatus.privateAccountStatus.value");
          assertEquals(tcbsNewOnboardingStatus.getStatusValue(), privateAccountStatus);
          break;
        case "EKYC_STATUS":
          String ekycStatus = response.jsonPath().getString("originalProfile.accountStatus.onboardingStatus.ekycStatus.value");
          assertEquals(tcbsNewOnboardingStatus.getStatusValue(), ekycStatus);
          break;
        case "BANK_INFO_STATUS":
          String bankInfoStatus = response.jsonPath().getString("originalProfile.accountStatus.onboardingStatus.bankInfoStatus.value");
          assertEquals(tcbsNewOnboardingStatus.getStatusValue(), bankInfoStatus);
          break;
        case "PREFER_ACTIVATION_CHANNEL":
          String preferActivationChannelStatus = response.jsonPath().getString("originalProfile.accountStatus.onboardingStatus.preferActivationChannelStatus.value");
          assertEquals(tcbsNewOnboardingStatus.getStatusValue(), preferActivationChannelStatus);
          break;
      }
    }
  }

  public void verifyPersonalInfoField(Response response) {

    Map<String, Object> getResponse = response.jsonPath().getMap("originalProfile.personalInfo");
    Map<String, Object> getIdentityResponse = response.jsonPath().getMap("originalProfile.personalInfo.identityCard");

    obTask = ObTask.getByTaskId(taskId);
    tcbsUser = TcbsUser.getById(obTask.getUserId());
    TcbsAddress tcbsAddress = TcbsAddress.getByTcbsAddress(obTask.getUserId().toString());
    TcbsUserInstrument tcbsUserInstrument = TcbsUserInstrument.getByTcbsInstrument(obTask.getUserId().toString());
    TcbsIdentification tcbsIdentification = TcbsIdentification.getByTcbsIdentification(obTask.getUserId().toString());
    assertEquals(tcbsUser.getLastname() + " " + tcbsUser.getFirstname(), getResponse.get("fullName"));
    assertEquals(tcbsUser.getFirstname(), getResponse.get("firstName"));
    assertEquals(tcbsUser.getLastname(), getResponse.get("lastName"));
    assertEquals(tcbsUser.getEmail(), getResponse.get("email"));
    assertEquals(tcbsUser.getPhone(), getResponse.get("phoneNumber"));
    assertEquals(tcbsAddress.getAddress(), getResponse.get("contactAddress"));
    assertEquals(tcbsUserInstrument.getCitizenship(), getResponse.get("nationality"));
    assertEquals(tcbsIdentification.getIdNumber(), getIdentityResponse.get("idNumber"));
    assertEquals(tcbsIdentification.getIdPlace(), getIdentityResponse.get("idPlace"));
  }

  public void verifyBankAccountsField(Response response) {

    List<Map<String, Object>> getResponse = response.jsonPath().getList("originalProfile.bankAccounts");

    obTask = ObTask.getByTaskId(taskId);
    TcbsBankAccount tcbsBankAccount = TcbsBankAccount.getListBanks(obTask.getUserId().toString()).get(0);

    assertEquals(tcbsBankAccount.getId().toString(), getResponse.get(0).get("bankAccountId").toString());
    assertEquals(tcbsBankAccount.getBankAccountNo(), getResponse.get(0).get("accountNo"));
    assertEquals(tcbsBankAccount.getBankAccountName(), getResponse.get(0).get("accountName"));
    assertEquals(tcbsBankAccount.getBankCode(), getResponse.get(0).get("bankCode"));
    assertEquals(tcbsBankAccount.getBankName(), getResponse.get(0).get("bankName"));
  }

  public String getsTaskId(String taskId) {
    String getsTaskId;
    if (taskId.equalsIgnoreCase("761")) {
      getsTaskId = getTaskId;
    } else {
      getsTaskId = taskId;
    }
    return getsTaskId;
  }
}
