package com.tcbs.automation.isquare;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "UM_USER_ROLE")
@Slf4j
public class UmUserRoleEntity implements Serializable {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "UmUserRoleSequence")
  @SequenceGenerator(name = "UmUserRoleSequence", sequenceName = "UM_USER_ROLE_SEQUENCE", initialValue = 1, allocationSize = 1)
  @Column(name = "UM_ID")
  private Long id;

  @Column(name = "UM_ROLE_ID")
  private Long roleId;

  @Column(name = "UM_USER_ID")
  private Long userId;

  @Id
  @Column(name = "UM_TENANT_ID")
  private Long umTenantId;

  @Column(name = "STATUS")
  private String status;

  public static UmUserRoleEntity getUserRole(Long userId, Long roleId) {
    try {
      Session session = ISquare.iSquareDbConnection.getSession();
      Query<UmUserRoleEntity> query = session.createQuery(
        "FROM UmUserRoleEntity WHERE userId = :userId AND roleId = :roleId"
      );
      query.setParameter("userId", userId);
      query.setParameter("roleId", roleId);
      List<UmUserRoleEntity> userRoleList = query.getResultList();
      if (userRoleList == null || CollectionUtils.isEmpty(userRoleList)) {
        return null;
      }
      return userRoleList.get(0);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    } finally {
      ISquare.iSquareDbConnection.closeSession();
    }
  }

  public static void insertUserRole(Long userId, Long roleId) {
    UmUserRoleEntity umUserRoleEntity = getUserRole(userId, roleId);
    if (umUserRoleEntity != null) {
      return;
    }
    try {
      Session session = ISquare.iSquareDbConnection.getSession();
      Transaction transaction = session.beginTransaction();
      umUserRoleEntity = UmUserRoleEntity.builder()
        .roleId(roleId)
        .userId(userId)
        .umTenantId(-1234L)
        .status("ACTIVE")
        .build();
      session.save(umUserRoleEntity);
      transaction.commit();
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    } finally {
      ISquare.iSquareDbConnection.closeSession();
    }
  }
}
