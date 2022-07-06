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
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Data
@Entity
@Table(name = "PE_PORTFOLIO_PRESENT")
public class PePortfolioPresentEntity {
  @Column(name = "ID")
  private long id;
  @Column(name = "ACCOUNT_ID")
  private String accountId;
  @Id
  @Column(name = "TICKER")
  private String ticker;
  @Column(name = "REPORT_DATE")
  private Date reportDate;
  @Column(name = "OPEN_VOLUME")
  private Double openVolume;
  @Column(name = "REF_PRICE")
  private Double refPrice;
  @Column(name = "OPEN_VALUE")
  private Long openValue;
  @Column(name = "BUYING_VOLUME")
  private Double buyingVolume;
  @Column(name = "BUYING_VALUE")
  private Long buyingValue;
  @Column(name = "SELLING_VOLUME")
  private Double sellingVolume;
  @Column(name = "SELLING_VALUE")
  private Long sellingValue;
  @Column(name = "CLOSE_VOLUME")
  private Double closeVolume;
  @Column(name = "CLOSE_PRICE")
  private Double closePrice;
  @Column(name = "CLOSE_VALUE")
  private Long closeValue;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Step
  public static List<PePortfolioPresentEntity> getPortfolio(String reportDate) {
    Query<PePortfolioPresentEntity> query = IData.idataDbConnection.getSession()
      .createQuery("from PePortfolioPresentEntity WHERE reportDate = to_date(:reportDate, 'yyyy-MM-dd') ", PePortfolioPresentEntity.class);
    query.setParameter("reportDate", reportDate);
    List<PePortfolioPresentEntity> list = query.getResultList();
    IData.idataDbConnection.closeSession();
    return list;
  }


  public static List<HashMap<String, Object>> sumUpPortfolio(String reportDate, String preWorkingDay) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" SELECT ");
    queryBuilder.append(" NVL(pps.ACCOUNT_ID, pts.ACCOUNT_ID) AS ACCOUNT_ID ");
    queryBuilder.append(" 		, :reportDate AS REPORT_DATE");
    queryBuilder.append(" 		, NVL(pps.TICKER, pts.TICKER) AS TICKER ");
    queryBuilder.append("       , NVL(pps.CLOSE_VOLUME, 0) AS OPEN_VOLUME ");
    queryBuilder.append(" 		, NVL(pts.BUYING_VOLUME, 0) AS BUYING_VOLUME ");
    queryBuilder.append(" 		, NVL(pts.BUYING_VALUE, 0) AS BUYING_VALUE ");
    queryBuilder.append(" 		, NVL(pts.SELLING_VOLUME, 0) AS SELLING_VOLUME ");
    queryBuilder.append(" 		, NVL(pts.SELLING_VALUE, 0) AS SELLING_VALUE ");
    queryBuilder.append(" 		, (NVL(pps.CLOSE_VOLUME, 0) + NVL(pts.BUYING_VOLUME, 0) - NVL(pts.SELLING_VOLUME, 0)) AS CLOSE_VOLUME ");
    queryBuilder.append(" 		, SYSDATE");
    queryBuilder.append(" 		, CASE WHEN pts.COGS_PER_UNIT IS NULL THEN pps.COGS_PER_UNIT");
    queryBuilder.append(" 		      ELSE NVL(pts.COGS_PER_UNIT, 0) END AS COGS_PER_UNIT");
    queryBuilder.append(" 		, CASE WHEN pts.COGS_PER_UNIT IS NULL THEN pps.COGS_PER_UNIT");
    queryBuilder.append(" 		      ELSE NVL(pts.COGS_PER_UNIT, 0) END AS COGS_PER_UNIT_ADJ");
    queryBuilder.append(" FROM PE_PORTFOLIO_SNAPSHOT pps ");
    queryBuilder.append(" FULL OUTER JOIN PE_TRANSACTION_SUMMARY pts ");
    queryBuilder.append(" ON pts.ACCOUNT_ID = pps.ACCOUNT_ID ");
    queryBuilder.append(" AND pts.TICKER = pps.TICKER ");
    queryBuilder.append(" AND pts.TRADING_DATE = pps.REPORT_DATE + (to_date(:reportDate, 'yyyy-MM-dd') - to_date(:preWorkingDay, 'yyyy-MM-dd')) ");
    queryBuilder.append(" WHERE (pts.TRADING_DATE = to_date(:reportDate, 'yyyy-MM-dd') OR pps.REPORT_DATE = to_date(:preWorkingDay, 'yyyy-MM-dd')) ");
    queryBuilder.append(" AND (pps.CLOSE_VOLUME > 0 OR pts.BUYING_VOLUME > 0 OR pts.SELLING_VOLUME > 0) ");


    List<HashMap<String, Object>> listResult = IData.idataDbConnection.getSession().createNativeQuery(queryBuilder.toString())
      .setParameter("reportDate", reportDate)
      .setParameter("preWorkingDay", preWorkingDay)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    IData.idataDbConnection.closeSession();
    return listResult;
  }
}
