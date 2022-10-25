package com.automation.cas;

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

@Entity
@Table(name = "xxxx_USER_CHANGE_INFOR_RECORD")
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
  @Column(name = "xxxxID")
  private String xxxxId;
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

  private static final String DATA_xxxxID = "xxxxId";

  private static Session getSession() {
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    return CAS.casConnection.getSession();
  }

  public static Integer countByxxxxIdAndStatus(String xxxxId, Integer status) {
    Query<Integer> query = getSession().createNativeQuery(
      "SELECT COUNT(ID) FROM xxxx_USER_CHANGE_INFOR_RECORD WHERE xxxxID=:xxxxId AND STATUS=:status", Integer.class);
    query.setParameter(DATA_xxxxID, xxxxId);
    query.setParameter("status", status);
    return query.getSingleResult();
  }

  public static void updateStatusDoneForAllByxxxxid(String xxxxId) {
    Session session = getSession();
    Query query = session.createNativeQuery("UPDATE xxxx_USER_CHANGE_INFOR_RECORD SET STATUS = 1 WHERE  xxxxID=:xxxxId");
    query.setParameter(DATA_xxxxID, xxxxId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void insert(String xxxxId, Integer status) {
    Session session = getSession();
    Query query = session.createNativeQuery(
      "INSERT INTO xxxx_USER_CHANGE_INFOR_RECORD (ID, xxxxID, STATUS, UPDATED_DATE, CREATED_DATE, PROCESS_INSTANCE_ID) VALUES(DEFAULT, ?, ?, SYSDATE, SYSDATE, '')");
    query.setParameter(1, xxxxId);
    query.setParameter(2, status);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static List<UserChangeInforRecordEntity> getListByxxxxIdAndType(String xxxxId, String type) {
    Query<UserChangeInforRecordEntity> query = CAS.casConnection.getSession().createQuery(
      "from UserChangeInforRecordEntity a where a.xxxxId=:xxxxId and a.type=:type order by id desc ", UserChangeInforRecordEntity.class);
    query.setParameter(DATA_xxxxID, xxxxId);
    query.setParameter("type", type);
    return query.getResultList();

  }

  public static void deleteByxxxxIdAndType(String xxxxId, String type) {
    try {
      Session session = CAS.casConnection.getSession();
      Transaction trans = session.beginTransaction();

      Query<?> query = session.createQuery("DELETE UserChangeInforRecordEntity WHERE xxxxId=:xxxxId AND type=:type");
      query.setParameter(DATA_xxxxID, xxxxId);
      query.setParameter("type", type);
      query.executeUpdate();
      trans.commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

}
