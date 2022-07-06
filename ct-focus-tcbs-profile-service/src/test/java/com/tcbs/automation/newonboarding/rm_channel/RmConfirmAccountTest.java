package com.tcbs.automation.newonboarding.rm_channel;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsUserOpenaccountQueue;
import common.CallApiUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.thucydides.core.annotations.Title;
import org.junit.Before;
import org.junit.Ignore;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.TCBSPROFILE_RM_CONFIRM_ACCOUNT;
import static common.EncodeDataUtils.encodeValue;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

//@RunWith(SerenityParameterizedRunner.class)
//@UseTestDataFrom(value = "data/newonboarding/RmConfirmAccount.csv", separator = '|')
public class RmConfirmAccountTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private int status;
  private String errorMessage;
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
  private String referenceId;

  @Before
  public void setup() throws UnsupportedEncodingException {
    if (testCaseName.contains("valid request")) {
      fullName = encodeValue(fullName);
      birthday = encodeValue(birthday);
      contactAddress = encodeValue(contactAddress);
      idDate = encodeValue(idDate);
      idPlace = encodeValue(idPlace);

      String prepareValue = String.valueOf(new Date().getTime());
      idNumber = prepareValue.substring(1);
      phone = "01" + prepareValue.substring(5);
      email = "hung.dao" + prepareValue.substring(5) + "@gmail.com";

      CallApiUtils.callRmOpenAccountApi(fullName, birthday, idNumber, email, phone, contactAddress, idDate, idPlace);
    }
  }

  @Ignore
  @TestCase(name = "#testCaseName")
  @Title("Verify api rm confirm account")
  public void perfomTest() {

    RequestSpecification requestSpecification = given()
      .baseUri(TCBSPROFILE_RM_CONFIRM_ACCOUNT)
      .cookie("JSESSIONID=F19338AC7B88EB8FA1E685D6BF0D9B6A;crowd.token_key=UxiqEJ_TDxQbRKOXHNqicAAAAAAALwABdGh1eXRt")
      .contentType("application/x-www-form-urlencoded; charset=UTF-8");
    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      if (testCaseName.contains("valid request")) {
        body = "referenceid=" + TcbsUserOpenaccountQueue.getByPhone(phone).getReferenceid();
      } else {
        body = "referenceid=" + referenceId;
      }
      response = requestSpecification.body(body).post();
    }

    assertThat(response.getStatusCode(), is(200));
    assertThat("verify status", response.jsonPath().get("status"), is(status));
    assertThat("verify msg", response.jsonPath().get("msg"), is(errorMessage));

  }
}
