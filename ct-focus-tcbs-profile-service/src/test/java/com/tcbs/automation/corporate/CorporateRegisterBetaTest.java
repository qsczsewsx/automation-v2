package com.tcbs.automation.corporate;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsBankAccount;
import com.tcbs.automation.cas.TcbsNewOnboardingStatus;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.cas.TcbsUserOpenAccountQueue;
import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.*;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/corporate/CorporateRegisterBeta.csv", separator = '|')
public class CorporateRegisterBetaTest {
  private String testCaseName;
  private int statusCode;
  private String fullName;
  private String fullName1;
  private String acronym;
  private String fullNameEN;
  private String phoneNumber;
  private String phoneCode;
  private String email;
  private String email1;
  private String taxNo;
  private String businessType;
  private String contactAddress;
  private String permanceAddress;
  private String nationality;
  private String idNumber;
  private String idPlace;
  private String idPlace1;
  private String idDate;
  private String idDate1;
  private String birthday;
  private String position;
  private String address;
  private String citizenShip;
  private String idType;
  private String typePerson;
  private String permissFromDate;
  private String permissToDate;
  private String bankCode;
  private String bankName;
  private String accountNo;
  private String chiefAccountantInfo;
  private final HashMap<String, Object> body = new HashMap<>();
  private String errorCode;
  private String errorMessage;

  @Before
  public void before() throws Exception {
    testCaseName = syncData(testCaseName);
    HashMap<String, Object> personalInfo = new HashMap<>();
    HashMap<String, Object> identityCard = new HashMap<>();
    personalInfo.put("fullName", fullName);
    personalInfo.put("acronym", "TRE");
    personalInfo.put("fullNameEN", "GREEN TREE COMPANY");
    personalInfo.put("phoneNumber", phoneNumber);
    personalInfo.put("phoneCode", phoneCode);
    personalInfo.put("email", email);
    personalInfo.put("taxNo", taxNo);
    personalInfo.put("businessType", businessType);
    personalInfo.put("contactAddress", contactAddress);
    personalInfo.put("permanceAddress", permanceAddress);
    personalInfo.put("nationality", nationality);
    personalInfo.put("identityCard", identityCard);

    identityCard.put("idNumber", idNumber);
    identityCard.put("idPlace", idPlace);
    identityCard.put("idDate", idDate);

    HashMap<String, Object> enterpriseInfo = new HashMap<>();
    HashMap<String, Object> representativePersons= new HashMap<>();
    List<Map<String, Object>> listRepresentativePersons = new ArrayList<>();

    representativePersons.put("fullName", fullName1);
    representativePersons.put("birthday", birthday);
    representativePersons.put("position", position);
    representativePersons.put("email", email1);
    representativePersons.put("phone", phoneNumber);
    representativePersons.put("idNumber", "123");
    representativePersons.put("idPlace", idPlace);
    representativePersons.put("idDate", idDate);
    representativePersons.put("address", address);
    representativePersons.put("citizenShip", citizenShip);
    representativePersons.put("idType", idType);
    representativePersons.put("typePerson", typePerson);
    representativePersons.put("permissFromDate", permissFromDate);
    representativePersons.put("permissToDate", permissToDate);
    listRepresentativePersons.add(representativePersons);

    List<HashMap<String, Object>> contactPersons = new ArrayList<>();
    List<HashMap<String, Object>> authorizedPersons = new ArrayList<>();
    enterpriseInfo.put("chiefAccountantInfo", null);
    enterpriseInfo.put("representativePersons", listRepresentativePersons);
    enterpriseInfo.put("contactPersons", contactPersons);
    enterpriseInfo.put("authorizedPersons", authorizedPersons);

    List<HashMap<String, Object>> bankAccounts = new ArrayList<>();
    HashMap<String, Object> bankAccount1 = new HashMap<>();
    bankAccount1.put("accountNo", accountNo);
    bankAccount1.put("accountName", fullName);
    bankAccount1.put("bankCode", bankCode);
    bankAccount1.put("bankName", bankName);
    bankAccount1.put("bankProvince", "HA-NOI");
    bankAccount1.put("branchCode", "NH TMCP KY THUONG HOI SO CHINH");
    bankAccount1.put("bankType", "CENTRALIZED_PAYMENT");
    bankAccounts.add(bankAccount1);

    HashMap<String, Object> additionalInfo = new HashMap<>();
    HashMap<String, Object> registeredFund = new HashMap<>();
    HashMap<String, Object> individualTrustorInfo = new HashMap<>();
    HashMap<String, Object> institutionalTrustorInfo = new HashMap<>();
    HashMap<String, Object> investmentKnowledge = new HashMap<>();
    registeredFund.put("others", "TEST");
    registeredFund.put("tcbf", "NO");
    registeredFund.put("tcef", "YES");
    registeredFund.put("tcff", "YES");
    individualTrustorInfo.put("birthday", "11/11/1981");
    individualTrustorInfo.put("fullName", "Nguyen Van A");
    individualTrustorInfo.put("issueDate", "11/11/2018");
    individualTrustorInfo.put("issuePlace", "Ha Noi");
    individualTrustorInfo.put("nationality", "VN");
    individualTrustorInfo.put("passportNo", "B1227826");
    institutionalTrustorInfo.put("abbreviation", "ABC");
    institutionalTrustorInfo.put("address", "Dong Da, Ha Noi");
    institutionalTrustorInfo.put("businessLicenseNo", "C9083023487");
    institutionalTrustorInfo.put("fullTradingName", "Nguyen Van A");
    institutionalTrustorInfo.put("issueDate", "11/11/2011");
    institutionalTrustorInfo.put("issuePlace", "Ha noi");
    investmentKnowledge.put("experience", "none");
    investmentKnowledge.put("objectives", "Long-Term");
    investmentKnowledge.put("riskTolerance", "low");
    investmentKnowledge.put("understanding", "Very-Good");

    List<HashMap<String, Object>> otherSecuritiesAccounts = new ArrayList<>();
    HashMap<String, Object> otherSecuritiesAccounts1 = new HashMap<>();
    otherSecuritiesAccounts1.put("securitiesCompany", "Công ty ABC");
    otherSecuritiesAccounts1.put("tradingAccountNumber", "2132437943247697234");
    otherSecuritiesAccounts.add(otherSecuritiesAccounts1);

    List<HashMap<String, Object>> managedPublicCompanys = new ArrayList<>();
    HashMap<String, Object> managedPublicCompanys1 = new HashMap<>();
    managedPublicCompanys1.put("companyName", "Công ty ABC");
    managedPublicCompanys1.put("title", "Manager");
    managedPublicCompanys.add(managedPublicCompanys1);

    List<HashMap<String, Object>> ownedPublicCompanys = new ArrayList<>();
    HashMap<String, Object> ownedPublicCompanys1 = new HashMap<>();
    ownedPublicCompanys1.put("companyName", "Công ty ABC");
    ownedPublicCompanys1.put("title", "Engineer");
    ownedPublicCompanys.add(ownedPublicCompanys1);

    List<HashMap<String, Object>> relatedPublicCompanys = new ArrayList<>();
    HashMap<String, Object> relatedPublicCompanys1 = new HashMap<>();
    relatedPublicCompanys1.put("issuer", "Công ty ABC");
    relatedPublicCompanys1.put("relationship", "Investor");
    relatedPublicCompanys1.put("securitiesCode", "VIC2012");
    relatedPublicCompanys1.put("title", "Engineer");
    relatedPublicCompanys.add(relatedPublicCompanys1);

    HashMap<String, Object> falcaInfo = new HashMap<>();
    falcaInfo.put("bornInUS", "YES");
    falcaInfo.put("haveRelatedUSCommunication", "YES");
    falcaInfo.put("haveUSAuthority", "YES");
    falcaInfo.put("haveUSFundTransfer", "YES");
    falcaInfo.put("usResident", "YES");

    if(testCaseName.contains("Additional")){
      additionalInfo.put("additionalInfo",null);
    }
    else {
      additionalInfo.put("registeredFund",registeredFund);
      additionalInfo.put("individualTrustorInfo",individualTrustorInfo);
      additionalInfo.put("institutionalTrustorInfo",institutionalTrustorInfo);
      additionalInfo.put("investmentKnowledge",investmentKnowledge);
      additionalInfo.put("otherSecuritiesAccounts",otherSecuritiesAccounts);
      additionalInfo.put("managedPublicCompanys",managedPublicCompanys);
      additionalInfo.put("ownedPublicCompanys",ownedPublicCompanys);
      additionalInfo.put("relatedPublicCompanys",relatedPublicCompanys);
      additionalInfo.put("falcaInfo",falcaInfo);
    }
    body.put("personalInfo", personalInfo);
    body.put("enterpriseInfo", enterpriseInfo);
    body.put("bankAccounts", bankAccounts);
    body.put("additionalInfo", additionalInfo);
    System.out.println(body);
  }
  @Test
  @TestCase(name = "#testCaseName")
  public void corporateRegisterBeta() {
    Gson gson = new Gson();
    Response response = given()
      .baseUri(PARTNERSHIP_REGISTER_BETA)
      .header("x-api-key", CORPORATE_X_API_KEY)
      .contentType("application/json")
      .header("Connection", "keep-alive")
      .body(gson.toJson(body))
      .when()
      .post();

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if(statusCode == 200){
      assertThat(response.jsonPath().get("basicInfo.tcbsId"), is(notNullValue()));
      assertThat(response.jsonPath().get("basicInfo.custodyCode"), is(nullValue()));
      assertThat(response.jsonPath().get("basicInfo.status"), is("ACTIVE"));
      assertThat(response.jsonPath().get("basicInfo.type"), is("BETA"));
      assertThat(response.jsonPath().get("basicInfo.accountType"), is("CORPORATE"));
      assertThat(response.jsonPath().get("accountStatus.fundActivationStatus"), is("0"));
      assertThat(response.jsonPath().get("accountStatus.flexActivationStatus"), is("0"));
      assertThat(response.jsonPath().get("accountStatus.onboardingStatus.preferActivationChannelStatus.value"), is("ONLINE_PREFER"));
      assertThat(response.jsonPath().get("accountStatus.onboardingStatus.eContractStatus.value"), is("WAIT_ADDITIONAL_INFO"));
      String userId = TcbsUser.getByPhoneNumber(phoneNumber).getId().toString();
      assertThat(userId, is(notNullValue()));
      // Lưu thông tin TK beta vào bảng tạm
      assertThat(TcbsUserOpenAccountQueue.getByPhone(phoneNumber).getUserId().toString(), is(userId));
      // Lưu thông tin ngân hàng
      assertThat(TcbsBankAccount.getBank(userId).getBankAccountNo(), is(accountNo));
      // Lưu thông tin doanh nghiệp select * from TCBS_USER where phone ='0935161296'
      // Lưu thông tin trạng thái newonboarding
      assertThat(TcbsNewOnboardingStatus.getByUserIdAndStatusKey(userId, "ID_STATUS").getStatusValue(), is("WAIT_FOR_VERIFY"));
      // Lưu thông tin người đại diện: need to confirm

    }
    else {
      assertThat("verify error message", response.jsonPath().get("code"), is(errorCode));
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }

}

