package com.tcbs.automation.ixu;

import com.sun.istack.NotNull;
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
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "manual_transaction_history")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ManualTransactionHistoryEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private Long id;

  @Column(name = "MANUAL_TRANSACTION_ID")
  private Long manualTransactionId;

  @Column(name = "ACTION")
  private String action;

  @Column(name = "ACTION_USER")
  private String actionUser;

  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Column(name = "DESCRIPTION")
  private String description;

  @Step
  public static List<ManualTransactionHistoryEntity> findManualTransactionHistoryByManualTransactionId(Long id) {
    Query<ManualTransactionHistoryEntity> query = ixuDbConnection.getSession().createQuery(
      "from ManualTransactionHistoryEntity a where a.manualTransactionId=:id order by a.id asc",
      ManualTransactionHistoryEntity.class);
    query.setParameter("id", id);
    List<ManualTransactionHistoryEntity> result = query.getResultList();
    if (result.size() > 0) {
      ixuDbConnection.closeSession();
      return result;
    } else {
      ixuDbConnection.closeSession();
      return null;
    }
  }

  @Step
  public static List<ManualTransactionHistoryEntity> findManualTransactionHistoryByManualTransactionId(List<Long> ids) {
    Query<ManualTransactionHistoryEntity> query = ixuDbConnection.getSession().createQuery(
      "from ManualTransactionHistoryEntity a where a.manualTransactionId IN (:id) order by a.id asc",
      ManualTransactionHistoryEntity.class);
    query.setParameter("id", ids);
    List<ManualTransactionHistoryEntity> result = query.getResultList();
    if (result.size() > 0) {
      ixuDbConnection.closeSession();
      return result;
    } else {
      ixuDbConnection.closeSession();
      return null;
    }
  }

  @Step
  public static void clearManualTransactionHistory(Long id) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from ManualTransactionHistoryEntity a where a.manualTransactionId=:id");
    query.setParameter("id", id);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }
}