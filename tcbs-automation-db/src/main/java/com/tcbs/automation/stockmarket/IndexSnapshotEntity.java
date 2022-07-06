package com.tcbs.automation.stockmarket;

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
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class IndexSnapshotEntity {
  private String marketCode;
  private Float openIndex;
  private Float highIndex;
  private Float lowIndex;
  private Float closeIndex;
  private Float openChangeIndex;
  private Float volume;
  @Id
  private Long seqTime;

  @Step("select data")
  public static List<IndexSnapshotEntity> getIndexIntraday(String indexes, Long from, Long to) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT t_market_code, f_open_index, f_close_index, i_volume, f_open_index_change, i_seq_time ");
    queryStringBuilder.append("   FROM intradayindex_by_1min ");
    queryStringBuilder.append("   WHERE t_market_code in (:indexCodes) ");
    queryStringBuilder.append("     AND i_seq_time >= :from_time ");
    queryStringBuilder.append("     AND i_seq_time < :to_time ");
    queryStringBuilder.append("   ORDER BY i_seq_time ASC ");

    List<String> list = Arrays.asList(StringUtils.split(indexes, ","));
    try {
      List<Map<String, Object>> listMap = Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("indexCodes", list)
        .setParameter("from_time", from)
        .setParameter("to_time", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

      List<IndexSnapshotEntity> listResult = new ArrayList<>();
      listMap.forEach(item -> {
        IndexSnapshotEntity entity = IndexSnapshotEntity.builder()
          .marketCode((String) item.get("t_market_code"))
          .openIndex(Float.valueOf(item.get("f_open_index").toString()))
          .closeIndex(Float.valueOf(item.get("f_close_index").toString()))
          .volume(Float.valueOf(item.get("i_volume").toString()))
          .openChangeIndex(Float.valueOf(item.get("f_open_index_change").toString()))
          .seqTime(Long.valueOf(item.get("i_seq_time").toString()))
          .build();
        listResult.add(entity);
      });
      return listResult;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList();
  }


  @Step("select data")
  public static List<IndexSnapshotEntity> getTickerIntraday(String tickers, Long from, Long to) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT t_ticker, f_open_price, f_close_price, i_volume, f_open_price_change, i_seq_time ");
    queryStringBuilder.append("   FROM intraday_by_5min ");
    queryStringBuilder.append("   WHERE t_ticker in (:tickers) ");
    queryStringBuilder.append("     AND i_seq_time >= :from_time ");
    queryStringBuilder.append("     AND i_seq_time <= :to_time ");
    queryStringBuilder.append("   ORDER BY i_seq_time ASC ");

    List<String> list = Arrays.asList(StringUtils.split(tickers, ","));
    try {
      List<Map<String, Object>> listMap = Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("tickers", list)
        .setParameter("from_time", from)
        .setParameter("to_time", to)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

      List<IndexSnapshotEntity> listResult = new ArrayList<>();
      listMap.forEach(item -> {
        IndexSnapshotEntity entity = IndexSnapshotEntity.builder()
          .marketCode((String) item.get("t_ticker"))
          .openIndex(Float.valueOf(item.get("f_open_price").toString()))
          .closeIndex(Float.valueOf(item.get("f_close_price").toString()))
          .volume(Float.valueOf(item.get("i_volume").toString()))
          .openChangeIndex(Float.valueOf(item.get("f_open_price_change").toString()))
          .seqTime(Long.valueOf(item.get("i_seq_time").toString()))
          .build();
        listResult.add(entity);
      });
      return listResult;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList();
  }

}
