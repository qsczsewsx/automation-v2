package com.tcbs.automation.tcinvest;

import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Getter
@Entity
@Table(name = "INV_BOND_CATEGORY")
public class InvBondCategory {

  @Id
  @Column(name = "BOND_CATEGORY_ID")
  private String bondCategoryId;
  @Column(name = "NAME")
  private String name;
  @Column(name = "CODE")
  private String code;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "MAX_TIME_INVEST")
  private String maxTimeInvest;
  @Column(name = "MAX_INVEST_VALUE")
  private String maxInvestValue;
  @Column(name = "MIN_TIME_INVEST")
  private String minTimeInvest;
  @Column(name = "MIN_INVEST_VALUE")
  private String minInvestValue;
  @Column(name = "IS_COUPON")
  private String isCoupon;
  @Column(name = "COUPON_FREQ")
  private String couponFreq;
  @Column(name = "SELLBACK_METHOD")
  private String sellbackMethod;
  @Column(name = "BROKER_FEE")
  private String brokerFee;
  @Column(name = "BROKER_FEE_DATE")
  private Date brokerFeeDate;
  @Column(name = "FIX_BROKER_FEE")
  private String fixBrokerFee;
  @Column(name = "FIX_BROKER_DATE")
  private Date fixBrokerDate;
  @Column(name = "DESTROY_ORDER_SELL")
  private String destroyOrderSell;
  @Column(name = "DESTROY_ORDER_SELLBACK")
  private String destroyOrderSellback;
  @Column(name = "TOTAL_SELLBACK")
  private String totalSellback;
  @Column(name = "TOTAL_BUYBACK")
  private String totalBuyback;
  @Column(name = "IS_DEFAULT")
  private String isDefault;
  @Column(name = "ACTIVE")
  private String active;
  @Column(name = "APPLY_RATE_DATE")
  private Date applyRateDate;
  @Column(name = "EXPIRED_RATE_DATE")
  private Date expiredRateDate;
  @Column(name = "RECURRING_DATA")
  private String recurringData;
  @Column(name = "TYPE")
  private String type;
  @Column(name = "LIMIT_VALUE")
  private String limitValue;
  @Column(name = "TOTAL_LIMIT_VALUE")
  private String totalLimitValue;
  @Column(name = "CREATED_DATE")
  private Date createdDate;
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;

  public static void deleteFinancialTermByCode(String code) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvBondCategory> query = session.createQuery(
      "DELETE FROM InvBondCategory ivb WHERE ivb.code=:code"
    );
    query.setParameter("code", code);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static List<InvBondCategory> getBuyCode(String code) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    Query<InvBondCategory> query = session.createQuery("from InvBondCategory ivb where ivb.code =: code");
    query.setParameter("code", code);
    List<InvBondCategory> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }
}
