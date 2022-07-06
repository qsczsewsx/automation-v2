package com.tcbs.automation.accountClosing;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.CLOSE_ACCOUNT_DOWNLOAD_DOC;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.DOWNLOAD_DOC_CLOSE;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/accountClosing/CloseAccountDownloadDocListTest.csv", separator = '|')
public class CloseAccountDownDocListTest {

  @Getter
  private String testCaseName;
  @Getter
  private String downloadId;
  private int statusCode;
  private String errorMessage;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Download Doc List Close")
  public void verifyApiDownDocListTest() {

    System.out.println("TestCaseName : " + testCaseName);
    downloadId = syncData(downloadId);

    Response response = given()
      .baseUri(DOWNLOAD_DOC_CLOSE)
      .header("Authorization", "Bearer " + CLOSE_ACCOUNT_DOWNLOAD_DOC)
      .contentType("application/json")
      .param("downloadId", downloadId)
      .when()
      .get();

    assertThat(response.getStatusCode(), is(statusCode));
    if (response.statusCode() == 200) {
      assertThat("verify Delete Doc", response.jsonPath().get("fileName"), is("test.txt"));
    } else if (response.statusCode() == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}



