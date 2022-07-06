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
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "IBOND_FEE_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IbondFeeTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "ORDER_ID")
  private String orderId;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "POINT")
  private String point;
  @Column(name = "ISSUE_DATE")
  private java.sql.Timestamp issueDate;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "TRANS_FEE")
  private String transFee;
  @Column(name = "TRADING_CODE")
  private String tradingCode;
  @Column(name = "TRADING_DATE")
  private java.sql.Timestamp tradingDate;
  @Column(name = "PRINCIPAL")
  private String principal;
  @Column(name = "CREATED_DATE")
  private java.sql.Timestamp createdDate;

  @Step
  public static void deleteByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    org.hibernate.query.Query query = ixuDbConnection.getSession().createQuery(
      "delete from IbondFeeTransaction s where s.tcbsId =:tcbsId");
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByListTcbsId(List<String> tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    org.hibernate.query.Query query = ixuDbConnection.getSession().createQuery(
      "delete from IbondFeeTransaction s where s.tcbsId in :tcbsId");
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }


  @Step
  public static void create(IbondFeeTransaction entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<IbondFeeTransaction> findByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    org.hibernate.query.Query<IbondFeeTransaction> query = ixuDbConnection.getSession().createQuery(
      "from IbondFeeTransaction a where a.tcbsId=:tcbsId ");
    query.setParameter("tcbsId", tcbsId);
    List<IbondFeeTransaction> ibondFeeTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return ibondFeeTransactionList;
  }

  @Step
  public static List<IbondFeeTransaction> findByTcbsIdList(List<String> tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<IbondFeeTransaction> query = ixuDbConnection.getSession().createQuery(
      "from IbondFeeTransaction a where a.tcbsId IN :tcbsId ");
    query.setParameter("tcbsId", tcbsId);
    List<IbondFeeTransaction> ibondFeeTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return ibondFeeTransactionList;
  }
}
