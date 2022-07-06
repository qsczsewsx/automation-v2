package com.tcbs.automation.newonboarding.handle;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsIdentification;
import com.tcbs.automation.cas.TcbsNewOnboardingStatus;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.cas.TcbsUserOpenaccountQueue;
import com.tcbs.automation.tools.SerenityTools;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.json.JSONObject;
import org.junit.Before;
import tasks.service.FSSService;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;


//@RunWith(SerenityParameterizedRunner.class)
//@UseTestDataFrom(value = "data/newonboarding/AutoRetryGen105C.csv", separator = '|')
public class AutoRetryGen105CTest {
  @Getter
  private String testCaseName;
  @Getter
  private String statusValueGen105C;
  private int numConfig;
  private Response response;
  private String statusValue;
  private String prepareValue;
  private String suffixPhone;

  @Before
  public void before() throws Exception {
    prepareValue = String.valueOf(new Date().getTime() / 10);
    suffixPhone = prepareValue.substring(4);

//      Call API Register
    String body = fileTxtToString("src/test/resources/requestBody/RegisterInfoBody.json")
      .replaceAll("#value#", prepareValue).replaceAll("#phone#", suffixPhone);
    JSONObject requestBody = new JSONObject(body);
    RequestSpecification respSpec = SerenityRest.given()
      .baseUri(TCBSPROFILE_DEV_DOMAIN + TCBSPROFILE_API + TCBSPROFILE_REGISTER)
      .contentType("application/json")
      .header("x-api-key", API_KEY)
      .body(body);
    Response resp = respSpec.when().post();
    assertEquals(200, resp.statusCode());

//      Set prepare data
    String refId = resp.jsonPath().get("data.refId").toString();
    String fullName = requestBody.getJSONObject("personalInfo").get("fullName").toString();
    String phoneNumber = requestBody.getJSONObject("personalInfo").get("phoneNumber").toString();
    String email = requestBody.getJSONObject("personalInfo").get("email").toString();

//      Insert data in TCBS_USER
    TcbsUser tcbsUser = new TcbsUser();
    tcbsUser.setLastname(fullName.substring(0, 10));
    tcbsUser.setFirstname(fullName.substring(11));
    tcbsUser.setPhone(phoneNumber);
    tcbsUser.setTcbsid(prepareValue.substring(1));
    tcbsUser.setEmail(email);
    tcbsUser.setGender(BigDecimal.valueOf(1));
    tcbsUser.setCustype(BigDecimal.valueOf(0));
    tcbsUser.setBirthday(Timestamp.valueOf(
      LocalDateTime.parse(requestBody.getJSONObject("personalInfo").get("birthday").toString(),
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"))));
    tcbsUser.insert();

    String identification = requestBody.getJSONObject("personalInfo").getJSONObject("identityCard").get("idNumber").toString();

//      Insert data in TCBS_IDENTIFICATION
    TcbsIdentification tcbsIdentification = new TcbsIdentification();
    tcbsIdentification.setUserId(tcbsUser.getId());
    tcbsIdentification.setIdType("I");
    tcbsIdentification.setIdPlace("Ha Noi");
    tcbsIdentification.setIdDate(Timestamp.valueOf("2020-12-23 00:00:00"));
    tcbsIdentification.setIdNumber(identification);
    tcbsIdentification.insert();

//      Insert data in TCBS_NEW_ONBOARDING_STATUS, setStatusValue=ACTIVATE_PENDING
    TcbsNewOnboardingStatus tcbsNewOnboardingStatus = new TcbsNewOnboardingStatus();
    tcbsNewOnboardingStatus.setUserId(tcbsUser.getId());
    tcbsNewOnboardingStatus.setGroupStatus("OPEN_ACCOUNT");
    tcbsNewOnboardingStatus.setStatusKey("GEN_105C");
    tcbsNewOnboardingStatus.setStatusValue("ACTIVATE_PENDING");
    tcbsNewOnboardingStatus.setRejectPerson("0");
    tcbsNewOnboardingStatus.insert();

    String userId = tcbsUser.getId().toString();

//      Update user_id in TCBS_OpenAccountQueue
    TcbsUserOpenaccountQueue.updateUserId(phoneNumber, userId);

//      Update statusValue
    if (statusValueGen105C.equals("OPEN_ACCOUNT")) {
      // Call API to FSS Service to open an account
      FSSService.accountOpenRequest(refId, fullName, identification, email, phoneNumber);
      TcbsNewOnboardingStatus.updateStatusValueByUserId(userId, "GEN_105C", "OPEN_ACCOUNT");
    } else if (statusValueGen105C.equals("CONFIRM_ACCOUNT")) {
      FSSService.accountOpenRequest(refId, fullName, identification, email, phoneNumber);
      // Call the API to FSS Service to confirm account opening
      FSSService.accountOpenConfirm(refId);
      TcbsNewOnboardingStatus.updateStatusValueByUserId(userId, "GEN_105C", "CONFIRM_ACCOUNT");
    }
  }

  @TestCase(name = "#testCaseName")
  @Title("Verify api auto retry gen 105C")
  public void perfomTest() {

    RequestSpecification requestSpecification = SerenityRest.given()
      .baseUri(TCBSPROFILE_AUTO_RETRY)
      .contentType("application/json")
      .header("x-api-key", API_KEY);
    do {
      response = requestSpecification.when().get();
      assertThat(response.statusCode(), is(200));
      String phoneNumber = "01" + suffixPhone;
      TcbsUser tcbsUserResult = TcbsUser.getByPhoneNumber(phoneNumber);
      TcbsNewOnboardingStatus tcbsNewObResult = TcbsNewOnboardingStatus.getByUserIdAndStatusKey(tcbsUserResult.getId().toString(), "GEN_105C");
      statusValue = tcbsNewObResult.getStatusValue();
      SerenityTools.manualReport("STATUS VALUE", statusValue);
      if (statusValue.equals("SYNC_105C")) {
        assertThat(tcbsUserResult.getUsername(), is(notNullValue()));
        break;
      } else {
        int retryCount = Objects.isNull(tcbsNewObResult.getRejectPerson()) ? 0 : Integer.parseInt(tcbsNewObResult.getRejectPerson());
        if (retryCount == numConfig) {
          assertThat(tcbsUserResult.getUsername(), is(nullValue()));
          break;
        }
      }
    } while (true);
  }

}
