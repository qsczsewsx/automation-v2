package com.tcbs.automation.newonboarding.tci3;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GEN_FINAL_CONTRACT;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/GenFinalContract.csv", separator = '|')
public class GenFinalContractTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String typeValue;
  private String accountLogin;
  private String tcbsId;
  private String errorMessage;
  private String token;
  private HashMap<String, Object> body;

  @Before
  public void before() {
    Actor actor = Actor.named("haihv");
    LoginApi.withCredentials(accountLogin, "abc123").performAs(actor);
    token = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();
    typeValue = syncData(typeValue);

    body = new HashMap<>();

    List<String> listValues;
    if (testCaseName.contains("list value of param type is empty") || testCaseName.contains("missing BODY")) {
      listValues = new ArrayList<>();
    } else if (testCaseName.contains("list tcbsId contain empty value")) {
      listValues = new ArrayList<>(Arrays.asList(""));
    } else {
      listValues = new ArrayList<>(Arrays.asList(typeValue.split(",")));
    }

    if (testCaseName.contains("value of param type is not a list")) {
      body.put("types", typeValue);
    } else {
      body.put("types", listValues);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api gen final contract")
  public void verifyGenFinalContractTest() {
    RequestSpecification requestSpecification = given()
      .baseUri(GEN_FINAL_CONTRACT.replace("#tcbsId#", tcbsId))
      .contentType("application/json")
      .header("Authorization", "Bearer " + token);

    Response response;
    Gson gson = new Gson();
    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
      typeValue = "";
    } else {
      response = requestSpecification.body(gson.toJson(body)).post();
    }

    assertThat("verify status code", response.getStatusCode(), is(statusCode));
    if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
    if (statusCode == 200) {
      if (typeValue.equals("1,3")) {
        assertThat("verify response with type 0", response.jsonPath().get(),
          allOf(hasKey("onboarding"), hasKey("derivative_account"))
        );
      }
      if (typeValue.equals("1")) {
        assertThat("verify response with type 1", response.jsonPath().get(),
          hasKey("onboarding")
        );
      }
      if (typeValue.equals("3")) {
        assertThat("verify response with type 3", response.jsonPath().get(),
          hasKey("derivative_account")
        );
      }
    }
  }
}
