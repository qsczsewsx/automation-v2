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

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.publicmatcher.CombinableMatcher.all;
import static com.tcbs.automation.publicmatcher.HashMapMatcher.has;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static java.net.HttpURLConnection.HTTP_BAD_METHOD;
import static net.serenitybdd.screenplay.GivenWhenThen.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/register/InvalidMethod.csv", separator = '|')
public class RegisterInvalidMethodTest {
  private Actor user;
  private String description;
  private String method;
  private String url;
  private String body;
  private Header xApiKey;
  private Header contentType;

  @Before
  public void before() throws Exception {
    user = Actor.named("Focus Tester");
    url = TCBSPROFILE_API + TCBSPROFILE_REGISTER;
    xApiKey = new Header("x-api-key", API_KEY);
    contentType = new Header("Content-Type", "application/json");
    body = fileTxtToString("src/test/resources/requestBody/RegisterBody.json");
  }

  @Test
  @TestCase(name = "#description")
  public void verifyApiRegisterWithInvalidMethod() {
    when(user).attemptsTo(RegisterAPI.withInfo(method, url, xApiKey, contentType, body));
    then(user).should(seeThat(
      RegisterQuestion.fromApi(),
      all(has("status", Matchers.is(HTTP_BAD_METHOD))))
    );
  }
}

