package com.tcbs.automation.validateidentity;

import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.abilities.CallAllAPI;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import questions.validateidentity.ValidateIdentityQuestion;
import tasks.validateidentity.ValidateIdentityAPI;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceKey.TCBS_PS_OLD_ACC_105C;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceKey.TCBS_PS_OLD_ACC_PASS;
import static com.tcbs.automation.publicmatcher.CombinableMatcher.all;
import static com.tcbs.automation.publicmatcher.HashMapMatcher.has;
import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/validateidentity/SuccessValidateBody.csv", separator = '|')
public class ValidateIdentityValidBodyTest {
  private Actor user;
  private String InputBody;
  private int ExpectedStatus;
  private String tcbsId;


  @Before
  public void before() throws Exception {
    user = Actor.named("Focus Tester");
    givenThat(user).can(CallAllAPI.withProvidedInfo());
    givenThat(user).attemptsTo(LoginApi.withCredentials(TCBS_PS_OLD_ACC_105C, TCBS_PS_OLD_ACC_PASS));
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Validate identity with valid data")
  public void validate_identity_with_valid_data() {
    tcbsId = user.asksFor(TheUserInfo.aboutLoginData()).getTcbsid();
    when(user).attemptsTo(ValidateIdentityAPI.withInfo(InputBody, tcbsId));
    then(user).should(seeThat(
      String.format("Response status %s and corresponding error message", ExpectedStatus),
      ValidateIdentityQuestion.fromApi(),
      all(has("status", is(ExpectedStatus))))
    );
  }
}