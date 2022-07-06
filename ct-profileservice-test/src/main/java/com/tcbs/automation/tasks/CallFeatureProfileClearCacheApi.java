package com.tcbs.automation.tasks;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import static com.tcbs.automation.config.common.CommonConfig.QE_X_API_KEY;
import static com.tcbs.automation.config.profileservice.ProfileServiceConfig.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class CallFeatureProfileClearCacheApi implements Task {

  private String identity;

  public CallFeatureProfileClearCacheApi(String identity) {
    this.identity = identity;
  }

  public static CallFeatureProfileClearCacheApi with(String identity) {
    return instrumented(CallFeatureProfileClearCacheApi.class, identity);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    String baseUri = TCI3PROFILE_DOMAIN + TCI3PROFILE_API + TCI3PROFILE_CACHE;
    given().baseUri(baseUri).header("Content-Type", "application/json")
      .queryParam(is105C(identity) ? "username": "tcbsId", identity)
      .header("X-Api-Key", QE_X_API_KEY).delete();
  }

  public static boolean is105C(String id) {
    return id.startsWith("105C");
  }
}
