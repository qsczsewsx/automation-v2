package com.tcbs.automation.ixu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "COMMISSION_IWP_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CommissionIwpTransaction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "GROSS_VALUE")
  private String grossValue;
  @Column(name = "TAX")
  private String tax;
  @Column(name = "PERIOD")
  private Timestamp period;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Step
  public static void deleteByTcbsIdAndPeriod(String tcbsId, String period) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    Transaction transaction = session.beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from CommissionIwpTransaction a where a.tcbsId = :tcbsId and TRUNC(a.period) = to_date(:period,'YYYY-MM-DD')"
    );
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("period", period);
    query.executeUpdate();
    transaction.commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static CommissionIwpTransaction findTransactionByTcbsId(String tcbsId, String period) {
    ixuDbConnection.getSession().clear();
    Query<CommissionIwpTransaction> query = ixuDbConnection.getSession().createQuery(
      "from CommissionIwpTransaction a where a.tcbsId = :tcbsId and TRUNC(a.period) = to_date(:period,'YYYY-MM-DD')"
    );
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("period", period);
    CommissionIwpTransaction commissionIwpTransaction = query.getSingleResult();
    ixuDbConnection.closeSession();
    if (commissionIwpTransaction == null) {
      return null;
    }
    return commissionIwpTransaction;
  }
}
