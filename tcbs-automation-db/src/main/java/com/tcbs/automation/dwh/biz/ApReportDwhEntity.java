package com.tcbs.automation.dwh.biz;

import lombok.Builder;
import lombok.Data;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Builder
@Entity
@Table(name = "AP_Report_DWH")
public class ApReportDwhEntity {
  private String id;
  private String tradingcode;
  private Timestamp tradingdate;
  private Timestamp matchingdate;
  private Timestamp transferdate;
  private Double quantity;
  private Integer price;
  private Integer principal;
  private Integer unitprice2;
  private Byte marketstatus;
  private String custodycode;
  private String description;
  private String cusRegion;
  private String destinationName;
  private String bankname;
  private String destinationAccount;
  private String bankcity;
  private String branchname;
  private Timestamp createddate;
  private Timestamp fromdate;
  private Timestamp todate;
  private String transactiontype;
  private String customertype;
  private String bankcode;
  private String banksys;
  private String sourceAccNumber;
  private String clientId;
  private String maker;

  public static List<ApReportDwhEntity> getDataFromAP() {
    Session session = DwhAP.dwhAPDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<ApReportDwhEntity> query = DwhAP.dwhAPDbConnection.getSession().createQuery(
      "from ApReportDwhEntity ", ApReportDwhEntity.class);

    try {
      return query.getResultList();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  public static List<ApReportDwhEntity> getDataFromAPWithTxnList(List<Integer> txnIds) {
    Session session = DwhAP.dwhAPDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<ApReportDwhEntity> query = DwhAP.dwhAPDbConnection
      .getSession().createQuery(
        "from ApReportDwhEntity where id in :txnIds", ApReportDwhEntity.class);
    query.setParameter("txnIds", txnIds);
    try {
      return query.getResultList();
    } catch (Exception e) {
      return new ArrayList<>();
    }
  }

  @Step("get list failed txn")
  public static List<HashMap<String, Object>> getListFailedTxn() {
    Session session = DwhAP.dwhAPDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append("with reconcile as ");
    queryBuilder.append("( ");
    queryBuilder.append("select ");
    queryBuilder.append("   ISNULL(dwh.id, tcbond.id) as id");
    queryBuilder.append("   , dwh.quantity as DWH_Quantity");
    queryBuilder.append("   , tcbond.Quantity as TCBOND_Quantity");
    queryBuilder.append("   , dwh.Principal as DWH_Principal");
    queryBuilder.append("   , TCBond.Principal as TCBOND_Principal");
    queryBuilder.append("   , dwh.id as dwh_id");
    queryBuilder.append("   , case when DATEPART(hour, GETDATE()) >= 15 or (DATEPART(hour, GETDATE()) < 15 and dwh.id is not null) then 'NOT_MATCHED' else 'MATCHED' end as Reconcile_Status ");
    queryBuilder.append("from (select * from AP_Report_dwh where Cus_Region = N'DOMESTIC') dwh ");
    queryBuilder.append("full outer join (select * from AP_Report_TCBond_Details where nationality = 'DOMESTIC') tcbond on Dwh.id = tcbond.id ");
    queryBuilder.append("where ISNULL(dwh.quantity,0) <> ISNULL(tcbond.quantity,0) ");
    queryBuilder.append("or ISNULL(dwh.principal,0) <> ISNULL(tcbond.principal,0)) ");
    queryBuilder.append("select * from reconcile where Reconcile_Status = 'NOT_MATCHED' ");

    try {
      return DwhAP.dwhAPDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Basic
  @Id
  @GeneratedValue
  @Column(name = "ID")
  public String getId() {
    return id;
  }

  public void setId(String id) {
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
  @Column(name = "TRADINGDATE")
  public Timestamp getTradingdate() {
    return tradingdate;
  }

  public void setTradingdate(Timestamp tradingdate) {
    this.tradingdate = tradingdate;
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
  @Column(name = "QUANTITY")
  public Double getQuantity() {
    return quantity;
  }

  public void setQuantity(Double quantity) {
    this.quantity = quantity;
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
  @Column(name = "PRINCIPAL")
  public Integer getPrincipal() {
    return principal;
  }

  public void setPrincipal(Integer principal) {
    this.principal = principal;
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
  @Column(name = "MARKETSTATUS")
  public Byte getMarketstatus() {
    return marketstatus;
  }

  public void setMarketstatus(Byte marketstatus) {
    this.marketstatus = marketstatus;
  }

  @Basic
  @Column(name = "CUSTODYCODE")
  public String getCustodycode() {
    return custodycode;
  }

  public void setCustodycode(String custodycode) {
    this.custodycode = custodycode;
  }

  @Basic
  @Column(name = "DESCRIPTION")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Basic
  @Column(name = "CUS_REGION")
  public String getCusRegion() {
    return cusRegion;
  }

  public void setCusRegion(String cusRegion) {
    this.cusRegion = cusRegion;
  }

  @Basic
  @Column(name = "DESTINATION_NAME")
  public String getDestinationName() {
    return destinationName;
  }

  public void setDestinationName(String destinationName) {
    this.destinationName = destinationName;
  }

  @Basic
  @Column(name = "BANKNAME")
  public String getBankname() {
    return bankname;
  }

  public void setBankname(String bankname) {
    this.bankname = bankname;
  }

  @Basic
  @Column(name = "DESTINATION_ACCOUNT")
  public String getDestinationAccount() {
    return destinationAccount;
  }

  public void setDestinationAccount(String destinationAccount) {
    this.destinationAccount = destinationAccount;
  }

  @Basic
  @Column(name = "BANKCITY")
  public String getBankcity() {
    return bankcity;
  }

  public void setBankcity(String bankcity) {
    this.bankcity = bankcity;
  }

  @Basic
  @Column(name = "BRANCHNAME")
  public String getBranchname() {
    return branchname;
  }

  public void setBranchname(String branchname) {
    this.branchname = branchname;
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
  @Column(name = "FROMDATE")
  public Timestamp getFromdate() {
    return fromdate;
  }

  public void setFromdate(Timestamp fromdate) {
    this.fromdate = fromdate;
  }

  @Basic
  @Column(name = "TODATE")
  public Timestamp getTodate() {
    return todate;
  }

  public void setTodate(Timestamp todate) {
    this.todate = todate;
  }

  @Basic
  @Column(name = "TRANSACTIONTYPE")
  public String getTransactiontype() {
    return transactiontype;
  }

  public void setTransactiontype(String transactiontype) {
    this.transactiontype = transactiontype;
  }

  @Basic
  @Column(name = "CUSTOMERTYPE")
  public String getCustomertype() {
    return customertype;
  }

  public void setCustomertype(String customertype) {
    this.customertype = customertype;
  }

  @Basic
  @Column(name = "BANKCODE")
  public String getBankcode() {
    return bankcode;
  }

  public void setBankcode(String bankcode) {
    this.bankcode = bankcode;
  }


  @Basic
  @Column(name = "BANKSYS")
  public String getBanksys() {
    return banksys;
  }

  public void setBanksys(String banksys) {
    this.banksys = banksys;
  }

  public String getSourceAccNumber() {
    return sourceAccNumber;
  }

  public void setSourceAccNumber(String sourceAccNumber) {
    this.sourceAccNumber = sourceAccNumber;
  }

  public String getClientId() {
    return clientId;
  }

  public void setClientId(String clientId) {
    this.clientId = clientId;
  }

  public String getMaker() {
    return maker;
  }

  public void setMaker(String maker) {
    this.maker = maker;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApReportDwhEntity that = (ApReportDwhEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(tradingcode, that.tradingcode) && Objects.equals(tradingdate, that.tradingdate) && Objects.equals(matchingdate,
      that.matchingdate) && Objects.equals(transferdate, that.transferdate) && Objects.equals(quantity, that.quantity) && Objects.equals(price, that.price) && Objects.equals(principal,
      that.principal) && Objects.equals(unitprice2, that.unitprice2) && Objects.equals(marketstatus, that.marketstatus) && Objects.equals(custodycode, that.custodycode) && Objects.equals(description,
      that.description) && Objects.equals(cusRegion, that.cusRegion) && Objects.equals(destinationName, that.destinationName) && Objects.equals(bankname, that.bankname) && Objects.equals(
      destinationAccount, that.destinationAccount) && Objects.equals(bankcity, that.bankcity) && Objects.equals(branchname, that.branchname) && Objects.equals(createddate,
      that.createddate) && Objects.equals(fromdate, that.fromdate) && Objects.equals(todate, that.todate) && Objects.equals(transactiontype, that.transactiontype) && Objects.equals(customertype,
      that.customertype) && Objects.equals(bankcode, that.bankcode) && Objects.equals(banksys, that.banksys);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, tradingcode, tradingdate, matchingdate, transferdate, quantity, price, principal, unitprice2, marketstatus, custodycode, description, cusRegion, destinationName, bankname,
      destinationAccount, bankcity, branchname, createddate, fromdate, todate, transactiontype, customertype, bankcode, banksys);
  }
}
