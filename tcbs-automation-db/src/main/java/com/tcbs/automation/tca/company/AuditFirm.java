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
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AuditFirm {
  @Id // @Id for match
  private Long no;

  private String ticker;

  private String auditor;

  private Short yearReport;

  @Step
  public List<AuditFirm> getByTicker(String ticker, String lang, int page, int size) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT CAST(Row_number() OVER(ORDER BY a.yearReport DESC) AS INT) AS no, a.OrganCode as Ticker,  CAST(a.yearReport AS SMALLINT) AS YearReport ");
    if ("en".equals(lang)) {
      queryBuilder.append(", o.en_OrganName AS Auditor ");
    } else {
      queryBuilder.append(", o.OrganName Auditor ");
    }
    queryBuilder.append(" FROM stx_fsc_AuditOpinion A ");
    queryBuilder.append(" INNER JOIN stx_cpf_Organization O ");
    queryBuilder.append(" 	ON A.AuditorOrganCode = O.OrganCode  ");
    queryBuilder.append(" WHERE A.OrganCode = :ticker ");
    queryBuilder.append(" ORDER BY a.yearReport DESC ");

    List<AuditFirm> listResult = new ArrayList<>();
    try {
      List<Map<String, Object>> result = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", TickerBasic.getTickerBasic(ticker).getOrganCode())
        .setFirstResult(page * size)
        .setMaxResults(size)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      listResult = result.stream()
                    .map(map ->  AuditFirm.builder()
                          .no(map.get("no") == null ? null : Long.valueOf(map.get("no").toString()))
                          .ticker((String) map.get("Ticker"))
                          .auditor((String) map.get("Auditor"))
                          .yearReport((Short) map.get("YearReport"))
                          .build()
                    ).collect(Collectors.toList());
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return listResult;
  }
}