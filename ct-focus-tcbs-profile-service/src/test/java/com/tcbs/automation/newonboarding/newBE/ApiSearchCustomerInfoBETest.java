package com.tcbs.automation.newonboarding.newBE;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.SEARCH_CUSTOMER_INFO_BE;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static common.DatesUtils.convertTimestampToString;
import static common.DatesUtils.covertDateToString;
import static common.ProfileTools.TOKEN;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiSearchCustomerInfoBE.csv", separator = '|')
public class ApiSearchCustomerInfoBETest {

  private final List<TcbsBankAccount> tcbsBankAccounts = new ArrayList<>();
  private final List<TcbsBankSubaccount> tcbsBankSubaccounts = new ArrayList<>();
  @Getter
  private String testCaseName;
  @Getter
  private String params;
  private String paramValue;
  private int statusCode;
  private String errorMsg;
  private String fields;
  private TcbsUser tcbsUser;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Search Customer Info BE")
  public void verifyApiSearchCustomerInfoBETest() {

    System.out.println("TestCaseName : " + testCaseName);

    fields = syncData(fields);

    Response response = callSearchCustInfoBeApi(params, fields);

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {
//      if (testCaseName.contains("with valid")) {
//        verifyContractTrustDtoField(response, params, paramValue);
//        verifyKycHistoryDtosField(response, params, paramValue);
//      }
      verifyProfileDtoField(response, params, paramValue);
      if (fields != null) {
        for (String item : fields.split(",", -1)) {
          assertThat(response.jsonPath().getMap("profileDto"), hasKey(item));
        }
      }

    } else if (response.statusCode() == 400) {
      String actualMessage = response.jsonPath().get("message");
      assertEquals(errorMsg, actualMessage);
    }
  }

  public Response callSearchCustInfoBeApi(String params, String fields) {

    Response response;
    if (params.equalsIgnoreCase("paramNull")) {
      response = getSearchCustInfoBeApiRequest()
        .get();
    } else if (fields == null) {
      response = getSearchCustInfoBeApiRequest()
        .param(params, paramValue)
        .get();
    } else {
      response = getSearchCustInfoBeApiRequest()
        .param(params, paramValue)
        .param("fields", fields)
        .get();
    }
    return response;
  }

  public RequestSpecification getSearchCustInfoBeApiRequest() {
    return given()
      .baseUri(SEARCH_CUSTOMER_INFO_BE)
      .header("Authorization", "Bearer " + TOKEN);
  }

  public TcbsUser getTcbsUser(String params, String paramValue) {
    TcbsUser tcbsUser1;
    switch (params) {
      case "phone":
        tcbsUser1 = TcbsUser.getByPhoneNumber(paramValue);
        break;
      case "code105c":
        tcbsUser1 = TcbsUser.getByUserName(paramValue);
        break;
      case "email":
        tcbsUser1 = TcbsUser.getByEmail(paramValue);
        break;
      case "idNumber":
        TcbsIdentification tcbsIdentification = TcbsIdentification.getByIdNumber(paramValue);
        tcbsUser1 = TcbsUser.getById(tcbsIdentification.getUserId());
        break;
      case "tcbsId":
        tcbsUser1 = TcbsUser.getByTcbsId(paramValue);
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + params);
    }

    return tcbsUser1;
  }

  public void verifyContractTrustDtoField(Response response, String params, String paramValue) {

    HashMap<String, Object> contractTrust = response.jsonPath().getJsonObject("contractTrustDto");

    tcbsUser = getTcbsUser(params, paramValue);
    TcbsContractTrust tcbsContractTrust = TcbsContractTrust.getByTcbsId(tcbsUser.getTcbsid());

    assertEquals(tcbsContractTrust.getTcbsid(), contractTrust.get("tcbsId"));
    assertEquals(tcbsContractTrust.getFullName(), contractTrust.get("fullName"));
    assertEquals(tcbsContractTrust.getBirthday(), contractTrust.get("birthday"));
    assertEquals(tcbsContractTrust.getNationnality(), contractTrust.get("nationnality"));
    assertEquals(tcbsContractTrust.getPassportNo(), contractTrust.get("passportNo"));
    assertEquals(tcbsContractTrust.getIssueDate(), contractTrust.get("issueDate"));
    assertEquals(tcbsContractTrust.getIssuePlace(), contractTrust.get("issuePlace"));
    assertEquals(tcbsContractTrust.getType(), contractTrust.get("type"));
    assertEquals(tcbsContractTrust.getShortName(), contractTrust.get("shortName"));
    assertEquals(tcbsContractTrust.getHeadquarters(), contractTrust.get("headquaters"));
    assertEquals(tcbsContractTrust.getEnterpriseCode(), contractTrust.get("eterpriseCode"));
  }

  public void verifyKycHistoryDtosField(Response response, String params, String paramValue) {

    List<HashMap<String, Object>> kycHistoryList = response.jsonPath().getList("kycHistoryDtos");
    tcbsUser = getTcbsUser(params, paramValue);
    List<ObTask> obTaskList = ObTask.getListByUserId(tcbsUser.getId());

    for (int i = 0; i < obTaskList.size(); i++) {
      assertEquals(obTaskList.get(i).getId().toString(), kycHistoryList.get(i).get("taskId").toString());
      assertEquals(obTaskList.get(i).getUserId().toString(), kycHistoryList.get(i).get("userId").toString());
      assertEquals(covertDateToString(obTaskList.get(i).getCreatedDatetime()), convertTimestampToString((kycHistoryList.get(i).get("createdDatetime").toString())));
      assertEquals(obTaskList.get(i).getStatus(), kycHistoryList.get(i).get("status"));
      assertEquals(obTaskList.get(i).getKycStatus(), kycHistoryList.get(i).get("kycStatus"));
      assertEquals(obTaskList.get(i).getReason(), kycHistoryList.get(i).get("reason"));
      assertEquals(obTaskList.get(i).getActor(), kycHistoryList.get(i).get("actor"));
      if (testCaseName.contains("task with status CANCELED")) {
        assertThat(kycHistoryList.get(i).get("status"), not("CANCELED"));
      }
    }

  }

  public void verifyProfileDtoField(Response response, String params, String paramValue) {

//    List<HashMap<String, Object>> bankAccounts = response.jsonPath().get("profileDto.bankAccounts");
//    List<HashMap<String, Object>> bankSubAccounts = response.jsonPath().get("profileDto.bankSubAccounts");
    HashMap<String, Object> accountStatus = response.jsonPath().get("profileDto.accountStatus");
    HashMap<String, Object> personalInfo = response.jsonPath().get("profileDto.personalInfo");
    HashMap<String, Object> closeInfo = response.jsonPath().get("profileDto.closeInfo");
    tcbsUser = getTcbsUser(params, paramValue);

    if (testCaseName.contains("account is opened via NFM")) {
      assertThat(personalInfo.get("subFlowOpenAccount"), is(notNullValue()));
    } else if (testCaseName.contains("is VSD activated")) {
      assertThat(accountStatus.get("derivativeActivationStatus"), is(notNullValue()));
    } else if (testCaseName.contains("has task with status CANCELED")) {
      assertThat(personalInfo.get("flowOpenAccount"), is(2));
      assertThat(accountStatus.get("subFlowOpenAccount"), is(nullValue()));
    } else if (testCaseName.contains("closeInfo")) {
      assertThat(closeInfo.get("channel"), is(notNullValue()));
    }
//    } else {
//      tcbsBankAccounts = TcbsBankAccount.getListBanks(tcbsUser.getId().toString());
//      for (int i = 0; i < tcbsBankAccounts.size(); i++) {
//        assertThat(bankAccounts.get(i).get("bankAccountId").toString(), is(tcbsBankAccounts.get(i).getId().toString()));
//        assertThat(bankAccounts.get(i).get("accountNo"), is(tcbsBankAccounts.get(i).getBankAccountNo()));
//        assertThat(bankAccounts.get(i).get("bankSys"), is(notNullValue()));
//      }
//      tcbsBankSubaccounts = TcbsBankSubaccount.getListBank(tcbsUser.getId().toString());
//      for (int i = 0; i < tcbsBankSubaccounts.size(); i++) {
//        assertThat(bankSubAccounts.get(i).get("accountNo"), is(tcbsBankSubaccounts.get(i).getAccountNo()));
//        assertThat(bankSubAccounts.get(i).get("status"), is(tcbsBankSubaccounts.get(i).getStatus().toString()));
//      }
//
//    }

  }
}
