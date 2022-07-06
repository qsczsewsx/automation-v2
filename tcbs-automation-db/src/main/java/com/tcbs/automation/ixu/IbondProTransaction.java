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
@Table(name = "IBOND_PRO_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IbondProTransaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "CODE_105C")
  private String code105C;
  @Column(name = "ORDER_ID")
  private Integer orderId;
  @Column(name = "BOND_CODE")
  private String bondCode;
  @Column(name = "PAR")
  private Double par;
  @Column(name = "RULE_INPUT")
  private String ruleInput;
  @Column(name = "RULE_OUTPUT")
  private String ruleOutput;
  @Column(name = "PRINCIPAL")
  private Double principal;
  @Column(name = "QUANTITY")
  private Long quantity;
  @Column(name = "TRADING_DATE")
  private Timestamp tradingDate;
  @Column(name = "TRADING_CODE")
  private String tradingCode;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  private static final String TCBS_ID = "tcbsId";

  @Step
  public static List<IbondProTransaction> getAllByTcbsIdOrderById(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query<IbondProTransaction> query = ixuDbConnection.getSession().createQuery(
      "from IbondProTransaction a where a.tcbsId = :tcbsId order by a.id desc", IbondProTransaction.class);
    query.setParameter(TCBS_ID, tcbsId);
    List<IbondProTransaction> results = query.getResultList();
    ixuDbConnection.closeSession();
    return results;
  }

  @Step
  public static void deleteByOrderId(Integer orderId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from IbondProTransaction a where a.orderId = :orderId");
    query.setParameter("orderId", orderId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
  }

  @Step
  public static void deleteByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from IbondProTransaction a where a.tcbsId = :tcbsId");
    query.setParameter(TCBS_ID, tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
  }

  @Step
  public static IbondProTransaction insertOrSave(IbondProTransaction entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().saveOrUpdate(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
    return entity;
  }
}
