package com.tcbs.automation.cas;

import lombok.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.cas.CAS.casConnection;

@Entity
@Table(name = "TCBS_USER_CHANGE_INFOR_RECORD")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserChangeInforRecordEntity {
  private static Logger logger = LoggerFactory.getLogger(UserChangeInforRecordEntity.class);
  @Id
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @NotNull
  @Column(name = "TCBSID")
  private String tcbsId;
  @NotNull
  @Column(name = "STATUS")
  private BigDecimal status;
  @Column(name = "UPDATED_DATE")
  private Timestamp updatedDate;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "PROCESS_INSTANCE_ID")
  private String processInstanceId;
  @Column(name = "TYPE")
  private String type;
  @Column(name = "NEW_PHONE")
  private String newPhone;
  @Column(name = "NEW_EMAIL")
  private String newEmail;
  @Column(name = "NEW_IDENTITY_NO")
  private String newIdentityNo;
  @Column(name = "NEW_BANK_ACCOUNT_NO")
  private String newBankAccountNo;

  private static final String DATA_TCBSID = "tcbsId";

  private static Session getSession() {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    return casConnection.getSession();
  }

  public static Integer countByTcbsIdAndStatus(String tcbsId, Integer status) {
    Query<Integer> query = getSession().createNativeQuery(
      "SELECT COUNT(ID) FROM TCBS_USER_CHANGE_INFOR_RECORD WHERE TCBSID=:tcbsId AND STATUS=:status", Integer.class);
    query.setParameter(DATA_TCBSID, tcbsId);
    query.setParameter("status", status);
    return query.getSingleResult();
  }

  public static void updateStatusDoneForAllByTcbsid(String tcbsId) {
    Session session = getSession();
    Query query = session.createNativeQuery("UPDATE TCBS_USER_CHANGE_INFOR_RECORD SET STATUS = 1 WHERE  TCBSID=:tcbsId");
    query.setParameter(DATA_TCBSID, tcbsId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void insert(String tcbsId, Integer status) {
    Session session = getSession();
    Query query = session.createNativeQuery(
      "INSERT INTO TCBS_USER_CHANGE_INFOR_RECORD (ID, TCBSID, STATUS, UPDATED_DATE, CREATED_DATE, PROCESS_INSTANCE_ID) VALUES(DEFAULT, ?, ?, SYSDATE, SYSDATE, '')");
    query.setParameter(1, tcbsId);
    query.setParameter(2, status);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static List<UserChangeInforRecordEntity> getListByTcbsIdAndType(String tcbsId, String type) {
    Query<UserChangeInforRecordEntity> query = CAS.casConnection.getSession().createQuery(
      "from UserChangeInforRecordEntity a where a.tcbsId=:tcbsId and a.type=:type order by id desc ", UserChangeInforRecordEntity.class);
    query.setParameter(DATA_TCBSID, tcbsId);
    query.setParameter("type", type);
    return query.getResultList();

  }

  public static void deleteByTcbsIdAndType(String tcbsId, String type) {
    try {
      Session session = CAS.casConnection.getSession();
      Transaction trans = session.beginTransaction();

      Query<?> query = session.createQuery("DELETE UserChangeInforRecordEntity WHERE tcbsId=:tcbsId AND type=:type");
      query.setParameter(DATA_TCBSID, tcbsId);
      query.setParameter("type", type);
      query.executeUpdate();
      trans.commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

}
