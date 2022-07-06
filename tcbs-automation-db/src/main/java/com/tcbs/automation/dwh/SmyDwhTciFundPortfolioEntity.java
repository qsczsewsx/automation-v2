package com.tcbs.automation.dwh;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Smy_dwh_tci_Fund_Portfolio")
public class SmyDwhTciFundPortfolioEntity {
  private int id;
  private Timestamp dates;
  private Integer orderId;
  private Timestamp tradingTimestamp;
  private String accountId;
  private String custodycd;
  private Integer actionId;
  private String productCode;
  private BigDecimal netVolume;
  private BigDecimal obalVolume;
  private BigDecimal fundValue;
  private Timestamp createdDate;

  public static List<HashMap<String, Object>> getByCondition(String fromDate, String toDate, List<String> fundProductList) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append("select * from ( ");
    queryStringBuilder.append("  select CONVERT(varchar(10), cast(DATES as date), 23) as dateReport, custodyCD, PRODUCT_CODE as productCode,  ");
    queryStringBuilder.append("  OBAL_VOLUME as oBalVolume, FUND_VALUE as fundValue ");
    queryStringBuilder.append("  from Smy_dwh_tci_Fund_Portfolio ");
    queryStringBuilder.append("  where DATES >= :fromDate and DATES < dateadd(day, 1, :toDate) ");
    queryStringBuilder.append("  and PRODUCT_CODE in :fundList ) fund ");
    queryStringBuilder.append(" order by dateReport, custodyCD ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setParameter("fundList", fundProductList)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get latest tnxDate")
  public static Date getLatestTxnDate() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT cast(MAX(CREATED_DATE) as date) as latestDate ");
    queryStringBuilder.append(" FROM Smy_dwh_tci_Fund_Portfolio   ");

    try {
      List<Date> result = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .getResultList();
      Date latestDate = result.get(0);
      return latestDate;
    } catch (Exception ex) {

      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @Basic
  @Column(name = "DATES")
  public Timestamp getDates() {
    return dates;
  }

  public void setDates(Timestamp dates) {
    this.dates = dates;
  }

  @Basic
  @Column(name = "ORDER_ID")
  public Integer getOrderId() {
    return orderId;
  }

  public void setOrderId(Integer orderId) {
    this.orderId = orderId;
  }

  @Basic
  @Column(name = "TRADING_TIMESTAMP")
  public Timestamp getTradingTimestamp() {
    return tradingTimestamp;
  }

  public void setTradingTimestamp(Timestamp tradingTimestamp) {
    this.tradingTimestamp = tradingTimestamp;
  }

  @Basic
  @Column(name = "ACCOUNT_ID")
  public String getAccountId() {
    return accountId;
  }

  public void setAccountId(String accountId) {
    this.accountId = accountId;
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
  @Column(name = "ACTION_ID")
  public Integer getActionId() {
    return actionId;
  }

  public void setActionId(Integer actionId) {
    this.actionId = actionId;
  }

  @Basic
  @Column(name = "PRODUCT_CODE")
  public String getProductCode() {
    return productCode;
  }

  public void setProductCode(String productCode) {
    this.productCode = productCode;
  }

  @Basic
  @Column(name = "NET_VOLUME")
  public BigDecimal getNetVolume() {
    return netVolume;
  }

  public void setNetVolume(BigDecimal netVolume) {
    this.netVolume = netVolume;
  }

  @Basic
  @Column(name = "OBAL_VOLUME")
  public BigDecimal getObalVolume() {
    return obalVolume;
  }

  public void setObalVolume(BigDecimal obalVolume) {
    this.obalVolume = obalVolume;
  }

  @Basic
  @Column(name = "FUND_VALUE")
  public BigDecimal getFundValue() {
    return fundValue;
  }

  public void setFundValue(BigDecimal fundValue) {
    this.fundValue = fundValue;
  }

  @Basic
  @Column(name = "CREATED_DATE")
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
    SmyDwhTciFundPortfolioEntity that = (SmyDwhTciFundPortfolioEntity) o;
    return id == that.id &&
      Objects.equals(dates, that.dates) &&
      Objects.equals(orderId, that.orderId) &&
      Objects.equals(tradingTimestamp, that.tradingTimestamp) &&
      Objects.equals(accountId, that.accountId) &&
      Objects.equals(custodycd, that.custodycd) &&
      Objects.equals(actionId, that.actionId) &&
      Objects.equals(productCode, that.productCode) &&
      Objects.equals(netVolume, that.netVolume) &&
      Objects.equals(obalVolume, that.obalVolume) &&
      Objects.equals(fundValue, that.fundValue) &&
      Objects.equals(createdDate, that.createdDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dates, orderId, tradingTimestamp, accountId, custodycd, actionId, productCode, netVolume, obalVolume, fundValue, createdDate);
  }

  @Step("insert data")
  public boolean saveFundBalance(SmyDwhTciFundPortfolioEntity entity) {
    try {
      Session session = Dwh.dwhDbConnection.getSession();
      session.clear();
      beginTransaction(session);
      session.save(entity);
      session.getTransaction().commit();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return true;
  }

  @Step("delete data by key")
  public void deleteById(int id) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<SmyDwhTciFundPortfolioEntity> query = session.createQuery(
      "DELETE FROM SmyDwhTciFundPortfolioEntity i WHERE i.id=:id"
    );
    query.setParameter("id", id);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
