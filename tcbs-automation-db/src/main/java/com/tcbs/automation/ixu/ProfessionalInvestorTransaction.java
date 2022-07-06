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
@Table(name = "PROFESSIONAL_INVESTOR_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionalInvestorTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "ORDER_ID")
  private String orderId;
  @Column(name = "ACTION")
  private String action;
  @Column(name = "BOND_CODE")
  private String bondCode;
  @Column(name = "ISSUED_DATE")
  private Timestamp issuedDate;
  @Column(name = "POINT")
  private String point;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "COUPON_VALUE")
  private String couponValue;
  @Column(name = "TIME_COUPON")
  private Timestamp timeCoupon;
  @Column(name = "TIME_APPLY_COUPON")
  private Timestamp timeApplyCoupon;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "LAST_UPDATED")
  private Timestamp lastUpdated;
  @Column(name = "ORDER_ID_COUPLE")
  private String orderIdCouple;
  @Column(name = "TAX")
  private String tax;
  @Column(name = "QUANTITY")
  private String quantity;
  @Column(name = "UNIT_PRICE")
  private String unitPrice;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "DAYS_HOLDING_COUPON")
  private String daysHoldingCoupon;
  @Column(name = "BASE")
  private String base;
  @Column(name = "POINT_COUPON")
  private String pointCoupon;
  @Column(name = "EXTRA_STATUS")
  private String extraStatus;
  @Column(name = "END_TIME_EXTRA")
  private Timestamp endTimeExtra;
  @Column(name = "EXTRA_POINT")
  private String extraPoint;
  @Column(name = "BOND_PRODUCT_CODE")
  private String bondProductCode;
  @Column(name = "CODE_105C")
  private String code105C;

  @Step
  public static void deleteByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from ProfessionalInvestorTransaction s where s.tcbsId =:tcbsId");
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByOrderId(Integer orderId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from ProfessionalInvestorTransaction s where s.orderId =:orderId");
    query.setParameter("orderId", orderId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<ProfessionalInvestorTransaction> findByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query query = ixuDbConnection.getSession().createQuery(
      "from ProfessionalInvestorTransaction s where s.tcbsId =:tcbsId");
    query.setParameter("tcbsId", tcbsId);
    List<ProfessionalInvestorTransaction> professionalInvestorTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return professionalInvestorTransactionList;
  }

  @Step
  public static List<ProfessionalInvestorTransaction> findByOrderId(Integer orderId) {
    ixuDbConnection.getSession().clear();
    Query query = ixuDbConnection.getSession().createQuery(
      "from ProfessionalInvestorTransaction s where s.orderId =:orderId");
    query.setParameter("orderId", orderId.toString());
    List<ProfessionalInvestorTransaction> professionalInvestorTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return professionalInvestorTransactionList;
  }

  @Step
  public static List<ProfessionalInvestorTransaction> findProfessionalInvestorTransactionByTcbsId(String tcbsId, String bondCode, Integer quantity, Integer action) {
    ixuDbConnection.getSession().clear();
    Query<ProfessionalInvestorTransaction> query = ixuDbConnection.getSession().createQuery(
      "from ProfessionalInvestorTransaction a where a.tcbsId =:tcbsId and a.bondCode =:bondCode and a.quantity =:quantity and a.action =:action");
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("bondCode", bondCode);
    query.setParameter("quantity", quantity.toString());
    query.setParameter("action", action.toString());
    List<ProfessionalInvestorTransaction> professionalInvestorTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return professionalInvestorTransactionList;
  }

  @Step
  public static void deleteByTcbsIdBondCodeQuantityAndAction(String tcbsId, String bondCode, Integer quantity, Integer action) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from ProfessionalInvestorTransaction s where s.tcbsId =:tcbsId and a.bondCode =:bondCode and a.quantity =:quantity and a.action =:action");
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("bondCode", bondCode);
    query.setParameter("quantity", quantity);
    query.setParameter("action", action);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void updateProfessionalInvestorTransaction(String id, String status) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "update ProfessionalInvestorTransaction a set a.status=:status where a.id=:id");
    query.setParameter("status", status);
    query.setParameter("id", id);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void updateIssuedDateProfessionalInvestorTransactionById(String issuedDate, String id) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "update ProfessionalInvestorTransaction a set a.issuedDate = to_date(:issuedDate, 'yyyy-MM-dd') where a.id=:id");
    query.setParameter("issuedDate", issuedDate);
    query.setParameter("id", id);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static Long createNewEntity(ProfessionalInvestorTransaction entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Long id = Long.valueOf(ixuDbConnection.getSession().save(entity).toString());
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
    return id;
  }

}
