package com.tcbs.automation.iris.bond;

import com.tcbs.automation.iris.AwsIRis;
import lombok.*;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "RISK_BOND_INFO")
public class RiskBondInfoEntity {
  @Id
  private String bondCode;
  private Long bondId;
  private String bondName;
  private Long bondTempId;
  private String bondTempCode;
  private Long bondCaseId;
  private String bondCaseName;
  private Long issuerId;
  private String issuerName;
  private String issuerShortName;
  private Long gaId;
  private Long groupIssuerIdDcm;
  private String groupIssuerNameDcm;
  private Long groupIssuerId;
  private String groupIssuerName;

  @Step("insert data")
  public static void insertData(RiskBondInfoEntity entity) {
    Session session = AwsIRis.AwsIRisDbConnection.getSession();
    Transaction trans = session.beginTransaction();
    StringBuilder queryStringBuilder = new StringBuilder();
    Query<?> query;
    if(entity.getGroupIssuerId() == null) {
      queryStringBuilder.append(" INSERT INTO RISK_BOND_INFO ");
      queryStringBuilder.append(
        "(BOND_CODE,BOND_ID,BOND_NAME,BOND_TEMP_ID,BOND_TEMP_CODE,BOND_CASE_ID,BOND_CASE_NAME,ISSUER_ID,ISSUER_NAME,ISSUER_SHORT_NAME,GA_ID,GROUP_ISSUER_ID_DCM,GROUP_ISSUER_NAME_DCM,UPDATED_DATE ) ");
      queryStringBuilder.append(" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
      query = session.createNativeQuery(queryStringBuilder.toString());
    } else {
      queryStringBuilder.append(" INSERT INTO RISK_BOND_INFO ");
      queryStringBuilder.append(
        "(BOND_CODE,BOND_ID,BOND_NAME,BOND_TEMP_ID,BOND_TEMP_CODE,BOND_CASE_ID,BOND_CASE_NAME,ISSUER_ID,ISSUER_NAME,ISSUER_SHORT_NAME,GA_ID,GROUP_ISSUER_ID_DCM,GROUP_ISSUER_NAME_DCM,UPDATED_DATE,GROUP_ISSUER_ID,GROUP_ISSUER_NAME ) ");
      queryStringBuilder.append(" values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
      query = session.createNativeQuery(queryStringBuilder.toString());
      query.setParameter(15, entity.getGroupIssuerId());
      query.setParameter(16, entity.getGroupIssuerName());
    }
    query.setParameter(1, entity.getBondCode());
    query.setParameter(2, entity.getBondId());
    query.setParameter(3, entity.getBondName());
    query.setParameter(4, entity.getBondTempId());
    query.setParameter(5, entity.getBondTempCode());
    query.setParameter(6, entity.getBondCaseId());
    query.setParameter(7, entity.getBondCaseName());
    query.setParameter(8, entity.getIssuerId());
    query.setParameter(9, entity.getIssuerName());
    query.setParameter(10, entity.getIssuerShortName());
    query.setParameter(11, entity.getGaId());
    query.setParameter(12, entity.getGroupIssuerIdDcm());
    query.setParameter(13, entity.getGroupIssuerNameDcm());
    query.setParameter(14, getMaxDate());
    query.executeUpdate();
    trans.commit();
    session.clear();
  }

  @Step("delete data by object")
  public static void deleteData(RiskBondInfoEntity entity) {
    Session session = AwsIRis.AwsIRisDbConnection.getSession();
    Transaction trans = session.beginTransaction();
    Query<?> query;

    query = session.createNativeQuery(" DELETE from RISK_BOND_INFO where BOND_CODE = :bondCode");
    query.setParameter("bondCode", entity.getBondCode());
    query.executeUpdate();
    trans.commit();
    session.clear();
  }

  @Step("get max date data in bond info table")
  public static LocalDateTime getMaxDate() {
    StringBuilder query = new StringBuilder();
    LocalDateTime maxDate = null;
    query.append(" select DISTINCT (UPDATED_DATE) from RISK_BOND_INFO where UPDATED_DATE  = (select max(UPDATED_DATE) from RISK_BOND_INFO) ");
    Session session = AwsIRis.AwsIRisDbConnection.getSession();
    try {

      List<Date> date =  session.createNativeQuery(query.toString()).getResultList();
      if(date != null && !date.isEmpty()) {
        String dateTime = date.get(0).toString().replace(".0", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        maxDate = LocalDateTime.parse(dateTime, formatter);
      } else {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        maxDate = LocalDateTime.parse(now.format(formatter), formatter);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    session.clear();
    return maxDate;
  }


  @Basic
  @Column(name = "BOND_CODE")
  public String getBondCode() {
    return bondCode;
  }

  public void setBondCode(String bondCode) {
    this.bondCode = bondCode;
  }

  @Basic
  @Column(name = "BOND_ID")
  public Long getBondId() {
    return bondId;
  }

  public void setBondId(Long bondId) {
    this.bondId = bondId;
  }

  @Basic
  @Column(name = "BOND_NAME")
  public String getBondName() {
    return bondName;
  }

  public void setBondName(String bondName) {
    this.bondName = bondName;
  }

  @Basic
  @Column(name = "BOND_TEMP_ID")
  public Long getBondTempId() {
    return bondTempId;
  }

  public void setBondTempId(Long bondTempId) {
    this.bondTempId = bondTempId;
  }

  @Basic
  @Column(name = "BOND_TEMP_CODE")
  public String getBondTempCode() {
    return bondTempCode;
  }

  public void setBondTempCode(String bondTempCode) {
    this.bondTempCode = bondTempCode;
  }

  @Basic
  @Column(name = "BOND_CASE_ID")
  public Long getBondCaseId() {
    return bondCaseId;
  }

  public void setBondCaseId(Long bondCaseId) {
    this.bondCaseId = bondCaseId;
  }

  @Basic
  @Column(name = "BOND_CASE_NAME")
  public String getBondCaseName() {
    return bondCaseName;
  }

  public void setBondCaseName(String bondCaseName) {
    this.bondCaseName = bondCaseName;
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
  @Column(name = "GA_ID")
  public Long getGaId() {
    return gaId;
  }

  public void setGaId(Long gaId) {
    this.gaId = gaId;
  }

  @Basic
  @Column(name = "GROUP_ISSUER_ID_DCM")
  public Long getGroupIssuerIdDcm() {
    return groupIssuerIdDcm;
  }

  public void setGroupIssuerIdDcm(Long groupIssuerIdDcm) {
    this.groupIssuerIdDcm = groupIssuerIdDcm;
  }

  @Basic
  @Column(name = "GROUP_ISSUER_NAME_DCM")
  public String getGroupIssuerNameDcm() {
    return groupIssuerNameDcm;
  }

  public void setGroupIssuerNameDcm(String groupIssuerNameDcm) {
    this.groupIssuerNameDcm = groupIssuerNameDcm;
  }

  @Basic
  @Column(name = "GROUP_ISSUER_ID")
  public Long getGroupIssuerId() {
    return groupIssuerId;
  }

  public void setGroupIssuerId(Long groupIssuerId) {
    this.groupIssuerId = groupIssuerId;
  }

  @Basic
  @Column(name = "GROUP_ISSUER_NAME")
  public String getGroupIssuerName() {
    return groupIssuerName;
  }

  public void setGroupIssuerName(String groupIssuerName) {
    this.groupIssuerName = groupIssuerName;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RiskBondInfoEntity that = (RiskBondInfoEntity) o;
    return Objects.equals(bondCode, that.bondCode) && Objects.equals(bondId, that.bondId) && Objects.equals(bondName, that.bondName) && Objects.equals(bondTempId,
      that.bondTempId) && Objects.equals(bondTempCode, that.bondTempCode) && Objects.equals(bondCaseId, that.bondCaseId) && Objects.equals(bondCaseName,
      that.bondCaseName) && Objects.equals(issuerId, that.issuerId) && Objects.equals(issuerName, that.issuerName) && Objects.equals(issuerShortName,
      that.issuerShortName) && Objects.equals(gaId, that.gaId) && Objects.equals(groupIssuerIdDcm, that.groupIssuerIdDcm) && Objects.equals(groupIssuerNameDcm,
      that.groupIssuerNameDcm) && Objects.equals(groupIssuerId, that.groupIssuerId) && Objects.equals(groupIssuerName, that.groupIssuerName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bondCode, bondId, bondName, bondTempId, bondTempCode, bondCaseId, bondCaseName, issuerId, issuerName, issuerShortName, gaId, groupIssuerIdDcm, groupIssuerNameDcm,
      groupIssuerId, groupIssuerName);
  }
}

