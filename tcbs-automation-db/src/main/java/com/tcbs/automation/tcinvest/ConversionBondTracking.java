package com.tcbs.automation.tcinvest;

import com.tcbs.automation.functions.PublicConstant;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

@Entity
@Table(name = "CONVERSION_BOND_TRACKING")
public class ConversionBondTracking {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @NotNull
  @Column(name = "customerId")
  private String customerId;
  @NotNull
  @Column(name = "bondCode")
  private String bondCode;
  @NotNull
  @Column(name = "totalValue")
  private Double totalValue;
  @NotNull
  @Column(name = "time2maturity")
  private Double time2Maturity;
  @NotNull
  @Column(name = "yield2maturity")
  private Double yield2Maturity;
  @NotNull
  @Column(name = "unitPrice")
  private Double unitPrice;
  @NotNull
  @Column(name = "quantity")
  private Long quantity;
  @Column(name = "tradingDate")
  private Timestamp tradingDate;
  @NotNull
  @Column(name = "createdDate")
  private Timestamp createdDate;
  @NotNull
  @Column(name = "updatedDate")
  private Timestamp updatedDate;
  @Column(name = "orderId")
  private String orderId;
  @Column(name = "actualQuantity")
  private Long actualQuantity;
  @Column(name = "actualUnitPrice")
  private Double actualUnitPrice;
  @Column(name = "actualYield2Maturity")
  private Double actualYield2Maturity;
  @Column(name = "actualValue")
  private Double actualValue;
  @Column(name = "actualTradingDate")
  private Timestamp actualTradingDate;
  @NotNull
  @Column(name = "currentBondCode")
  private String currentBondCode;
  @Column(name = "currentProductCode")
  private String currentProductCode;
  @Column(name = "actualBondCode")
  private String actualBondCode;


  public ConversionBondTracking(String customerId, String bondCode, Double totalValue,
                                Double time2Maturity, Double yield2Maturity, Double unitPrice,
                                Long quantity, Timestamp tradingDate, Timestamp createdDate,
                                Timestamp updatedDate, String orderId, Long actualQuantity,
                                Double actualUnitPrice, Double actualYield2Maturity, Double actualValue,
                                Timestamp actualTradingDate, String currentBondCode, String currentProductCode, String actualBondCode) {
    this.customerId = customerId;
    this.bondCode = bondCode;
    this.totalValue = totalValue;
    this.time2Maturity = time2Maturity;
    this.yield2Maturity = yield2Maturity;
    this.unitPrice = unitPrice;
    this.quantity = quantity;
    this.tradingDate = tradingDate;
    this.createdDate = createdDate;
    this.updatedDate = updatedDate;
    this.orderId = orderId;
    this.actualQuantity = actualQuantity;
    this.actualUnitPrice = actualUnitPrice;
    this.actualYield2Maturity = actualYield2Maturity;
    this.actualValue = actualValue;
    this.actualTradingDate = actualTradingDate;
    this.currentBondCode = currentBondCode;
    this.currentProductCode = currentProductCode;
    this.actualBondCode = actualBondCode;
  }

  public ConversionBondTracking() {
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getBondCode() {
    return bondCode;
  }

  public void setBondCode(String bondCode) {
    this.bondCode = bondCode;
  }

  public Double getTotalValue() {
    return totalValue;
  }

  public void setTotalValue(Double totalValue) {
    this.totalValue = totalValue;
  }

  public Double getTime2Maturity() {
    return time2Maturity;
  }

  public void setTime2Maturity(Double time2Maturity) {
    this.time2Maturity = time2Maturity;
  }

  public Double getYield2Maturity() {
    return yield2Maturity;
  }

  public void setYield2Maturity(Double yield2Maturity) {
    this.yield2Maturity = yield2Maturity;
  }

  public Double getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(Double unitPrice) {
    this.unitPrice = unitPrice;
  }

  public Long getQuantity() {
    return quantity;
  }

  public void setQuantity(Long quantity) {
    this.quantity = quantity;
  }

  public String getTradingDate() {
    return tradingDate == null ? null : PublicConstant.dateTimeFormat.format(tradingDate);
  }

  public void setTradingDate(String tradingDate) throws ParseException {
    this.tradingDate = new Timestamp(PublicConstant.dateTimeFormat.parse(tradingDate).getTime());
  }

  public String getCreatedDate() {
    return createdDate == null ? null : PublicConstant.dateTimeFormat.format(createdDate);
  }

  public void setCreatedDate(String createdDate) throws ParseException {
    this.createdDate = new Timestamp(PublicConstant.dateTimeFormat.parse(createdDate).getTime());
  }

  public String getUpdatedDate() {
    return updatedDate == null ? null : PublicConstant.dateTimeFormat.format(updatedDate);
  }

  public void setUpdatedDate(String updatedDate) throws ParseException {
    this.updatedDate = new Timestamp(PublicConstant.dateTimeFormat.parse(updatedDate).getTime());
  }

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public Long getActualQuantity() {
    return actualQuantity;
  }

  public void setActualQuantity(Long actualQuantity) {
    this.actualQuantity = actualQuantity;
  }

  public Double getActualUnitPrice() {
    return actualUnitPrice;
  }

  public void setActualUnitPrice(Double actualUnitPrice) {
    this.actualUnitPrice = actualUnitPrice;
  }

  public Double getActualYield2Maturity() {
    return actualYield2Maturity;
  }

  public void setActualYield2Maturity(Double actualYield2Maturity) {
    this.actualYield2Maturity = actualYield2Maturity;
  }

  public Double getActualValue() {
    return actualValue;
  }

  public void setActualValue(Double actualValue) {
    this.actualValue = actualValue;
  }

  public String getActualTradingDate() {
    return actualTradingDate == null ? null : PublicConstant.dateTimeFormat.format(actualTradingDate);
  }

  public void setActualTradingDate(String actualTradingDate) throws ParseException {
    this.actualTradingDate = new Timestamp(PublicConstant.dateTimeFormat.parse(actualTradingDate).getTime());
  }

  public String getCurrentBondCode() {
    return currentBondCode;
  }

  public void setCurrentBondCode(String currentBondCode) {
    this.currentBondCode = currentBondCode;
  }

  public String getCurrentProductCode() {
    return currentProductCode;
  }

  public void setCurrentProductCode(String currentProductCode) {
    this.currentProductCode = currentProductCode;
  }

  public String getActualBondCode() {
    return actualBondCode;
  }

  public void setActualBondCode(String actualBondCode) {
    this.actualBondCode = actualBondCode;
  }

  @Step("Delete records test")
  public void deleteConversionBondTracking() {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query query = session
      .createQuery("DELETE ConversionBondTracking where customerId = '0001888666' and bondCode = 'ABC123'");
    query.executeUpdate();
    trans.commit();
  }

  @Step("Get the newest record from TCInvest.ConversionBondTracking")
  public ConversionBondTracking getNewestConversionBondTracking() {
    TcInvest.tcInvestDbConnection.getSession().clear();
    Query<ConversionBondTracking> query = TcInvest.tcInvestDbConnection.getSession()
      .createQuery("from ConversionBondTracking a order by a.id DESC",
        ConversionBondTracking.class);
    query.setMaxResults(1);
    return query.uniqueResult();
  }


  @Step("Get all bond recommend from TCInvest.ConversionBondTracking")
  public List<ConversionBondTracking> getAllBondConversionTracking() {
    Query<ConversionBondTracking> query = TcInvest.tcInvestDbConnection.getSession()
      .createQuery("from ConversionBondTracking", ConversionBondTracking.class);
    List<ConversionBondTracking> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  public void insert() {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    session.save(this);
    session.getTransaction().commit();
  }

}
