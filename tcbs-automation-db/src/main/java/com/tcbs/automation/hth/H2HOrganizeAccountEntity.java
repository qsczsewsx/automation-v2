package com.tcbs.automation.hth;

import org.hibernate.Session;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "H2H_ORGANIZE_ACCOUNT")
public class H2HOrganizeAccountEntity {
  private int id;
  private String creditAccountNo;
  private String formattedAccountName;
  private String fullNameE;
  private String fullNameV;
  private String status;
  private Timestamp createdTime;
  private Timestamp updatedTime;
  private String custodyCode;

  //by Lybtk
  public static void deleteByTrustedAccount(String creditAccountNo) {
    Session session = HthDb.h2hConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createQuery(
      "delete from H2HOrganizeAccountEntity where creditAccountNo=:creditAccountNo"
    );
    query.setParameter("creditAccountNo", creditAccountNo);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  //by Lybtk
  public static Integer getIdFromBankAcount(String creditAccountNo) {
    try {
      HthDb.h2hConnection.getSession().clear();
      org.hibernate.query.Query<Integer> query = HthDb.h2hConnection.getSession().createQuery(
        "select id from H2HOrganizeAccountEntity where creditAccountNo=:creditAccountNo"
        , Integer.class);
      query.setParameter("creditAccountNo", creditAccountNo);
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  //by Lybtk
  public static String getStatusFromId(String id) {
    try {
      HthDb.h2hConnection.getSession().clear();
      org.hibernate.query.Query<String> query = HthDb.h2hConnection.getSession().createQuery(
        "select status from H2HOrganizeAccountEntity where id=:id"
        , String.class);
      query.setParameter("id", Integer.parseInt(id));
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  @Id
  @Column(name = "ID")
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "CREDIT_ACCOUNT_NO")
  public String getCreditAccountNo() {
    return creditAccountNo;
  }

  public void setCreditAccountNo(String creditAccountNo) {
    this.creditAccountNo = creditAccountNo;
  }

  @Basic
  @Column(name = "FORMATTED_ACCOUNT_NAME")
  public String getFormattedAccountName() {
    return formattedAccountName;
  }

  public void setFormattedAccountName(String formattedAccountName) {
    this.formattedAccountName = formattedAccountName;
  }

  @Basic
  @Column(name = "FULL_NAME_E")
  public String getFullNameE() {
    return fullNameE;
  }

  public void setFullNameE(String fullNameE) {
    this.fullNameE = fullNameE;
  }

  @Basic
  @Column(name = "FULL_NAME_V")
  public String getFullNameV() {
    return fullNameV;
  }

  public void setFullNameV(String fullNameV) {
    this.fullNameV = fullNameV;
  }

  @Basic
  @Column(name = "STATUS")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Basic
  @Column(name = "CREATED_TIME")
  public Timestamp getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Timestamp createdTime) {
    this.createdTime = createdTime;
  }

  @Basic
  @Column(name = "UPDATED_TIME")
  public Timestamp getUpdatedTime() {
    return updatedTime;
  }

  public void setUpdatedTime(Timestamp updatedTime) {
    this.updatedTime = updatedTime;
  }

  @Basic
  @Column(name = "CUSTODY_CODE")
  public String getCustodyCode() {
    return custodyCode;
  }

  public void setCustodyCode(String custodyCode) {
    this.custodyCode = custodyCode;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    H2HOrganizeAccountEntity that = (H2HOrganizeAccountEntity) o;
    return id == that.id &&
      Objects.equals(creditAccountNo, that.creditAccountNo) &&
      Objects.equals(formattedAccountName, that.formattedAccountName) &&
      Objects.equals(fullNameE, that.fullNameE) &&
      Objects.equals(fullNameV, that.fullNameV) &&
      Objects.equals(status, that.status) &&
      Objects.equals(createdTime, that.createdTime) &&
      Objects.equals(updatedTime, that.updatedTime) &&
      Objects.equals(custodyCode, that.custodyCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, creditAccountNo, formattedAccountName, fullNameE, fullNameV, status, createdTime, updatedTime, custodyCode);
  }
}
