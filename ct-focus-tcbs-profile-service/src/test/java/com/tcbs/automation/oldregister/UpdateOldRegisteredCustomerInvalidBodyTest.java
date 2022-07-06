package com.tcbs.automation.oldregister;

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
import questions.oldregister.TheOldRegisterQuestion;
import tasks.oldregister.OldRegisterAPI;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceKey.TCBS_PS_USER_105C;
import static com.tcbs.automation.functions.PublicConstant.DEFAULT_PASSWORD;
import static com.tcbs.automation.publicmatcher.CombinableMatcher.all;
import static com.tcbs.automation.publicmatcher.HashMapMatcher.has;
import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/oldregister/InvalidOldRegisterBody1.csv", separator = '|')
public class UpdateOldRegisteredCustomerInvalidBodyTest {

  private Actor user;
  private String TestCaseName;
  private String Body;
  private int ExpectedHttpCode;
  private String ExpectedHttpMessage;
  private String tcbsId;


  @Before
  public void before() throws Exception {
    user = Actor.named("Focus Tester");
    givenThat(user).can(CallAllAPI.withProvidedInfo());
    givenThat(user).attemptsTo(LoginApi.withCredentials(TCBS_PS_USER_105C, DEFAULT_PASSWORD));
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Update old registered customer with invalid body")
  public void update_old_registered_customer_with_invalid_body() {
    tcbsId = user.asksFor(TheUserInfo.aboutLoginData()).getTcbsid();
    when(user).attemptsTo(OldRegisterAPI.withInfo(tcbsId, Body));
    then(user).should(seeThat(
      String.format("Response status %s and corresponding error message", ExpectedHttpCode),
      TheOldRegisterQuestion.fromApi(),
      all(has("status", is(ExpectedHttpCode)))
        .and(has("message", is(ExpectedHttpMessage))))
    );

  }


}
