package com.tcbs.automation.cas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import static com.tcbs.automation.cas.CAS.casConnection;

@Entity
@Table(name = "TCBS_APPLICATION_USER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TcbsApplicationUser {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "USER_ID")
  private BigDecimal userId;
  @Column(name = "APP_ID")
  private BigDecimal appId;
  @Column(name = "USER_APP_ID")
  private String userAppId;
  @Column(name = "STATUS")
  private BigDecimal status;
  @Column(name = "ACTORID")
  private BigDecimal actorid;
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;
  @Column(name = "CREATED_DATE")
  private Date createdDate;

  private static final String DATA_USER_ID = "userId";
  private static final String DATA_APP_ID = "appId";

  @Step
  public static TcbsApplicationUser getByTcbsApplicationUserAppId2(String userId, String appId) {
    Query<TcbsApplicationUser> query = CAS.casConnection.getSession().createQuery(
      "from TcbsApplicationUser a where a.userId=:userId and a.appId=:appId", TcbsApplicationUser.class);
    query.setParameter(DATA_USER_ID, new BigDecimal(userId));
    query.setParameter(DATA_APP_ID, new BigDecimal(appId));
    return query.getSingleResult();
  }

  public static List<TcbsApplicationUser> getByUserAppId(String userAppId, String appId) {
    Query<TcbsApplicationUser> query = CAS.casConnection.getSession().createQuery(
      "from TcbsApplicationUser a where a.userAppId=:userAppId and a.appId=:appId", TcbsApplicationUser.class);
    query.setParameter("userAppId", userAppId);
    query.setParameter(DATA_APP_ID, new BigDecimal(appId));
    return query.getResultList();
  }

  public static List<TcbsApplicationUser> getByAppIdAndStatus(String appId, String status) {
    Query<TcbsApplicationUser> query = CAS.casConnection.getSession().createQuery(
      "from TcbsApplicationUser a where a.appId=:appId and a.status=:status", TcbsApplicationUser.class);
    query.setParameter(DATA_APP_ID, new BigDecimal(appId));
    query.setParameter("status", new BigDecimal(status));
    return query.getResultList();
  }

  @Step
  public static void updateStatusApp(String userId, String appId, String status) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    Query<?> query = casConnection.getSession().createQuery(
      "Update TcbsApplicationUser a set a.status =:status where a.userId=:userId and a.appId =:appId ");
    query.setParameter(DATA_USER_ID, new BigDecimal(userId));
    query.setParameter(DATA_APP_ID, new BigDecimal(appId));
    query.setParameter("status", new BigDecimal(status));
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  @Step
  public static void updateStatusBy105CAndAppId(String code, String appId, String status) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    Query query = casConnection.getSession().createNativeQuery(
      "UPDATE TCBS_APPLICATION_USER SET STATUS =?1 WHERE APP_ID =?2 AND USER_ID=(SELECT ID FROM TCBS_USER WHERE USERNAME=?3)");
    query.setParameter(1, new BigDecimal(status));
    query.setParameter(2, new BigDecimal(appId));
    query.setParameter(3, code);
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  @Step
  public static void updateStatusAppByList(List<String> code105CList, String appId, String status) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    Query<?> query = casConnection.getSession().createNativeQuery(
      "UPDATE TCBS_APPLICATION_USER SET STATUS = ?1 WHERE APP_ID = ?2 AND USER_ID IN (SELECT ID FROM TCBS_USER WHERE USERNAME IN (?3))");
    query.setParameter(1, new BigDecimal(status));
    query.setParameter(2, new BigDecimal(appId));
    query.setParameter(3, code105CList);
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  public static List<TcbsApplicationUser> getUserByMultiAppidStandard(String wantedAppid, String withStatus, String unwantedAppid) {
    casConnection.getSession().clear();
    Query<TcbsApplicationUser> query = CAS.casConnection.getSession().createQuery(
      "from TcbsApplicationUser a where a.appId =: wantedAppid and a.status =: withStatus and a.userId not in (select userId from TcbsApplicationUser b where b.appId =: unwantedAppid)",
      TcbsApplicationUser.class
    );
    query.setParameter("wantedAppid", new BigDecimal(wantedAppid));
    query.setParameter("withStatus", new BigDecimal(withStatus));
    query.setParameter("unwantedAppid", new BigDecimal(unwantedAppid));

    return query.getResultList();
  }

  public static List<TcbsApplicationUser> getByUserId(String userId) {
    Query<TcbsApplicationUser> query = CAS.casConnection.getSession().createQuery(
      "from TcbsApplicationUser a where a.userId=:userId", TcbsApplicationUser.class);
    query.setParameter(DATA_USER_ID, new BigDecimal(userId));

    return query.getResultList();
  }

  public static List<TcbsApplicationUser> getByUserName(String username) {
    Query<TcbsApplicationUser> query = CAS.casConnection.getSession().createQuery(
      "from TcbsApplicationUser a where a.username=:username", TcbsApplicationUser.class);
    query.setParameter("username", new BigDecimal(username));

    return query.getResultList();
  }

}

