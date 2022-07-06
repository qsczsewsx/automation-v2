package com.tcbs.automation.config.idatatcanalysis.performance;

import com.tcbs.automation.config.idatatcanalysis.IData;
import lombok.Data;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

@Data
@Entity
@Table(name = "PE_PERFORMANCE_PRESENT")
public class PePerformancePresentEntity {
  @Column(name = "ID")
  private long id;
  @Id
  @Column(name = "ACCOUNT_ID")
  private String accountId;
  @Column(name = "REPORT_DATE")
  private Date reportDate;
  @Column(name = "DAILY_PROFIT")
  private Long dailyProfit;
  @Column(name = "ACC_PROFIT")
  private Long accProfit;
  @Column(name = "NUMERATOR")
  private Long numerator;
  @Column(name = "DENOMINATOR")
  private Long denominator;
  @Column(name = "PORTF_INDEX")
  private Double portfIndex;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Step
  public static List<PePerformancePresentEntity> getPerformance(String reportDate) {
    Query<PePerformancePresentEntity> query = IData.idataDbConnection.getSession()
      .createQuery("from PePerformancePresentEntity WHERE reportDate = to_date(:reportDate, 'yyyy-MM-dd') ", PePerformancePresentEntity.class);
    query.setParameter("reportDate", reportDate);
    List<PePerformancePresentEntity> list = query.getResultList();
    IData.idataDbConnection.closeSession();
    return list;
  }


  public static List<HashMap<String, Object>> sumUpPerformance(String reportDate, String preWorkingDay) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" SELECT ppp.ACCOUNT_ID, ppp.REPORT_DATE, ppp.NUMERATOR, ppp.DENOMINATOR, ppp.NUMERATOR - ppp.DENOMINATOR AS DAILY_PROFIT ");
    queryBuilder.append("   , CASE WHEN ppp.DENOMINATOR = 0 THEN ");
    queryBuilder.append("       NVL(pps.PORTF_INDEX, 100) ");
    queryBuilder.append("   ELSE ");
    queryBuilder.append("       (ppp.NUMERATOR/ppp.DENOMINATOR) * NVL(pps.PORTF_INDEX, 100) ");
    queryBuilder.append("   END AS PORTF_INDEX ");
    queryBuilder.append("   , ppp.NUMERATOR - ppp.DENOMINATOR + NVL(pps.ACC_PROFIT, 0) AS ACC_PROFIT ");
    queryBuilder.append("   , ppp.TOTAL_COGS");
    queryBuilder.append("   , ppp.TOTAL_ESTIMATED_REVENUE ");
    queryBuilder.append("   , cast(CURRENT_TIMESTAMP as DATE)");
    queryBuilder.append(" FROM ( ");
    queryBuilder.append("     SELECT ppp.ACCOUNT_ID, ppp.REPORT_DATE ");
    queryBuilder.append("     , SUM(ppp.CLOSE_VALUE + ppp.SELLING_VALUE) AS NUMERATOR ");
    queryBuilder.append("     , SUM(ppp.OPEN_VALUE  + ppp.BUYING_VALUE) AS DENOMINATOR ");
    queryBuilder.append("     , SUM(ppp.COGS_PER_UNIT_ADJ  * ppp.CLOSE_VOLUME) AS TOTAL_COGS ");
    queryBuilder.append("     , SUM(ppp.CLOSE_VALUE) AS TOTAL_ESTIMATED_REVENUE ");
    queryBuilder.append("     FROM PE_PORTFOLIO_PRESENT ppp ");
    queryBuilder.append("     WHERE REPORT_DATE = to_date(:reportDate, 'yyyy-MM-dd') ");
    queryBuilder.append("     GROUP BY ACCOUNT_ID, REPORT_DATE ");
    queryBuilder.append(" ) ppp ");
    queryBuilder.append(" LEFT JOIN PE_PERFORMANCE_SNAPSHOT pps ");
    queryBuilder.append(" ON ppp.ACCOUNT_ID = pps.ACCOUNT_ID AND ppp.REPORT_DATE = pps.REPORT_DATE + (to_date(:reportDate, 'yyyy-MM-dd') - to_date(:preWorkingDay, 'yyyy-MM-dd')) ");

    List<HashMap<String, Object>> listResult = IData.idataDbConnection.getSession().createNativeQuery(queryBuilder.toString())
      .setParameter("reportDate", reportDate)
      .setParameter("preWorkingDay", preWorkingDay)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    IData.idataDbConnection.closeSession();

    return listResult;
  }
}
