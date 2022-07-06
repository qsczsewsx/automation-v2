package com.tcbs.automation.newonboarding.rm_channel;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.thucydides.core.annotations.Title;
import org.junit.Before;
import org.junit.Ignore;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.TCBSPROFILE_RM_OPEN_ACCOUNT;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static common.EncodeDataUtils.encodeValue;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

//@RunWith(SerenityParameterizedRunner.class)
//@UseTestDataFrom(value = "data/newonboarding/RmOpenAccount.csv", separator = '|')
public class RmOpenAccountTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String data;
  private String fullName;
  private String phone;
  private String email;
  private String birthday;
  private String idNumber;
  private String gender;
  private String idPlace;
  private String idDate;
  private String contactAddress;
  private String body;

  @Before
  public void setup() throws UnsupportedEncodingException {

    fullName = encodeValue(fullName);
    birthday = encodeValue(birthday);
    contactAddress = encodeValue(contactAddress);
    idDate = encodeValue(idDate);
    idPlace = encodeValue(idPlace);

    String prepareValue = String.valueOf(new Date().getTime());
    HashMap<String, String> resultPrepareData = new HashMap<>();
    resultPrepareData.put("idNumber", prepareValue.substring(1));
    resultPrepareData.put("phone", "01" + prepareValue.substring(5));
    resultPrepareData.put("email", "huy.tq" + prepareValue.substring(5) + "@gmail.com");

    if (testCaseName.contains("ERROR phone")) {
      resultPrepareData.put("phone", phone);
    } else if (testCaseName.contains("ERROR email")) {
      resultPrepareData.put("email", email);
    } else if (testCaseName.contains("ERROR idNumber")) {
      resultPrepareData.put("idNumber", idNumber);
    } else if (testCaseName.contains("ERROR contactAddress")) {
      resultPrepareData.put("contactAddress", contactAddress);
    }

    body = fileTxtToString("src/test/resources/requestBody/RmOpenAccount.txt")
      .replaceAll("#fullName#", fullName).replaceAll("#birthday#", birthday).replaceAll("#idNumber#", resultPrepareData.get("idNumber"))
      .replaceAll("#email#", resultPrepareData.get("email")).replaceAll("#phone#", resultPrepareData.get("phone"))
      .replaceAll("#contactAddress#", contactAddress).replaceAll("#idDate#", idDate).replaceAll("#idPlace#", idPlace);
  }

  @Ignore
  @TestCase(name = "#testCaseName")
  @Title("Verify api rm open account")
  public void perfomTest() {
    RequestSpecification requestSpecification = given()
      .baseUri(TCBSPROFILE_RM_OPEN_ACCOUNT)
      .cookie("JSESSIONID=53DE4D703500CBB4AD5C0B08E2E32A66")
      .contentType("application/x-www-form-urlencoded; charset=UTF-8");
    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }
    assertThat(response.getStatusCode(), is(200));
    assertThat("verify message", response.jsonPath().get("msg"), is(errorMessage));

    if (errorMessage.equals("SUCCESS")) {
      assertThat("verify status", response.jsonPath().get("status"), is(0));
    } else {
      if (testCaseName.contains("existed in DB")) {
        assertThat("verify data", response.jsonPath().get("data.defaultMessage[0]"), is(data));
      } else {
        assertThat("verify data", response.jsonPath().get("data.code[0]"), is(data));
      }
    }
  }
}
