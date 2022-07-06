package com.tcbs.automation.devmana;

import com.tcbs.automation.coman.Connection;
import com.tcbs.automation.coman.ReferBank;
import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user_device")
public class UserDevice {
  public static final String TCBS_ID = "tcbsID";
  public static final String DEVICE_UUID = "deviceUuid";
  public static final String DEVICE_TOKEN = "deviceToken";
  public static final String DEVICE_UUID_TABLE = "device_uuid";
  public static final String TCBSID = "tcbsId";
  @Id
  @Column(name = "id")
  private Integer id;
  @Column(name = "browser")
  private String browser;
  @Column(name = "device_token")
  private String deviceToken;
  @Column(name = "platform")
  private String platform;
  @Column(name = "device_uuid")
  private String deviceUuid;
  @Column(name = "tcbs_id")
  private String tcbsID;
  @Column(name = "ip")
  private String ip;
  @Column(name = "browser_version")
  private String browserVersion;
  @Column(name = "register_status")
  private Integer status;
  @Column(name = "last_update_at")
  private Timestamp lastUpdateAt;
  @Column(name = "created_at")
  private Timestamp createdAt;

  @Step
  public static List<UserDevice> getUserDeviceByUserId(String tcbsID) {
    Session session = DevmanaService.devmanaConnection.getSession();
    Query<UserDevice> query = session.getSession()
      .createQuery("from UserDevice a where a.tcbsID=:tcbsID order by id desc", UserDevice.class);
    query.setParameter(TCBS_ID, tcbsID);
    return query.getResultList();
  }

  public UserDevice getUserDevice(HashMap hashMap) {
    Session session = DevmanaService.devmanaConnection.getSession();
    session.clear();
    StringBuilder stringBuilder = new StringBuilder("from UserDevice a where 1 = 1 ");
    if (hashMap.get(TCBS_ID) != null) {
      stringBuilder.append(" AND tcbsID = :tcbsID ");
    }
    if (hashMap.get(DEVICE_UUID) != null) {
      stringBuilder.append(" AND deviceUuid = :deviceUuid ");
    }
    if (hashMap.get(DEVICE_TOKEN) != null) {
      stringBuilder.append(" AND deviceToken = :deviceToken ");
    }

    Query<UserDevice> query = session.createQuery(stringBuilder.toString());
    if (hashMap.get(TCBS_ID) != null) {
      query.setParameter(TCBS_ID, hashMap.get(TCBS_ID));
    }
    if (hashMap.get(DEVICE_UUID) != null) {
      query.setParameter(DEVICE_UUID, hashMap.get(DEVICE_UUID));
    }
    if (hashMap.get(DEVICE_TOKEN) != null) {
      query.setParameter(DEVICE_TOKEN, hashMap.get(DEVICE_TOKEN));
    }
    return query.getSingleResult();
  }

  public int deleteUserDevice(String tcbsID, String deviceUuid) {
    Session session = DevmanaService.devmanaConnection.getSession();
    try {
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      StringBuilder stringBuilder = new StringBuilder("delete from UserDevice where tcbsID =:tcbsID AND deviceUuid =:deviceUuid");

      Query query = session.createQuery(stringBuilder.toString());
      query.setParameter(TCBS_ID, tcbsID);
      query.setParameter(DEVICE_UUID, deviceUuid);

      int result = query.executeUpdate();

      session.getTransaction().commit();
      return result;
    } catch (Exception e) {
      session.getTransaction().rollback();
      return 0;
    }
  }

  public List<RegisterInfoDto> getRegisterInfo(String tcbsID) {
    Session session = DevmanaService.devmanaConnection.getSession();
    session.clear();
    StringBuilder stringBuilder = new StringBuilder("select gd.tcbs_id as tcbsId, gd.group_name as groupName,ud.device_uuid as device_uuid, ud.device_token as deviceToken" +
      "              , t.name as topic from group_device gd  \n" +
      "              inner join user_device ud on gd.tcbs_id = ud.tcbs_id \n" +
      "              inner join user_topic ut on ud.tcbs_id = ut.tcbs_id and ud.device_uuid = ut.device_uuid \n" +
      "              inner join topic t on ut.topic_id = t.id where gd.tcbs_id = :tcbsID ");
    Query query = session.createSQLQuery(stringBuilder.toString())
      .addScalar(TCBSID)
      .addScalar("groupName")
      .addScalar(DEVICE_UUID_TABLE)
      .addScalar(DEVICE_TOKEN)
      .addScalar("topic")
      .setResultTransformer(Transformers.aliasToBean(RegisterInfoDto.class));
    query.setParameter(TCBS_ID, tcbsID);
    return query.list();
  }

  public List<RegisterInfoDto> getListInactive() {
    Session session = DevmanaService.devmanaConnection.getSession();
    session.clear();
    StringBuilder stringBuilder = new StringBuilder("select tcbs_id as tcbsId, device_uuid from user_device where register_status = 0 or register_status is null ");
    Query query = session.createSQLQuery(stringBuilder.toString())
      .addScalar(TCBSID)
      .addScalar(DEVICE_UUID_TABLE)
      .setResultTransformer(Transformers.aliasToBean(RegisterInfoDto.class));
    return query.list();
  }

  public List<UserDevice> getListUserDeviceByUserId() {
    Session session = DevmanaService.devmanaConnection.getSession();
    session.clear();
    StringBuilder stringBuilder = new StringBuilder("select tcbs_id as tcbsId, device_uuid from user_device where register_status = 0 or register_status is null ");
    Query query = session.createSQLQuery(stringBuilder.toString())
      .addScalar(TCBSID)
      .addScalar(DEVICE_UUID_TABLE)
      .setResultTransformer(Transformers.aliasToBean(RegisterInfoDto.class));
    return query.list();
  }

  public static UserDevice getDeviceByUserIdAndDevice(String tcbsID, String deviceUuid) {
    Session session = DevmanaService.devmanaConnection.getSession();
    session.clear();
    Query<UserDevice> query = session.getSession()
      .createQuery("from UserDevice a where a.tcbsID=:tcbsID and a.deviceUuid =:deviceUuid order by id desc", UserDevice.class);
    query.setParameter(TCBS_ID, tcbsID);
    query.setParameter("deviceUuid", deviceUuid);
    return query.getSingleResult();
  }
}
