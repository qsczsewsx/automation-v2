package com.automation.login;

import com.automation.publicmatcher.Verifier;
import com.automation.complaints.UnauthenticatedUser;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.GivenWhenThen;
import net.serenitybdd.screenplay.Question;

import static com.automation.publicmatcher.Verifier.verify;
import static org.hamcrest.Matchers.*;

public class TheRMRole implements Question<Boolean> {
  public static TheRMRole isGranted() {
    return new TheRMRole();
  }

  @Override
  public Boolean answeredBy(Actor actor) {
    actor.should(
      GivenWhenThen.seeThat("<User info>",
        TheUserInfo.aboutLoginData(), notNullValue()).orComplainWith(UnauthenticatedUser.class)
    );

    return Verifier.verify(TheUserInfo.aboutUserRoles().answeredBy(actor), hasItem(equalToIgnoringCase("rm")));
  }

}
