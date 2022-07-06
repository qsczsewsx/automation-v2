package com.tcbs.automation.clients.hfc.exception;

import lombok.Data;

@Data
public class GetHfcDataInvalidReponseException extends GetHfcDataException {
  private String response;

  public GetHfcDataInvalidReponseException(String msg, String reponse) {
    super(msg);
    this.response = reponse;
  }
}
