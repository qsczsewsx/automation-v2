package com.tcbs.automation.isquare;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

@Entity
@Table(name = "UM_USER")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class UmUserEntity implements Serializable {

  public static final String DEFAULT_USERNAME = "autotest@gmail.com";
  public static final String DEFAULT_PASSWORD = "Autotest@12345";
  public static final String DEFAULT_HASHED_PASSWORD = "6FeDO89CW6ulxstUrqV1jk1D4i0jpyujpe84E9bcGH0=";
  public static final String DEFAULT_SALT = "0Rh92O4B5vAR5qAulY2kfA==";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "userAccountSequence")
  @SequenceGenerator(name = "userAccountSequence", sequenceName = "UM_USER_SEQUENCE", initialValue = 1, allocationSize = 1)
  @Column(name = "UM_ID")
  private Long id;
  @Column(name = "UM_USER_NAME")
  private String userName;
  @Column(name = "UM_USER_PASSWORD")
  private String password;
  @Column(name = "UM_SALT_VALUE")
  private String saltValue;
  @Column(name = "UM_REQUIRE_CHANGE")
  private Long requireChange;
  @Column(name = "UM_CHANGED_TIME")
  private Date changedTime;
  @Id
  @Column(name = "UM_TENANT_ID")
  private Long tenantId;
  @Column(name = "EMAIL")
  private String email;
  @Column(name = "FULL_NAME")
  private String fullName;
  @Column(name = "ADDRESS")
  private String address;
  @Column(name = "PHONE")
  private String phone;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "LAST_MODIFIED_DATE")
  private Date lastModifiedDate;

  public static Optional<UmUserEntity> getUserByUsername(String username) {
    Session session = ISquare.iSquareDbConnection.getSession();
    session.clear();

    Query<UmUserEntity> query = session.createQuery("FROM UmUserEntity WHERE userName = :username");
    query.setParameter("username", username);

    try {
      UmUserEntity result = query.getSingleResult();
      ISquare.iSquareDbConnection.closeSession();
      return Optional.of(result);
    } catch (NoResultException e) {
      ISquare.iSquareDbConnection.closeSession();
      return Optional.empty();
    }
  }

  public static Long insertDefaultUser() {
    try {
      Optional<UmUserEntity> optional = getUserByUsername(DEFAULT_USERNAME);
      UmUserEntity umUserEntity;

      Session session = ISquare.iSquareDbConnection.getSession();
      session.clear();

      Transaction transaction = session.beginTransaction();

      if (optional.isPresent()) {
        umUserEntity = optional.get();
        if (!DEFAULT_HASHED_PASSWORD.equals(umUserEntity.getPassword())) {
          umUserEntity.setPassword(DEFAULT_HASHED_PASSWORD);
        }
      } else {
        umUserEntity = UmUserEntity.builder()
          .userName(DEFAULT_USERNAME)
          .password(DEFAULT_HASHED_PASSWORD)
          .saltValue(DEFAULT_SALT)
          .tenantId(-1234L)
          .status("ACTIVE")
          .changedTime(new Date())
          .build();
      }

      session.saveOrUpdate(umUserEntity);
      transaction.commit();
      optional = getUserByUsername(DEFAULT_USERNAME);
      if (!optional.isPresent()) {
        return null;
      }
      umUserEntity = optional.get();
      return umUserEntity.getId();
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    } finally {
      ISquare.iSquareDbConnection.closeSession();
    }
  }
}
