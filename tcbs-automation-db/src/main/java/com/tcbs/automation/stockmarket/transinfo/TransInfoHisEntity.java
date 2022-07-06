package com.tcbs.automation.stockmarket.transinfo;

import com.tcbs.automation.stockmarket.Stockmarket;
import com.tcbs.automation.stoxplus.Stoxplus;
import com.tcbs.automation.tca.TcAnalysis;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransInfoHisEntity {

  @Step("get intraday price")
  public static List<HashMap<String, Object>> getIntradayPrice(String ticker, String fromTime, String toTime) {
    StringBuilder query = new StringBuilder();
    query.append("SELECT DATE_FORMAT(FROM_UNIXTIME(t1.seq_time), '%Y-%m-%d') as trading_date, t1.ticker , t1.sma20 , ");
    query.append("vsma5, t1.rsi , t3.bsr ");
    query.append("FROM ( select FROM_UNIXTIME(seq_time), seq_time, ticker , sma20 , vsma5 , rsi ");
    query.append("from indicators_by_1D where ticker = :t_ticker  group by seq_time order by updated desc ) t1 ");
    query.append("left join ( ");
    query.append("SELECT ticker, seq_time as seq_day, bs_acc_ratio as bsr ");
    query.append("from buysellactive_by_1D icbm  ");
    query.append("WHERE ticker = :t_ticker and seq_time <= UNIX_TIMESTAMP(:to_time)  ");
    query.append("order by seq_time desc ");
    query.append(")t3 on t1.ticker = t3.ticker AND t1.seq_time = t3.seq_day ");
//    query.append("cross join ( ");
//    query.append("select @vsma5 \\:= (select vsma5 from indicators_by_1D  ");
//    query.append("WHERE ticker = :t_ticker and seq_time < UNIX_TIMESTAMP(:from_time) ");
//    query.append("order by seq_time desc limit 1) ");
//    query.append(") params ");
    query.append("WHERE t1.ticker = :t_ticker   ");
    query.append("and t1.seq_time BETWEEN ( UNIX_TIMESTAMP(:from_time) - 30*86400 ) AND UNIX_TIMESTAMP(:to_time) ");
    query.append(" UNION   ");
    query.append("SELECT DATE_FORMAT(FROM_UNIXTIME(t1.seq_time), '%Y-%m-%d') as trading_date, t1.ticker , t1.sma20 , ");
    query.append("vsma5, t1.rsi , t3.bsr ");
    query.append("FROM ( select FROM_UNIXTIME(seq_time), seq_time, ticker , sma20 , vsma5 , rsi ");
    query.append("from indicators_by_1min where ticker = :t_ticker  group by seq_time order by updated desc ) t1 ");
    query.append("left join ( ");
    query.append("SELECT ticker, seq_time as seq_day, bs_acc_ratio as bsr ");
    query.append("from buysellactive_acc_by_1min icbm  ");
    query.append("WHERE ticker = :t_ticker and seq_time <= UNIX_TIMESTAMP(:to_time)  ");
    query.append("order by seq_time desc ");
    query.append(")t3 on t1.ticker = t3.ticker AND t1.seq_time = t3.seq_day ");
    query.append("WHERE t1.ticker = :t_ticker   ");
    query.append("AND t1.seq_time = (SELECT MAX(seq_time) FROM indicators_by_1min)  ");
    query.append("and t1.seq_time BETWEEN ( UNIX_TIMESTAMP(:from_time) - 30*86400 ) AND UNIX_TIMESTAMP(:to_time) ");
    query.append("order by trading_date asc;  ");

    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(query.toString())
        .setParameter("t_ticker", ticker)
        .setParameter("from_time", fromTime)
        .setParameter("to_time", toTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception e) {
      StepEventBus.getEventBus().testFailed(new AssertionError(e.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get foreigner trading")
  public static List<HashMap<String, Object>> getForeignerTrading(String ticker, String fromTime, String toTime) {
    StringBuilder query = new StringBuilder();

    // query.append("; WITH foreign_trade AS ( ");
    // query.append("		SELECT trading_date ");
    // query.append("			, code AS t_ticker ");
    // query.append("			, buy_foreign_qtty-sell_foreign_qtty as foreigner_qtty ");
    // query.append("			, case when isnull(basic_price ,0) = 0 then open_price else basic_price end as basic_price ");
    // query.append("		 , ceiling_price ");
    // query.append("		 , floor_price ");
    // query.append("	  , open_price ");
    // query.append("   , OpenPrice_Adjusted as f_open_price ");
    // query.append("	  , case when isnull(close_price,0) = 0 then  ");
    // query.append("		      case when isnull(basic_price ,0) = 0 then open_price else basic_price end  ");
    // query.append("			 else close_price end as close_price ");
    // query.append("		 , ClosePrice_Adjusted as f_close_price ");
    // query.append("	  , highest_price ");
    // query.append("		 , Highest_Adjusted as f_high_price ");
    // query.append("		 , lowest_price ");
    // query.append("		 , Lowest_Adjusted as f_low_price ");
    // query.append("			, nm_total_traded_qtty as i_volume ");
    // query.append("			, 'HNX' AS bourse ");
    // query.append("		FROM stox_tb_StocksInfo  ");
    // query.append("		WHERE code = :stock_code ");
    // query.append("			AND trading_date >= dateadd(MONTH, -7, dbo.businessDaysAdd(-1, CAST(CURRENT_TIMESTAMP AS DATE)))  and trading_date < CAST(CURRENT_TIMESTAMP AS DATE) ");
    // query.append("		UNION ");
    // query.append("		SELECT trading_date, code as t_ticker ");
    // query.append("			, buy_foreign_qtty-sell_foreign_qtty  as foreigner_qtty ");
    // query.append("			, case when isnull(basic_price ,0) = 0 then open_price else basic_price end as basic_price ");
    // query.append("		 , ceiling_price ");
    // query.append("		 , floor_price ");
    // query.append("		 , open_price ");
    // query.append("		 , OpenPrice_Adjusted as f_open_price ");
    // query.append("		 , case when isnull(average_price,0) = 0 then  ");
    // query.append("			     case when isnull(basic_price,0) = 0 then round(average_price,-2) else basic_price end  ");
    // query.append("			 else round(average_price,-2) end as close_price ");
    // query.append("		 , round(ClosePrice_Adjusted, -2) as f_close_price ");
    // query.append("	  , highest_price ");
    // query.append("		 , Highest_Adjusted as f_high_price ");
    // query.append("		 , lowest_price ");
    // query.append("	  , Lowest_Adjusted as f_low_price ");
    // query.append("			, nm_total_traded_qtty as i_volume ");
    // query.append("			, 'UPCOM' AS bourse ");
    // query.append("		FROM stox_tb_UpCom_StocksInfo   ");
    // query.append("		WHERE code = :stock_code ");
    // query.append("			AND trading_date >= dateadd(MONTH, -7, dbo.businessDaysAdd(-1, CAST(CURRENT_TIMESTAMP AS DATE)))  and trading_date < CAST(CURRENT_TIMESTAMP AS DATE) ");
    // query.append("		UNION  ");
    // query.append("		SELECT trading_date as TradingDate ");
    // query.append("			, stock_code as t_ticker   ");
    // query.append("			, buy_foreign_qtty-sell_foreign_qtty   as foreigner_qtty ");
    // query.append("			, round((Ceiling+Floor)*10/2,-2)  ");
    // query.append("			, ceiling ");
    // query.append("		 , floor ");
    // query.append("		 , openprice ");
    // query.append("		 , OpenPrice_Adjusted*10 as f_open_price ");
    // query.append("		 , case when isnull([Last],0)=0 then round((Ceiling+Floor)*10/2,-2) else [Last]*10 END ");
    // query.append("		 , ClosePrice_Adjusted*10 as f_close_price ");
    // query.append("	  , Highest ");
    // query.append("		 , Highest_Adjusted*10  as f_high_price ");
    // query.append("	  , Lowest ");
    // query.append("		 , Lowest_Adjusted*10  as f_low_price ");
    // query.append("		 , Totalshare*10  as i_volume ");
    // query.append("		 , 'HOSE' AS bourse ");
    // query.append("		FROM stox_tb_HOSE_ForeignerTrading F   ");
    // query.append("		INNER JOIN stox_tb_HOSE_Trading D ");
    // query.append("			ON F.stock_code = D.StockSymbol ");
    // query.append("			AND F.trading_date = D.DateReport ");
    // query.append("		WHERE stock_code = :stock_code ");
    // query.append("			AND  trading_date >= dateadd(MONTH, -7, dbo.businessDaysAdd(-1, CAST(CURRENT_TIMESTAMP AS DATE))) and trading_date < CAST(CURRENT_TIMESTAMP AS DATE) ");
    // query.append("	) ");
    // query.append("	SELECT s.* ");
    // query.append("		, R.TC_RS as rs ");
    // query.append(" 	FROM foreign_trade S  	  ");
    // query.append("	LEFT JOIN Stock_RSRating_Refining R ");
    // query.append("		ON S.t_ticker = R.Ticker  ");
    // query.append("		AND S.trading_date = R.DATEREPORT  ");
    // query.append("	ORDER BY S.trading_date DESC; ");

    query.append("	WITH foreign_trade AS ( ");
    query.append("SELECT TradingDate  AS trading_date ");
    query.append("	  , ticker AS t_ticker");
    query.append("	  , cast(ForeignBuyVolumeMatched - ForeignSellVolumeMatched as float(24)) AS foreigner_qtty");
    query.append("	  , cast(ReferencePrice as float(24)) AS basic_price");
    query.append("	  , cast(CeilingPrice as float(24)) AS ceiling_price");
    query.append("	  , cast(FloorPrice as float(24)) AS floor_price");
    query.append("	  , cast(OpenPrice as float(24)) AS open_price");
    query.append("	  , cast(OpenPriceAdjusted as float(24)) AS f_open_price");
    query.append("	  , cast(ClosePrice as float(24)) AS close_price");
    query.append("	  , cast(ClosePriceAdjusted as float(24)) AS f_close_price");
    query.append("	  , cast(HighestPrice as float(24)) AS highest_price");
    query.append("	  , cast(HighestPriceAdjusted as float(24)) AS f_high_price");
    query.append("	  , cast(LowestPrice as float(24)) AS lowest_price");
    query.append("	  , cast(LowestPriceAdjusted as float(24)) AS  f_low_price");
    query.append("	  , cast(TotalMatchVolume as float(24)) AS  i_volume");
    query.append("	  FROM stx_mrk_HnxStock  ");
    query.append("	  WHERE Ticker = :stock_code  ");
    query.append("	    AND tradingdate >= dateadd(MONTH, -7, dbo.businessDaysAdd(-1, CAST(CURRENT_TIMESTAMP AS DATE))) ");
    query.append(" UNION ");
    query.append(" SELECT TradingDate  AS trading_date ");
    query.append("	    , ticker AS t_ticker ");
    query.append("	    , cast(ForeignBuyVolumeMatched - ForeignSellVolumeMatched as float(24)) AS foreigner_qtty ");
    query.append("	    , cast(ReferencePrice as float(24)) AS basic_price ");
    query.append("	    , cast(CeilingPrice as float(24)) AS ceiling_price ");
    query.append("	    , cast(FloorPrice as float(24)) AS floor_price ");
    query.append("	    , cast(OpenPrice as float(24)) AS open_price ");
    query.append("	    , cast(OpenPriceAdjusted as float(24)) AS f_open_price ");
    query.append("	    , cast(ClosePrice as float(24)) AS close_price ");
    query.append("	    , cast(ClosePriceAdjusted as float(24)) AS f_close_price ");
    query.append("	    , cast(HighestPrice as float(24)) AS highest_price ");
    query.append("	    , cast(HighestPriceAdjusted as float(24)) AS f_high_price ");
    query.append("	    , cast(LowestPrice as float(24)) AS lowest_price ");
    query.append("	    , cast(LowestPriceAdjusted as float(24)) AS  f_low_price ");
    query.append("	    , cast(TotalMatchVolume as float(24)) AS  i_volume ");
    query.append("	  FROM stx_mrk_UpcomStock ");
    query.append("	  WHERE Ticker = :stock_code AND tradingdate >= dateadd(MONTH, -7, dbo.businessDaysAdd(-1, CAST(CURRENT_TIMESTAMP AS DATE)))  ");
    query.append("	  UNION ");
    query.append("	  SELECT TradingDate  AS trading_date ");
    query.append("	    , ticker AS t_ticker");
    query.append("	    , cast(ForeignBuyVolumeMatched - ForeignSellVolumeMatched as float(24)) AS  foreigner_qtty");
    query.append("	    , cast(ReferencePrice as float(24)) AS basic_price");
    query.append("	    , cast(CeilingPrice as float(24)) AS ceiling_price");
    query.append("	    , cast(FloorPrice as float(24)) AS floor_price");
    query.append("	    , cast(OpenPrice as float(24)) AS open_price");
    query.append("	    , cast(OpenPriceAdjusted as float(24)) AS f_open_price");
    query.append("	    , cast(ClosePrice as float(24)) AS close_price");
    query.append("	    , cast(ClosePriceAdjusted as float(24)) AS f_close_price");
    query.append("	    , cast(HighestPrice as float(24)) AS highest_price");
    query.append("	    , cast(HighestPriceAdjusted as float(24)) AS f_high_price");
    query.append("	    , cast(LowestPrice as float(24)) AS lowest_price");
    query.append("	    , cast(LowestPriceAdjusted as float(24)) AS  f_low_price");
    query.append("	    , cast(TotalMatchVolume as float(24)) AS  i_volume");
    query.append("	  FROM stx_mrk_HoseStock  ");
    query.append("	  WHERE Ticker = :stock_code  ");
    query.append("	    AND tradingdate >= dateadd(MONTH, -7, dbo.businessDaysAdd(-1, CAST(CURRENT_TIMESTAMP AS DATE))) ) ");
    query.append("	SELECT s.*, R.TC_RS ");
    query.append("	 FROM foreign_trade S  	  ");
    query.append("	LEFT JOIN Stock_RSRating_Refining R ");
    query.append("	  ON S.t_ticker = R.Ticker  ");
    query.append("	  AND S.Trading_Date = R.DATEREPORT  ");
    query.append("	ORDER BY S.trading_date DESC; ");

    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(query.toString()) //TODO change v2
        .setParameter("stock_code", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception e) {
      StepEventBus.getEventBus().testFailed(new AssertionError(e.getMessage()));
    }
    return new ArrayList<>();
  }
}
