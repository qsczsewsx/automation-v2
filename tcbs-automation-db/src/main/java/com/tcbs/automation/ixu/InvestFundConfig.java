package com.tcbs.automation.ixu;

import joptsimple.internal.Strings;
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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "INVEST_IFUND_CONFIG")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InvestFundConfig {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "FUND_TYPE")
  private String fundType;
  @Column(name = "INVEST_TYPE")
  private String investType;
  @Column(name = "CRON_TIME")
  private String cronTime;
  @Column(name = "POINT_TRIGGER")
  private Double pointTrigger;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "UPDATED_DATE")
  private Timestamp updatedDate;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "DEFS_ID")
  private String defsId;
  @Column(name = "START_TIME")
  private Date startTime;
  @Column(name = "END_TIME")
  private Date endTime;
  @Column(name = "CONTRACT_ID")
  private String contractId;

//  @Step
//  public static void clear(String tcbsId) {
//    ixuDbConnection.getSession().clear();
//    Session session = ixuDbConnection.getSession();
//    session.beginTransaction();
//    Query query = session.createQuery("delete from InvestFundConfig where tcbsId = :tcbsId");
//    query.setParameter("tcbsId", tcbsId);
//    query.executeUpdate();
//    session.getTransaction().commit();
//  }

  @Step
  public static List<InvestFundConfig> filterBy(List<String> lstTcbsId, List<String> lstStatus, List<String> lstFundType) {
    ixuDbConnection.getSession().clear();

    String sqlQuery = "from InvestFundConfig a";
    List<String> lstSqlCondition = new LinkedList<>();
    if(lstTcbsId != null && lstTcbsId.size() > 0) {
      lstSqlCondition.add(" a.tcbsId in (:lstTcbsId) ");
    }

    if(lstStatus != null && lstStatus.size() > 0) {
      lstSqlCondition.add(" a.status in (:lstStatus) ");
    }

    if(lstFundType != null && lstFundType.size() > 0) {
      lstSqlCondition.add(" a.fundType in (:lstFundType) ");
    }

    String sqlCondition = Strings.join(lstSqlCondition, " AND ");
    if(!sqlCondition.isEmpty()) {
      sqlQuery += " where " + sqlCondition;
    }
    Query<InvestFundConfig> query = ixuDbConnection.getSession().createQuery(sqlQuery);

    if(lstTcbsId != null && lstTcbsId.size() > 0) {
      query.setParameter("lstTcbsId", lstTcbsId);
    }

    if(lstStatus != null && lstStatus.size() > 0) {
      query.setParameter("lstStatus", lstStatus);
    }

    if(lstFundType != null && lstFundType.size() > 0) {
      query.setParameter("lstFundType", lstFundType);
    }

    List<InvestFundConfig> invest = query.getResultList();
    ixuDbConnection.closeSession();
    return invest;
  }

  @Step
  public static List<InvestFundConfig> getConfig(String tcbsId, String status) {
    ixuDbConnection.getSession().clear();
    Query<InvestFundConfig> query = ixuDbConnection.getSession().createQuery(
      "from InvestFundConfig a where a.tcbsId = :tcbsId and a.status = :status", InvestFundConfig.class);
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("status", status);
    List<InvestFundConfig> invest = query.getResultList();
    ixuDbConnection.closeSession();
    return invest;
  }

  @Step
  public static List<InvestFundConfig> getConfig(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<InvestFundConfig> query = ixuDbConnection.getSession().createQuery(
      "from InvestFundConfig a where a.tcbsId = :tcbsId and a.status = :status order by createdDate"
      , InvestFundConfig.class);
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("status", "ACTIVE");
    List<InvestFundConfig> invest = query.getResultList();
    ixuDbConnection.closeSession();
    return invest;
  }

  @Step
  public static List<InvestFundConfig> getConfigWithId(String id) {
    ixuDbConnection.getSession().clear();
    Query<InvestFundConfig> query = ixuDbConnection.getSession().createQuery(
      "from InvestFundConfig a where a.id = :id"
      , InvestFundConfig.class);
    query.setParameter("id", id);
    List<InvestFundConfig> invest = query.getResultList();
    ixuDbConnection.closeSession();
    return invest;
  }

  @Step
  public static void saveOrUpdate(InvestFundConfig entity) {
    ixuDbConnection.getSession().clear();
    Session session = ixuDbConnection.getSession();
    session.beginTransaction();
    session.saveOrUpdate(entity);
    session.getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void create(InvestFundConfig entity) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    Transaction transaction = session.beginTransaction();
    session.save(entity);
    transaction.commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByTcbsId(String id) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    Transaction transaction = session.beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from InvestFundConfig a where a.tcbsId = :id"
    );
    query.setParameter("id", id);
    query.executeUpdate();
    transaction.commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteAll() {
    Session session = ixuDbConnection.getSession();
    session.clear();
    Transaction transaction = session.beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
            "delete from InvestFundConfig"
    );
    query.executeUpdate();
    transaction.commit();
    ixuDbConnection.closeSession();
  }
}
