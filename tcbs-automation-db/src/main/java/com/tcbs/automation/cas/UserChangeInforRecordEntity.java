package com.tcbs.automation.cas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;

import static com.tcbs.automation.cas.CAS.casConnection;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "TCBS_USER_CHANGE_INFOR_RECORD")
public class UserChangeInforRecordEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "TCBSID")
  private String tcbsId;

  @Column(name = "STATUS")
  private Integer status;

  @Column(name = "PROCESS_INSTANCE_ID")
  private String processInstanceId;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "UPDATED_DATE")
  private Date updatedDate;

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
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("status", status);
    return query.getSingleResult();
  }

  public static void updateStatusDoneForAllByTcbsid(String tcbsId) {
    Session session = getSession();
    Query query = session.createNativeQuery("UPDATE TCBS_USER_CHANGE_INFOR_RECORD SET STATUS = 1 WHERE  TCBSID=:tcbsId");
    query.setParameter("tcbsId", tcbsId);
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

}
