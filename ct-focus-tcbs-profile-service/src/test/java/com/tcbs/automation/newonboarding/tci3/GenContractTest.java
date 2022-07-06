package com.tcbs.automation.newonboarding.tci3;


import com.adaptavist.tm4j.junit.annotation.TestCase;
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

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GEN_CONTRACT_API;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/GenContract.csv", separator = '|')
public class GenContractTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String typeValue;
  private String accountLogin;
  private String tcbsid;
  private String errorMessage;
  private String token;

  @Before
  public void before() {
    Actor actor = Actor.named("tuanna2");
    LoginApi.withCredentials(accountLogin, "abc123").performAs(actor);
    token = TheUserInfo.aboutLoginData().answeredBy(actor).getToken();
    typeValue = syncData(typeValue);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api gen contract")
  public void verifyGenContractTest() {
    RequestSpecification requestSpecification = given()
      .baseUri(GEN_CONTRACT_API + tcbsid)
      .header("Authorization", "Bearer " + token);

    Response response;
    if (testCaseName.contains("missing type_value")) {
      response = requestSpecification.get();
      typeValue = "";
    } else {
      response = requestSpecification.param("type", typeValue).get();
    }

    assertThat("verify status code", response.getStatusCode(), is(statusCode));
    if (statusCode == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
    if (statusCode == 200) {
      if (typeValue.equals("0")) {
        assertThat("verify response with type 0", response.jsonPath().get(),
          allOf(hasKey("onboarding"), hasKey("private_account"), hasKey("derivative_account"))
        );
      }
      if (typeValue.equals("1")) {
        assertThat("verify response with type 1", response.jsonPath().get(),
          hasKey("onboarding")
        );
      }
      if (typeValue.equals("2")) {
        assertThat("verify response with type 2", response.jsonPath().get(),
          hasKey("private_account")
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
