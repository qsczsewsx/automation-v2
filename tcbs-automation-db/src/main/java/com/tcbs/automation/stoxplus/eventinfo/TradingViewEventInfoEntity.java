package com.tcbs.automation.stoxplus.eventinfo;

import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TradingViewEventInfoEntity {

  @Step(" get data ")
  public static List<HashMap<String, Object>> getEventInfo(String ticker, String from, String to){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT * FROM dbo.tbl_idata_event_info_tradingview Where ticker = :ticker AND EVENT_DATE between :from and :to ");
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("from", from)
        .setParameter("to", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data from trading view table")
  public static List<HashMap<String, Object>> tradingViewInfo() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT * FROM dbo.tbl_idata_event_info_tradingview");
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Truncate table")
  public static void truncateTable(){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("TRUNCATE TABLE tbl_idata_event_info_tradingview");
    executeQuery(queryBuilder);
  }

  @Step(" Call proc ")
  public static void callProc(String proc){
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("EXEC " + proc);
    executeQuery(queryBuilder);
  }

  @Step("execute query")
  public static void executeQuery(StringBuilder sql) {
    Session session = TcAnalysis.tcaDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createNativeQuery(sql.toString());
    try {
      query.executeUpdate();
      session.getTransaction().commit();
      TcAnalysis.tcaDbConnection.closeSession();
    } catch (Exception e) {
      TcAnalysis.tcaDbConnection.closeSession();
      throw e;
    }
  }

  @Step("Get cash divedned payout info")
  public static List<HashMap<String, Object>> cashDividendPayoutInfo() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT cp.*, stage.StageName, stage.en_StageName FROM stx_mst_Stage stage ");
    queryBuilder.append(" INNER JOIN ( SELECT * FROM stx_cpa_CashDividendPayout WHERE ExrightDate IS NOT NULL ");
    queryBuilder.append(" AND ExrightDate > '1990-01-01' AND ValuePershare is not null and Status = 1) cp ON stage.StageCode = cp.DividendStageCode ");
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get share issue info")
  public static List<HashMap<String, Object>> sharedIssuedInfo() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM stx_cpa_ShareIssue ");
    queryBuilder.append(" WHERE STATUS = 1 AND UPPER(IssueMethodCode)= 'DIV' AND IssueStatusCode = 'D' AND ExrightDate IS NOT NULL ");
    queryBuilder.append(" AND YEAR(ExrightDate) > 1990 and Dividend_Year is not null and ExerciseRatio is not null ");
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step(" Get stock event ")
  public static List<HashMap<String, Object>> getStockEvent()
  {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM stx_cpa_ShareIssue ");
    queryBuilder.append(" WHERE STATUS = 1 AND IssueStatusCode = 'D' AND ExrightDate IS NOT NULL AND ExerciseRatio IS NOT NULL AND ExerciseRatio >0 ");
    queryBuilder.append(" AND YEAR(ExrightDate) > 1990 AND UPPER(IssueMethodCode) IN ('BONUS','RIGHTS','PP','ICRE','TRANS','EMPL','PUBL','BBOD','MERGER')");
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step(" Finance statement ")
  public static List<HashMap<String, Object>> financeStatement() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT * FROM stx_cpa_Event ");
    queryBuilder.append(" WHERE EventListCode IN ('KQCT','KQQY') AND STATUS = 1 AND PublicDate IS NOT NULL AND YEAR(PublicDate) > 1990 ");
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step(" cpf organization ")
  public static List<HashMap<String, Object>> organization() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT OrganCode, Ticker FROM stx_cpf_Organization WHERE Ticker is not null and len(Ticker) =  3 ");
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
