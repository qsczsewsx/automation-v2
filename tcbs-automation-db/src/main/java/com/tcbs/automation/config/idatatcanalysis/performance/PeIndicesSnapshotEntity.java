package com.tcbs.automation.config.idatatcanalysis.performance;

import com.tcbs.automation.config.idatatcanalysis.IData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import wiremock.org.apache.commons.collections4.CollectionUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Entity
@Table(name = "PE_INDICES_SNAPSHOT")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeIndicesSnapshotEntity {
  @Id
  @Column(name = "ACCOUNT_ID")
  private String accountId;
  @Column(name = "EOMONTH")
  private Timestamp eomonth;
  @Column(name = "PORTF_INDEX")
  private Double portfIndex;
  @Column(name = "PORTF_MONRET_FACTOR")
  private Double portfMonretFactor;
  @Column(name = "PORTF_MONRET_FACTOR_LN")
  private Double portfMonretFactorLn;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Step
  public static List<PeIndicesSnapshotEntity> getIndices(String accountId, String eomonth) {
    Query<PeIndicesSnapshotEntity> query = IData.idataDbConnection.getSession()
      .createQuery("from PeIndicesSnapshotEntity WHERE eomonth = to_date(:eomonth, 'yyyy-MM-dd') AND accountId = :accountId", PeIndicesSnapshotEntity.class);
    query.setParameter("eomonth", eomonth);
    query.setParameter("accountId", accountId);
    List<PeIndicesSnapshotEntity> list = query.getResultList();
    IData.idataDbConnection.closeSession();
    return list;
  }

  public static List<HashMap<String, Object>> sumUpIndices(String eomonthPreMonth, String reportDate) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" SELECT tlast.ACCOUNT_ID, tlast.REPORT_DATE ");
    queryBuilder.append("   , tlast.PORTF_INDEX ");
    queryBuilder.append("   , (tlast.PORTF_INDEX/preMonth.PORTF_INDEX) AS PORTF_MONRET_FACTOR ");
    queryBuilder.append("   , ln((tlast.PORTF_INDEX/preMonth.PORTF_INDEX)) AS PORTF_MONRET_FACTOR_LN ");
    queryBuilder.append(" FROM ( ");
    queryBuilder.append("   SELECT * ");
    queryBuilder.append("     FROM PE_PERFORMANCE_SNAPSHOT pps ");
    queryBuilder.append("   WHERE REPORT_DATE = to_date(:reportDate, 'yyyy-MM-dd') ");
    queryBuilder.append(" ) tlast ");
    queryBuilder.append(" LEFT JOIN ( ");
    queryBuilder.append("   SELECT * ");
    queryBuilder.append("     FROM PE_PERFORMANCE_SNAPSHOT pps ");
    queryBuilder.append("   WHERE REPORT_DATE = to_date(:eomonthPreMonth, 'yyyy-MM-dd') ");
    queryBuilder.append(" ) preMonth ON tlast.ACCOUNT_ID = preMonth.ACCOUNT_ID ");

    List<HashMap<String, Object>> listResult = IData.idataDbConnection.getSession().createNativeQuery(queryBuilder.toString())
      .setParameter("eomonthPreMonth", eomonthPreMonth)
      .setParameter("reportDate", reportDate)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    IData.idataDbConnection.closeSession();
    return listResult;
  }

  @Step("select portfolio indices")
  public static List<PeIndicesSnapshotEntity> getPortfolioIndices(String accountId) {
    String query = "SELECT * FROM PE_INDICES_SNAPSHOT WHERE ACCOUNT_ID = :accountId " +
      "OR ACCOUNT_ID = 'VNINDEX' " +
      "OR ACCOUNT_ID = 'tcbs_all_clients' ORDER BY EOMONTH";
    List<PeIndicesSnapshotEntity> listResult = new ArrayList<>();
    try {
      List<Map<String, Object>> resultIndices = IData.idataDbConnection.getSession().createNativeQuery(query)
        .setParameter("accountId", accountId).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      if (CollectionUtils.isNotEmpty(resultIndices)) {
        for (Map<String, Object> item : resultIndices) {
          PeIndicesSnapshotEntity peIndicesSnapshotEntity = PeIndicesSnapshotEntity.builder()
            .accountId(String.valueOf(item.get("ACCOUNT_ID")).trim())
            .eomonth((Timestamp) item.get("EOMONTH"))
            .portfIndex(Double.parseDouble(item.get("PORTF_INDEX").toString()))
            .portfMonretFactor(item.get("PORTF_MONRET_FACTOR") == null ? null : Double.parseDouble(item.get("PORTF_MONRET_FACTOR").toString()))
            .portfMonretFactorLn(item.get("PORTF_MONRET_FACTOR_LN") == null ? null : Double.parseDouble(item.get("PORTF_MONRET_FACTOR_LN").toString()))
            .createdDate((Timestamp) item.get("CREATED_DATE"))
            .build();
          listResult.add(peIndicesSnapshotEntity);
        }
      }
    } catch (Exception exception) {
      StepEventBus.getEventBus().testFailed(new AssertionError(exception.getMessage()));
    }
    return listResult;
  }

  @Override
  public String toString() {
    return "EOMONTH " + eomonth + " PORTF_INDEX " + portfIndex + " PORTF_MONRET_FACTOR " + portfMonretFactor + " PORTF_MONRET_FACTOR_LN " + portfMonretFactorLn;
  }
}
