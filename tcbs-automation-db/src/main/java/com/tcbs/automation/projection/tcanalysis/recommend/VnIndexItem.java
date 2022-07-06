package com.tcbs.automation.projection.tcanalysis.recommend;

import com.tcbs.automation.tca.TcAnalysis;
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
public class VnIndexItem {
  private String dateReport;
  private Double vnindex;

  public static List<VnIndexItem> get(String dateAgo) {
    String query = " SELECT convert(date, TradingDate) date_report, "
	        + "		CONVERT (float, IndexValue / 100) AS vnindex "
	        + "	FROM  stx_mrk_HoseIndex "
	        + "	where ComGroupCode = 'VNINDEX' "
	        + "		AND TradingDate >= :dateAgo "
	        + "	order by TradingDate DESC";

    try {
      List<Map<String, Object>> results = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(query)
        .setParameter("dateAgo", dateAgo)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return buildObj(results);
    } catch (Exception e) {
      StepEventBus.getEventBus().testFailed(new AssertionError(e.getMessage()));
    }
    return new ArrayList<>();
  }

  private static List<VnIndexItem> buildObj(List<Map<String, Object>> lhm) {
    List<VnIndexItem> results = new ArrayList<>();
    for (Map<String, Object> m : lhm) {
      results.add(VnIndexItem.builder()
        .dateReport(m.get("date_report") + "")
        .vnindex((Double) m.get("vnindex"))
        .build());
    }
    return results;
  }
}
