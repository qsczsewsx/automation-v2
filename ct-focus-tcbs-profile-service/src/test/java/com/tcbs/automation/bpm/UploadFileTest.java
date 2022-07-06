package com.tcbs.automation.bpm;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.UPLOAD_FILE;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/bpm/UploadFile.csv", separator = '|')
public class UploadFileTest {

  @Getter
  private String testcaseName;
  private int statusCode;
  private String cookie;
  private String body;
  private String errMess;

  @Test
  @TestCase(name = "#testcaseName#")
  @Title("Verify API upload file")
  public void uploadFile() {
    System.out.println("Testcase Name: " + testcaseName);
    Response response = given()
      .urlEncodingEnabled(false)
      .baseUri(UPLOAD_FILE)
      .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
      .header("Cookie", cookie)
      .body(body)
      .when()
      .post();

    assertEquals(statusCode, response.getStatusCode());

    if (response.getStatusCode() == 200) {
      assertEquals(errMess, response.jsonPath().get("msg"));
    }
  }
}
