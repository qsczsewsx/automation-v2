package com.tcbs.automation.iris.bond;

import com.tcbs.automation.iris.AwsIRis;
import com.tcbs.automation.staging.AwsStagingDwh;
import lombok.*;
import net.sf.cglib.core.Local;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "Stg_risk_InvestmentLimit_CorpBond_OnGoing")
public class InvestmentCorpBondOnGoingEntity {
  @Id
  private Double id;
  private String instanceId;
  private String bondCode;
  private Double totalAmount;
  private String processStatus;
  private String side;
  private String tradingDate;
  private String issueDate;
  private static final String FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";

  @Step("insert data")
  public static void insertData(InvestmentCorpBondOnGoingEntity entity) {
    Session session = AwsStagingDwh.awsStagingDwhDbConnection.getSession();
    Transaction trans = session.beginTransaction();
    StringBuilder queryStringBuilder = new StringBuilder();
    Query<?> query;
    queryStringBuilder.append(" INSERT INTO Stg_risk_InvestmentLimit_CorpBond_OnGoing (");
    queryStringBuilder.append(" id,  instanceId, bondCode, totalAmount, processStatus, side, ");
    queryStringBuilder.append(" tradingDate, issueDate, updateDate) ");
    queryStringBuilder.append(" values (?, ?, ?, ?, ?, ?, ?, ?, ? )");
    query = session.createNativeQuery(queryStringBuilder.toString());
    query.setParameter(1, entity.getId());
    query.setParameter(2, entity.getInstanceId());
    query.setParameter(3, entity.getBondCode());
    query.setParameter(4, entity.getTotalAmount());
    query.setParameter(5, entity.getProcessStatus());
    query.setParameter(6, entity.getSide());
    query.setParameter(7, mappingDate(entity.getTradingDate()));
    query.setParameter(8, mappingDate(entity.getIssueDate()));
    query.setParameter(9, getMaxDate());
    query.executeUpdate();
    trans.commit();
    session.clear();
  }

  @Step("delete data by object")
  public static void deleteData(InvestmentCorpBondOnGoingEntity entity) {
    Session session = AwsStagingDwh.awsStagingDwhDbConnection.getSession();
    Transaction trans = session.beginTransaction();
    Query<?> query;

    query = session.createNativeQuery(" DELETE from Stg_risk_InvestmentLimit_CorpBond_OnGoing where bondCode = :bondCode");
    query.setParameter("bondCode", entity.getBondCode());
    query.executeUpdate();
    trans.commit();
    session.clear();
  }

  @Step("get max date data in bond info table")
  public static LocalDateTime getMaxDate() {
    StringBuilder query = new StringBuilder();
    LocalDateTime maxDate = null;
    query.append(" select DISTINCT (updateDate) from Stg_risk_InvestmentLimit_CorpBond_OnGoing where updateDate  = (select max(updateDate) from Stg_risk_InvestmentLimit_CorpBond_OnGoing) ");
    Session session = AwsStagingDwh.awsStagingDwhDbConnection.getSession();
    try {
      List<Date> date = session.createNativeQuery(query.toString()).getResultList();
      if (date != null && !date.isEmpty()) {
        String dateTime = date.get(0).toString().replace(".0", "");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
        maxDate = LocalDateTime.parse(dateTime, formatter);
      } else {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
        maxDate = LocalDateTime.parse(now.format(formatter), formatter);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    session.clear();
    return maxDate;
  }

  @Step("mapping date in test data")
  public static LocalDateTime mappingDate(String date) {
    LocalDateTime dateTime;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
    switch (date) {
      case "yesterday":
        String yesterday = new SimpleDateFormat(FORMAT_DATE).format(new Date(new Date().getTime() - 86400000));
        dateTime = LocalDateTime.parse(yesterday, formatter);
        break;
      case "tomorrow":
        String tomorrow = new SimpleDateFormat(FORMAT_DATE).format(new Date(new Date().getTime() + 86400000));
        dateTime = LocalDateTime.parse(tomorrow, formatter);
        break;
      default:
        LocalDateTime now = LocalDateTime.now();
        dateTime = LocalDateTime.parse(now.format(formatter), formatter);
    }
    return dateTime;
  }


  @Basic
  @Column(name = "bondCode")
  public String getBondCode() {
    return bondCode;
  }

  public void setBondCode(String bondCode) {
    this.bondCode = bondCode;
  }


  @Basic
  @Column(name = "id")
  public Double getId() {
    return id;
  }

  public void setId(Double id) {
    this.id = id;
  }

  @Basic
  @Column(name = "instanceId")
  public String getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(String instanceId) {
    this.instanceId = instanceId;
  }


  @Basic
  @Column(name = "totalAmount")
  public Double getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(Double totalAmount) {
    this.totalAmount = totalAmount;
  }


  @Basic
  @Column(name = "processStatus")
  public String getProcessStatus() {
    return processStatus;
  }

  public void setProcessStatus(String processStatus) {
    this.processStatus = processStatus;
  }

  @Basic
  @Column(name = "side")
  public String getSide() {
    return side;
  }

  public void setSide(String side) {
    this.side = side;
  }

  @Basic
  @Column(name = "tradingDate")
  public String getTradingDate() {
    return tradingDate;
  }

  public void setTradingDate(String tradingDate) {
    this.tradingDate = tradingDate;
  }


  @Basic
  @Column(name = "issueDate")
  public String getIssueDate() {
    return issueDate;
  }

  public void setIssueDate(String issueDate) {
    this.issueDate = issueDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InvestmentCorpBondOnGoingEntity that = (InvestmentCorpBondOnGoingEntity) o;
    return Objects.equals(bondCode, that.bondCode) && Objects.equals(id, that.id) && Objects.equals(instanceId, that.instanceId) && Objects.equals(totalAmount,
      that.totalAmount) && Objects.equals(processStatus, that.processStatus) && Objects.equals(side, that.side) && Objects.equals(tradingDate,
      that.tradingDate) && Objects.equals(issueDate, that.issueDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bondCode, id, instanceId, totalAmount, processStatus, side, tradingDate, issueDate);
  }
}

