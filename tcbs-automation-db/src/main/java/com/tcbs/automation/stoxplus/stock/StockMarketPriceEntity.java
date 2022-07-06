package com.tcbs.automation.stoxplus.stock;

import com.tcbs.automation.staging.AwsStagingDwh;
import com.tcbs.automation.stockmarket.FuturesMarket;
import com.tcbs.automation.stockmarket.InvestorEntity;
import com.tcbs.automation.stockmarket.Stockmarket;
import com.tcbs.automation.stoxplus.Stoxplus;
import com.tcbs.automation.stoxplus.StoxplusV2;
import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockMarketPriceEntity {
  @Step("select data")
  public static List<Map<String, Object>> getPriceAndVolume(String ticker, Integer numDays, String futureName) {
    if (ticker.startsWith("VN30F") || ticker.startsWith("GB10F") || ticker.startsWith("GB05F")) {
      return getPriceAndVolumeDerivative(ticker, numDays, futureName);
    }
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT * FROM ( ");
    queryStringBuilder.append("       SELECT TOP(:numDays) Ticker AS code, TradingDate AS trading_Date, ReferencePrice, ClosePriceAdjusted AS ClosePrice_Adjusted, TotalMatchVolume as Volume ");
    queryStringBuilder.append("       FROM Smy_dwh_stox_MarketPrices ");
    queryStringBuilder.append("       WHERE Ticker = :ticker ");
    queryStringBuilder.append("       ORDER BY TradingDate DESC ) t ");
    queryStringBuilder.append(" ORDER BY trading_Date ASC ");

//    queryStringBuilder.append(" SELECT * FROM ( ");
//    queryStringBuilder.append("       SELECT TOP(:numDays) Ticker AS Ticker, TradingDate as trading_Date, ReferencePrice as basic_price, ");
//    queryStringBuilder.append("       OpenPriceAdjusted as ClosePrice_Adjusted, TotalVolume as Volume ");
//    queryStringBuilder.append("       FROM Smy_dwh_stox_MarketPrices ");
//    queryStringBuilder.append("       WHERE Ticker = :ticker and  TradingDate < FORMAT(getdate(), 'yyyy-MM-dd ')  ");
//    queryStringBuilder.append("       ORDER BY TradingDate DESC ) t ");
//    queryStringBuilder.append(" ORDER BY trading_Date ASC ");

    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("numDays", numDays)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  private static List<Map<String, Object>> getPriceAndVolumeDerivative(String ticker, Integer numDays, String futureName) {
    if (StringUtils.isEmpty(futureName)) {
      return new ArrayList<>();
    }
    StringBuilder queryStringBuilder = new StringBuilder();
//    queryStringBuilder.append(" SELECT t_ticker as Ticker, ");
//    queryStringBuilder.append(" 	from_unixtime(i_seq_time) as trading_Date, ");
//    queryStringBuilder.append(" 	0 as basic_price, ");
//    queryStringBuilder.append(" 	f_close_price as ClosePrice_Adjusted, ");
//    queryStringBuilder.append(" 	i_volume  as Volume ");
//    queryStringBuilder.append(" FROM intraday_by_1D ");
//    queryStringBuilder.append(" WHERE func_idata_future_name_by_ticker_and_time(t_ticker, i_seq_time) = :future_name ");
//    queryStringBuilder.append(" AND i_seq_time < FLOOR(UNIX_TIMESTAMP()/86400)*86400 ");
//    queryStringBuilder.append(" ORDER BY i_seq_time desc  ");
//    queryStringBuilder.append(" LIMIT :numDays ; ");

    queryStringBuilder.append(" SELECT ticker as Ticker, ");
    queryStringBuilder.append(" 	from_unixtime(seq_time) as trading_Date, ");
    queryStringBuilder.append(" 	reference as basic_price, ");
    queryStringBuilder.append(" 	close_price as ClosePrice_Adjusted, ");
    queryStringBuilder.append(" 	acc_vol  as Volume ");
    queryStringBuilder.append(" FROM future_by_1D ");
    queryStringBuilder.append(" WHERE future_name = :future_name ");
    queryStringBuilder.append(" AND seq_time < FLOOR(UNIX_TIMESTAMP()/86400)*86400 ");
    queryStringBuilder.append(" AND reference is not null ");
    queryStringBuilder.append(" ORDER BY seq_time desc  ");
    queryStringBuilder.append(" LIMIT :numDays ; ");

    try {
      return FuturesMarket.futuresMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString()) //TODO doi db
        .setParameter("future_name", futureName)
        .setParameter("numDays", numDays)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("select data")
  public static List<HashMap<String, Object>> getStockCandleBarHistory(String ticker, String from, String to) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT Ticker as ticker, OpenPriceAdjusted as [open], HighestPriceAdjusted as high, LowestPriceAdjusted as low, ");
    queryStringBuilder.append(" ClosePriceAdjusted as [close], TotalMatchVolume as vol , DATEDIFF(s, '1970-01-01 00:00:00', TradingDate) as seq  ");
    queryStringBuilder.append(" FROM Smy_dwh_stox_MarketPrices_AllData ");
    queryStringBuilder.append(" WHERE Ticker = :ticker ");
    queryStringBuilder.append(" AND TradingDate > :fromDate AND TradingDate <= :toDate ");
    queryStringBuilder.append(" ORDER BY TradingDate ASC ");

    try {
      return StoxplusV2.stoxV2DbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("fromDate", from)
        .setParameter("toDate", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("select Max date data")
  public static String getLatestTradingDate() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT CAST(MAX(TradingDate) as Date) as tradingDate ");
    queryStringBuilder.append(" FROM Smy_dwh_stox_MarketPrices ");

    try {
      List<HashMap<String, Object>> result = AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return result.get(0).get("tradingDate").toString();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  @Step("Get bid/ask 1")
  public static List<HashMap<String, Object>> getBidAsk1(String ticker, long startTrading, long timeCurrent, long fromDate, long toDate) {
    //TODO check working day
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT * FROM ( ");
    queryStringBuilder.append(" 	SELECT :ticker_s as ticker,  ");
    queryStringBuilder.append(" 		seq_time as seq_time, ");
    queryStringBuilder.append(" 		DATE_FORMAT( CONVERT_TZ(from_unixtime(seq_time), '+00:00', '+07:00'), '%H:%i') as t, ");
    queryStringBuilder.append(" 		@bidp \\:= IF(bid_price_1 is null, @bidp, bid_price_1) as bp1, ");
    queryStringBuilder.append(" 		@askp \\:= IF(ask_price_1 is null, @askp, ask_price_1) as ap1,  ");
    queryStringBuilder.append(" 		@bidv \\:= IF(bid_vol_1 is null, @bidv, bid_vol_1) as bv1, ");
    queryStringBuilder.append(" 		@askv \\:= IF(ask_vol_1 is null, @askv, ask_vol_1) as av1 ");
    queryStringBuilder.append(" 	FROM  ");
//    queryStringBuilder.append(" 	( ");
//    queryStringBuilder.append(" 		SELECT time_series + :startTrading as temp_seq_time, FROM_UNIXTIME(time_series + :startTrading)  ");
//    queryStringBuilder.append(" 		FROM  timeseries_1min_temp tsr  ");
//    queryStringBuilder.append(" 		WHERE (time_series + :startTrading) < (:timeCurrent -60)  ");
//    queryStringBuilder.append(" 	) tsr  ");
//    queryStringBuilder.append(" 	LEFT JOIN  ");
    queryStringBuilder.append(" 	( ");
    queryStringBuilder.append(" 		SELECT seq_time, bid_price_1, ask_price_1, bid_vol_1, ask_vol_1 ");
    queryStringBuilder.append(" 		FROM bid_ask_by_1min ");
    queryStringBuilder.append(" 		WHERE ticker = :ticker_s AND seq_time >= :startTrading AND seq_time < (:timeCurrent -60)  ");
    queryStringBuilder.append(" 	) t1  ");
//    queryStringBuilder.append(" 	ON tsr.temp_seq_time = t1.seq_time  ");
    queryStringBuilder.append(" 	cross join (select @bidp\\:=0, @askp\\:=0, @bidv\\:=0, @askv\\:=0) params ");
    queryStringBuilder.append(" )tlb ");
    queryStringBuilder.append(" WHERE seq_time >= :fromDate AND seq_time <= :toDate ");
    queryStringBuilder.append(" ORDER BY seq_time ASC; ");

    try {
      return InvestorEntity.getHibernateEdition(ticker).getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker_s", ticker)
        .setParameter("startTrading", startTrading)
        .setParameter("timeCurrent", timeCurrent)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get net trading stock foreign")
  public static List<HashMap<String, Object>> getNetVolumeTradingStockForeign(String ticker, String from, String to) {

    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" select " +
      " CONVERT(VARCHAR(10),TradingDate ,23) as trading_date," +
      " CAST(ForeignBuyVolumeTotal - ForeignSellVolumeTotal as decimal) as acc_trade " +
      " from Smy_dwh_stox_MarketPrices_AllData " +
      " where Ticker = :p_ticker " +
      " AND TradingDate BETWEEN :p_from_date AND :p_to_date " +
      " ORDER BY TradingDate asc");
    try {
      return StoxplusV2.stoxV2DbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("p_ticker", ticker)
        .setParameter("p_from_date", from)
        .setParameter("p_to_date", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get net trading future foreign")
  public static List<HashMap<String, Object>> getNetVolumeTradingFutureForeign(String ticker, String from, String to) {

    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT trading_date,CAST(acc_trade as decimal) as  acc_trade FROM foreigntrade_by_1d WHERE ticker = :p_ticker AND trading_date between :p_from_date AND :p_to_date ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("p_ticker", ticker)
        .setParameter("p_from_date", from)
        .setParameter("p_to_date", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
