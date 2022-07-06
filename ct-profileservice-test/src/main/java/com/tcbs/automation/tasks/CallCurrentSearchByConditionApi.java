package com.tcbs.automation.tasks;

import static com.tcbs.automation.config.profileservice.ProfileServiceConfig.TCI3PROFILE_API_KEY;
import static com.tcbs.automation.config.profileservice.ProfileServiceConfig.TCI3PROFILE_CURRENT_DOMAIN;
import static com.tcbs.automation.config.profileservice.ProfileServiceConfig.TCI3PROFILE_API;
import static com.tcbs.automation.config.profileservice.ProfileServiceConfig.TCI3PROFILE_SEARCH_BY_CONDITION;
import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.screenplay.Tasks.instrumented;

import io.restassured.response.Response;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

public class CallCurrentSearchByConditionApi implements Task {

  private String requestBody;

  public CallCurrentSearchByConditionApi(String requestBody) {
    this.requestBody = requestBody;
  }

  public static CallCurrentSearchByConditionApi performDynamicLogic(String requestBody) {
    return instrumented(CallCurrentSearchByConditionApi.class, requestBody);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    String baseUri = TCI3PROFILE_CURRENT_DOMAIN + TCI3PROFILE_API
      + TCI3PROFILE_SEARCH_BY_CONDITION;
    Response response = given().baseUri(baseUri).header("Content-Type", "application/json")
      .header("X-Api-Key", TCI3PROFILE_API_KEY).body(requestBody).post();
    actor.remember(baseUri + requestBody.trim(), response);
  }
}
