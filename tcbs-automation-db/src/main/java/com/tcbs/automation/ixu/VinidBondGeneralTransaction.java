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
@Table(name = "VINID_BOND_GENERAL_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VinidBondGeneralTransaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "CODE_105C")
  private String code105C;
  @Column(name = "AMOUNT")
  private String amount;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "RULE_INPUT")
  private String ruleInput;
  @Column(name = "RULE_OUTPUT")
  private String ruleOutput;
  @Column(name = "CREATED_DATE")
  private java.sql.Timestamp createdDate;
  @Column(name = "ISSUE_DATE")
  private java.sql.Timestamp issueDate;
  @Column(name = "PRODUCT_CODE")
  private String productCode;


  @Step
  public static void insert(VinidBondGeneralTransaction entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void delete(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    org.hibernate.query.Query query = ixuDbConnection.getSession().createQuery(
      "delete from VinidBondGeneralTransaction a where a.tcbsId =:tcbsid");
    query.setParameter("tcbsid", tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<VinidBondGeneralTransaction> find(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<VinidBondGeneralTransaction> query = ixuDbConnection.getSession().createQuery(
      "from VinidBondGeneralTransaction a where a.tcbsId = :tcbsid ");
    query.setParameter("tcbsid", tcbsId);
    List<VinidBondGeneralTransaction> generalTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return generalTransactionList;
  }

  @Step
  public static VinidBondGeneralTransaction findById(String id) {
    ixuDbConnection.getSession().clear();
    Query<VinidBondGeneralTransaction> query = ixuDbConnection.getSession().createQuery(
      "from VinidBondGeneralTransaction a where a.id = :id ");
    query.setParameter("id", id);
    VinidBondGeneralTransaction vinidBondGeneralTransaction = query.getSingleResult();
    ixuDbConnection.closeSession();
    if (vinidBondGeneralTransaction == null) {
      return null;
    }
    return vinidBondGeneralTransaction;
  }
}