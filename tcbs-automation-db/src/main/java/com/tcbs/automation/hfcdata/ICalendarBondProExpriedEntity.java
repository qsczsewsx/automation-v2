package com.tcbs.automation.hfcdata;

import net.thucydides.core.annotations.Step;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.HashMap;
import java.util.List;

public class ICalendarBondProExpriedEntity {
  @Step("get pro expired data from db")
  public static List<HashMap<String, Object>> byCondition(String parentCustody, String view, String startDate, String endDate) {
    StringBuilder query = new StringBuilder();
    String parentCustodyStr = "parentCustody";
    String startDateStr = "startDate";
    String toDateStr = "toDate";
    query.append(" SELECT * FROM ( ");
    query.append(" SELECT ical.DEFID AS defId, ical.DEFTYPE AS defType, ical.OBJID AS objId, ical.\"DATA\" , ical.EVENTID AS eventId,   ");
    query.append("        ical.EVENTDUEDATE AS eventDueDate, ical.EVENTSTATUS AS eventStatus, ical.CUSTODYCD as custodyCd, ical.TCBSID AS TcbsId,   ");
    query.append("        ical.TRADINGCODE AS TradingCode, ical.OBALQ AS OBalQ, ical.RECEIVABLEAMOUNTPROEXPIRED AS receivableAmountProExpired, ");
    query.append("        ical.INVESTMENTRATEPROEXPIRED AS investmentRateProExpired, ical.MATURITYDATE AS maturityDate,  ");
    query.append("        ical.RECEIVABLEAMOUNTHTM AS receivableAmountHTM, ical.INVESTMENTRATEHTM AS investmentRateHTM  ");
    query.append(" FROM ICALENDAR_BONDPROEXPRIED ical  ");
    query.append(" WHERE ical.ETLCURDATE = (SELECT max(ETLCURDATE) FROM ICALENDAR_BONDPROEXPRIED)  ");
    query.append(" AND ical.EVENTDUEDATE BETWEEN TO_DATE(:startDate, 'YYYY-MM-DD') AND TO_DATE(:toDate, 'YYYY-MM-DD')  ");
    if (view.equalsIgnoreCase("CUS")) {
      query.append("  AND ical.CUSTODYCD = :parentCustody ");
    } else {
      query.append(" AND ( ical.CUSTODYCD = :parentCustody ");
      query.append("      OR ( ical.RM_CUSTODYCD = :parentCustody ");
      query.append("          AND (SUBSTR(ical.TRADINGCODE ,1,2) != 'IB' OR SUBSTR(ical.TRADINGCODE ,1,4) != 'IWDC') )) ");
    }
    query.append(" ) ic  ");
    query.append(" ORDER BY ic.EVENTDUEDATE ASC  ");
    return HfcData.hfcDataDbConnection.getSession().createNativeQuery(query.toString())
      .setParameter(parentCustodyStr, parentCustody)
      .setParameter(startDateStr, startDate)
      .setParameter(toDateStr, endDate)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
  }
}

