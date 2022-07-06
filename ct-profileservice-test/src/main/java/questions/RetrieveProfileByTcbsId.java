package questions;

import io.restassured.http.ContentType;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.rest.questions.RestQuestionBuilder;

import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.profileservice.ProfileServiceConfig.TCI3PROFILE_DOMAIN;
import static com.tcbs.automation.config.profileservice.ProfileServiceConfig.TCI3PROFILE_API;

public class RetrieveProfileByTcbsId {

  public static Question<List<HashMap<String, Object>>> fromApiReturnBookInfo(String tcbsId, String token) {
    return new RestQuestionBuilder<List<HashMap<String, Object>>>().about("personal info from api")
      .to(TCI3PROFILE_DOMAIN + TCI3PROFILE_API + "/" + tcbsId)
      .with(request -> request.contentType(ContentType.JSON).header("x-api-key", token))
      .returning(resp -> resp.jsonPath().getList("books"));
  }
}
