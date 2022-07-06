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
@Table(name = "user_topic")
public class UserTopic {
  public static final String TCBS_ID = "tcbsID";
  public static final String DEVICE_UUID = "deviceUuid";
  public static final String TOPIC_NAME = "topicName";
  @Id
  @Column(name = "id")
  private Integer id;
  @Column(name = "tcbs_id")
  private String tcbsID;
  @Column(name = "device_token")
  private String deviceToken;
  @Column(name = "topic_id")
  private Integer topicID;
  @Column(name = "device_uuid")
  private String deviceUuid;
  @Column(name = "status")
  private Integer status;
  @Column(name = "created_at")
  private Timestamp createdAt;
  @Column(name = "last_update_at")
  private Timestamp lastUpdateAt;

  public UserTopic get(HashMap hashMap) {
    Session session = DevmanaService.devmanaConnection.getSession();
    session.clear();
    StringBuilder stringBuilder = new StringBuilder("from UserTopic a where 1 = 1 ");
    if (hashMap.get(TCBS_ID) != null) {
      stringBuilder.append(" AND tcbsID = :tcbsID ");
    }
    if (hashMap.get(DEVICE_UUID) != null) {
      stringBuilder.append(" AND deviceUuid = :deviceUuid ");
    }
    if (hashMap.get(TOPIC_NAME) != null) {
      stringBuilder.append(" AND topicID = (select id from Topic where name = :topicName and status =1) ");
    }

    Query<UserTopic> query = session.createQuery(stringBuilder.toString());
    if (hashMap.get(TCBS_ID) != null) {
      query.setParameter(TCBS_ID, hashMap.get(TCBS_ID));
    }
    if (hashMap.get(DEVICE_UUID) != null) {
      query.setParameter(DEVICE_UUID, hashMap.get(DEVICE_UUID));
    }
    if (hashMap.get(TOPIC_NAME) != null) {
      query.setParameter(TOPIC_NAME, hashMap.get(TOPIC_NAME));
    }
    return query.getSingleResult();
  }
}
