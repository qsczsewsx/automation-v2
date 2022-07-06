package com.tcbs.automation.crosssell;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.google.gson.Gson;
import com.tcbs.automation.cas.TcbsUser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/crosssell/HandleDuplicateRmEmail.csv", separator = '|')
public class HandleDuplicateRmEmailTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String tcbsId;
  private String email;
  private HashMap<String, Object> body;
  private String prepareValue;

  @Before
  public void setup() {
    prepareValue = String.valueOf(new Date().getTime() / 10);
    tcbsId = syncData(tcbsId);
    if (testCaseName.contains("case valid request")) {
      email = "testRm" + prepareValue + "@techcombank.com.vn";
      // Insert data in TCBS_USER (Rm user login through LDAP)
      TcbsUser tcbsUser = new TcbsUser();
      tcbsUser.setLastname("Nguyễn Đình");
      tcbsUser.setFirstname("Tú");
      tcbsUser.setTcbsid(prepareValue.substring(1));
      tcbsUser.setEmail(email);
      tcbsUser.setGender(BigDecimal.valueOf(1));
      tcbsUser.setCustype(BigDecimal.valueOf(0));
      tcbsUser.setBirthday(Timestamp.valueOf(
        LocalDateTime.parse("1983-07-23T00:00:00.000",
          DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))));
      tcbsUser.insert();
    } else {
      email = syncData(email);
    }
    body = new HashMap<>();
    body.put("tcbsId", tcbsId);
    body.put("email", email);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api handle duplicate rm email")
  public void perfomTest() {

    RequestSpecification requestSpecification = given()
      .baseUri(HANDLE_DUPLICATE_RM_EMAIL)
      .contentType("application/json")
      .header("x-api-key", (testCaseName.contains("missing x-api-key") ? TCBSPROFILE_AUTHORIZATION : API_KEY));

    Gson gson = new Gson();
    Response response;
    if (testCaseName.contains("missing BODY")) {
      response = requestSpecification.post();
    } else {
      response = requestSpecification.body(gson.toJson(body)).post();
    }

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      assertThat(TcbsUser.getByTcbsId(tcbsId).getEmail(), is(email));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(errorMessage));
    }
  }
}
