package com.tcbs.automation.iris.impact;

import com.tcbs.automation.staging.AwsStagingDwh;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "ris_el_bonus_shares", schema = "dbo", catalog = "tcbs-staging")
public class StgRisElBonusSharesEntity {
  private String afacctno;
  private String symbol;
  private String catype;
  private BigDecimal caass;
  private BigDecimal caamt;
  private BigDecimal qtty;
  private BigDecimal pqtty;
  private Integer approved;
  private Date duedate;
  private Date exdate;
  private Date enddate;
  private Integer etlcurdate;
  private Timestamp etlrundatetime;

  @Step("insert data")
  public static void insertData(StgRisElBonusSharesEntity entity) {
    Session session = AwsStagingDwh.awsStagingDwhDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append(" INSERT INTO ris_el_bonus_shares");

    queryStringBuilder.append("(afacctno, symbol, caass, caamt, qtty,pqtty, approved, duedate, etlcurdate) ");
    queryStringBuilder.append("values (?, ?, ?, ?, ?, ?, ?, ? , ?)");

    org.hibernate.query.Query<?> query = session.createNativeQuery(queryStringBuilder.toString());
    query.setParameter(1, entity.afacctno);
    query.setParameter(2, entity.getSymbol());
    query.setParameter(3, entity.getCaass());
    query.setParameter(4, entity.getCaamt());
    query.setParameter(5, entity.getQtty());
    query.setParameter(6, entity.getPqtty());
    query.setParameter(7, entity.getApproved());
    query.setParameter(8, entity.getDuedate());
    query.setParameter(9, entity.getEtlcurdate());
    query.executeUpdate();
    trans.commit();
  }

  @Step("delete data by object")
  public static void deleteData(StgRisElBonusSharesEntity entity) {
    Session session = AwsStagingDwh.awsStagingDwhDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query;


    query = session.createNativeQuery(" DELETE from ris_el_bonus_shares where etlcurdate = :etlcurdate ");

    query.setParameter("etlcurdate", entity.getEtlcurdate());
    query.executeUpdate();
    trans.commit();
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
  @Column(name = "catype")
  public String getCatype() {
    return catype;
  }

  public void setCatype(String catype) {
    this.catype = catype;
  }

  @Basic
  @Column(name = "caass")
  public BigDecimal getCaass() {
    return caass;
  }

  public void setCaass(BigDecimal caass) {
    this.caass = caass;
  }

  @Basic
  @Column(name = "caamt")
  public BigDecimal getCaamt() {
    return caamt;
  }

  public void setCaamt(BigDecimal caamt) {
    this.caamt = caamt;
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
  @Column(name = "pqtty")
  public BigDecimal getPqtty() {
    return pqtty;
  }

  public void setPqtty(BigDecimal pqtty) {
    this.pqtty = pqtty;
  }

  @Basic
  @Column(name = "approved")
  public Integer getApproved() {
    return approved;
  }

  public void setApproved(Integer approved) {
    this.approved = approved;
  }

  @Basic
  @Column(name = "duedate")
  public Date getDuedate() {
    return duedate;
  }

  public void setDuedate(Date duedate) {
    this.duedate = duedate;
  }

  @Basic
  @Column(name = "exdate")
  public Date getExdate() {
    return exdate;
  }

  public void setExdate(Date exdate) {
    this.exdate = exdate;
  }

  @Basic
  @Column(name = "enddate")
  public Date getEnddate() {
    return enddate;
  }

  public void setEnddate(Date enddate) {
    this.enddate = enddate;
  }

  @Basic
  @Column(name = "etlcurdate")
  public Integer getEtlcurdate() {
    return etlcurdate;
  }

  public void setEtlcurdate(Integer etlcurdate) {
    this.etlcurdate = etlcurdate;
  }

  @Basic
  @Column(name = "etlrundatetime")
  public Timestamp getEtlrundatetime() {
    return etlrundatetime;
  }

  public void setEtlrundatetime(Timestamp etlrundatetime) {
    this.etlrundatetime = etlrundatetime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StgRisElBonusSharesEntity that = (StgRisElBonusSharesEntity) o;
    return Objects.equals(afacctno, that.afacctno) && Objects.equals(symbol, that.symbol) && Objects.equals(catype, that.catype) && Objects.equals(caass,
      that.caass) && Objects.equals(caamt, that.caamt) && Objects.equals(qtty, that.qtty) && Objects.equals(pqtty, that.pqtty) && Objects.equals(approved,
      that.approved) && Objects.equals(duedate, that.duedate) && Objects.equals(exdate, that.exdate) && Objects.equals(enddate, that.enddate) && Objects.equals(
      etlcurdate, that.etlcurdate) && Objects.equals(etlrundatetime, that.etlrundatetime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(afacctno, symbol, catype, caass, caamt, qtty, pqtty, approved, duedate, exdate, enddate, etlcurdate, etlrundatetime);
  }
}
