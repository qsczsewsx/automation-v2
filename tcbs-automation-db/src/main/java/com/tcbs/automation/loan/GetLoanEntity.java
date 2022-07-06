package com.tcbs.automation.loan;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import com.tcbs.automation.tcbsdwh.ani.CommonProcData;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

public class GetLoanEntity {

  public static final String MYCURSOR = ", 'mycursor')  ";
  public static final HibernateEdition redShiftDb = Database.TCBS_DWH.getConnection();

  public static String getQuery(Integer page, Integer size, int type, String from, String to) {
    String proc = null;
    switch (type) {
      case 0:
        proc = " call api.proc_liquidity_getloanlimit (" + page + "," + size + MYCURSOR;
        break;
      case 1:
        proc = " call api.proc_liquidity_getloanpayment (" + page + "," + size + MYCURSOR;
        break;
      case 2:
        proc = " call api.proc_liquidity_getloandetail (" + page + "," + size + MYCURSOR;
        break;
      case 3:
        proc = " call api.proc_liquidity_getisave ('" + from + "'," + "'" + to + "'," + page + "," + size + MYCURSOR;
        break;
      case 4:
        proc = " call api.proc_liquidity_getbondpro ('" + from + "'," + page + "," + size + MYCURSOR;
        break;
      case 5:
        proc = " call api.proc_liquidity_getwarehouse ('" + from + "'," + page + "," + size + MYCURSOR;
        break;
      case 6:
        proc = " call api.proc_liquidity_gettcsbond_outstanding ('" + from + "'," + "'" + to + "'," + page + "," + size + MYCURSOR;
        break;
      case 7:
        proc = " call api.proc_liquidity_gettcsbond_trading ('" + from + "'," + "'" + to + "'," + page + "," + size + MYCURSOR;
        break;
      case 8:
        proc = " call api.proc_liquidity_gettcsbond_couponpayment ('" + from + "'," + "'" + to + "'," + page + "," + size + MYCURSOR;
        break;
      default:
        break;
    }
    return proc;
  }

  public static List<Object[]> getListLoan(Integer page, Integer size, int type, String from, String to) {
    String queryBuilder = getQuery(page, size, type, from, to);
    CommonProcData.redShiftDb.closeSession();
    CommonProcData.redShiftDb.openSession();
    Session ses = CommonProcData.redShiftDb.getSession();
    try {
      if (!ses.getTransaction().isActive()) {
        ses.beginTransaction();
      }
      ses.createNativeQuery(queryBuilder + ";").executeUpdate();
      String query = " FETCH ALL FROM mycursor ; ";
      List res = ses.createNativeQuery(query).getResultList();
      return res;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    } finally {
      CommonProcData.redShiftDb.closeSession();
    }
    return new ArrayList<>();
  }
}
