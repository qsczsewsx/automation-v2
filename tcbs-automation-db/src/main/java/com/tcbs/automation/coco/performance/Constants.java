package com.tcbs.automation.coco.performance;

public class Constants {
  private Constants() {
  }

  public enum Ctype {
    INDUSTRY("INDUSTRY"),
    TICKER("TICKER"),
    INVALID_PARAM("INVALID_PARAM");

    private String value;

    Ctype(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }

  public static class Procedure {
    public static final String PROC_PE_GET_PERFORMANCE_BY_USER = "PROC_PE_GET_PERFORMANCE_BY_USER";
    public static final String PROC_PE_GET_LATEST_RISK_SCORE_BY_USER = "PROC_PE_GET_LATEST_12_MONTHS_RISK_SCORE_BY_USER";
    public static final String PROC_PE_GET_LATEST_RETURN_BY_USER = "PROC_PE_GET_LATEST_12_MONTHS_RETURNS_BY_USER";
    public static final String PROC_PE_GET_PORTFOLIO_PERCENT_BY_USER = "PROC_PE_GET_PORTFOLIO_PERCENT_BY_USER";
    public static final String PROC_PE_GET_PORTFOLIO_HISTORY_BY_USER = "PROC_PE_GET_PORTFOLIO_HISTORY_BY_USER";
    public static final String PROC_PE_GET_PERFORMANCE_BY_ACCOUNT = "PROC_PE_GET_PERFORMANCE_BY_ACCOUNT";
    public static final String PROC_PE_GET_LATEST_RISK_SCORE_BY_ACCOUNT = "PROC_PE_GET_LATEST_12_MONTHS_RISK_SCORE_BY_ACCOUNT";
    public static final String PROC_PE_GET_LATEST_RETURN_BY_ACCOUNT = "PROC_PE_GET_LATEST_12_MONTHS_RETURNS_BY_ACCOUNT";
    public static final String PROC_PE_GET_PORTFOLIO_PERCENT_BY_ACCOUNT = "PROC_PE_GET_PORTFOLIO_PERCENT_BY_ACCOUNT";
    public static final String PROC_PE_GET_PORTFOLIO_CASH_PERCENT_BY_ACCOUNT = "PROC_PE_GET_PORTFOLIO_CASH_PERCENT_BY_ACCOUNT";

    private Procedure() {
    }


  }
}
