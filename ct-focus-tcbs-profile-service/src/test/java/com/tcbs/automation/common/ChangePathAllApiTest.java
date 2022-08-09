package com.tcbs.automation.common;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.config.Env;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.API_KEY;
import static common.ProfileTools.TOKEN;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/common/VerifyChangePathData.csv", separator = '|')
public class ChangePathAllApiTest {
  public String method;
  public String baseUrl;

  @Test
  @TestCase(name = "Verify change path api #baseUrl")
  public void verifyAllApiChangePath() {
    System.out.println(method + " yolo " + baseUrl);
    if (Env.get().getName().equalsIgnoreCase("uat")) {
      baseUrl = baseUrl.replace("sit", "uat");
    }
    Response response = null;
    RequestSpecification req = given()
      .baseUri(baseUrl.replace("{param}", "abc"));

    if (baseUrl.contains("apiint")) {
      req.header("x-api-key", API_KEY);
    } else if (baseUrl.contains("apiext")) {
      req.header("Authorization", "Bearer " + TOKEN);
    }

    switch (method) {
      case "GET":
        response = req.get();
        break;
      case "DELETE":
        response = req.delete();
        break;
      case "POST":
        response = req
          .contentType("application/json")
          .body("{}")
          .post();
        break;
      case "PUT":
        response = req
          .contentType("application/json")
          .body("{}")
          .put();
        break;
      default:
        System.out.println(method + " not support ");
        break;
    }

    assert response != null;
    assertThat("Verify status code", response.getStatusCode(), anyOf(is(400), is(200), is(204), is(401), is(403), is(500)));
    if (response.getStatusCode() == 500) {
      assertThat("verify error code", response.jsonPath().getMap("").get("code").toString(), startsWith("104"));
    }
  }
}