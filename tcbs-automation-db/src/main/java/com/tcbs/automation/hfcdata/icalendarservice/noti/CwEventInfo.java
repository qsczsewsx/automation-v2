package com.tcbs.automation.hfcdata.icalendarservice.noti;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import net.serenitybdd.screenplay.Question;
import net.thucydides.core.annotations.Step;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CwEventInfo {
  public static final HibernateEdition hfc = Database.HFC_DATA.getConnection();
  private static final HashMap<String, String> CONVERSATIONALIST = new HashMap<>();
  static {
    CONVERSATIONALIST.put("ISSUED_DATE", "ISSUED_DATE");
    CONVERSATIONALIST.put("LISTED_DATE", "LISTED_DATE");
    CONVERSATIONALIST.put("FIRST_TRADING_DATE", "FIRST_TRADING_DATE");
    CONVERSATIONALIST.put("MATURITY_DATE", "MATURITY_DATE");
    CONVERSATIONALIST.put("LAST_TRADING_DATE", "LAST_TRADING_DATE");
  }
  public static Question<List<HashMap<String, Object>>> fromDB(String pTicker) {
    return Question.about("Get Data from Databases")
      .answeredBy(
        actor -> {
          return getCWEventInfo(pTicker);
        }
      );
  }
  @Step("Get data cw event info")
  public static List<HashMap<String, Object>> getCWEventInfo(String pTicker) {
    List<HashMap<String, Object>> resultList = hfc.getSession().createNativeQuery("SELECT * FROM ICAL_EVM_EXT_CW WHERE CW_CODE = :P_TICKER")
      .setParameter("P_TICKER", pTicker)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
      .getResultList();
    if (resultList == null) {
      return new ArrayList<>();
    }
    if (resultList.isEmpty()) {
      return new ArrayList<>();
    }
    convertToAPI(resultList, CONVERSATIONALIST);
    return resultList;
  }
  public static Long getLongFromTimestamp(Timestamp timestamp) {
    return timestamp.getTime();
  }
  public static void convertToAPI(List<HashMap<String, Object>> resultList, HashMap<String, String> listMappingConvert) {
    for (HashMap<String, Object> item : resultList) {
      for (Map.Entry<String, Object> entry : item.entrySet()) {
        String key = entry.getKey();
        Object value = entry.getValue();
        if (listMappingConvert.containsKey(key)) {
          item.put(key, getLongFromTimestamp((Timestamp) value));
        }
      }
    }
  }
}
