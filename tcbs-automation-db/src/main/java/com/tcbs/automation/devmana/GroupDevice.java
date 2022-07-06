package com.tcbs.automation.devmana;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.HashMap;

@Getter
@Setter
@Entity
@Table(name = "group_device")
public class GroupDevice {
  public static final String TCBS_ID = "tcbsID";
  @Id
  @Column(name = "id")
  private Integer id;
  @Column(name = "group_name")
  private String groupName;
  @Column(name = "notification_key")
  private String notificationKey;
  @Column(name = "tcbs_id")
  private String tcbsID;
  @Column(name = "status")
  private Integer status;
  @Column(name = "created_at")
  private Timestamp createdAt;
  @Column(name = "last_update_at")
  private Timestamp lastUpdatedAt;

  public GroupDevice get(HashMap hashMap) {
    Session session = DevmanaService.devmanaConnection.getSession();
    session.clear();
    StringBuilder stringBuilder = new StringBuilder("from GroupDevice a where 1 = 1 ");
    if (hashMap.get(TCBS_ID) != null) {
      stringBuilder.append(" AND tcbsID = :tcbsID ");
    }

    Query<GroupDevice> query = session.createQuery(stringBuilder.toString());
    if (hashMap.get(TCBS_ID) != null) {
      query.setParameter(TCBS_ID, hashMap.get(TCBS_ID));
    }
    return query.getSingleResult();
  }

  public int deleteGroup(String tcbsID) {
    Session session = DevmanaService.devmanaConnection.getSession();
    try {
      session.clear();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      StringBuilder stringBuilder = new StringBuilder("delete from GroupDevice where tcbsID =:tcbsID ");

      Query query = session.createQuery(stringBuilder.toString());
      query.setParameter(TCBS_ID, tcbsID);

      int result = query.executeUpdate();

      session.getTransaction().commit();
      return result;
    } catch (Exception e) {
      session.getTransaction().rollback();
      return 0;
    }
  }

  public void update(String tcbsID, String notificationKey) {
    Session session = DevmanaService.devmanaConnection.getSession();
    ;
    try {
      session.clear();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      StringBuilder stringBuilder = new StringBuilder("update GroupDevice set notificationKey =:notificationKey where tcbsID =:tcbsID ");

      Query query = session.createQuery(stringBuilder.toString());
      query.setParameter(TCBS_ID, tcbsID);
      query.setParameter("notificationKey", notificationKey);

      int result = query.executeUpdate();

      session.getTransaction().commit();
    } catch (NullPointerException e) {
      session.getTransaction().rollback();
      return;
    }
  }
}
