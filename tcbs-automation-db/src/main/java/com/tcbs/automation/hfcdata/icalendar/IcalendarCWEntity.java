package com.tcbs.automation.hfcdata.icalendar;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IcalendarCWEntity {
  public static final HibernateEdition hfc = Database.HFC_DATA.getConnection();

  @Step("Get data")
  public static List<HashMap<String, Object>> getEventStockListedDateByCustody(String custodyId, String fromDate, String toDate, List<String> productType) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select ica.DEFID,ica.DEFTYPE,TO_CHAR(ica.EVENTDATE , 'YYYY-MM-DD') AS EVENTDATE,ica.OBJID AS SYMBOL,\n" +
      "ica.EVENTSTATUS,ica.CUSTODYCD,ica.QUANTITY_CW, NAME_CW,\n" +
      "ica.STRIKE_PRICE_CW,ica.SECTYPE_CW,ica.UNDERLYINGSTOCK_CW,REPLACE(ica.PERIOD_CW,' tháng','') AS PERIOD_CW,ica.RATIO ,\n" +
      "ica.ISSUER_CW ,ica.ISSUER_CW_EN ,TO_CHAR(ica.LASTTRADINGDATE_CW , 'YYYY-MM-DD') AS LASTTRADINGDATE_CW ,TO_CHAR(ica.EXPIREDDATE_CW , 'YYYY-MM-DD') AS EXPIREDDATE_CW ,REGISTRATIONVOLUME_CW" +
      " FROM ICALENDAR_ASSET_ALLDATA ica " +
      " WHERE CUSTODYCD=:p_custodyID AND PRODUCTTYPE in :p_productType" +
      " AND EVENTDATE BETWEEN TO_DATE(:p_startDate, 'YYYY-MM-DD') AND TO_DATE(:p_endDate, 'YYYY-MM-DD')" +
      " AND ETLCURDATE = (SELECT MAX(ETLCURDATE) FROM ICALENDAR_ASSET_ALLDATA) ORDER BY ica.EVENTDATE DESC");
    try {
      List<HashMap<String, Object>> resultList = hfc.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("p_custodyID", custodyId)
        .setParameter("p_productType", productType)
        .setParameter("p_startDate", fromDate)
        .setParameter("p_endDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
