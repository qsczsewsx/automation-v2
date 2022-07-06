package com.tcbs.automation.stockmarket;

import com.tcbs.automation.stockmarket.models.FixedLengthQueue;
import com.tcbs.automation.stoxplus.Stoxplus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Indicator {

  public static final int RSI_PERIOD = 14;
  public static final int MACD_PERIOD = 26;
  public static final int ADX_PERIOD = 28;
  public static final int MACD_HIST_PERIOD = 9;
  public static final int CCI_PERIOD = 20;
  public static final int EMA_SMOOTH = 2;
  public static final double MAX_ACCELERATE = 0.2;
  public static final double INCRE_ACCELERATE = 0.02;
  public static final String SELL = "Sell";
  public static final String BUY = "Buy";
  public static final String NEUTRAL = "Neutral";


  private String symbol;


  private Double close;

  private Double high;

  private Double low;


  private Double avgGain;


  private Double avgLoss;


  private Double rsi;


  private String rsiSignal;


  private Double ema12;


  private Double ema26;


  private Double macd;


  private String macdSignal;


  private Double macdEma;


  private Double macdHist;


  private String macdHistSignal;


  private Double stochK;


  private String stochKSignal;


  private Double stochRsiFastK;


  private String stochRsiFastKSignal;


  private Double wpr;


  private String wprSignal;


  private Double typical;


  private Double maTypical;
  private Double meanDeviation;


  private Double cci;


  private String cciSignal;


  private Double roc;


  private String rocSignal;


  private Double ultosc;


  private String ultoscSignal;


  private Double bbw;


  private String bbwSignal;


  private Double adx;

  private Double preAdx = 0d;

  private String adxSignal;


  private Double dx = 0d;


  private Double plusDi;


  private Double minusDi;


  private double str = 0d;

  private double plusDm = 0d;
  private double minusDm = 0d;

  private boolean isRaising = true;
  private Double af;
  private Double ep;

  private Double nextSar = 0d;

  private Double sar;


  private String sarSignal;

  private double buyPressure = 0d;

  private double trueRange = 0d;

  private double bp7 = 0d;

  private double tr7 = 0d;


  private double bp14 = 0d;

  private double tr14 = 0d;


  private double bp28 = 0d;

  private double tr28 = 0d;


  private Double sma5;

  private String sma5Signal;


  private Double sma10;

  private String sma10Signal;


  private Double sma20;

  private String sma20Signal;


  private Double sma50;

  private String sma50Signal;


  private Double sma100;

  private String sma100Signal;


  private Double sma200;

  private String sma200Signal;


  private Double ema5;

  private String ema5Signal;


  private Double ema10;

  private String ema10Signal;


  private Double ema20;

  private String ema20Signal;


  private Double ema50;

  private String ema50Signal;


  private Double ema100;

  private String ema100Signal;


  private Double ema200;

  private String ema200Signal;

  private Queue<Double> closeQueue = new FixedLengthQueue<>(Indicator.RSI_PERIOD);
  private Queue<Double> highQueue = new FixedLengthQueue<>(Indicator.RSI_PERIOD);
  private Queue<Double> lowQueue = new FixedLengthQueue<>(Indicator.RSI_PERIOD);
  private Queue<Double> rsiQueue = new FixedLengthQueue<>(Indicator.RSI_PERIOD);
  private Queue<Double> typicalQueue = new FixedLengthQueue<>(Indicator.CCI_PERIOD);
  private Queue<Double> maTypicalQueue = new FixedLengthQueue<>(Indicator.CCI_PERIOD);
  private Queue<Double> close5 = new FixedLengthQueue<>(5);
  private Queue<Double> close9 = new FixedLengthQueue<>(9);
  private Queue<Double> close10 = new FixedLengthQueue<>(10);
  private Queue<Double> close12 = new FixedLengthQueue<>(12);
  private Queue<Double> close26 = new FixedLengthQueue<>(26);
  private Queue<Double> close20 = new FixedLengthQueue<>(20);
  private Queue<Double> close50 = new FixedLengthQueue<>(50);
  private Queue<Double> close100 = new FixedLengthQueue<>(100);
  private Queue<Double> close200 = new FixedLengthQueue<>(200);
  private Queue<Double> bpQueue7 = new FixedLengthQueue<>(7);
  private Queue<Double> trQueue7 = new FixedLengthQueue<>(7);
  private Queue<Double> bpQueue14 = new FixedLengthQueue<>(14);
  private Queue<Double> trQueue14 = new FixedLengthQueue<>(14);
  private Queue<Double> bpQueue28 = new FixedLengthQueue<>(28);
  private Queue<Double> trQueue28 = new FixedLengthQueue<>(28);
  private Queue<Double> macdQueue9 = new FixedLengthQueue<>(9);


  @Step("get one intraday indicator from db")
  public static List<HashMap<String, Object>> getLastIntradayIndicator(String ticker) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT  seq_time ");
    queryStringBuilder.append(" , `close`  ");
    queryStringBuilder.append(" , high   ");
    queryStringBuilder.append(" , low  ");
    queryStringBuilder.append(" , rsi  ");
    queryStringBuilder.append(" , macd  ");
    queryStringBuilder.append(" ,   avg_gain as avgGain ");
    queryStringBuilder.append(" ,   avg_loss as avgLoss ");
    queryStringBuilder.append(" ,   stochk as stochK  ");
    queryStringBuilder.append(" ,   stochrsi_fastk as stochRsiFastK  ");
    queryStringBuilder.append(" ,   ema26    ");
    queryStringBuilder.append(" ,   ema12    ");
    queryStringBuilder.append(" ,   macdema as macdEma  ");
    queryStringBuilder.append(" ,   macdhist  as macdHist  ");
    queryStringBuilder.append(" ,   adx as preAdx   ");
    queryStringBuilder.append(" ,   adx as adx   ");
    queryStringBuilder.append(" ,   plus_di  as plusDi ");
    queryStringBuilder.append(" ,   minus_di as minusDi ");
    queryStringBuilder.append(" ,   plus_dm  as plusDm ");
    queryStringBuilder.append(" ,   minus_dm  as minusDm ");
    queryStringBuilder.append(" ,   dx    ");
    queryStringBuilder.append(" , 	atr as str  ");
    queryStringBuilder.append(" ,   wpr    ");
    queryStringBuilder.append(" ,   cci    ");
    queryStringBuilder.append(" ,   roc    ");
    queryStringBuilder.append(" ,   sar    ");
    queryStringBuilder.append(" ,   next_sar as nextSar    ");
    queryStringBuilder.append(" ,   ultosc    ");
    queryStringBuilder.append(" ,   bbw    ");
    queryStringBuilder.append(" ,   sma5    ");
    queryStringBuilder.append(" ,   sma10    ");
    queryStringBuilder.append(" ,   sma20    ");
    queryStringBuilder.append(" ,   sma50    ");
    queryStringBuilder.append(" ,   sma100    ");
    queryStringBuilder.append(" ,   sma200    ");
    queryStringBuilder.append(" ,   ema5    ");
    queryStringBuilder.append(" ,   ema10    ");
    queryStringBuilder.append(" ,   ema20    ");
    queryStringBuilder.append(" ,   ema50    ");
    queryStringBuilder.append(" ,   ema100    ");
    queryStringBuilder.append(" ,   ema200    ");
    queryStringBuilder.append(" FROM indicators_tc_price ");
    queryStringBuilder.append(" WHERE ticker = :t_ticker ");
    queryStringBuilder.append(" order by seq_time desc, updated desc ");
    queryStringBuilder.append(" limit 1 ; ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("t_ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


  @Step("get queue intraday indicator from db")
  public static List<HashMap<String, Object>> getIntradayQueue(String ticker) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT * from (");
    queryStringBuilder.append(" SELECT seq_time ");
    queryStringBuilder.append(" 	,	high    ");
    queryStringBuilder.append(" 	,   low    ");
    queryStringBuilder.append(" 	,   `close`   ");
    queryStringBuilder.append(" 	,   rsi    ");
    queryStringBuilder.append(" 	,   macd    ");
    queryStringBuilder.append(" 	,   typical    ");
    queryStringBuilder.append(" 	,   ma_typical as maTypical   ");
    queryStringBuilder.append(" 	,   buy_pressure_7 as  bp7   ");
    queryStringBuilder.append(" 	,   buy_pressure_14 as  bp14   ");
    queryStringBuilder.append(" 	,   buy_pressure_28 as  bp28   ");
    queryStringBuilder.append(" 	,   true_range_7 as  tr7   ");
    queryStringBuilder.append(" 	,   true_range_14 as  tr14   ");
    queryStringBuilder.append(" 	,   true_range_28 as  tr28   ");
    queryStringBuilder.append(" 	,   updated   ");
    queryStringBuilder.append(" FROM indicators_tc_price  ");
    queryStringBuilder.append(" where ticker = :ticker  ");
    queryStringBuilder.append(" order by seq_time  desc ");
    queryStringBuilder.append(" limit 200 ) x ");
    queryStringBuilder.append(" order by seq_time asc ; ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get intraday indicator from db")
  public static HashMap<String, Object> getIntradayIndicator(String ticker, Long timestamp) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT ");
    queryStringBuilder.append(" ticker as symbol,seq_time as timeSec ,close as close,high as high,low as low, ");
    queryStringBuilder.append(" avg_gain as avgGain,avg_loss as avgLoss,rsi as rsi,rsi_signal as rsiS, ");
    queryStringBuilder.append(" ema12 as ema12,ema26 as ema26,macd as macd,macd_signal as macdS, ");
    queryStringBuilder.append(" macdema as macdEma,macdhist as macdh,macdhist_signal as macdhS, ");
    queryStringBuilder.append(" stochk as stochk,stochk_signal as stochS,stochrsi_fastk as stochrsi, ");
    queryStringBuilder.append(" stochrsi_fastk_signal as stochrsiS,wpr as wpr,wpr_signal as wprS, ");
    queryStringBuilder.append(" typical as typical,ma_typical as maTypical,cci as cci,cci_signal as cciS,roc as roc, ");
    queryStringBuilder.append(" roc_signal as rocS,ultosc as ultosc,ultosc_signal as ultoscS,bbw as bbw, ");
    queryStringBuilder.append(" bbw_signal as bbwS,IFNULL(adx, 0) as adx,adx_signal as adxS,dx as dx,plus_di as `+di`, ");
    queryStringBuilder.append(" minus_di as `-di`,atr as str ,is_raising as isRaising, ");
    queryStringBuilder.append(" accelerate_factor as af,extreme_point as ep,ROUND(sar, 3 ) as sar,next_sar as nextSar,sar_signal as sarS, ");
    queryStringBuilder.append(" buy_pressure as  buyPressure ,true_range as trueRange ,buy_pressure_7 as bp7 ,true_range_7 as tr7 , ");
    queryStringBuilder.append(" buy_pressure_14 as bp14 ,true_range_14 as tr14 ,buy_pressure_28 as bp28 ,true_range_28 as tr28 , ");
    queryStringBuilder.append(" sma5 as s5,sma5_signal as s5S,sma10 as s10,sma10_signal as s10S, ");
    queryStringBuilder.append(" sma20 as s20,sma20_signal as s20S,sma50 as s50,sma50_signal as s50S, ");
    queryStringBuilder.append(" sma100 as s100,sma100_signal as s100S,sma200 as s200,sma200_signal as s200S, ");
    queryStringBuilder.append(" ema5 as e5,ema5_signal as e5S,ema10 as e10,ema10_signal as e10S,ema20 as e20, ");
    queryStringBuilder.append(" ema20_signal as e20S,ema50 as e50,ema50_signal as e50S,ema100 as e100, ");
    queryStringBuilder.append(" ema100_signal as e100S,ema200 as e200,ema200_signal as e200S ");
    queryStringBuilder.append(" FROM indicators_tc_price  WHERE ticker = :ticker_t ");
    if (timestamp != null) {
      queryStringBuilder.append(" and seq_time <= :timestamp and seq_time >= FLOOR( :timestamp /86400) *86400 ");
    }
    queryStringBuilder.append(" order by seq_time desc, updated desc limit 1 ; ");
    try {
      NativeQuery sql = Stockmarket.stockMarketConnection.getSession()
        .createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker_t", ticker);
      if (timestamp != null) {
        sql.setParameter("timestamp", timestamp);
      }
      List<HashMap<String, Object>> rs = sql.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      if (!rs.isEmpty()) {
        return rs.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    HashMap<String, Object> rs = new HashMap<>();
    rs.put("adx", 0);
    return rs;
  }

  @Step("get one daily indicator from db")
  public static HashMap<String, Object> getLastDailyIndicator(String ticker, Long timestamp) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT ");
    queryStringBuilder.append(" ticker as symbol,seq_time as timeSec ,close as close,high as high,low as low, ");
    queryStringBuilder.append(" avg_gain as avgGain,avg_loss as avgLoss,rsi as rsi,rsi_signal as rsiS, ");
    queryStringBuilder.append(" ema12 as ema12,ema26 as ema26,macd as macd,macd_signal as macdS, ");
    queryStringBuilder.append(" macdema as macdEma,macdhist as macdh,macdhist_signal as macdhS, ");
    queryStringBuilder.append(" stochk as stochk,stochk_signal as stochS,stochrsi_fastk as stochrsi, ");
    queryStringBuilder.append(" stochrsi_fastk_signal as stochrsiS,wpr as wpr,wpr_signal as wprS, ");
    queryStringBuilder.append(" typical as typical,ma_typical as maTypical,cci as cci,cci_signal as cciS,roc as roc, ");
    queryStringBuilder.append(" roc_signal as rocS,ultosc as ultosc,ultosc_signal as ultoscS,bbw as bbw, ");
    queryStringBuilder.append(" bbw_signal as bbwS,IFNULL(adx, 0) as adx,adx_signal as adxS,dx as dx,plus_di as `+di`, ");
    queryStringBuilder.append(" minus_di as `-di`,atr as str ,is_raising as isRaising, ");
    queryStringBuilder.append(" ROUND(sar, 3 ) as sar,sar_signal as sarS, ");
    queryStringBuilder.append(" sma5 as s5,sma5_signal as s5S,sma10 as s10,sma10_signal as s10S, ");
    queryStringBuilder.append(" sma20 as s20,sma20_signal as s20S,sma50 as s50,sma50_signal as s50S, ");
    queryStringBuilder.append(" sma100 as s100,sma100_signal as s100S,sma200 as s200,sma200_signal as s200S, ");
    queryStringBuilder.append(" ema5 as e5,ema5_signal as e5S,ema10 as e10,ema10_signal as e10S,ema20 as e20, ");
    queryStringBuilder.append(" ema20_signal as e20S,ema50 as e50,ema50_signal as e50S,ema100 as e100, ");
    queryStringBuilder.append(" ema100_signal as e100S,ema200 as e200,ema200_signal as e200S ");
    queryStringBuilder.append(" FROM indicators_by_1min WHERE ticker = :ticker_t ");
    if (timestamp != null) {
      queryStringBuilder.append(" and seq_time <= :timestamp and seq_time >= FLOOR( :timestamp /86400) *86400 ");
    }
    queryStringBuilder.append(" order by seq_time desc, updated desc  limit 1 ; ");
    try {
      NativeQuery sql = Stockmarket.stockMarketConnection.getSession()
        .createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker_t", ticker);
      if (timestamp != null) {
        sql.setParameter("timestamp", timestamp);
      }
      List<HashMap<String, Object>> rs = sql
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      if (!rs.isEmpty()) {
        return rs.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    HashMap<String, Object> rs = new HashMap<>();
    rs.put("adx", 0);
    return rs;
  }


  @Step("get queue daily indicator from db")
  public static List<HashMap<String, Object>> getDailyQueue(String ticker) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT * from ( ");
    queryStringBuilder.append(" SELECT top(200) Datereport ");
    queryStringBuilder.append(" 	,	high    ");
    queryStringBuilder.append(" 	,   low    ");
    queryStringBuilder.append(" 	,   [close]   ");
    queryStringBuilder.append(" 	,   rsi    ");
    queryStringBuilder.append(" 	,   macd    ");
    queryStringBuilder.append(" 	,   typical    ");
    queryStringBuilder.append(" 	,   ma_typical as maTypical   ");
    queryStringBuilder.append(" 	,   buy_pressure  ");
    queryStringBuilder.append(" 	,   true_range  ");
    queryStringBuilder.append(" 	,   updated  ");
    queryStringBuilder.append(" FROM idata_indicators_daily  ");
    queryStringBuilder.append(" where ticker = :ticker  ");
    queryStringBuilder.append(" order by Datereport  desc , updated desc) x ");
    queryStringBuilder.append(" order by Datereport  asc, updated asc ; ");
    try {
      return Stoxplus.stoxDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}

    
		










