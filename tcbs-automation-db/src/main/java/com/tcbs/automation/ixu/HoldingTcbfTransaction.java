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
@Table(name = "HOLDING_TCBF_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HoldingTcbfTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "ORDER_ID")
  private String orderId;
  @Column(name = "HOLDING_TIME")
  private Double holdingTime;
  @Column(name = "NAV_CURRENT")
  private Double navCurrent;
  @Column(name = "VOLUME_AVAILABLE")
  private Double volumeAvailable;
  @Column(name = "VALUE_AVAILABLE")
  private Double valueAvailable;
  @Column(name = "POINT")
  private Double point;
  @Column(name = "RATE")
  private String rate;
  @Column(name = "CAMPAIGN_ID")
  private Long campaignId;
  @Column(name = "YEAR_ROUND")
  private String yearRound;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "RULE_INPUT")
  private String ruleInput;
  @Column(name = "RULE_OUTPUT")
  private String ruleOutput;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "ISSUED_DATE")
  private Timestamp issuedDate;


  @Step
  public static void deleteHoldingFundTransactionWithTcbsIds(List<String> tcbsIds) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from HoldingTcbfTransaction a where a.tcbsId in :tcbsIds");
    query.setParameter("tcbsIds", tcbsIds);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }


  @Step
  public static List<HoldingTcbfTransaction> findHoldingTcbf(List<String> tcbsIds) {
    ixuDbConnection.getSession().clear();
    Query<HoldingTcbfTransaction> query = ixuDbConnection.getSession().createQuery(
      "from HoldingTcbfTransaction a where a.tcbsId in :tcbsIds"
    );
    query.setParameter("tcbsIds", tcbsIds);
    List<HoldingTcbfTransaction> holdingTcbfTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return holdingTcbfTransactionList;
  }

}
