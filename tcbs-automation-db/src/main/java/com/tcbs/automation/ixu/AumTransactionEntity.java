package com.tcbs.automation.ixu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "AUM_TRANSACTION")
public class AumTransactionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "TCBSID")
  private String tcbsid;

  @Column(name = "POINT")
  private Long point;

  @Column(name = "VIP_TYPE")
  private String vipType;

  @Column(name = "ORDER_ID")
  private Long orderId;

  @Column(name = "VOLUME")
  private Long volume;

  @Column(name = "ISSUED_DATE")
  private Time issuedDate;

  @Column(name = "ORDER_CODE")
  private String orderCode;

  @Column(name = "RAW_DATA")
  private String rawData;

  @Column(name = "RULE_INPUT")
  private String ruleInput;

  @Column(name = "RULEOPS_INFO")
  private String ruleopsInfo;

  @Column(name = "RULE_OUTPUT")
  private String ruleOutput;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Step
  public static void deleteByOrderId(Long orderId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from AumTransactionEntity a where a.orderId=:orderId");
    query.setParameter("orderId", orderId);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByTcbsId(String tcbsid) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from AumTransactionEntity a where a.tcbsid=:tcbsid");
    query.setParameter("tcbsid", tcbsid);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void create(AumTransactionEntity entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    ixuDbConnection.getSession().save(entity);

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<AumTransactionEntity> findAumTransactionByOrderId(Long orderId) {
    Query<AumTransactionEntity> query = ixuDbConnection.getSession().createQuery(
      "from AumTransactionEntity a where a.orderId=:orderId ");
    query.setParameter("orderId", orderId);
    List<AumTransactionEntity> aumTransactionEntityList = query.getResultList();
    ixuDbConnection.closeSession();
    return aumTransactionEntityList;
  }
}
