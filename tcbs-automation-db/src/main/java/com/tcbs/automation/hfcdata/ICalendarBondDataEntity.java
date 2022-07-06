package com.tcbs.automation.hfcdata;

import net.thucydides.core.annotations.Step;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.HashMap;
import java.util.List;

public class ICalendarBondDataEntity {
  @Step("get bond static data from db")
  public static List<HashMap<String, Object>> byCondition(String parentCustody, String childCustody, String startDate, String endDate) {
    StringBuilder query = new StringBuilder();

    query.append(" SELECT * FROM ( ");
    query.append(" SELECT ical.DEFID , ICAL.DEFTYPE , ICAL.OBJID , ICAL.\"DATA\" , ICAL.EVENTID , ICAL.EVENTDUEDATE , TCBSID ,  ");
    query.append("        ical.EVENTSTATUS , ical.CUSTODYCD , ical.OBALQ , ical.OBALPARVALUE   ");
    query.append(" FROM ICALENDAR_BONDDATA ical  ");
    query.append(" WHERE ical.ETLCURDATE = (SELECT max(ETLCURDATE) FROM ICALENDAR_BONDDATA ib)  ");
    query.append(" AND ical.EVENTDUEDATE BETWEEN TO_DATE(:startDate, 'YYYY-MM-DD') AND TO_DATE(:toDate, 'YYYY-MM-DD')   ");
    query.append(" AND ical.OBALQ IS NOT NULL AND ical.OBALQ > 0 AND ical.CUSTODYCD = :parentCustody ");
    if (childCustody.equalsIgnoreCase("RM")) {
      query.append("  UNION ALL  ");
      query.append(" SELECT ical.DEFID , ICAL.DEFTYPE , ICAL.OBJID , ICAL.\"DATA\" , ICAL.EVENTID , ICAL.EVENTDUEDATE , TCBSID ,  ");
      query.append("        ical.EVENTSTATUS , ical.CUSTODYCD , ical.RM_OBALQ AS OBALQ , ical.RM_OBALPARVALUE AS OBALPARVALUE     ");
      query.append(" FROM ICALENDAR_BONDDATA ical  ");
      query.append(" WHERE ical.ETLCURDATE = (SELECT max(ETLCURDATE) FROM ICALENDAR_BONDDATA ib)  ");
      query.append(" AND ical.EVENTDUEDATE BETWEEN TO_DATE(:startDate, 'YYYY-MM-DD') AND TO_DATE(:toDate, 'YYYY-MM-DD') ");
      query.append(" AND ical.RM_OBALQ IS NOT NULL AND ical.RM_OBALQ > 0 AND ical.RM_CUSTODYCD = :parentCustody ");
    }
    query.append(" ) ic  ");
    query.append(" ORDER BY ic.EVENTDUEDATE ASC  ");

    return HfcData.hfcDataDbConnection.getSession().createNativeQuery(query.toString())
      .setParameter("parentCustody", parentCustody)
      .setParameter("startDate", startDate)
      .setParameter("toDate", endDate)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

  }

}
