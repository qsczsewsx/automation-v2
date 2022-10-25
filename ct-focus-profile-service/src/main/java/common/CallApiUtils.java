package common;

import io.restassured.response.Response;

import java.util.HashMap;

import static com.automation.config.xxxxprofileservice.xxxxProfileServiceConfig.*;
import static com.automation.tools.ConvertUtils.fileTxtToString;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class CallApiUtils {
  private static final String APPLICATION_JSON = "application/json";
  private static final String X_API_KEY = "x-api-key";


  public static Response callGenAuthenKeyApi() {
    String body = fileTxtToString("src/test/resources/requestBody/GenAuthenKey.json");
    Response response = given()
      .baseUri(GEN_AUTHEN_KEY)
      .contentType(APPLICATION_JSON)
      .header(X_API_KEY, API_KEY)
      .body(body)
      .post();
    assertThat(response.statusCode(), is(200));
    return response;
  }

  public static Response callLoginApi(String loginKey) {
    HashMap<String, Object> body = new HashMap<>();
    body.put("login_key", loginKey);
    Response response = given()
      .baseUri(LOGIN_TO_TCI3)
      .contentType(APPLICATION_JSON)
      .body(body)
      .post();
    assertThat(response.statusCode(), is(200));
    return response;
  }
}
