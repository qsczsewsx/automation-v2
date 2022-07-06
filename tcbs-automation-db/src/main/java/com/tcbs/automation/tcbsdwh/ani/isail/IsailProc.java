package com.tcbs.automation.tcbsdwh.ani.isail;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

public class IsailProc {
  public static final HibernateEdition redShiftDb = Database.TCBS_DWH.getConnection();

  @Step("Get data from sql")
  public static List<HashMap<String, Object>> getUserLoginFromSql(String fromDate) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append("with lastvisit as ( ");
    queryBuilder.append("select tcbsid,last_visit_date	");
    queryBuilder.append("from dwh.prc_account_matomo ");
    queryBuilder.append("where last_visit_date >= :fromdate ");
    queryBuilder.append("	and last_visit_date >= cast(dateadd(month,-1,getdate()-1) as date) ");
    queryBuilder.append(") ");
    queryBuilder.append("select lv.tcbsid, auv.custodycd as ccd ");
    queryBuilder.append("from lastvisit lv ");
    queryBuilder.append("	left join dwh.smy_dwh_cas_alluserview auv on lv.tcbsid = auv.tcbsid ");
    queryBuilder.append("	order by lv.tcbsid ");
    try {
      Session session = redShiftDb.getSession();
      session.clear();
      beginTransaction(session);
      List<HashMap<String, Object>> resultList = session.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("fromdate", fromDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      redShiftDb.closeSession();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
