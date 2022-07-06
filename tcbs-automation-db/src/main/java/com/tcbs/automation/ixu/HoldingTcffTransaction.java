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
@Table(name = "HOLDING_TCFF_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HoldingTcffTransaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "AWARD_TYPE")
  private String awardType;
  @Column(name = "MEMBERSHIP_NAME")
  private String membershipName;
  @Column(name = "CAMPAIGN_ID")
  private String campaignId;
  @Column(name = "AMOUNT")
  private Double amount;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "ISSUED_DATE")
  private Timestamp issueDate;
  @Column(name = "POINT")
  private Double point;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "DETAIL")
  private String detail;
  @Column(name = "RULE_INPUT")
  private String ruleInput;
  @Column(name = "RULE_OUTPUT")
  private String ruleOutput;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Step
  public static void deleteByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    org.hibernate.query.Query query = ixuDbConnection.getSession().createQuery(
      "delete from HoldingTcffTransaction s where s.tcbsId =:tcbsId");
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteByListTcbsId(List<String> tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from HoldingTcffTransaction s where s.tcbsId in :tcbsId");
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }


  @Step
  public static void create(HoldingTcffTransaction entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<HoldingTcffTransaction> findByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<HoldingTcffTransaction> query = ixuDbConnection.getSession().createQuery(
      "from HoldingTcffTransaction a where a.tcbsId=:tcbsId ");
    query.setParameter("tcbsId", tcbsId);
    List<HoldingTcffTransaction> holdingTcffTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return holdingTcffTransactionList;
  }

  @Step
  public static List<HoldingTcffTransaction> findByTcbsIdList(List<String> tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<HoldingTcffTransaction> query = ixuDbConnection.getSession().createQuery(
      "from HoldingTcffTransaction a where a.tcbsId IN :tcbsId ");
    query.setParameter("tcbsId", tcbsId);
    List<HoldingTcffTransaction> holdingTcffTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return holdingTcffTransactionList;
  }
}

