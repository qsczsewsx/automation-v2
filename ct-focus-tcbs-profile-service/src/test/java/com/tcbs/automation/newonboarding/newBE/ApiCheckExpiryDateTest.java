package com.tcbs.automation.newonboarding.newBE;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.CHECK_EXPIRY_DATE;
import static common.ProfileTools.TOKEN;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiCheckExpiryDate.csv", separator = '|')
public class ApiCheckExpiryDateTest {

  @Getter
  private String testCaseName;
  @Getter
  private String idNumber;
  private String idDate;
  private String birthday;
  private int statusCode;
  private String errorMsg;
  private String resData;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Check Expiry Date")
  public void verifyApiCheckExpiryDateTest() {

    System.out.println("TestCaseName : " + testCaseName);

    Response response = given()
      .baseUri(CHECK_EXPIRY_DATE)
      .header("Authorization", TOKEN)
      .param("idNumber", idNumber)
      .param("idDate", idDate)
      .param("birthday", birthday)
      .when()
      .get();

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {
      String data = response.jsonPath().get("data");
      if (data != null) {
        assertEquals(resData, data);
      }

    } else if (response.statusCode() == 400) {
      String actualMessage = response.jsonPath().get("message");
      assertEquals(errorMsg, actualMessage);
    }
  }

}
