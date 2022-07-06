package com.tcbs.automation.projection.tcanalysis.recommend;

import com.tcbs.automation.projection.Projection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecommendItem {
  private static Logger logger = LoggerFactory.getLogger(RecommendItem.class);
  private String from;
  private String to;
  private Double vnindex;
  private Double vnindexNorm;
  private Double sell;
  private Double buy;
  private Double waitingSell;
  private Double waitingBuy;
  private String dateReport;
  private String recommendText;

  public static List<RecommendItem> get(String dateAgo, String filterData) {
    String query = "";
    if ("A".equals(filterData)) {
      query = "SELECT CONVERT(varchar(30), date_report) as date_report,\n" +
        "recommend_text \n" +
        "FROM db_owner.tbl_idata_recommend_history rh \n" +
        "WHERE date_report >= :dateAgo\n" +
        "AND :filterData = 'A'\n" +
        "ORDER BY date_report";
    } else {
      query = "SELECT CONVERT(varchar(30), date_report) AS date_report,\n" +
        "recommend_text \n" +
        "FROM db_owner.tbl_idata_recommend_history rh\n" +
        "left join db_owner.stox_tb_Company comp\n" +
        "on rh.ticker = comp.Ticker\n" +
        "WHERE comp.IndustryID IN (SELECT IdLevel4 FROM view_idata_industry vii WHERE IdLevel2  =  :filterData)\n" +
        "AND rh.date_report >= :dateAgo\n" +
        "ORDER BY date_report";
    }
    logger.info("query = {}\n dateAgo={}, \n filterData={}", query, dateAgo, filterData);
    try {
      List<Map<String, Object>> results = Projection.projectionDbConnection.getSession().createNativeQuery(query)
        .setParameter("dateAgo", dateAgo)
        .setParameter("filterData", filterData)
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

  private static List<RecommendItem> buildObj(List<Map<String, Object>> lhm) {
    List<RecommendItem> results = new ArrayList<>();
    for (Map<String, Object> m : lhm) {
      results.add(RecommendItem.builder()
        .dateReport((String) m.get("date_report"))
        .recommendText((String) m.get("recommend_text"))
        .build());
    }
    return results;
  }

}
