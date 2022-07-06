package com.tcbs.automation.isquare;

import lombok.*;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Getter
@Setter
@Table(name = "IS_AUTHEN_INFO")
@AllArgsConstructor
@NoArgsConstructor
public class IsAuthenInfoEntity {

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
  private Date loginTime;
  @Column(name = "USER_ID")
  private String userId;

  @Step
  public static List<IsAuthenInfoEntity> getListBySessionId(String userId) {
    Session session = ISquare.iSquareDbConnection.getSession();
    session.clear();
    Query<IsAuthenInfoEntity> query = session.createQuery(
      "from IsAuthenInfoEntity a where a.userId=:userId order by loginTime DESC", IsAuthenInfoEntity.class);
    query.setParameter("userId", userId);
    return query.getResultList();
  }
}
