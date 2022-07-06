package com.tcbs.automation.iris.bonusshare;

import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BonusShareEntity {
  @Step("Call to get actual data")
  public static boolean callActualData(String date) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("  call api.proc_ris_bonus_shares(" + date + ") ");

    Session session = Tcbsdwh.tcbsDwhDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createNativeQuery(queryBuilder.toString());
    try {
      query.executeUpdate();
//      session.getTransaction().commit();
      Tcbsdwh.tcbsDwhDbConnection.closeSession();
    } catch (Exception e) {
      Tcbsdwh.tcbsDwhDbConnection.closeSession();
      throw e;
    }
    return true;
  }

  @Step("Get actual data")
  public static List<HashMap<String, Object>> getActualData(String date) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from api.ris_el_bonus_shares where approved = 1 and etlcurdate = " + date);
    try {
      return Tcbsdwh.tcbsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Get expected wca data")
  public static List<HashMap<String, Object>> getExpectedWcaData() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select afacctno, symbol, catype, caass, caamt, qtty, pqtty, duedate\n" +
      "from staging.stg_flx_v_getsecmarginratio_wca where etlcurdate = " +
      "(select max(etlcurdate) from staging.stg_flx_v_getsecmarginratio_wca) ");
    try {
      return Tcbsdwh.tcbsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Get expected stock event data")
  public static List<HashMap<String, Object>> getExpectedEventData() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select symbol, catype, exdate from (\n" +
      "select ticker as symbol, case event_code when 'DIV010' then '010' when 'DIV011' then '011' " +
      "when 'RIGHTS' then '014' else '021' end as catype,\n" +
      "exright_date as exdate, ROW_NUMBER() OVER(PARTITION BY symbol, event_code ORDER BY exright_date desc) as rn \n" +
      "from api.ical_evm_ext_stock where event_code in ('DIV010', 'DIV011', 'RIGHTS', 'BONUS')) where rn = 1 ");
    try {
      return Tcbsdwh.tcbsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get holiday data")
  public static List<HashMap<String, Object>> getHolidayData() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT DISTINCT cast(trunc(listdistince.timestamp) as varchar) AS datex \n" +
      "FROM staging.stg_tci_inv_holiday listdistince \n" +
      "where listdistince.timestamp >= dateadd(year, -1, date_trunc('year', getdate())) \n" +
      "and listdistince.timestamp <= dateadd(year, 2, date_trunc('year', getdate())) \n" +
      "order by listdistince.timestamp desc");
    try {
      return Tcbsdwh.tcbsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
