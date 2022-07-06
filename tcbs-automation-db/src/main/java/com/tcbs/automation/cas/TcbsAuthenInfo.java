package com.tcbs.automation.cas;

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
@Table(name = "TCBS_AUTHEN_INFO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TcbsAuthenInfo {

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

  public static List<TcbsAuthenInfo> getSessionByUserId(String userId) {
    CAS.casConnection.getSession().clear();
    Query<TcbsAuthenInfo> query = CAS.casConnection.getSession().createQuery(
      "from TcbsAuthenInfo a where a.userId=:userId", TcbsAuthenInfo.class);
    query.setParameter("userId", userId);
    return query.getResultList();
  }
}
