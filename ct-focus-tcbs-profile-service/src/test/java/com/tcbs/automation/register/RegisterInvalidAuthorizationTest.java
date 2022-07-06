package com.tcbs.automation.register;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.http.Header;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import questions.register.RegisterQuestion;
import tasks.register.RegisterAPI;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.TCBSPROFILE_API;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.TCBSPROFILE_REGISTER;
import static com.tcbs.automation.publicmatcher.CombinableMatcher.all;
import static com.tcbs.automation.publicmatcher.HashMapMatcher.has;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static java.net.HttpURLConnection.HTTP_UNAUTHORIZED;
import static net.serenitybdd.screenplay.GivenWhenThen.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/register/InvalidAuthorization.csv", separator = '|')
public class RegisterInvalidAuthorizationTest {
  private Actor user;
  private String description;
  private String headerKey;
  private String headerValue;
  private String method;
  private String url;
  private String body;
  private Header contentType;
  private Header xApiKey;

  @Before
  public void before() throws Exception {
    user = Actor.named("Focus Tester");
    url = TCBSPROFILE_API + TCBSPROFILE_REGISTER;
    method = "POST";
    xApiKey = new Header(headerKey, headerValue);
    contentType = new Header("Content-Type", "application/json");
    body = fileTxtToString("src/test/resources/requestBody/RegisterBody.json");
  }

  @Test
  @TestCase(name = "#description")
  public void verifyApiRegisterWithInvalidAuthorization() {
    when(user).attemptsTo(RegisterAPI.withInfo(method, url, xApiKey, contentType, body));
    then(user).should(seeThat(
      RegisterQuestion.fromApi(),
      all(has("status", Matchers.is(HTTP_UNAUTHORIZED))))
    );
  }
}

