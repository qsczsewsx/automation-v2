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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "UM_ROLE")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class UmRoleEntity implements Serializable {

  public static final String ROLE_SUPER_ADMIN = "SUPER_ADMIN";
  public static final String ROLE_FORMAT = "ISQUARE/IS_C{companyId}_GA{gaId}-{roleName}";
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "roleSequence")
  @SequenceGenerator(name = "roleSequence", sequenceName = "UM_ROLE_SEQUENCE", initialValue = 1, allocationSize = 1)
  @Column(name = "UM_ID")
  private Long id;
  @Column(name = "UM_ROLE_NAME")
  private String roleName;
  @Id
  @Column(name = "UM_TENANT_ID")
  private Long tenantId;
  @Column(name = "UM_SHARED_ROLE")
  private String sharedRole;
  @Column(name = "COMPANY_ID")
  private Long companyId;

  public static UmRoleEntity getRole(String role) {
    try {
      Session session = ISquare.iSquareDbConnection.getSession();
      Query<UmRoleEntity> query = session.createQuery("FROM UmRoleEntity WHERE roleName = :roleValue");
      query.setParameter("roleValue", role);
      List<UmRoleEntity> roleList = query.getResultList();
      if (roleList == null || CollectionUtils.isEmpty(roleList)) {
        return null;
      }
      return roleList.get(0);
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    } finally {
      ISquare.iSquareDbConnection.closeSession();
    }
  }

  public static Long insertRole(Long companyId, Long gaId, String roleName) {
    String role = ROLE_FORMAT.replace("{companyId}", companyId.toString())
      .replace("{gaId}", gaId.toString())
      .replace("{roleName}", roleName);
    UmRoleEntity umRoleEntity = getRole(role);
    if (umRoleEntity != null) {
      return umRoleEntity.getId();
    }
    try {
      Session session = ISquare.iSquareDbConnection.getSession();
      Transaction transaction = session.beginTransaction();
      umRoleEntity = UmRoleEntity.builder()
        .roleName(role)
        .companyId(companyId)
        .sharedRole(null)
        .tenantId(-1234L)
        .build();
      session.save(umRoleEntity);
      transaction.commit();
      umRoleEntity = getRole(role);
      if (umRoleEntity != null) {
        return umRoleEntity.getId();
      }
    } catch (Exception e) {
      log.error(e.getMessage());
      throw e;
    } finally {
      ISquare.iSquareDbConnection.closeSession();
    }
    log.error("Error insert role for company = {}, gaId = {}", companyId, gaId);
    return null;
  }
}
