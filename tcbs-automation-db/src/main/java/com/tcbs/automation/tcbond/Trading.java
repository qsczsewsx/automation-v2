package com.tcbs.automation.tcbond;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Trading")
public class Trading extends TcBond {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  public Integer id;
  @Column(name = "TradingCode")
  public String tradingCode;
  @Column(name = "Action")
  public Integer action;
  @Column(name = "TradingDate")
  @Temporal(TemporalType.DATE)
  public Date tradingDate;
  @Column(name = "BondProductId")
  public Integer bondProductId;
  @Column(name = "Price")
  public BigDecimal price;
  @Column(name = "CustomerId")
  public Integer customerId;
  @Column(name = "Quantity")
  public Double quantity;
  @Column(name = "UserId")
  public Integer userId;
  @Column(name = "OperationStatus")
  public Integer operationStatus;
  @Column(name = "AccountantStatus")
  public Integer accountantStatus;
  @Column(name = "AgencyStatus")
  public Integer agencyStatus;
  @Column(name = "SuperVisorStatus")
  public Integer superVisorStatus;
  @Column(name = "CreatedDate")
  @Temporal(TemporalType.DATE)
  public Date createdDate;
  @Column(name = "RateId")
  public Integer rateId;
  @Column(name = "Active")
  public Integer active;
  @Column(name = "AccruedInterest")
  public Double accruedInterest;
  @Column(name = "AgencyId")
  public Integer agencyId;
  @Column(name = "CommissionFee")
  public Double commissionFee;
  @Column(name = "IdentityID")
  public String identityId;
  @Column(name = "IdentityCard")
  public String identityCard;
  @Column(name = "Quality")
  public Long quality;
  @Column(name = "CustomerStatus")
  public Integer customerStatus;
  @Column(name = "ListedBond")
  public Integer listedBond;
  @Column(name = "ProrataQuantity")
  public Double prorataQuantity;
  @Column(name = "ProrataPrice")
  public Double prorataPrice;
  @Column(name = "FlexPrice")
  public Double flexPrice;
  @Column(name = "UnitPrice2")
  public BigDecimal unitPrice2;
  @Column(name = "Principal")
  public BigDecimal principal;
  @Column(name = "AccDate")
  @Temporal(TemporalType.DATE)
  public Date accDate;
  @Column(name = "BuildbookDate")
  @Temporal(TemporalType.DATE)
  public Date buildbookDate;
  @Column(name = "BuildbookPrice")
  public Double buildbookPrice;
  @Column(name = "Status")
  public Integer status;
  @Column(name = "CurrentQueue")
  public Integer currentQueue;
  @Column(name = "DisbursementMethod")
  public Integer disbursementMethod;
  @Column(name = "ForceSellStatus")
  public Integer forceSellStatus;
  @Column(name = "LoanReleaseDate")
  @Temporal(TemporalType.DATE)
  public Date loanReleaseDate;
  @Column(name = "LoanValue")
  public Double loanValue;
  @Column(name = "OpQueueStatus")
  public Integer opQueueStatus;
  @Column(name = "ProcessorID")
  public Integer processorId;
  @Column(name = "QueueStatus")
  public Integer queueStatus;
  @Column(name = "ReferenceAssetsId")
  public Integer referenceAssetsId;
  @Column(name = "WaitingStatus")
  public Integer waitingStatus;
  @Column(name = "ConfirmType")
  public Integer confirmType;
  @Column(name = "TransactionFee")
  public Double transactionFee;
  @Column(name = "IncomeTax")
  public Double incomeTax;
  @Column(name = "FeeSupport")
  public Double feeSupport;
  @Column(name = "TaxSupport")
  public Double taxSupport;
  @Column(name = "MatchingDate")
  @Temporal(TemporalType.DATE)
  public Date matchingDate;
  @Column(name = "TransferDate")
  @Temporal(TemporalType.DATE)
  public Date transferDate;
  @Column(name = "Exception")
  public Integer exception;
  @Column(name = "CounterParty")
  public Integer counterParty;
  @Column(name = "Reason")
  public String reason;
  @Column(name = "Frozen")
  public Integer frozen;
  @Column(name = "DepositoryStatus")
  public Integer depositoryStatus;
  @Column(name = "MarketStatus")
  public Integer marketStatus;
  @Column(name = "BundleStatus")
  public String bundleStatus;
  @Column(name = "CollateralStatus")
  public Integer collateralStatus;
  @Column(name = "OrderStatus")
  public Integer orderStatus;
  @Column(name = "CpStatus")
  public Integer cpStatus;
  @Column(name = "ReferenceNumber")
  public String referenceNumber;
  @Column(name = "ConvertStatus")
  public Integer convertStatus;
  @Column(name = "Buildbook")
  public Integer buildbook;
  @Column(name = "BuildbookStatus")
  public Integer buildbookStatus;
  @Column(name = "ReferenceStatus")
  public Integer referenceStatus;
  @Column(name = "SalesID_FromTCB")
  public String salesIdFromTcb;
  @Column(name = "AgencyCode_FromTCB")
  public String agencyCodeFromTcb;
  @Column(name = "Referee")
  public String referee;

  public Trading setId(Integer id) {
    this.id = id;
    return this;
  }

  public Trading setTradingDate(Date tradingDate) {
    this.tradingDate = tradingDate;
    return this;
  }

  public Trading setMatchingDate(Date matchingDate) {
    this.matchingDate = matchingDate;
    return this;
  }

  public Trading setTransferDate(Date transferDate) {
    this.transferDate = transferDate;
    return this;
  }

  public Trading setOrderStatus(Integer orderStatus) {
    this.orderStatus = orderStatus;
    return this;
  }

  public Trading setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
    return this;
  }


  @Step
  public void updateStatusForTrading(Integer operationStatus, Integer accountantStatus, Integer agencyStatus,
                                     Integer supervisorStatus, String tradingCode, Integer action) {
    Query<Bond> query = tcBondDbConnection.getSession().createQuery(
      "update Trading set OperationStatus = :OperationStatus, AccountantStatus = :AccountantStatus, AgencyStatus = :AgencyStatus, SuperVisorStatus = :SuperVisorStatus where TradingCode = :TradingCode and Action = :Action");
    query.setParameter("OperationStatus", operationStatus);
    query.setParameter("AccountantStatus", accountantStatus);
    query.setParameter("AgencyStatus", agencyStatus);
    query.setParameter("SuperVisorStatus", supervisorStatus);
    query.setParameter("TradingCode", tradingCode);
    query.setParameter("Action", action);
  }

  @Step
  public void clearTradingByBondId(Integer id) {
    tcBondDbConnection.getSession().createQuery(
      "delete from Trading where id = " + id);
  }

  @Step
  public Trading getListTrading(String tradingCode) {
    Query<Trading> query = tcBondDbConnection.getSession().createQuery(
      "from Trading where ID = " + tradingCode);
    List<Trading> result = query.getResultList();

    if (result.size() > 0) {
      return result.get(0);
    } else {
      return null;
    }
  }

  @Step
  public Trading updateReferenceStatusValueById(Integer referenceStatus, Integer id) {
    Session session = tcBondDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<Trading> query = session.createQuery(
      "update Trading i\n" +
        "    SET i.referenceStatus=:referenceStatus\n" +
        "    where i.id=:id"
    );

    query.setParameter("referenceStatus", referenceStatus);
    query.setParameter("id", id);
    query.executeUpdate();
    session.getTransaction().commit();
    return this;
  }

  @Step
  public Trading updateTrading() {
    Session session = tcBondDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<Trading> query = tcBondDbConnection.getSession().createQuery(
      "update Trading set referenceStatus = :referenceStatus" +
        ", accountantStatus = :accountantStatus" +
        ", agencyStatus = :agencyStatus" +
        ", waitingStatus =:waitingStatus" +
        ", customerStatus =:customerStatus" +
        ", orderStatus =:orderStatus " +
        "where id = :id ");
    query.setParameter("accountantStatus", this.accountantStatus);
    query.setParameter("agencyStatus", this.agencyStatus);
    query.setParameter("customerStatus", this.customerStatus);
    query.setParameter("waitingStatus", this.waitingStatus);
    query.setParameter("orderStatus", this.orderStatus);
    query.setParameter("referenceStatus", this.referenceStatus);
    query.setParameter("id", this.id);
    query.executeUpdate();
    session.getTransaction().commit();
    return this;
  }

  @Step("Get Buy Trading of OrderMatched {0}")
  public Trading getBuyTradingByProcessorId(Integer processorId) {
    Query<Trading> query = tcBondDbConnection.getSession().createQuery(
      "from Trading where processorId =: processorId and action = 5");
    List<Trading> result = query
      .setParameter("processorId", processorId)
      .getResultList();
    if (result.size() == 1) {
      return result.get(0);
    } else {
      return null;
    }
  }

  @Step("Get Buy Trading of OrderMatched {0}")
  public Trading getSellTradingByProcessorId(Integer processorId) {
    Query<Trading> query = tcBondDbConnection.getSession().createQuery(
      "from Trading where processorId =: processorId and action = 7");
    List<Trading> result = query
      .setParameter("processorId", processorId)
      .getResultList();
    if (result.size() == 1) {
      return result.get(0);
    } else {
      return null;
    }
  }

  public void insertBondOrderForReferrals(int dataReferralId) {
    Session session = tcBondDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = tcBondDbConnection.getSession().createSQLQuery(
      "INSERT INTO TRADING (ID, TRADINGCODE, ACTION, TRADINGDATE, BONDPRODUCTID, PRICE, CUSTOMERID, QUANTITY, USERID, OPERATIONSTATUS, ACCOUNTANTSTATUS, AGENCYSTATUS, SUPERVISORSTATUS, CREATEDDATE, RATEID, ACTIVE, ACCRUEDINTEREST, AGENCYID, COMMISSIONFEE, IDENTITYID, IDENTITYCARD, QUALITY, CUSTOMERSTATUS, LISTEDBOND, PRORATAQUANTITY, PRORATAPRICE, FLEXPRICE, UNITPRICE2, PRINCIPAL, ACCDATE, BUILDBOOKDATE, BUILDBOOKPRICE, STATUS, CURRENTQUEUE, DISBURSEMENTMETHOD, FORCESELLSTATUS, LOANRELEASEDATE, LOANVALUE, OPQUEUESTATUS, PROCESSORID, QUEUESTATUS, REFERENCEASSETSID, WAITINGSTATUS, CONFIRMTYPE, TRANSACTIONFEE, INCOMETAX, FEESUPPORT, TAXSUPPORT, MATCHINGDATE, TRANSFERDATE, EXCEPTION, COUNTERPARTY, REASON, FROZEN, DEPOSITORYSTATUS, MARKETSTATUS, BUNDLESTATUS, COLLATERALSTATUS, ORDERSTATUS, CPSTATUS, REFERENCENUMBER, CONVERTSTATUS, BUILDBOOK, BUILDBOOKSTATUS, REFERENCESTATUS, SALESID_FROMTCB, AGENCYCODE_FROMTCB, REFEREE, ASSETHOLDID, UPDATEDDATE, UNITPRICECLEAN, TRADING_SOURCE, YIELDTOMATURITY, YTMCOUPONREINVESTED, ACCOUNT_NO) VALUES (:id, 'IWDC.B0000066275-iBondPrix-VPL72020', 5, TO_TIMESTAMP('2019-03-20 01:00:01.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 116, 695310750, 72157, 6750, null, 1, 0, 0, 0, TO_TIMESTAMP('2019-03-19 15:41:58.787000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 0, 1, null, 2525, null, null, null, null, 0, 0, null, null, null, 103009, 696353716, null, TO_TIMESTAMP('2019-03-20 00:00:00.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), 696353716, 1, 0, null, null, null, null, null, null, null, null, 4, null, 1042966.125, null, null, null, TO_TIMESTAMP('2019-03-20 01:00:01.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), TO_TIMESTAMP('2019-03-20 01:00:01.000000', 'YYYY-MM-DD HH24:MI:SS.FF6'), null, 3926, null, null, null, null, 'Normal', null, 100, null, null, null, null, null, 0, null, null, null, null, null, 103009.000000, 1, null, null, '0001897999')");
    query.setParameter("id", dataReferralId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public void deleteBondOrderForReferrals(int dataReferralId) {
    Session session = tcBondDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = tcBondDbConnection.getSession().createSQLQuery(
      "DELETE FROM TRADING WHERE id=:id");
    query.setParameter("id", dataReferralId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public void dmlBondOrderForReferralsWithQueryString(String queryString) {
    Session session = tcBondDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = tcBondDbConnection.getSession().createSQLQuery(queryString);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public void insert() {
    Session session = TcBond.tcBondDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    session.save(this);
    session.getTransaction().commit();
  }
}
