package com.tcbs.automation.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import static com.tcbs.automation.config.profileservice.ProfileServiceConfig.*;
import static com.tcbs.automation.config.common.CommonConfig.QE_X_API_KEY;
import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class CallFeatureProfileByIdentityApi implements Task {

  private String identity;

  public CallFeatureProfileByIdentityApi(String identity) {
    this.identity = identity;
  }

  public static CallFeatureProfileByIdentityApi with(String identity) {
    return instrumented(CallFeatureProfileByIdentityApi.class, identity);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    String baseUri = TCI3PROFILE_DOMAIN + TCI3PROFILE_API + (is105C(identity) ? TCI3PROFILE_PROFILE_BY_USERNAME : "") + "/" + identity;
    given().baseUri(baseUri).header("Content-Type", "application/json")
      .header("X-Api-Key", QE_X_API_KEY).get();
  }

  public static boolean is105C(String id) {
    return id.startsWith("105C");
  }
}
