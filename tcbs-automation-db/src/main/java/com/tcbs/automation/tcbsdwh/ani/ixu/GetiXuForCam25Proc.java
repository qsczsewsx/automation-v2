package com.tcbs.automation.tcbsdwh.ani.ixu;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

public class GetiXuForCam25Proc {
  public static final HibernateEdition redShiftDb = Database.TCBS_DWH.getConnection();

  @Step("Get data from sql")
  public static List<HashMap<String, Object>> getiXuForCam25FromSql(String businessdate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select busdate, CustodyCD, TCBSID, membership_type_id, (Total_Amount + feetax_amt) as TotalAmt, point,etlcurdate  ");
    queryBuilder.append("from dwh.Smy_dwh_flx_ixu_campaign25 ");
    queryBuilder.append("where BusDate = cast(:businessdate as date) order by tcbsid ");
    try {
      Session session = redShiftDb.getSession();
      session.clear();
      beginTransaction(session);
      List<HashMap<String, Object>> resultList = session.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("businessdate", businessdate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      redShiftDb.closeSession();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static void updateEtlDate() {
    Session session = Tcbsdwh.tcbsStagingDwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    String querySql = new StringBuilder()
      .append("update dwh.Smy_dwh_flx_ixu_campaign25 set etlcurdate = cast( to_char(cast(getdate() - 1 as date), 'yyyyMMdd') as integer) where etlcurdate in ( ")
      .append(" select max(etlcurdate) from dwh.Smy_dwh_flx_ixu_campaign25) ")
      .toString();
    try {
      Query<?> query = session.createNativeQuery(querySql);
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    Tcbsdwh.tcbsStagingDwhDbConnection.closeSession();
  }

}
