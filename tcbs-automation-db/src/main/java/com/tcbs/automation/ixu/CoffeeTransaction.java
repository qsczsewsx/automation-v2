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
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "COFFEE_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CoffeeTransaction {
  private static final String TCBS_ID = "tcbsId";
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "TCBS_ID")
  private String tcbsId;

  @Column(name = "POINT")
  private String point;

  @Column(name = "RAW_DATA")
  private String rawData;

  @Column(name = "ORDER_ID")
  private String orderId;

  @Column(name = "ISSUE_DATE")
  private Timestamp issueDate;

  @Step
  public static void create(CoffeeTransaction entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    ixuDbConnection.getSession().save(entity);

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    org.hibernate.query.Query query = ixuDbConnection.getSession().createNativeQuery(
      "DELETE FROM COFFEE_TRANSACTION where TCBS_ID = :tcbsId");
    query.setParameter(TCBS_ID, tcbsId);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static CoffeeTransaction getByTcbsId(String tcbsId) {
    Query<CoffeeTransaction> query = ixuDbConnection.getSession().createQuery(
      "from CoffeeTransaction where tcbsId = :tcbsId", CoffeeTransaction.class);
    query.setParameter(TCBS_ID, tcbsId);
    List<CoffeeTransaction> result = query.getResultList();
    if (!result.isEmpty()) {
      ixuDbConnection.closeSession();
      return result.get(0);
    } else {
      ixuDbConnection.closeSession();
      return null;
    }
  }

}
