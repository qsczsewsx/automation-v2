package com.tcbs.automation.dwh.dwhservice;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "Prc_Flx_Stock_TradingFee")
public class PrcFlxStockTradingFeeEntity {
  @Id
  private Timestamp txdate;
  private String sectypeName;
  private String symbol;
  private String exectypeName;
  private String custodycd;
  private String afacctno;
  private String vat;
  private String custtype;
  private String custtypeName;
  private Double matchqttyB;
  private Double matchpriceB;
  private Double execamtB;
  private Double matchqttyS;
  private Double matchpriceS;
  private Double execamtS;
  private Double feeRate;
  private Double feeAmtDetail;
  private Double feetaxAmtDetail;
  private Double taxsellamt;
  private String hdSectype;
  private String typename;

  @Step("update ETL data")
  public static void updateEtlDate(String txDate) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query = session.createNativeQuery("UPDATE Prc_Flx_Stock_TradingFee " +
      " SET TXDATE= ? " +
      " WHERE TXDATE = (SELECT MAX(TXDATE) FROM Prc_Flx_Stock_TradingFee) ");
    query.setParameter(1, txDate);
    query.executeUpdate();
    trans.commit();
  }

  @Basic
  @Column(name = "TXDATE")
  public Timestamp getTxdate() {
    return txdate;
  }

  public void setTxdate(Timestamp txdate) {
    this.txdate = txdate;
  }

  @Basic
  @Column(name = "SECTYPE_NAME")
  public String getSectypeName() {
    return sectypeName;
  }

  public void setSectypeName(String sectypeName) {
    this.sectypeName = sectypeName;
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
  @Column(name = "EXECTYPE_NAME")
  public String getExectypeName() {
    return exectypeName;
  }

  public void setExectypeName(String exectypeName) {
    this.exectypeName = exectypeName;
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
  @Column(name = "VAT")
  public String getVat() {
    return vat;
  }

  public void setVat(String vat) {
    this.vat = vat;
  }

  @Basic
  @Column(name = "CUSTTYPE")
  public String getCusttype() {
    return custtype;
  }

  public void setCusttype(String custtype) {
    this.custtype = custtype;
  }

  @Basic
  @Column(name = "CUSTTYPE_NAME")
  public String getCusttypeName() {
    return custtypeName;
  }

  public void setCusttypeName(String custtypeName) {
    this.custtypeName = custtypeName;
  }

  @Basic
  @Column(name = "MATCHQTTY_B")
  public Double getMatchqttyB() {
    return matchqttyB;
  }

  public void setMatchqttyB(Double matchqttyB) {
    this.matchqttyB = matchqttyB;
  }

  @Basic
  @Column(name = "MATCHPRICE_B")
  public Double getMatchpriceB() {
    return matchpriceB;
  }

  public void setMatchpriceB(Double matchpriceB) {
    this.matchpriceB = matchpriceB;
  }

  @Basic
  @Column(name = "EXECAMT_B")
  public Double getExecamtB() {
    return execamtB;
  }

  public void setExecamtB(Double execamtB) {
    this.execamtB = execamtB;
  }

  @Basic
  @Column(name = "MATCHQTTY_S")
  public Double getMatchqttyS() {
    return matchqttyS;
  }

  public void setMatchqttyS(Double matchqttyS) {
    this.matchqttyS = matchqttyS;
  }

  @Basic
  @Column(name = "MATCHPRICE_S")
  public Double getMatchpriceS() {
    return matchpriceS;
  }

  public void setMatchpriceS(Double matchpriceS) {
    this.matchpriceS = matchpriceS;
  }

  @Basic
  @Column(name = "EXECAMT_S")
  public Double getExecamtS() {
    return execamtS;
  }

  public void setExecamtS(Double execamtS) {
    this.execamtS = execamtS;
  }

  @Basic
  @Column(name = "FEE_RATE")
  public Double getFeeRate() {
    return feeRate;
  }

  public void setFeeRate(Double feeRate) {
    this.feeRate = feeRate;
  }

  @Basic
  @Column(name = "FEE_AMT_DETAIL")
  public Double getFeeAmtDetail() {
    return feeAmtDetail;
  }

  public void setFeeAmtDetail(Double feeAmtDetail) {
    this.feeAmtDetail = feeAmtDetail;
  }

  @Basic
  @Column(name = "FEETAX_AMT_DETAIL")
  public Double getFeetaxAmtDetail() {
    return feetaxAmtDetail;
  }

  public void setFeetaxAmtDetail(Double feetaxAmtDetail) {
    this.feetaxAmtDetail = feetaxAmtDetail;
  }

  @Basic
  @Column(name = "TAXSELLAMT")
  public Double getTaxsellamt() {
    return taxsellamt;
  }

  public void setTaxsellamt(Double taxsellamt) {
    this.taxsellamt = taxsellamt;
  }

  @Basic
  @Column(name = "HD_SECTYPE")
  public String getHdSectype() {
    return hdSectype;
  }

  public void setHdSectype(String hdSectype) {
    this.hdSectype = hdSectype;
  }

  @Basic
  @Column(name = "TYPENAME")
  public String getTypename() {
    return typename;
  }

  public void setTypename(String typename) {
    this.typename = typename;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PrcFlxStockTradingFeeEntity that = (PrcFlxStockTradingFeeEntity) o;
    return Objects.equals(txdate, that.txdate) &&
      Objects.equals(sectypeName, that.sectypeName) &&
      Objects.equals(symbol, that.symbol) &&
      Objects.equals(exectypeName, that.exectypeName) &&
      Objects.equals(custodycd, that.custodycd) &&
      Objects.equals(afacctno, that.afacctno) &&
      Objects.equals(vat, that.vat) &&
      Objects.equals(custtype, that.custtype) &&
      Objects.equals(custtypeName, that.custtypeName) &&
      Objects.equals(matchqttyB, that.matchqttyB) &&
      Objects.equals(matchpriceB, that.matchpriceB) &&
      Objects.equals(execamtB, that.execamtB) &&
      Objects.equals(matchqttyS, that.matchqttyS) &&
      Objects.equals(matchpriceS, that.matchpriceS) &&
      Objects.equals(execamtS, that.execamtS) &&
      Objects.equals(feeRate, that.feeRate) &&
      Objects.equals(feeAmtDetail, that.feeAmtDetail) &&
      Objects.equals(feetaxAmtDetail, that.feetaxAmtDetail) &&
      Objects.equals(taxsellamt, that.taxsellamt) &&
      Objects.equals(hdSectype, that.hdSectype) &&
      Objects.equals(typename, that.typename);
  }

  @Override
  public int hashCode() {
    return Objects.hash(txdate, sectypeName, symbol, exectypeName, custodycd, afacctno, vat, custtype, custtypeName, matchqttyB, matchpriceB, execamtB, matchqttyS, matchpriceS, execamtS, feeRate,
      feeAmtDetail, feetaxAmtDetail, taxsellamt, hdSectype, typename);
  }
}
