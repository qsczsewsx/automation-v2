package com.tcbs.automation.hfcdata.ixu;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetIxuForCampEntity {
  public static final HibernateEdition redShiftDb = Database.TCBS_DWH.getConnection();
  @Step("get max etldate")
  public static List<HashMap<String, Object>> getMaxEtlDate(){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select max(etlcurdate) from dwh.Smy_dwh_flx_ixu_campaign25 ");
    try {
      return redShiftDb.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step(" Get data ")
  public static List<Object[]> getListBondProExpired(String busDate, Integer page, Integer size) {
    String queryBuilder = new StringBuilder()
      .append(" call api.ixu_getixuforcam25 ( '")
      .append(busDate).append("' , '")
      .append(page).append("', '")
      .append(size)
      .append("' , 'mycursor') ").toString();

    redShiftDb.closeSession();
    redShiftDb.openSession();

    Session session = redShiftDb.getSession();
    session.clear();
    try {
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      session.createNativeQuery(queryBuilder).executeUpdate();
      String query = " FETCH ALL FROM mycursor ";
      List<Object[]> res = session.createNativeQuery(query).list();
      return res;
    } catch (Exception ex) {
      throw ex;
    } finally {
      redShiftDb.closeSession();
    }
  }


}
