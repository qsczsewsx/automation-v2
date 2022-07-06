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
@Table(name = "RT_tcb_TRADING")
public class RtTcbTradingEntity {
  private Integer id;
  private String tradingcode;
  private Integer action;
  private Timestamp tradingdate;
  private Integer bondproductid;
  private Integer price;
  private Integer customerid;
  private Integer quantity;
  private Integer userid;
  private Integer operationstatus;
  private Integer accountantstatus;
  private Integer agencystatus;
  private Integer supervisorstatus;
  private Timestamp createddate;
  private Integer rateid;
  private Integer active;
  private BigDecimal accruedinterest;
  private Integer agencyid;
  private BigDecimal commissionfee;
  private String identityid;
  private String identitycard;
  private Integer quality;
  private Integer customerstatus;
  private Integer listedbond;
  private Integer prorataquantity;
  private Integer prorataprice;
  private Integer flexprice;
  private Integer unitprice2;
  private Integer principal;
  private Timestamp accdate;
  private Timestamp buildbookdate;
  private Integer buildbookprice;
  private Integer status;
  private Integer currentqueue;
  private Integer disbursementmethod;
  private Integer forcesellstatus;
  private Timestamp loanreleasedate;
  private Integer loanvalue;
  private Integer opqueuestatus;
  private String processorid;
  private Integer queuestatus;
  private Integer referenceassetsid;
  private Integer waitingstatus;
  private Integer confirmtype;
  private Float transactionfee;
  private Integer incometax;
  private Integer feesupport;
  private Integer taxsupport;
  private Timestamp matchingdate;
  private Timestamp transferdate;
  private Integer exception;
  private Integer counterparty;
  private String reason;
  private Integer frozen;
  private Integer depositorystatus;
  private Integer marketstatus;
  private String bundlestatus;
  private Integer collateralstatus;
  private Integer orderstatus;
  private Integer cpstatus;
  private String referencenumber;
  private Integer convertstatus;
  private Integer buildbook;
  private Integer buildbookstatus;
  private Integer referencestatus;
  private String salesidFromtcb;
  private String agencycodeFromtcb;
  private String referee;
  private Integer assetholdid;
  private Timestamp updateddate;
  private BigDecimal unitpriceclean;
  private Integer tradingSource;
  private Float yieldtomaturity;
  private Float ytmcouponreinvested;
  private String accountNo;

  public static boolean insertRtTrading(RtTcbTradingEntity entity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    entity.setTradingdate(entity.getTradingdate() == null ? new Timestamp(System.currentTimeMillis()) : entity.getTradingdate());
    entity.setCreateddate(entity.getCreateddate() == null ? new Timestamp(System.currentTimeMillis()) : entity.getCreateddate());
    entity.setAccdate(entity.getAccdate() == null ? new Timestamp(System.currentTimeMillis()) : entity.getAccdate());
    entity.setMatchingdate(entity.getMatchingdate() == null ? new Timestamp(System.currentTimeMillis()) : entity.getMatchingdate());
    entity.setTransferdate(entity.getTransferdate() == null ? new Timestamp(System.currentTimeMillis()) : entity.getTransferdate());
    entity.setUpdateddate(entity.getUpdateddate() == null ? new Timestamp(System.currentTimeMillis()) : entity.getUpdateddate());
    session.save(entity);
    session.getTransaction().commit();
    return true;

  }

  public static void deleteData(RtTcbTradingEntity entity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<?> query = session.createNativeQuery("DELETE RT_tcb_TRADING WHERE ID = :orderId ");
    query.setParameter("orderId", entity.getId());
    query.executeUpdate();
    session.getTransaction().commit();
    Dwh.dwhDbConnection.closeSession();
  }

  @Basic
  @Id
  @Column(name = "ID")
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  @Basic
  @Column(name = "TRADINGCODE")
  public String getTradingcode() {
    return tradingcode;
  }

  public void setTradingcode(String tradingcode) {
    this.tradingcode = tradingcode;
  }

  @Basic
  @Column(name = "ACTION")
  public Integer getAction() {
    return action;
  }

  public void setAction(Integer action) {
    this.action = action;
  }

  @Basic
  @Column(name = "TRADINGDATE")
  public Timestamp getTradingdate() {
    return tradingdate;
  }

  public void setTradingdate(Timestamp tradingdate) {
    this.tradingdate = tradingdate;
  }

  @Basic
  @Column(name = "BONDPRODUCTID")
  public Integer getBondproductid() {
    return bondproductid;
  }

  public void setBondproductid(Integer bondproductid) {
    this.bondproductid = bondproductid;
  }

  @Basic
  @Column(name = "PRICE")
  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  @Basic
  @Column(name = "CUSTOMERID")
  public Integer getCustomerid() {
    return customerid;
  }

  public void setCustomerid(Integer customerid) {
    this.customerid = customerid;
  }

  @Basic
  @Column(name = "QUANTITY")
  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  @Basic
  @Column(name = "USERID")
  public Integer getUserid() {
    return userid;
  }

  public void setUserid(Integer userid) {
    this.userid = userid;
  }

  @Basic
  @Column(name = "OPERATIONSTATUS")
  public Integer getOperationstatus() {
    return operationstatus;
  }

  public void setOperationstatus(Integer operationstatus) {
    this.operationstatus = operationstatus;
  }

  @Basic
  @Column(name = "ACCOUNTANTSTATUS")
  public Integer getAccountantstatus() {
    return accountantstatus;
  }

  public void setAccountantstatus(Integer accountantstatus) {
    this.accountantstatus = accountantstatus;
  }

  @Basic
  @Column(name = "AGENCYSTATUS")
  public Integer getAgencystatus() {
    return agencystatus;
  }

  public void setAgencystatus(Integer agencystatus) {
    this.agencystatus = agencystatus;
  }

  @Basic
  @Column(name = "SUPERVISORSTATUS")
  public Integer getSupervisorstatus() {
    return supervisorstatus;
  }

  public void setSupervisorstatus(Integer supervisorstatus) {
    this.supervisorstatus = supervisorstatus;
  }

  @Basic
  @Column(name = "CREATEDDATE")
  public Timestamp getCreateddate() {
    return createddate;
  }

  public void setCreateddate(Timestamp createddate) {
    this.createddate = createddate;
  }

  @Basic
  @Column(name = "RATEID")
  public Integer getRateid() {
    return rateid;
  }

  public void setRateid(Integer rateid) {
    this.rateid = rateid;
  }

  @Basic
  @Column(name = "ACTIVE")
  public Integer getActive() {
    return active;
  }

  public void setActive(Integer active) {
    this.active = active;
  }

  @Basic
  @Column(name = "ACCRUEDINTEREST")
  public BigDecimal getAccruedinterest() {
    return accruedinterest;
  }

  public void setAccruedinterest(BigDecimal accruedinterest) {
    this.accruedinterest = accruedinterest;
  }

  @Basic
  @Column(name = "AGENCYID")
  public Integer getAgencyid() {
    return agencyid;
  }

  public void setAgencyid(Integer agencyid) {
    this.agencyid = agencyid;
  }

  @Basic
  @Column(name = "COMMISSIONFEE")
  public BigDecimal getCommissionfee() {
    return commissionfee;
  }

  public void setCommissionfee(BigDecimal commissionfee) {
    this.commissionfee = commissionfee;
  }

  @Basic
  @Column(name = "IDENTITYID")
  public String getIdentityid() {
    return identityid;
  }

  public void setIdentityid(String identityid) {
    this.identityid = identityid;
  }

  @Basic
  @Column(name = "IDENTITYCARD")
  public String getIdentitycard() {
    return identitycard;
  }

  public void setIdentitycard(String identitycard) {
    this.identitycard = identitycard;
  }

  @Basic
  @Column(name = "QUALITY")
  public Integer getQuality() {
    return quality;
  }

  public void setQuality(Integer quality) {
    this.quality = quality;
  }

  @Basic
  @Column(name = "CUSTOMERSTATUS")
  public Integer getCustomerstatus() {
    return customerstatus;
  }

  public void setCustomerstatus(Integer customerstatus) {
    this.customerstatus = customerstatus;
  }

  @Basic
  @Column(name = "LISTEDBOND")
  public Integer getListedbond() {
    return listedbond;
  }

  public void setListedbond(Integer listedbond) {
    this.listedbond = listedbond;
  }

  @Basic
  @Column(name = "PRORATAQUANTITY")
  public Integer getProrataquantity() {
    return prorataquantity;
  }

  public void setProrataquantity(Integer prorataquantity) {
    this.prorataquantity = prorataquantity;
  }

  @Basic
  @Column(name = "PRORATAPRICE")
  public Integer getProrataprice() {
    return prorataprice;
  }

  public void setProrataprice(Integer prorataprice) {
    this.prorataprice = prorataprice;
  }

  @Basic
  @Column(name = "FLEXPRICE")
  public Integer getFlexprice() {
    return flexprice;
  }

  public void setFlexprice(Integer flexprice) {
    this.flexprice = flexprice;
  }

  @Basic
  @Column(name = "UNITPRICE2")
  public Integer getUnitprice2() {
    return unitprice2;
  }

  public void setUnitprice2(Integer unitprice2) {
    this.unitprice2 = unitprice2;
  }

  @Basic
  @Column(name = "PRINCIPAL")
  public Integer getPrincipal() {
    return principal;
  }

  public void setPrincipal(Integer principal) {
    this.principal = principal;
  }

  @Basic
  @Column(name = "ACCDATE")
  public Timestamp getAccdate() {
    return accdate;
  }

  public void setAccdate(Timestamp accdate) {
    this.accdate = accdate;
  }

  @Basic
  @Column(name = "BUILDBOOKDATE")
  public Timestamp getBuildbookdate() {
    return buildbookdate;
  }

  public void setBuildbookdate(Timestamp buildbookdate) {
    this.buildbookdate = buildbookdate;
  }

  @Basic
  @Column(name = "BUILDBOOKPRICE")
  public Integer getBuildbookprice() {
    return buildbookprice;
  }

  public void setBuildbookprice(Integer buildbookprice) {
    this.buildbookprice = buildbookprice;
  }

  @Basic
  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Basic
  @Column(name = "CURRENTQUEUE")
  public Integer getCurrentqueue() {
    return currentqueue;
  }

  public void setCurrentqueue(Integer currentqueue) {
    this.currentqueue = currentqueue;
  }

  @Basic
  @Column(name = "DISBURSEMENTMETHOD")
  public Integer getDisbursementmethod() {
    return disbursementmethod;
  }

  public void setDisbursementmethod(Integer disbursementmethod) {
    this.disbursementmethod = disbursementmethod;
  }

  @Basic
  @Column(name = "FORCESELLSTATUS")
  public Integer getForcesellstatus() {
    return forcesellstatus;
  }

  public void setForcesellstatus(Integer forcesellstatus) {
    this.forcesellstatus = forcesellstatus;
  }

  @Basic
  @Column(name = "LOANRELEASEDATE")
  public Timestamp getLoanreleasedate() {
    return loanreleasedate;
  }

  public void setLoanreleasedate(Timestamp loanreleasedate) {
    this.loanreleasedate = loanreleasedate;
  }

  @Basic
  @Column(name = "LOANVALUE")
  public Integer getLoanvalue() {
    return loanvalue;
  }

  public void setLoanvalue(Integer loanvalue) {
    this.loanvalue = loanvalue;
  }

  @Basic
  @Column(name = "OPQUEUESTATUS")
  public Integer getOpqueuestatus() {
    return opqueuestatus;
  }

  public void setOpqueuestatus(Integer opqueuestatus) {
    this.opqueuestatus = opqueuestatus;
  }

  @Basic
  @Column(name = "PROCESSORID")
  public String getProcessorid() {
    return processorid;
  }

  public void setProcessorid(String processorid) {
    this.processorid = processorid;
  }

  @Basic
  @Column(name = "QUEUESTATUS")
  public Integer getQueuestatus() {
    return queuestatus;
  }

  public void setQueuestatus(Integer queuestatus) {
    this.queuestatus = queuestatus;
  }

  @Basic
  @Column(name = "REFERENCEASSETSID")
  public Integer getReferenceassetsid() {
    return referenceassetsid;
  }

  public void setReferenceassetsid(Integer referenceassetsid) {
    this.referenceassetsid = referenceassetsid;
  }

  @Basic
  @Column(name = "WAITINGSTATUS")
  public Integer getWaitingstatus() {
    return waitingstatus;
  }

  public void setWaitingstatus(Integer waitingstatus) {
    this.waitingstatus = waitingstatus;
  }

  @Basic
  @Column(name = "CONFIRMTYPE")
  public Integer getConfirmtype() {
    return confirmtype;
  }

  public void setConfirmtype(Integer confirmtype) {
    this.confirmtype = confirmtype;
  }

  @Basic
  @Column(name = "TRANSACTIONFEE")
  public Float getTransactionfee() {
    return transactionfee;
  }

  public void setTransactionfee(Float transactionfee) {
    this.transactionfee = transactionfee;
  }

  @Basic
  @Column(name = "INCOMETAX")
  public Integer getIncometax() {
    return incometax;
  }

  public void setIncometax(Integer incometax) {
    this.incometax = incometax;
  }

  @Basic
  @Column(name = "FEESUPPORT")
  public Integer getFeesupport() {
    return feesupport;
  }

  public void setFeesupport(Integer feesupport) {
    this.feesupport = feesupport;
  }

  @Basic
  @Column(name = "TAXSUPPORT")
  public Integer getTaxsupport() {
    return taxsupport;
  }

  public void setTaxsupport(Integer taxsupport) {
    this.taxsupport = taxsupport;
  }

  @Basic
  @Column(name = "MATCHINGDATE")
  public Timestamp getMatchingdate() {
    return matchingdate;
  }

  public void setMatchingdate(Timestamp matchingdate) {
    this.matchingdate = matchingdate;
  }

  @Basic
  @Column(name = "TRANSFERDATE")
  public Timestamp getTransferdate() {
    return transferdate;
  }

  public void setTransferdate(Timestamp transferdate) {
    this.transferdate = transferdate;
  }

  @Basic
  @Column(name = "EXCEPTION")
  public Integer getException() {
    return exception;
  }

  public void setException(Integer exception) {
    this.exception = exception;
  }

  @Basic
  @Column(name = "COUNTERPARTY")
  public Integer getCounterparty() {
    return counterparty;
  }

  public void setCounterparty(Integer counterparty) {
    this.counterparty = counterparty;
  }

  @Basic
  @Column(name = "REASON")
  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  @Basic
  @Column(name = "FROZEN")
  public Integer getFrozen() {
    return frozen;
  }

  public void setFrozen(Integer frozen) {
    this.frozen = frozen;
  }

  @Basic
  @Column(name = "DEPOSITORYSTATUS")
  public Integer getDepositorystatus() {
    return depositorystatus;
  }

  public void setDepositorystatus(Integer depositorystatus) {
    this.depositorystatus = depositorystatus;
  }

  @Basic
  @Column(name = "MARKETSTATUS")
  public Integer getMarketstatus() {
    return marketstatus;
  }

  public void setMarketstatus(Integer marketstatus) {
    this.marketstatus = marketstatus;
  }

  @Basic
  @Column(name = "BUNDLESTATUS")
  public String getBundlestatus() {
    return bundlestatus;
  }

  public void setBundlestatus(String bundlestatus) {
    this.bundlestatus = bundlestatus;
  }

  @Basic
  @Column(name = "COLLATERALSTATUS")
  public Integer getCollateralstatus() {
    return collateralstatus;
  }

  public void setCollateralstatus(Integer collateralstatus) {
    this.collateralstatus = collateralstatus;
  }

  @Basic
  @Column(name = "ORDERSTATUS")
  public Integer getOrderstatus() {
    return orderstatus;
  }

  public void setOrderstatus(Integer orderstatus) {
    this.orderstatus = orderstatus;
  }

  @Basic
  @Column(name = "CPSTATUS")
  public Integer getCpstatus() {
    return cpstatus;
  }

  public void setCpstatus(Integer cpstatus) {
    this.cpstatus = cpstatus;
  }

  @Basic
  @Column(name = "REFERENCENUMBER")
  public String getReferencenumber() {
    return referencenumber;
  }

  public void setReferencenumber(String referencenumber) {
    this.referencenumber = referencenumber;
  }

  @Basic
  @Column(name = "CONVERTSTATUS")
  public Integer getConvertstatus() {
    return convertstatus;
  }

  public void setConvertstatus(Integer convertstatus) {
    this.convertstatus = convertstatus;
  }

  @Basic
  @Column(name = "BUILDBOOK")
  public Integer getBuildbook() {
    return buildbook;
  }

  public void setBuildbook(Integer buildbook) {
    this.buildbook = buildbook;
  }

  @Basic
  @Column(name = "BUILDBOOKSTATUS")
  public Integer getBuildbookstatus() {
    return buildbookstatus;
  }

  public void setBuildbookstatus(Integer buildbookstatus) {
    this.buildbookstatus = buildbookstatus;
  }

  @Basic
  @Column(name = "REFERENCESTATUS")
  public Integer getReferencestatus() {
    return referencestatus;
  }

  public void setReferencestatus(Integer referencestatus) {
    this.referencestatus = referencestatus;
  }

  @Basic
  @Column(name = "SALESID_FROMTCB")
  public String getSalesidFromtcb() {
    return salesidFromtcb;
  }

  public void setSalesidFromtcb(String salesidFromtcb) {
    this.salesidFromtcb = salesidFromtcb;
  }

  @Basic
  @Column(name = "AGENCYCODE_FROMTCB")
  public String getAgencycodeFromtcb() {
    return agencycodeFromtcb;
  }

  public void setAgencycodeFromtcb(String agencycodeFromtcb) {
    this.agencycodeFromtcb = agencycodeFromtcb;
  }

  @Basic
  @Column(name = "REFEREE")
  public String getReferee() {
    return referee;
  }

  public void setReferee(String referee) {
    this.referee = referee;
  }

  @Basic
  @Column(name = "ASSETHOLDID")
  public Integer getAssetholdid() {
    return assetholdid;
  }

  public void setAssetholdid(Integer assetholdid) {
    this.assetholdid = assetholdid;
  }

  @Basic
  @Column(name = "UPDATEDDATE")
  public Timestamp getUpdateddate() {
    return updateddate;
  }

  public void setUpdateddate(Timestamp updateddate) {
    this.updateddate = updateddate;
  }

  @Basic
  @Column(name = "UNITPRICECLEAN")
  public BigDecimal getUnitpriceclean() {
    return unitpriceclean;
  }

  public void setUnitpriceclean(BigDecimal unitpriceclean) {
    this.unitpriceclean = unitpriceclean;
  }

  @Basic
  @Column(name = "TRADING_SOURCE")
  public Integer getTradingSource() {
    return tradingSource;
  }

  public void setTradingSource(Integer tradingSource) {
    this.tradingSource = tradingSource;
  }

  @Basic
  @Column(name = "YIELDTOMATURITY")
  public Float getYieldtomaturity() {
    return yieldtomaturity;
  }

  public void setYieldtomaturity(Float yieldtomaturity) {
    this.yieldtomaturity = yieldtomaturity;
  }

  @Basic
  @Column(name = "YTMCOUPONREINVESTED")
  public Float getYtmcouponreinvested() {
    return ytmcouponreinvested;
  }

  public void setYtmcouponreinvested(Float ytmcouponreinvested) {
    this.ytmcouponreinvested = ytmcouponreinvested;
  }

  @Basic
  @Column(name = "ACCOUNT_NO")
  public String getAccountNo() {
    return accountNo;
  }

  public void setAccountNo(String accountNo) {
    this.accountNo = accountNo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    RtTcbTradingEntity that = (RtTcbTradingEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(tradingcode, that.tradingcode) && Objects.equals(action, that.action) && Objects.equals(tradingdate, that.tradingdate) && Objects.equals(
      bondproductid, that.bondproductid) && Objects.equals(price, that.price) && Objects.equals(customerid, that.customerid) && Objects.equals(quantity, that.quantity) && Objects.equals(userid,
      that.userid) && Objects.equals(operationstatus, that.operationstatus) && Objects.equals(accountantstatus, that.accountantstatus) && Objects.equals(agencystatus,
      that.agencystatus) && Objects.equals(supervisorstatus, that.supervisorstatus) && Objects.equals(createddate, that.createddate) && Objects.equals(rateid, that.rateid) && Objects.equals(active,
      that.active) && Objects.equals(accruedinterest, that.accruedinterest) && Objects.equals(agencyid, that.agencyid) && Objects.equals(commissionfee, that.commissionfee) && Objects.equals(
      identityid, that.identityid) && Objects.equals(identitycard, that.identitycard) && Objects.equals(quality, that.quality) && Objects.equals(customerstatus, that.customerstatus) && Objects.equals(
      listedbond, that.listedbond) && Objects.equals(prorataquantity, that.prorataquantity) && Objects.equals(prorataprice, that.prorataprice) && Objects.equals(flexprice,
      that.flexprice) && Objects.equals(unitprice2, that.unitprice2) && Objects.equals(principal, that.principal) && Objects.equals(accdate, that.accdate) && Objects.equals(buildbookdate,
      that.buildbookdate) && Objects.equals(buildbookprice, that.buildbookprice) && Objects.equals(status, that.status) && Objects.equals(currentqueue, that.currentqueue) && Objects.equals(
      disbursementmethod, that.disbursementmethod) && Objects.equals(forcesellstatus, that.forcesellstatus) && Objects.equals(loanreleasedate, that.loanreleasedate) && Objects.equals(loanvalue,
      that.loanvalue) && Objects.equals(opqueuestatus, that.opqueuestatus) && Objects.equals(processorid, that.processorid) && Objects.equals(queuestatus, that.queuestatus) && Objects.equals(
      referenceassetsid, that.referenceassetsid) && Objects.equals(waitingstatus, that.waitingstatus) && Objects.equals(confirmtype, that.confirmtype) && Objects.equals(transactionfee,
      that.transactionfee) && Objects.equals(incometax, that.incometax) && Objects.equals(feesupport, that.feesupport) && Objects.equals(taxsupport, that.taxsupport) && Objects.equals(matchingdate,
      that.matchingdate) && Objects.equals(transferdate, that.transferdate) && Objects.equals(exception, that.exception) && Objects.equals(counterparty, that.counterparty) && Objects.equals(reason,
      that.reason) && Objects.equals(frozen, that.frozen) && Objects.equals(depositorystatus, that.depositorystatus) && Objects.equals(marketstatus, that.marketstatus) && Objects.equals(bundlestatus,
      that.bundlestatus) && Objects.equals(collateralstatus, that.collateralstatus) && Objects.equals(orderstatus, that.orderstatus) && Objects.equals(cpstatus, that.cpstatus) && Objects.equals(
      referencenumber, that.referencenumber) && Objects.equals(convertstatus, that.convertstatus) && Objects.equals(buildbook, that.buildbook) && Objects.equals(buildbookstatus,
      that.buildbookstatus) && Objects.equals(referencestatus, that.referencestatus) && Objects.equals(salesidFromtcb, that.salesidFromtcb) && Objects.equals(agencycodeFromtcb,
      that.agencycodeFromtcb) && Objects.equals(referee, that.referee) && Objects.equals(assetholdid, that.assetholdid) && Objects.equals(updateddate, that.updateddate) && Objects.equals(
      unitpriceclean, that.unitpriceclean) && Objects.equals(tradingSource, that.tradingSource) && Objects.equals(yieldtomaturity, that.yieldtomaturity) && Objects.equals(ytmcouponreinvested,
      that.ytmcouponreinvested) && Objects.equals(accountNo, that.accountNo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tradingcode, action, tradingdate, bondproductid, price, customerid, quantity, userid, operationstatus, accountantstatus, agencystatus, supervisorstatus, createddate,
      rateid, active, accruedinterest, agencyid, commissionfee, identityid, identitycard, quality, customerstatus, listedbond, prorataquantity, prorataprice, flexprice, unitprice2, principal, accdate,
      buildbookdate, buildbookprice, status, currentqueue, disbursementmethod, forcesellstatus, loanreleasedate, loanvalue, opqueuestatus, processorid, queuestatus, referenceassetsid, waitingstatus,
      confirmtype, transactionfee, incometax, feesupport, taxsupport, matchingdate, transferdate, exception, counterparty, reason, frozen, depositorystatus, marketstatus, bundlestatus,
      collateralstatus, orderstatus, cpstatus, referencenumber, convertstatus, buildbook, buildbookstatus, referencestatus, salesidFromtcb, agencycodeFromtcb, referee, assetholdid, updateddate,
      unitpriceclean, tradingSource, yieldtomaturity, ytmcouponreinvested, accountNo);
  }
}
