package com.tcbs.automation.ixu;

import lombok.Data;
import net.thucydides.core.annotations.Step;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Data
@Entity
@Table(name = "CONVERT_FUND_TRANSACTION")
public class ConvertFundTransaction {
  @Id
  @Column(name = "ID")
  private Long id;

  @Column(name = "TCBS_ID")
  private String tcbsId;

  @Column(name = "POINT")
  private BigDecimal point;

  @Column(name = "CASHBACK_VALUE")
  private BigDecimal cashbackValue;

  @Column(name = "ORDER_ID")
  private Long orderId;

  @Column(name = "ISSUED_DATE")
  private java.sql.Timestamp issuedDate;

  @Column(name = "RAW_DATA")
  private String rawData;

  @Column(name = "CREATED_DATE")
  private java.sql.Timestamp createdDate;

  @Column(name = "LAST_UPDATED_DATE")
  private java.sql.Timestamp lastUpdatedDate;

  @Step
  public static void deleteByTcbsIds(List<String> tcbsIds) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    org.hibernate.query.Query query = ixuDbConnection.getSession().createQuery(
      "delete from ConvertFundTransaction s where s.tcbsId in :tcbsId");
    query.setParameter("tcbsId", tcbsIds);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByOrderId(Long orderId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    org.hibernate.query.Query query = ixuDbConnection.getSession().createQuery(
      "delete from ConvertFundTransaction s where s.orderId = :orderId");
    query.setParameter("orderId", orderId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void createNewEntity(ConvertFundTransaction entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<ConvertFundTransaction> findByTcbsIdIn(List<String> tcbsIds) {
    ixuDbConnection.getSession().clear();
    org.hibernate.query.Query<ConvertFundTransaction> query = ixuDbConnection.getSession().createQuery(
      "from ConvertFundTransaction a where a.tcbsId in :tcbsId ");
    query.setParameter("tcbsId", tcbsIds);
    List<ConvertFundTransaction> convertFundTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return convertFundTransactionList;
  }
}
