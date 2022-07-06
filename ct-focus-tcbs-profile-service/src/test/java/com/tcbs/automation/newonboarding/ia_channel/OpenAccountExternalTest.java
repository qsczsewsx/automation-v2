package com.tcbs.automation.newonboarding.ia_channel;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.TCBSPROFILE_OPEN_ACCOUNT_EXTERNAL;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.TCBSPROFILE_TOKENKEY;
import static com.tcbs.automation.tools.ConvertUtils.fileTxtToString;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static common.EncodeDataUtils.encodeValue;
import static common.EncodeDataUtils.md5;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/OpenAccountExternal.csv", separator = '|')
public class OpenAccountExternalTest {
  private static DateTimeFormatter formatter;
  private static LocalDateTime currentDate;
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String data;
  private String fullName;
  private String phone;
  private String email;
  private String birthday;
  private String idNumber;
  private String gender;
  private String idPlace;
  private String idDate;
  private String contactAddress;
  private String messageId;
  private String partner;
  private String signature;
  private String body;

  @BeforeClass
  public static void beforeClass() {
    formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
    currentDate = LocalDateTime.now();
  }

  @Before
  public void setup() throws UnsupportedEncodingException {

    if (birthday.equalsIgnoreCase("notyet18")) {
      birthday = formatter.format(currentDate.plusYears(-18).plusDays(1));
    } else if (birthday.equalsIgnoreCase("just18")) {
      birthday = formatter.format(currentDate.plusYears(-18));
    }

    fullName = encodeValue(fullName);
    birthday = encodeValue(birthday);
    contactAddress = encodeValue(contactAddress);
    idDate = encodeValue(idDate);
    idPlace = encodeValue(idPlace);

    String prepareValue = String.valueOf(new Date().getTime());
    String phonePrepare = "09" + prepareValue.substring(5);
    String emailPrepare = "linh.tth6" + prepareValue.substring(5) + "@gmail.com";
    String idNumberPrepare = prepareValue.substring(1);

    HashMap<String, String> resultPrepareData = new HashMap<>();
    String ID_NUMBER = "idNumber";
    resultPrepareData.put(ID_NUMBER, idNumberPrepare);
    String PHONE_DATA = "phone";
    resultPrepareData.put(PHONE_DATA, phonePrepare);
    if (errorMessage.equalsIgnoreCase("SUCCESS")) {
      email = emailPrepare;
    } else {
      email = syncData(email);
    }
    String EMAILS = "email";
    resultPrepareData.put(EMAILS, email);
    String MESSAGING = "messageId";
    resultPrepareData.put(MESSAGING, "FSS" + prepareValue);
    String SIGNATURES = "signature";
    resultPrepareData.put(SIGNATURES, md5("FSS" + prepareValue + idNumberPrepare + phonePrepare + emailPrepare + "FSS"));

    if (testCaseName.contains("ERROR invalid phone")) {
      resultPrepareData.put(PHONE_DATA, phone);
      resultPrepareData.put(SIGNATURES, md5("FSS" + prepareValue + idNumberPrepare + phone + email + "FSS"));
    } else if (testCaseName.contains("ERROR email")) {
      resultPrepareData.put(EMAILS, email);
      resultPrepareData.put(SIGNATURES, md5("FSS" + prepareValue + idNumberPrepare + phonePrepare + email + "FSS"));
    } else if (testCaseName.contains("ERROR idNumber")) {
      resultPrepareData.put(ID_NUMBER, idNumber);
      resultPrepareData.put(SIGNATURES, md5("FSS" + prepareValue + idNumber + phonePrepare + email + "FSS"));
    } else if (testCaseName.contains("ERROR messageId")) {
      resultPrepareData.put(MESSAGING, messageId);
      resultPrepareData.put(SIGNATURES, md5(messageId + idNumberPrepare + phonePrepare + emailPrepare + "FSS"));
    } else if (testCaseName.contains("ERROR partner")) {
      resultPrepareData.put("partner", partner);
    } else if (testCaseName.contains("ERROR signature")) {
      resultPrepareData.put(SIGNATURES, signature);
    }

    System.out.println(resultPrepareData);
    body = fileTxtToString("src/test/resources/requestBody/OpenAccountExternal.txt")
      .replaceAll("#fullName#", fullName).replaceAll("#birthday#", birthday)
      .replace("#email#", Matcher.quoteReplacement(resultPrepareData.get(EMAILS))).replace("#phone#", resultPrepareData.get(PHONE_DATA))
      .replace("#idNumber#", resultPrepareData.get(ID_NUMBER)).replace("#partner#", partner)
      .replace("#messageId#", resultPrepareData.get(MESSAGING)).replace("#signature#", resultPrepareData.get(SIGNATURES))
      .replaceAll("#contactAddress#", contactAddress).replaceAll("#idDate#", idDate).replaceAll("#idPlace#", idPlace);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api open account external")
  public void performTest() {
    System.out.println("Test case: " + testCaseName);

    RequestSpecification requestSpecification = given()
      .baseUri(TCBSPROFILE_OPEN_ACCOUNT_EXTERNAL)
      .header("Authorization", "Basic " + TCBSPROFILE_TOKENKEY)
      .contentType("application/x-www-form-urlencoded; charset=UTF-8");
    Response response;
    System.out.println(body);
    if (!testCaseName.contains("invalid phone")) {
      if (testCaseName.contains("missing BODY")) {
        response = requestSpecification.post();
      } else {
        response = requestSpecification.body(body).when().post();
      }
      if (statusCode == 200) {
        assertThat("verify message", response.jsonPath().get("msg"), is(errorMessage));
        if (errorMessage.equals("SUCCESS")) {
          assertThat("verify status", response.jsonPath().get("status"), is(0));
        } else {
          assertThat("verify data", response.jsonPath().get("data"), is(data));
        }
      }
    }
  }
}
