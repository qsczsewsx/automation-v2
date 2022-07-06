package com.tcbs.automation.ixu;

import com.tcbs.automation.SqlUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
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
@Table(name = "MARGIN_ACCUMULATION_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MarginAccumulationTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "POINT")
  private Double point;
  @Column(name = "VOLUME")
  private Double volume;
  @Column(name = "AWARD_TYPE")
  private String awardType;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "CAMPAIGN_ID")
  private String campaignId;
  @Column(name = "RULE_OUTPUT")
  private String ruleOutput;
  @Column(name = "MEMBERSHIP_NAME")
  private String membershipName;
  @Column(name = "POINT_PER_BILLION_PER_QUARTER")
  private String pointPerBillionPerQuarter;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "ISSUE_DATE")
  private Timestamp issueDate;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "ERROR_CODE")
  private String errorCode;
  @Column(name = "ERROR_MESSAGE")
  private String errorMessage;
  @Column(name = "ACTION")
  private String action;

  @Step
  public static void insertMarginAccumulationTransaction(MarginAccumulationTransaction marginAccumulationTransaction) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    ixuDbConnection.getSession().save(marginAccumulationTransaction);

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void clearMarginAccumulationTransaction(List<String> tcbsId) {
    ixuDbConnection.getSession().clear();
    Session session = TcXu.ixuDbConnection.getSession();
    StringBuilder stringBuilder = new StringBuilder();
    session.beginTransaction();
    stringBuilder.append("delete from MARGIN_ACCUMULATION_TRANSACTION a where ");
    stringBuilder.append(SqlUtils.getTcbsidListForSql(tcbsId, "TCBS_ID"));
    Query query = session.createNativeQuery(stringBuilder.toString());
    query.executeUpdate();

    session.getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<MarginAccumulationTransaction> findMarginAccumulationTransactionByListTcbsId(List<String> tcbsId) {
    ixuDbConnection.getSession().clear();
    Session session = ixuDbConnection.getSession();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("select * from MARGIN_ACCUMULATION_TRANSACTION a where ");
    stringBuilder.append(SqlUtils.getTcbsidListForSql(tcbsId, "TCBS_ID"));
    Query<MarginAccumulationTransaction> query = session.createNativeQuery(stringBuilder.toString(), MarginAccumulationTransaction.class);
    List<MarginAccumulationTransaction> result = query.getResultList();
    if (result.size() > 0) {
      ixuDbConnection.closeSession();
      return result;
    } else {
      ixuDbConnection.closeSession();
      return null;
    }
  }
}
