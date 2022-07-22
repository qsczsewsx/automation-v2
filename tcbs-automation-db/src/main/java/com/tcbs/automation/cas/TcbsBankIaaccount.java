package com.tcbs.automation.cas;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.cas.CAS.casConnection;

@Entity
@Table(name = "TCBS_BANK_IAACCOUNT")
@Getter
@Setter
public class TcbsBankIaaccount {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @NotNull
  @Column(name = "ACCOUNT_NO")
  private String accountNo;
  @Column(name = "BANK_CODE")
  private String bankCode;
  @Column(name = "ACCOUNT_NAME")
  private String accountName;
  @NotNull
  @Column(name = "USER_ID")
  private BigDecimal userId;
  @Column(name = "BANK_NAME")
  private String bankName;
  @Column(name = "BANK_BRANCH")
  private String bankBranch;
  @Column(name = "BANKPROVINCE")
  private String bankprovince;
  @Column(name = "STATUS")
  private BigDecimal status;
  @Column(name = "AUTO_TRANSFER")
  private String autoTransfer;
  @Column(name = "IS_IA_PAID")
  private String isIaPaid;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "UPDATED_DATE")
  private Timestamp updatedDate;
  @Column(name = "BANK_SOURCE")
  private String bankSource;
  @Column(name = "ERR_CODE")
  private String errCode;
  @Column(name = "ERR_MSG")
  private String errMsg;
  @Column(name = "SYNC_JOB")
  private String syncJob;
  @Column(name = "REQUEST_ID")
  private String requestId;
  @Column(name = "PARTNER_REGISTER_ID")
  private String partnerRegisterId;

  private static final String PPUSERID = "userId";
  private static final String PPSTATUS = "status";

  @Step
  public static List<TcbsBankIaaccount> getListBanks(String userId) {
    CAS.casConnection.getSession().clear();
    Query<TcbsBankIaaccount> query = CAS.casConnection.getSession().createQuery(
      "from TcbsBankIaaccount a where a.userId=:userId order by id desc", TcbsBankIaaccount.class);
    query.setParameter(PPUSERID, new BigDecimal(userId));
    return query.getResultList();
  }

  public static TcbsBankIaaccount getBank(String userId) {
    CAS.casConnection.getSession().clear();
    Query<TcbsBankIaaccount> query = CAS.casConnection.getSession().createQuery(
      "from TcbsBankIaaccount a where a.userId=:userId", TcbsBankIaaccount.class);
    query.setParameter(PPUSERID, new BigDecimal(userId));
    return query.getSingleResult();
  }

  public static void deleteByUserId(String userId) {
    Session session = CAS.casConnection.getSession();
    session.beginTransaction();
    session.createQuery("delete from TcbsBankIaaccount where userId =:userId")
      .setParameter(PPUSERID, new BigDecimal(userId))
      .executeUpdate();
    session.getTransaction().commit();
  }

  public static void deleteFromUserId(List<BigDecimal> userIds) {
    Session session = CAS.casConnection.getSession();
    session.beginTransaction();
    session.createQuery("delete from TcbsBankIaaccount where userId in (:userId)")
      .setParameter(PPUSERID, userIds)
      .executeUpdate();
    session.getTransaction().commit();
  }

  public static void updateStatusByUserId(String userId, String status) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    Query query = casConnection.getSession().createQuery(
      "Update TcbsBankIaaccount a set a.status =:status where a.userId=:userId");
    query.setParameter(PPUSERID, new BigDecimal(userId));
    query.setParameter(PPSTATUS, new BigDecimal(status));
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  public static void updateStatusById(BigDecimal id, String status) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    Query query = casConnection.getSession().createQuery(
      "Update TcbsBankIaaccount a set a.status =:status where a.id=:id");
    query.setParameter("id", id);
    query.setParameter(PPSTATUS, new BigDecimal(status));
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  public static void updateStatusOfLastRecord(String userId, String status, String bankSource) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    Query query = casConnection.getSession().createNativeQuery(
      "Update TCBS_BANK_IAACCOUNT set status =:status, bank_Source=:bankSource where " +
        "id=(select id from (select id from TCBS_BANK_IAACCOUNT where user_Id=:userId order by id desc) where rownum = 1)");
    query.setParameter(PPUSERID, new BigDecimal(userId));
    query.setParameter(PPSTATUS, new BigDecimal(status));
    query.setParameter("bankSource", bankSource);
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  public static BigDecimal getIaiSaveBank(String userId, String bankSource) {
    CAS.casConnection.getSession().clear();
    Query<TcbsBankIaaccount> query = CAS.casConnection.getSession().createQuery(
      "from TcbsBankIaaccount a where a.userId=:userId and a.bankSource=:bankSource", TcbsBankIaaccount.class);
    query.setParameter(PPUSERID, new BigDecimal(userId));
    query.setParameter("bankSource", bankSource);
    return query.getSingleResult().getStatus();
  }
}
