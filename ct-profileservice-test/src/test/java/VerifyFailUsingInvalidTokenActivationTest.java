import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.abilities.CallAllAPI;
import tasks.TokenActivation;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static com.tcbs.automation.config.profileservice.ProfileServiceKey.*;
import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SerenityRunner.class)
public class VerifyFailUsingInvalidTokenActivationTest {
  private static Actor user = Actor.named("user");
  private HashMap apiBody = new HashMap();
  private Gson gson = new Gson();

  @Before
  public void setUp() {
    givenThat(user).can(CallAllAPI.withProvidedInfo());
    apiBody.put("token", PS_INPUT_INVALID_TOKEN);
  }

  @Test
  @TestCase(name = "Verify fail case when using invalid token to activate")
  public void verify_fail_using_invalid_token_activation() {
    when(user).attemptsTo(TokenActivation.withInvalidToken(gson.toJson(apiBody)));

    then(user).should(seeThatResponse(
      response -> response.statusCode(400)
        .body("status", equalTo(400))
        .body("code", equalTo(PS_INVALID_TOKEN_CODE_RESP))
        .body("message", equalTo(PS_INVALID_TOKEN_MESSAGE_RESP))
    ));
  }
}
