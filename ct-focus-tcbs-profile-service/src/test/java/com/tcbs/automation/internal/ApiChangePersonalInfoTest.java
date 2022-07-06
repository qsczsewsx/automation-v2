package com.tcbs.automation.internal;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.tools.ConvertUtils;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.jruby.util.log.Logger;
import org.jruby.util.log.LoggerFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.RestRequests.given;
import static org.junit.Assert.assertEquals;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ApiChangePersonalInfo.csv")
public class ApiChangePersonalInfoTest {
  private static final Logger logger = LoggerFactory.getLogger(ConvertUtils.class);
  private String gen_processInstanceId;
  @Getter
  private String testCaseName;
  private int statusCode;
  private String tcbsid;
  private String qrCode;
  private String processInstanceId;
  private String oldInformation;
  private String newInformation;
  private String registrationType;
  private String applyFor;
  private String sendRequestTime;
  private String fullName;
  private String phone;
  private String idNumber;
  private String idDate;
  private String idPlace;
  private String bookNumber;
  private String errMess;
  private String email;
  private String prepareValue;
  private String oldInfo;
  private String newInfo;

  @Before
  public void before() {
    tcbsid = syncData(tcbsid);
    oldInfo = syncData(oldInformation);
    newInfo = syncData(newInformation);

    prepareValue = String.valueOf(new Date().getTime());

    if ("read_from_file".equalsIgnoreCase(newInformation)) {
      newInfo = fileTxtToString("src/test/resources/data/newonboarding/newInformation.json");
    }
    if ("read_from_file".equalsIgnoreCase(oldInformation)) {
      oldInfo = fileTxtToString("src/test/resources/data/newonboarding/oldInformation.json");
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify Api change personal information")
  public void verifyApiChangePresonalInfo() {
    System.out.println("Testcase Name: " + testCaseName);

    String genProcessInstanceId = prepareValue;
    SimpleDateFormat sendRequestTimeFormat = new SimpleDateFormat("dd/MM/yyyy' 'hh:mm:ss");
    SimpleDateFormat idDateFormat = new SimpleDateFormat("dd/MM/yyyy");

    LinkedHashMap<String, Object> body = new LinkedHashMap<>();

    body.put("tcbsid", tcbsid);
    body.put("qrCode", qrCode);
    body.put("bookNumber", (bookNumber.isEmpty()) ? null : bookNumber);
    body.put("processInstanceId", (processInstanceId.contains("gen")) ? genProcessInstanceId : processInstanceId);
    body.put("oldInformation", oldInfo);
    body.put("newInformation", newInfo);
    body.put("registrationType", (registrationType.equalsIgnoreCase("number")) ? Integer.valueOf("12345678") : registrationType);
    body.put("applyFor", (applyFor.equalsIgnoreCase("number")) ? Integer.valueOf("12345678") : applyFor);
    body.put("sendRequestTime", sendRequestTime.equalsIgnoreCase("gen") ?
      sendRequestTimeFormat.format(new Date()) : sendRequestTime);
    body.put("fullName", fullName);
    body.put("phone", phone);
    body.put("idNumber", idNumber);
    body.put("idDate", idDate.equalsIgnoreCase("gen") ?
      idDateFormat.format(new Date()) : idDate);
    body.put("idPlace", idPlace);
    body.put("email", email);


    Response response = given()
      .baseUri(CHANGE_PERSONAL_INFO)
      .header("x-api-key", (statusCode == 401) ? FMB_X_API_KEY : API_KEY)
      .contentType("application/json")
      .cookie(COOKIE)
      .body(body)
      .when()
      .post();

    assertEquals(statusCode, response.getStatusCode());

    if (statusCode != 200) {
      assertEquals(errMess, response.jsonPath().get("message"));
    }

  }

}
