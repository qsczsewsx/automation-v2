package com.tcbs.automation.iactg;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsBankIaaccount;
import com.tcbs.automation.cas.TcbsUser;
import com.tcbs.automation.login.LoginData;
import com.tcbs.automation.tools.LoginUtil;
import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GET_INFO_IA_TCI3;
import static com.tcbs.automation.publicmatcher.HashMapMatcher.has;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/iactg/GetIaTci3Data.csv")
public class GetInfoIaTci3Test {
  private String testCaseName;
  private String username;
  private String tcbsId;
  private int statusCode;
  private String iaConnect;
  private String isIa;
  private String iaStatus;

  @Before
  public void before() {
    iaConnect = syncData(iaConnect);
    isIa = syncData(isIa);
    if (StringUtils.isNotEmpty(iaConnect)) {
      TcbsUser tcbsUser = TcbsUser.getByUserName(username);
      TcbsBankIaaccount.updateStatusOfLastRecord(tcbsUser.getId().toString(), iaStatus, iaConnect);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  public void verifyApiGetIaInfoFromTci3() throws Exception {

    LoginData loginData = LoginUtil.login(username, "abc123");
    Response response = given()
      .baseUri(GET_INFO_IA_TCI3 + "/" + tcbsId)
      .header("Authorization", "Bearer " + loginData.getToken())
      .get();
    assertThat("verify status code", response.statusCode(), is(statusCode));
    if (statusCode == 200) {
      assertThat("verify IA connection", response.jsonPath().get(),
        has("iaConnect", is(iaConnect)));
      assertThat("verify IA status",
        response.jsonPath().get(iaConnect + ".accountStatus.iaStatus.isIA"),
        is(isIa)
      );
      if (StringUtils.isNotEmpty(isIa)) {
        assertThat("verify IA bank account",
          response.jsonPath().get(iaConnect + ".iaBankAccount"),
          is(notNullValue())
        );
      }
      assertThat("verify TCB bank account",
        response.jsonPath().get("TCB.bankAccount"),
        is(notNullValue())
      );
    }
  }
}
