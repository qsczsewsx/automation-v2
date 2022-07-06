package com.tcbs.automation.dwh;

import net.thucydides.core.annotations.Step;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.HashMap;
import java.util.List;

public class ICalPlanEventEntity {
  @Step("get plan event from db")
  public static List<HashMap<String, Object>> byCondition(String tcbsId, String startDate, String endDate, List<String> eventType) {
    StringBuilder query = new StringBuilder();
    query.append("select productEventType, eventType, defId, defType, [objId], [data], eventId,   ");
    query.append("    cast(eventDueDate as date) as eventDueDate, eventStatus, eventIndex, custodyCd, tcbsId,   ");
    query.append("    amount, eventName, productCode ");
    query.append("from icalendar_tcw_plan_event  ");
    query.append("where etlcurdate = (select max(etlcurdate) from icalendar_tcw_plan_event)  ");
    query.append("and tcbsid = :tcbsid   ");
    query.append("and cast(eventDueDate as date) between cast(:startDate as date) and cast(:endDate as date) ");
    if (eventType.size() != 0 && !eventType.get(0).equals("")) {
      query.append(" and eventType in :eventType  ");
      return Dwh.dwhDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("tcbsid", tcbsId)
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .setParameter("eventType", eventType)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } else {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("tcbsid", tcbsId)
        .setParameter("startDate", startDate)
        .setParameter("endDate", endDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    }
  }
}
