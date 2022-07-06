package com.tcbs.automation.iris.impact;

import com.tcbs.automation.iris.AwsIRis;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Time;
import java.util.Objects;

@Entity
@Table(name = "RISK_RTT_CALCULATE_RESULT")
public class RiskRttCalculateResultEntity {
  private int id;
  private String custodycd;
  private String afacctno;
  private String fullName;
  private Double debt;
  private Double cash;
  private Double netDebt;
  private Double seass;
  private Double marginRate;
  private Double mrmRate;
  private Double mrlRate;
  private Double sumDelta;
  private Double rttAfterBonusSumDelta;
  private String ticker1;
  private Double delta1;
  private String ticker2;
  private Double delta2;
  private String ticker3;
  private Double delta3;
  private String statusBonus;
  private Double moneyAddedTotalBonus;
  private Double totalAssets;
  private Double totalAssetsIncrease;
  private Double totalAssetsBonus;
  private String etlUpdateDate;
  private Time updateDate;

  @Step("insert stock for get bonus share")
  public static void insertStock(RiskRttCalculateResultEntity entity) {
    Session session = AwsIRis.AwsIRisDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append(" INSERT INTO RISK_RTT_CALCULATE_RESULT");

    queryStringBuilder.append("(ID, CUSTODYCD, AFACCTNO, TOTAL_ASSETS_INCREASE,TOTAL_ASSETS_BONUS, ETL_UPDATE_DATE) ");
    queryStringBuilder.append("values (?, ?, ?, ?, ?, ? )");

    org.hibernate.query.Query<?> query = session.createNativeQuery(queryStringBuilder.toString());
    query.setParameter(1, entity.getId());
    query.setParameter(2, entity.getCustodycd());
    query.setParameter(3, entity.getAfacctno());
    query.setParameter(4, entity.getTotalAssetsIncrease());
    query.setParameter(5, entity.getTotalAssetsBonus());
    query.setParameter(6, entity.getEtlUpdateDate());
    query.executeUpdate();
    trans.commit();
  }

  @Step("insert data")
  public static void insertData(RiskRttCalculateResultEntity entity) {
    Session session = AwsIRis.AwsIRisDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append(" INSERT INTO RISK_RTT_CALCULATE_RESULT");

    queryStringBuilder.append("(ID, CUSTODYCD, AFACCTNO, DEBT, MARGIN_RATE,MRM_RATE, RTT_AFTER_BONUS_SUM_DELTA, STATUS_BONUS, MONEY_ADDED_TOTAL_BONUS, ETL_UPDATE_DATE) ");
    queryStringBuilder.append("values (?, ?, ?, ?, ?, ?, ?, ? , ?, ? )");

    org.hibernate.query.Query<?> query = session.createNativeQuery(queryStringBuilder.toString());
    query.setParameter(1, entity.getId());
    query.setParameter(2, entity.getCustodycd());
    query.setParameter(3, entity.getAfacctno());
    query.setParameter(4, entity.getDebt());
    query.setParameter(5, entity.getMarginRate());
    query.setParameter(6, entity.getMrmRate());
    query.setParameter(7, entity.getRttAfterBonusSumDelta());
    query.setParameter(8, entity.getStatusBonus());
    query.setParameter(9, entity.getMoneyAddedTotalBonus());
    query.setParameter(10, entity.getEtlUpdateDate());
    query.executeUpdate();
    trans.commit();
  }

  @Step("delete data by object")
  public static void deleteData(RiskRttCalculateResultEntity entity) {
    Session session = AwsIRis.AwsIRisDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query;


    query = session.createNativeQuery(" DELETE from RISK_RTT_CALCULATE_RESULT where ETL_UPDATE_DATE = :etlUpdateDate ");

    query.setParameter("etlUpdateDate", entity.getEtlUpdateDate());
    query.executeUpdate();
    trans.commit();
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
  @Column(name = "CUSTODYCD")
  public String getCustodycd() {
    return custodycd;
  }

  public void setCustodycd(String custodycd) {
    this.custodycd = custodycd;
  }

  @Basic
  @Column(name = "AFACCTNO")
  public String getAfacctno() {
    return afacctno;
  }

  public void setAfacctno(String afacctno) {
    this.afacctno = afacctno;
  }

  @Basic
  @Column(name = "FULL_NAME")
  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  @Basic
  @Column(name = "DEBT")
  public Double getDebt() {
    return debt;
  }

  public void setDebt(Double debt) {
    this.debt = debt;
  }

  @Basic
  @Column(name = "CASH")
  public Double getCash() {
    return cash;
  }

  public void setCash(Double cash) {
    this.cash = cash;
  }

  @Basic
  @Column(name = "NET_DEBT")
  public Double getNetDebt() {
    return netDebt;
  }

  public void setNetDebt(Double netDebt) {
    this.netDebt = netDebt;
  }

  @Basic
  @Column(name = "SEASS")
  public Double getSeass() {
    return seass;
  }

  public void setSeass(Double seass) {
    this.seass = seass;
  }

  @Basic
  @Column(name = "MARGIN_RATE")
  public Double getMarginRate() {
    return marginRate;
  }

  public void setMarginRate(Double marginRate) {
    this.marginRate = marginRate;
  }

  @Basic
  @Column(name = "MRM_RATE")
  public Double getMrmRate() {
    return mrmRate;
  }

  public void setMrmRate(Double mrmRate) {
    this.mrmRate = mrmRate;
  }

  @Basic
  @Column(name = "MRL_RATE")
  public Double getMrlRate() {
    return mrlRate;
  }

  public void setMrlRate(Double mrlRate) {
    this.mrlRate = mrlRate;
  }

  @Basic
  @Column(name = "SUM_DELTA")
  public Double getSumDelta() {
    return sumDelta;
  }

  public void setSumDelta(Double sumDelta) {
    this.sumDelta = sumDelta;
  }

  @Basic
  @Column(name = "RTT_AFTER_BONUS_SUM_DELTA")
  public Double getRttAfterBonusSumDelta() {
    return rttAfterBonusSumDelta;
  }

  public void setRttAfterBonusSumDelta(Double rttAfterBonusSumDelta) {
    this.rttAfterBonusSumDelta = rttAfterBonusSumDelta;
  }

  @Basic
  @Column(name = "TICKER_1")
  public String getTicker1() {
    return ticker1;
  }

  public void setTicker1(String ticker1) {
    this.ticker1 = ticker1;
  }

  @Basic
  @Column(name = "DELTA_1")
  public Double getDelta1() {
    return delta1;
  }

  public void setDelta1(Double delta1) {
    this.delta1 = delta1;
  }

  @Basic
  @Column(name = "TICKER_2")
  public String getTicker2() {
    return ticker2;
  }

  public void setTicker2(String ticker2) {
    this.ticker2 = ticker2;
  }

  @Basic
  @Column(name = "DELTA_2")
  public Double getDelta2() {
    return delta2;
  }

  public void setDelta2(Double delta2) {
    this.delta2 = delta2;
  }

  @Basic
  @Column(name = "TICKER_3")
  public String getTicker3() {
    return ticker3;
  }

  public void setTicker3(String ticker3) {
    this.ticker3 = ticker3;
  }

  @Basic
  @Column(name = "DELTA_3")
  public Double getDelta3() {
    return delta3;
  }

  public void setDelta3(Double delta3) {
    this.delta3 = delta3;
  }

  @Basic
  @Column(name = "STATUS_BONUS")
  public String getStatusBonus() {
    return statusBonus;
  }

  public void setStatusBonus(String statusBonus) {
    this.statusBonus = statusBonus;
  }

  @Basic
  @Column(name = "MONEY_ADDED_TOTAL_BONUS")
  public Double getMoneyAddedTotalBonus() {
    return moneyAddedTotalBonus;
  }

  public void setMoneyAddedTotalBonus(Double moneyAddedTotalBonus) {
    this.moneyAddedTotalBonus = moneyAddedTotalBonus;
  }

  @Basic
  @Column(name = "TOTAL_ASSETS")
  public Double getTotalAssets() {
    return totalAssets;
  }

  public void setTotalAssets(Double totalAssets) {
    this.totalAssets = totalAssets;
  }

  @Basic
  @Column(name = "TOTAL_ASSETS_INCREASE")
  public Double getTotalAssetsIncrease() {
    return totalAssetsIncrease;
  }

  public void setTotalAssetsIncrease(Double totalAssetsIncrease) {
    this.totalAssetsIncrease = totalAssetsIncrease;
  }

  @Basic
  @Column(name = "TOTAL_ASSETS_BONUS")
  public Double getTotalAssetsBonus() {
    return totalAssetsBonus;
  }

  public void setTotalAssetsBonus(Double totalAssetsBonus) {
    this.totalAssetsBonus = totalAssetsBonus;
  }

  @Basic
  @Column(name = "ETL_UPDATE_DATE")
  public String getEtlUpdateDate() {
    return etlUpdateDate;
  }

  public void setEtlUpdateDate(String etlUpdateDate) {
    this.etlUpdateDate = etlUpdateDate;
  }

  @Basic
  @Column(name = "UPDATE_DATE")
  public Time getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Time updateDate) {
    this.updateDate = updateDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RiskRttCalculateResultEntity that = (RiskRttCalculateResultEntity) o;
    return id == that.id && Objects.equals(custodycd, that.custodycd) && Objects.equals(afacctno, that.afacctno) && Objects.equals(fullName,
      that.fullName) && Objects.equals(debt, that.debt) && Objects.equals(cash, that.cash) && Objects.equals(netDebt, that.netDebt) && Objects.equals(seass,
      that.seass) && Objects.equals(marginRate, that.marginRate) && Objects.equals(mrmRate, that.mrmRate) && Objects.equals(mrlRate,
      that.mrlRate) && Objects.equals(sumDelta, that.sumDelta) && Objects.equals(rttAfterBonusSumDelta, that.rttAfterBonusSumDelta) && Objects.equals(ticker1,
      that.ticker1) && Objects.equals(delta1, that.delta1) && Objects.equals(ticker2, that.ticker2) && Objects.equals(delta2, that.delta2) && Objects.equals(
      ticker3, that.ticker3) && Objects.equals(delta3, that.delta3) && Objects.equals(statusBonus, that.statusBonus) && Objects.equals(moneyAddedTotalBonus,
      that.moneyAddedTotalBonus) && Objects.equals(totalAssets, that.totalAssets) && Objects.equals(etlUpdateDate, that.etlUpdateDate) && Objects.equals(updateDate,
      that.updateDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, custodycd, afacctno, fullName, debt, cash, netDebt, seass, marginRate, mrmRate, mrlRate, sumDelta, rttAfterBonusSumDelta, ticker1, delta1, ticker2, delta2, ticker3, delta3,
      statusBonus,
      moneyAddedTotalBonus, totalAssets, etlUpdateDate, updateDate);
  }
}
