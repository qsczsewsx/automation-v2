package com.tcbs.automation.hfcdata.icalendar.noti;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import net.serenitybdd.screenplay.Question;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotiConfigEntity {
  public static final HibernateEdition hfc = Database.HFC_DATA.getConnection();
  private static final Logger logger = LoggerFactory.getLogger(NotiConfigEntity.class);

  @Step("Get data noti config list")
  public static List<HashMap<String, Object>> getConfigNoti(String combineKey, String objId, String eventCode, String objCode) {
    List<HashMap<String, Object>> result = new ArrayList<>();
    HashMap<String, Object> memberKey = new HashMap<>();
    StringBuilder queryConfig = new StringBuilder();
    queryConfig.append("SELECT * FROM ICAL_NOTI_CONFIG WHERE COMBINE_KEY = :P_COMBINE_KEY");
    try {
      List<HashMap<String, Object>> resultList = hfc.getSession().createNativeQuery(queryConfig.toString())
        .setParameter("P_COMBINE_KEY", combineKey)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      if (resultList == null) {
        return result;
      }
      if (resultList.isEmpty()) {
        return result;
      }
      memberKey = resultList.get(0);


      StringBuilder queryTML = new StringBuilder();
      queryTML.append("SELECT DEF_STATUS FROM ICAL_MTL_TIMELINE WHERE OBJ_ID  = :P_OBJID AND EVENT_CODE = :P_EVENT_CODE AND OBJ_CODE = :P_OBJCODE");


      List<HashMap<String, Object>> resultMTLList = hfc.getSession().createNativeQuery(queryTML.toString())
        .setParameter("P_OBJID", objId)
        .setParameter("P_EVENT_CODE", eventCode)
        .setParameter("P_OBJCODE", objCode)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

      if (resultList != null || resultMTLList.size() != 0) {
        memberKey.put("DEF_STATUS", resultMTLList.get(0).get("DEF_STATUS"));
      }


      BigDecimal inboxId = (BigDecimal) resultList.get(0).get("INBOX_ID");
      if (inboxId != null) {
        StringBuilder emailQueryStr = new StringBuilder();
        emailQueryStr.append("SELECT * FROM ICAL_NOTI_CONFIG_INBOX WHERE ID = :P_ID");
        List<HashMap<String, Object>> configInbox = hfc.getSession().createNativeQuery(emailQueryStr.toString())
          .setParameter("P_ID", inboxId.toPlainString())
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();


        if (configInbox == null) {
          return result;
        }
        if (configInbox.isEmpty()) {
          return result;
        }
        HashMap<String, Object> inboxMapping = configInbox.get(0);

        for (Map.Entry<String, Object> entry : inboxMapping.entrySet()) {
          String key = entry.getKey();
          Object value = entry.getValue();
          memberKey.put(key, value);
        }
      }
      result.add(memberKey);
      return result;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
      ex.printStackTrace();
    }
    return new ArrayList<>();
  }

  public static Question<List<HashMap<String, Object>>> fromDB(String combineKey, String objId, String eventCode, String objCode) {
    return Question.about("Get Data from Databases")
      .answeredBy(
        actor -> {
          List<HashMap<String, Object>> listData = getConfigNoti(combineKey,objId,eventCode,objCode);
          return listData;
        }
      );
  }
}
