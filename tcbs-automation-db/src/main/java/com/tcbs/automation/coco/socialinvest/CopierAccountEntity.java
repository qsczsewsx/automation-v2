package com.tcbs.automation.coco.socialinvest;

import com.tcbs.automation.coco.CocoConnBridge;
import com.tcbs.automation.constants.coco.Constants;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "COPIER_ACCOUNT")
public class CopierAccountEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @OneToOne(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
  @JoinColumn(name = "ACCOUNT_ID", nullable = true)
  private AccountEntity account;

  @Column(name = "TCBSID")
  private String tcbsId;

  @Column(name = "NET_INVESTED")
  private Double netInvested;

  @Column(name = "CUT_LOST_PCT")
  private Float cutLostPct;

  @Column(name = "TAKE_PROFIT_PCT")
  private Float takeProfitPct;

  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS")
  private Constants.CopierStatus status;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CREATED_TIME", updatable = false, insertable = false)
  private java.util.Date createdTime;

  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "UPDATED_TIME", insertable = false)
  private java.util.Date updatedTime;

  public static List<CopierAccountEntity> getAllCopierAccountStatusIn(String tcbsId, Constants.CopierStatus[] statusList) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();

    Query<CopierAccountEntity> query = session.createQuery(
      "from CopierAccountEntity a " +
        "where a.tcbsId=:tcbsId " +
        "and a.status in :statusList");
    query.setParameter("tcbsId", tcbsId);
    query.setParameterList("statusList", statusList);
    List<CopierAccountEntity> res = query.getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();
    return res;
  }

  public static CopierAccountEntity getCopierAccountById(Long id) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();

    Query<CopierAccountEntity> query = session.createQuery(
      "from CopierAccountEntity a where a.id = :id");
    query.setParameter("id", id);

    try {
      return query.getSingleResult();
    } catch (NoResultException ex) {
      return null;
    } finally {
      CocoConnBridge.socialInvestConnection.closeSession();
    }
  }
}
