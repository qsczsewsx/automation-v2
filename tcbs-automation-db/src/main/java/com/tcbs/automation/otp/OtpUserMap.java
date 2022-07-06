package com.tcbs.automation.otp;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@Table(name = "OTP_USER_MAP")
@Entity
public class OtpUserMap {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private long id;
  @Column(name = "USER_ID")
  private String userId;
  @Column(name = "TYPE_NAME")
  private String typeName;
  @Column(name = "IS_ENABLED")
  private Integer isEnabled;
  @Column(name = "IS_DEFAULT")
  private Integer isDefault;
  @Column(name = "UPDATED_AT")
  private Timestamp updatedAt;

  @Step("Get all otp type from user map")
  public static OtpUserMap getByUserId(String id) {
    Session session = OtpRepository.otpDbConnection.getSession();
    session.clear();

    Query<OtpUserMap> query = session.createQuery("from OtpUserMap where userId=:id", OtpUserMap.class);
    query.setParameter("id", id);

    try {
      OtpUserMap result = query.getSingleResult();
      return result;
    } catch (Exception e) {
      return null;
    }
  }

  @Step
  public static OtpUserMap getUserMapByTcbsId(String userId) {
    Session session = OtpRepository.otpDbConnection.getSession();
    session.clear();
    Query<OtpUserMap> query = session
      .createQuery("from OtpUserMap a where a.userId=:userId order by id desc ", OtpUserMap.class);
    query.setMaxResults(1);
    query.setParameter("userId", userId);
    return query.getSingleResult();
  }

  public static List<OtpUserMap> getListUserMapByTcbsId(String userId) {
    Session session = OtpRepository.otpDbConnection.getSession();
    session.clear();
    Query<OtpUserMap> query = session
      .createQuery("from OtpUserMap a where a.userId=:userId order by id desc ", OtpUserMap.class);
    query.setParameter("userId", userId);
    return query.getResultList();
  }

  @Step
  public static List<OtpUserMap> coutUserMapByTcbsId(String userId) {
    Session session = OtpRepository.otpDbConnection.getSession();
    session.clear();
    Query<OtpUserMap> query = session
      .createQuery("from OtpUserMap a where a.userId=:userId order by id desc ", OtpUserMap.class);
    query.setParameter("userId", userId);

    return query.getResultList();
  }

  public static List<OtpUserMap> getListUserMapByTcbsIdTOTPTrue(String userId) {
    Session session = OtpRepository.otpDbConnection.getSession();
    session.clear();
    Query<OtpUserMap> query = session
      .createQuery("from OtpUserMap a where a.userId=:userId", OtpUserMap.class);
    query.setParameter("userId", userId);
    return query.getResultList();
  }
}
