package com.tcbs.automation.coco.socialinvest;

import com.tcbs.automation.coco.CocoConnBridge;
import com.tcbs.automation.constants.coco.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.query.Query;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "TRADER_ACCOUNT")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TraderAccountEntity {
  private static final String TCBS_ID = "tcbsid";
  private static final String STATUS = "status";
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;
  @Column(name = "TCBSID")
  private String tcbsid;
  @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinColumn(name = "ACCOUNT_ID", nullable = true)
  private AccountEntity account;
  @Column(name = "STRATEGY")
  private String strategy;
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "RANK_CODE", nullable = true)
  private RankEntity rank;
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS")
  private Constants.TraderStatus status;
  @Enumerated(EnumType.STRING)
  @Column(name = "QUALIFY")
  private Constants.TraderQualify qualify;
  @CreationTimestamp
  @Column(name = "CREATED_TIME")
  private Date createdTime;
  @UpdateTimestamp
  @Column(name = "UPDATED_TIME")
  private Date updatedTime;
  @Column(name = "LAST_STOPPED_TIME")
  private Date lastStoppedTime;
  @Column(name = "SENT_MSG")
  private String sentMsg;
  @Column(name = "SHOULD_OPEN_TRADE")
  private boolean shouldOpenTrader;

  public static List<TraderAccountEntity> findTradersByIds(Long[] ids) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();
    Query<TraderAccountEntity> query = session.createQuery(
      "from TraderAccountEntity t where t.id in :ids"
    );
    query.setParameterList("ids", ids);
    List<TraderAccountEntity> res = query.getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();
    return res;
  }

  public static List<TraderAccountEntity> findActiveTraderByIdOrTcbsId(Long id, String tcbsId) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();
    Query<TraderAccountEntity> query = session.createQuery(
      "from TraderAccountEntity t where t.status = :status and ( t.id = :id or t.tcbsid = :tcbsid )"
    );
    query.setParameter(STATUS, Constants.TraderStatus.ACTIVE);
    query.setParameter("id", id);
    query.setParameter(TCBS_ID, tcbsId);
    List<TraderAccountEntity> res = query.getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();
    return res;
  }

  public static TraderAccountEntity findTraderByTcbsId(String tcbsId) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();
    Query<TraderAccountEntity> query = session.createQuery(
      "from TraderAccountEntity t " +
        "where t.tcbsid = :tcbsid "
    );
    query.setParameter(TCBS_ID, tcbsId);
    TraderAccountEntity res = query.getSingleResult();
    CocoConnBridge.socialInvestConnection.closeSession();
    return res;
  }

  public static boolean isTrader(String custodyOrTcbsId) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();
    Query<TraderAccountEntity> query = session.createQuery(
      "from TraderAccountEntity t where t.status = :status and (t.tcbsid = :custodyOrTcbsId or t.account.custodyId = :custodyOrTcbsId)"
    );
    query.setParameter("custodyOrTcbsId", custodyOrTcbsId);
    query.setParameter(STATUS, Constants.TraderStatus.ACTIVE);
    List<TraderAccountEntity> res = query.getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();
    return !CollectionUtils.isEmpty(res);
  }

  public static List<TraderAccountEntity> findNotStoppedTrader(String tcbsId) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();
    Query<TraderAccountEntity> query = session.createQuery(
      "from TraderAccountEntity t where t.status not in :statusList and t.tcbsid = :tcbsid"
    );
    query.setParameterList("statusList", Arrays.asList(Constants.TraderStatus.STOPPED, Constants.TraderStatus.PENDING_STOP));
    query.setParameter(TCBS_ID, tcbsId);
    List<TraderAccountEntity> res = query.getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();
    return res;
  }

  public static Trader toTraderModel(TraderAccountEntity t) {
    if (t == null) {
      return null;
    }
    AccountEntity acc = t.getAccount();
    RankEntity rankEntity = t.getRank();
    return Trader.builder()
      .traderId(t.getId())
      .custodyId(acc.getCustodyId())
      .accountId(acc.getId())
      .accountNo(acc.getAccountNo())
      .regularAccountNo(acc.getRegularAccountNo())
      .tcbsId(t.getTcbsid())
      .strategy(t.getStrategy())
      .rank(RankEntity.toRankModel(rankEntity))
      .status(t.getStatus())
      .qualify(t.getQualify())
      .lastStoppedTime(t.getLastStoppedTime())
      .sentMsg(t.getSentMsg())
      .createdDate(t.getCreatedTime())
      .updatedDate(t.getUpdatedTime())
      .stoppedDate(t.getLastStoppedTime())
      .build();
  }

  public static List<TraderAccountEntity> getTraderByTcbsIdAndStatusIn(String tcbsId, List<Constants.TraderStatus> status) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();
    Query<TraderAccountEntity> query = session.createQuery(
      "from TraderAccountEntity t where t.status in :statusList and t.tcbsid = :tcbsid"
    );
    query.setParameterList("statusList", status);
    query.setParameter(TCBS_ID, tcbsId);
    List<TraderAccountEntity> res = query.getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();
    return res;
  }

  public static TraderAccountEntity getOneTraderWithStatus(Constants.TraderStatus status) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();
    Query<TraderAccountEntity> query = session.createQuery(
      "from TraderAccountEntity t where t.status = :status order by t.createdTime asc"
    );
    query.setParameter(STATUS, status);
    query.setMaxResults(1);
    List<TraderAccountEntity> res = query.getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();
    if (CollectionUtils.isEmpty(res)) {
      throw new NoResultException("not found any " + status + " trader");
    }
    return res.get(0);
  }

  public static Trader save(TraderAccountEntity traderAccount) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();
    session.beginTransaction();
    session.save(traderAccount);
    session.getTransaction().commit();
    Trader trader = toTraderModel(traderAccount);
    CocoConnBridge.socialInvestConnection.closeSession();
    return trader;
  }

  public static void delete(String tcbsId) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();
    Query<TraderAccountEntity> query = session.createQuery(
      "from TraderAccountEntity t where t.tcbsid = :tcbsid"
    );
    query.setParameter(TCBS_ID, tcbsId);
    List<TraderAccountEntity> res = query.getResultList();
    List<Long> traderIds = new ArrayList<>();
    List<Long> accIds = new ArrayList<>();
    for(TraderAccountEntity e: res) {
      traderIds.add(e.getId());
      accIds.add(e.getAccount().getId());
    }
    session.beginTransaction();
    Query delTraderQuery = session.createQuery("delete from TraderAccountEntity t where t.id in :ids");
    delTraderQuery.setParameterList("ids", traderIds);
    delTraderQuery.executeUpdate();

    Query delAccQuery = session.createQuery("delete from AccountEntity a where a.id in :ids");
    delAccQuery.setParameterList("ids", accIds);
    delAccQuery.executeUpdate();
    session.getTransaction().commit();
    CocoConnBridge.socialInvestConnection.closeSession();
  }
}
