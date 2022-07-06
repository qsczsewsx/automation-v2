package com.tcbs.automation.ligo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Session;
import org.hibernate.query.Query;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "user_setting")
public class UserSetting {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "id")
  private Long id;
  @NotNull
  @Column(name = "tcbs_id")
  private String tcbsId;
  @Column(name = "settings")
  private String settings;

  public static UserSetting getUserSettingByTcbsId(String tcbsId) {
    Session session = LigoRepository.ligoDbConnection.getSession();
    session.clear();
    Query<UserSetting> query = session.createQuery("from UserSetting w where w.tcbsId = :tcbsId");
    query.setParameter("tcbsId", tcbsId);
    return query.getSingleResult();
  }
}
