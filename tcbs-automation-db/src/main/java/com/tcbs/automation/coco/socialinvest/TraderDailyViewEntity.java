package com.tcbs.automation.coco.socialinvest;

import com.tcbs.automation.coco.CocoConnBridge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TRADER_DAILY_VIEW")
public class TraderDailyViewEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "TCBSID")
  private String tcbsid;

  @Column(name = "REPORT_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date reportDate;

  @Column(name = "VIEWS")
  private Long views;

  @CreationTimestamp
  @Column(name = "CREATED_TIME", updatable = false, insertable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdTime;

  @UpdateTimestamp
  @Column(name = "UPDATED_TIME", insertable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date updatedTime;

  @Column(name = "TRADER_ID")
  private Long traderId;

  public static List<TraderDailyViewEntity> findAllByTcbsidAndReportDateBetweenOrderByReportDateAsc(String tcbsId, Date startDate, Date endDate) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();

    Query<TraderDailyViewEntity> query = session.createQuery(
      "from TraderDailyViewEntity s " +
        "where s.tcbsid = :tcbsId " +
        "and s.reportDate between :startDate and :endDate " +
        "order by s.reportDate asc"
    );
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("startDate", startDate);
    query.setParameter("endDate", endDate);
    List<TraderDailyViewEntity> res = query.getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();
    return res;
  }

  public static List<TraderDailyViewEntity> findAllByTraderIdAndReportDateBetweenOrderByReportDateAsc(Long traderId, Date startDate, Date endDate) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();

    Query<TraderDailyViewEntity> query = session.createQuery(
      "from TraderDailyViewEntity s " +
        "where s.traderId = :traderId " +
        "and s.reportDate between :startDate and :endDate " +
        "order by s.reportDate asc"
    );
    query.setParameter("traderId", traderId);
    query.setParameter("startDate", startDate);
    query.setParameter("endDate", endDate);
    List<TraderDailyViewEntity> res = query.getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();
    return res;
  }
}