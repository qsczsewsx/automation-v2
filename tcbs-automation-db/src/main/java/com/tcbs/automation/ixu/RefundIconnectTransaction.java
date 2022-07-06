package com.tcbs.automation.ixu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "REFUND_ICONNECT_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RefundIconnectTransaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;
  @Column(name = "ORDER_ID")
  private Long orderId;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "POINT")
  private Double point;
  @Column(name = "ORIGINAL_CONTRACT")
  private String originalContract;
  @Column(name = "RULE_OUTPUT")
  private String ruleOutput;
  @Column(name = "ISSUED_DATE")
  private java.sql.Timestamp issuedDate;
  @Column(name = "CREATED_DATE")
  private java.sql.Timestamp createdDate;

  @Step
  public static void create(RefundIconnectTransaction entity) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    Transaction transaction = session.beginTransaction();
    session.save(entity);
    transaction.commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByOrderId(Long id) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    Transaction transaction = session.beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from RefundIconnectTransaction a where a.orderId = :id"
    );
    query.setParameter("id", id);
    query.executeUpdate();
    transaction.commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static RefundIconnectTransaction findRecordByOrderId(Long orderId, String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<RefundIconnectTransaction> query = ixuDbConnection.getSession().createQuery(
      "from RefundIconnectTransaction tb where tb.orderId = :orderId and tb.tcbsId = :tcbsId"
    );
    query.setParameter("orderId", orderId);
    query.setParameter("tcbsId", tcbsId);
    RefundIconnectTransaction refundIwpTransaction = query.getSingleResult();
    ixuDbConnection.closeSession();
    if (refundIwpTransaction == null) {
      return null;
    }
    return refundIwpTransaction;
  }
}
