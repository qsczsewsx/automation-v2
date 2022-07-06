package com.tcbs.automation.dwh;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "smy_dwh_flx_SE0067")
public class SmyDwhFlxSe0067Entity {
  private Date txdate;
  private String txnuM;
  private String fullname;
  private String custodycd;
  private String seacctno;
  private String afacctno;
  private String symbol;
  private BigDecimal price;
  private BigDecimal qtty;
  private BigDecimal amt;
  private String status;
  private BigDecimal feeamt;
  private BigDecimal taxamt;
  private BigDecimal ramt;
  private Date sdate;
  private Date vdate;
  private Date mdate;
  private String custtype;
  private String brid;
  private int etlCurDate;
  private Timestamp etlRunDatetime;

  public static List<HashMap<String, Object>> getByCus(Date fromDate, Date toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append("  SELECT txnuM as txNum, txdate as registerDate, custodyCd, afacctNo, symbol, qtty as volume, price, amt as value,");
    queryStringBuilder.append("         feeamt as fee, ramt as amount, vdate as sentDate, sdate as effectiveDate, mdate as paidDate ");
    queryStringBuilder.append("  FROM smy_dwh_flx_SE0067 sdfs    ");
    queryStringBuilder.append("  WHERE txdate >= :fromDate and txdate <= :toDate ORDER BY txdate ASC");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get latest etlDate")
  public static Integer getLatestEtlDate() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT MAX(etlCurDate) ");
    queryStringBuilder.append(" FROM smy_dwh_flx_SE0067 ");

    try {
      List<Integer> result = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .getResultList();
      Integer latestDate = result.get(0);
      return latestDate;
    } catch (Exception ex) {

      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }


  @Step("update ETL data")
  public static void updateEtlDate(Integer etlDate, String etlTime) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query = session.createNativeQuery("UPDATE smy_dwh_flx_SE0067 " +
      " SET EtlCurDate= ? , EtlRunDatetime=?  " +
      " WHERE EtlCurDate = (SELECT MAX(EtlCurDate) FROM smy_dwh_flx_SE0067) ");
    query.setParameter(1, etlDate);
    query.setParameter(2, etlTime);
    query.executeUpdate();
    trans.commit();
  }

  @Basic
  @Column(name = "txdate")
  public Date getTxdate() {
    return txdate;
  }

  public void setTxdate(Date txdate) {
    this.txdate = txdate;
  }

  @Basic
  @Column(name = "txnuM")
  public String getTxnuM() {
    return txnuM;
  }

  public void setTxnuM(String txnuM) {
    this.txnuM = txnuM;
  }

  @Basic
  @Column(name = "fullname")
  public String getFullname() {
    return fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
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
  @Column(name = "seacctno")
  public String getSeacctno() {
    return seacctno;
  }

  public void setSeacctno(String seacctno) {
    this.seacctno = seacctno;
  }

  @Basic
  @Column(name = "afacctno")
  public String getAfacctno() {
    return afacctno;
  }

  public void setAfacctno(String afacctno) {
    this.afacctno = afacctno;
  }

  @Basic
  @Column(name = "symbol")
  public String getSymbol() {
    return symbol;
  }

  public void setSymbol(String symbol) {
    this.symbol = symbol;
  }

  @Basic
  @Column(name = "price")
  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  @Basic
  @Column(name = "qtty")
  public BigDecimal getQtty() {
    return qtty;
  }

  public void setQtty(BigDecimal qtty) {
    this.qtty = qtty;
  }

  @Basic
  @Column(name = "amt")
  public BigDecimal getAmt() {
    return amt;
  }

  public void setAmt(BigDecimal amt) {
    this.amt = amt;
  }

  @Basic
  @Column(name = "status")
  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @Basic
  @Column(name = "feeamt")
  public BigDecimal getFeeamt() {
    return feeamt;
  }

  public void setFeeamt(BigDecimal feeamt) {
    this.feeamt = feeamt;
  }

  @Basic
  @Column(name = "taxamt")
  public BigDecimal getTaxamt() {
    return taxamt;
  }

  public void setTaxamt(BigDecimal taxamt) {
    this.taxamt = taxamt;
  }

  @Basic
  @Column(name = "ramt")
  public BigDecimal getRamt() {
    return ramt;
  }

  public void setRamt(BigDecimal ramt) {
    this.ramt = ramt;
  }

  @Basic
  @Column(name = "sdate")
  public Date getSdate() {
    return sdate;
  }

  public void setSdate(Date sdate) {
    this.sdate = sdate;
  }

  @Basic
  @Column(name = "vdate")
  public Date getVdate() {
    return vdate;
  }

  public void setVdate(Date vdate) {
    this.vdate = vdate;
  }

  @Basic
  @Column(name = "mdate")
  public Date getMdate() {
    return mdate;
  }

  public void setMdate(Date mdate) {
    this.mdate = mdate;
  }

  @Basic
  @Column(name = "custtype")
  public String getCusttype() {
    return custtype;
  }

  public void setCusttype(String custtype) {
    this.custtype = custtype;
  }

  @Basic
  @Column(name = "brid")
  public String getBrid() {
    return brid;
  }

  public void setBrid(String brid) {
    this.brid = brid;
  }

  @Basic
  @Column(name = "EtlCurDate")
  public int getEtlCurDate() {
    return etlCurDate;
  }

  public void setEtlCurDate(int etlCurDate) {
    this.etlCurDate = etlCurDate;
  }

  @Basic
  @Column(name = "EtlRunDatetime")
  public Timestamp getEtlRunDatetime() {
    return etlRunDatetime;
  }

  public void setEtlRunDatetime(Timestamp etlRunDatetime) {
    this.etlRunDatetime = etlRunDatetime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SmyDwhFlxSe0067Entity that = (SmyDwhFlxSe0067Entity) o;
    return etlCurDate == that.etlCurDate &&
      Objects.equals(txdate, that.txdate) &&
      Objects.equals(txnuM, that.txnuM) &&
      Objects.equals(fullname, that.fullname) &&
      Objects.equals(custodycd, that.custodycd) &&
      Objects.equals(seacctno, that.seacctno) &&
      Objects.equals(afacctno, that.afacctno) &&
      Objects.equals(symbol, that.symbol) &&
      Objects.equals(price, that.price) &&
      Objects.equals(qtty, that.qtty) &&
      Objects.equals(amt, that.amt) &&
      Objects.equals(status, that.status) &&
      Objects.equals(feeamt, that.feeamt) &&
      Objects.equals(taxamt, that.taxamt) &&
      Objects.equals(ramt, that.ramt) &&
      Objects.equals(sdate, that.sdate) &&
      Objects.equals(vdate, that.vdate) &&
      Objects.equals(mdate, that.mdate) &&
      Objects.equals(custtype, that.custtype) &&
      Objects.equals(brid, that.brid) &&
      Objects.equals(etlRunDatetime, that.etlRunDatetime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(txdate, txnuM, fullname, custodycd, seacctno, afacctno, symbol, price, qtty, amt, status, feeamt, taxamt, ramt, sdate, vdate, mdate, custtype, brid, etlCurDate,
      etlRunDatetime);
  }
}
