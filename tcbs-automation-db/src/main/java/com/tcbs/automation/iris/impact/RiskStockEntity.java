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
@Table(name = "RISK_STOCK", schema = "IRISK_SIT", catalog = "")
public class RiskStockEntity {
  private int id;
  private String custodycd;
  private String afacctno;
  private String symbol;
  private Double asset;
  private Double caassAfter;
  private Double caamtAfter;
  private Double marketPrice;
  private Double loanPriceBefore;
  private Double loanRatioBefore;
  private Double loanPriceAfter;
  private Double loanRatioAfter;
  private String etlUpdateDate;
  private Time updateDate;
  private String typeSync;
  private Double rateCl;
  private Double basicPrice;
  private Time etlSyncDate;

  @Step("insert data")
  public static void insertData(RiskStockEntity entity) {
    Session session = AwsIRis.AwsIRisDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append(" INSERT INTO RISK_STOCK");

    queryStringBuilder.append("(ID, CUSTODYCD, AFACCTNO,SYMBOL, MARKET_PRICE, LOAN_PRICE_AFTER,LOAN_RATIO_AFTER, ETL_UPDATE_DATE, CAASS_AFTER, CAAMT_AFTER ) ");
    queryStringBuilder.append("values (?, ?, ?, ?, ?, ?, ?, ?,?,? )");

    org.hibernate.query.Query<?> query = session.createNativeQuery(queryStringBuilder.toString());
    query.setParameter(1, entity.getId());
    query.setParameter(2, entity.getCustodycd());
    query.setParameter(3, entity.getAfacctno());
    query.setParameter(4, entity.getSymbol());
    query.setParameter(5, entity.getMarketPrice());
    query.setParameter(6, entity.getLoanPriceAfter());
    query.setParameter(7, entity.getLoanRatioAfter());
    query.setParameter(8, entity.getEtlUpdateDate());
    query.setParameter(9, entity.getCaassAfter());
    query.setParameter(10, entity.getCaamtAfter());
    query.executeUpdate();
    trans.commit();
  }

  @Step("delete data by object")
  public static void deleteData(RiskStockEntity entity) {
    Session session = AwsIRis.AwsIRisDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query;


    query = session.createNativeQuery(" DELETE from RISK_STOCK where ETL_UPDATE_DATE = :etlUpdateDate ");

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
  @Column(name = "SYMBOL")
  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  @Basic
  @Column(name = "ASSET")
  public Double getAsset() {
    return asset;
  }

  public void setAsset(Double asset) {
    this.asset = asset;
  }

  @Basic
  @Column(name = "CAASS")
  public Double getCaassAfter() {
    return caassAfter;
  }

  public void setCaassAfter(Double caassAfter) {
    this.caassAfter = caassAfter;
  }

  @Basic
  @Column(name = "CAAMT")
  public Double getCaamtAfter() {
    return caamtAfter;
  }

  public void setCaamtAfter(Double caamt) {
    this.caamtAfter = caamtAfter;
  }

  @Basic
  @Column(name = "MARKET_PRICE")
  public Double getMarketPrice() {
    return marketPrice;
  }

  public void setMarketPrice(Double marketPrice) {
    this.marketPrice = marketPrice;
  }

  @Basic
  @Column(name = "LOAN_PRICE_BEFORE")
  public Double getLoanPriceBefore() {
    return loanPriceBefore;
  }

  public void setLoanPriceBefore(Double loanPriceBefore) {
    this.loanPriceBefore = loanPriceBefore;
  }

  @Basic
  @Column(name = "LOAN_RATIO_BEFORE")
  public Double getLoanRatioBefore() {
    return loanRatioBefore;
  }

  public void setLoanRatioBefore(Double loanRatioBefore) {
    this.loanRatioBefore = loanRatioBefore;
  }

  @Basic
  @Column(name = "LOAN_PRICE_AFTER")
  public Double getLoanPriceAfter() {
    return loanPriceAfter;
  }

  public void setLoanPriceAfter(Double loanPriceAfter) {
    this.loanPriceAfter = loanPriceAfter;
  }

  @Basic
  @Column(name = "LOAN_RATIO_AFTER")
  public Double getLoanRatioAfter() {
    return loanRatioAfter;
  }

  public void setLoanRatioAfter(Double loanRatioAfter) {
    this.loanRatioAfter = loanRatioAfter;
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

  @Basic
  @Column(name = "TYPE_SYNC")
  public String getTypeSync() {
    return typeSync;
  }

  public void setTypeSync(String typeSync) {
    this.typeSync = typeSync;
  }

  @Basic
  @Column(name = "RATE_CL")
  public Double getRateCl() {
    return rateCl;
  }

  public void setRateCl(Double rateCl) {
    this.rateCl = rateCl;
  }

  @Basic
  @Column(name = "BASIC_PRICE")
  public Double getBasicPrice() {
    return basicPrice;
  }

  public void setBasicPrice(Double basicPrice) {
    this.basicPrice = basicPrice;
  }

  @Basic
  @Column(name = "ETL_SYNC_DATE")
  public Time getEtlSyncDate() {
    return etlSyncDate;
  }

  public void setEtlSyncDate(Time etlSyncDate) {
    this.etlSyncDate = etlSyncDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RiskStockEntity that = (RiskStockEntity) o;
    return id == that.id && Objects.equals(custodycd, that.custodycd) && Objects.equals(afacctno, that.afacctno) && Objects.equals(symbol,
      that.symbol) && Objects.equals(asset, that.asset) && Objects.equals(marketPrice, that.marketPrice) && Objects.equals(loanPriceBefore,
      that.loanPriceBefore) && Objects.equals(loanRatioBefore, that.loanRatioBefore) && Objects.equals(loanPriceAfter, that.loanPriceAfter) && Objects.equals(
      loanRatioAfter, that.loanRatioAfter) && Objects.equals(etlUpdateDate, that.etlUpdateDate) && Objects.equals(updateDate, that.updateDate) && Objects.equals(typeSync,
      that.typeSync) && Objects.equals(rateCl, that.rateCl) && Objects.equals(basicPrice, that.basicPrice) && Objects.equals(etlSyncDate, that.etlSyncDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, custodycd, afacctno, symbol, asset, marketPrice, loanPriceBefore, loanRatioBefore, loanPriceAfter, loanRatioAfter, etlUpdateDate, updateDate, typeSync, rateCl, basicPrice,
      etlSyncDate);
  }
}
