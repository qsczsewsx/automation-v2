package com.tcbs.automation.hfcdata.icalendar;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.hfcdata.HfcData.hfcDataDbConnection;

public class IcalendarStockEntity {
  public static final HibernateEdition hfc = Database.HFC_DATA.getConnection();

  @Step("Get data")
  public static List<HashMap<String, Object>> getEventStockWatchListByCustody(String custodyId, String fromDate,
                                                                              String toDate, List<String> productType) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select DEFID, eventtype," +
      "TO_CHAR(EVENTDATE , 'YYYY-MM-DD') AS EVENTDATE,eventstatus," +
      "OBJID,STOCK_RATIO," +
      "Case when (stock_code is null and eventtype = 'icalendar.wl.stock.additionalListedDate') then '801' " +
      "when (stock_code is null and eventtype = 'icalendar.wl.stock.FirstlListedDate') then '800' else stock_code end as STOCK_CODE " +
      "FROM ICALENDAR_WL_ALLDATA" +
      " WHERE CUSTODYCD=:p_custodyID AND PRODUCTTYPE in :p_productType" +
      " AND EVENTDATE BETWEEN TO_DATE(:p_startDate, 'YYYY-MM-DD') AND TO_DATE(:p_endDate, 'YYYY-MM-DD')" +
      " AND ETLCURDATE = (SELECT MAX(ETLCURDATE) FROM ICALENDAR_WL_ALLDATA) ORDER BY EVENTDATE DESC");

    try {
      List<HashMap<String, Object>> resultList = hfc.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("p_custodyID", custodyId)
        .setParameter("p_productType", productType)
        .setParameter("p_startDate", fromDate)
        .setParameter("p_endDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
