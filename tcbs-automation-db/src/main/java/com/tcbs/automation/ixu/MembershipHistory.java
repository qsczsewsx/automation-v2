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
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "IXU_MEMBERSHIP_HISTORY")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MembershipHistory {
  @Id
  @NotNull
  @Column(name = "ID")
  private int id;
  @Column(name = "TCBSID")
  private String tcbsId;
  @Column(name = "CREATED_DATE")
  private Time createdDate;
  @Column(name = "APPLIED_DATE")
  private Date appliedDate;
  @Column(name = "EXPIRED_DATE")
  private Date expiredDate;
  @Column(name = "MEMBERSHIP_TYPE_ID")
  private int membershipTypeId;

  @Step
  public static MembershipHistory getMembershipLatestByTcbsId(String tcbsId) {
    Session session = ixuDbConnection.getSession();

    Query<MembershipHistory> query = session.createQuery(
      "from MembershipHistory a where a.tcbsId in :TCBSID order by a.id desc ",
      MembershipHistory.class);
    query.setParameter("TCBSID", tcbsId);
    List<MembershipHistory> result = query.getResultList();
    ixuDbConnection.closeSession();
    return result.get(0);
  }

  @Step
  public static void updateMembershipHistory(String expiredDate, int id) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "update MembershipHistory a set a.expiredDate = to_date(:expiredDate, 'yyyy-MM-dd') where a.id=:id");
    query.setParameter("expiredDate", expiredDate);
    query.setParameter("id", id);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void updateMembershipHistoryWithApplyDate(String appliedDate, int id) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "update MembershipHistory a set a.appliedDate = to_date(:appliedDate, 'yyyy-MM-dd') where a.id=:id");
    query.setParameter("appliedDate", appliedDate);
    query.setParameter("id", id);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void insert(String tcbsId, String appliedDate, String expiredDate, Integer membership) throws ParseException {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    MembershipHistory membershipHistory = new MembershipHistory();
    membershipHistory.setAppliedDate(Date.valueOf(appliedDate));
    membershipHistory.setExpiredDate(Date.valueOf(expiredDate));
    membershipHistory.setMembershipTypeId(membership);
    membershipHistory.setTcbsId(tcbsId);
    ixuDbConnection.getSession().save(membershipHistory);

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<MembershipHistory> getMembershipLatestByTcbsIdAndDate(String tcbsId, String date) {
    Query<MembershipHistory> query = ixuDbConnection.getSession().createNativeQuery(String.format(
      "select * from (select * from IXU_MEMBERSHIP_HISTORY where TCBSID='%s' and APPLIED_DATE <= to_date('%s', 'yyyy-MM-dd') order by APPLIED_DATE desc, CREATED_DATE desc) where ROWNUM=1 ",
      tcbsId, date), MembershipHistory.class);
    List<MembershipHistory> result = query.getResultList();
    if (result.size() > 0) {
      ixuDbConnection.closeSession();
      return result;
    } else {
      ixuDbConnection.closeSession();
      return null;
    }
  }

  @Step
  public static List<MembershipHistory> getMembershipList(String tcbsId) {
    Query<MembershipHistory> query = ixuDbConnection.getSession().createNativeQuery(String.format(
      "select * from IXU_MEMBERSHIP_HISTORY where TCBSID='%s' order by APPLIED_DATE desc, CREATED_DATE desc",
      tcbsId), MembershipHistory.class);
    List<MembershipHistory> result = query.getResultList();
    if (result.size() > 0) {
      ixuDbConnection.closeSession();
      return result;
    } else {
      ixuDbConnection.closeSession();
      return null;
    }
  }

  @Step
  public static List<MembershipHistory> get10000MembershipLatestByTcbsId(List<String> tcbsId) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    java.util.Date currentDate = new java.util.Date();
    Session session = ixuDbConnection.getSession();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(
      "SELECT TCBSID, APPLIED_DATE , ID, MEMBERSHIP_TYPE_ID, EXPIRED_DATE, CREATED_DATE FROM (SELECT imh.TCBSID, imh.MEMBERSHIP_TYPE_ID, imh.CREATED_DATE, imh.ID , imh.APPLIED_DATE, imh.EXPIRED_DATE, rank() over (partition by imh.TCBSID order by imh.APPLIED_DATE desc,imh.CREATED_DATE desc) rnk FROM IXU_MEMBERSHIP_HISTORY imh  inner join IXU_MEMBERSHIP_TYPE imt ON imh.MEMBERSHIP_TYPE_ID  = imt.ID");
    stringBuilder.append(" WHERE imh.APPLIED_DATE <=To_Date('" + sdf.format(currentDate) + "','YYYY-MM-DD') AND ");
    int size = tcbsId.size();
    for (int i = 0; i < size; i++) {
      if (i != size - 1) {
        stringBuilder.append("imh.tcbsId = '" + tcbsId.get(i) + "' OR ");
      } else {
        stringBuilder.append("imh.tcbsId = '" + tcbsId.get(i) + "') where rnk=1");
      }
    }
    Query<MembershipHistory> query = session.createNativeQuery(stringBuilder.toString(), MembershipHistory.class);
    List<MembershipHistory> result = query.getResultList();
    if (result.size() > 0) {
      ixuDbConnection.closeSession();
      return result;
    } else {
      ixuDbConnection.closeSession();
      return null;
    }
  }

  @Step
  public static void deleteMembershipLatestByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().beginTransaction();
    Session session = ixuDbConnection.getSession();

    Query<MembershipHistory> query = session.createQuery(
      "delete from MembershipHistory a where a.tcbsId = :TCBSID ");
    query.setParameter("TCBSID", tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();

  }
}
