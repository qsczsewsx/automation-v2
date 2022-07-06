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

import java.sql.*;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "TRANSFER_IXU_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TransferIxuTransactionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "SENDER_TCBS_ID")
  private String tcbsIdTransfer;
  @Column(name = "RECEIVER_TCBS_ID")
  private String tcbsIdReward;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "POINT")
  private String point;
  @Column(name = "TEMPLATE")
  private String template;
  @Column(name = "TEMPLATE_NOTIFY")
  private String templateNotify;
  @Column(name = "MESSAGE")
  private String message;
  @Column(name = "NOTIFY")
  private String notify;

  private static final String TCBS_ID = "tcbsId";

  @Step
  public static TransferIxuTransactionEntity getTransactionWithTcbsIdSender(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<TransferIxuTransactionEntity> query = ixuDbConnection.getSession().createQuery(
      "from TransferIxuTransactionEntity a where a.tcbsIdTransfer = :tcbsId", TransferIxuTransactionEntity.class);
    query.setParameter(TCBS_ID, tcbsId);
    TransferIxuTransactionEntity transaction = query.getSingleResult();
    ixuDbConnection.closeSession();
    return transaction;
  }

  @Step
  public static TransferIxuTransactionEntity getTransactionWithTcbsIdReward(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<TransferIxuTransactionEntity> query = ixuDbConnection.getSession().createQuery(
      "from TransferIxuTransactionEntity a where a.tcbsIdReward = :tcbsId", TransferIxuTransactionEntity.class);
    query.setParameter(TCBS_ID, tcbsId);
    TransferIxuTransactionEntity transaction = query.getSingleResult();
    ixuDbConnection.closeSession();
    return transaction;
  }


  @Step
  public static void deleteByTcbsId(String tcbsId) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    Transaction transaction = session.beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from TransferIxuTransactionEntity a where a.tcbsIdReward = :id or a.tcbsIdTransfer = :id"
    );
    query.setParameter("id", tcbsId);
    query.executeUpdate();
    transaction.commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void saveOrUpdateTransaction(TransferIxuTransactionEntity entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().saveOrUpdate(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<TransferIxuTransactionEntity> getAllBySenderTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query<TransferIxuTransactionEntity> query = ixuDbConnection.getSession().createQuery(
      "from TransferIxuTransactionEntity a where a.tcbsIdTransfer =:tcbsId order by a.id desc", TransferIxuTransactionEntity.class);
    query.setParameter(TCBS_ID, tcbsId);
    List<TransferIxuTransactionEntity> results = query.getResultList();
    ixuDbConnection.closeSession();
    return results;
  }

  @Step
  public static List<TransferIxuTransactionEntity> getAllByReceiverTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query<TransferIxuTransactionEntity> query = ixuDbConnection.getSession().createQuery(
      "from TransferIxuTransactionEntity a where a.tcbsIdReward =:tcbsId order by a.id desc", TransferIxuTransactionEntity.class);
    query.setParameter(TCBS_ID, tcbsId);
    List<TransferIxuTransactionEntity> results = query.getResultList();
    ixuDbConnection.closeSession();
    return results;
  }

  @Step
  public static List<TransferIxuTransactionEntity> getTheLatest() {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query<TransferIxuTransactionEntity> query = ixuDbConnection.getSession().createQuery(
      "from TransferIxuTransactionEntity a order by a.id desc", TransferIxuTransactionEntity.class);
    List<TransferIxuTransactionEntity> results = query.getResultList();
    ixuDbConnection.closeSession();
    return results;
  }

  @Step
  public static void clearAll() {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    javax.persistence.Query query = ixuDbConnection.getSession().createQuery(
      "delete from TransferIxuTransactionEntity where id is not null");
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }
}
