package com.tcbs.automation.tcinvest.ifund.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.List;

import static com.tcbs.automation.tcinvest.TcInvest.tcInvestDbConnection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("unchecked")
@Table(name = "INV_FUND")
public class InvFund {
  public static Session session;
  @Id
  @Column(name = "FUND_ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int fundId;
  @Column(name = "NAME")
  private String name;
  @Column(name = "CODE")
  private String code;
  @Column(name = "COMPANY_MANAGE")
  private String copayManage;
  @Column(name = "SUPERVISING_BANK")
  private String supervisingBank;
  @Column(name = "MANAGER")
  private String manager;
  @Column(name = "CREATED_TIMESTAMP")
  private String createdTimestamp;
  @Column(name = "UPDATED_TIMESTAMP")
  private String updatedTimestamp;
  @Column(name = "CONSTITUENT_ID")
  private String constituentId;
  @Column(name = "VOLUME_CURRENT")
  private Double volumeCurrent;
  @Column(name = "NAV_CURRENT")
  private Double navCurrent;
  @Column(name = "NAV_TOTAL")
  private Double navTotal;
  @Column(name = "TRADING_FREQUENCY")
  private String tradingFrequency;
  @Column(name = "TRADING_CLOSING_TIME")
  private String tradingClosingTime;
  @Column(name = "ALLOCATION_DATE")
  private String allocationDate;
  @Column(name = "PAYMENT_DATE")
  private int paymentDate;
  @Column(name = "SWITCHING_ALLOWABLE")
  private String switchingAllowable;
  @Column(name = "TRANSFERRING_ALLOWABLE")
  private String transferringAllowable;
  @Column(name = "MIN_SELL")
  private Double minSell;
  @Column(name = "MIN_BUY")
  private Double minBuy;
  @Column(name = "SUPERVISOR_BANK")
  private String supervisorBank;
  @Column(name = "[DESC]")
  private String desc;
  @Column(name = "SWITCHING_FEE")
  private Double switchingFee;
  @Column(name = "TRANSFERRING_FEE")
  private Double transferringFee;
  @Column(name = "URL_INFO")
  private String urlInfo;
  @Column(name = "EXPECTED_PROFIT")
  private String expectedProfit;
  @Column(name = "DISPLAY_NAME")
  private String displayName;
  @Column(name = "ORDER_BY")
  private String orderBy;
  @Column(name = "MONEY_AVAILABLE_TIME")
  private String moneyAvailableTime;
  @Column(name = "SHORT_NAME")
  private String shortName;
  @Column(name = "PERIOD_ORDER_ALLOWABLE")
  private String periodOrderAllowable;

  public List<InvFund> getAllFundInfo() {
    if (session == null) {
      session = tcInvestDbConnection.getSession();
    }
    Query<InvFund> query = session.createQuery("from InvFund");
    List<InvFund> result = query.getResultList();
    return result;
  }

  public InvFund getFundInfoByProductCode(String code) {
    if (session == null) {
      session = tcInvestDbConnection.getSession();
    }
    Query<InvFund> query = session.createQuery("from InvFund where code=:code");
    query.setParameter("code", code);
    List<InvFund> result = query.getResultList();
    if (result.size() > 0) {
      return result.get(0);
    } else {
      return null;
    }
  }
}
