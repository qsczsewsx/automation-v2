package com.tcbs.automation.ixu;

import com.sun.istack.NotNull;
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
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "IXU_MEMBERSHIP_HISTORY")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class IxuMembershipHistoryEntity {
  @Id
  @NotNull
  @Column(name = "ID")
  private Long id;

  @Column(name = "TCBSID")
  private String tcbsid;

  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Column(name = "APPLIED_DATE")
  private Timestamp appliedDate;

  @Column(name = "EXPIRED_DATE")
  private Timestamp expiredDate;

  @Column(name = "MEMBERSHIP_TYPE_ID")
  private Integer membershipTypeId;

  @Step
  public static void clearMembership(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from IxuMembershipHistoryEntity gl where gl.tcbsid=:tcbsid");
    query.setParameter("tcbsid", tcbsId);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void clearMemberships(List<String> tcbsId, String appliedDate) {
    TcXu.ixuDbConnection.getSession().clear();
    Session session = TcXu.ixuDbConnection.getSession();
    StringBuilder stringBuilder = new StringBuilder();
    session.beginTransaction();
    Query query = session.createQuery("delete from IxuMembershipHistoryEntity where tcbsid in :tcbsId and appliedDate = to_date(:appliedDate, 'yyyy-MM-dd')",
      IxuMembershipHistoryEntity.class);
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("appliedDate", appliedDate);
    query.executeUpdate();

    session.getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void insertMembershipHistory(String tcbsId, String appliedDate, String expiredDate, int membershipTypeId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    javax.persistence.Query query = ixuDbConnection.getSession().createNativeQuery(String.format(
      "insert into IXU_MEMBERSHIP_HISTORY (TCBSID,CREATED_DATE, APPLIED_DATE ,EXPIRED_DATE, MEMBERSHIP_TYPE_ID)  values('%s', CURRENT_TIMESTAMP,TO_DATE('%s','yyyy-MM-dd'), TO_DATE('%s','yyyy-MM-dd'),%d) ",
      tcbsId, appliedDate, expiredDate, membershipTypeId));
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<IxuMembershipHistoryEntity> getIxuMembershipHistoriesByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query<IxuMembershipHistoryEntity> query = ixuDbConnection.getSession().createQuery(
      "from IxuMembershipHistoryEntity a where tcbsId = :tcbsId order by id desc", IxuMembershipHistoryEntity.class);
    query.setParameter("tcbsId", tcbsId);
    List<IxuMembershipHistoryEntity> results = query.getResultList();
    ixuDbConnection.closeSession();
    return results;
  }
}
