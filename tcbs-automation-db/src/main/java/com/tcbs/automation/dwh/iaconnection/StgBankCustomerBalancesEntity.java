package com.tcbs.automation.dwh.iaconnection;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;
import static com.tcbs.automation.dwh.Dwh.dwhDbConnection;

@Entity
@Table(name = "Stg_bank_CustomerBalances")
public class StgBankCustomerBalancesEntity {
  private int id;
  @Id
  private String custodyCd;
  private String bankAcctNo;
  private Double workingBalance;
  private Double availableBalance;
  private Double totalHeld;
  private String acctAcctRelStatus;
  private String acctRelType;
  private Timestamp createdDate;

  @Step
  public static List<StgBankCustomerBalancesEntity> getByListCus(List<String> cus) {
    Query<StgBankCustomerBalancesEntity> query = dwhDbConnection.getSession().createQuery(
      "from StgBankCustomerBalancesEntity a where a.custodyCd in :cus ", StgBankCustomerBalancesEntity.class);
    query.setParameter("cus", cus);
    List<StgBankCustomerBalancesEntity> result = query.getResultList();
    return result;
  }

  public static List<HashMap<String, Object>> getByCondition(List<String> cus) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append("select custodyCd, BankAccountNumber as bankAcctNo, workingBalance, availableBalance, totalHeld, acctAcctRelStatus, acctRelType ");
    queryStringBuilder.append("from Stg_bank_CustomerBalances ");
    queryStringBuilder.append("where CustodyCd in :cus ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("cus", cus)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("delete data by key")
  public static void deleteByCustody(String custody) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<StgBankCustomerBalancesEntity> query = session.createQuery(
      "DELETE FROM StgBankCustomerBalancesEntity i WHERE i.custodyCd = :custody");
    query.setParameter("custody", custody);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  @Basic
  @Column(name = "Id")
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "CustodyCd")
  public String getCustodyCd() {
    return custodyCd;
  }

  public void setCustodyCd(String custodyCd) {
    this.custodyCd = custodyCd;
  }

  @Basic
  @Column(name = "BankAccountNumber")
  public String getBankAcctNo() {
    return bankAcctNo;
  }

  public void setBankAcctNo(String bankAccountNumber) {
    this.bankAcctNo = bankAccountNumber;
  }

  @Basic
  @Column(name = "WorkingBalance")
  public Double getWorkingBalance() {
    return workingBalance;
  }

  public void setWorkingBalance(Double workingBalance) {
    this.workingBalance = workingBalance;
  }

  @Basic
  @Column(name = "AvailableBalance")
  public Double getAvailableBalance() {
    return availableBalance;
  }

  public void setAvailableBalance(Double availableBalance) {
    this.availableBalance = availableBalance;
  }

  @Basic
  @Column(name = "TotalHeld")
  public Double getTotalHeld() {
    return totalHeld;
  }

  public void setTotalHeld(Double totalHeld) {
    this.totalHeld = totalHeld;
  }

  @Basic
  @Column(name = "AcctAcctRelStatus")
  public String getAcctAcctRelStatus() {
    return acctAcctRelStatus;
  }

  public void setAcctAcctRelStatus(String acctAcctRelStatus) {
    this.acctAcctRelStatus = acctAcctRelStatus;
  }

  @Basic
  @Column(name = "AcctRelType")
  public String getAcctRelType() {
    return acctRelType;
  }

  public void setAcctRelType(String acctRelType) {
    this.acctRelType = acctRelType;
  }

  @Basic
  @Column(name = "CreatedDate")
  public Timestamp getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Timestamp createdDate) {
    this.createdDate = createdDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StgBankCustomerBalancesEntity that = (StgBankCustomerBalancesEntity) o;
    return id == that.id &&
      Objects.equals(custodyCd, that.custodyCd) &&
      Objects.equals(bankAcctNo, that.bankAcctNo) &&
      Objects.equals(workingBalance, that.workingBalance) &&
      Objects.equals(availableBalance, that.availableBalance) &&
      Objects.equals(totalHeld, that.totalHeld) &&
      Objects.equals(acctAcctRelStatus, that.acctAcctRelStatus) &&
      Objects.equals(acctRelType, that.acctRelType) &&
      Objects.equals(createdDate, that.createdDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, custodyCd, bankAcctNo, workingBalance, availableBalance, totalHeld, acctAcctRelStatus, acctRelType, createdDate);
  }
}
