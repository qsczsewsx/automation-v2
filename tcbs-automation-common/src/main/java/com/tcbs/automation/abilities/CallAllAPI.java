package com.tcbs.automation.abilities;

import net.serenitybdd.screenplay.rest.abilities.CallAnApi;

public class CallAllAPI {
  public static CallAnApi withProvidedInfo() {
    return CallAnApi.at("");
  }
}
