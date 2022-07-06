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
public class KeyOfficer {
  @Id // @Id for match
  private Long no;

  private String ticker;

  private String name;

  private String position;

  private Double ownPercent;

  @Step
  public List<KeyOfficer> getByTicker(String ticker, String lang, int page, int size) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT CAST(Row_number() OVER(ORDER BY C.Quantity DESC) AS INT) AS no ");
    if ("en".equals(lang)) {
      queryBuilder.append(", N.en_FullName  AS Name ");
      queryBuilder.append(", P.en_PositionName AS position ");
    } else {
      queryBuilder.append(", N.FullName as Name ");
      queryBuilder.append(", P.PositionName AS position ");
    }
    queryBuilder.append("     , C.OrganCode AS Ticker ");
    queryBuilder.append("     , CAST( C.Quantity AS FLOAT) AS CPCanhan ");
    queryBuilder.append("     , CAST( C.Percentage AS FLOAT) AS ownPercent ");
    queryBuilder.append(" FROM stx_cpf_IndividualShareHolder C ");
    queryBuilder.append(" LEFT JOIN stx_cpf_Person N ");
    queryBuilder.append("   ON C.PersonId = N.PersonId  ");
    queryBuilder.append("  LEFT JOIN ( ");
    queryBuilder.append("   SELECT PersonId , en_PositionName, PositionName ");
    queryBuilder.append("   FROM stx_cpf_Individual_Position O ");
    queryBuilder.append("   LEFT JOIN stx_mst_Position  P ");
    queryBuilder.append("     ON O.PositionId = P.PositionId ");
    queryBuilder.append("   WHERE O.OrganCode = :ticker ");
    queryBuilder.append("     AND O.ResisnationDate IS NULL ");
    queryBuilder.append(" )  P ");
    queryBuilder.append(" ON C.PersonId = P.PersonId ");
    queryBuilder.append(" WHERE C.organCode = :ticker  ");
    queryBuilder.append(" ORDER BY C.Quantity DESC ");
    List<KeyOfficer> listResult = new ArrayList<>();
    try {
      List<Map<String, Object>> result = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", TickerBasic.getTickerBasic(ticker).getOrganCode())
        .setFirstResult(page * size)
        .setMaxResults(size)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

      result.stream().forEach(map -> {
        KeyOfficer keyOfficer = KeyOfficer.builder()
          .no(map.get("no") == null ? null : Long.valueOf(map.get("no").toString()))
          .ticker((String) map.get("Ticker"))
          .name((String) map.get("Name"))
          .position((String) map.get("position"))
          .ownPercent((Double)map.get("ownPercent"))
          .build();
        listResult.add(keyOfficer);
      });
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return listResult;
  }
}
