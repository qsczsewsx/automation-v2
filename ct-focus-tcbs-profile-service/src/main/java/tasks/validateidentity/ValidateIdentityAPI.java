package tasks.validateidentity;

import com.tcbs.automation.login.LoginData;
import com.tcbs.automation.login.TheUserInfo;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.TCBSPROFILE_DOMAIN;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.TCBSPROFILE_OLD_REGISTER;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceKey.TCBS_PS_OLD_REGISTER_RESP;
import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class ValidateIdentityAPI implements Task {

  private final String body;
  private final String tcbsId;


  public ValidateIdentityAPI(String body, String tcbsId) {
    this.body = body;
    this.tcbsId = tcbsId;
  }

  public static ValidateIdentityAPI withInfo(String body, String tcbsId) {
    return instrumented(ValidateIdentityAPI.class, body, tcbsId);
  }

  @Override
  public <T extends Actor> void performAs(T user) {
    LoginData userInfo = user.asksFor(TheUserInfo.aboutLoginData());
    Response resp = given()
      .baseUri(TCBSPROFILE_DOMAIN + TCBSPROFILE_OLD_REGISTER + tcbsId)
      .header("Authorization", "Bearer " + userInfo.getToken())
      .contentType(ContentType.JSON)
      .body(body)
      .put();

    user.remember(TCBS_PS_OLD_REGISTER_RESP, resp);
  }
}