package com.tcbs.automation.hfcdata.bond;

import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.HashMap;
import java.util.List;

public class GetBondCouponEventEntity {

  @Step("Get data")
  public static List<HashMap<String, Object>> getFutureEvent() {
    StringBuilder query = new StringBuilder();
    query.append(" select bondCode, baseInterestDays, to_char(couponExDate, 'yyyy-mm-dd') as couponExDate, to_char(couponPaymentDate, 'yyyy-mm-dd') as couponPaymentDate, ");
    query.append(" couponRate, couponPerUnit_beforeTax, Tax, couponPerUnit ");
    query.append(" from dwh.smy_dwh_tcb_coupon_payment ");
    query.append(" where couponExDate > cast(getdate() as date) ");
    query.append(" order by bondcode, couponExDate ");
    Session session = Tcbsdwh.tcbsDwhDbConnection.getSession();
    session.clear();
    try {
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      return Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } finally {
      Tcbsdwh.tcbsDwhDbConnection.closeSession();
    }
  }


  @Step("Get data")
  public static List<HashMap<String, Object>> getCouponReceived(String fromDate, String toDate) {
    StringBuilder query = new StringBuilder();
    query.append(" select TradingCode, BondCode, OBal_Quantity, to_char(cast(cfidate as date), 'yyyy-mm-dd') as couponpaymentdate, CFI as couponvalue, CustomerId, CustodyCode ");
    query.append(" from dwh.smy_dwh_tcb_bondcontractcfi ");
    query.append(" where \"Tag\" = 'Coupon Payment' ");
    query.append(" and TradingCode in (select TradingCode from dwh.Smy_dwh_tcb_BondOutstanding where OBalQ > 0) ");
    query.append(" and TradingCode in ( ");
    query.append("   select TradingCode ");
    query.append("   from dwh.Smy_dwh_tcb_Bond_Trading_Details ");
    query.append("   where :fromDate <= TradingDate AND TradingDate < :toDate ");
    query.append(" ) ");
    query.append(" and CFIDate < CAST(getdate() as date) ");
    query.append(" order by TradingCode, cfidate ");

    Session session = Tcbsdwh.tcbsDwhDbConnection.getSession();
    session.clear();
    try {
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      return Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } finally {
      Tcbsdwh.tcbsDwhDbConnection.closeSession();
    }
  }
}
