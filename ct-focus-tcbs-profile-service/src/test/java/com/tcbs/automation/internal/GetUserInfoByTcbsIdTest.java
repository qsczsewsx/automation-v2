package com.tcbs.automation.internal;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsAddress;
import com.tcbs.automation.cas.TcbsIdentification;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.cas.TcbsUserInstrument;
import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static common.CommonUtils.*;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/GetProfileByTcbsId.csv", separator = '|')
public class GetUserInfoByTcbsIdTest {
  private String testCaseName;
  private String condition;
  private String tcbsId;
  private int statusCode;
  private String errorMessage;

  public static void verifyBasicInfoField(Map<String, Object> basicInfo, TcbsUser tcbsUser) {
    assertEquals(tcbsUser.getTcbsid(), basicInfo.get("tcbsId"));
    assertEquals(tcbsUser.getUsername(), basicInfo.get("code105C"));
    String status = getUserStatus(tcbsUser.getAccountStatus().toString());
    assertEquals(status, basicInfo.get("status"));
    String type = getUserType(tcbsUser.getCustype().toString());
    assertEquals(type, basicInfo.get("type"));
    String accountType = getAccountType(tcbsUser.getUsername());
    assertEquals(accountType, basicInfo.get("accountType"));
  }

  public static void verifyPersonalInfoField(Map<String, Object> personalInfo, Map<String, Object> identityCard, TcbsUser tcbsUser) {
    String userId = tcbsUser.getId().toString();
    TcbsAddress tcbsAddress = TcbsAddress.getByTcbsAddress(userId);
    TcbsUserInstrument tcbsUserInstrument = TcbsUserInstrument.getByTcbsInstrument(userId);
    TcbsIdentification tcbsIdentification = TcbsIdentification.getByTcbsIdentification(userId);
    assertEquals(tcbsUser.getLastname() + " " + tcbsUser.getFirstname(), personalInfo.get("fullName"));
    assertEquals(tcbsUser.getFirstname(), personalInfo.get("firstName"));
    assertEquals(tcbsUser.getLastname(), personalInfo.get("lastName"));
    assertEquals(tcbsUser.getEmail(), personalInfo.get("email"));
    assertEquals(tcbsUser.getPhone(), personalInfo.get("phoneNumber"));
    assertEquals(BigDecimal.valueOf(1).equals(tcbsUser.getIsForeignPhone()), personalInfo.get("isForeignPhone"));
    assertThat(personalInfo.get("avatarUrl").toString(), is(containsString(tcbsUser.getAvatarUrl())));
    if (Objects.nonNull(tcbsAddress)) {
      assertEquals(tcbsAddress.getAddress(), personalInfo.get("contactAddress"));
    }
    if (Objects.nonNull(tcbsUserInstrument)) {
      assertEquals(tcbsUserInstrument.getCitizenship(), personalInfo.get("nationality"));
    }
    if (Objects.nonNull(tcbsIdentification)) {
      assertEquals(tcbsIdentification.getIdNumber(), identityCard.get("idNumber"));
      assertEquals(tcbsIdentification.getIdPlace(), identityCard.get("idPlace"));
    }
  }

  @Before
  public void setup() {
    condition = syncData(condition);
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api get get user info by tcbsid")
  public void verifyApiGetCustInfoTest() {
    System.out.println(testCaseName);

    Response response = given()
      .baseUri(INTERNAL_GET_CUSTOMER_INFO.replace("{tcbsId}", tcbsId))
      .header("x-api-key", testCaseName.contains("invalid x-api-key") ? FMB_X_API_KEY : API_KEY)
      .param("fields", condition)
      .when()
      .get();

    assertThat(response.getStatusCode(), is(statusCode));
    if (response.statusCode() == 200) {
      if (testCaseName.contains("fields invalid")) {
        assertThat(response.getBody().print(), is("{}"));
      } else {
        TcbsUser tcbsUser = TcbsUser.getByTcbsId(tcbsId);
        if (StringUtils.isBlank(condition) || condition.contains("personalInfo")) {
          Map<String, Object> personalInfo = response.jsonPath().getMap("personalInfo");
          Map<String, Object> identityCard = response.jsonPath().getMap("personalInfo.identityCard");
          verifyPersonalInfoField(personalInfo, identityCard, tcbsUser);
        }
        if (StringUtils.isBlank(condition) || condition.contains("basicInfo")) {
          Map<String, Object> basicInfo = response.jsonPath().getMap("basicInfo");
          verifyBasicInfoField(basicInfo, tcbsUser);
        }
      }
    } else {
      String msg = response.jsonPath().getString("message");
      assertEquals(errorMessage, msg);
    }
  }
}
