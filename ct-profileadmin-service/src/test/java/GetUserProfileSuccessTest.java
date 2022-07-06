import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.abilities.CallAllAPI;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import matchers.UserProfileMatcher;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import questions.TheUserProfile;
import tasks.GetUserProfileAPI;

import static com.tcbs.automation.config.profileadmin.ProfileAdminKey.*;
import static com.tcbs.automation.functions.PublicConstant.DEFAULT_PASSWORD;
import static java.net.HttpURLConnection.HTTP_OK;
import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;

@RunWith(SerenityRunner.class)
public class GetUserProfileSuccessTest {
  private static Actor user = Actor.named("user");
  private String tcbsId;
  private String userId;

  @Before
  public void setup() {
    givenThat(user).can(CallAllAPI.withProvidedInfo());
    givenThat(user).attemptsTo(LoginApi.withCredentials(PA_USER_105C, DEFAULT_PASSWORD));
  }

  @Test
  @TestCase(name = "[Automation] Verify user gets profile info successfully")
  @Title("Get user profile successfully")
  public void get_profile_ok() {
    tcbsId = user.asksFor(TheUserInfo.aboutLoginData()).getTcbsid();
    when(user).attemptsTo(GetUserProfileAPI.withTcbsId(tcbsId));
    then(user).should(seeThatResponse("Call API Success", response -> response.statusCode(HTTP_OK)));
    userId = user.asksFor(TheUserProfile.forTcbsUserfromDB(tcbsId)).getId().toString();
    then(user).should(seeThat(
      "User's profile info from API should be the same as in DB",
      TheUserProfile.forTcbsUserfromAPI(),
      UserProfileMatcher.sameAs(user.asksFor(TheUserProfile.forTcbsUserfromDB(tcbsId)),
        user.asksFor(TheUserProfile.forAddressfromDB(userId)),
        user.asksFor(TheUserProfile.forAllAddressfromDB(userId)),
        user.asksFor(TheUserProfile.forUserAccountfromDB(userId)),
        user.asksFor(TheUserProfile.forApplicationUserflexfromDB(userId, PA_FLEX_ID)),
        user.asksFor(TheUserProfile.forApplicationUserflexfromDB(userId, PA_FUND_ID)),
        user.asksFor(TheUserProfile.forIdentificationfromDB(userId)),
        user.asksFor(TheUserProfile.forUserInstrumentfromDB(userId))))
    );
  }
}
