package com.tcbs.automation.ixu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "PROFESSIONAL_INVESTOR_GENERAL_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionalInvestorGeneralTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "POINT")
  private Double point;
  @Column(name = "ISSUED_DATE")
  private Timestamp issuedDate;
  @Column(name = "IS_COUPON")
  private String isCoupon;
  @Column(name = "POINT_COUPON")
  private Double pointCoupon;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "RULE_INPUT")
  private String ruleInput;
  @Column(name = "RULE_OUTPUT")
  private String ruleOutput;
  @Column(name = "TIME_COUPON")
  private Timestamp timeCoupon;
  @Column(name = "BASE")
  private Double base;
  @Column(name = "DAYS_HOLDING_COUPON")
  private Double daysHoldingCoupon;
  @Column(name = "BUY_FIRST_TIME")
  private Timestamp buyFirstTime;
  @Column(name = "AMOUNT_BUY")
  private Double amountBuy;
  @Column(name = "AMOUNT_SELL")
  private Double amountSell;
  @Column(name = "SELL_LAST_TIME")
  private Timestamp sellLastTime;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "LAST_TIME")
  private Timestamp lastTime;
  @Column(name = "BOND_CODE")
  private String bondCode;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "QUANTITY_COUPON")
  private Double quantityCoupon;
  @Column(name = "TAX")
  private Double tax;
  @Column(name = "TIME_APPLY_COUPON")
  private Timestamp timeApplyCoupon;
  @Column(name = "MAX_QUANTITY")
  private Double maxQuantity;
  @Column(name = "UNIT_AVERAGE_BUY")
  private Double unitAverageBuy;
  @Column(name = "UNIT_AVERAGE_SELL")
  private Double unitAverageSell;
  @Column(name = "LOAN_AMOUNT")
  private Double loanAmount;
  @Column(name = "INTEREST_RATE")
  private Double interestRate;
  @Column(name = "BASE_YEAR")
  private Double baseYear;

  @Step
  public static void deleteByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from ProfessionalInvestorGeneralTransaction s where s.tcbsId =:tcbsId");
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<ProfessionalInvestorGeneralTransaction> findByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query query = ixuDbConnection.getSession().createQuery(
      "from ProfessionalInvestorGeneralTransaction s where s.tcbsId =:tcbsId");
    query.setParameter("tcbsId", tcbsId);
    List<ProfessionalInvestorGeneralTransaction> generalTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return generalTransactionList;
  }

  @Step
  public static void updateProfessionalInvestorGeneralTransaction(String issueDate, Long id) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "update ProfessionalInvestorGeneralTransaction a set a.buyFirstTime = to_date(:buyFirstTime, 'yyyy-MM-dd') where a.id=:id");
    query.setParameter("buyFirstTime", issueDate);
    query.setParameter("id", id);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void updateStatusProfessionalInvestorGeneralTransactionById(String status, Long id) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "update ProfessionalInvestorGeneralTransaction a set a.status=:status where a.id=:id");
    query.setParameter("status", status);
    query.setParameter("id", id);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static Long createNewEntity(ProfessionalInvestorGeneralTransaction entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Long id = Long.valueOf(ixuDbConnection.getSession().save(entity).toString());
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
    return id;
  }
}
