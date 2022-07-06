package tasks;


import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import static com.tcbs.automation.config.profileservice.ProfileServiceConfig.*;
import static com.tcbs.automation.config.profileservice.ProfileServiceKey.PS_RETRIEVE_EMAIL_USING_FORGOT_PWD;
import com.tcbs.automation.login.TheUserInfo;
import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class RetrieveEmailUsingForgotPassword implements Task {
  private String apiBody;

  public RetrieveEmailUsingForgotPassword(String apiBody) {
    this.apiBody = apiBody;
  }

  public static RetrieveEmailUsingForgotPassword with(String email) {
    return instrumented(RetrieveEmailUsingForgotPassword.class, email);
  }

  @Override
  public <T extends Actor> void performAs(T user) {
    Response resp = given().baseUri(TCI3PROFILE_DOMAIN + TCI3PROFILE_API + TCI3PROFILE_PASSWORD + TCI3PROFILE_FORGOT + TCI3PROFILE_REGISTRY)
      .queryParam("channel", "email")
      .header("x-api-key", user.asksFor(TheUserInfo.aboutLoginData()).getToken())
      .contentType(ContentType.JSON)
      .body(apiBody)
      .post();

    user.remember(PS_RETRIEVE_EMAIL_USING_FORGOT_PWD, resp);
  }
}
