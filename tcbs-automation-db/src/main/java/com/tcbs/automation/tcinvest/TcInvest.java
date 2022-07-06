package com.tcbs.automation.tcinvest;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import com.tcbs.automation.tcinvest.ifund.entity.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class TcInvest {
  public static HibernateEdition tcInvestDbConnection = Database.TCINVEST_ORCL.getConnection();
  private static Session session;

  public static Session sendSectionDBTcInvest() {

    if (session != null && session.getTransaction().isActive()) {
      session.clear();
      return session;
    }
    session = tcInvestDbConnection.getSession();
    session.beginTransaction();
    InvOrder.session = session;
    InvFundPlan.session = session;
    InvFundPlanDetail.session = session;
    InvOrderAttr.session = session;
    InvFundSchedule.session = session;
    InvFundScheduleDetail.session = session;
    InvGroupOrder.session = session;
    InvOrderMatched.session = session;
    InvOrderDataMatched.session = session;
    InvFundMarketData.session = session;
    return session;
  }


  public void updateBondConversiontrackingOrderIdToNull(String trackingId) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    Transaction trans = session.beginTransaction();

    Query<InvGlobalAttr> query = session.createSQLQuery(
      "UPDATE CONVERSION_BOND_TRACKING set orderId=null  where CustomerId in  (select customerId from CONVERSION_BOND_TRACKING where  id=:trackingId)"
    );
    query.setParameter("trackingId", trackingId);
    query.executeUpdate();
    trans.commit();
  }

}
