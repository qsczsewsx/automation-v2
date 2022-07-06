package com.tcbs.automation.tasks;

import static com.tcbs.automation.config.profileservice.ProfileServiceConfig.TCI3PROFILE_API_KEY;
import static com.tcbs.automation.config.profileservice.ProfileServiceConfig.TCI3PROFILE_DOMAIN;
import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.screenplay.Tasks.instrumented;

import java.util.Map;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

public class CallFeatureApiProfileService implements Task {

  private String subPath;

  private Map<String, String> queryParams;

  public CallFeatureApiProfileService(String subPath, Map<String, String> queryParams) {
    this.subPath = subPath;
    this.queryParams = queryParams;
  }

  public static CallFeatureApiProfileService performDynamicLogic(String subPath,
      Map<String, String> queryParams) {
    return instrumented(CallFeatureApiProfileService.class, subPath, queryParams);
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    String baseUri = TCI3PROFILE_DOMAIN + subPath;
    RequestSpecification spec = given().baseUri(baseUri).header("Content-Type", "application/json")
        .header("X-Api-Key", TCI3PROFILE_API_KEY)
        .header("Authorization", "Bearer " + TCI3PROFILE_API_KEY);
    if (null != queryParams && !queryParams.isEmpty())
      spec = spec.queryParams(queryParams);
    Response response = spec.get();
    actor.remember(
        null != queryParams && !queryParams.isEmpty() ? baseUri + queryParams.toString() : baseUri,
        response);
  }
}
