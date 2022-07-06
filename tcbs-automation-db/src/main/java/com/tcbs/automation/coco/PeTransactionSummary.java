package com.tcbs.automation.coco;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "PE_TRANSACTION_SUMMARY")
@IdClass(PeTransactionSummaryId.class)
public class PeTransactionSummary implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ACCOUNT_ID")
  private String accountId;
  @Column(name = "TICKER")
  private String ticker;
  @Id
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
  @Column(name = "COGS_PER_UNIT_SELL")
  private Double cogsPerUnitSell;
  @Column(name = "USER_ID")
  private String userId;

  @Step("fetch dtb record")
  public static List<PeTransactionSummary> getSellTransactionSummaryByCustodyIdAndDate(String custodyId, String fromDate, String toDate) {
    Session session = CocoConnBridge.cocoConnection.getSession();

    Query<PeTransactionSummary> query = session.createQuery(" from PeTransactionSummary" +
      " where userId =:custodyId and tradingDate >= to_date(:fromDate, 'yyyy-MM-dd') and" +
      " tradingDate <= to_date(:toDate, 'yyyy-MM-dd') and sellingVolume > 0" +
      " order by tradingDate", PeTransactionSummary.class);
    query.setParameter("custodyId", custodyId);
    query.setParameter("fromDate", fromDate);
    query.setParameter("toDate", toDate);

    List<PeTransactionSummary> list = query.getResultList();

    return list;
  }
}
