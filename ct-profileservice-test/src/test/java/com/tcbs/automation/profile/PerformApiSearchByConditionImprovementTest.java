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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.abilities.CallAllAPI;
import com.tcbs.automation.matchers.ListDtoActionMatcher;
import com.tcbs.automation.models.ProfileDto;
import com.tcbs.automation.questions.ApiImprovement;
import com.tcbs.automation.tasks.CallCurrentSearchByConditionApi;
import com.tcbs.automation.tasks.CallFeatureSearchByConditionApi;
import com.tcbs.automation.tools.ConvertUtils;

import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom("data/PerformApiSearchByConditionImprovementLogic.csv")
public class PerformApiSearchByConditionImprovementTest {

  String testCase;

  String description;

  String filePath;

  String testDataFile;

  String subPath;

  Boolean ok;

  private Actor actor = Actor.named("Actor");

  private String rootPath = System.getProperty("user.dir");

  private String requestBody;

  public List<ProfileDto> expectedObjAction;

  @Before
  public void setUp() {
    givenThat(actor).can(CallAllAPI.withProvidedInfo());
    requestBody = ConvertUtils.fileTxtToString(rootPath + filePath + testDataFile);
    when(actor).attemptsTo(CallCurrentSearchByConditionApi.performDynamicLogic(requestBody));
    expectedObjAction = ApiImprovement
        .fromSearchAPIAsListFromPage(TCI3PROFILE_CURRENT_DOMAIN + subPath + requestBody.trim()).answeredBy(actor);
  }

  @Test
  @TestCase(name = "#testCase Verify api search by condition improvement logic for profile #description")
  @Title("Verify api search by condition improvement logic for profile")
  public void verify_perform_dynamic_logic_for_obj_action() {
    when(actor).attemptsTo(CallFeatureSearchByConditionApi.performDynamicLogic(requestBody));

    if (ok) {
      then(actor)
          .should(seeThatResponse(response -> response.statusCode(HttpURLConnection.HTTP_OK)));
      then(actor)
          .should(seeThat(ApiImprovement.fromSearchAPIAsListFromPage(TCI3PROFILE_DOMAIN + subPath + requestBody.trim()),
              ListDtoActionMatcher.sameAs(true, expectedObjAction)));
    } else {
      then(actor)
          .should(seeThatResponse(response -> response.statusCode(not(HttpURLConnection.HTTP_OK))));
    }

  }
}
