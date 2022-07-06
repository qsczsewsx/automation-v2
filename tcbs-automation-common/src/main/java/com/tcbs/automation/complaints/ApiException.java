package com.tcbs.automation.complaints;

import io.restassured.response.Response;

public class ApiException extends Exception {
  private Response response;

  public ApiException(Response response, String message) {
    super(message);
    this.response = response;
  }

  public Response getHttpResponse() {
    return response;
  }
}
