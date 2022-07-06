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
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "DAILY_WITHDRAW_LIMIT_CONFIG")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DailyWithdrawLimitConfig {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TYPE")
  private String type;
  @Column(name = "BALANCE")
  private String balance;
  @Column(name = "LIMIT")
  private String limit;
  @Column(name = "CREATED_DATE")
  private Date createdDate;
  @Column(name = "LAST_MODIFY_DATE")
  private Date lastDate;


  @Step
  public static DailyWithdrawLimitConfig insert(DailyWithdrawLimitConfig entity) {
    Session session = ixuDbConnection.getSession();
    session.beginTransaction();
    session.save(entity);
    session.getTransaction().commit();
    ixuDbConnection.closeSession();
    return entity;
  }

  @Step
  public static List<DailyWithdrawLimitConfig> getAll() {
    Query<DailyWithdrawLimitConfig> query = ixuDbConnection.getSession().createQuery(
      "from DailyWithdrawLimitConfig", DailyWithdrawLimitConfig.class);
    List<DailyWithdrawLimitConfig> result = query.getResultList();
    ixuDbConnection.closeSession();
    return result;
  }

  @Step
  public static void clearAll() {
    ixuDbConnection.getSession().clear();
    if (!ixuDbConnection.getSession().getTransaction().isActive()) {
      ixuDbConnection.getSession().beginTransaction();
    }
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from DailyWithdrawLimitConfig a where a.id is not null");
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }
}
