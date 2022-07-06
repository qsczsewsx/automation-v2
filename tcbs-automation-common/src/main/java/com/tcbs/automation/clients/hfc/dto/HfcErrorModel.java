package com.tcbs.automation.clients.hfc.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class HfcErrorModel implements Serializable {
  private String error;
  private String message;
}
