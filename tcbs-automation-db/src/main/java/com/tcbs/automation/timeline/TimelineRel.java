package com.tcbs.automation.timeline;

import net.thucydides.core.annotations.Step;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.List;
import java.util.Map;

public class TimelineRel {
  public TimelineRel() {

  }

  @SuppressWarnings({"deprecation", "unchecked"})
  @Step
  public String getTimelineBondFromTimelinePlan(String timelinePlan) {
    String sql =
      "select * from timeline_rel where src_id = :timelinePlan ";
    List<Map<String, Object>> result = Timeline.timelineDbConnection.getSession().createNativeQuery(sql)
      .setParameter("timelinePlan", timelinePlan)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    if (result.size() > 0) {
      return result.get(0).get("target_id").toString();
    } else {
      return null;
    }
  }
}
