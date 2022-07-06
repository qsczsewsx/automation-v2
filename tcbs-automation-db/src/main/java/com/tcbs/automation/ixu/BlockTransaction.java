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
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "BLOCK_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BlockTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "AMOUNT")
  private String amount;
  @Column(name = "TRANSACTION_ID")
  private String transactionId;
  @Column(name = "EXPIRED_DATE")
  private Timestamp expiredDate;
  @Column(name = "EXPIRED_ACTION")
  private String expiredAction;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "LAST_UPDATED_DATE")
  private Timestamp lastUpdatedDate;
  @Column(name = "DELETED")
  private String deleted;
  
  private static final String TCBS_ID = "tcbsId";

  @Step
  public static BlockTransaction getBlockTransactionByTcbsId(String tcbsId) {
    Query<BlockTransaction> query = ixuDbConnection.getSession().createQuery(
      "from BlockTransaction a where a.tcbsId=:tcbsId order by a.id desc",
      BlockTransaction.class);
    query.setParameter(TCBS_ID, tcbsId);
    List<BlockTransaction> result = query.getResultList();
    if (result.size() > 0) {
      ixuDbConnection.closeSession();
      return result.get(0);
    } else {
      ixuDbConnection.closeSession();
      return null;
    }
  }

  @Step
  public static void createBlockTransaction(BlockTransaction entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<BlockTransaction> findBlockTransactionByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    org.hibernate.query.Query<BlockTransaction> query = ixuDbConnection.getSession().createQuery(
      "from BlockTransaction a where a.tcbsId=:tcbsId ");
    query.setParameter(TCBS_ID, tcbsId);
    List<BlockTransaction> blockTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return blockTransactionList;
  }

  @Step
  public static List<BlockTransaction> findBlockTransactionById(String id) {
    ixuDbConnection.getSession().clear();
    org.hibernate.query.Query<BlockTransaction> query = ixuDbConnection.getSession().createQuery(
      "from BlockTransaction a where a.id=:id");
    query.setParameter("id", id);
    List<BlockTransaction> blockTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return blockTransactionList;
  }

  @Step
  public static List<BlockTransaction> findBlockTransactionByTransactionId(List<String> transactionId) {
    ixuDbConnection.getSession().clear();
    org.hibernate.query.Query<BlockTransaction> query = ixuDbConnection.getSession().createQuery(
      "from BlockTransaction a where a.transactionId in :transactionId order by a.id asc");
    query.setParameter("transactionId", transactionId);
    List<BlockTransaction> blockTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return blockTransactionList;
  }

  @Step
  public static void deleteBlockTransactionByTcbsId(String tcbsId) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    Transaction transaction = session.beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery("delete from BlockTransaction a where a.tcbsId = :tcbsId");
    query.setParameter(TCBS_ID, tcbsId);
    query.executeUpdate();
    transaction.commit();
    ixuDbConnection.closeSession();
  }
}
