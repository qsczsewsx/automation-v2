package com.tcbs.automation.fundstation.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "NET_INFLOW")
public class NetInFlow {
  public static Session session;
  static Map<String, List<NetInFlow>> mapNetInFlow = new HashMap<>();
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "ID")
  private int id;
  @Column(name = "START_DATE")
  @Temporal(TemporalType.DATE)
  private Date startDate;
  @Column(name = "END_DATE")
  @Temporal(TemporalType.DATE)
  private Date endDate;
  @Column(name = "NET_INFLOW")
  private Double netInFlow;
  @Column(name = "PORTFOLIO_CODE")
  private String portfolioCode;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;
  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  public NetInFlow(Integer id, Date startDate, Date endDate, Double netInflow, String portfolioCode, String status, Date createdTimestamp, Date updatedTimestamp) {
    this.id = id;
    this.startDate = startDate;
    this.endDate = endDate;
    this.netInFlow = netInflow;
    this.portfolioCode = portfolioCode;
    this.status = status;
    this.createdTimestamp = createdTimestamp;
    this.updatedTimestamp = updatedTimestamp;
  }

  public static List<NetInFlow> getNetInFlow(String portfolioCode, Date today) {
    if (!mapNetInFlow.containsKey(portfolioCode)) {
      Query<NetInFlow> query = session.createQuery("from NetInFlow where portfolioCode =:portfolioCode and status = 'ACTIVE' and endDate >=:today");
      query.setParameter("portfolioCode", portfolioCode);
      query.setParameter("today", today);
      mapNetInFlow.put(portfolioCode, query.getResultList());
    }

    return mapNetInFlow.get(portfolioCode);
  }

  public static List<NetInFlow> getNetInflowOfPortfolio(String portfolioCode) {
    Query<NetInFlow> query = session.createQuery("from NetInFlow where portfolioCode =:portfolioCode and status='ACTIVE'");
    query.setParameter("portfolioCode", portfolioCode);
    List<NetInFlow> a = query.getResultList();
    return a;
  }

  public static List<NetInFlow> getActiveNetInflowOfPortfolio(String portfolioCode, String status) {
    session.clear();
    Query<NetInFlow> query = session.createQuery("from NetInFlow where portfolioCode =:portfolioCode and status =:status");
    query.setParameter("portfolioCode", portfolioCode);
    query.setParameter("status", status);
    List<NetInFlow> a = query.getResultList();
    return a;
  }

  public static List<NetInFlow> getActiveNetInflowOfPortfolio(String portfolioCode, String status, Date endDate) {
    Query<NetInFlow> query = session.createQuery("from NetInFlow where portfolioCode =:portfolioCode and status =:status and endDate >=: endDate");
    query.setParameter("portfolioCode", portfolioCode);
    query.setParameter("status", status);
    query.setParameter("endDate", endDate);
    List<NetInFlow> a = query.getResultList();
    return a;
  }

  public static void deleteNetInflowOfPortfolio_FromDate(String portfolioCode, String status, Date endDate) {
    Query<NetInFlow> query = session.createQuery("delete NetInFlow where portfolioCode =:portfolioCode and status =: status and endDate >=: endDate");
    query.setParameter("portfolioCode", portfolioCode);
    query.setParameter("status", status);
    query.setParameter("endDate", endDate);
    query.executeUpdate();
  }

  public static void deleteActiveNetInflowOfPortfolio(String portfolioCode, String status) {

    Query<NetInFlow> query = session.createQuery("delete NetInFlow where portfolioCode =:portfolioCode and status =: status ");
    query.setParameter("portfolioCode", portfolioCode);
    query.setParameter("status", status);

    int row = query.executeUpdate();
    session.flush();
    session.getTransaction().commit();
    System.out.println(row);
  }

  public static void deleteNetInflowOfPortfolio(String portfolioCode) {
    Query<NetInFlow> query = session.createQuery("delete NetInFlow where portfolioCode =:portfolioCode");
    query.setParameter("portfolioCode", portfolioCode);

    query.executeUpdate();
  }
}
