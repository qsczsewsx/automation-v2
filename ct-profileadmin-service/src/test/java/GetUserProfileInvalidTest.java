import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.abilities.CallAllAPI;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import questions.TheUserProfile;
import tasks.GetUserProfileAPI;

import static com.tcbs.automation.publicmatcher.CombinableMatcher.all;
import static com.tcbs.automation.publicmatcher.HashMapMatcher.has;
import static java.net.HttpURLConnection.HTTP_OK;
import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom("data/UserProfileTest.csv")
public class GetUserProfileInvalidTest {
  private static Actor user = Actor.named("user");
  private String description;
  private String tcbsId;
  private String code;

  @Before
  public void setup() {
    givenThat(user).can(CallAllAPI.withProvidedInfo());
  }

  @Test
  @TestCase(name = "[Automation] #description")
  @Title("Get user profile - invalid TcbsId")
  @SuppressWarnings("unchecked")
  public void get_profile_invalid() {
    when(user).attemptsTo(GetUserProfileAPI.withTcbsId(tcbsId));
    then(user).should(seeThatResponse("Call get profile API successfully", response -> response.statusCode(HTTP_OK)));
    then(user).should(seeThat(
      String.format("Response code %s and corresponding error message", code),
      TheUserProfile.forTcbsUserfromAPI(),
      all(has("response.code", is(code)))
        .and(has("response.message", is("Không tìm thấy thông tin người dùng"))))
    );
  }
}

