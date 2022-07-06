package com.tcbs.automation.oldregister;


import com.adaptavist.tm4j.junit.annotation.TestCase;
import com.tcbs.automation.abilities.CallAllAPI;
import com.tcbs.automation.login.LoginApi;
import com.tcbs.automation.login.TheUserInfo;
import matchers.oldregister.UpdatedOldCustomerMatcher;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.serenitybdd.screenplay.Actor;
import net.thucydides.core.annotations.Title;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import questions.TheUserProfile;
import questions.oldregister.TheOldRegisterQuestion;
import tasks.oldregister.OldRegisterAPI;

import java.util.HashMap;

import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceKey.TCBS_PS_OLD_ACC_105C;
import static com.tcbs.automation.config.tcbsprofileservice.TcbsProfileServiceKey.TCBS_PS_OLD_ACC_PASS;
import static com.tcbs.automation.publicmatcher.CombinableMatcher.all;
import static com.tcbs.automation.publicmatcher.HashMapMatcher.has;
import static net.serenitybdd.screenplay.GivenWhenThen.*;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom(value = "data/oldregister/SuccessOldRegisterBody.csv", separator = '|')
public class UpdateOldRegisterCustomerSuccessTest {
  private Actor user;
  private String TestCaseName;
  private String InputBody;
  private int ExpectedStatus;
  private String ExpectedMessage;
  private String tcbsId;
  private String userId;
  private HashMap<String, Object> inputBodyMap;


  @Before
  public void before() throws Exception {
    user = Actor.named("Focus Tester");
    givenThat(user).can(CallAllAPI.withProvidedInfo());
    givenThat(user).attemptsTo(LoginApi.withCredentials(TCBS_PS_OLD_ACC_105C, TCBS_PS_OLD_ACC_PASS));
  }

  @Test
  @TestCase(name = "#testCaseName")
  @Title("Update old registered customer with valid body")
  public void update_old_registered_customer_with_valid_body() {

    tcbsId = user.asksFor(TheUserInfo.aboutLoginData()).getTcbsid();
    when(user).attemptsTo(OldRegisterAPI.withInfo(tcbsId, InputBody));
    then(user).should(seeThat(
      String.format("Response status %s and corresponding error message", ExpectedStatus),
      TheOldRegisterQuestion.fromApi(),
      all(has("status", is(ExpectedStatus)))
        .and(has("message", is(ExpectedMessage))))
    );
    userId = user.asksFor(TheUserProfile.forTcbsUserfromDB(tcbsId)).getId().toString();
    then(user).should(seeThat(
      "Compare input body with database after updating successfully",
      TheOldRegisterQuestion.inputJsonToMap(InputBody),
      UpdatedOldCustomerMatcher.sameAs(user.asksFor(TheUserProfile.forTcbsUserfromDB(tcbsId)),
        user.asksFor(TheUserProfile.forAddressfromDB(userId)),
        user.asksFor(TheUserProfile.forIdentificationfromDB(userId)),
        user.asksFor(TheUserProfile.forBankfromDB(userId))))
    );

  }
}
