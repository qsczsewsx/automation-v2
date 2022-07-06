package com.tcbs.automation.derivative;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsApplicationUser;
import com.tcbs.automation.cas.TcbsUser;
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

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static common.ProfileTools.TOKEN;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/derivative/SendCustomMsgToDerivative.csv", separator = '|')
public class SendCustomMsgToDerivativeTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String tcbsId;
  private String errorMessage;
  private String custodyCd;
  private String custName;
  private String birthday;
  private String cardId;
  private String idDate;
  private String expireDate;
  private String idPlace;
  private String address;
  private String body;
  private String userId;

  @Before
  public void setup() {

    tcbsId = syncData(tcbsId);
    custodyCd = syncData(custodyCd);
    custName = syncData(custName);
    birthday = syncData(birthday);
    cardId = syncData(cardId);
    idDate = syncData(idDate);
    expireDate = syncData(expireDate);
    idPlace = syncData(idPlace);
    address = syncData(address);

    body = fileTxtToString("src/test/resources/requestBody/SendCustomMsgToDerivativeBody.json")
      .replaceAll("#tcbsId#", tcbsId).replaceAll("#custodyCd#", custodyCd)
      .replaceAll("#custName#", custName).replaceAll("#address#", address)
      .replaceAll("#cardId#", cardId).replaceAll("#idDate#", idDate)
      .replaceAll("#expireDate#", expireDate).replaceAll("#idPlace#", idPlace);

  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api send custom msg to derivative")
  public void perfomTest() {

    RequestSpecification requestSpecification = given()
      .baseUri(SEND_CUSTOM_MSG_TO_DERIVATIVE)
      .contentType("application/json")
      .header("Authorization", "Bearer " +
        (testCaseName.contains("user has no permission") ? TCBSPROFILE_BACKENDWBLKEY : TOKEN));
    Response response;

    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(body).post();
    }

    assertThat("verify status", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      userId = TcbsUser.getByTcbsId(tcbsId).getId().toString();
      String status = TcbsApplicationUser.getByTcbsApplicationUserAppId2(userId, "7")
        .getStatus().toString();
      assertThat(status, not("9"));
    } else if (statusCode == 403) {
      assertThat("verify error message", response.jsonPath().get("errorMessage"), is(errorMessage));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(containsString(errorMessage)));
    }
  }

  @After
  public void clearData() {
    // Clear data
    if (statusCode == 200) {
      TcbsApplicationUser.updateStatusApp(userId, "7", "9");
      CallApiUtils.clearCache(DELETE_CACHE, "x-api-key", API_KEY);
    }
  }
}
