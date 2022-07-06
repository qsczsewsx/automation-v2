package com.tcbs.automation.dwh;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "DailyPort_CashBal")
public class DailyPortCashBalEntity {
  @Id
  private Timestamp txDate;
  private String custodycd;
  private BigDecimal balance;
  private Long cashReceivingT0;
  private Long cashSendingT0;
  private Long cashReceivingT1;
  private Long cashSendingT1;
  private Long cashReceivingT2;
  private Long cashSendingT2;
  private Long cashReceivingT3;
  private Long cashSendingT3;
  private Long cashReceivingTn;
  private BigDecimal totalBalance;
  private Integer bankavlbal;
  private Date bankinqirydt;


  public static List<HashMap<String, Object>> getByCondition(String fromDate, String toDate, String minCashBalance) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append("select * from ( ");
    queryStringBuilder.append("  select CONVERT(varchar(10), cast(TXDATE as date), 23) as dateReport, custodyCD, balance as cashValue ");
    queryStringBuilder.append("  from DailyPort_CashBal ");
    queryStringBuilder.append("  where TxDate >= :fromDate ");
    queryStringBuilder.append("  and TxDate < dateadd(day, 1, :toDate)) cash ");
    queryStringBuilder.append("where cashValue >= :minCashBalance order by dateReport, custodyCD ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setParameter("minCashBalance", minCashBalance)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get latest tnxDate")
  public static java.util.Date getLatestTxnDate() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT cast(MAX(TXDATE) as date) as latestDate ");
    queryStringBuilder.append(" FROM DailyPort_CashBal   ");

    try {
      List<java.util.Date> result = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .getResultList();
      java.util.Date latestDate = result.get(0);
      return latestDate;
    } catch (Exception ex) {

      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }


  @Step("insert data")
  public void saveCashBalance(DailyPortCashBalEntity entity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query = session.createNativeQuery("insert into DailyPort_CashBal " +
      "(balance , CASH_RECEIVING_T0 , CASH_RECEIVING_T1 , CASH_RECEIVING_T2 , CASH_RECEIVING_T3 , CASH_RECEIVING_TN , CASH_SENDING_T0, " +
      " CASH_SENDING_T1 , CASH_SENDING_T2 , CASH_SENDING_T3 , custodycd , TotalBalance , TxDate ) " +
      "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    query.setParameter(1, entity.getBalance());
    query.setParameter(2, entity.getCashReceivingT0());
    query.setParameter(3, entity.getCashReceivingT1());
    query.setParameter(4, entity.getCashReceivingT2());
    query.setParameter(5, entity.getCashReceivingT3());
    query.setParameter(6, entity.getCashReceivingTn());
    query.setParameter(7, entity.getCashSendingT0());
    query.setParameter(8, entity.getCashSendingT1());
    query.setParameter(9, entity.getCashSendingT2());
    query.setParameter(10, entity.getCashSendingT3());
    query.setParameter(11, entity.getCustodycd());
    query.setParameter(12, entity.getTotalBalance());
    query.setParameter(13, entity.getTxDate());

    query.executeUpdate();
    trans.commit();
  }

  @Step("delete data by key")
  public void deleteByCustody(String custodyCd) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<DailyPortCashBalEntity> query = session.createQuery(
      "DELETE FROM DailyPortCashBalEntity i WHERE i.custodycd=:custodycd"
    );
    query.setParameter("custodycd", custodyCd);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  @Basic
  @Column(name = "TxDate")
  public Timestamp getTxDate() {
    return txDate;
  }

  public void setTxDate(Timestamp txDate) {
    this.txDate = txDate;
  }

  @Basic
  @Column(name = "custodycd")
  public String getCustodycd() {
    return custodycd;
  }

  public void setCustodycd(String custodycd) {
    this.custodycd = custodycd;
  }

  @Basic
  @Column(name = "balance")
  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  @Basic
  @Column(name = "CASH_RECEIVING_T0")
  public Long getCashReceivingT0() {
    return cashReceivingT0;
  }

  public void setCashReceivingT0(Long cashReceivingT0) {
    this.cashReceivingT0 = cashReceivingT0;
  }

  @Basic
  @Column(name = "CASH_SENDING_T0")
  public Long getCashSendingT0() {
    return cashSendingT0;
  }

  public void setCashSendingT0(Long cashSendingT0) {
    this.cashSendingT0 = cashSendingT0;
  }

  @Basic
  @Column(name = "CASH_RECEIVING_T1")
  public Long getCashReceivingT1() {
    return cashReceivingT1;
  }

  public void setCashReceivingT1(Long cashReceivingT1) {
    this.cashReceivingT1 = cashReceivingT1;
  }

  @Basic
  @Column(name = "CASH_SENDING_T1")
  public Long getCashSendingT1() {
    return cashSendingT1;
  }

  public void setCashSendingT1(Long cashSendingT1) {
    this.cashSendingT1 = cashSendingT1;
  }

  @Basic
  @Column(name = "CASH_RECEIVING_T2")
  public Long getCashReceivingT2() {
    return cashReceivingT2;
  }

  public void setCashReceivingT2(Long cashReceivingT2) {
    this.cashReceivingT2 = cashReceivingT2;
  }

  @Basic
  @Column(name = "CASH_SENDING_T2")
  public Long getCashSendingT2() {
    return cashSendingT2;
  }

  public void setCashSendingT2(Long cashSendingT2) {
    this.cashSendingT2 = cashSendingT2;
  }

  @Basic
  @Column(name = "CASH_RECEIVING_T3")
  public Long getCashReceivingT3() {
    return cashReceivingT3;
  }

  public void setCashReceivingT3(Long cashReceivingT3) {
    this.cashReceivingT3 = cashReceivingT3;
  }

  @Basic
  @Column(name = "CASH_SENDING_T3")
  public Long getCashSendingT3() {
    return cashSendingT3;
  }

  public void setCashSendingT3(Long cashSendingT3) {
    this.cashSendingT3 = cashSendingT3;
  }

  @Basic
  @Column(name = "CASH_RECEIVING_TN")
  public Long getCashReceivingTn() {
    return cashReceivingTn;
  }

  public void setCashReceivingTn(Long cashReceivingTn) {
    this.cashReceivingTn = cashReceivingTn;
  }

  @Basic
  @Column(name = "TotalBalance")
  public BigDecimal getTotalBalance() {
    return totalBalance;
  }

  public void setTotalBalance(BigDecimal totalBalance) {
    this.totalBalance = totalBalance;
  }

  @Basic
  @Column(name = "BANKAVLBAL")
  public Integer getBankavlbal() {
    return bankavlbal;
  }

  public void setBankavlbal(Integer bankavlbal) {
    this.bankavlbal = bankavlbal;
  }

  @Basic
  @Column(name = "BANKINQIRYDT")
  public Date getBankinqirydt() {
    return bankinqirydt;
  }

  public void setBankinqirydt(Date bankinqirydt) {
    this.bankinqirydt = bankinqirydt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DailyPortCashBalEntity that = (DailyPortCashBalEntity) o;
    return Objects.equals(txDate, that.txDate) &&
      Objects.equals(custodycd, that.custodycd) &&
      Objects.equals(balance, that.balance) &&
      Objects.equals(cashReceivingT0, that.cashReceivingT0) &&
      Objects.equals(cashSendingT0, that.cashSendingT0) &&
      Objects.equals(cashReceivingT1, that.cashReceivingT1) &&
      Objects.equals(cashSendingT1, that.cashSendingT1) &&
      Objects.equals(cashReceivingT2, that.cashReceivingT2) &&
      Objects.equals(cashSendingT2, that.cashSendingT2) &&
      Objects.equals(cashReceivingT3, that.cashReceivingT3) &&
      Objects.equals(cashSendingT3, that.cashSendingT3) &&
      Objects.equals(cashReceivingTn, that.cashReceivingTn) &&
      Objects.equals(totalBalance, that.totalBalance) &&
      Objects.equals(bankavlbal, that.bankavlbal) &&
      Objects.equals(bankinqirydt, that.bankinqirydt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(txDate, custodycd, balance, cashReceivingT0, cashSendingT0, cashReceivingT1, cashSendingT1, cashReceivingT2, cashSendingT2, cashReceivingT3, cashSendingT3, cashReceivingTn,
      totalBalance, bankavlbal, bankinqirydt);
  }
}
