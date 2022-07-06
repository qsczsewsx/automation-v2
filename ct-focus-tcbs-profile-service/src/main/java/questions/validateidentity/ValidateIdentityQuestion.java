package questions.validateidentity;

import io.restassured.response.Response;
import net.serenitybdd.screenplay.Question;

import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceKey.TCBS_PS_OLD_REGISTER_RESP;

public class ValidateIdentityQuestion {

  public static Question<HashMap<String, Object>> fromApi() {
    return Question.about("Get all response after update old registered customer")
      .answeredBy(actor -> {
        HashMap<String, Object> output;
        Response resp = actor.recall(TCBS_PS_OLD_REGISTER_RESP);
        output = resp.jsonPath().get();
        return output;
      });
  }
}