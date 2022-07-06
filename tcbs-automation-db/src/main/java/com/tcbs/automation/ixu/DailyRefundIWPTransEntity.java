package com.tcbs.automation.ixu;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "DAILY_REFUND_IWP_TRANSACTION")
public class DailyRefundIWPTransEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;

  @Column(name = "TCBS_ID")
  private String tcbsId;

  @Column(name = "PERIOD")
  private ZonedDateTime period;

  @Column(name = "GROSS_VALUE")
  private BigDecimal grossValue;

  @Column(name = "ACTUAL_REFUND_POINT")
  private BigDecimal actualRefundPoint;

  @Column(name = "ISSUED_DATE")
  private ZonedDateTime issuedDate;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Step
  public static DailyRefundIWPTransEntity insert(DailyRefundIWPTransEntity entity) {
    Session session = ixuDbConnection.getSession();
    session.beginTransaction();
    session.save(entity);
    session.getTransaction().commit();
    ixuDbConnection.closeSession();
    return entity;
  }

  @Step
  public static List<DailyRefundIWPTransEntity> findByTcbsIdAndIssuedDate(String tcbsId, String issuedDate) {
    ixuDbConnection.getSession().clear();
    Query<DailyRefundIWPTransEntity> query =
      ixuDbConnection
        .getSession()
        .createQuery(
          "from DailyRefundIWPTransEntity a where a.tcbsId = :tcbsId and trunc(a.issuedDate) = TO_DATE(:issuedDate, 'YYYY-MM-DD')",
          DailyRefundIWPTransEntity.class);
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("issuedDate", issuedDate);
    List<DailyRefundIWPTransEntity> refundIWPTransEntityList = query.getResultList();
    ixuDbConnection.closeSession();
    return refundIWPTransEntityList;
  }
}
