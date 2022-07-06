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
@Table(name = "ICONNECT_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IconnectTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "USER_ID")
  private String userId;
  @Column(name = "CODE105C")
  private String code105C;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "POINT")
  private Long point;
  @Column(name = "AWARD_TYPE")
  private String awardType;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "RULE_OUTPUT")
  private String ruleOutput;
  @Column(name = "MEMBERSHIP_NAME")
  private String membershipName;
  @Column(name = "NOTIFY_MESSAGE")
  private String notifyMessage;
  @Column(name = "ISSUE_DATE")
  private Timestamp issueDate;
  @Column(name = "VOLUME")
  private String volume;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "ORDER_ID")
  private String orderId;
  @Column(name = "POINT_PER_BILLION")
  private String pointPerBillion;

  @Step
  public static void clearIConnectTransaction(List<String> orderId) {
    TcXu.ixuDbConnection.getSession().clear();
    Session session = TcXu.ixuDbConnection.getSession();
    StringBuilder stringBuilder = new StringBuilder();
    session.beginTransaction();
    stringBuilder.append("delete from ICONNECT_TRANSACTION a where ");
    stringBuilder.append(SqlUtils.getTcbsidListForSql(orderId, "ORDER_ID"));
    Query query = session.createNativeQuery(stringBuilder.toString());
    query.executeUpdate();

    session.getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteIConnectTransactionByListTcbsId(List<String> tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete IconnectTransaction a where a.tcbsId in :tcbsId");
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void insertIconnectTransaction(IconnectTransaction iconnectTransaction) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    ixuDbConnection.getSession().save(iconnectTransaction);

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<IconnectTransaction> findIconnectTransactionByTcbsId(String tcbsId) {
    Query<IconnectTransaction> query = ixuDbConnection.getSession().createQuery(
      "from IconnectTransaction a where a.tcbsId=:tcbsId ",
      IconnectTransaction.class);
    query.setParameter("tcbsId", tcbsId);
    List<IconnectTransaction> result = query.getResultList();
    if (result.size() > 0) {
      ixuDbConnection.closeSession();
      return result;
    } else {
      ixuDbConnection.closeSession();
      return null;
    }
  }

}
