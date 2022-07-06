package com.tcbs.automation.dwh.ixu;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.text.SimpleDateFormat;
import java.util.*;

public class GetCasaEntity {
  public static final String  DATE_REPORT = "datereport";

  @Step("Get casa")
  public static List<HashMap<String, Object>> getCasa(String date, String date20, String date30, String date90) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" with cusDate as ( ");
    queryStringBuilder.append("   select tcbsid, eop_bal casa, datereport, rmemail, aum_vip ");
    queryStringBuilder.append("   from smy_dwh_dwhtcb_tcbs_casa ");
    queryStringBuilder.append("   where datereport = :date and aum_vip is not null ");
    queryStringBuilder.append(" ) ");
    queryStringBuilder.append(" , avg20Date as ( ");
    queryStringBuilder.append("   select tcbsid, sum(eop_bal)/(1 + datediff(dd, :date20, :date)) avg20 from smy_dwh_dwhtcb_tcbs_casa ");
    queryStringBuilder.append(" where datereport BETWEEN :date20 and :date ");
//    queryStringBuilder.append(" and aum_vip is not null ");
    queryStringBuilder.append(" group by tcbsid  ) ");
    queryStringBuilder.append(" , avg30Date as ( ");
    queryStringBuilder.append("   select tcbsid, sum(eop_bal)/(1 + datediff(dd, :date30, :date)) avg30   from smy_dwh_dwhtcb_tcbs_casa ");
    queryStringBuilder.append(" where datereport BETWEEN :date30 and :date ");
//    queryStringBuilder.append(" and aum_vip is not null ");
    queryStringBuilder.append(" group by tcbsid  ) ");
    queryStringBuilder.append(" , avg90Date as ( ");
    queryStringBuilder.append("   select tcbsid, sum(eop_bal)/( 1+ datediff(dd, :date90, :date)) avg90 from smy_dwh_dwhtcb_tcbs_casa ");
    queryStringBuilder.append(" where datereport BETWEEN :date90 and :date ");
//    queryStringBuilder.append(" and aum_vip is not null ");
    queryStringBuilder.append(" group by tcbsid ");
    queryStringBuilder.append(" ) ");
    queryStringBuilder.append(" select c.dateReport, c.tcbsId, c.rmemail, c.aum_vip as vipType, c.casa, a20.avg20, a30.avg30, a90.avg90 ");
    queryStringBuilder.append(" from cusDate c ");
    queryStringBuilder.append(" left join avg20Date a20 on c.tcbsid = a20.tcbsid ");
    queryStringBuilder.append(" left join avg30Date a30 on c.tcbsid = a30.tcbsid ");
    queryStringBuilder.append(" left join avg90Date a90 on c.tcbsid = a90.tcbsid ");
    queryStringBuilder.append(" order by c.tcbsId ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("date", date)
        .setParameter("date20", date20)
        .setParameter("date30", date30)
        .setParameter("date90", date90)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get casa at date")
  public static Map<String, Object> getCasaAtDate(String datereport, String tcbsid) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select tcbsid, eop_bal, datereport from smy_dwh_dwhtcb_tcbs_casa " +
      "where datereport = :datereport and tcbsid = :tcbsid and aum_vip is not null");

    try {
      List<HashMap<String, Object>> result = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter(DATE_REPORT, datereport)
        .setParameter("tcbsid", tcbsid)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      if (!result.isEmpty()) {
        Date date = (Date) result.get(0).get(DATE_REPORT);
        SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd");
        result.get(0).put(DATE_REPORT, simpleFormatter.format(date));
        return result.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  @Step("Get casa at date")
  public static Integer countDateCasa(Date dateFrom, Date date) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select count(distinct datereport) as count_date from smy_dwh_dwhtcb_tcbs_casa where datereport BETWEEN :dateFrom and :date");

    try {
      List<HashMap<String, Object>> result = Dwh.dwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("dateFrom", dateFrom)
        .setParameter("date", date)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return Integer.parseInt(String.valueOf(result.get(0).get("count_date")));
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return 0;
  }
}
