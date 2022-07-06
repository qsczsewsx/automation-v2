package com.tcbs.automation.ixu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "IXU_STATUS_UPDATE_RANKING")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IxuStatusUpdateRanking {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "BACK_DATE")
  private Date backDate;

  @Step
  public static List<IxuStatusUpdateRanking> getIxuStatusUpdateRankingByBackDate(String backDate) {
    Query<IxuStatusUpdateRanking> query = ixuDbConnection.getSession().createQuery(
      "from IxuStatusUpdateRanking a where a.backDate= to_date(:backDate, 'yyyy-MM-dd') order by a.id desc",
      IxuStatusUpdateRanking.class);
    query.setParameter("backDate", backDate);
    List<IxuStatusUpdateRanking> result = query.getResultList();
    ixuDbConnection.closeSession();
    return result;
  }

  @Step
  public static List<IxuStatusUpdateRanking> getIxuStatusUpdateRankingMoreThanInputDate(String inputDate) {
    Query<IxuStatusUpdateRanking> query = ixuDbConnection.getSession().createQuery(
      "from IxuStatusUpdateRanking a where a.backDate > to_date(:backDate, 'yyyy-MM-dd') order by a.id desc",
      IxuStatusUpdateRanking.class);
    query.setParameter("backDate", inputDate);
    List<IxuStatusUpdateRanking> result = query.getResultList();
    ixuDbConnection.closeSession();
    return result;
  }

  @Step
  public static void insertStatusUpdateRanking(String backDateString) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createNativeQuery(
      String.format("insert into IXU_STATUS_UPDATE_RANKING (CREATED_DATE, BACK_DATE)  values (CURRENT_TIMESTAMP, TO_DATE('%s','yyyy-MM-dd')) ", backDateString));
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteBackDateByDate(String backDateString) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createNativeQuery(String.format("delete from IXU_STATUS_UPDATE_RANKING where BACK_DATE = TO_DATE('%s','YYYY-MM-dd')", backDateString));
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteAllBackDateMoreThanAndEqualInputDate(String backDateString) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createNativeQuery(String.format("delete from IXU_STATUS_UPDATE_RANKING where BACK_DATE >= TO_DATE('%s','YYYY-MM-dd')", backDateString));
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }
}
