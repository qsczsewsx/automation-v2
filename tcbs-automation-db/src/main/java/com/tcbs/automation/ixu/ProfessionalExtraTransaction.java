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
@Table(name = "PROFESSIONAL_EXTRA_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionalExtraTransaction {
  private static final String TCBS_ID = "tcbsId";
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "PROFESSIONAL_ID")
  private Long professionalId;
  @Column(name = "RULE_INPUT")
  private String ruleInput;
  @Column(name = "RULE_OUTPUT")
  private String ruleOutput;
  @Column(name = "ISSUE_DATE")
  private Timestamp issuedDate;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;


  @Step
  public static void deleteByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createNativeQuery(
      "delete from PROFESSIONAL_EXTRA_TRANSACTION s where s.TCBS_ID =:tcbsId ");
    query.setParameter(TCBS_ID, tcbsId);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<ProfessionalExtraTransaction> findByTcbsId(String tcbsId) {
    Query<ProfessionalExtraTransaction> query = ixuDbConnection.getSession().createQuery(
      "from ProfessionalExtraTransaction a where a.tcbsId = :tcbsId");
    query.setParameter(TCBS_ID, tcbsId);
    List<ProfessionalExtraTransaction> extraTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return extraTransactionList;
  }

  @Step
  public static List<ProfessionalExtraTransaction> findByTcbsIdAndProfessionalId(String tcbsId, String professionalId) {
    Query<ProfessionalExtraTransaction> query = ixuDbConnection.getSession().createQuery(
      "from ProfessionalExtraTransaction a where a.tcbsId = :tcbsId and a.professionalId = :professionalId");
    query.setParameter(TCBS_ID, tcbsId);
    query.setParameter("professionalId", Long.valueOf(professionalId));
    List<ProfessionalExtraTransaction> professionalExtraTransactions = query.getResultList();
    ixuDbConnection.closeSession();
    return professionalExtraTransactions;
  }

  @Step
  public static void createNewEntity(ProfessionalExtraTransaction entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }
}
