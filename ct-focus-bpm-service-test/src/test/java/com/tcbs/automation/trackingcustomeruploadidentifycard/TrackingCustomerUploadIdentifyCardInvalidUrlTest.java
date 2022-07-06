package com.tcbs.automation.trackingcustomeruploadidentifycard;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.abilities.CallAllAPI;
import com.tcbs.automation.login.LoginApi;
import io.restassured.http.Header;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import tasks.trackingcustomeruploadidentitycard.TrackingCustomerUploadIdentifyCard;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceKey.TCBS_PS_OLD_ACC_105C;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceKey.TCBS_PS_OLD_ACC_PASS;
import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static net.serenitybdd.screenplay.rest.questions.ResponseConsequence.seeThatResponse;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/trackingcustomeruploadidentifycard/InvalidTrackingCustomerUploadIdentifyCardUrl.csv", separator = '|')
public class TrackingCustomerUploadIdentifyCardInvalidUrlTest {
  private Actor user;
  private Header header;
  private String TestCaseName;
  private String InputBody;
  private String HeaderAuthorization;
  private String HeaderKey;
  private String HeaderValue;
  private String Url;
  private String Method;
  private int ExpectedStatus;

  @Before
  public void before() throws Exception {
    user = Actor.named("Focus Tester");
    header = new Header(HeaderKey, HeaderValue);

    givenThat(user).can(CallAllAPI.withProvidedInfo());
    givenThat(user).attemptsTo(LoginApi.withCredentials(TCBS_PS_OLD_ACC_105C, TCBS_PS_OLD_ACC_PASS));
  }

  @Test
  @TestCase(name = "#TestCaseName")
  @Title("Tracking customer upload identify card with invalid url")
  public void tracking_customer_upload_identity_card_with_invalid_url() {
    when(user).attemptsTo(TrackingCustomerUploadIdentifyCard.withInfo(InputBody, header, HeaderAuthorization, Url, Method));
    then(user).should(seeThatResponse(response -> response.statusCode(ExpectedStatus)));
  }
}
