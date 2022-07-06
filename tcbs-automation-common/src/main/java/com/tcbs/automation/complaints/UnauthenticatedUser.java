package com.tcbs.automation.complaints;

import net.serenitybdd.core.exceptions.CausesCompromisedTestFailure;

public class UnauthenticatedUser extends AssertionError implements CausesCompromisedTestFailure {

  public UnauthenticatedUser(String message, Throwable cause) {
    super(message, cause);
  }

  public UnauthenticatedUser(Throwable cause) {
    super(cause);
  }

  public UnauthenticatedUser(String message) {
    super(message);
  }
}
