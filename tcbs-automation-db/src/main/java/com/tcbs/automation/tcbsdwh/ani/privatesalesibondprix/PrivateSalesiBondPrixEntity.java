package com.tcbs.automation.tcbsdwh.ani.privatesalesibondprix;

import com.tcbs.automation.Database;
import com.tcbs.automation.HibernateEdition;
import com.tcbs.automation.tcbsdwh.ani.CommonProcData;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

public class PrivateSalesiBondPrixEntity {
  public static final HibernateEdition redShiftDb = Database.TCBS_DWH.getConnection();

  @Step("Get data from sql")
  public static List<HashMap<String, Object>> getPrivateSalesiBondPrixFromSql(String fromDate, String toDate, String customercustodycd) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" WITH bond_details AS ( ");
    queryBuilder.append(" select bd.tradingcode,id, ");
    queryBuilder.append(" bd.customername, ");
    queryBuilder.append(" bd.customercustodycd, ");
    queryBuilder.append(" bd.quantity, ");
    queryBuilder.append(" bd.bondparvalue , ");
    queryBuilder.append(" cast(bd.tradingdate as date) as tradingdate, ");
    queryBuilder.append(" hm.holdmonth, ");
    queryBuilder.append(" CASE WHEN round(bd.quantity * bd.bondparvalue,0) >= 1e9 AND round(bd.quantity * bd.bondparvalue,0) < 5e9 THEN round(round(bd.quantity * bd.bondparvalue,0) * 0.0042/1000,0) ");
    queryBuilder.append(" WHEN round(bd.quantity * bd.bondparvalue,0) >= 5e9 AND round(bd.quantity * bd.bondparvalue,0) < 10e9 THEN round(round(bd.quantity * bd.bondparvalue,0) * 0.004/1000,0) ");
    queryBuilder.append(" WHEN round(bd.quantity * bd.bondparvalue,0) >= 10e9 AND round(bd.quantity * bd.bondparvalue,0) < 20e9 THEN round(round(bd.quantity * bd.bondparvalue,0) * 0.005/1000,0) ");
    queryBuilder.append(" WHEN round(bd.quantity * bd.bondparvalue,0) >= 20e9 THEN round(round(bd.quantity * bd.bondparvalue,0) * 0.0058/1000,0) ");
    queryBuilder.append(" END AS iXuPoint, ");
    queryBuilder.append(" round(bd.quantity * bd.bondparvalue,0) AS total_value ");
    queryBuilder.append(" ,CASE WHEN round(bd.quantity * bd.bondparvalue,0) >= 1e9 AND round(bd.quantity * bd.bondparvalue,0) < 5e9 THEN '0.42%' ");
    queryBuilder.append(" WHEN round(bd.quantity * bd.bondparvalue,0) >= 5e9 AND round(bd.quantity * bd.bondparvalue,0) < 10e9 THEN '0.40%' ");
    queryBuilder.append(" WHEN round(bd.quantity * bd.bondparvalue,0) >= 10e9 AND round(bd.quantity * bd.bondparvalue,0) < 20e9 THEN '0.50%' ");
    queryBuilder.append(" WHEN round(bd.quantity * bd.bondparvalue,0) >= 20e9 THEN '0.58%' ");
    queryBuilder.append(" END AS uudai ");
    queryBuilder.append(" from dwh.Smy_dwh_tcb_Bond_Trading_Details bd ");
    queryBuilder.append(" LEFT JOIN (select Order_ID, cast(VALUE as int) as holdmonth  ");
    queryBuilder.append(" from staging.stg_tcb_trading_attr where name = 'holdMonth') hm on bd.ID = hm.ORDER_ID ");
    queryBuilder.append(" WHERE bd.etlcurdate = (SELECT max(etlcurdate) FROM dwh.Smy_dwh_tcb_Bond_Trading_Details) ");
    queryBuilder.append(" and bd.action = 5  ");
    queryBuilder.append(" AND hm.holdmonth >= 12  ");
    queryBuilder.append(" AND nvl(bd.marketstatus, 0) <> 1   ");
    queryBuilder.append(" AND nvl(bd.accountantstatus, 0) = 1 ");
    queryBuilder.append(" AND nvl(bd.agencystatus, 0) = 1  ");
    queryBuilder.append(" AND round((bd.quantity * bd.bondparvalue),0) >= 1e9  ");
    queryBuilder.append(" and bondproductcode = 'iBondPrix-VIFCB2124004' ");
    queryBuilder.append(" and  cast(:fromdate as date ) <= bd.tradingdate AND bd.tradingdate <= cast(:todate as date ) ");
    queryBuilder.append(" and cast(bd.tradingdate as date) > '2022-03-31' order by customercustodycd  ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" ,bondbal2 AS ( ");
    queryBuilder.append(" select custodycd , sum(nvl(principal,0)) as bondbal_202203 ");
    queryBuilder.append(" from dwh.dailyport_bondbal  ");
    queryBuilder.append(" where bondcode <> bondproductcode ");
    queryBuilder.append(" and datereport = '2022-03-31' ");
    queryBuilder.append(" group by custodycd ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" SELECT bd.tradingcode, bd.customername, bd.customercustodycd, bd.quantity , bd.tradingdate, bd.holdmonth  ");
    queryBuilder.append(" , bd.ixupoint, bd.total_value, bb2.bondbal_202203, sum(nvl(bb.principal,0)) as bondbal_tradingdate ");
    queryBuilder.append(" FROM bond_details bd  ");
    queryBuilder.append(" left JOIN bondbal2 bb2 ON bb2.custodycd = bd.customercustodycd  ");
    queryBuilder.append(" left join dwh.dailyport_bondbal bb  ");
    queryBuilder.append(" on cast(bb.datereport as date) = bd.tradingdate and bb.custodycd = bd.customercustodycd  ");
    queryBuilder.append(" WHERE 1=1 and bd.customercustodycd like :customercustodycd ");
    queryBuilder.append(" group by bd.tradingcode, bd.customername, bd.customercustodycd, bd.quantity, bd.tradingdate, bd.holdmonth ");
    queryBuilder.append(" , bd.ixupoint, bb2.bondbal_202203, bd.total_value ");
    queryBuilder.append(" having (nvl(bondbal_tradingdate, 0) - nvl(bb2.bondbal_202203, 0)) >= 1e9 ");
    queryBuilder.append(" ORDER BY bd.tradingdate, bd.tradingcode ");


    try {
      Session session = redShiftDb.getSession();
      session.clear();
      beginTransaction(session);
      List<HashMap<String, Object>> resultList = session.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("fromdate", fromDate)
        .setParameter("todate", toDate)
        .setParameter("customercustodycd", customercustodycd)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      redShiftDb.closeSession();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getPrivateSalesiBondPrixDAFromSql(String fromDate, String toDate, String customercustodycd) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" WITH bond_details AS ( ");
    queryBuilder.append(" select bd.tradingcode, ");
    queryBuilder.append(" bd.customername, ");
    queryBuilder.append(" bd.customercustodycd, ");
    queryBuilder.append(" bd.quantity, ");
    queryBuilder.append(" bd.bondparvalue , ");
    queryBuilder.append(" cast(bd.tradingdate as date) as tradingdate, ");
    queryBuilder.append(" hm.holdmonth, ");
    queryBuilder.append(
      " CASE WHEN (nvl(CAST(bd.quantity AS int), 0) * nvl(bd.bondparvalue, 0)) >= 1e9 AND (nvl(CAST(bd.quantity AS int), 0) * nvl(bd.bondparvalue, 0)) < 5e9 THEN (nvl(CAST(bd.quantity AS int), 0) * nvl(bd.bondparvalue, 0)) * 0.0042/1000 ");
    queryBuilder.append(
      " WHEN (nvl(CAST(bd.quantity AS int), 0) * nvl(bd.bondparvalue, 0)) >= 5e9 AND (nvl(CAST(bd.quantity AS int), 0) * nvl(bd.bondparvalue, 0)) < 10e9 THEN (nvl(CAST(bd.quantity AS int), 0) * nvl(bd.bondparvalue, 0)) * 0.004/1000 ");
    queryBuilder.append(
      " WHEN (nvl(CAST(bd.quantity AS int), 0) * nvl(bd.bondparvalue, 0)) >= 10e9 AND (nvl(CAST(bd.quantity AS int), 0) * nvl(bd.bondparvalue, 0)) < 20e9 THEN (nvl(CAST(bd.quantity AS int), 0) * nvl(bd.bondparvalue, 0)) * 0.005/1000 ");
    queryBuilder.append(" WHEN (nvl(CAST(bd.quantity AS int), 0) * nvl(bd.bondparvalue, 0)) >= 20e9 THEN (nvl(CAST(bd.quantity AS int), 0) * nvl(bd.bondparvalue, 0)) * 0.0058/1000 ");
    queryBuilder.append(" END AS iXuPoint, ");
    queryBuilder.append(" round(bd.quantity * bd.bondparvalue,0) AS total_value ");
    queryBuilder.append(" FROM dwh.Smy_dwh_tcb_Bond_Trading_Details bd ");
    queryBuilder.append(" LEFT JOIN (select Order_ID, cast(VALUE as int) as holdmonth  ");
    queryBuilder.append(" from staging.stg_tcb_trading_attr where name = 'holdMonth') hm on bd.ID = hm.ORDER_ID ");
    queryBuilder.append(" WHERE etlcurdate = (SELECT max(etlcurdate) FROM dwh.Smy_dwh_tcb_Bond_Trading_Details) ");
    queryBuilder.append(" AND ACTION = 5  ");
    queryBuilder.append(" AND hm.holdmonth >= 12  ");
    queryBuilder.append(" AND nvl(bd.marketstatus, 0) <> 1  ");
    queryBuilder.append(" AND nvl(bd.accountantstatus, 0) = 1  ");
    queryBuilder.append(" AND nvl(bd.agencystatus, 0) = 1  ");
    queryBuilder.append(" AND (nvl(bd.quantity, 0) * nvl(bd.bondparvalue, 0)) >= 1e9 ");
    queryBuilder.append(" and bondproductcode = 'iBondPrix-VIFCB2124004' ");
    queryBuilder.append(" and cast(:fromdate as date ) <= bd.tradingdate AND bd.tradingdate <= cast(:todate as date ) ");
    queryBuilder.append(" )  ");
    queryBuilder.append(" ,bondbal2 AS ( ");
    queryBuilder.append(" select custodycd , sum(nvl(principal,0)) as bondbal_202203 ");
    queryBuilder.append(" from dwh.dailyport_bondbal ");
    queryBuilder.append(" where bondcode <> bondproductcode ");
    queryBuilder.append(" and datereport = '2022-03-31' ");
    queryBuilder.append(" group by custodycd ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" SELECT bd.tradingcode, bd.customername, bd.customercustodycd, bd.quantity ");
    queryBuilder.append(" , bd.tradingdate, bd.holdmonth ");
    queryBuilder.append(" , bd.ixupoint, bd.total_value, bb2.bondbal_202203, sum(nvl(bb.principal,0)) as bondbal_tradingdate ");
    queryBuilder.append(" FROM bond_details bd  ");
    queryBuilder.append(" left JOIN bondbal2 bb2 ON bb2.custodycd = bd.customercustodycd  ");
    queryBuilder.append(" left join dwh.dailyport_bondbal bb on cast(bb.datereport as date) = bd.tradingdate and bb.custodycd = bd.customercustodycd  ");
    queryBuilder.append(" WHERE 1=1 and bd.customercustodycd like :customercustodycd ");
    queryBuilder.append(" group by bd.tradingcode, bd.customername, bd.customercustodycd, bd.quantity ");
    queryBuilder.append(" , bd.tradingdate, bd.holdmonth ");
    queryBuilder.append(" , bd.ixupoint, bb2.bondbal_202203, bd.total_value ");
    queryBuilder.append(" having (nvl(bondbal_tradingdate, 0) - nvl(bb2.bondbal_202203, 0)) >= 1e9 ");
    queryBuilder.append(" and tradingdate > '2022-03-31' ");
    queryBuilder.append(" ORDER BY bd.tradingdate, bd.tradingcode ");


    try {
      Session session = redShiftDb.getSession();
      session.clear();
      beginTransaction(session);
      List<HashMap<String, Object>> resultList = session.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("fromdate", fromDate)
        .setParameter("todate", toDate)
        .setParameter("customercustodycd", customercustodycd)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      redShiftDb.closeSession();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


  @Step(" Get data ")
  public static List<HashMap<String, Object>> executeQuery(String query) {
    StringBuilder sqlQuery = new StringBuilder()
      .append(query);

    CommonProcData.stagingRedShiftDb.closeSession();
    CommonProcData.stagingRedShiftDb.openSession();
    Session session = CommonProcData.stagingRedShiftDb.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    try {
      session.createNativeQuery(sqlQuery.toString()).executeUpdate();
    } catch (Exception ex) {
      System.out.println(ex);
    }

    return null;
  }


}
