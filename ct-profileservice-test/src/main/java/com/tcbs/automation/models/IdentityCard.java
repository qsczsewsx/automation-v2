package com.tcbs.automation.models;

public class IdentityCard {
  private String idNumber;
  private String idPlace;
  private String idDate;
  private String idUrl;

  // Getter Methods

  public String getIdNumber() {
    return idNumber;
  }

  public String getIdPlace() {
    return idPlace;
  }

  public String getIdDate() {
    return idDate;
  }

  public String getIdUrl() {
    return idUrl;
  }

  // Setter Methods

  public void setIdNumber(String idNumber) {
    this.idNumber = idNumber;
  }

  public void setIdPlace(String idPlace) {
    this.idPlace = idPlace;
  }

  public void setIdDate(String idDate) {
    this.idDate = idDate;
  }

  public void setIdUrl(String idUrl) {
    this.idUrl = idUrl;
  }
}