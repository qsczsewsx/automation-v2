package com.tcbs.automation.multiIA;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import common.CommonUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.MULTIIA_USERNAME_X_API_KEY;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.MULTI_GET_BY_USERNAME;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/multiIA/MultiGetByUsernameTest.csv", separator = '|')
public class MultiGetByUsernameTest {

  @Getter
  private String testCaseName;
  @Getter
  private String value;
  private String condition;
  private int statusCode;
  private String errorMessage;

  @TestCase(name = "#testCaseName")
  @Test
  @Title("Verify get profile by username")
  @Step("Verify get profile by key and value")
  public void performTest() {
    RequestSpecification requestSpecification = SerenityRest.given()
      .baseUri(MULTI_GET_BY_USERNAME.replace("{value}", value))
      .header("x-api-key", MULTIIA_USERNAME_X_API_KEY);

    condition = syncData(condition);
    if (StringUtils.isEmpty(condition)) {
      requestSpecification.param("fields", condition);
      condition = "basicInfo,personalInfo";
    } else {
      requestSpecification.param("fields", condition);
    }
    Response response = requestSpecification
      .when()
      .get();
    assertThat(response.statusCode(), is(statusCode));

    if (statusCode == 200) {
      CommonUtils.verifyGetMultiIA(response, condition);
    } else if (statusCode == 400) {
      Assert.assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}




