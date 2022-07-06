package com.tcbs.automation.bpm;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.cas.TcbsUserBook;
import common.CallApiUtils;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static common.CommonUtils.getBookId;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/bpm/UpdateProfileDocumentById.csv", separator = '|')
public class UpdateProfileDocumentByIdTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String tcbsId;
  private String errorMessage;
  private String bookIdInput;
  private String bookEmailRMUpload;
  private String bookManagerGroup;
  private String bookManagerUser;
  private String bookNote;
  private String bookReceivedDate;
  private String bookType;
  private int bookStatus;
  private HashMap<String, Object> body;
  private String userId;
  private String bookIdCreated;

  @Before
  public void setup() {

    tcbsId = syncData(tcbsId);
    bookEmailRMUpload = syncData(bookEmailRMUpload);
    bookManagerGroup = syncData(bookManagerGroup);
    bookManagerUser = syncData(bookManagerUser);
    bookNote = syncData(bookNote);
    bookReceivedDate = syncData(bookReceivedDate);
    bookType = syncData(bookType);

    if (statusCode == 200) {
      userId = TcbsUser.getByTcbsId(tcbsId).getId().toString();
      CallApiUtils.callCreateNewProfileDocumentApi(tcbsId, bookType);
      bookIdCreated = TcbsUserBook.getByUserIdAndType(userId, bookType).get(0).getId().toString();
    }
    String getBookId = getBookId(bookIdInput, bookIdCreated);

    HashMap<String, Object> book = new HashMap<>();
    book.put("bookEmailRMUpload", bookEmailRMUpload);
    book.put("bookManagerGroup", bookManagerGroup);
    book.put("bookManagerUser", bookManagerUser);
    book.put("bookNote", bookNote);
    book.put("bookReceivedDate", bookReceivedDate);
    book.put("bookType", bookType);
    book.put("bookId", getBookId);
    book.put("bookStatus", bookStatus);

    body = new HashMap<>();
    body.put("book", book);

  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api update profile document by id")
  public void performTest() {

    RequestSpecification requestSpecification = given()
      .baseUri(UPDATE_PROFILE_DOCUMENT_BY_ID.replace("#tcbsId#", tcbsId))
      .contentType("application/json")
      .header("x-api-key", API_KEY);
    Response response;

    Gson gson = new Gson();

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.put();
    } else {
      response = requestSpecification.body(gson.toJson(body)).put();
    }

    assertThat("verify status", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      int status = TcbsUserBook.getById(bookIdCreated).getStatus().intValue();
      assertThat(status, is(bookStatus));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(containsString(errorMessage)));
    }
  }

  @After
  public void clearData() {
    // Clear data
    if (statusCode == 200) {
      TcbsUserBook.deleteByUserId(userId, bookType);
      CallApiUtils.clearCache(DELETE_CACHE, "x-api-key", API_KEY);
    }
  }
}