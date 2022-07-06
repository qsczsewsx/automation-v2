package com.tcbs.automation.accountClosing;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.ObUserCloseDocs;
import com.tcbs.automation.cas.TcbsUser;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.CLOSE_ACCOUNT_GET_DOC;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GET_DOC_CLOSE;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static common.DatesUtils.convertTimestampToString;
import static common.DatesUtils.covertDateToString;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/accountClosing/CloseAccountGetDocListTest.csv", separator = '|')
public class CloseAccountGetDocListTest {

  @Getter
  private String testCaseName;
  @Getter
  private String username;
  private int statusCode;
  private String errorMessage;


  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Get Doc List Close")
  public void verifyApiGetDocListTest() {

    System.out.println("TestCaseName : " + testCaseName);
    username = syncData(username);

    Response response = given()
      .baseUri(GET_DOC_CLOSE)
      .header("Authorization", "Bearer " + CLOSE_ACCOUNT_GET_DOC)
      .contentType("application/json")
      .param("username", username)
      .when()
      .get();

    assertThat(response.getStatusCode(), is(statusCode));
    if (response.statusCode() == 200) {
      String userId = TcbsUser.getByUserName(username).getId().toString();
      List<HashMap<String, Object>> taskDocument = response.jsonPath().getList("data");
      ObUserCloseDocs obUserCloseDocs = ObUserCloseDocs.getCloseDocsByUserId(userId).get(0);
      for (HashMap<String, Object> document : taskDocument) {
        if (document.get("id").toString().equalsIgnoreCase(obUserCloseDocs.getId().toString())) {
          assertThat(document.get("objectId"), is(obUserCloseDocs.getEcmId()));
          assertEquals(convertTimestampToString(document.get("createDate").toString()), covertDateToString((Timestamp) obUserCloseDocs.getCreateDate()));
          assertTrue(document.get("fileName").toString().contains(obUserCloseDocs.getFileName()));
          assertThat(document.get("fileType"), is(obUserCloseDocs.getFileType()));
          assertThat(document.get("actor"), is(obUserCloseDocs.getActor()));
          assertThat(document.get("fileSuffix"), is(obUserCloseDocs.getFileSuffix()));
          assertThat(document.get("taskId").toString(), is(obUserCloseDocs.getTaskId().toString()));
          //assertThat(document.get("userId"), is(obUserCloseDocs.getUserId()));
        }
      }

    } else if (response.statusCode() == 400) {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}




