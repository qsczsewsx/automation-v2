package tasks;

import io.restassured.response.Response;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import static com.tcbs.automation.config.profileservice.ProfileServiceConfig.*;
import static com.tcbs.automation.config.profileservice.ProfileServiceKey.PS_ACTIVATE_INVALID_TOKEN;
import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class TokenActivation implements Task {
  private String apiBody;

  public TokenActivation(String apiBody) {
    this.apiBody = apiBody;
  }

  public static TokenActivation withInvalidToken(String body) {
    return instrumented(TokenActivation.class, body);
  }

  @Override
  public <T extends Actor> void performAs(T user) {
    Response resp = given().baseUri(TCI3PROFILE_DOMAIN + TCI3PROFILE_API + TCI3PROFILE_TOKEN + TCI3PROFILE_VERIFY)
      .header("Content-Type", "application/json")
      .body(apiBody)
      .post();

    user.remember(PS_ACTIVATE_INVALID_TOKEN, resp);
  }
}
