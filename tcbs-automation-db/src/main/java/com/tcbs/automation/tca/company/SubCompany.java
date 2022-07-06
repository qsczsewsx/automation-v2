package com.tcbs.automation.tca.company;


import com.tcbs.automation.tca.TcAnalysis;
import com.tcbs.automation.tca.ticker.TickerBasic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SubCompany {
  @Id // @Id for match
  private Long no;

  private String ticker;

  private String companyName;

  private Double ownPercent;

  @Step
  public List<SubCompany> getByTicker(String ticker, String lang, int page, int size) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT Row_number() OVER(ORDER BY ownPercent DESC, CompanyName) AS no, Ticker, OwnPercent ");
    if ("en".equals(lang)) {
      queryBuilder.append(", eCompanyName AS CompanyName ");
    } else {
      queryBuilder.append(", CompanyName ");
    }
    queryBuilder.append(" FROM (SELECT R.MasterOrganCode as Ticker ");
    queryBuilder.append("          , O.en_OrganName AS eCompanyName ");
    queryBuilder.append("          , O.OrganName AS CompanyName ");
    queryBuilder.append("          , cast (ISNULL(r.Percentage , 0.0 ) as float) AS OwnPercent ");
    queryBuilder.append("        FROM stx_cpf_CorporateShareHolder R ");
    queryBuilder.append("        INNER JOIN stx_cpf_organization O ");
    queryBuilder.append("          ON R.SlaveOrganCode = O.OrganCode  ");
    queryBuilder.append("        WHERE r.masterOrganCode = :ticker ");
    queryBuilder.append("          AND  r.Percentage <> 1) tbl ");
    queryBuilder.append("ORDER BY OwnPercent DESC ");

    List<SubCompany> listResult = new ArrayList<>();
    try {
      String organ = TickerBasic.getTickerBasic(ticker).getOrganCode();
      List<Map<String, Object>> result = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", organ)
        .setFirstResult(page * size)
        .setMaxResults(size)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

      result.stream().forEach(map -> {
        SubCompany subCompany = SubCompany.builder()
          .no(map.get("no") == null ? null : Long.valueOf(map.get("no").toString()))
          .ticker(organ)
          .companyName((String) map.get("CompanyName"))
          .ownPercent((Double) map.get("OwnPercent"))
          .build();
        listResult.add(subCompany);
      });
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return listResult;
  }

}
