package com.tcbs.automation.iris.bond;

import com.tcbs.automation.staging.AwsStagingDwh;
import lombok.*;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "Stg_risk_InvestmentLimit_CorpBond_Final")
public class InvestmentCorpBondFinalEntity {
  @Id
  private String bondCode;
  private Double bondTempLimit;
  private Double bondBalance;
  private Double bondTempBalance;
  private Double bondTempRemain;
  private Double remain;

  @Step("insert data")
  public static void insertData(InvestmentCorpBondFinalEntity entity) {
    Session session = AwsStagingDwh.awsStagingDwhDbConnection.getSession();
    Transaction trans = session.beginTransaction();
    StringBuilder queryStringBuilder = new StringBuilder();
    Query<?> query;
    queryStringBuilder.append(" INSERT INTO Stg_risk_InvestmentLimit_CorpBond_Final ( ");
    queryStringBuilder.append(" Bond_Code,  Bond_Balance, Bond_Temp_Balance, ");
    queryStringBuilder.append(" Bond_Temp_Limit, Bond_Temp_Remain, ");
    queryStringBuilder.append(" Remain, Updated_Date) ");
    queryStringBuilder.append(" values (?, ?, ?, ?, ?, ?, ? )");
    query = session.createNativeQuery(queryStringBuilder.toString());
    query.setParameter(1, entity.getBondCode());
    query.setParameter(2, entity.getBondBalance());
    query.setParameter(3, entity.getBondTempBalance());
    query.setParameter(4, entity.getBondTempLimit());
    query.setParameter(5, entity.getBondTempRemain());
    query.setParameter(6, entity.getRemain());
    query.setParameter(7, getMaxDate());
    query.executeUpdate();
    trans.commit();
    session.clear();
  }

  @Step("delete data by object")
  public static void deleteData(InvestmentCorpBondFinalEntity entity) {
    Session session = AwsStagingDwh.awsStagingDwhDbConnection.getSession();
    Transaction trans = session.beginTransaction();
    Query<?> query;
    query = session.createNativeQuery(" DELETE from Stg_risk_InvestmentLimit_CorpBond_Final where Bond_Code = :bondCode");
    query.setParameter("bondCode", entity.getBondCode());
    query.executeUpdate();
    trans.commit();
    session.clear();
  }

  @Step("get max date data in bond info table")
  public static LocalDateTime getMaxDate() {
    StringBuilder query = new StringBuilder();
    LocalDateTime maxDate = null;
    query.append(" select DISTINCT (Updated_Date) from Stg_risk_InvestmentLimit_CorpBond_Final where Updated_Date  = (select max(Updated_Date) from Stg_risk_InvestmentLimit_CorpBond_Final) ");
    Session session = AwsStagingDwh.awsStagingDwhDbConnection.getSession();
    try {
      List<Date> date = session.createNativeQuery(query.toString()).getResultList();
      if (date != null && !date.isEmpty()) {
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
  @Column(name = "Bond_Code")
  public String getBondCode() {
    return bondCode;
  }

  public void setBondCode(String bondCode) {
    this.bondCode = bondCode;
  }

  @Basic
  @Column(name = "Bond_Temp_Limit")
  public Double getBondTempLimit() {
    return bondTempLimit;
  }

  public void setBondTempLimit(Double bondTempLimit) {
    this.bondTempLimit = bondTempLimit;
  }

  @Basic
  @Column(name = "Bond_Temp_Balance")
  public Double getBondTempBalance() {
    return bondTempBalance;
  }

  public void setBondTempBalance(Double bondTempBalance) {
    this.bondTempBalance = bondTempBalance;
  }

  @Basic
  @Column(name = "Bond_Temp_Remain")
  public Double getBondTempRemain() {
    return bondTempRemain;
  }

  public void setBondTempRemain(Double bondTempRemain) {
    this.bondTempRemain = bondTempRemain;
  }

  @Basic
  @Column(name = "Remain")
  public Double getRemain() {
    return remain;
  }

  public void setRemain(Double remain) {
    this.remain = remain;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    InvestmentCorpBondFinalEntity that = (InvestmentCorpBondFinalEntity) o;
    return Objects.equals(bondCode, that.bondCode) && Objects.equals(bondBalance,
      that.bondBalance) && Objects.equals(bondTempBalance, that.bondTempBalance) && Objects.equals(bondTempLimit, that.bondTempLimit) && Objects.equals(bondTempRemain,
      that.bondTempRemain) && Objects.equals(remain, that.remain);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bondCode, bondBalance, bondTempBalance, bondTempLimit, bondTempRemain, remain);
  }
}