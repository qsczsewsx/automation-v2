package tasks.register;

import io.restassured.http.Header;
import io.restassured.response.Response;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.TCBSPROFILE_DOMAIN;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceKey.TCBS_PS_REGISTER_RESP;
import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class RegisterAPI implements Task {
  private final String method;
  private final String url;
  private final Header xApiKey;
  private final Header contentType;
  private final String body;

  public RegisterAPI(String method, String url, Header xApiKey, Header contentType, String body) {
    this.method = method;
    this.url = url;
    this.xApiKey = xApiKey;
    this.contentType = contentType;
    this.body = body;
  }

  public static RegisterAPI withInfo(String method, String url, Header xApiKey, Header contentType, String body) {
    return instrumented(RegisterAPI.class, method, url, xApiKey, contentType, body);
  }

  @Step("{0} register api")
  public <T extends Actor> void performAs(T user) {
    Response resp = given()
      .header(xApiKey)
      .header(contentType)
      .body(body)
      .request(method, TCBSPROFILE_DOMAIN + url);
    user.remember(TCBS_PS_REGISTER_RESP, resp);
  }
}
