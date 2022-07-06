package com.tcbs.automation.tca.company;


import com.tcbs.automation.tca.TcAnalysis;
import com.tcbs.automation.tca.ticker.TickerBasic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.collections.CollectionUtils;
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
public class ShareHolder {
  @Id // @Id for match
  private Long no;

  private String ticker;

  private String name;

  private String eName;

  private Double qttyOwnedShares;

  private Integer show;

  private Double ownPercent;

  @Step
  public List<ShareHolder> getByTicker(final String ticker) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append("SELECT Row_number() OVER(ORDER BY Show DESC, QttyOwnedShares DESC) AS no, * ");
    queryBuilder.append("FROM ");
    queryBuilder.append("( ");
    queryBuilder.append("    SELECT CASE WHEN (C.Percentage >= 0.05) THEN 1 ELSE 0 END AS Show  ");
    queryBuilder.append("    , C.MasterOrganCode AS Ticker ");
    queryBuilder.append("    , O.OrganName AS Name ");
    queryBuilder.append("    , ISNULL(O.en_OrganName, '')  as eName ");
    queryBuilder.append("    , CAST(Percentage AS FLOAT) AS QttyOwnedShares ");
    queryBuilder.append("  FROM stx_cpf_CorporateShareHolder C ");
    queryBuilder.append("  INNER JOIN stx_cpf_Organization O ");
    queryBuilder.append("    ON C.MasterOrganCode = O.OrganCode ");
    queryBuilder.append("  WHERE C.SlaveOrganCode = :ticker  ");
    queryBuilder.append("    AND C.Percentage IS NOT NULL  ");
    queryBuilder.append("    AND C.Percentage <> 1 ");
    queryBuilder.append("    AND C.Percentage > 0  ");
    queryBuilder.append("    AND O.OrganName IS NOT NULL ");
    queryBuilder.append("  UNION ALL ");
    queryBuilder.append("  SELECT CASE WHEN (NULLIF(PublicDate , '') is null AND Percentage >= 0.0001 ) THEN 1 ELSE 0 END AS Show ");
    queryBuilder.append("    , C.OrganCode AS Ticker ");
    queryBuilder.append("    , O.FullName AS Name ");
    queryBuilder.append("    , ISNULL(O.en_FullName , '')  as eName ");
    queryBuilder.append("    , CAST(Percentage AS FLOAT)  AS QttyOwnedShares ");
    queryBuilder.append("  FROM stx_cpf_IndividualShareHolder C  ");
    queryBuilder.append("  INNER JOIN stx_cpf_Person O ");
    queryBuilder.append("    ON C.PersonId = O.PersonId ");
    queryBuilder.append("  WHERE c.OrganCode = :ticker  ");
    queryBuilder.append("    AND C.Percentage > 0  ");
    queryBuilder.append("    AND O.FullName IS NOT NULL");
    queryBuilder.append(") tblLarge ");
    queryBuilder.append("ORDER BY Show DESC, QttyOwnedShares DESC ");

    List<ShareHolder> listResult = new ArrayList<>();
    try {
      List<Map<String, Object>> result = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", TickerBasic.getTickerBasic(ticker).getOrganCode())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

      if (CollectionUtils.isNotEmpty(result)) {
        for (Map<String, Object> map : result) {
          ShareHolder shareHolder = ShareHolder.builder()
            .no(Long.valueOf(map.get("no").toString()))
            .show((Integer) map.get("Show"))
            .ticker((String) map.get("Ticker"))
            .name((String) map.get("Name"))
            .eName((String) map.get("eName"))
            .ownPercent((Double) map.get("QttyOwnedShares"))
            .build();
          listResult.add(shareHolder);
        }
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return listResult;
  }

}
