package com.tcbs.automation.newonboarding.tci3;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.cas.TcbsFancy105C;
import com.tcbs.automation.cas.TcbsUser;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.SUGGEST_FANCY_105C;
import static common.CallApiUtils.callPostApiHasBody;
import static common.ProfileTools.TOKEN;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/SuggestionFancy105C.csv", separator = '|')
public class SuggestionFancy105CTest {
  @Getter
  private String testCaseName;
  private int statusCode;
  private String phone;
  private String message;


  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Suggestion Fancy 105C")
  public void verifySuggestionFancy105CTest() {

    System.out.println("TescaseName : " + testCaseName);

    HashMap<String, Object> body = getSuggestFancy105CBody(testCaseName, phone);

    Response response = callPostApiHasBody(SUGGEST_FANCY_105C, "Authorization", "Bearer " + TOKEN, body);

    Assert.assertThat(response.getStatusCode(), is(statusCode));
    if (response.statusCode() == 200) {
      String suggestByPhone = response.jsonPath().getString("suggestionByPhone");
      if (testCaseName.contains("valid") || testCaseName.contains("contains space")) {
        assertEquals(TcbsUser.getListByUserName(suggestByPhone).size(), 0);
        assertEquals(TcbsFancy105C.getListByCode105C(suggestByPhone).size(), 0);
      } else {
        assertNull(suggestByPhone);
      }

    }

  }

  public HashMap<String, Object> getSuggestFancy105CBody(String testCaseName, String phone) {

    HashMap<String, Object> body = new HashMap<>();
    if (testCaseName.contains("missing BODY")) {
      body = new HashMap<>();
    } else {
      body.put("phone", phone);
    }
    return body;
  }
}
