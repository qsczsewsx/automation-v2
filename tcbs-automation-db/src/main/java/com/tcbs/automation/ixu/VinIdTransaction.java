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
@Table(name = "VIN_ID_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VinIdTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "CAMPAIGN_ID")
  private String campaignId;
  @Column(name = "POINT")
  private String point;
  @Column(name = "ISSUE_DATE")
  private Timestamp issueDate;
  @Column(name = "AWARD_TYPE")
  private String awardType;
  @Column(name = "CARD_NUMBER")
  private String cardNumber;
  @Column(name = "VIN_ID_AMOUNT")
  private Long vinidAmt;

  @Column(name = "DATE_TRANSACTION")
  private String dateTx;

  @Column(name = "TIME_TRANSACTION")
  private String timeTx;

  @Column(name = "INVOICE_NUMBER")
  private String invoiceNo;

  @Column(name = "USER_ID")
  private String userId;
  @Column(name = "PHONE_NUMBER")
  private String phoneNumber;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Step
  public static List<VinIdTransaction> getVinIdTransactionById(Long id) {
    ixuDbConnection.getSession().clear();
    Query query = ixuDbConnection.getSession().createQuery(
      "from VinIdTransaction a where a.id =:id"
      , VinIdTransaction.class);
    query.setParameter("id", id.toString());
    List<VinIdTransaction> list = query.getResultList();
    ixuDbConnection.closeSession();
    return list;
  }

  @Step
  public static List<VinIdTransaction> getVinIdTransactionByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query query = ixuDbConnection.getSession().createQuery(
      "from VinIdTransaction a where a.tcbsId =:tcbsId"
      , VinIdTransaction.class);
    query.setParameter("tcbsId", tcbsId);
    List<VinIdTransaction> list = query.getResultList();
    ixuDbConnection.closeSession();
    return list;
  }

  @Step
  public static void clearTransactionById(Long id) {
    ixuDbConnection.getSession().clear();
    Session session = ixuDbConnection.getSession();
    session.beginTransaction();
    Query query = session.createQuery(
      "delete from VinIdTransaction a where a.id =:id"
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
      "delete from VinIdTransaction a where a.tcbsId =:tcbsId"
    );
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();
    session.getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void insertVinIdTransaction(VinIdTransaction vinIdTransaction) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(vinIdTransaction);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static VinIdTransaction insert(VinIdTransaction vinIdTransaction) {
    ixuDbConnection.getSession().clear();
    Session session = ixuDbConnection.getSession();
    session.save(vinIdTransaction);
    ixuDbConnection.closeSession();
    return vinIdTransaction;
  }

  @Step
  public static VinIdTransaction saveOrUpdate(VinIdTransaction vinIdTransaction) {

    ixuDbConnection.getSession().clear();
    Session session = ixuDbConnection.getSession();
    Transaction tx = session.beginTransaction();
    VinIdTransaction vinid = session.get(VinIdTransaction.class, vinIdTransaction.getId());
    vinid.setInvoiceNo(vinIdTransaction.getInvoiceNo());
    session.saveOrUpdate(vinid);
    session.getTransaction().commit();
    ixuDbConnection.closeSession();
    return vinIdTransaction;
  }

}
