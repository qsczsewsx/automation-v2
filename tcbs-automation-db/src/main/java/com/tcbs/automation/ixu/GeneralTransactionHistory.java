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
@Table(name = "GENERAL_TRANSACTION_HISTORY")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GeneralTransactionHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "GENERAL_TRANSACTION_ID")
  private String generalTransactionId;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "DOER")
  private String doer;
  @Column(name = "DETAIL")
  private String detail;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Step
  public static void insertGeneralTransactionHistory(GeneralTransactionHistory generalTransactionHistory) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    ixuDbConnection.getSession().save(generalTransactionHistory);

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void clearGL(List<String> id) {
    ixuDbConnection.getSession().clear();
    Session session = ixuDbConnection.getSession();
    StringBuilder stringBuilder = new StringBuilder();
    session.beginTransaction();
    stringBuilder.append("delete from GENERAL_TRANSACTION_HISTORY a where ");
    stringBuilder.append(SqlUtils.getTcbsidListForSql(id, "GENERAL_TRANSACTION_ID"));

    Query query = session.createNativeQuery(stringBuilder.toString());
    query.executeUpdate();

    session.getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<GeneralTransactionHistory> getTransactionByIdGeneralTransaction(Long id) {
    ixuDbConnection.getSession().clear();
    Query query = ixuDbConnection.getSession()
      .createQuery("from GeneralTransactionHistory a where a.generalTransactionId = :id order by a.id desc", GeneralTransactionHistory.class);
    query.setParameter("id", String.valueOf(id));
    List<GeneralTransactionHistory> list = query.getResultList();
    return list;
  }

  @Step
  public static void clearGTHByGTId(Long idGeneralTransaction) {
    ixuDbConnection.getSession().clear();
    Session session = ixuDbConnection.getSession();
    session.beginTransaction();
    Query query = ixuDbConnection.getSession()
      .createQuery("delete from GeneralTransactionHistory a where a.generalTransactionId = :idGeneralTransaction");
    query.setParameter("idGeneralTransaction", idGeneralTransaction.toString());
    query.executeUpdate();
    session.getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<GeneralTransactionHistory> findGTHBygeneralTransactionId(String generalTransactionId) {
    Query<GeneralTransactionHistory> query = ixuDbConnection.getSession().createQuery(
      "from GeneralTransactionHistory a where a.generalTransactionId=:generalTransactionId ",
      GeneralTransactionHistory.class);
    query.setParameter("generalTransactionId", generalTransactionId);
    List<GeneralTransactionHistory> result = query.getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  @Step
  public static List<GeneralTransactionHistory> findGTHBygeneralTransactionIdAndStatus(String generalTransactionId, String status) {
    Query<GeneralTransactionHistory> query = ixuDbConnection.getSession().createQuery(
      "from GeneralTransactionHistory a where a.generalTransactionId=:generalTransactionId and a.status=:status",
      GeneralTransactionHistory.class);
    query.setParameter("generalTransactionId", generalTransactionId);
    query.setParameter("status", status);
    List<GeneralTransactionHistory> result = query.getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  public static GeneralTransactionHistory getLastGeneralTransactionHistory(String tcbsId, String referenceLoaction) {
    ixuDbConnection.getSession().clear();
    Query query = ixuDbConnection.getSession()
      .createQuery("select his from GeneralTransaction gen" +
        " inner  join  GeneralTransactionHistory his " +
        " on  gen.id = his.generalTransactionId " +
        " where gen.tcbsId = :tcbsId AND gen.referenceLocation = :location " +
        " order by his.id DESC ", GeneralTransactionHistory.class);
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("location", referenceLoaction);
    query.setMaxResults(1);
    List result = query.getResultList();
    if (result == null || result.isEmpty()) {
      return null;
    } else {
      return (GeneralTransactionHistory) result.get(0);
    }
  }
}
