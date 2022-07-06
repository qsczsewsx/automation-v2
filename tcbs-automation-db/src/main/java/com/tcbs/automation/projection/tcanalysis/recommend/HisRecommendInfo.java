package com.tcbs.automation.projection.tcanalysis.recommend;

import com.tcbs.automation.projection.Projection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HisRecommendInfo {
  public static final String SELECT_ALL = "SELECT rh.* \n" +
    "FROM db_owner.tbl_idata_recommend_history rh\n";

  private String ticker;
  private String d;
  private Double recommendValue;
  private String recommendText;
  private Integer recommendType;
  private String reason;
  private List<HisBuyItem> hisBuyItemList;

  public static List<HisRecommendInfo> getAllByFilter(String fType, String fData) {
    String query = "";
    if ("tickers".equals(fType)) {
      query = "SELECT ticker ,\n" +
        "date_report ,\n" +
        "recommend_text ,\n" +
        "recommend_value ,\n" +
        "recommend_type,\n" +
        "recommend_reason \n" +
        "FROM db_owner.tbl_idata_recommend_history\n" +
        "WHERE ticker IN (Select value from STRING_SPLIT(:fData, ',')) \n" +
        "ORDER BY date_report DESC ";
    } else if ("industryId".equals(fType)) {
      query = SELECT_ALL +
        "left join db_owner.stox_tb_Company comp\n" +
        "on rh.ticker = comp.Ticker\n" +
        "WHERE comp.IndustryID IN (SELECT IdLevel4 FROM view_idata_industry vii WHERE IdLevel2  = :fData )\n" +
        "ORDER BY rh.date_report DESC ";
    } else if ("industryIdL4".equals(fType)) {
      query = SELECT_ALL +
        "left join db_owner.stox_tb_Company comp\n" +
        "on rh.ticker = comp.Ticker\n" +
        "WHERE comp.IndustryID IN \n" +
        "(SELECT IdLevel4 FROM view_idata_industry vii \n" +
        "WHERE IdLevel2  = (SELECT IdLevel2 FROM view_idata_industry vii WHERE IdLevel4  = :fData) )\n" +
        "ORDER BY rh.date_report DESC ";
    } else {
      query = SELECT_ALL +
        "WHERE :fData = '1'\n" +
        "ORDER BY rh.date_report DESC";
    }
    List<Map<String, Object>> results;
    try {
      results = Projection.projectionDbConnection.getSession().createNativeQuery(query)
        .setParameter("fData", fData)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return buildObj(results);
    } catch (Exception e) {
      StepEventBus.getEventBus().testFailed(new AssertionError(e.getMessage()));
    } finally {
      Projection.projectionDbConnection.closeSession();
    }
    return new ArrayList<>();
  }

  private static List<HisRecommendInfo> buildObj(List<Map<String, Object>> results) {
    List<HisRecommendInfo> hisRecommendInfoList = new ArrayList<>();
    for (Map<String, Object> m : results) {
      HisRecommendInfo hisRecommendInfo = HisRecommendInfo.builder()
        .ticker((String) m.get("ticker"))
        .d(String.valueOf(m.get("date_report")))
        .recommendText((String) m.get("recommend_text"))
        .recommendValue(m.get("recommend_value") == null ? null : (Double) m.get("recommend_value") / 1000.0)
        .recommendType((Integer) m.get("recommend_type"))
        .reason((String) m.get("recommend_reason"))
        .build();
      hisRecommendInfoList.add(hisRecommendInfo);
    }
    return hisRecommendInfoList;
  }

}
