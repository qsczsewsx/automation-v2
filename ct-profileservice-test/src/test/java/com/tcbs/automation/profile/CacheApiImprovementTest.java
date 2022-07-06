package com.tcbs.automation.profile;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.abilities.CallAllAPI;
import com.tcbs.automation.cas.CAS;
import com.tcbs.automation.questions.CallRedisQuestion;
import com.tcbs.automation.tasks.CallFeatureProfileByIdentityApi;
import com.tcbs.automation.tasks.CallFeatureProfileClearCacheApi;
import com.tcbs.automation.tasks.CallRedisKeyRemoval;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.hamcrest.Matchers;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.HttpURLConnection;

import static com.tcbs.automation.cas.CAS.createNativeQuery;
import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom("data/CacheProfileByTcbsIdOr105C.csv")
public class CacheApiImprovementTest {

  String description;

  String identity;

  private Actor actor = Actor.named("Actor");

  @Before
  public void setUp() {
    givenThat(actor).can(CallAllAPI.withProvidedInfo());
  }

  @Test
  @TestCase(name = "Verify cache created when retrieve profile #description")
  @Title("Verify cache created when retrieve profile")
  public void verify_cache_created_when_retrieve_profile() {
    givenThat(actor).attemptsTo(CallRedisKeyRemoval.with(toRedisCacheKey(identity)));
    when(actor).attemptsTo(CallFeatureProfileByIdentityApi.with(identity));

    then(actor).should(
      seeThatResponse(response -> response.statusCode(HttpURLConnection.HTTP_OK)));

    then(actor).should(seeThat(CallRedisQuestion.keyExists(toRedisCacheKey(identity)), Matchers.is(true)));
  }

  @Test
  @TestCase(name = "Verify api clear cache #description")
  @Title("Verify cache cleared api")
  public void verify_cache_improvement_for_profile() {
    when(actor).attemptsTo(CallFeatureProfileByIdentityApi.with(identity));
    when(actor).attemptsTo(CallFeatureProfileClearCacheApi.with(identity));

    then(actor).should(
      seeThatResponse(response -> response.statusCode(HttpURLConnection.HTTP_NO_CONTENT)));

    then(actor).should(seeThat(CallRedisQuestion.keyExists(toRedisCacheKey(identity)), Matchers.is(false)));
  }

  @Test
  @TestCase(name = "Verify cache hit when retrieve profile #description")
  @Title("Verify cache hit when retrieve profile")
  public void verify_cache_hit_when_retrieve_profile() {
    givenThat(actor).attemptsTo(CallRedisKeyRemoval.with(toRedisCacheKey(identity)));
    when(actor).attemptsTo(CallFeatureProfileByIdentityApi.with(identity));

    String newName = "TCBS" + System.currentTimeMillis();
    updateProfileFirstName(newName, identity);

    when(actor).attemptsTo(CallFeatureProfileByIdentityApi.with(identity)); // call again
    then(actor).should(
        seeThatResponse(response -> response.statusCode(HttpURLConnection.HTTP_OK)
        .body("personalInfo.firstName", Matchers.is(Matchers.not(newName)))) );
  }

  @Test
  @TestCase(name = "Verify cache update when update profile #description")
  @Title("Verify cache update when update profile")
  public void verify_cache_update_after_update_profile() {
    when(actor).attemptsTo(CallFeatureProfileByIdentityApi.with(identity));

    String newName = "TCBS" + System.currentTimeMillis();
    updateProfileFirstName(newName, identity);
    when(actor).attemptsTo(CallFeatureProfileClearCacheApi.with(identity));

    when(actor).attemptsTo(CallFeatureProfileByIdentityApi.with(identity)); // call again
    then(actor).should(
        seeThatResponse(response -> response.statusCode(HttpURLConnection.HTTP_OK)
        .body("personalInfo.firstName", Matchers.is(newName))) );
  }

  private void updateProfileFirstName(String name, String identity) {
    Transaction tnx = CAS.casConnection.getSession().beginTransaction();
    Query query = createNativeQuery("UPDATE TCBS_USER SET firstname = :firstname WHERE "
        + (CallFeatureProfileByIdentityApi.is105C(identity) ? "username = :identity" : "tcbsid = :identity"));
    query.setParameter("firstname", name);
    query.setParameter("identity", identity);
    query.executeUpdate();
    tnx.commit();
  }

  @NotNull
  private String toRedisCacheKey(String identity) {
    return (CallFeatureProfileByIdentityApi.is105C(identity) ? "UserProfileByUserName:" : "UserProfileByTcbsId:") + identity;
  }
}
