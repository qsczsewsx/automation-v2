package com.tcbs.automation.wait;

import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.MatcherAssert.assertThat;

public class Wait implements Task {

  private static Logger logger = LoggerFactory.getLogger(Wait.class);
  private int milliseconds;

  public Wait(int milliSeconds) {
    this.milliseconds = milliSeconds;
  }

  public static Wait forTime(int milliseconds) {
    return Instrumented.instanceOf(Wait.class).withProperties(milliseconds);
  }

  @Override
  @Step("{0} waits for #milliseconds")
  public synchronized <T extends Actor> void performAs(T t) {
    try {
      wait(milliseconds);
    } catch (Exception e) {
      logger.info(e.getMessage());
      assertThat("Sleep fail due to exception", false);
    }
  }
}
