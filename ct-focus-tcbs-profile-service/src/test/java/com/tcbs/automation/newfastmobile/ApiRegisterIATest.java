package com.tcbs.automation.newfastmobile;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsBankIaaccount;
import com.tcbs.automation.cas.TcbsNewOnboardingStatus;
import com.tcbs.automation.cas.TcbsUser;
import common.CallApiUtils;
import common.CommonUtils;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static common.CallApiUtils.callPostApiHasBody;
import static common.CommonUtils.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newfastmobile/ApiRegisterIA.csv", separator = '|')
public class ApiRegisterIATest {

  private String tcbsId;
  private String idNumberVal;
  private String getPhoneNumber;
  private String getEmail;
  private String prepareValue;
  @Getter
  private String testCaseName;
  @Getter
  private int statusCode;
  private String nationality;
  private String province;
  private String fullName;
  private String email;
  private String birthday;
  private String idNumber;
  private String idPlace;
  private String idDate;
  private String accountNo;
  private String accountName;
  private String bankCode;
  private String bankName;
  private String bankProvince;
  private String branchCode;
  private String bankType;
  private String autoTransfer;
  private String isIAPaid;
  private String isIA;
  private String message;
  private String userId;
  private String getTcbsId;
  private String action;

  @Before
  public void beforeTestCase() {

    prepareValue = String.valueOf(new Date().getTime());
    idNumberVal = prepareValue.substring(0, 12);
    getPhoneNumber = "0" + prepareValue.substring(3, 12);

    if (action.equalsIgnoreCase("CANCEL with")) {
      tcbsId = CallApiUtils.prepareRegIA(idNumberVal, getPhoneNumber);
      LinkedHashMap<String, Object> body = CommonUtils.getRegisterIABody(testCaseName, accountNo, accountName, bankCode, bankName, bankType,
        autoTransfer, isIAPaid, "CREATE", isIA);
      Response responseBf = callPostApiHasBody(FMB_REGISTER_IA.replace("{tcbsId}", tcbsId), "x-api-key", FMB_X_API_KEY, body);
      assertThat(responseBf.getStatusCode(), is(200));
      userId = TcbsUser.getByTcbsId(tcbsId).getId().toString();
      TcbsNewOnboardingStatus.updateStatusValueByUserId(userId, "ID_STATUS", "VERIFIED");
      TcbsNewOnboardingStatus.updateStatusValueByUserId(userId, "ECONTRACT_STATUS", "VERIFIED");
      TcbsBankIaaccount.updateStatusByUserId(userId, "1");
      CallApiUtils.clearCache(CLEAR_CACHE, "x-api-key", API_KEY);
    } else {
      if (action.equalsIgnoreCase("CREATE")) {
        tcbsId = CallApiUtils.prepareRegIA(idNumberVal, getPhoneNumber);
      } else {
        tcbsId = "10000025740";
      }
      userId = TcbsUser.getByTcbsId(tcbsId).getId().toString();
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Register IA")
  public void verifyRegisterIATest() {

    System.out.println("TestCaseName : " + testCaseName);

    getTcbsId = getDesiredData(testCaseName, "invalid tcbsid", "10000017565", tcbsId);
    LinkedHashMap<String, Object> body = CommonUtils.getRegisterIABody(testCaseName, accountNo, accountName, bankCode, bankName, bankType,
      autoTransfer, isIAPaid, action, isIA);

    Response response = callPostApiHasBody(FMB_REGISTER_IA.replace("{tcbsId}", getTcbsId), "x-api-key", FMB_X_API_KEY, body);

    assertThat(response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      verifyBasicInfo(response, tcbsId);
      verifyOnboardingStatus(response, tcbsId);
      verifyInputAndOutput(response, userId);
      verifyFMBankIaInfo(response, "iaBankAccount", "userId", userId);
    } else if (statusCode == 400) {
      assertEquals(message, response.jsonPath().get("message"));
    }
  }

  public void verifyBasicInfo(Response response, String tcbsId) {
    Map<String, Object> basicInfoResponse = response.jsonPath().getMap("basicInfo");
    TcbsUser tcbsUser = TcbsUser.getByTcbsId(tcbsId);
    assertEquals(tcbsUser.getTcbsid(), basicInfoResponse.get("tcbsId"));
    assertEquals(tcbsUser.getUsername(), basicInfoResponse.get("code105C"));
    String status = getUserStatus(tcbsUser.getAccountStatus().toString());
    assertEquals(status, basicInfoResponse.get("status"));
    String type = getUserType(tcbsUser.getCustype().toString());
    assertEquals(type, basicInfoResponse.get("type"));
    String accountType = getAccountType(tcbsUser.getUsername());
    assertEquals(accountType, basicInfoResponse.get("accountType"));
  }

  public void verifyOnboardingStatus(Response response, String tcbsId) {
    userId = TcbsUser.getByTcbsId(tcbsId).getId().toString();
    verifyFMBOnboardingStatus(response, "accountStatus.onboardingStatus.", "userId", userId);
  }

  public void verifyInputAndOutput(Response response, String userId) {
    TcbsBankIaaccount tcbsBankIaaccount = TcbsBankIaaccount.getListBanks(userId).get(0);
    assertEquals(accountNo, tcbsBankIaaccount.getAccountNo());
    assertEquals(accountName, tcbsBankIaaccount.getAccountName());
    assertEquals(bankCode, tcbsBankIaaccount.getBankCode());
    assertEquals(bankName, tcbsBankIaaccount.getBankName());
    Map<String, Object> regIAStatusResponse = response.jsonPath().getMap("accountStatus.iaStatus");
    assertEquals(autoTransfer, regIAStatusResponse.get("autoTransfer"));
    assertEquals(isIAPaid, regIAStatusResponse.get("isIAPaid"));
  }

}
