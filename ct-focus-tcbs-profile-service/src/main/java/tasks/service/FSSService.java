package tasks.service;

import io.restassured.response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.FSS_SERVICE;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static net.serenitybdd.rest.SerenityRest.given;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FSSService {
  private String referenceId;
  private String fullName;
  private String idNumber;
  private String email;
  private String phone;

  public static Response accountOpenRequest(String referenceId, String fullName,
                                            String idNumber, String email, String phone) {
    FSSService fssService = new FSSService(referenceId, fullName, idNumber, email, phone);
    return fssService.accountOpenRequest();
  }

  public static Response accountOpenConfirm(String referenceId) {
    FSSService fssService = new FSSService();
    fssService.setReferenceId(referenceId);

    return fssService.accountOpenConfirm();
  }

  public Response accountOpenRequest() {
    if (this.referenceId == null) {
      this.referenceId = String.valueOf(new Date().getTime());
    }
    if (this.fullName == null) {
      this.fullName = "fullName " + this.referenceId;
    }
    if (this.idNumber == null) {
      this.idNumber = this.referenceId.substring(this.referenceId.length() - 2);
    }
    if (this.email == null) {
      this.email = "email_" + this.referenceId + "@gmail.com";
    }
    if (this.phone == null) {
      this.phone = this.referenceId.substring(this.referenceId.length() - 3);
    }
    String xmlBody = fileTxtToString("src/main/resources/SoapBody/account-open-request.xml")
      .replace("{referenceid}", referenceId)
      .replace("{fullname}", fullName)
      .replace("{idnumber}", idNumber)
      .replace("{email}", email)
      .replace("{phone}", phone);

    return given()
      .baseUri(FSS_SERVICE)
      .contentType("text/xml; charset=utf-8")
      .header("SOAPAction", "http://tempuri.org/IOnlineTradingWcf/FlexService")
      .body(xmlBody)
      .when()
      .post();
  }

  public Response accountOpenConfirm() {
    if (this.referenceId == null) {
      return null;
    }

    String xmlBody = fileTxtToString("src/main/resources/SoapBody/account-open-confirm.xml")
      .replace("{referenceid}", referenceId);
    return given()
      .baseUri(FSS_SERVICE)
      .contentType("text/xml; charset=utf-8")
      .header("SOAPAction", "http://tempuri.org/IOnlineTradingWcf/FlexService")
      .body(xmlBody)
      .when()
      .post();
  }

}
