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
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_idata_RatingIndex", schema = "db_owner")
public class RatingIndex {
  private static final Logger log = LoggerFactory.getLogger(RatingIndex.class);

  @Id
  private Integer id;

  @Column(name = "Ticker")
  private String ticker;

  @Column(name = "ParentID")
  private Integer parentID;

  @Column(name = "RatingKey")
  private String ratingKey;

  @Column(name = "RatingValue")
  private Double ratingValue;

  @Column(name = "UpdateTime")
  private Date updateTime;

  @Column(name = "Note")
  private String note;


  /**
   * get lastest rating
   *
   * @param ticker
   * @param ratingKeyId
   * @return
   */

  public static List<RatingIndex> getRatingIndexByTicker(String ticker, Integer ratingKeyId) {
    StringBuilder query = new StringBuilder();
    try {
      query.append("   SELECT tkey.RatingKey, max(tinx.RatingValue ) RatingValue ");
      query.append("   FROM db_owner.tbl_idata_RatingIndex tinx ");
      query.append("   JOIN db_owner.tbl_idata_RatingKey tkey ");
      query.append("   ON tinx.RatingKeyID  = tkey.ID   ");
      query.append("   WHERE Ticker = :ticker   ");
      query.append("   AND (tkey.id = :ratingKeyID OR tkey.ParentID = :ratingKeyID) group by  tkey.RatingKey ");
      List<Map<String, Object>> results = Projection.projectionDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("ticker", ticker)
        .setParameter("ratingKeyID", ratingKeyId)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return extractResult(results);
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    } finally {
      Projection.projectionDbConnection.closeSession();
    }
    return null;
  }

  @Step
  public static List<RatingIndex> getRatingIndexByTop200(Integer ratingKeyId) {
    StringBuilder query = new StringBuilder();
    try {
      query.append(" SELECT tkey.RatingKey, AVG(tinx.RatingValue) as RatingValue   ");
      query.append(" FROM (    ");
      query.append("     SELECT TOP(200) Ticker    ");
      query.append("     FROM	[db_owner].[stox_tb_Ratio]    ");
      query.append(" WHERE	CAST(UpdateDate AS date) = CAST((SELECT MAX(UpdateDate) FROM [db_owner].[stox_tb_Ratio] ) AS date)    ");
      query.append(" ORDER BY F5_7 DESC ) top200    ");
      query.append(" LEFT JOIN  db_owner.tbl_idata_RatingIndex tinx    ");
      query.append(" ON top200.Ticker  = tinx.Ticker    ");
      query.append(" LEFT JOIN (    ");
      query.append("   SELECT ID, ParentID, RatingKey    ");
      query.append(" FROM db_owner.tbl_idata_RatingKey    ");
      query.append(" WHERE ID = :ratingKeyId OR ParentID = :ratingKeyId    ");
      query.append(" 		) tkey    ");
      query.append(" ON tinx.RatingKeyID  = tkey.ID    ");
      query.append(" GROUP BY tkey.RatingKey    ");

      List<Map<String, Object>> results = Projection.projectionDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("ratingKeyId", ratingKeyId)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return extractResult(results);
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    } finally {
      Projection.projectionDbConnection.closeSession();
    }
    return null;
  }


  @Step
  public static List<RatingIndex> getRatingIndexByIndustry(String ticker, Integer ratingKeyId) {
    StringBuilder query = new StringBuilder();
    try {
      query.append("  SELECT tkey.RatingKey, AVG(tinx.RatingValue) as RatingValue  ");
      query.append("  FROM  (   ");
      query.append("      SELECT comp.Ticker   ");
      query.append("      FROM db_owner.stox_tb_Company comp   ");
      query.append("      JOIN view_idata_industry vwind   ");
      query.append("      ON comp.IndustryID = vwind.IdLevel4   ");
      query.append("      WHERE vwind.IdLevel2 = ( SELECT IdLevel2 FROM db_owner.stox_tb_Company comp   ");
      query.append("      JOIN view_idata_industry vwind   ");
      query.append("      ON vwind.IdLevel4 = comp.IndustryID   ");
      query.append("      WHERE comp.Ticker = :ticker)   ");
      query.append("		) indusAvg   ");
      query.append("  LEFT JOIN  db_owner.tbl_idata_RatingIndex tinx   ");
      query.append("  ON indusAvg.Ticker  = tinx.Ticker   ");
      query.append("  LEFT JOIN  (   ");
      query.append("    SELECT ID, ParentID, RatingKey   ");
      query.append("  FROM db_owner.tbl_idata_RatingKey   ");
      query.append("  WHERE ID = :ratingKeyID OR ParentID = :ratingKeyID   ");
      query.append("		) tkey   ");
      query.append("  ON tinx.RatingKeyID  = tkey.ID   ");
      query.append("  GROUP BY tkey.RatingKey   ");

      List<Map<String, Object>> results = Projection.projectionDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("ratingKeyID", ratingKeyId)
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return extractResult(results);
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    } finally {
      Projection.projectionDbConnection.closeSession();
    }
    return null;
  }

  private static List<RatingIndex> extractResult(List<Map<String, Object>> results) {
    List<RatingIndex> listResult = new ArrayList<>();
    if (CollectionUtils.isNotEmpty(results)) {
      results.stream().forEach(map -> {
        RatingIndex info = RatingIndex.builder()
          .id((Integer) map.get("ID"))
          .parentID((Integer) map.get("ParentID"))
          .ratingKey((String) map.get("RatingKey"))
          .note((String) map.get("Note"))
          .ticker((String) map.get("Ticker"))
          .ratingValue((Double) map.get("RatingValue"))
          .updateTime((Date) map.get("UpdateTime"))
          .build();
        listResult.add(info);
      });
    }
    return listResult;
  }

  public static void fillValueFromListKey(List<RatingIndex> listRating, Object toObject, String ticker) {
    if (CollectionUtils.isNotEmpty(listRating)) {
      DecimalFormat formater = new DecimalFormat();
      formater.setRoundingMode(RoundingMode.HALF_UP);
      formater.applyPattern("#.");
      for (RatingIndex rating : listRating) {
        Field[] fields = toObject.getClass().getDeclaredFields();
        Stream.of(fields).forEach(f -> {
          Double ratingValue = rating.getRatingValue();
          if (ratingValue != null && (f.getName().equals("stockRating") || f.getName().equals("businessModel")
            || f.getName().equals("businessOperation") || f.getName().equals("financialHealth")
            || f.getName().equals("taScore") || f.getName().equals("rsRating") || f.getName().equals("valuation"))) {
            ratingValue = Math.round(ratingValue * Math.pow(10, 1)) / Math.pow(10, 1);
          } else if (ratingValue != null) {
            ratingValue = Math.round(ratingValue * Math.pow(10, 0)) / Math.pow(10, 0);
          }
          fillValueByAnnotaion(toObject, f, rating.getRatingKey(), ratingValue);
        });
      }
    }
  }

  private static void fillValueByAnnotaion(Object target, Field field, String ratingKey, Double ratingValue) {
    try {
      Value anno = field.getAnnotation(Value.class);
      if (anno == null) {
        System.out.println("FieldName: " + field.getName() + " Not define annotation Value");
        return;
      }
      if (anno.value().equals(ratingKey)) {
        field.setAccessible(true);
        field.set(target, ratingValue);
      }
    } catch (IllegalAccessException e) {
      System.out.println("Fail to set value: " + ratingValue + " to annotation @Value(name=" + ratingKey + ") - " + e.getMessage());
    }
  }
}
