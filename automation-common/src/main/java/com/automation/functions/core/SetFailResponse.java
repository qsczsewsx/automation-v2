package com.automation.functions.core;


public class SetFailResponse {
  public static FailResponse withCodeAndMessage(String code, String message) {
    FailResponse failResponse = new FailResponse();
    failResponse.setCode(code);
    failResponse.setMessage(message);
    return failResponse;
  }
}
