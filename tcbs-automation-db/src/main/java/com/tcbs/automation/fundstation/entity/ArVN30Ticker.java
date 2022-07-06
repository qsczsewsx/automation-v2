package com.tcbs.automation.fundstation.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.fundstation.entity.TcAssets.sendSessionDBAssets;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "AR_VN30_TICKER")
public class ArVN30Ticker {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "TICKER")
  private String ticker;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "FREE_FLOAT")
  private Double freeFloat;

  @Column(name = "CAPPING")
  private Double capping;

  @Column(name = "MARKET_CAP")
  private Double marketCap;

  @Column(name = "ADJUSTED_MARKET_CAP")
  private Double adjustedMarketCap;

  @Column(name = "INDEX_WEIGHT")
  private Double indexWeight;

  @Column(name = "IMPORT_ID")
  private Integer importId;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  public static List<ArVN30Ticker> getActiveArPortfolioDetail() {
    Query<ArVN30Ticker> query = session.createQuery("from ArVN30Ticker where status = 'ACTIVE'");
    return query.getResultList();
  }

  public static List<ArVN30Ticker> getListArVn30TickerWithId(List<Integer> listId, List<String> statusList) {
    Query<ArVN30Ticker> query = session.createQuery("from ArVN30Ticker where id in :listId ");
    query.setParameter("listId", listId);
//    query.setParameter("statusList", statusList);
    return query.getResultList();
  }

  public static void insertListArVn30Ticker(String ticker, Double freeFloat, Double capping, String status, Double marketCap, Double adjustedMarketCap, Double indexWeight, Integer importId) {
    Session session2 = sendSessionDBAssets();
    ArVN30Ticker arVN30Ticker = new ArVN30Ticker();
    arVN30Ticker.setTicker(ticker);
    arVN30Ticker.setFreeFloat(freeFloat);
    arVN30Ticker.setStatus(status);
    arVN30Ticker.setMarketCap(marketCap);
    arVN30Ticker.setAdjustedMarketCap(adjustedMarketCap);
    arVN30Ticker.setIndexWeight(indexWeight);
    arVN30Ticker.setImportId(importId);
    session2.save(arVN30Ticker);
    session2.getTransaction().commit();
  }

  public static void deleteArVn30TickerWithImportId(Integer importId) {
    Session session2 = sendSessionDBAssets();
    Query<ArVN30Ticker> query = session.createQuery("delete ArVN30Ticker where importId =:importId ");
    query.setParameter("importId", importId);
    query.executeUpdate();
    session2.getTransaction().commit();
  }

  public static List<ArVN30Ticker> getTicketWithImportId(Integer importId) {
    Query<ArVN30Ticker> query = session.createQuery("from ArVN30Ticker where importId =:importId ");
    query.setParameter("importId", importId);
    return query.getResultList();
  }

}