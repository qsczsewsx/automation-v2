package com.tcbs.automation.config.idatatcanalysis.performance;

import com.tcbs.automation.config.idatatcanalysis.IData;
import lombok.Data;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;
import java.util.List;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@Data
@Entity
@Table(name = "PE_TRANSACTION_SUMMARY")
public class PeTransactionSummaryEntity {
  @Column(name = "ACCOUNT_ID")
  private String accountId;
  @Id
  @Column(name = "TICKER")
  private String ticker;
  @Column(name = "TRADING_DATE")
  private Date tradingDate;
  @Column(name = "BUYING_VOLUME")
  private Double buyingVolume;
  @Column(name = "BUYING_VALUE")
  private Double buyingValue;
  @Column(name = "SELLING_VOLUME")
  private Double sellingVolume;
  @Column(name = "SELLING_VALUE")
  private Double sellingValue;
  @Column(name = "COGS_PER_UNIT")
  private Double cogsPerUnit;

  @Step
  public static List<PeTransactionSummaryEntity> getTransaction(String reportDate) {
    Query<PeTransactionSummaryEntity> query = IData.idataDbConnection.getSession().createQuery(
      "from PeTransactionSummaryEntity t WHERE t.tradingDate = to_date(:tradingDate, 'yyyy-MM-dd') ", PeTransactionSummaryEntity.class);
    query.setParameter("tradingDate", reportDate);
    List<PeTransactionSummaryEntity> list = query.getResultList();
    IData.idataDbConnection.closeSession();
    return list;
  }

  @Step("delete data by key")
  public static void deleteByTxnDate(Date tnxDate) {
    Session session = IData.idataDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<PeTransactionSummaryEntity> query = session.createQuery(
      "DELETE FROM PeTransactionSummaryEntity i WHERE i.tradingDate=:date"
    );
    query.setParameter("date", tnxDate);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
