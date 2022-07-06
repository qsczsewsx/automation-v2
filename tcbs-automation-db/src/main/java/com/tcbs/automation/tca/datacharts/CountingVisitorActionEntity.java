package com.tcbs.automation.tca.datacharts;

import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@Table(name = "Counting_VA_byDate")
public class CountingVisitorActionEntity {
  private Date dateReport;
  private Integer countVA;

  public static List<HashMap<String, Object>> getByTicker(String ticker, String fromDate, String toDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("  SELECT ticker, dateReport, countVA");
    queryStringBuilder.append("  FROM Smy_mtm_interestedticker ");
    queryStringBuilder.append("  WHERE Ticker = :ticker and dateReport >= :fromDate and dateReport <= :toDate ");
    queryStringBuilder.append("  ORDER BY dateReport ASC ");

    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", ticker)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Basic
  @Column(name = "dateReport")
  public Date getDateReport() {
    return dateReport;
  }

  public void setDateReport(Date dateReport) {
    this.dateReport = dateReport;
  }

  @Basic
  @Column(name = "countVA")
  public Integer getCountVA() {
    return countVA;
  }

  public void setCountVA(Integer countVA) {
    this.countVA = countVA;
  }
}
