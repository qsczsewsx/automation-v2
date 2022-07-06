package com.tcbs.automation.projection.tcanalysis;


import com.tcbs.automation.projection.Projection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Prc_Stock_CAPM", schema = "db_owner")
public class StockCapm {
  private static final Logger log = LoggerFactory.getLogger(StockCapm.class);

  @Id
  @Column(name = "Ticker")
  private Integer ticker;

  @Column(name = "Beta")
  private Double beta;

  @Column(name = "Alpha")
  private Double alpha;

  @Column(name = "PE")
  private Double pe;

  @Column(name = "DateReport")
  private Double dateReport;

  @Step
  public static Map<String, Object> getStockCapmByTop200() {
    StringBuilder queryBuilder = new StringBuilder();
    try {
      queryBuilder.append("   SELECT AVG(Alpha) as alpha, AVG(Beta) as beta ");
      queryBuilder.append("   FROM db_owner.Prc_Stock_CAPM  ");
      queryBuilder.append("   WHERE DateReport = (  ");
      queryBuilder.append("         SELECT MAX(DateReport) FROM db_owner.Prc_Stock_CAPM)  ");
      queryBuilder.append("   AND Ticker in ( SELECT TOP(200) Ticker    ");
      queryBuilder.append("         FROM [db_owner].[stox_tb_Ratio]    ");
      queryBuilder.append("         WHERE CAST(UpdateDate AS date) = CAST((SELECT MAX(UpdateDate) FROM [db_owner].[stox_tb_Ratio] ) AS date)    ");
      queryBuilder.append("         ORDER BY F5_7 DESC ) ");

      List<Map<String, Object>> map = Projection.projectionDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      if (CollectionUtils.isNotEmpty(map)) {
        return map.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  @Step
  public static Map<String, Object> getStockCapmByIndustry(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    try {
      queryBuilder.append("   SELECT AVG(Alpha) as alpha, AVG(Beta) as beta ");
      queryBuilder.append("   FROM db_owner.Prc_Stock_CAPM  ");
      queryBuilder.append("   WHERE DateReport = (  ");
      queryBuilder.append("         SELECT MAX(DateReport) FROM db_owner.Prc_Stock_CAPM)  ");
      queryBuilder.append("   AND Ticker in ( SELECT comp.Ticker    ");
      queryBuilder.append("               FROM db_owner.stox_tb_Company comp   ");
      queryBuilder.append("               JOIN view_idata_industry vwind   ");
      queryBuilder.append("               ON comp.IndustryID = vwind.IdLevel4   ");
      queryBuilder.append("               WHERE vwind.IdLevel2 = ( SELECT IdLevel2 FROM db_owner.stox_tb_Company comp   ");
      queryBuilder.append("               JOIN view_idata_industry vwind   ");
      queryBuilder.append("               ON vwind.IdLevel4 = comp.IndustryID   ");
      queryBuilder.append("               WHERE comp.Ticker = :ticker))   ");

      List<Map<String, Object>> map = Projection.projectionDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      if (CollectionUtils.isNotEmpty(map)) {
        return map.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  @Step
  public static Map<String, Object> getStockCapmByTicker(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    try {
      queryBuilder.append("   SELECT Alpha as alpha, Beta as beta ");
      queryBuilder.append("   FROM db_owner.Prc_Stock_CAPM  ");
      queryBuilder.append("   WHERE DateReport = (  ");
      queryBuilder.append("         SELECT MAX(DateReport) FROM db_owner.Prc_Stock_CAPM)  ");
      queryBuilder.append("   AND Ticker = :ticker   ");

      List<Map<String, Object>> map = Projection.projectionDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      if (CollectionUtils.isNotEmpty(map)) {
        return map.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }
}
