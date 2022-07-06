package com.tcbs.automation.ixu;

import lombok.Data;
import net.thucydides.core.annotations.Step;
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
@Table(name = "OPEN_ACCOUNT_TRANSACTION")
@Data
public class OpenAccountTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "CAMPAIGN_ID")
  private Long campaignId;

  @Column(name = "BLOCK_TRANSACTION_ID")
  private Long blockTransactionId;

  @Column(name = "TCBS_ID")
  private String tcbsId;

  @Column(name = "CODE105C")
  private String code105C;

  @Column(name = "CUSTOMER_NAME")
  private String customerName;

  @Column(name = "POINT")
  private Long point;

  @Column(name = "AWARD_TYPE")
  private String awardType;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "RAW_DATA")
  private String rawData;

  @Column(name = "RULE_OUTPUT")
  private String ruleOutput;

  @Column(name = "EXPIRED_DATE")
  private Timestamp expiredDate;

  @Column(name = "ISSUE_DATE")
  private Timestamp issueDate;

  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Column(name = "LAST_UPDATED_DATE")
  private Timestamp lastUpdatedDate;

  @Step
  public static void deleteByTcbsIds(List<String> tcbsIds) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    org.hibernate.query.Query query = ixuDbConnection.getSession().createQuery(
      "delete from OpenAccountTransaction s where s.tcbsId in :tcbsId");
    query.setParameter("tcbsId", tcbsIds);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteBy105Cs(List<String> tcbsIds) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    org.hibernate.query.Query query = ixuDbConnection.getSession().createQuery(
      "delete from OpenAccountTransaction s where s.code105C in :tcbsId");
    query.setParameter("tcbsId", tcbsIds);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void createNewEntity(OpenAccountTransaction entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().saveOrUpdate(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<OpenAccountTransaction> findByTcbsIdIn(List<String> tcbsIds) {
    ixuDbConnection.getSession().clear();
    Query<OpenAccountTransaction> query = ixuDbConnection.getSession().createQuery(
      "from OpenAccountTransaction a where a.tcbsId in :tcbsId ");
    query.setParameter("tcbsId", tcbsIds);
    List<OpenAccountTransaction> openAccountTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return openAccountTransactionList;
  }

  @Step
  public static void updateCreatedDateById(Timestamp createdDate, Long id) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    org.hibernate.query.Query query = ixuDbConnection.getSession().createQuery(
      "update OpenAccountTransaction a set a.createdDate = :createdDate where a.id = :id");
    query.setParameter("createdDate", createdDate);
    query.setParameter("id", id);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void updateExpiredDateById(Timestamp expiredDate, Long id) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    org.hibernate.query.Query query = ixuDbConnection.getSession().createQuery(
      "update OpenAccountTransaction a set a.expiredDate = :expiredDate where a.id = :id");
    query.setParameter("expiredDate", expiredDate);
    query.setParameter("id", id);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }
}

