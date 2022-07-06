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
@Table(name = "PROFESSIONAL_EXTRA")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionalExtra {
  private static final String TCBS_ID = "tcbsId";
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "ORDER_ID")
  private String orderId;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "PRINCIPAL")
  private String principal;
  @Column(name = "QUANTITY")
  private String quantity;
  @Column(name = "ACTION")
  private Integer action;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "RULE_INPUT")
  private String ruleInput;
  @Column(name = "RULE_OUTPUT")
  private String ruleOutput;
  @Column(name = "ISSUED_DATE")
  private Timestamp issuedDate;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "PROFESSIONAL_ID")
  private Long professionalId;


  @Step
  public static void deleteByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createNativeQuery(
      "delete from PROFESSIONAL_EXTRA s where s.TCBS_ID =:tcbsId ");
    query.setParameter(TCBS_ID, tcbsId);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<ProfessionalExtra> findByTcbsId(String tcbsId) {
    Query<ProfessionalExtra> query = ixuDbConnection.getSession().createQuery(
      "from ProfessionalExtra a where a.tcbsId = :tcbsId");
    query.setParameter(TCBS_ID, tcbsId);
    List<ProfessionalExtra> professionalExtraList = query.getResultList();
    ixuDbConnection.closeSession();
    return professionalExtraList;
  }

  @Step
  public static List<ProfessionalExtra> findByTcbsIdAndOrderId(String tcbsId, String orderId) {
    Query<ProfessionalExtra> query = ixuDbConnection.getSession().createQuery(
      "from ProfessionalExtra a where a.tcbsId = :tcbsId and a.orderId = :orderId");
    query.setParameter(TCBS_ID, tcbsId);
    query.setParameter("orderId", orderId);
    List<ProfessionalExtra> professionalExtraList = query.getResultList();
    ixuDbConnection.closeSession();
    return professionalExtraList;
  }

  @Step
  public static void createNewEntity(ProfessionalExtra entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }
}
