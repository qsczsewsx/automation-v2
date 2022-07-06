package com.tcbs.automation.stoxplus.stock;


import com.tcbs.automation.stoxplus.Stoxplus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

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
@Table(name = "stox_tb_Company")
public class Company {
  @Id
  private Long id;
  @Column(name = "Ticker")
  private String ticker;

  @Column(name = "TypeID")
  private Integer typeID;

  @Column(name = "IndustryID")
  private String industryId;

  @Column(name = "ShareCirculate")
  private Float shareCirculate;

  @Column(name = "shortName")
  private String companyName;

  @Column(name = "companyProfile")
  private String companyProfile;

  @Column(name = "historyDev")
  private String historyDev;

  @Column(name = "companyPromise")
  private String companyPromise;

  @Column(name = "BusinessRisk")
  private String businessRisks;

  @Column(name = "keyDevelopments")
  private String keyDevelopments;

  @Column(name = "businessStrategies")
  private String businessStrategies;

  public static Company getCompany(String ticker) {
    Query<Company> query = Stoxplus.stoxDbConnection.getSession().createQuery(
      "from com.tcbs.automation.stoxplus.stock.Company as cus "
        + " where  ticker = :ticker "
      , Company.class
    );
    query.setParameter("ticker", ticker);
    List<Company> results = query.getResultList();
    if (CollectionUtils.isNotEmpty(results)) {
      return results.get(0);
    }
    return null;
  }

  public Double getShareCirculateByTicker(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT ShareCirculate ");
    queryBuilder.append("FROM stox_tb_Company ");
    queryBuilder.append("WHERE ticker = :ticker ");

    try {
      List<Map<String, Object>> result = Stoxplus.stoxDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      if (CollectionUtils.isNotEmpty(result)) {
        Map<String, Object> item = result.get(0);
        return (Double) item.get("ShareCirculate");
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }


  public Company getByTicker(String ticker, String lang) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT id, ticker ");
    if ("en".equals(lang)) {
      queryBuilder.append(", EnglishName AS companyName ");
      queryBuilder.append(", eCompanyProfile AS companyProfile ");
      queryBuilder.append(", eHistoryDev AS historyDev ");
      queryBuilder.append(", eCompanyPromise AS companyPromise ");
      queryBuilder.append(", eBusinessRisk AS businessRisks ");
      queryBuilder.append(", eKeyDevelopments AS keyDevelopments ");
      queryBuilder.append(", eBusinessStrategies AS businessStrategies ");
    } else {
      queryBuilder.append(", shortName AS companyName ");
      queryBuilder.append(", companyProfile ");
      queryBuilder.append(", historyDev ");
      queryBuilder.append(", companyPromise ");
      queryBuilder.append(", businessRisk AS businessRisks ");
      queryBuilder.append(", keyDevelopments ");
      queryBuilder.append(", businessStrategies ");
    }
    queryBuilder.append("FROM stox_tb_Company ");
    queryBuilder.append("WHERE ticker = :ticker ");

    try {
      List<Map<String, Object>> result = Stoxplus.stoxDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      if (CollectionUtils.isNotEmpty(result)) {
        Map<String, Object> item = result.get(0);
        return Company.builder()
          .id(Long.valueOf(item.get("id").toString()))
          .ticker((String) item.get("ticker"))
          .companyName((String) item.get("companyName"))
          .companyProfile((String) item.get("companyProfile"))
          .historyDev((String) item.get("historyDev"))
          .companyPromise((String) item.get("companyPromise"))
          .businessRisks((String) item.get("businessRisks"))
          .keyDevelopments((String) item.get("keyDevelopments"))
          .businessStrategies((String) item.get("businessStrategies"))
          .build();
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

}

