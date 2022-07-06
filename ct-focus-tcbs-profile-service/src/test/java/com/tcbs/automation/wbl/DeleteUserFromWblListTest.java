package com.tcbs.automation.wbl;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.WblPolicy;
import com.tcbs.automation.cas.WblPolicyUser;
import com.tcbs.automation.cas.WblUser;
import common.CallApiUtils;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.*;
import static com.tcbs.automation.tools.FormatUtils.syncData;
import static net.serenitybdd.rest.SerenityRest.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/wbl/DeleteUserFromWblList.csv", separator = '|')
public class DeleteUserFromWblListTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String errorMessage;
  private String wblUserId;
  private String prepareValue;
  private String idNumber;
  private String policyCode;

  @Before
  public void setup() {
    prepareValue = String.valueOf(new Date().getTime());
    idNumber = prepareValue.substring(1);
    policyCode = "NNB_NLQ.FUCVREIT";
    if (testCaseName.contains("case valid request")) {
      CallApiUtils.callAddUserToWblListApi(testCaseName, prepareValue, idNumber, "2021-06-01", "2021-10-31",
        "NNB", policyCode, null, "Hồ Thủy Anh", "192 Lê Trọng Tấn, Hà Nội");
      wblUserId = WblUser.getByIdNumber(idNumber).getId().toString();
    } else {
      wblUserId = syncData(wblUserId);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api delete user from wbl list")
  public void perfomTest() {
    Response response = given()
      .baseUri(DELETE_USER_FROM_WBL_LIST.replaceAll("#wblUserId#", wblUserId))
      .contentType("application/json")
      .header("Authorization", "Bearer " +
        (testCaseName.contains("user has no permission") ? TCBSPROFILE_AUTHORIZATION : TCBSPROFILE_SPECIALWBLKEY))
      .delete();

    assertThat("verify status code", response.getStatusCode(), is(statusCode));

    if (statusCode == 200) {
      List<WblPolicyUser> wblPolicyUsers = WblPolicyUser.getByWblUserIdAndPolicyId(new BigDecimal(wblUserId), WblPolicy.getByPolicyCode(policyCode).getId());
      assertThat(wblPolicyUsers.size(), is(0));
    } else if (statusCode == 403) {
      assertThat("verify error message", response.jsonPath().get("errorMessage"), is(errorMessage));
    } else {
      assertThat("verify error message", response.jsonPath().get("message"), is(containsString(errorMessage)));
    }
  }
}
