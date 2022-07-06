package com.tcbs.automation.hfcdata;

import net.thucydides.core.annotations.Step;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.HashMap;
import java.util.List;

public class ICalendarAnniversaryDataEntity {
  @Step("get bond static data from db")
  public static List<HashMap<String, Object>> byCondition(String parentCustody, String childCus, String startDate, String endDate) {
    StringBuilder query = new StringBuilder();

    query.append(" SELECT * FROM ( ");
    query.append("      SELECT ICAL.DEFID, ICAL.DEFTYPE, ICAL.OBJID, ICAL.\"DATA\", ICAL.EVENTID, ICAL.EVENTDUEDATE ,  ");
    query.append("      ICAL.EVENTSTATUS, ICAL.EVENTINDEX, ICAL.CUSTODYCD, ICAL.TCBSID, ICAL.STOCKCACODE, ICAL.STOCKBALANCE,  ");
    query.append("      ICAL.STOCKQUANTITY, ICAL.STOCKAMOUNT, ICAL.STOCKSTATUS, ICAL.COUPONPAYMENTAMTAFTERTAX as \"couponPayment\", ICAL.MILESTONE_META    ");
    query.append("      FROM ICALENDAR_ALLDATA ical  ");
    query.append("      WHERE ICAL.ETLCURDATE = (SELECT max(ETLCURDATE) FROM ICALENDAR_ALLDATA)  ");
    query.append("      AND ical.EVENTDUEDATE BETWEEN TO_DATE(:startDate,'YYYY-MM-DD') AND TO_DATE(:endDate, 'YYYY-MM-DD')  ");
    query.append("      AND ICAL.CUSTODYCD = :parentCustody ");
    if (childCus.equalsIgnoreCase("RM")) {
      query.append("      UNION ALL  ");
      query.append("      SELECT ICAL.DEFID, ICAL.DEFTYPE, ICAL.OBJID, ICAL.\"DATA\", ICAL.EVENTID, ICAL.EVENTDUEDATE ,  ");
      query.append("      ICAL.EVENTSTATUS, ICAL.EVENTINDEX, ICAL.CUSTODYCD, ICAL.TCBSID, ICAL.STOCKCACODE, ICAL.STOCKBALANCE,   ");
      query.append("      ICAL.STOCKQUANTITY, ICAL.STOCKAMOUNT, ICAL.STOCKSTATUS, ICAL.RM_COUPONPAYMENTAMTAFTERTAX as \"couponPayment\", ICAL.MILESTONE_META  ");
      query.append("      FROM ICALENDAR_ALLDATA ical  ");
      query.append("      WHERE ICAL.ETLCURDATE = (SELECT max(ETLCURDATE) FROM ICALENDAR_ALLDATA)  ");
      query.append("      AND ical.EVENTDUEDATE BETWEEN TO_DATE(:startDate,'YYYY-MM-DD') AND TO_DATE(:endDate, 'YYYY-MM-DD')  ");
      query.append("      AND ICAL.RM_CUSTODYCD = :parentCustody  ");
    }
    query.append(" ) ic  ");
    query.append(" ORDER BY ic.EVENTDUEDATE ASC  ");

    return HfcData.hfcDataDbConnection.getSession().createNativeQuery(query.toString())
      .setParameter("parentCustody", parentCustody)
      .setParameter("startDate", startDate)
      .setParameter("endDate", endDate)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

  }

}
