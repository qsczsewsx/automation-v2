package tasks;

import io.restassured.response.Response;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import static com.tcbs.automation.config.profileadmin.ProfileAdminConfig.PROFILEADMIN_USERS;
import static com.tcbs.automation.config.profileadmin.ProfileAdminConfig.PROFILES_ADMIN_DOMAIN;
import static com.tcbs.automation.config.profileadmin.ProfileAdminKey.PA_USER_PROFILE_DETAIL;
import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.screenplay.Tasks.instrumented;

public class GetUserProfileAPI implements Task {
  private String tcbsId;

  public GetUserProfileAPI(String tcbsId) {
    this.tcbsId = tcbsId;
  }

  public static GetUserProfileAPI withTcbsId(String tcbsId) {
    return instrumented(GetUserProfileAPI.class, tcbsId);
  }

  @Override
  public <T extends Actor> void performAs(T user) {
    Response resp = given()
      .baseUri(PROFILES_ADMIN_DOMAIN
        + PROFILEADMIN_USERS)
      .header("Content-Type", "application/json")
      .get(tcbsId);
    user.remember(PA_USER_PROFILE_DETAIL, resp);
  }
}
