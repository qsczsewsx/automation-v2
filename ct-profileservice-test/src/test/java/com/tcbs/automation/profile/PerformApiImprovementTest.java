package com.tcbs.automation.profile;

import static com.tcbs.automation.config.profileservice.ProfileServiceConfig.TCI3PROFILE_CURRENT_DOMAIN;
import static com.tcbs.automation.config.profileservice.ProfileServiceConfig.TCI3PROFILE_DOMAIN;
import static net.serenitybdd.screenplay.GivenWhenThen.givenThat;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.GivenWhenThen.then;
import static net.serenitybdd.screenplay.GivenWhenThen.when;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.not;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.abilities.CallAllAPI;
import com.tcbs.automation.matchers.ListDtoActionMatcher;
import com.tcbs.automation.models.ProfileDto;
import com.tcbs.automation.questions.ApiImprovement;
import com.tcbs.automation.tasks.CallCurrentApiProfileService;
import com.tcbs.automation.tasks.CallFeatureApiProfileService;

import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom("data/PerformApiImprovementLogic.csv")
public class PerformApiImprovementTest {

  String testCase;

  String description;

  String subPath;

  String queryParams;

  Boolean ok;

  private Actor actor = Actor.named("Actor");

  private Map<String, String> params;

  public List<ProfileDto> expectedObjAction;

  @Before
  public void setUp() {
    givenThat(actor).can(CallAllAPI.withProvidedInfo());
    params = ApiImprovement.getQueryParams(queryParams);
    when(actor).attemptsTo(CallCurrentApiProfileService.performDynamicLogic(subPath, params));
    expectedObjAction = ApiImprovement.fromSearchAPIAsList(TCI3PROFILE_CURRENT_DOMAIN + subPath + params.toString())
        .answeredBy(actor);
  }

  @Test
  @TestCase(name = "#testCase Verify api improvement logic for profile #description")
  @Title("Verify api improvement logic for profile")
  public void verify_perform_dynamic_logic_for_obj_action() {
    when(actor).attemptsTo(CallFeatureApiProfileService.performDynamicLogic(subPath, params));

    if (ok) {
      then(actor)
          .should(seeThatResponse(response -> response.statusCode(HttpURLConnection.HTTP_OK)));
      then(actor).should(seeThat(ApiImprovement.fromSearchAPIAsList(TCI3PROFILE_DOMAIN + subPath + params.toString()),
          ListDtoActionMatcher.sameAs(false, expectedObjAction)));
    } else {
      then(actor)
          .should(seeThatResponse(response -> response.statusCode(not(HttpURLConnection.HTTP_OK))));
    }

  }
}
