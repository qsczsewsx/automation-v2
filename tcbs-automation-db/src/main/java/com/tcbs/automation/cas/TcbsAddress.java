package com.tcbs.automation.cas;

import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static com.tcbs.automation.cas.CAS.casConnection;

@Entity
@Table(name = "TCBS_ADDRESS")
public class TcbsAddress {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "USER_ID")
  private String userId;
  @Column(name = "ADDRESS")
  private String address;
  @Column(name = "DIVISION_1")
  private String division1;
  @Column(name = "DIVISION_2")
  private String division2;
  @Column(name = "DIVISION_3")
  private String division3;
  @Column(name = "DIVISION_4")
  private String division4;
  @Column(name = "DIVISION_5")
  private String division5;
  @Column(name = "DIVISION_6")
  private String division6;
  @Column(name = "ADDRESS_TYPE")
  private String addressType;
  @Column(name = "ORIGINAL_ID")
  private String originalId;
  @Column(name = "PERMANENT_ADDRESS")
  private String permanentAddress;

  @Step
  public static TcbsAddress getByTcbsAddress(String userId) {
    Query<TcbsAddress> query = CAS.casConnection.getSession().createQuery(
      "from TcbsAddress a where a.userId=:userId", TcbsAddress.class);
    query.setParameter("userId", userId);
    return query.getSingleResult();
  }

  public static void updateAddress(String userId, String address, String division2) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    org.hibernate.query.Query query = casConnection.getSession().createQuery(
      "Update TcbsAddress a set a.address =:address, a.division2 =: division2 where a.userId=:userId");
    query.setParameter("userId", userId);
    query.setParameter("address", address);
    query.setParameter("division2", division2);
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getDivision1() {
    return division1;
  }

  public void setDivision1(String division1) {
    this.division1 = division1;
  }

  public String getDivision2() {
    return division2;
  }

  public void setDivision2(String division2) {
    this.division2 = division2;
  }

  public String getDivision3() {
    return division3;
  }

  public void setDivision3(String division3) {
    this.division3 = division3;
  }

  public String getDivision4() {
    return division4;
  }

  public void setDivision4(String division4) {
    this.division4 = division4;
  }

  public String getDivision5() {
    return division5;
  }

  public void setDivision5(String division5) {
    this.division5 = division5;
  }

  public String getDivision6() {
    return division6;
  }

  public void setDivision6(String division6) {
    this.division6 = division6;
  }

  public String getAddressType() {
    return addressType;
  }

  public void setAddressType(String addressType) {
    this.addressType = addressType;
  }

  public String getOriginalId() {
    return originalId;
  }

  public void setOriginalId(String originalId) {
    this.originalId = originalId;
  }

  public String getPermanentAddress() {
    return permanentAddress;
  }

  public void setPermanentAddress(String permanentAddress) {
    this.permanentAddress = permanentAddress;
  }

}

