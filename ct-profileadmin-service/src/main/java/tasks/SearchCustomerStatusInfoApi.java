package tasks;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import models.CustomerStatusInfo;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;

import java.net.HttpURLConnection;

import static com.tcbs.automation.config.profileadmin.ProfileAdminConfig.PROFILES_ADMIN_DOMAIN;
import static com.tcbs.automation.config.profileadmin.ProfileAdminConfig.PROFILES_ADMIN_SEARCHECUSTOMER;
import static com.tcbs.automation.config.profileadmin.ProfileAdminKey.PA_USER_PROFILE;

public class SearchCustomerStatusInfoApi implements Task {

  private String code105C;

  public SearchCustomerStatusInfoApi(String code105C) {
    this.code105C = code105C;
  }

  public static SearchCustomerStatusInfoApi findUser(String code105C) {
    return new SearchCustomerStatusInfoApi(code105C);
  }

  @Override
  @Step("Call API calculate cashback VIP")
  public <T extends Actor> void performAs(T user) {
    Response response = RestAssured.given().baseUri(PROFILES_ADMIN_DOMAIN)
      .header("Content-Type", ContentType.JSON)
      .when()
      .queryParam("code105C", code105C)
      .get(PROFILES_ADMIN_SEARCHECUSTOMER);

    if (response.getStatusCode() == HttpURLConnection.HTTP_OK) {
      user.remember(PA_USER_PROFILE, response.as(CustomerStatusInfo.class));
    }
  }
}
