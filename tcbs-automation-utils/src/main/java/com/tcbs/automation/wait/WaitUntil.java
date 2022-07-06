package com.tcbs.automation.wait;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.SilentInteraction;
import org.hamcrest.Matcher;

import static com.tcbs.automation.publicmatcher.Verifier.verify;

public class WaitUntil extends SilentInteraction {
  private Question question;
  private Matcher matcher;
  private int timeout;

  public WaitUntil(Question question, Matcher matcher) {
    this.question = question;
    this.matcher = matcher;
  }

  public static WaitUntil the(Question object, Matcher matcher) {
    return new WaitUntil(object, matcher);
  }

  public WaitUntil forNoLongerThan(int timeout) {
    this.timeout = timeout;
    return this;
  }

  public WaitUntil seconds() {
    this.timeout = this.timeout * 1000;
    return this;
  }

  public WaitUntil miliseconds() {
    return this;
  }

  @Override
  public <T extends Actor> void performAs(T actor) {
    int retry = 10;
    int waitTime = timeout / retry;
    while (retry > 0) {
      if (verify(actor.asksFor(question), matcher)) {
        break;
      } else {
        retry--;
        actor.attemptsTo(Wait.forTime(waitTime));
      }
    }
  }
}
