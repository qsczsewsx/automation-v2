package com.tcbs.automation.ixu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "IXU_CASH_TRANSACTIONS")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IxuCashTransactions {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "AMOUNT_XU")
  private Long amountXu;
  @Column(name = "AMOUNT_CASH")
  private Long amountCash;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Step
  public static List<IxuCashTransactions> getCashTransactionsById(Long id) {
    ixuDbConnection.getSession().clear();
    Query query = ixuDbConnection.getSession().createQuery(
      "from IxuCashTransactions a where a.id =:id"
      , IxuCashTransactions.class);
    query.setParameter("id", id.toString());
    List<IxuCashTransactions> list = query.getResultList();
    ixuDbConnection.closeSession();
    return list;
  }

  @Step
  public static List<IxuCashTransactions> getCashTransactionsByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query query = ixuDbConnection.getSession().createQuery(
      "from IxuCashTransactions a where a.tcbsId =:tcbsId"
      , IxuCashTransactions.class);
    query.setParameter("tcbsId", tcbsId);
    List<IxuCashTransactions> list = query.getResultList();
    ixuDbConnection.closeSession();
    return list;
  }

  @Step
  public static void clearTransaction(Long id) {
    ixuDbConnection.getSession().clear();
    Session session = ixuDbConnection.getSession();
    session.beginTransaction();
    Query query = session.createQuery(
      "delete from IxuCashTransactions a where a.id =:id"
    );
    query.setParameter("id", id.toString());
    query.executeUpdate();
    session.getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void clearTransactionByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Session session = ixuDbConnection.getSession();
    session.beginTransaction();
    Query query = session.createQuery(
      "delete from IxuCashTransactions a where a.tcbsId =:tcbsId"
    );
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();
    session.getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void insertIxuCashTransaction(IxuCashTransactions ixuCashTransactions) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(ixuCashTransactions);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

}
