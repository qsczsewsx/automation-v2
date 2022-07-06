package tasks.trackingcustomeruploadidentitycard;

import io.restassured.http.Header;
import io.restassured.response.Response;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import static net.serenitybdd.rest.SerenityRest.given;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static com.tcbs.automation.config.bpm.BpmConfig.BPM_SERVICE_DOMAIN;

public class TrackingCustomerUploadIdentifyCard implements Task {
  private String inputBody;
  private Header header;
  private String authorization;
  private String url;
  private String method;

  public TrackingCustomerUploadIdentifyCard(String inputBody, Header header, String authorization, String url, String method) {
    this.inputBody = inputBody;
    this.header = header;
    this.authorization = authorization;
    this.url = url;
    this.method = method;
  }

  public static TrackingCustomerUploadIdentifyCard withInfo(String inputBody, Header header, String authorization, String url, String method) {
    return instrumented(TrackingCustomerUploadIdentifyCard.class, inputBody, header, authorization, url, method);
  }

  @Override
  public <T extends Actor> void performAs(T t) {
    Response resp = given()
      .header(header)
      .header("Authorization", authorization)
      .body(inputBody).request(method, BPM_SERVICE_DOMAIN + url);
  }
}

