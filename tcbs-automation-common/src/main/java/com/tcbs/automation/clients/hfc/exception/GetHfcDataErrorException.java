package com.tcbs.automation.clients.hfc.exception;

import com.tcbs.automation.clients.hfc.dto.HfcErrorModel;
import lombok.Data;

@Data
public class GetHfcDataErrorException extends GetHfcDataException {
  private final HfcErrorModel error;

  public GetHfcDataErrorException(HfcErrorModel error) {
    super(error.getMessage());
    this.error = error;
  }

  public String getErrorMessage() {
    if (error == null) {
      return this.getMessage();
    }
    return error.getMessage();
  }
}
