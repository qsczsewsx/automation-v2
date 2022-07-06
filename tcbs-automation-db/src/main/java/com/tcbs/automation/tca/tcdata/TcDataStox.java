package com.tcbs.automation.tca.tcdata;

import com.tcbs.automation.tca.TcAnalysis;
import lombok.Builder;
import lombok.Data;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class TcDataStox {
  @Step
  public static List<Map<String, Object>> getFieldsMap(String table) {
    StringBuilder sql = new StringBuilder(" SELECT fm.field_name, fm.stox_name "
      + " FROM stox_field_map fm "
      + " INNER JOIN stox_menu mn "
      + " ON mn.id = fm.table_id  "
      + " WHERE mn.table_name = (:table)");

    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(sql.toString())
        .setParameter("table", table).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step
  public static List<Map<String, Object>> getTickerInfo(String ticker) {
    StringBuilder sql = new StringBuilder(" SELECT C.Ticker " 
      + ", CASE  "
      + "             WHEN c.comGroupCode = 'HNXIndex' THEN CAST(1 AS SMALLINT) "
      + "             WHEN c.comGroupCode = 'UpcomIndex' THEN CAST(3  AS SMALLINT) "
      + "             WHEN c.comGroupCode = 'VNINDEX' THEN CAST(0  AS SMALLINT) "
      + "         END AS ExchangeID "
      + ", CASE  "
      + "             WHEN C.COMTYPECODE = 'CT' THEN 2 "
      + "      WHEN C.COMTYPECODE = 'BH' THEN 3 "
      + "      WHEN C.COMTYPECODE = 'CK' THEN 4 "
      + "      WHEN C.COMTYPECODE = 'NH' THEN 0 "
      + "         END AS TypeID " 
      + " FROM stx_cpf_Organization C  "
      + " WHERE C.ticker = (:ticker)");

    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(sql.toString())
        .setParameter("ticker", ticker).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step
  public static List<HashMap<String, Object>> getIndustry(List<String> tickers) {
    String sql = " SELECT comp.ticker, i.* "
      + " FROM stx_cpf_Organization comp  "
      + " INNER JOIN ( SELECT DISTINCT IndLevel4.IcbCode AS industry_id  "
      + " , IndLevel4.IcbName AS industry_name "
      + " , IndLevel4.IcbNamePath AS industry_desc "
      + " , IndLevel1.IcbCode AS industry_id_1 "
      + " , IndLevel1.IcbName AS industry_name_1 "
      + " , IndLevel1.IcbNamePath AS industry_desc_1 "
      + " , IndLevel2.IcbCode AS industry_id_2 "
      + " , IndLevel2.IcbName AS industry_name_2 "
      + " , IndLevel2.IcbNamePath AS industry_desc_2 "
      + " , IndLevel3.IcbCode AS industry_id_3 "
      + " , IndLevel3.IcbName AS industry_name_3 "
      + " , IndLevel3.IcbNamePath AS industry_desc_3 "
      + " FROM  dbo.stx_mst_IcbIndustry AS IndLevel1 "
      + " INNER JOIN dbo.stx_mst_IcbIndustry AS IndLevel2  "
      + " ON IndLevel1.IcbCode = IndLevel2.ParentIcbCode  "
      + " INNER JOIN dbo.stx_mst_IcbIndustry AS IndLevel3  "
      + " ON IndLevel2.IcbCode = IndLevel3.ParentIcbCode  "
      + " INNER JOIN dbo.stx_mst_IcbIndustry AS IndLevel4  "
      + " ON IndLevel3.IcbCode = IndLevel4.ParentIcbCode) i "
      + " ON comp.IcbCode = i.industry_id "
      + " WHERE comp.ticker IN (:tickers) ";
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(sql)
        .setParameter("tickers", tickers).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step
  public static List<HashMap<String, Object>> getFundamentalStatement(String sql, String ticker, String industry
    , int from, int to) {
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(sql)
        .setParameter("ticker", ticker)
        .setParameter("from", from)
        .setParameter("to", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step
  public static List<HashMap<String, Object>> getStockPrice(String sql, String ticker, String exchange
    , String from, String to) {
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(sql)
        .setParameter("exchange", exchange)
        .setParameter("ticker", ticker)
        .setParameter("from", from)
        .setParameter("to", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step
  public static List<HashMap<String, Object>> getMarketInfo(String sql, String from, String to) {
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(sql)
        .setParameter("from", from)
        .setParameter("to", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step
  public static List<HashMap<String, Object>> getCompanyInfo(String sql, List<String> tickers) {
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(sql)
        .setParameter("tickers", tickers)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
