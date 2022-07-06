package com.tcbs.automation.cas;

import lombok.*;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

import static com.tcbs.automation.cas.CAS.casConnection;

@Entity
@Table(name = "R3RD_SYNC_TCB")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class R3RdSyncTcb {
  private static Logger logger = LoggerFactory.getLogger(R3RdSyncTcb.class);

  @Id
  @Column(name = "ID")
  private BigDecimal id;

  @Column(name = "BRANCH_CODE")
  private String branchCode;

  @Column(name = "ROLE")
  private String role;

  @Column(name = "SALE_CODE")
  private String saleCode;

  @Column(name = "FULL_NAME")
  private String fullName;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "TCBS_ID")
  private String tcbsId;

  @Column(name = "USERNAME")
  private String username;

  @Column(name = "STATUS")
  private BigDecimal status;

  public static List<R3RdSyncTcb> getListRmRboActive() {
    CAS.casConnection.getSession().clear();
    Query<R3RdSyncTcb> query = CAS.casConnection.getSession().createQuery(
      "from R3RdSyncTcb a where a.role in ('RM', 'RBO') and a.status = 1", R3RdSyncTcb.class);
    return query.getResultList();
  }

  public static void deleteByTcbsId(String tcbsId) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    Query query = casConnection.getSession().createQuery(
      "Delete R3RdSyncTcb where tcbsId =:tcbsId");
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  public void insert() {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    Query query = casConnection.getSession().createNativeQuery(
      "INSERT INTO R3RD_SYNC_TCB " +
        "(ID, BRANCH_CODE, \"ROLE\", SALE_CODE, FULL_NAME, EMAIL, TCBS_ID, USERNAME, CREATED_DATE, STATUS) " +
        "VALUES(R3RD_SYNC_TCB_SEQ.nextval, ?1, ?2, ?3, ?4, ?5, ?6, ?7, SYSDATE, ?8)");
    query.setParameter(1, branchCode);
    query.setParameter(2, role);
    query.setParameter(3, saleCode);
    query.setParameter(4, fullName);
    query.setParameter(5, email);
    query.setParameter(6, tcbsId);
    query.setParameter(7, username);
    query.setParameter(8, status);
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }
}
