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
@Table(name = "DAILY_WITHDRAW_LIMIT")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DailyWithdrawLimit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "REMAIN")
  private String remain;
  @Column(name = "LIMIT")
  private String limit;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "LAST_UPDATED_DATE")
  private Timestamp lastUpdatedDate;


  @Step
  public static DailyWithdrawLimit insert(DailyWithdrawLimit entity) {
    Session session = ixuDbConnection.getSession();
    session.beginTransaction();
    session.save(entity);
    session.getTransaction().commit();
    ixuDbConnection.closeSession();
    return entity;
  }

  @Step
  public static DailyWithdrawLimit getByTcbsId(String tcbsId) {
    Query<DailyWithdrawLimit> query = ixuDbConnection.getSession().createQuery(
      "from DailyWithdrawLimit where tcbsId = :tcbsId", DailyWithdrawLimit.class);
    query.setParameter("tcbsId", tcbsId);
    List<DailyWithdrawLimit> result = query.getResultList();
    ixuDbConnection.closeSession();
    return result.get(0);
  }

  public static List<DailyWithdrawLimit> getAll() {
    ixuDbConnection.getSession().clear();
    Query<DailyWithdrawLimit> query = ixuDbConnection.getSession().createQuery(
      "from DailyWithdrawLimit a where a.id is not null", DailyWithdrawLimit.class);
    List<DailyWithdrawLimit> result = query.getResultList();
    ixuDbConnection.closeSession();
    return result;
  }

  @Step
  public static void updateDailyWithdrawLimitByTcbsId(String tcbsId, String remain) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "update DailyWithdrawLimit a set a.remain=:remain where a.tcbsId=:tcbsId");
    query.setParameter("remain", remain);
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteDailyWithdrawLimitByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete DailyWithdrawLimit a where a.tcbsId=:tcbsId");
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteDailyWithdrawLimitByTcbsIdList(List<String> tcbsIds) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from DailyWithdrawLimit a where a.tcbsId in :tcbsIds");
    query.setParameter("tcbsIds", tcbsIds);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }
}
