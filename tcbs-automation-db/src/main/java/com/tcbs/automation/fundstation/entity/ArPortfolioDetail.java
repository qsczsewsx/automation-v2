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
@Table(name = "AR_PORTFOLIO_DETAIL")
public class ArPortfolioDetail {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private int id;

  @Column(name = "TICKER")
  private String ticker;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "TICKER_ID")
  private Integer tickerId;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  public static List<ArPortfolioDetail> getActiveArPortfolioDetail() {
    Query<ArPortfolioDetail> query = session.createQuery("from ArPortfolioDetail where status = 'ACTIVE'");
    return query.getResultList();
  }

  public static void insertArPortfolioDetail(String ticker, String status, Integer tickerId) {
    Session session2 = sendSessionDBAssets();
    ArPortfolioDetail arVN30Ticker = new ArPortfolioDetail();
    arVN30Ticker.setTicker(ticker);
    arVN30Ticker.setStatus(status);
    arVN30Ticker.setTickerId(tickerId);
    session2.save(arVN30Ticker);
    session2.getTransaction().commit();
  }

}