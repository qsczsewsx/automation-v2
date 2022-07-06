package com.tcbs.automation.stoxplus.stock;

import com.tcbs.automation.stoxplus.Stoxplus;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Builder
public class CashFlowCompanyEntity {
  private static final Logger logger = LoggerFactory.getLogger(CashFlowCompanyEntity.class);

  @Step
  public static List<Map<String, Object>> getCompanyInfo(List<String> tickerList, String fromDate, String toDate, String lang) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT *, FORMAT(UpdateDate, 'MM-yyyy') as month_year FROM ( ");
    queryBuilder.append("SELECT tb1.Ticker, ExchangeCode,ShortName,IndustryName,F5_7/1000000000 as [MarketCapBn], sr.UpdateDate, Color FROM ( ");
    queryBuilder.append("SELECT st.Ticker as TICKER, ");
    queryBuilder.append("(CASE WHEN st.ExchangeID = 0 THEN 'HSX' ");
    queryBuilder.append("WHEN st.ExchangeID = 1 THEN 'HNX' ");
    queryBuilder.append("WHEN st.ExchangeID = 3 THEN 'UPC' ");
    queryBuilder.append("END) AS ExchangeCode, (case when :lang = 'vi' then st.ShortName else st.EnglishName end ) ShortName, i2.Code AS IndustryCode, ");
    queryBuilder.append("(case when :lang = 'vi' then i2.Name  else i2.Name_En end ) IndustryName, cl.Color ");
    queryBuilder.append("FROM stox_tb_Company st ");
    queryBuilder.append("JOIN stox_tb_Industry i4 ON st.IndustryID = i4.ID ");
    queryBuilder.append("LEFT JOIN stox_tb_Industry i3 ON i4.ParentID = i3.ID ");
    queryBuilder.append("LEFT JOIN stox_tb_Industry i2 ON i3.ParentID = i2.ID ");
    queryBuilder.append("LEFT JOIN tbl_idata_industry_level2_color cl on cl.IndustryID = i2.Code ");
    queryBuilder.append("WHERE st.ExchangeID IN (0,1,3) AND st.Ticker IN ( ");
    queryBuilder.append("SELECT value FROM STRING_SPLIT(:tickers, ',')))tb1 ");
    queryBuilder.append("LEFT JOIN stox_tb_ratio sr ");
    queryBuilder.append("ON tb1.TICKER = sr.Ticker)tb2 ");
    queryBuilder.append("WHERE tb2.UpdateDate >= CONVERT(smalldatetime, :from_time) ");
    queryBuilder.append("AND tb2.UpdateDate < CONVERT(smalldatetime, :to_time) ");
    queryBuilder.append("order by tb2.UpdateDate asc ");

    try {
      String tickers = String.join(",", tickerList);
      return Stoxplus.stoxDbConnection.getSession().createNativeQuery(queryBuilder.toString()) //TODO change v2
        .setParameter("tickers", tickers)
        .setParameter("from_time", fromDate)
        .setParameter("to_time", toDate)
        .setParameter("lang", lang)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList();
  }

  @Step
  public static List<HashMap<String, Object>> getTransHistory(List<String> tickerList, String fromDate, String toDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT code, basic_price, ClosePrice_Adjusted, trading_Date, total_trading_qtty, Total_Value, FORMAT(trading_Date, 'MM-yyyy') as month_year ");
    queryBuilder.append("FROM Smy_dwh_stox_MarketPrices ");
    queryBuilder.append("WHERE code IN (SELECT value FROM STRING_SPLIT(:tickers, ',')) ");
    queryBuilder.append("AND trading_Date >= CONVERT(datetime, :from_time) ");
    queryBuilder.append("AND trading_Date <= CONVERT(datetime, :to_time) ");
    queryBuilder.append("ORDER BY trading_Date ASC;");

    try {
      String tickers = String.join(",", tickerList);
      return Stoxplus.stoxDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("tickers", tickers)
        .setParameter("from_time", fromDate)
        .setParameter("to_time", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList();
  }
}
