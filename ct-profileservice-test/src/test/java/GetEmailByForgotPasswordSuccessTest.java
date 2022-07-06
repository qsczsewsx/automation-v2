import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.abilities.CallAllAPI;
import com.tcbs.automation.functions.PublicConstant;
import com.tcbs.automation.login.LoginApi;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import tasks.RetrieveEmailUsingForgotPassword;
import java.util.HashMap;
import static com.tcbs.automation.abilities.GivenWhenThen.when;
import static com.tcbs.automation.config.profileservice.ProfileServiceKey.PS_USER_105C;
import static java.net.HttpURLConnection.HTTP_OK;
import static net.serenitybdd.screenplay.GivenWhenThen.givenThat;
import static net.serenitybdd.screenplay.GivenWhenThen.then;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom("data/RetrieveEmailByForgotPasswordSuccess.csv")
public class GetEmailByForgotPasswordSuccessTest {
  private static Actor user = Actor.named("user");
  private String description;
  private String code105C;
  private String email;
  private String birthday;
  private HashMap apiBody = new HashMap();
  private Gson gson = new Gson();

  @BeforeClass
  public static void setUp() {
    givenThat(user).can(CallAllAPI.withProvidedInfo());
    givenThat(user).attemptsTo(LoginApi.withCredentials(PS_USER_105C, PublicConstant.DEFAULT_PASSWORD));
  }

  @Before
  public void before() {
    apiBody.put("email", email);
    apiBody.put("code105C", code105C);
    apiBody.put("birthday", birthday);
  }

  @Test
  @TestCase(name = "Success Case - #description")
  public void get_email_by_forgot_password_success() {
    when(user).attemptsTo(RetrieveEmailUsingForgotPassword.with(gson.toJson(apiBody)));

    then(user).should(seeThatResponse(
      response -> response.statusCode(HTTP_OK)
        .body("transactionId", notNullValue())
    ));
  }
}
