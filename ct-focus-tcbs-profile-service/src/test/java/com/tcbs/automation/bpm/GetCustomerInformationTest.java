package com.tcbs.automation.bpm;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.junit.Assert.assertEquals;


@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/bpm/GetCustomerInformation.csv", separator = '|')
public class GetCustomerInformationTest {
  @Getter
  private String testcaseName;
  private int statusCode;
  private String tcbsId;
  private String totalElements;
  private String errorMess;

  @Test
  @TestCase(name = "#testcaseName#")
  @Title("Verify API Get Customer Information")
  public void getCustomerInformation() {
    String tcbsid_filter = "tcbsIds:%5B%22".concat(tcbsId.concat("%22%5D"));

    System.out.println("Testcase name: " + testcaseName);
    Response response = given()
      .urlEncodingEnabled(false)
      .baseUri(GET_CUSTOMER_INFORMATION.replace("#tcbsId#", tcbsId))
      .param("filter", tcbsid_filter)
      .param("isHiddenAvatar", true)
      .param("size", 100)
      .param("fields", "bankAccounts,personalInfo,basicInfo,addtionalInfo")
      .header("x-api-key",
        testcaseName.contains("user having no permission") ? FMB_X_API_KEY : PROFILE_X_API_KEY)
      .when()
      .get();

    assertEquals(statusCode, response.getStatusCode());

    if (response.getStatusCode() == 200) {
      assertEquals(totalElements, response.jsonPath().get("totalElements").toString());
      if (testcaseName.contains("valid")) {
        assertEquals(tcbsId, response.jsonPath().get("content[0].basicInfo.tcbsId"));
      }
    } else {
      assertEquals(errorMess, response.jsonPath().get("message"));
    }
  }
}
