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
import java.time.ZonedDateTime;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "BIRTHDAY_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BirthdayTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "POINT")
  private String point;
  @Column(name = "AWARD_TYPE")
  private String awardType;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "RULE_OUTPUT")
  private String ruleOutput;
  @Column(name = "USER_ID")
  private String userId;
  @Column(name = "CODE105C")
  private String code105C;
  @Column(name = "BIRTHDAY")
  private String birthday;
  @Column(name = "CUSTOMER_NAME")
  private String customerName;
  @Column(name = "MEMBERSHIP_NAME")
  private String membershipName;
  @Column(name = "NOTIFY_MESSAGE")
  private String notifyMessage;
  @Column(name = "ISSUE_DATE")
  private ZonedDateTime issueDate;

  @Step
  public static void clearGL(List<String> tcbsId) {
    TcXu.ixuDbConnection.getSession().clear();
    Session session = TcXu.ixuDbConnection.getSession();
    StringBuilder stringBuilder = new StringBuilder();
    session.beginTransaction();
    stringBuilder.append("delete from BIRTHDAY_TRANSACTION a where ");
    stringBuilder.append(SqlUtils.getTcbsidListForSql(tcbsId, "TCBS_ID"));
    Query query = session.createNativeQuery(stringBuilder.toString());
    query.executeUpdate();

    session.getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void insertBirthdayTransaction(BirthdayTransaction birthdayTransaction) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    ixuDbConnection.getSession().save(birthdayTransaction);

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<BirthdayTransaction> findBirthdayTransactionByTcbsId(String tcbsId) {
    Query<BirthdayTransaction> query = ixuDbConnection.getSession().createQuery(
      "from BirthdayTransaction a where a.tcbsId=:tcbsId ",
      BirthdayTransaction.class);
    query.setParameter("tcbsId", tcbsId);
    List<BirthdayTransaction> result = query.getResultList();
    if (result.size() > 0) {
      ixuDbConnection.closeSession();
      return result;
    } else {
      ixuDbConnection.closeSession();
      return null;
    }
  }

}
