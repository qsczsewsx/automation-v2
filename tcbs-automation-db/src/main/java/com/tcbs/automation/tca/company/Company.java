package com.tcbs.automation.tca.company;


import com.tcbs.automation.tca.TcAnalysis;
import com.tcbs.automation.tca.ticker.TickerBasic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Company {
  
  private String ticker;

  private Integer typeID;

  private String industryId;

  private Float shareCirculate;

  @Id
  private String companyName;

  private String companyProfile;

  private String historyDev;

  private String companyPromise;

  private String businessRisks;

  private String keyDevelopments;

  private String businessStrategies;


  public Company getByTicker(String ticker, String lang) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT o.ticker ");
    if ("en".equals(lang)) {
      queryBuilder.append(", o.EN_OrganName AS companyName ");
      queryBuilder.append(", C.EN_CompanyProfile AS companyProfile ");
      queryBuilder.append(", C.EN_History AS historyDev ");
      queryBuilder.append(", I.EN_Promise AS companyPromise ");
      queryBuilder.append(", I.EN_BusinessRisks AS businessRisks ");
      queryBuilder.append(", C.en_BusinessLine AS keyDevelopments ");
      queryBuilder.append(", C.en_BusinessStategies AS businessStrategies ");
    } else {
      queryBuilder.append(", O.OrganName AS companyName ");
      queryBuilder.append(", C.CompanyProfile AS companyProfile ");
      queryBuilder.append(", C.History AS historyDev ");
      queryBuilder.append(", I.Promise AS companyPromise ");
      queryBuilder.append(", I.BusinessRisks AS businessRisks ");
      queryBuilder.append(", C.BusinessLine AS keyDevelopments ");
      queryBuilder.append(", C.BusinessStategies AS businessStrategies ");
    }
    queryBuilder.append(" FROM stx_cpf_Organization o ");
    queryBuilder.append(" LEFT JOIN stx_cpf_CompanyOverview C ");
    queryBuilder.append(" 	ON O.OrganCode = C.OrganCode ");
    queryBuilder.append(" LEFT JOIN stx_cpf_CompanyInsight I ");
    queryBuilder.append(" 	ON O.OrganCode = I.OrganCode ");
    queryBuilder.append(" WHERE O.OrganCode = :ticker ");

    try {
      List<Map<String, Object>> result = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", TickerBasic.getTickerBasic(ticker).getOrganCode())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      if (CollectionUtils.isNotEmpty(result)) {
        Map<String, Object> item = result.get(0);
        return Company.builder()
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

