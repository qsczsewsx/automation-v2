package com.tcbs.automation.dwh.biz;

import com.tcbs.automation.dwh.Dwh;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@Entity
@Table(name = "RT_tcb_BOND")
public class RtTcbBondEntity {
  @Id
  private Integer id;
  private String name;
  private String code;
  private Integer price;
  private Timestamp publicdate;
  private Timestamp expireddate;
  private String seriescontractcode;
  private String seriesbondcode;
  private Timestamp firstinterestdate;
  private BigDecimal interestbuyback;
  private String description;
  private String notetype;
  private String noteinterest;
  private String notepayinterest;
  private Timestamp createddate;
  private Timestamp updateddate;
  private Integer active;
  private Integer issuerid;
  private Timestamp lastperiodtrading;
  private Integer baseinterestbottom;
  private Integer baseinteresttop;
  private Integer ispaymentcouponendterm;
  private Integer couponpayment;
  private Integer iscoupon;
  private Integer guarantee;
  private Timestamp couponeffectdate;
  private Timestamp fistdateperiodpl;
  private Timestamp lastdateperiodpl;
  private Float floatbandinterest;
  private Float referenceratecoupon;
  private Integer trustasset;
  private Integer listedstatus;
  private String guaranteevalue;
  private String trustassetvalue;
  private Integer buildbookstatus;
  private String listedcode;
  private Integer frozenstatus;
  private String paymentterm;
  private Integer publicoffering;
  private String sectype;


  public static void insertRtBond(RtTcbBondEntity entity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    entity.setCreateddate(entity.getCreateddate() == null ? new Timestamp(System.currentTimeMillis()) : entity.getCreateddate());
    entity.setUpdateddate(entity.getUpdateddate() == null ? new Timestamp(System.currentTimeMillis()) : entity.getUpdateddate());
    Query<?> query = session.createNativeQuery("insert into RT_tcb_BOND " +
      "(ID, NAME, CODE, PRICE, PUBLICDATE, EXPIREDDATE, SERIESBONDCODE, NOTETYPE, NOTEINTEREST, NOTEPAYINTEREST, CREATEDDATE, " +
      "UPDATEDDATE, ACTIVE, ISSUERID, BASEINTERESTBOTTOM, BASEINTERESTTOP, ISPAYMENTCOUPONENDTERM, COUPONPAYMENT, " +
      "ISCOUPON, GUARANTEE, COUPONEFFECTDATE, FISTDATEPERIODPL, LASTDATEPERIODPL, FLOATBANDINTEREST, " +
      "REFERENCERATECOUPON, TRUSTASSET, LISTEDSTATUS, GUARANTEEVALUE, TRUSTASSETVALUE, BUILDBOOKSTATUS, LISTEDCODE," +
      " FROZENSTATUS, PAYMENTTERM, SECTYPE) " +
      "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    query.setParameter(1, entity.getId());
    query.setParameter(2, entity.getName());
    query.setParameter(3, entity.getCode());
    query.setParameter(4, entity.getPrice());
    query.setParameter(5, entity.getPublicdate());
    query.setParameter(6, entity.getExpireddate());
    query.setParameter(7, entity.getSeriesbondcode());
    query.setParameter(8, entity.getNotetype());
    query.setParameter(9, entity.getNoteinterest());
    query.setParameter(10, entity.getNotepayinterest());
    query.setParameter(11, entity.getCreateddate());
    query.setParameter(12, entity.getUpdateddate());
    query.setParameter(13, entity.getActive());
    query.setParameter(14, entity.getIssuerid());
    query.setParameter(15, entity.getBaseinterestbottom());
    query.setParameter(16, entity.getBaseinteresttop());
    query.setParameter(17, entity.getIspaymentcouponendterm());
    query.setParameter(18, entity.getCouponpayment());
    query.setParameter(19, entity.getIscoupon());
    query.setParameter(20, entity.getGuarantee());
    query.setParameter(21, entity.getCouponeffectdate());
    query.setParameter(22, entity.getFirstinterestdate());
    query.setParameter(23, entity.getLastdateperiodpl());
    query.setParameter(24, entity.getFloatbandinterest());
    query.setParameter(25, entity.getReferenceratecoupon());
    query.setParameter(26, entity.getTrustasset());
    query.setParameter(27, entity.getListedstatus());
    query.setParameter(28, entity.getGuaranteevalue());
    query.setParameter(29, entity.getTrustassetvalue());
    query.setParameter(30, entity.getBuildbookstatus());
    query.setParameter(31, entity.getListedcode());
    query.setParameter(32, entity.getFrozenstatus());
    query.setParameter(33, entity.getPaymentterm());
    query.setParameter(34, entity.getSectype());

    query.executeUpdate();
    session.getTransaction().commit();
    Dwh.dwhDbConnection.closeSession();
  }

  public static void deleteData(RtTcbBondEntity entity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<?> query = session.createNativeQuery("DELETE RT_tcb_BOND WHERE ID = :id ");
    query.setParameter("id", entity.getId());
    query.executeUpdate();
    session.getTransaction().commit();
    Dwh.dwhDbConnection.closeSession();
  }

  @Basic
  @Column(name = "ID", nullable = true, precision = 0)
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Basic
  @Column(name = "NAME", nullable = true, length = 120)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Basic
  @Column(name = "CODE", nullable = true, length = 60)
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Basic
  @Column(name = "PRICE", nullable = true, precision = 0)
  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  @Basic
  @Column(name = "PUBLICDATE", nullable = true)
  public Timestamp getPublicdate() {
    return publicdate;
  }

  public void setPublicdate(Timestamp publicdate) {
    this.publicdate = publicdate;
  }

  @Basic
  @Column(name = "EXPIREDDATE", nullable = true)
  public Timestamp getExpireddate() {
    return expireddate;
  }

  public void setExpireddate(Timestamp expireddate) {
    this.expireddate = expireddate;
  }

  @Basic
  @Column(name = "SERIESCONTRACTCODE", nullable = true, length = 80)
  public String getSeriescontractcode() {
    return seriescontractcode;
  }

  public void setSeriescontractcode(String seriescontractcode) {
    this.seriescontractcode = seriescontractcode;
  }

  @Basic
  @Column(name = "SERIESBONDCODE", nullable = true, length = 80)
  public String getSeriesbondcode() {
    return seriesbondcode;
  }

  public void setSeriesbondcode(String seriesbondcode) {
    this.seriesbondcode = seriesbondcode;
  }

  @Basic
  @Column(name = "FIRSTINTERESTDATE", nullable = true)
  public Timestamp getFirstinterestdate() {
    return firstinterestdate;
  }

  public void setFirstinterestdate(Timestamp firstinterestdate) {
    this.firstinterestdate = firstinterestdate;
  }

  @Basic
  @Column(name = "INTERESTBUYBACK", nullable = true, precision = 4)
  public BigDecimal getInterestbuyback() {
    return interestbuyback;
  }

  public void setInterestbuyback(BigDecimal interestbuyback) {
    this.interestbuyback = interestbuyback;
  }

  @Basic
  @Column(name = "DESCRIPTION", nullable = true, length = 2000)
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Basic
  @Column(name = "NOTETYPE", nullable = true, length = 2000)
  public String getNotetype() {
    return notetype;
  }

  public void setNotetype(String notetype) {
    this.notetype = notetype;
  }

  @Basic
  @Column(name = "NOTEINTEREST", nullable = true, length = 2000)
  public String getNoteinterest() {
    return noteinterest;
  }

  public void setNoteinterest(String noteinterest) {
    this.noteinterest = noteinterest;
  }

  @Basic
  @Column(name = "NOTEPAYINTEREST", nullable = true, length = 2000)
  public String getNotepayinterest() {
    return notepayinterest;
  }

  public void setNotepayinterest(String notepayinterest) {
    this.notepayinterest = notepayinterest;
  }

  @Basic
  @Column(name = "CREATEDDATE", nullable = true)
  public Timestamp getCreateddate() {
    return createddate;
  }

  public void setCreateddate(Timestamp createddate) {
    this.createddate = createddate;
  }

  @Basic
  @Column(name = "UPDATEDDATE", nullable = true)
  public Timestamp getUpdateddate() {
    return updateddate;
  }

  public void setUpdateddate(Timestamp updateddate) {
    this.updateddate = updateddate;
  }

  @Basic
  @Column(name = "ACTIVE", nullable = true, precision = 0)
  public Integer getActive() {
    return active;
  }

  public void setActive(Integer active) {
    this.active = active;
  }

  @Basic
  @Column(name = "ISSUERID", nullable = true, precision = 0)
  public Integer getIssuerid() {
    return issuerid;
  }

  public void setIssuerid(Integer issuerid) {
    this.issuerid = issuerid;
  }

  @Basic
  @Column(name = "LASTPERIODTRADING", nullable = true)
  public Timestamp getLastperiodtrading() {
    return lastperiodtrading;
  }

  public void setLastperiodtrading(Timestamp lastperiodtrading) {
    this.lastperiodtrading = lastperiodtrading;
  }

  @Basic
  @Column(name = "BASEINTERESTBOTTOM", nullable = true, precision = 0)
  public Integer getBaseinterestbottom() {
    return baseinterestbottom;
  }

  public void setBaseinterestbottom(Integer baseinterestbottom) {
    this.baseinterestbottom = baseinterestbottom;
  }

  @Basic
  @Column(name = "BASEINTERESTTOP", nullable = true, precision = 0)
  public Integer getBaseinteresttop() {
    return baseinteresttop;
  }

  public void setBaseinteresttop(Integer baseinteresttop) {
    this.baseinteresttop = baseinteresttop;
  }

  @Basic
  @Column(name = "ISPAYMENTCOUPONENDTERM", nullable = true, precision = 0)
  public Integer getIspaymentcouponendterm() {
    return ispaymentcouponendterm;
  }

  public void setIspaymentcouponendterm(Integer ispaymentcouponendterm) {
    this.ispaymentcouponendterm = ispaymentcouponendterm;
  }

  @Basic
  @Column(name = "COUPONPAYMENT", nullable = true, precision = 0)
  public Integer getCouponpayment() {
    return couponpayment;
  }

  public void setCouponpayment(Integer couponpayment) {
    this.couponpayment = couponpayment;
  }

  @Basic
  @Column(name = "ISCOUPON", nullable = true, precision = 0)
  public Integer getIscoupon() {
    return iscoupon;
  }

  public void setIscoupon(Integer iscoupon) {
    this.iscoupon = iscoupon;
  }

  @Basic
  @Column(name = "GUARANTEE", nullable = true, precision = 0)
  public Integer getGuarantee() {
    return guarantee;
  }

  public void setGuarantee(Integer guarantee) {
    this.guarantee = guarantee;
  }

  @Basic
  @Column(name = "COUPONEFFECTDATE", nullable = true)
  public Timestamp getCouponeffectdate() {
    return couponeffectdate;
  }

  public void setCouponeffectdate(Timestamp couponeffectdate) {
    this.couponeffectdate = couponeffectdate;
  }

  @Basic
  @Column(name = "FISTDATEPERIODPL", nullable = true)
  public Timestamp getFistdateperiodpl() {
    return fistdateperiodpl;
  }

  public void setFistdateperiodpl(Timestamp fistdateperiodpl) {
    this.fistdateperiodpl = fistdateperiodpl;
  }

  @Basic
  @Column(name = "LASTDATEPERIODPL", nullable = true)
  public Timestamp getLastdateperiodpl() {
    return lastdateperiodpl;
  }

  public void setLastdateperiodpl(Timestamp lastdateperiodpl) {
    this.lastdateperiodpl = lastdateperiodpl;
  }

  @Basic
  @Column(name = "FLOATBANDINTEREST", nullable = true, precision = 0)
  public Float getFloatbandinterest() {
    return floatbandinterest;
  }

  public void setFloatbandinterest(Float floatbandinterest) {
    this.floatbandinterest = floatbandinterest;
  }

  @Basic
  @Column(name = "REFERENCERATECOUPON", nullable = true, precision = 0)
  public Float getReferenceratecoupon() {
    return referenceratecoupon;
  }

  public void setReferenceratecoupon(Float referenceratecoupon) {
    this.referenceratecoupon = referenceratecoupon;
  }

  @Basic
  @Column(name = "TRUSTASSET", nullable = true, precision = 0)
  public Integer getTrustasset() {
    return trustasset;
  }

  public void setTrustasset(Integer trustasset) {
    this.trustasset = trustasset;
  }

  @Basic
  @Column(name = "LISTEDSTATUS", nullable = true, precision = 0)
  public Integer getListedstatus() {
    return listedstatus;
  }

  public void setListedstatus(Integer listedstatus) {
    this.listedstatus = listedstatus;
  }

  @Basic
  @Column(name = "GUARANTEEVALUE", nullable = true, length = 200)
  public String getGuaranteevalue() {
    return guaranteevalue;
  }

  public void setGuaranteevalue(String guaranteevalue) {
    this.guaranteevalue = guaranteevalue;
  }

  @Basic
  @Column(name = "TRUSTASSETVALUE", nullable = true, length = 200)
  public String getTrustassetvalue() {
    return trustassetvalue;
  }

  public void setTrustassetvalue(String trustassetvalue) {
    this.trustassetvalue = trustassetvalue;
  }

  @Basic
  @Column(name = "BUILDBOOKSTATUS", nullable = true, precision = 0)
  public Integer getBuildbookstatus() {
    return buildbookstatus;
  }

  public void setBuildbookstatus(Integer buildbookstatus) {
    this.buildbookstatus = buildbookstatus;
  }

  @Basic
  @Column(name = "LISTEDCODE", nullable = true, length = 50)
  public String getListedcode() {
    return listedcode;
  }

  public void setListedcode(String listedcode) {
    this.listedcode = listedcode;
  }

  @Basic
  @Column(name = "FROZENSTATUS", nullable = true, precision = 0)
  public Integer getFrozenstatus() {
    return frozenstatus;
  }

  public void setFrozenstatus(Integer frozenstatus) {
    this.frozenstatus = frozenstatus;
  }

  @Basic
  @Column(name = "PAYMENTTERM", nullable = true, length = 2000)
  public String getPaymentterm() {
    return paymentterm;
  }

  public void setPaymentterm(String paymentterm) {
    this.paymentterm = paymentterm;
  }

  @Basic
  @Column(name = "PUBLICOFFERING", nullable = true, precision = 0)
  public Integer getPublicoffering() {
    return publicoffering;
  }

  public void setPublicoffering(Integer publicoffering) {
    this.publicoffering = publicoffering;
  }

  @Basic
  @Column(name = "SECTYPE", nullable = true, length = 3)
  public String getSectype() {
    return sectype;
  }

  public void setSectype(String sectype) {
    this.sectype = sectype;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RtTcbBondEntity that = (RtTcbBondEntity) o;
    return Objects.equals(id, that.id) &&
      Objects.equals(name, that.name) &&
      Objects.equals(code, that.code) &&
      Objects.equals(price, that.price) &&
      Objects.equals(publicdate, that.publicdate) &&
      Objects.equals(expireddate, that.expireddate) &&
      Objects.equals(seriescontractcode, that.seriescontractcode) &&
      Objects.equals(seriesbondcode, that.seriesbondcode) &&
      Objects.equals(firstinterestdate, that.firstinterestdate) &&
      Objects.equals(interestbuyback, that.interestbuyback) &&
      Objects.equals(description, that.description) &&
      Objects.equals(notetype, that.notetype) &&
      Objects.equals(noteinterest, that.noteinterest) &&
      Objects.equals(notepayinterest, that.notepayinterest) &&
      Objects.equals(createddate, that.createddate) &&
      Objects.equals(updateddate, that.updateddate) &&
      Objects.equals(active, that.active) &&
      Objects.equals(issuerid, that.issuerid) &&
      Objects.equals(lastperiodtrading, that.lastperiodtrading) &&
      Objects.equals(baseinterestbottom, that.baseinterestbottom) &&
      Objects.equals(baseinteresttop, that.baseinteresttop) &&
      Objects.equals(ispaymentcouponendterm, that.ispaymentcouponendterm) &&
      Objects.equals(couponpayment, that.couponpayment) &&
      Objects.equals(iscoupon, that.iscoupon) &&
      Objects.equals(guarantee, that.guarantee) &&
      Objects.equals(couponeffectdate, that.couponeffectdate) &&
      Objects.equals(fistdateperiodpl, that.fistdateperiodpl) &&
      Objects.equals(lastdateperiodpl, that.lastdateperiodpl) &&
      Objects.equals(floatbandinterest, that.floatbandinterest) &&
      Objects.equals(referenceratecoupon, that.referenceratecoupon) &&
      Objects.equals(trustasset, that.trustasset) &&
      Objects.equals(listedstatus, that.listedstatus) &&
      Objects.equals(guaranteevalue, that.guaranteevalue) &&
      Objects.equals(trustassetvalue, that.trustassetvalue) &&
      Objects.equals(buildbookstatus, that.buildbookstatus) &&
      Objects.equals(listedcode, that.listedcode) &&
      Objects.equals(frozenstatus, that.frozenstatus) &&
      Objects.equals(paymentterm, that.paymentterm) &&
      Objects.equals(publicoffering, that.publicoffering) &&
      Objects.equals(sectype, that.sectype);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, code, price, publicdate, expireddate, seriescontractcode, seriesbondcode, firstinterestdate, interestbuyback, description, notetype, noteinterest, notepayinterest,
      createddate, updateddate, active, issuerid, lastperiodtrading, baseinterestbottom, baseinteresttop, ispaymentcouponendterm, couponpayment, iscoupon, guarantee, couponeffectdate,
      fistdateperiodpl, lastdateperiodpl, floatbandinterest, referenceratecoupon, trustasset, listedstatus, guaranteevalue, trustassetvalue, buildbookstatus, listedcode, frozenstatus, paymentterm,
      publicoffering, sectype);
  }
}
