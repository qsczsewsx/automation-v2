package com.tcbs.automation.cas;

import lombok.*;
import net.thucydides.core.annotations.Step;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.cas.CAS.casConnection;

@Entity
@Table(name = "R3RD_USER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class R3RdUser {
  private static Logger logger = LoggerFactory.getLogger(R3RdUser.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "USERNAME")
  private String username;
  @Column(name = "FULL_NAME")
  private String fullName;
  @Column(name = "EMAIL")
  private String email;
  @Column(name = "BANK_CODE")
  private String bankCode;
  @NotNull
  @Column(name = "BRANCH_CODE")
  private String branchCode;
  @Column(name = "PASSWORD")
  private String password;
  @Column(name = "ADDRESS")
  private String address;
  @Column(name = "PHONE")
  private String phone;
  @Column(name = "ACTIVE")
  private BigDecimal active;
  @Column(name = "CREATEDDATE")
  private Timestamp createddate;
  @Column(name = "AGENCY")
  private String agency;
  @Column(name = "CUSTODY_CODE")
  private String custodyCode;
  @Column(name = "TYPE")
  private BigDecimal type;
  @Column(name = "ROLE_ID")
  private BigDecimal roleId;
  @Column(name = "STATUS")
  private BigDecimal status;
  @Column(name = "MASTER_ROLE")
  private String masterRole;
  @Column(name = "NOTE")
  private String note;

  private static final String DT_CUSTODY_CODE = "custodyCode";

  public void insert() {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    Query query = casConnection.getSession().createNativeQuery(
      "INSERT INTO R3RD_USER " +
        "(ID, TCBS_ID, USERNAME, FULL_NAME, EMAIL, BANK_CODE, BRANCH_CODE, CREATED_DATE, UPDATED_DATE, ROLE_ID, STATUS, MASTER_ROLE, NOTE, CUSTODY_CODE)" +
        "VALUES(R3RD_USER_SEQ.nextval, ?1, ?2, ?3, ?4, ?5, ?6, SYSDATE, SYSDATE, ?7, ?8, ?9, ?10, ?11)"
    );
    query.setParameter(1, tcbsId);
    query.setParameter(2, username);
    query.setParameter(3, fullName);
    query.setParameter(4, email);
    query.setParameter(5, bankCode);
    query.setParameter(6, branchCode);
    query.setParameter(7, roleId);
    query.setParameter(8, status);
    query.setParameter(9, masterRole);
    query.setParameter(10, note);
    query.setParameter(11, custodyCode);
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  @Step

  public static R3RdUser getByCustodyCode(String custodyCode) {
    CAS.casConnection.getSession().clear();
    Query<R3RdUser> query = CAS.casConnection.getSession().createQuery(
      "from R3RdUser a WHERE a.custodyCode=:custodyCode", R3RdUser.class);
    query.setParameter(DT_CUSTODY_CODE, custodyCode);
    return query.getSingleResult();
  }

  public static R3RdUser getById(String id) {
    CAS.casConnection.getSession().clear();
    Query<R3RdUser> query = CAS.casConnection.getSession().createQuery(
      "from R3RdUser a WHERE a.id=:id", R3RdUser.class);
    query.setParameter("id", new BigDecimal(id));
    return query.getSingleResult();
  }

  public static void updateRoleIdByCustodyCode(String custodyCode, String roleId) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    casConnection.getSession().createQuery(
      "Update R3RdUser a set a.roleId =:roleId where a.custodyCode=:custodyCode", R3RdUser.class).setParameter(DT_CUSTODY_CODE, custodyCode).setParameter("roleId",
      new BigDecimal(roleId)).executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  public static List<R3RdUser> searchByCondition(String custodyCode, String bankCode, String branchCode, String role, String status) {
    CAS.casConnection.getSession().clear();
    HashMap<String, Object> params = new HashMap<>();
    String queryStr = "from R3RdUser a WHERE 1 = 1 ";
    if (StringUtils.isNotEmpty(role)) {
      try {
        List<R3RdRole> r3RdRoles = R3RdRole.getRoleByName(role);
        if (r3RdRoles.isEmpty()) {
          return new ArrayList<>();
        }
        List<BigDecimal> roleIds = new ArrayList<>();
        for (R3RdRole r : r3RdRoles) {
          roleIds.add(r.getId());
        }
        queryStr += " and a.roleId in(:roleIds) ";
        params.put("roleIds", roleIds);
      } catch (NoResultException ex) {
        return new ArrayList<>();
      }
    }
    if (StringUtils.isNotEmpty(custodyCode)) {
      queryStr += " and a.custodyCode=:custodyCode ";
      params.put(DT_CUSTODY_CODE, custodyCode);
    }
    if (StringUtils.isNotEmpty(bankCode)) {
      queryStr += " and a.bankCode=:bankCode ";
      params.put("bankCode", bankCode);
    }
    if (StringUtils.isNotEmpty(branchCode)) {
      queryStr += " and a.branchCode=:branchCode ";
      params.put("branchCode", branchCode);
    }
    if (StringUtils.isNotEmpty(status)) {
      queryStr += " and a.status=:status ";
      params.put("status", new BigDecimal(status));
    }

    Query<R3RdUser> query = CAS.casConnection.getSession().createQuery(queryStr, R3RdUser.class);
    for (String key : params.keySet()) {
      query.setParameter(key, params.get(key));
    }
    return query.getResultList();
  }

  public static void deleteByTcbsId(String tcbsId) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    casConnection.getSession().createQuery(
      "Delete from R3RdUser where tcbsId =:tcbsId", R3RdUser.class).setParameter("tcbsId", tcbsId).executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  public static R3RdUser getByTcbsId(String tcbsId) {
    CAS.casConnection.getSession().clear();
    Query<R3RdUser> query = CAS.casConnection.getSession().createQuery(
      "from R3RdUser a WHERE a.tcbsId=:tcbsId", R3RdUser.class);
    query.setParameter("tcbsId", tcbsId);
    return query.getSingleResult();
  }

  public static List<R3RdUser> getAllList() {
    CAS.casConnection.getSession().clear();
    Query<R3RdUser> query = CAS.casConnection.getSession().createQuery(
      "from R3RdUser a order by id desc", R3RdUser.class);
    return query.getResultList();
  }
}