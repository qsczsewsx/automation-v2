package com.tcbs.automation.newonboarding.tci3;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import common.CommonUtils;
import io.restassured.response.Response;
import lombok.Getter;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceConfig.CONFIRM_BOOKING_FANCY_105C;
import static common.CallApiUtils.callPostApiHasBody;
import static common.CommonUtils.getPhoneOrCode105C;
import static common.ProfileTools.TOKEN;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/newonboarding/ConfirmBookingFancy105C.csv", separator = '|')
public class ConfirmBookingFancy105CTest {
  private static String redisPhone;
  @Getter
  private String testCaseName;
  private int statusCode;
  private String phone;
  private String code105C;
  private String message;

  @Before
  public void before() {
    if (testCaseName.contains("exist in redis")) {
      HashMap<String, Object> body = new HashMap<>();
      redisPhone = String.valueOf(new Date().getTime());
      body.put("phone", redisPhone.substring(1, 13));
      body.put("code105C", "105C" + redisPhone.substring(7, 13));
      callPostApiHasBody(CONFIRM_BOOKING_FANCY_105C, "Authorization", "Bearer " + TOKEN, body);
    }
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Verify api Confirm Booking Fancy 105C")
  public void verifyConfirmBookingFancy105CTest() {

    System.out.println("TestCaseName : " + testCaseName);
    System.out.println("Redis : " + redisPhone);

    phone = getPhoneOrCode105C("phone", phone, redisPhone);
    code105C = getPhoneOrCode105C("105C", code105C, redisPhone);
    HashMap<String, Object> body = CommonUtils.getConfirmBookingFancy105CBody(testCaseName, phone, code105C);

    Response response = callPostApiHasBody(CONFIRM_BOOKING_FANCY_105C, "Authorization", "Bearer " + TOKEN, body);
    assertThat(response.getStatusCode(), is(statusCode));
    assertEquals(response.jsonPath().getString("code"), message);

  }
}
