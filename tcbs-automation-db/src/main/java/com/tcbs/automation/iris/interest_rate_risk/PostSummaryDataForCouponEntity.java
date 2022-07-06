package com.tcbs.automation.iris.interest_rate_risk;

import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PostSummaryDataForCouponEntity {
  @Step("Summary data")
  public static List<HashMap<String, Object>> getDataCouponPayment() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT bs.code, bs.issue_date, bs.start_trading_date, bs.expired_date, ct.define_date, ct.real_rate, ct.expected_rate,  ");
    queryBuilder.append(" ct.start_date, ct.end_date, ct.rate_type, ct.margin, pt.payment_date, ctp.coupon_freq FROM staging.stg_tci_inv_bond_static bs ");
    queryBuilder.append(" LEFT JOIN staging.stg_coman_coupon_template ctp ON bs.code = ctp.bond_code AND ctp.etlcurdate = ");
    queryBuilder.append(" (( SELECT \"max\"(stg_coman_coupon_template.etlcurdate) AS \"max\"  FROM staging.stg_coman_coupon_template)) ");
    queryBuilder.append("  JOIN staging.stg_coman_coupon_timeline ct ON bs.code = ct.bond_code AND ct.etlcurdate = (( SELECT \"max\"(stg_coman_coupon_timeline.etlcurdate) AS \"max\" ");
    queryBuilder.append(" FROM staging.stg_coman_coupon_timeline)) ");
    queryBuilder.append("  LEFT JOIN staging.stg_coman_payment_timeline pt ON bs.code = pt.bond_code AND ct.start_date >= pt.start_date AND ct.end_date <= pt.end_date ");
    queryBuilder.append("  WHERE bs.etlcurdate = (( SELECT \"max\"(stg_tci_inv_bond_static.etlcurdate) AS \"max\" ");
    queryBuilder.append("  FROM staging.stg_tci_inv_bond_static)) AND bs.active = 1 ORDER BY bs.code, ct.start_date ");
    try {
      return Tcbsdwh.tcbsDbMartDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data from bond coupon payment table")
  public static List<HashMap<String, Object>> getDataFromBondCouponPayment() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from dbmart_risk.iris_bond_coupon_payment ");
    try {
      return Tcbsdwh.tcbsDbMartDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static void executeQuery(String sql, String type) {
    Session session = Tcbsdwh.tcbsStagingDwhDbConnection.getSession();;
    session.clear();
    List <String> resultSql = new ArrayList();
    try {
      String[] sqlRows = sql.split("\n");
      if(type.equals("Delete")) {
        for(String item : sqlRows) {
          if(item.contains("DELETE")) {
            resultSql.add(item);
          }
        }
      }  else {
        resultSql = Arrays.asList(sql.split("\r\n"));
      }
      Transaction transaction = session.getTransaction();
      if(transaction != null && transaction.isActive() && transaction.getStatus().canRollback()){
        transaction.rollback();
      }
      transaction = session.beginTransaction();
      for (String s : resultSql) {
        session.createNativeQuery(s).executeUpdate();
      }
      transaction.commit();
    } catch (Exception e) {
      throw e;
    }finally {
      Tcbsdwh.tcbsStagingDwhDbConnection.closeSession();
    }
  }
}
