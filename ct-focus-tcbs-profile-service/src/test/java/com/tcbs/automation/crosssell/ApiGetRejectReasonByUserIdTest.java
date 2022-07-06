package com.tcbs.automation.crosssell;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.ObReason;
import com.tcbs.automation.cas.TcbsIdentification;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.GET_REJECT_REASON_BY_USER_ID;
import static common.CallApiUtils.callGetApiHasNoParams;
import static common.CommonUtils.getListRejectReasons;
import static common.CommonUtils.verifyRejectReasonsListField;
import static common.ProfileTools.TOKEN;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/crosssell/ApiGetRejectReasonByUserId.csv", separator = '|')
public class ApiGetRejectReasonByUserIdTest {

  @Getter
  private String testCaseName;
  @Getter
  private int statusCode;
  private String idNumber;
  private String errorMessage;

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Get Reject Reason By UserId")
  public void verifyGetRejectReasonByUserIdTest() {

    System.out.println("TestCaseName : " + testCaseName);

    Response response = callGetApiHasNoParams(GET_REJECT_REASON_BY_USER_ID.replace("{idNumber}", idNumber), "Authorization", "Bearer " + TOKEN);

    assertThat(response.getStatusCode(), is(statusCode));

    if (response.statusCode() == 200) {
      List<HashMap<String, Object>> responseList = response.jsonPath().getList("");
      if (testCaseName.contains("userId not exist")) {
        assertTrue(responseList.isEmpty());
      } else {
        String userId = TcbsIdentification.getByIdNumber(idNumber).getUserId().toString();
        List<ObReason> obReasons = getListRejectReasons(userId);
        verifyRejectReasonsListField(responseList, obReasons);
      }
    } else if (response.statusCode() == 400) {
      String actualMessage = response.jsonPath().get("message");
      assertEquals(errorMessage, actualMessage);
    }
  }


}
