package com.tcbs.automation.tcbond;

import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "Issuer")
public class Issuer {

  @Id
  @Column(name = "ID")
  private Long id;
  @Column(name = "Name")
  private String name;
  @Column(name = "BusinessNo")
  private String businessNo;
  @Column(name = "BusinessDate")
  private java.sql.Timestamp businessDate;
  @Column(name = "BusinessBy")
  private String businessBy;
  @Column(name = "RepresentUser")
  private String representUser;
  @Column(name = "RepersentRole")
  private String repersentRole;
  @Column(name = "Phone")
  private String phone;
  @Column(name = "Fax")
  private String fax;
  @Column(name = "Address")
  private String address;
  @Column(name = "Email")
  private String email;
  @Column(name = "CreatedDate")
  private java.sql.Timestamp createdDate;
  @Column(name = "UpdateDate")
  private java.sql.Timestamp updateDate;
  @Column(name = "Active")
  private Long active;
  @Column(name = "Type")
  private Long type;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }


  public String getBusinessNo() {
    return businessNo;
  }

  public void setBusinessNo(String businessNo) {
    this.businessNo = businessNo;
  }


  public java.sql.Timestamp getBusinessDate() {
    return businessDate;
  }

  public void setBusinessDate(java.sql.Timestamp businessDate) {
    this.businessDate = businessDate;
  }


  public String getBusinessBy() {
    return businessBy;
  }

  public void setBusinessBy(String businessBy) {
    this.businessBy = businessBy;
  }


  public String getRepresentUser() {
    return representUser;
  }

  public void setRepresentUser(String representUser) {
    this.representUser = representUser;
  }


  public String getRepersentRole() {
    return repersentRole;
  }

  public void setRepersentRole(String repersentRole) {
    this.repersentRole = repersentRole;
  }


  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }


  public String getFax() {
    return fax;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }


  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }


  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }


  public java.sql.Timestamp getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(java.sql.Timestamp createdDate) {
    this.createdDate = createdDate;
  }


  public java.sql.Timestamp getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(java.sql.Timestamp updateDate) {
    this.updateDate = updateDate;
  }


  public Long getActive() {
    return active;
  }

  public void setActive(Long active) {
    this.active = active;
  }


  public Long getType() {
    return type;
  }

  public void setType(Long type) {
    this.type = type;
  }

  @SuppressWarnings("unchecked")
  @Step("Get all bond issuers from TCbond.Issuer")
  public List<Issuer> getAllIssuers() {
    Query<Issuer> query = TcBond.tcBondDbConnection.getSession().createQuery("from Issuer");
    List<Issuer> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  @Step("Get active bond issuers from TCbond.Issuer")
  public List<Issuer> getActiveIssuers() {
    Query<Issuer> query = TcBond.tcBondDbConnection.getSession().createQuery("from Issuer where active = 1");
    List<Issuer> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }
}
