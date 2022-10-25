package com.automation.cas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "xxxx_AUTHEN_INFO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class xxxxAuthenInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "SESSION_ID")
  private String sessionId;
  @Column(name = "IP")
  private String ip;
  @Column(name = "DEVICE")
  private String device;
  @Column(name = "LOGIN_TIME")
  private Timestamp loginTime;
  @Column(name = "USER_ID")
  private String userId;
  @Column(name = "REGISTERED_CLIENT_ID")
  private Integer registeredClientId;

  public static List<xxxxAuthenInfo> getSessionByUserId(String userId) {
    CAS.casConnection.getSession().clear();
    Query<xxxxAuthenInfo> query = CAS.casConnection.getSession().createQuery(
      "from xxxxAuthenInfo a where a.userId=:userId", xxxxAuthenInfo.class);
    query.setParameter("userId", userId);
    return query.getResultList();
  }

  public static xxxxAuthenInfo getBySessionId(String sessionId) {
    CAS.casConnection.getSession().clear();
    Query<xxxxAuthenInfo> query = CAS.casConnection.getSession().createNativeQuery(
      "SELECT SESSION_ID, IP, DEVICE, LOGIN_TIME, USER_ID, REGISTERED_CLIENT_ID FROM xxxx_AUTHEN_INFO WHERE SESSION_ID = :sessionId", xxxxAuthenInfo.class);
    query.setParameter("sessionId", sessionId);
    return query.getSingleResult();
  }
}
