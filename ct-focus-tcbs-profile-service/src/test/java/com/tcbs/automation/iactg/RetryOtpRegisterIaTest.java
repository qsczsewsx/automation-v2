package com.tcbs.automation.iactg;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import common.CommonUtils;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.RETRY_OTP_REGISTER_IA;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/iactg/RetryOtpRegisterIa.csv", separator = '|')
public class RetryOtpRegisterIaTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String tcbsId;
  private String token;

  @Before
  public void before() {
    token = CommonUtils.getToken("105C300126");
    tcbsId = syncData(tcbsId);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api retry otp register ia")
  public void verifyRetryOtpRegisterIaTest() {

    System.out.println("TestCaseName : " + testCaseName);

    Response response = given()
      .baseUri(RETRY_OTP_REGISTER_IA.replace("#tcbsId#", tcbsId))
      .header("Authorization", "Bearer " + token)
      .contentType("application/json")
      .post();

    assertThat(response.getStatusCode(), is(statusCode));

    if (statusCode == 403) {
      assertThat("verify response header", response.header("X-Author"), is("retry_otp_required"));
    } else {
      assertThat("verify error message", response.jsonPath().get("error"), is(errorMessage));
    }

  }
}