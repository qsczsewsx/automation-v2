package com.tcbs.automation.newonboarding.handle;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsUser;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;

import java.util.Date;
import java.util.LinkedHashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

//@RunWith(SerenityParameterizedRunner.class)
//@UseTestDataFrom(value = "data/newonboarding/NewGen105C.csv", separator = '|')
public class NewGen105CTest {
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
  public void setup() {
    prepareValue = String.valueOf(new Date().getTime() / 10);
    suffixPhone = prepareValue.substring(4);
  }

  @Ignore
  @TestCase(name = "#testCaseName")
  @Title("Verify new flow gen 105C")
  public void perfomTest() {
    // Call API Register
    String bodyRegister = fileTxtToString("src/test/resources/requestBody/RegisterInfoBody.json")
      .replaceAll("#value#", prepareValue).replaceAll("#phone#", suffixPhone);
    Response respRegister = SerenityRest.given()
      .baseUri(TCBSPROFILE_DEV_DOMAIN + TCBSPROFILE_API + TCBSPROFILE_REGISTER)
      .contentType("application/json")
      .header("x-api-key", API_KEY)
      .body(bodyRegister)
      .post();
    assertThat(respRegister.statusCode(), is(200));

    // Call API Activate Pending
    LinkedHashMap<String, Object> bodyActivate = new LinkedHashMap<>();
    bodyActivate.put("otp", "111111");
    bodyActivate.put("transactionId", respRegister.jsonPath().get("data.otpId"));
    bodyActivate.put("token", respRegister.jsonPath().get("data.token"));

    Response respActivate = SerenityRest.given()
      .baseUri(TCBSPROFILE_DEV_DOMAIN + TCBSPROFILE_API + TCBSPROFILE_REGISTER)
      .contentType("application/json")
      .header("x-api-key", API_KEY)
      .body(bodyActivate)
      .post();
    assertThat(respActivate.statusCode(), is(200));

    String phoneNumber = "01" + suffixPhone;
    assertThat(TcbsUser.getByPhoneNumber(phoneNumber).getUsername(), is(notNullValue()));
  }

  @After
  public void clearData() {
  }
}
