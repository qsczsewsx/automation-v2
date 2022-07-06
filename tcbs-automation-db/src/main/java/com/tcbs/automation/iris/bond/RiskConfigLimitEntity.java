package com.tcbs.automation.iris.bond;

import com.tcbs.automation.iris.AwsIRis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "RISK_CONFIG_LIMIT")
public class RiskConfigLimitEntity {
  @Id
  private long id;
  private Long issuerId;
  private String issuerName;
  private String issuerShortName;
  private String issuerType;
  private Long issuerGaId;
  private Long issuerGaGroupId;
  private String issuerGaGroupName;
  private Double currentLimit;
  private String status;

  @Step("insert data")
  public static void insertData(RiskConfigLimitEntity entity) {
    Session session = AwsIRis.AwsIRisDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    StringBuilder queryStringBuilder = new StringBuilder();


    queryStringBuilder.append(" INSERT INTO RISK_CONFIG_LIMIT ");
    queryStringBuilder.append(
      "(ID, ISSUER_ID, ISSUER_NAME, ISSUER_SHORT_NAME, ISSUER_TYPE, ISSUER_GA_ID, ISSUER_GA_GROUP_ID, ISSUER_GA_GROUP_NAME, CURRENT_LIMIT, STATUS ) ");
    queryStringBuilder.append(" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

    Query<?> query = session.createNativeQuery(queryStringBuilder.toString());
    query.setParameter(1, entity.getId());
    query.setParameter(2, entity.getIssuerId());
    query.setParameter(3, entity.getIssuerName());
    query.setParameter(4, entity.getIssuerShortName());
    query.setParameter(5, entity.getIssuerType());
    query.setParameter(6, entity.getIssuerGaId());
    query.setParameter(7, entity.getIssuerGaGroupId());
    query.setParameter(8, entity.getIssuerGaGroupName());
    query.setParameter(9, entity.getCurrentLimit());
    query.setParameter(10, entity.getStatus());
    query.executeUpdate();
    trans.commit();
    System.out.println(query);
  }

  @Step("delete data by object")
  public static void deleteData(RiskConfigLimitEntity entity) {
    Session session = AwsIRis.AwsIRisDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query;

    query = session.createNativeQuery(" DELETE from RISK_CONFIG_LIMIT where ISSUER_GA_ID = :issuerGaId");
    query.setParameter("issuerGaId", entity.getIssuerGaId());
    query.executeUpdate();
    trans.commit();
  }


  @Basic
  @Column(name = "ID")
  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @Basic
  @Column(name = "ISSUER_ID")
  public Long getIssuerId() {
    return issuerId;
  }

  public void setIssuerId(Long issuerId) {
    this.issuerId = issuerId;
  }

  @Basic
  @Column(name = "ISSUER_NAME")
  public String getIssuerName() {
    return issuerName;
  }

  public void setIssuerName(String issuerName) {
    this.issuerName = issuerName;
  }

  @Basic
  @Column(name = "ISSUER_SHORT_NAME")
  public String getIssuerShortName() {
    return issuerShortName;
  }

  public void setIssuerShortName(String issuerShortName) {
    this.issuerShortName = issuerShortName;
  }

  @Basic
  @Column(name = "ISSUER_TYPE")
  public String getIssuerType() {
    return issuerType;
  }

  public void setIssuerType(String issuerType) {
    this.issuerType = issuerType;
  }

  @Basic
  @Column(name = "ISSUER_GA_ID")
  public Long getIssuerGaId() {
    return issuerGaId;
  }

  public void setIssuerGaId(Long issuerGaId) {
    this.issuerGaId = issuerGaId;
  }

  @Basic
  @Column(name = "ISSUER_GA_GROUP_ID")
  public Long getIssuerGaGroupId() {
    return issuerGaGroupId;
  }

  public void setIssuerGaGroupId(Long issuerGaGroupId) {
    this.issuerGaGroupId = issuerGaGroupId;
  }

  @Basic
  @Column(name = "ISSUER_GA_GROUP_NAME")
  public String getIssuerGaGroupName() {
    return issuerGaGroupName;
  }

  public void setIssuerGaGroupName(String issuerGaGroupName) {
    this.issuerGaGroupName = issuerGaGroupName;
  }

  @Basic
  @Column(name = "CURRENT_LIMIT")
  public Double getCurrentLimit() {
    return currentLimit;
  }

  public void setCurrentLimit(Double currentLimit) {
    this.currentLimit = currentLimit;
  }

  @Basic
  @Column(name = "STATUS")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RiskConfigLimitEntity that = (RiskConfigLimitEntity) o;
    return id == that.id && Objects.equals(issuerId, that.issuerId) && Objects.equals(issuerName, that.issuerName) && Objects.equals(issuerShortName,
      that.issuerShortName) && Objects.equals(issuerType, that.issuerType) && Objects.equals(issuerGaId, that.issuerGaId) && Objects.equals(issuerGaGroupId,
      that.issuerGaGroupId) && Objects.equals(issuerGaGroupName, that.issuerGaGroupName) && Objects.equals(currentLimit, that.currentLimit) && Objects.equals(status,
      that.status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, issuerId, issuerName, issuerShortName, issuerType, issuerGaId, issuerGaGroupId, issuerGaGroupName, currentLimit, status);
  }
}
