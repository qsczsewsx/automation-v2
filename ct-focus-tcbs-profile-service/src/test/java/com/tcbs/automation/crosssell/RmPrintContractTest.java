package com.tcbs.automation.crosssell;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.RM_PRINT_CONTRACT;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/crosssell/RmPrintContract.csv", separator = '|')
public class RmPrintContractTest {
  @Getter
  private String testCaseName;
  private int status;
  private String errorMessage;
  private String referenceId;
  private String body;

  @Before
  public void setup() {
    referenceId = syncData(referenceId);
    body = "referenceid=" + referenceId;
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api rm print contract")
  public void perfomTest() {
    RequestSpecification requestSpecification = given()
      .baseUri(RM_PRINT_CONTRACT)
      .header("Cookie", "JSESSIONID=ED08A8AE3000959BB2FB4D21E5FC291A; crowd.token_key=ye60Po_e6ZwSgUDOQcnhmgAAAAAALwABaGFpaHY=")
      .contentType("application/x-www-form-urlencoded; charset=UTF-8");
    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }
    assertThat("verify status", response.getStatusCode(), is(200));
    assertThat(response.jsonPath().get("msg"), is(errorMessage));
    assertThat(response.jsonPath().get("status"), is(status));
  }
}
