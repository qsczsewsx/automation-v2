package com.tcbs.automation.tca.stockinsight;

import com.tcbs.automation.tca.TcAnalysis;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Builder
public class CashFlowCompanyEntity {

  @Step
  public static List<Map<String, Object>> getCompanyInfo(List<String> tickerList, String fromDate, String toDate, String lang) {
//     StringBuilder queryBuilder = new StringBuilder();
//     queryBuilder.append("SELECT *, FORMAT(UpdateDate, 'MM-yyyy') as month_year FROM ( ");
//     queryBuilder.append("SELECT tb1.Ticker, ExchangeCode,ShortName,IndustryName,F5_7/1000000000 as [MarketCapBn], sr.UpdateDate, Color FROM ( ");
//     queryBuilder.append("SELECT st.Ticker as TICKER, ");
//     queryBuilder.append("(CASE WHEN st.ExchangeID = 0 THEN 'HSX' ");
//     queryBuilder.append("WHEN st.ExchangeID = 1 THEN 'HNX' ");
//     queryBuilder.append("WHEN st.ExchangeID = 3 THEN 'UPC' ");
//     queryBuilder.append("END) AS ExchangeCode, (case when :lang = 'vi' then st.ShortName else st.EnglishName end ) ShortName, i2.Code AS IndustryCode, ");
//     queryBuilder.append("(case when :lang = 'vi' then i2.Name  else i2.Name_En end ) IndustryName, cl.Color ");
//     queryBuilder.append("FROM stox_tb_Company st ");
//     queryBuilder.append("JOIN stox_tb_Industry i4 ON st.IndustryID = i4.ID ");
//     queryBuilder.append("LEFT JOIN stox_tb_Industry i3 ON i4.ParentID = i3.ID ");
//     queryBuilder.append("LEFT JOIN stox_tb_Industry i2 ON i3.ParentID = i2.ID ");
//     queryBuilder.append("LEFT JOIN tbl_idata_industry_level2_color cl on cl.IndustryID = i2.Code ");
//     queryBuilder.append("WHERE st.ExchangeID IN (0,1,3) AND st.Ticker IN ( ");
//     queryBuilder.append("SELECT value FROM STRING_SPLIT(:tickers, ',')))tb1 ");
//     queryBuilder.append("LEFT JOIN stox_tb_ratio sr ");
//     queryBuilder.append("ON tb1.TICKER = sr.Ticker)tb2 ");
//     queryBuilder.append("WHERE tb2.UpdateDate >= CONVERT(smalldatetime, :from_time) ");
//     queryBuilder.append("AND tb2.UpdateDate < CONVERT(smalldatetime, :to_time) ");
//     queryBuilder.append("order by tb2.UpdateDate asc ");

    StringBuilder query = new StringBuilder();
    query.append("SELECT *, FORMAT(UpdateDate, 'MM-yyyy') as month_year FROM ( ");
    query.append(" SELECT tb1.Ticker, ExchangeCode, ShortName, IndustryCode, IndustryName, Color ");
    query.append("   , RTD11/1000000000 as [MarketCapBn] ");
    query.append("   , CAST(sr.TradingDate AS smalldatetime) AS  UpdateDate ");
    query.append(" FROM ( ");
    query.append("     SELECT st.Ticker as TICKER, ");
    query.append("       case when :lang = 'vi' then st.OrganShortName else st.en_OrganName end   AS ShortName, ");
    query.append("       i2.IcbCode AS IndustryCode, ");
    query.append("       case when :lang = 'vi' then i2.IcbName else i2.en_IcbName end  AS IndustryName, ");
    query.append("       i5.Color AS Color, ");
    query.append("        (CASE  ");
    query.append("          WHEN st.comGroupCode = 'VNINDEX' THEN 'HSX' ");
    query.append("     WHEN st.comGroupCode = 'HNXIndex' THEN 'HNX' ");
    query.append("     WHEN st.comGroupCode = 'UpcomIndex' THEN 'UPC' ");
    query.append("       END) AS ExchangeCode ");
    query.append("     FROM stx_cpf_Organization st ");
    query.append("     LEFT JOIN view_idata_industry i4 ON st.IcbCode = i4.IdLevel4  ");
    query.append("     LEFT JOIN stx_mst_IcbIndustry i2  ");
    query.append("       ON i4.IdLevel2 = i2.IcbCode  ");
    query.append("       AND i2.IcbLevel = 2 ");
    query.append("     LEFT JOIN tbl_idata_industry_level2_color i5 ON i4.IdLevel2 = i5.IndustryID  ");
    query.append("     WHERE st.comGroupCode IN ('HNXIndex','UpcomIndex','VNINDEX') ");
    query.append("       AND st.Ticker IN (SELECT value FROM STRING_SPLIT(:tickers, ',')) ");
    query.append("   )tb1 ");
    query.append("   LEFT JOIN stx_rto_RatioTTMDaily  sr ");
    query.append("     ON tb1.TICKER = sr.Ticker ");
    query.append("   WHERE sr.TradingDate >= :from_time ");
    query.append("   AND sr.TradingDate < :to_time ) t ");
    try {
      String tickers = String.join(",", tickerList);
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("tickers", tickers)
        .setParameter("from_time", fromDate)
        .setParameter("to_time", toDate)
        .setParameter("lang", lang)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step
  public static List<HashMap<String, Object>> getTransHistory(List<String> tickerList, String fromDate, String toDate) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" SELECT  Ticker AS code ");
    queryBuilder.append("     , ReferencePrice AS basic_price ");
    queryBuilder.append("     , OpenPriceAdjusted AS OpenPrice_Adjusted ");
    queryBuilder.append("     , ClosePriceAdjusted AS ClosePrice_Adjusted ");
    queryBuilder.append("     , TradingDate AS trading_Date ");
    queryBuilder.append("     , TotalMatchVolume AS total_trading_qtty ");
    queryBuilder.append("     , TotalMatchValue AS Total_Value ");
    queryBuilder.append("     , FORMAT(TradingDate, 'MM-yyyy') as month_year ");
    queryBuilder.append(" FROM Smy_dwh_stox_MarketPrices ");
    queryBuilder.append(" WHERE Ticker IN ( ");
    queryBuilder.append("     SELECT value ");
    queryBuilder.append("             FROM STRING_SPLIT(:tickers, ',') ");
    queryBuilder.append(" )	 ");
    queryBuilder.append(" AND TradingDate >= CONVERT(datetime, :from_time) ");
    queryBuilder.append(" AND TradingDate <= CONVERT(datetime, :to_time) ");
    queryBuilder.append(" ORDER BY TradingDate ASC ");


    try {
      String tickers = String.join(",", tickerList);
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("tickers", tickers)
        .setParameter("from_time", fromDate)
        .setParameter("to_time", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }
}
