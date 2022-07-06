package com.tcbs.automation.tcbsdwh.ani.monthlystatement;

import com.tcbs.automation.tcbsdwh.Tcbsdwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MonthlyStatementProc6Entity {

  @Step("Get data from sql")
  public static List<HashMap<String, Object>> getSaoKeThangSql(String endmonth) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" with user_pe as ( ");
    queryBuilder.append(" select distinct user_id from dwh.rt_perf_ngin_pe_user_performance_snapshot ");
    queryBuilder.append(" where report_date BETWEEN DATE_TRUNC('mon', cast(:endmonth as date)) AND cast(:endmonth as date) ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" select top 50000 u.user_id, ");
    queryBuilder.append(" cast(d.date as timestamp) as report_date,d.lpickenddate ");
    queryBuilder.append(" ,NVL(pps.numerator,0) AS gia_tri_danh_muc ");
    queryBuilder.append(" ,NVL(pps.portf_index,0) AS portf_index ");
    queryBuilder.append(" from user_pe u ");
    queryBuilder.append(" left join dwh.smy_dwh_date_dim d ON 1=1 ");
    queryBuilder.append(" left join dwh.rt_perf_ngin_pe_user_performance_snapshot pps on u.user_id = pps.user_id and pps.report_date = d.lpickenddate ");
    queryBuilder.append(" where d.date between DATE_TRUNC('mon', cast(:endmonth as date)) AND cast(:endmonth as date) ");
    queryBuilder.append(" order by u.user_id,d.date ");


    try {

      return Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("endmonth", endmonth)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data from sql")
  public static List<HashMap<String, Object>> getSaoKeThangProcSql() {
    StringBuilder queryBuilderProc = new StringBuilder();

    queryBuilderProc.append(" select top 50000 * from dwh.smy_dwh_monthlystatement_pe_user order by user_id, report_date ");

    try {

      return Tcbsdwh.tcbsDwhDbConnection.getSession().createNativeQuery(queryBuilderProc.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
