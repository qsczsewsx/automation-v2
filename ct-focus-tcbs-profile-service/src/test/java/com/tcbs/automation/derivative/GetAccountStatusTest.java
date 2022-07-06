package com.tcbs.automation.derivative;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.STATUS_DERIVATIVE_ACCOUNT;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.TCBSPROFILE_INQUIRYGROUPINFOKEY;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/derivative/GetAccountStatus.csv", separator = '|')
public class GetAccountStatusTest {
  @Getter
  private String testCaseName;
  private String errorCode;
  private String tcbsId;
  private String errorMessage;


  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get account status")
  public void perfomTest() {
    Response response = given()
      .baseUri(STATUS_DERIVATIVE_ACCOUNT.replace("#tcbsId", tcbsId))
      .header("Authorization", TCBSPROFILE_INQUIRYGROUPINFOKEY)
      .get();

    assertThat("verify error code", response.jsonPath().get("code"), is(errorCode));
  }
}
