package com.tcbs.automation.projection.tcanalysis;

import com.tcbs.automation.projection.Projection;
import lombok.Builder;
import lombok.Data;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@Builder
public class TcData {
  @Step
  public static List<HashMap<String, Object>> getProjectionData(String ticker, Integer version, String query) {
    try {
      return Projection.projectionDbConnection.getSession().createNativeQuery(query)
        .setParameter("id", version == null ? ticker : version).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
      ex.printStackTrace(System.out);
    }
    return new ArrayList<>();
  }

}
