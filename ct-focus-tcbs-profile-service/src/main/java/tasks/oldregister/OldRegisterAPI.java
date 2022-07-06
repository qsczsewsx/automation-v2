package tasks.oldregister;

import com.tcbs.automation.login.LoginData;
import com.tcbs.automation.login.TheUserInfo;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.TCBSPROFILE_DOMAIN;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.TCBSPROFILE_OLD_REGISTER;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceKey.TCBS_PS_OLD_REGISTER_RESP;
import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class OldRegisterAPI implements Task {


  private final String tcbsId;
  private final String inputBody;

  public OldRegisterAPI(String tcbsId, String invalidBody) {
    this.tcbsId = tcbsId;
    this.inputBody = invalidBody;
  }

  public static OldRegisterAPI withInfo(String tcbsId, String inputBody) {
    return instrumented(OldRegisterAPI.class, tcbsId, inputBody);
  }

  @Step("{0} update old registered customer api")
  public <T extends Actor> void performAs(T user) {
    LoginData userInfo = user.asksFor(TheUserInfo.aboutLoginData());
    Response resp = given()
      .baseUri(TCBSPROFILE_DOMAIN + TCBSPROFILE_OLD_REGISTER + tcbsId)
      .header("Authorization", "Bearer " + userInfo.getToken())
      .contentType(ContentType.JSON)
      .body(inputBody)
      .put();

    user.remember(TCBS_PS_OLD_REGISTER_RESP, resp);
  }
}
