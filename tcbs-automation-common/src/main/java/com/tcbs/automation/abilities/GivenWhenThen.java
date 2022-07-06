package com.tcbs.automation.abilities;

import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.PerformsTasks;

public class GivenWhenThen extends net.serenitybdd.screenplay.GivenWhenThen {
  public static <T extends PerformsTasks> T givenThat(T actor) {
    return actor;
  }

  public static Actor andThat(Actor actor) {
    return actor;
  }

  public static Actor when(Actor actor) {
    return actor;
  }

  public static Actor and(Actor actor) {
    return actor;
  }
}
