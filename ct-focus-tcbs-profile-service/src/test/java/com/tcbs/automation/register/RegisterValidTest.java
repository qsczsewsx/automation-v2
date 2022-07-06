package com.tcbs.automation.register;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.negativequery.DeleteUserByPhone;
import com.tcbs.automation.cas.negativequery.GetPkVid;
import io.restassured.http.Header;
import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tasks.register.RegisterAPI;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceKey.TCBS_PS_REGISTER_RESP;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static net.serenitybdd.screenplay.GivenWhenThen.when;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

enum Gender {
  MALE(1, "MALE"),
  FEMALE(0, "FEMALE");

  private final Integer key;
  private final String value;

  Gender(Integer key, String value) {
    this.value = value;
    this.key = key;
  }

  public static Integer getStatusValue(String value) {
    for (Gender status : values()) {
      if (status.value.equals(value)) {
        return status.key;
      }
    }
    return null;
  }
}

@UseTestDataFrom(value = "data/register/Success.csv", separator = '|')
@RunWith(SerenityParameterizedRunner.class)
public class RegisterValidTest {
  private Actor user;
  private String description;
  private String body;
  private String method;
  private String url;
  private Header xApiKey;
  private Header contentType;
  private JSONObject request;

  @Before
  public void before() throws Exception {
    user = Actor.named("Focus Tester");
    url = TCBSPROFILE_API + TCBSPROFILE_REGISTER;
    method = "POST";
    xApiKey = new Header("x-api-key", API_KEY);
    contentType = new Header("Content-Type", "application/json");
    body = fileTxtToString("src/test/resources/requestBody/RegisterBody.json");
    request = new JSONObject(body);
    JSONObject personalInfo = new JSONObject(request.get("personalInfo").toString());
    DeleteUserByPhone.deleteUserByPhone(personalInfo.getString("phoneNumber"));
  }

  @Test
  @TestCase(name = "#description")
  public void verifyApiRegisterWithValidRequest() throws ParseException {
    when(user).attemptsTo(RegisterAPI.withInfo(method, url, xApiKey, contentType, body));
    Response resp = user.recall(TCBS_PS_REGISTER_RESP);
    assertEquals(200, resp.statusCode());
    JSONObject responseBody = new JSONObject(resp.getBody().prettyPrint());
    JSONObject data = new JSONObject(responseBody.get("data").toString());
    assertTrue(compareData(data));
  }

  public boolean compareData(JSONObject data) throws ParseException {
    String refId = data.getString("refId");
    GetPkVid response = GetPkVid.query(refId);
    if (!response.getPkVid().equals(request.getString("pkVid"))) {
      return false;
    }
    if (!response.getReceiveAdvertise().equals(request.get("receiveAdvertise"))) {
      return false;
    }

    if (!comparePersonalInfo(response)) {
      return false;
    }

    if (!compareBankAccount(response)) {
      return false;
    }

    JSONObject wealthPartnerInfo = new JSONObject(request.get("wealthPartnerInfo").toString());
    if (Objects.nonNull(wealthPartnerInfo.get("iwealthPartnerCode")) && Objects.nonNull(response.getWealthPartnerCode()) && !response.getWealthPartnerCode().equals(
      wealthPartnerInfo.getString("iwealthPartnerCode"))) {
      return false;
    }
    return !Objects.nonNull(wealthPartnerInfo.get("iwealthPartnerChannel")) || !Objects.nonNull(response.getWealthPartnerChannel()) || response.getWealthPartnerChannel().equals(
      wealthPartnerInfo.getString("iwealthPartnerChannel"));
  }

  public boolean comparePersonalInfo(GetPkVid response) throws ParseException {
    JSONObject personalInfo = new JSONObject(request.get("personalInfo").toString());
    if (!response.getEmail().equals(personalInfo.getString("email"))) {
      return false;
    }
    if (!response.getPhone().equals(personalInfo.getString("phoneNumber"))) {
      return false;
    }
    if (!response.getAddress().equals(personalInfo.getString("contactAddress"))) {
      return false;
    }
    if (!response.getCitizenship().equals(personalInfo.getString("nationality"))) {
      return false;
    }
    Date birthday = new SimpleDateFormat("yyyy-MM-dd").parse(personalInfo.getString("birthday"));
    if (response.getBirthday().compareTo(birthday) == 0) {
      return false;
    }
    if (!response.getProvince().equals(personalInfo.getString("province"))) {
      return false;
    }
    if (Gender.getStatusValue(personalInfo.getString("gender")) != response.getGender()) {
      return false;
    }
    if (!(response.getLastName() + " " + response.getFirstName()).equals(personalInfo.getString("fullName"))) {
      return false;
    }

    return compareIdentity(personalInfo, response);
  }

  public boolean compareBankAccount(GetPkVid response) {
    JSONArray bankAccounts = request.optJSONArray("bankAccounts");
    JSONObject bankAccount = bankAccounts.getJSONObject(0);
    if (!response.getBankAccountName().equals(bankAccount.getString("accountName"))) {
      return false;
    }
    if (!response.getBankAccountNumber().equals(bankAccount.getString("accountNo"))) {
      return false;
    }
    if (!response.getBankCode().equals(bankAccount.getString("bankCode"))) {
      return false;
    }
    if (!response.getBankName().equals(bankAccount.getString("bankName"))) {
      return false;
    }
    if (Objects.nonNull(bankAccount.get("bankProvince")) && Objects.nonNull(response.getBankProvince()) && !response.getBankProvince().equals(bankAccount.get("bankProvince"))) {
      return false;
    }
    return !Objects.nonNull(bankAccount.get("branchCode")) || !Objects.nonNull(response.getBankBranch()) || response.getBankBranch().equals(bankAccount.get("branchCode"));
  }

  public boolean compareIdentity(JSONObject data, GetPkVid response) throws ParseException {
    JSONObject identityCard = new JSONObject(data.get("identityCard").toString());
    if (!response.getIdNumber().equals(identityCard.getString("idNumber"))) {
      return false;
    }
    if (!response.getIdPlace().equals(identityCard.getString("idPlace"))) {
      return false;
    }
    Date idDate = new SimpleDateFormat("yyyy-MM-dd").parse(identityCard.getString("idDate"));
    return response.getIdDate().compareTo(idDate) != 0;
  }
}



