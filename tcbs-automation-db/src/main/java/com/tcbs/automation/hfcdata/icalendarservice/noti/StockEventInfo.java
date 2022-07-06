package com.tcbs.automation.hfcdata.icalendarservice.noti;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import net.serenitybdd.screenplay.Question;
import net.thucydides.core.annotations.Step;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StockEventInfo {
  public static final HibernateEdition hfc = Database.HFC_DATA.getConnection();

  private static final HashMap<String, String> CONVERSATIONALIST = new HashMap<>();

  static {
    CONVERSATIONALIST.put("RECORD_DATE", "RECORD_DATE");
    CONVERSATIONALIST.put("EXRIGHT_DATE", "EXRIGHT_DATE");
    CONVERSATIONALIST.put("ISSUE_DATE", "ISSUE_DATE");
  }

  public static Question<List<HashMap<String, Object>>> fromDB(String pTicker, String pEventCode, String pEventDate) {
    return Question.about("Get Data from Databases")
      .answeredBy(
        actor -> {
          return getCWEventInfo(pTicker, pEventCode, pEventDate);
        }
      );
  }

  @Step("Get data stock event info")
  public static List<HashMap<String, Object>> getCWEventInfo(String pTicker, String pEventCode, String pEventDate) {
    StringBuilder queryConfig = new StringBuilder();
    queryConfig.append("SELECT * FROM ICAL_EVM_EXT_STOCK WHERE TICKER = :P_TICKER and EVENT_CODE = :P_EVENT_CODE and EXRIGHT_DATE = TO_DATE(:P_EVENT_DATE, 'yyyy-MM-dd')");
    List<HashMap<String, Object>> resultList = hfc.getSession().createNativeQuery(queryConfig.toString())
      .setParameter("P_TICKER", pTicker)
      .setParameter("P_EVENT_CODE", pEventCode)
      .setParameter("P_EVENT_DATE", pEventDate)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
      .getResultList();
    if (resultList == null) {
      return new ArrayList<>();
    }
    if (resultList.isEmpty()) {
      return new ArrayList<>();
    }
    com.tcbs.automation.hfcdata.icalendarservice.noti.CwEventInfo.convertToAPI(resultList, CONVERSATIONALIST);
    return resultList;
  }
}
