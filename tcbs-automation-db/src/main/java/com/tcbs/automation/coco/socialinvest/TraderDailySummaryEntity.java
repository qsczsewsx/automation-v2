package com.tcbs.automation.coco.socialinvest;

import com.tcbs.automation.coco.CocoConnBridge;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "TRADER_DAILY_SUMMARY")
public class TraderDailySummaryEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private long id;

  @Column(name = "TRADER_ID")
  private Long traderId;

  @Column(name = "TCBSID")
  private String tcbsid;

  @Column(name = "CUSTODY_ID")
  private String custodyId;

  @Column(name = "REPORT_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date reportDate;

  @Column(name = "TOTAL_AUM")
  private Double totalAum;

  @Column(name = "NEW_COPY")
  private Long newCopy;

  @Column(name = "STOP_COPY")
  private Long stopCopy;

  @Column(name = "NEW_FOLLOW")
  private Long newFollow;

  @Column(name = "UNFOLLOW")
  private Long unfollow;

  @Column(name = "COPIER")
  private Long copier;

  @Column(name = "COPIER_CHANGE_IN_MONTH")
  private Long changeInMonth;

  @Column(name = "FOLLOWER")
  private Long follower;

  @Column(name = "VIEW_IN_DAY")
  private Long viewInDay;

  @Column(name = "VIEW_IN_30DAYS")
  private Long viewIn30Days;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CREATED_TIME", updatable = false, insertable = false)
  private Date createdTime;

  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "UPDATED_TIME", insertable = false)
  private Date updatedTime;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "RANK_CODE", nullable = true)
  private RankEntity rank;

  @Column(name = "EQUITY")
  private Double equity;

  public static List<TraderDailySummaryEntity> findAllByTcbsidAndReportDateBetweenOrderByReportDateAsc(String tcbsId, Date startDate, Date endDate) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();

    Query<TraderDailySummaryEntity> query = session.createQuery(
      "from TraderDailySummaryEntity s " +
        "where s.tcbsid = :tcbsId " +
        "and s.reportDate between :startDate and :endDate " +
        "order by s.reportDate asc"
    );
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("startDate", startDate);
    query.setParameter("endDate", endDate);
    List<TraderDailySummaryEntity> res = query.getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();
    return res;
  }

  public static List<TraderDailySummaryEntity> findAllByTraderIdAndReportDateBetweenOrderByReportDateAsc(Long traderId, Date startDate, Date endDate) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();

    Query<TraderDailySummaryEntity> query = session.createQuery(
      "from TraderDailySummaryEntity s " +
        "where s.traderId = :traderId " +
        "and s.reportDate between :startDate and :endDate " +
        "order by s.reportDate asc"
    );
    query.setParameter("traderId", traderId);
    query.setParameter("startDate", startDate);
    query.setParameter("endDate", endDate);
    List<TraderDailySummaryEntity> res = query.getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();
    return res;
  }

  public static Double findTraderEquityByTraderIdOnLastestDay(Long traderId) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();

    StringBuilder fromDailySummary = new StringBuilder();
    fromDailySummary.append("select s.equity from TraderDailySummaryEntity s\n" +
      "where reportDate = (select max(reportDate) from TraderDailySummaryEntity\n)" +
      "and s.traderId = :traderId");

    Query<Double> query = session.createQuery(fromDailySummary.toString())
      .setParameter("traderId", traderId);
    Double result = query.getSingleResult();
    CocoConnBridge.socialInvestConnection.closeSession();
    return result;
  }
}
