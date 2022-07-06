package com.tcbs.automation.hfcdata.icalendar.entity;

import com.tcbs.automation.hfcdata.HfcData;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class JobDataCollectionEntity {

  @Step("Get all data watch list")
  public static List<HashMap<String, Object>> getAllDataActual() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT * FROM api.ical_evm_fil_watchlist_info");
    try {
      List<HashMap<String, Object>> resultList = HfcData.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get all data watch list from original source")
  public static List<HashMap<String, Object>> getAllDataFromOriginalTable() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT tcbsid, max(etlcurdate) as etlcurdate, ");
    queryBuilder.append(" listagg(distinct list_stocks_code, ', ') within group (order by list_stocks_code) ");
    queryBuilder.append(" FROM staging.stg_poseidon_category ");
    queryBuilder.append(" WHERE etlcurdate = (SELECT max(etlcurdate) FROM staging.stg_poseidon_category) ");
    queryBuilder.append("       AND category_type = 'WL' ");
    queryBuilder.append("       AND list_stocks_code IS NOT null ");
    queryBuilder.append("       AND tcbsid IS NOT null ");
    queryBuilder.append(" GROUP BY tcbsid ");

    try {
      List<HashMap<String, Object>> resultList = HfcData.redshiftStaging.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
