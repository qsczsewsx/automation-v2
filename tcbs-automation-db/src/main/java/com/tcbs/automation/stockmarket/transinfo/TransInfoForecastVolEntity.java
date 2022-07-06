package com.tcbs.automation.stockmarket.transinfo;

import com.tcbs.automation.stockmarket.Stockmarket;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TransInfoForecastVolEntity {
  private static final Logger LOG = LoggerFactory.getLogger(TransInfoForecastVolEntity.class);

  public static List<Map<String, Object>> getAccVol(String ticker, String fromTime, String toTime) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("SELECT seq_time\n" +
      ", IF(seq_time < (:toTime - 15*60), IFNULL(i_acc_volume, 0), i_acc_volume) as acc_vol\n" +
      "FROM \n" +
      "(\n" +
      "select seq_time \n" +
      ", intraday.i_acc_volume\n" +
      "FROM\n" +
      "(\n" +
      "SELECT time_series + :fromTime AS seq_time\n" +
      "FROM timeseries_15min_temp_full  \n" +
      "WHERE (time_series + :fromTime) < (:toTime - 15*60)\n" +
      ") temp\n" +
      "LEFT JOIN \n" +
      "(\n" +
      "SELECT i_seq_time, i_acc_volume FROM intraday_by_15min \n" +
      "WHERE t_ticker = :ticker AND i_seq_time >= :fromTime\n" +
      ") intraday\n" +
      "ON temp.seq_time = intraday.i_seq_time\n" +
      "ORDER BY temp.seq_time ASC\n" +
      ") tbl");

    try {
      List<Map<String, Object>> lst = Stockmarket.stockMarketConnection.getSession().createNativeQuery(stringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("fromTime", fromTime)
        .setParameter("toTime", toTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return buildAndFixMissingAccVol(lst, ticker);
    } catch (Exception e) {
      StepEventBus.getEventBus().testFailed(new AssertionError(e.getMessage()));
    } finally {
      Stockmarket.stockMarketConnection.closeSession();
    }
    return new ArrayList<>();
  }

  public static List<Map<String, Object>> getForecastVol(String ticker, String fromTime, String toTime) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(" select ticker, seq_time, forecast_vol as fc_vol, intraday_acc_vol as acc_vol from intraday_forecast_vol_15min ifvm  ");
    stringBuilder.append(" WHERE ticker =:ticker and seq_time >= :fromTime and seq_time <= :toTime ");
    stringBuilder.append(" order by seq_time desc limit 0,1; ");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(stringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("fromTime", fromTime)
        .setParameter("toTime", toTime)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

    } catch (Exception e) {
      StepEventBus.getEventBus().testFailed(new AssertionError(e.getMessage()));
    } finally {
      Stockmarket.stockMarketConnection.closeSession();
    }
    return new ArrayList<>();
  }

  public static String getPrevDay(String pStartTrading) {
    int secondPerDay = 24 * 60 * 60;

    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    Date pStartTradingDate = null;
    try {
      pStartTradingDate = sdf.parse(pStartTrading);
    } catch (ParseException e) {
      StepEventBus.getEventBus().testFailed(new AssertionError(e.getMessage()));
    }

    long prevDay = 0;
    if (pStartTradingDate != null) {
      prevDay = pStartTradingDate.getTime() - secondPerDay * 1000;
    }

    Date inputDate = new java.sql.Date(prevDay);

    while (isWeekend(inputDate)) {
      prevDay = prevDay - secondPerDay * 1000;
      inputDate = new java.sql.Date(prevDay);
    }

    return sdf.format(new java.sql.Date(prevDay));
  }

  //Verify the date is weekend
  public static boolean isWeekend(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY;
  }

  private static List<Map<String, Object>> buildAndFixMissingForecastVol(List<Map<String, Object>> lm, String ticker) {
    Double[] listSeqTime = new Double[lm.size()];
    Double[] listExAV1 = new Double[lm.size()];
    Double[] listExAV2 = new Double[lm.size()];
    Double[] listExAV3 = new Double[lm.size()];
    Double[] listExAV4 = new Double[lm.size()];
    Double[] listExAV5 = new Double[lm.size()];
    Double[] listExAVP1 = new Double[lm.size()];
    Double[] listExAVP2 = new Double[lm.size()];
    Double[] listExAVP3 = new Double[lm.size()];
    Double[] listExAVP4 = new Double[lm.size()];
    Double[] listExAVP5 = new Double[lm.size()];

    Double[] listAvgAccVol = new Double[lm.size()];
    Double[] listAvgAccVolP = new Double[lm.size()];

    for (int i = 0; i < lm.size(); ++i) {
      listSeqTime[i] = ((Double) lm.get(i).get("seq_time"));
      listExAV1[i] = ((BigInteger) lm.get(i).get("exAV1")).doubleValue();
      listExAV2[i] = ((BigInteger) lm.get(i).get("exAV2")).doubleValue();
      listExAV3[i] = ((BigInteger) lm.get(i).get("exAV3")).doubleValue();
      listExAV4[i] = ((BigInteger) lm.get(i).get("exAV4")).doubleValue();
      listExAV5[i] = ((BigInteger) lm.get(i).get("exAV5")).doubleValue();
      listExAVP1[i] = ((BigDecimal) lm.get(i).get("exAVP1")).doubleValue();
      listExAVP2[i] = ((BigDecimal) lm.get(i).get("exAVP2")).doubleValue();
      listExAVP3[i] = ((BigDecimal) lm.get(i).get("exAVP3")).doubleValue();
      listExAVP4[i] = ((BigDecimal) lm.get(i).get("exAVP4")).doubleValue();
      listExAVP5[i] = ((BigDecimal) lm.get(i).get("exAVP5")).doubleValue();
    }

    listExAV1 = fixNullData(listExAV1);
    listExAV2 = fixNullData(listExAV2);
    listExAV3 = fixNullData(listExAV3);
    listExAV4 = fixNullData(listExAV4);
    listExAV5 = fixNullData(listExAV5);
    listExAVP1 = fixNullData(listExAVP1);
    listExAVP2 = fixNullData(listExAVP2);
    listExAVP3 = fixNullData(listExAVP3);
    listExAVP4 = fixNullData(listExAVP4);
    listExAVP5 = fixNullData(listExAVP5);

    for (int i = 0; i < lm.size(); ++i) {
      listAvgAccVol[i] = (listExAV1[i] + listExAV2[i] + listExAV3[i] + listExAV4[i] + listExAV5[i]) / 5;
      listAvgAccVolP[i] = (listExAVP1[i] + listExAVP2[i] + listExAVP3[i] + listExAVP4[i] + listExAVP5[i]) / 5;
    }

    List<Map<String, Object>> lmRet = new ArrayList<>();

    for (int i = 0; i < lm.size(); ++i) {
      Map<String, Object> m = new HashMap<>();
      m.put("seq_time", listSeqTime[i]);
      m.put("ticker", ticker);
      m.put("avgAccVol", listAvgAccVol[i]);
      m.put("avgAccVolP", listAvgAccVolP[i]);
      lmRet.add(m);
    }

    return lmRet;
  }

  private static List<Map<String, Object>> buildAndFixMissingAccVol(List<Map<String, Object>> data, String ticker) {
    List<Map<String, Object>> newData = new ArrayList<>();

    Double[] listSeqTime = new Double[data.size()];
    Double[] listAccVol = new Double[data.size()];

    for (int i = 0; i < data.size(); ++i) {
      listSeqTime[i] = ((Double) data.get(i).get("seq_time"));
      listAccVol[i] = ((BigInteger) data.get(i).get("acc_vol")).doubleValue();
    }
    listAccVol = fixNullData(listAccVol);

    for (int i = 0; i < data.size(); i++) {
      Map<String, Object> map = new HashMap<>();
      map.put("ticker", ticker);
      map.put("seq_time", listSeqTime[i]);
      map.put("acc_vol", listAccVol[i]);

      newData.add(map);
    }
    return newData;
  }

  private static Double[] fixNullData(Double[] data) {
    for (int i = 1; i < data.length; ++i) {
      if (data[i] == 0) {
        data[i] = data[i - 1];
      }
    }
    return data;
  }

}
