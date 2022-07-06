package com.tcbs.automation.stockmarket;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetIntradayPnlEntity {
  private static final String CUSTODY_ID = "custodyCd";
  private static final String FROM = "from";

  @Step("get data")
  public static void updateDataTest(String custodyCd, long from) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("UPDATE future_intraday_pnl_1min SET custody_cd = :custodyCd  ");
    queryStringBuilder.append("WHERE custody_cd IN ( ");
    queryStringBuilder.append("    SELECT custody_cd FROM ( ");
    queryStringBuilder.append("        SELECT DISTINCT custody_cd, case when STRCMP(custody_cd, :custodyCd) != 0 then 1 else 0 end cmp FROM future_intraday_pnl_1min  ");
    queryStringBuilder.append("        where seq_time >= FLOOR(:from /86400)*86400 and custody_cd is not null ");
    queryStringBuilder.append("        order by cmp asc LIMIT 0, 1 ");
    queryStringBuilder.append("    ) tmp ");
    queryStringBuilder.append(") ");

    try {
      Session session = FuturesMarket.futuresMarketConnection.getSession();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      Query query = session
        .createNativeQuery(queryStringBuilder.toString())
        .setParameter(CUSTODY_ID, custodyCd)
        .setParameter(FROM, from)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

      query.executeUpdate();
      session.getTransaction().commit();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    } finally {
      FuturesMarket.futuresMarketConnection.closeSession();
    }
  }

  @Step("get data")
  public static void updateDataLogTest(String custodyCd, long from) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("UPDATE future_balance_logs SET custody_cd = :custodyCd  ");
    queryStringBuilder.append("WHERE account IN ( ");
    queryStringBuilder.append(" SELECT account FROM ( ");
    queryStringBuilder.append("   select DISTINCT account from future_intraday_pnl_1min where seq_time >= FLOOR(:from /86400)*86400 and custody_cd = :custodyCd ");
    queryStringBuilder.append(" ) tmp ) ");

    try {
      Session session = FuturesMarket.futuresMarketConnection.getSession();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      Query query = session
        .createNativeQuery(queryStringBuilder.toString())
        .setParameter(CUSTODY_ID, custodyCd)
        .setParameter(FROM, from)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);

      query.executeUpdate();
      session.getTransaction().commit();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    } finally {
      FuturesMarket.futuresMarketConnection.closeSession();
    }
  }

  @Step("get data")
  public static List<Map<String, Object>> getIntradayPnL(String ticker, String custodyCd, long timePresent) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select seq_time , account , custody_cd ,total_vm, ");
    queryStringBuilder.append("CASE ");
    queryStringBuilder.append("    WHEN :ticker = 'VN30F1M' THEN vn30f1m_vm_remain ");
    queryStringBuilder.append("    WHEN :ticker = 'VN30F2M' THEN vn30f2m_vm_remain ");
    queryStringBuilder.append("    WHEN :ticker = 'VN30F1Q' THEN vn30f1q_vm_remain ");
    queryStringBuilder.append("    WHEN :ticker = 'VN30F2Q' THEN vn30f2q_vm_remain ");
    queryStringBuilder.append("    ELSE null ");
    queryStringBuilder.append("END  as vm_remain ");
    queryStringBuilder.append("from future_intraday_pnl_1min where seq_time >= FLOOR(:from /86400)*86400 and custody_cd =:custodyCd ");
    try {
      return FuturesMarket.futuresMarketConnection.getSession()
        .createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter(CUSTODY_ID, custodyCd)
        .setParameter(FROM, timePresent)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getDataIntradayPnL(String custodyCd, long from, long to) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select distinct seq_time seq, vn30f1m_vm_remain, vn30f2m_vm_remain, vn30f1q_vm_remain, vn30f2q_vm_remain, total_vm, total_vm_remain ");
    queryStringBuilder.append("from future_intraday_pnl_1min where seq_time >= :from and seq_time <= :to and custody_cd =:custodyCd ");
    try {
      return FuturesMarket.futuresMarketConnection.getSession()
        .createNativeQuery(queryStringBuilder.toString())
        .setParameter(CUSTODY_ID, custodyCd)
        .setParameter(FROM, from)
        .setParameter("to", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static List<HashMap<String, Object>> getDataIntradayPnLLogs(String custodyCd, long from) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select * from future_balance_logs where seq_time >= FLOOR(:from /86400)*86400 and custody_cd =:custodyCd ");
    try {
      return FuturesMarket.futuresMarketConnection.getSession()
        .createNativeQuery(queryStringBuilder.toString())
        .setParameter(CUSTODY_ID, custodyCd)
        .setParameter(FROM, from)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data")
  public static Long getLatestTime(long from) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("select max(seq_time) seq from future_intraday_pnl_1min where seq_time >= FLOOR(:from /86400)*86400 ");
    try {
      List<HashMap<String, Object>> rs = FuturesMarket.futuresMarketConnection.getSession()
        .createNativeQuery(queryStringBuilder.toString())
        .setParameter(FROM, from)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      if (!rs.isEmpty()) {
        return Long.valueOf(String.valueOf(rs.get(0).get("seq")));
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return 0L;
  }
}
