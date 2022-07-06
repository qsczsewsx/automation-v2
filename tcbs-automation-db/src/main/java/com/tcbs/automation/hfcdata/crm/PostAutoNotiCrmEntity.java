package com.tcbs.automation.hfcdata.crm;

import com.tcbs.automation.hfcdata.HfcData;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostAutoNotiCrmEntity {

  public static List<HashMap<String, Object>> getInfoFromDb(String notiDate){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT TO_CHAR(NOTI_DATA)  as NOTI_DATA , NOTI_DATE, STATUS, NOTE FROM CRM_SYNC_NOTI_TRANS WHERE to_char(noti_date,'YYYY-MM-DD') = '" + notiDate + "'");
    try {
      return HfcData.hfcDataDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static void deleteData(String notiDate){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("DELETE FROM CRM_SYNC_NOTI_TRANS WHERE to_char(noti_date,'YYYY-MM-DD') = '" + notiDate + "'");
    executeQuery(queryBuilder);
  }

  public static void executeQuery(StringBuilder sql) {
    Session session = HfcData.hfcDataDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createNativeQuery(sql.toString());
    try {
      query.executeUpdate();
      session.getTransaction().commit();
      HfcData.hfcDataDbConnection.closeSession();
    } catch (Exception e) {
      HfcData.hfcDataDbConnection.closeSession();
      throw e;
    }
  }

}
