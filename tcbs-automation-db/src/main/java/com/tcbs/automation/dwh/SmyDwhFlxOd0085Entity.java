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
@Table(name = "smy_dwh_flx_OD0085")
public class SmyDwhFlxOd0085Entity {
  private String custodycd;
  private String afacctno;
  private String fullname;
  private Date txdate;
  private Date cleardate;
  private String exectype;
  private String codeid;
  private String symbol;
  private Long matchprice;
  private Long matchqtty;
  private String via;
  private Long matchamt;
  private BigDecimal feerate;
  private Double taxrate;
  private BigDecimal iodfeeacr;
  private Double iodtaxsellamt;
  private String orderid;
  private Long orderqtty;
  private Long quoteprice;
  private Double rAmt;
  private int etlCurDate;
  private Timestamp etlRunDatetime;


  public static List<HashMap<String, Object>> getByCus(String txDate, String custodyCd, String afacctNo, String orderType, String channel) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("  SELECT CUSTODYCD, TXDATE as transDate, CLEARDATE as settlementDate, orderid as orderId, exectype as orderType, symbol, via as channel,");
    queryStringBuilder.append("         MATCHQTTY as volume, MATCHPRICE as price, matchamt as value, iodfeeacr as fee, iodtaxsellamt as pit, rAmt as amount");
    queryStringBuilder.append("  FROM smy_dwh_flx_OD0085 sdfs  ");
    queryStringBuilder.append("  WHERE TXDATE = :txDate ");

    if (custodyCd.equals("")) {
      custodyCd = "%";
      queryStringBuilder.append("   and CUSTODYCD like :custodyCd ");
    } else {
      queryStringBuilder.append("   and CUSTODYCD = :custodyCd ");
    }
    if (afacctNo.equals("")) {
      afacctNo = "%";
      queryStringBuilder.append("   and AFACCTNO like :afacctNo ");
    } else {
      queryStringBuilder.append("   and AFACCTNO = :afacctNo ");
    }
    if (orderType.equals("")) {
      orderType = "%";
      queryStringBuilder.append("   and exectype like :orderType ");
    } else {
      queryStringBuilder.append("   and exectype = :orderType ");
    }
    if (channel.equals("")) {
      channel = "%";
      queryStringBuilder.append("   and via like :channel ");
    } else {
      queryStringBuilder.append("   and via = :channel ");
    }
    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("txDate", txDate)
        .setParameter("custodyCd", custodyCd)
        .setParameter("afacctNo", afacctNo)
        .setParameter("orderType", orderType)
        .setParameter("channel", channel)
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
    queryStringBuilder.append(" FROM smy_dwh_flx_OD0085 ");

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
    Query<?> query = session.createNativeQuery("UPDATE smy_dwh_flx_OD0085 " +
      " SET EtlCurDate= ? , EtlRunDatetime=?  " +
      " WHERE EtlCurDate = (SELECT MAX(EtlCurDate) FROM smy_dwh_flx_OD0085) ");
    query.setParameter(1, etlDate);
    query.setParameter(2, etlTime);
    query.executeUpdate();
    trans.commit();
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
  @Column(name = "FULLNAME")
  public String getFullname() {
    return fullname;
  }

  public void setFullname(String fullname) {
    this.fullname = fullname;
  }

  @Basic
  @Column(name = "TXDATE")
  public Date getTxdate() {
    return txdate;
  }

  public void setTxdate(Date txdate) {
    this.txdate = txdate;
  }

  @Basic
  @Column(name = "CLEARDATE")
  public Date getCleardate() {
    return cleardate;
  }

  public void setCleardate(Date cleardate) {
    this.cleardate = cleardate;
  }

  @Basic
  @Column(name = "exectype")
  public String getExectype() {
    return exectype;
  }

  public void setExectype(String exectype) {
    this.exectype = exectype;
  }

  @Basic
  @Column(name = "CODEID")
  public String getCodeid() {
    return codeid;
  }

  public void setCodeid(String codeid) {
    this.codeid = codeid;
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
  @Column(name = "MATCHPRICE")
  public Long getMatchprice() {
    return matchprice;
  }

  public void setMatchprice(Long matchprice) {
    this.matchprice = matchprice;
  }

  @Basic
  @Column(name = "MATCHQTTY")
  public Long getMatchqtty() {
    return matchqtty;
  }

  public void setMatchqtty(Long matchqtty) {
    this.matchqtty = matchqtty;
  }

  @Basic
  @Column(name = "via")
  public String getVia() {
    return via;
  }

  public void setVia(String via) {
    this.via = via;
  }

  @Basic
  @Column(name = "matchamt")
  public Long getMatchamt() {
    return matchamt;
  }

  public void setMatchamt(Long matchamt) {
    this.matchamt = matchamt;
  }

  @Basic
  @Column(name = "FEERATE")
  public BigDecimal getFeerate() {
    return feerate;
  }

  public void setFeerate(BigDecimal feerate) {
    this.feerate = feerate;
  }

  @Basic
  @Column(name = "TAXRATE")
  public Double getTaxrate() {
    return taxrate;
  }

  public void setTaxrate(Double taxrate) {
    this.taxrate = taxrate;
  }

  @Basic
  @Column(name = "iodfeeacr")
  public BigDecimal getIodfeeacr() {
    return iodfeeacr;
  }

  public void setIodfeeacr(BigDecimal iodfeeacr) {
    this.iodfeeacr = iodfeeacr;
  }

  @Basic
  @Column(name = "iodtaxsellamt")
  public Double getIodtaxsellamt() {
    return iodtaxsellamt;
  }

  public void setIodtaxsellamt(Double iodtaxsellamt) {
    this.iodtaxsellamt = iodtaxsellamt;
  }

  @Basic
  @Column(name = "orderid")
  public String getOrderid() {
    return orderid;
  }

  public void setOrderid(String orderid) {
    this.orderid = orderid;
  }

  @Basic
  @Column(name = "orderqtty")
  public Long getOrderqtty() {
    return orderqtty;
  }

  public void setOrderqtty(Long orderqtty) {
    this.orderqtty = orderqtty;
  }

  @Basic
  @Column(name = "quoteprice")
  public Long getQuoteprice() {
    return quoteprice;
  }

  public void setQuoteprice(Long quoteprice) {
    this.quoteprice = quoteprice;
  }

  @Basic
  @Column(name = "rAmt")
  public Double getrAmt() {
    return rAmt;
  }

  public void setrAmt(Double rAmt) {
    this.rAmt = rAmt;
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
    SmyDwhFlxOd0085Entity that = (SmyDwhFlxOd0085Entity) o;
    return etlCurDate == that.etlCurDate &&
      Objects.equals(custodycd, that.custodycd) &&
      Objects.equals(afacctno, that.afacctno) &&
      Objects.equals(fullname, that.fullname) &&
      Objects.equals(txdate, that.txdate) &&
      Objects.equals(cleardate, that.cleardate) &&
      Objects.equals(exectype, that.exectype) &&
      Objects.equals(codeid, that.codeid) &&
      Objects.equals(symbol, that.symbol) &&
      Objects.equals(matchprice, that.matchprice) &&
      Objects.equals(matchqtty, that.matchqtty) &&
      Objects.equals(via, that.via) &&
      Objects.equals(matchamt, that.matchamt) &&
      Objects.equals(feerate, that.feerate) &&
      Objects.equals(taxrate, that.taxrate) &&
      Objects.equals(iodfeeacr, that.iodfeeacr) &&
      Objects.equals(iodtaxsellamt, that.iodtaxsellamt) &&
      Objects.equals(orderid, that.orderid) &&
      Objects.equals(orderqtty, that.orderqtty) &&
      Objects.equals(quoteprice, that.quoteprice) &&
      Objects.equals(rAmt, that.rAmt) &&
      Objects.equals(etlRunDatetime, that.etlRunDatetime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(custodycd, afacctno, fullname, txdate, cleardate, exectype, codeid, symbol, matchprice, matchqtty, via, matchamt, feerate, taxrate, iodfeeacr, iodtaxsellamt, orderid,
      orderqtty,
      quoteprice, rAmt, etlCurDate, etlRunDatetime);
  }
}
