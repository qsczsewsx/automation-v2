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
@Table(name = "PROFESSIONAL_INTEREST_LOAN_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionalInterestLoanTransaction {
  private static final String TCBS_ID = "tcbsId";
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "ORDER_ID")
  private Long orderId;
  @Column(name = "PROFESSIONAL_ID")
  private Long professionalId;
  @Column(name = "POINT")
  private Double point;
  @Column(name = "PRINCIPLE")
  private Double principle;
  @Column(name = "AMOUNT")
  private Double amount;
  @Column(name = "LOAN_AMOUNT")
  private Double loanAmount;
  @Column(name = "INTEREST_RATE")
  private Double interestRate;
  @Column(name = "DAY_OF_YEAR")
  private Long dayOfYear;
  @Column(name = "NEXT_WORKING_DAY")
  private Timestamp nextWorkingDay;
  @Column(name = "START_DATE")
  private Timestamp startDate;
  @Column(name = "END_DATE")
  private Timestamp endDate;
  @Column(name = "ISSUE_DATE")
  private Timestamp issuedDate;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;


  @Step
  public static void deleteByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createNativeQuery(
      "delete from PROFESSIONAL_INTEREST_LOAN_TRANSACTION s where s.TCBS_ID =:tcbsId ");
    query.setParameter(TCBS_ID, tcbsId);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<ProfessionalInterestLoanTransaction> findByTcbsId(String tcbsId) {
    Query<ProfessionalInterestLoanTransaction> query = ixuDbConnection.getSession().createQuery(
      "from ProfessionalInterestLoanTransaction a where a.tcbsId = :tcbsId", ProfessionalInterestLoanTransaction.class);
    query.setParameter(TCBS_ID, tcbsId);
    List<ProfessionalInterestLoanTransaction> list = query.getResultList();
    ixuDbConnection.closeSession();
    return list;
  }

  @Step
  public static List<ProfessionalInterestLoanTransaction> findByTcbsIdAndProfessionalId(String tcbsId, String professionalId) {
    Query<ProfessionalInterestLoanTransaction> query = ixuDbConnection.getSession().createQuery(
      "from ProfessionalInterestLoanTransaction a where a.tcbsId = :tcbsId and a.professionalId = :professionalId", ProfessionalInterestLoanTransaction.class);
    query.setParameter(TCBS_ID, tcbsId);
    query.setParameter("professionalId", Long.valueOf(professionalId));
    List<ProfessionalInterestLoanTransaction> professionalInterestLoanTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return professionalInterestLoanTransactionList;
  }

  @Step
  public static void createNewEntity(ProfessionalInterestLoanTransaction entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }
}
