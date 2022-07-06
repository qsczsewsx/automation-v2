package com.tcbs.automation.fundstation.entity;


import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

public class TcAssets {
  public static HibernateEdition fundStationConnection = Database.FUND_STATION.getConnection();
  private static Session session;

  public static Session sendSessionDBAssets() {
    if (session != null && session.getTransaction().isActive()) {
      session.clear();
      return session;
    }
    session = fundStationConnection.getSession();
    session.beginTransaction();

    Account.session = session;
    Portfolio.session = session;
    PortfolioType.session = session;
    AssetBankAccount.session = session;
    Broker.session = session;
    CounterParty.session = session;
    Company.session = session;
    CompanyGroup.session = session;
    Conf.session = session;
    Coupon.session = session;
    ExChange.session = session;
    ExchangeAttr.session = session;
    ExchangeTax.session = session;
    FundWorkingDay.session = session;
    Industry.session = session;
    Instrument.session = session;
    InvestPortfolio.session = session;
    Policy.session = session;
    Product.session = session;
    ProductAttr.session = session;
    ProductDetail.session = session;
    ProductGlobal.session = session;
    ProductInstrument.session = session;
    Transaction.session = session;
    TransactionDemo.session = session;
    TransactionAttr.session = session;
    TransactionFund.session = session;
    Underlying.session = session;
    UnderlyingType.session = session;
    UnderlyingTypeTransactionAction.session = session;
    UserAccount.session = session;
    UserAccountAuthorization.session = session;
    MktPrice.session = session;
    ReportBrokerFund.session = session;
    ReportPortfolioFund.session = session;
    ReportTransFund.session = session;
    RequestTransaction.session = session;
    NetInFlow.session = session;
    CashFund.session = session;
    TSPlan.session = session;
    TSPlanDetail.session = session;
    TSPlanTransaction.session = session;
    TSPortfolio.session = session;
    TSPortfolioHistory.session = session;
    TSTransaction.session = session;
    TSChannel.session = session;
    UnderlyingDetail.session = session;
    Fee.session = session;
    ConfFee.session = session;
    TsBondTempHistory.session = session;
    ArPortfolioDetail.session = session;
    ArConf.session = session;
    ArVN30Ticker.session = session;

    return session;
  }

  public static void sqlCommit() {
    session.getTransaction().commit();
  }

  public static Query createNativeQuery(String sql) {
    return session.createNativeQuery(sql).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
  }

  public static Query createQuery(String sql) {
    return session.createQuery(sql);
  }
}
