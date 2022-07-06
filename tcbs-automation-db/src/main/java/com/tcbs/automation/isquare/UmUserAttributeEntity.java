package com.tcbs.automation.isquare;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "UM_USER_ATTRIBUTE")
@Slf4j
public class UmUserAttributeEntity implements Serializable {

  private static final String DEFAULT_ATTR_NAME = "cusuid";
  private static final String DEFAULT_ATTR_VALUE = "1";
  private static final long DEFAULT_USER_ID = 1L;

  @Id
  @Column(name = "UM_ID")
  private Long umId;
  @Column(name = "UM_ATTR_NAME")
  private String umAttrName;
  @Column(name = "UM_ATTR_VALUE")
  private String umAttrValue;
  @Column(name = "UM_PROFILE_ID")
  private String umProfileId;
  @Column(name = "UM_USER_ID")
  private Long umUserId;
  @Id
  @Column(name = "UM_TENANT_ID")
  private Long umTenantId;

  public static Optional<UmUserAttributeEntity> getDefaultUserAttr(Long umUserId) {
    if (umUserId == null) {
      umUserId = DEFAULT_USER_ID;
    }
    Session session = ISquare.iSquareDbConnection.getSession();
    session.clear();

    Query<UmUserAttributeEntity> query = session.createQuery(
      "FROM UmUserAttributeEntity WHERE (umAttrName = :attrName)" +
        " AND (umAttrValue = :attrValue)" +
        " AND (umUserId = :userId)"
    );
    query.setParameter("attrName", DEFAULT_ATTR_NAME);
    query.setParameter("attrValue", DEFAULT_ATTR_VALUE);
    query.setParameter("userId", umUserId);

    try {
      UmUserAttributeEntity result = query.getSingleResult();
      ISquare.iSquareDbConnection.closeSession();
      return Optional.of(result);
    } catch (NoResultException e) {
      ISquare.iSquareDbConnection.closeSession();
      return Optional.empty();
    }
  }

  public static void insertDefaultUserAttr() {
    if (getDefaultUserAttr(null).isPresent()) {
      return;
    }

    try {
      Session session = ISquare.iSquareDbConnection.getSession();
      session.clear();

      Transaction transaction = session.beginTransaction();

      UmUserAttributeEntity umUserAttributeEntity = UmUserAttributeEntity.builder()
        .umAttrName(DEFAULT_ATTR_NAME)
        .umAttrValue(DEFAULT_ATTR_VALUE)
        .umUserId(DEFAULT_USER_ID)
        .umProfileId("default")
        .umTenantId(-1234L)
        .build();

      session.save(umUserAttributeEntity);
      transaction.commit();
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    } finally {
      ISquare.iSquareDbConnection.closeSession();
    }
  }

  public static void insertDefaultUserAttr(Long umUserId) {
    if (getDefaultUserAttr(umUserId).isPresent()) {
      return;
    }
    try {
      Session session = ISquare.iSquareDbConnection.getSession();
      Transaction transaction = session.beginTransaction();
      UmUserAttributeEntity umUserAttributeEntity = UmUserAttributeEntity.builder()
        .umAttrName(DEFAULT_ATTR_NAME)
        .umAttrValue(DEFAULT_ATTR_VALUE)
        .umUserId(umUserId)
        .umProfileId("default")
        .umTenantId(-1234L)
        .build();
      session.save(umUserAttributeEntity);
      transaction.commit();
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    } finally {
      ISquare.iSquareDbConnection.closeSession();
    }
  }
}
