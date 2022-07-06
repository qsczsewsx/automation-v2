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
import javax.persistence.Table;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "CASA_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CasaTransaction {

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "TCBS_ID")
  private String tcbsId;

  @Column(name = "CODE_105C")
  private String code105C;

  @Column(name = "RULE_OUTPUT")
  private String ruleOutput;

  @Column(name = "AWARD_TYPE")
  private String awardType;

  @Column(name = "MEMBERSHIP_NAME")
  private String membershipName;

  @Column(name = "CAMPAIGN_ID")
  private Long campaignId;

  @Column(name = "POINT")
  private Double point;

  @Column(name = "BALANCE")
  private Double balance;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "DETAIL")
  private String detail;

  @Column(name = "RAW_DATA")
  private String rawData;

  @Column(name = "ISSUE_DATE")
  private java.sql.Timestamp issueDate;

  @Column(name = "CREATED_DATE")
  private java.sql.Timestamp createdDate;

  @Column(name = "LAST_UPDATED_DATE")
  private java.sql.Timestamp lastUpdatedDate;


  @Step
  public static void deleteByListTcbsId(List<String> tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    org.hibernate.query.Query query = ixuDbConnection.getSession().createQuery(
      "delete from CasaTransaction s where s.tcbsId in :tcbsId");
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }


  @Step
  public static void create(CasaTransaction entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<CasaTransaction> findByTcbsIdList(List<String> tcbsId) {
    ixuDbConnection.getSession().clear();
    org.hibernate.query.Query<CasaTransaction> query = ixuDbConnection.getSession().createQuery(
      "from CasaTransaction a where a.tcbsId IN :tcbsId ");
    query.setParameter("tcbsId", tcbsId);
    List<CasaTransaction> casaTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return casaTransactionList;
  }
}
