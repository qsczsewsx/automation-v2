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

import java.util.Map;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.RM_DOWNLOAD_SINGED_CONTRACT;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/crosssell/RmDownloadSignedContract.csv", separator = '|')
public class RmDownloadSignedContractTest {
  private final String str = "contentUrl,name,size";
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
  @Title("Verify api rm download signed contract")
  public void perfomTest() {
    RequestSpecification requestSpecification = given()
      .baseUri(RM_DOWNLOAD_SINGED_CONTRACT)
      .header("Cookie", "JSESSIONID=ED08A8AE3000959BB2FB4D21E5FC291A; crowd.token_key=ye60Po_e6ZwSgUDOQcnhmgAAAAAALwABaGFpaHY=")
      .contentType("application/x-www-form-urlencoded; charset=UTF-8");
    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    if (testCaseName.contains("case valid request")) {
      Map<String, Object> data = response.jsonPath().get();
      for (String item : str.split(",", -1)) {
        assertThat(data, hasKey(item));
      }
    } else {
      assertThat(response.jsonPath().get("msg"), is(errorMessage));
      assertThat(response.jsonPath().get("status"), is(status));
    }
  }
}
