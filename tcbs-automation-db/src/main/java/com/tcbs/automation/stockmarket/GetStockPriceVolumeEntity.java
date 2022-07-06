package com.tcbs.automation.stockmarket;

import com.tcbs.automation.stoxplus.stock.TickerOverviewEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetStockPriceVolumeEntity {
  private Float absoluteChangedPrice;
  private Float changePrice;
  private Float closePrice;
  private Float referencePrice;
  private Integer volume;
  @Id
  private String tradingdate;

  @Step("select data")
  public static List<Map<String, Object>> getPriceAndVolume(String ticker, String resolution, String futureName) {
    if (ticker.startsWith("VN30F") || ticker.startsWith("GB10F") || ticker.startsWith("GB05F")) {
      return getPriceAndVolumeDerivative(ticker, resolution, futureName);
    }
    StringBuilder queryStringBuilder = new StringBuilder();
    if (resolution.equals("15")) {
      queryStringBuilder.append(" SELECT t.* FROM ( ");
      queryStringBuilder.append("       SELECT DISTINCT(i_seq_time), t_ticker, f_close_price, ");
      queryStringBuilder.append("       f_open_price,i_volume,i_acc_volume ");
      queryStringBuilder.append("       FROM intraday_by_15min WHERE t_ticker = :t_ticker ORDER BY i_seq_time DESC ");
      queryStringBuilder.append("       LIMIT 60 ) t ORDER BY i_seq_time ASC ");
    } else if (resolution.equals("30")) {
      queryStringBuilder.append(" SELECT t1.* FROM ( ");
      queryStringBuilder.append("       SELECT DISTINCT(i_seq_time), t_ticker, ");
      queryStringBuilder.append("     f_close_price,  f_open_price, ");
      queryStringBuilder.append("       i_volume,i_acc_volume ");
      queryStringBuilder.append("       FROM intraday_by_30min WHERE t_ticker = :t_ticker ORDER BY i_seq_time DESC ");
      queryStringBuilder.append("       LIMIT 60 ) t1 ORDER BY i_seq_time ASC ");
    } else if (resolution.equals("60")) {
      queryStringBuilder.append(" SELECT t2.* FROM ( ");
      queryStringBuilder.append("       SELECT DISTINCT(i_seq_time), ");
      queryStringBuilder.append("       f_close_price, t_ticker,  f_open_price, ");
      queryStringBuilder.append("       i_volume, ");
      queryStringBuilder.append("       i_acc_volume ");
      queryStringBuilder.append("       FROM intraday_by_60min ");
      queryStringBuilder.append("       WHERE t_ticker = :t_ticker ");
      queryStringBuilder.append("       ORDER BY i_seq_time DESC ");
      queryStringBuilder.append("       LIMIT 60 ) t2 ORDER BY i_seq_time ASC ");
    } else if (resolution.equals("5")) {
      queryStringBuilder.append(" SELECT t1.* FROM ( ");
      queryStringBuilder.append("       SELECT DISTINCT(i_seq_time), t_ticker, ");
      queryStringBuilder.append("     f_close_price,  f_open_price, ");
      queryStringBuilder.append("       i_volume,i_acc_volume ");
      queryStringBuilder.append("       FROM intraday_by_5min WHERE t_ticker = :t_ticker ORDER BY i_seq_time DESC ");
      queryStringBuilder.append("       LIMIT 60 ) t1 ORDER BY i_seq_time ASC ");
    }

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

  private static List<Map<String, Object>> getPriceAndVolumeDerivative(String ticker, String resolution, String futureName) {
    if (StringUtils.isEmpty(futureName)) {
      return new ArrayList<>();
    }
    String table = "";
    if (resolution.equals("15")) {
      table = "intraday_by_15min";
    } else if (resolution.equals("30")) {
      table = "intraday_by_30min";
    } else if (resolution.equals("60")) {
      table = "intraday_by_60min";
    }else if (resolution.equals("5")) {
      table = "intraday_by_5min";
    }
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT t.* FROM ( ");
    queryStringBuilder.append("   SELECT DISTINCT(i_seq_time), t_ticker, f_close_price, ");
    queryStringBuilder.append("   f_open_price,i_volume,i_acc_volume ");
    queryStringBuilder.append("   FROM ").append(table);
    queryStringBuilder.append("   WHERE func_idata_future_name_by_ticker_and_time(t_ticker, i_seq_time)  = :future_name ");
    queryStringBuilder.append("   ORDER BY i_seq_time DESC ");
    queryStringBuilder.append("   LIMIT 60 ) t ORDER BY i_seq_time ASC ");

    try {
      return FuturesMarket.futuresMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString()) //TODO doi db
//        .setParameter("ticker_f", ticker)
        .setParameter("future_name", futureName)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("select data")
  public static List<Map<String, Object>> getPriceAndVolume1min(String ticker, long startTrading) {
    String exchange = TickerOverviewEntity.getExchangeByTicker(ticker, "vi");
    long fromDate = startTrading;
    if (StringUtils.equals("HOSE", exchange)) {
      fromDate += 15 * 60;
    }
    long toDate = startTrading + 6*3600;//new Date().getTime() / 1000;
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT  i_seq_time, FROM_UNIXTIME(i_seq_time), t_ticker, cast(f_close_price as CHAR) as  f_close_price, f_open_price, i_volume, i_acc_volume ");
    queryStringBuilder.append("  FROM (  ");
    queryStringBuilder.append(" 	SELECT i_seq_time, FROM_UNIXTIME(i_seq_time),   ");
    queryStringBuilder.append(" 		:t_ticker as t_ticker, @p \\:= IFNULL(f_close_price, @p) as f_close_price,   ");
    queryStringBuilder.append(" 			f_open_price, i_acc_volume , @trad \\:= if(i_volume is null and @p is not null, 1, 0) as trad, ");
    queryStringBuilder.append(" 			IF(@trad = 1 and i_volume is null, 0, i_volume) as i_volume ");
    queryStringBuilder.append(" 	FROM ( ");
    queryStringBuilder.append(" 		SELECT DISTINCT(i_seq_time), FROM_UNIXTIME(i_seq_time) ,t_ticker, f_close_price, f_open_price, i_volume, i_acc_volume ");
    queryStringBuilder.append(" 		FROM intraday_by_1min   ");
    queryStringBuilder.append(" 		WHERE t_ticker = :t_ticker   ");
    queryStringBuilder.append(" 		AND i_seq_time >=  :startTrading  ");
    queryStringBuilder.append(" 	) t  ");
    queryStringBuilder.append(" 	cross join (select @p \\:=null , @trad \\:= 0, @v \\:= null)param  ");
    queryStringBuilder.append("  ) tbt WHERE i_seq_time >= :fromDate  AND i_seq_time <= :toDate ");
    queryStringBuilder.append("  ORDER BY i_seq_time ASC ;  ");
    try {
      return InvestorEntity.getHibernateEdition(ticker).getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("t_ticker", ticker)
        .setParameter("startTrading", startTrading)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("select data")
  public static Float getPresentOpenPrice(String ticker, String startDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT f_open_price FROM intraday_by_1min  ");
    queryStringBuilder.append(" WHERE t_ticker = :ticker AND i_seq_time > :startTrading");
    queryStringBuilder.append(" ORDER BY i_seq_time ASC ");
    try {
      List<Map<String, Object>> query = Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("startTrading", startDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      if (query.isEmpty()) {
        return null;
      }
      return (Float) query.get(0).get("f_open_price");
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  @Step("select data")
  public static Map<String, Object> getPresentPriceAndVolume(String ticker, String startDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(
      " SELECT t_ticker as ticker, i_seq_time as seq_time, f_close_price as close_price, f_open_price_change, i_acc_volume as volume, (f_open_price -  f_open_price_change) as price_ref ");
    queryStringBuilder.append(" FROM intraday_by_1min ");
    queryStringBuilder.append(" WHERE t_ticker = :ticker AND i_seq_time > :startTrade AND i_seq_time <= :startTrade + 3600*6");
    queryStringBuilder.append(" ORDER BY i_seq_time DESC ");

    try {
      List<Map<String, Object>> query = Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("startTrade", startDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return query.get(0);
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

}
