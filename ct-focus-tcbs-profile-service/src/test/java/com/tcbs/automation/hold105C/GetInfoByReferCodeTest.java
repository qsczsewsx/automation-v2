package com.tcbs.automation.hold105C;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsFancy105C;
import com.tcbs.automation.cas.TcbsUser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GET_INFO_REFER_CODE;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/hold105C/GetInfoReferCode.csv", separator = '|')
public class GetInfoByReferCodeTest {

  @Getter
  private String testCaseName;
  private int statusCode;
  private String referCode;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get info refer code")
  public void verifyGetInfoReferCodeTest() {

    System.out.println("TestCaseName : " + testCaseName);

    referCode = syncData(referCode);

    RequestSpecification requestSpecification = SerenityRest.given()
      .baseUri(GET_INFO_REFER_CODE);

    Response response;

    if (testCaseName.contains("case missing referCode param")) {
      response = requestSpecification.get();
    } else {
      response = requestSpecification.param("referCode", referCode).get();
    }

    assertThat(response.getStatusCode(), is(200));

    if (testCaseName.contains("case referCode is valid")) {
      TcbsFancy105C tcbsFancy105C = TcbsFancy105C.getByReferCode(referCode);
      TcbsUser tcbsUser = TcbsUser.getByPhoneNumber(tcbsFancy105C.getOwnerBy());
      assertThat("verify hold105C", response.jsonPath().get("code105C"), is(tcbsFancy105C.getCode105C()));
      assertThat("verify status", response.jsonPath().get("ownerBy"), is(tcbsUser.getLastname() + " " + tcbsUser.getFirstname()));
    }

  }
}