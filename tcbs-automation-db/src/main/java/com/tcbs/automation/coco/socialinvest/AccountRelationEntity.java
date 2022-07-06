package com.tcbs.automation.coco.socialinvest;

import com.tcbs.automation.coco.CocoConnBridge;
import com.tcbs.automation.constants.coco.Constants;
import lombok.Data;
import org.hibernate.Session;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.query.Query;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "ACCOUNT_RELATION")
public class AccountRelationEntity {

  private static String targetTcbsidString = "targetTcbsid";
  private static String typeString = "type";
  private static String sourceTcbsIdString = "sourceTcbsId";
  private static String statusString = "status";
  private static String copyIdString = "copyId";
  private static String statusListString = "statusList";
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;
  @Column(name = "SOURCE_TCBSID")
  private String sourceTcbsId;
  @Column(name = "TARGET_TCBSID")
  private String targetTcbsId;
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "SOURCE_ACCOUNT_ID", insertable = false, updatable = false)
  private AccountEntity sourceAccount;
  @Column(name = "SOURCE_ACCOUNT_ID")
  private Long sourceAccountId;
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "TARGET_ACCOUNT_ID", insertable = false, updatable = false)
  private AccountEntity targetAccount;
  @Column(name = "TARGET_ACCOUNT_ID")
  private Long targetAccountId;
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "SOURCE_ID", insertable = false, updatable = false)
  private TraderAccountEntity trader;
  @Column(name = "SOURCE_ID")
  private Long sourceId;
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "TARGET_ID", insertable = false, updatable = false)
  private CopierAccountEntity copier;
  @Column(name = "TARGET_ID")
  private Long targetId;
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE")
  private Constants.AccountRelationType type;
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS")
  private Constants.AccountRelationStatus status;
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CREATED_TIME", updatable = false, insertable = false)
  private java.util.Date createdTime;
  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "UPDATED_TIME", updatable = true, insertable = false)
  private java.util.Date updatedTime;

  public static List<AccountRelationEntity> getAccountRelationByTargetTcbsIDAndTypeAndStatus(String targetTcbsid, Constants.AccountRelationType type, Constants.AccountRelationStatus status) {
    return getAccountRelationByTargetTcbsIDAndTypeAndStatus(targetTcbsid, type, status, null);
  }

  public static List<AccountRelationEntity> getAccountRelationByTargetTcbsIDAndTypeAndStatus(String targetTcbsid, Constants.AccountRelationType type, Constants.AccountRelationStatus status, PagingParam pagingParam) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();

    Query<AccountRelationEntity> query = session.createQuery(
      "from AccountRelationEntity r where r.type=:type and r.status=:status and r.targetTcbsId =:targetTcbsid ");
    query.setParameter(typeString, type);
    query.setParameter(statusString, status);
    query.setParameter(targetTcbsidString, targetTcbsid);
    if (pagingParam != null) {
      query.setFirstResult(pagingParam.getPage() * pagingParam.getSize());
      query.setMaxResults(pagingParam.getSize());
    }
    List<AccountRelationEntity> res = query.getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();
    return res;
  }

  public static List<AccountRelationEntity> getAccountRelationByTargetTcbsIDAndTypeAndCopierStatusIn(String targetTcbsid, Constants.AccountRelationType type, Constants.CopierStatus[] statusList) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();

    Query<AccountRelationEntity> query = session.createQuery(
      "from AccountRelationEntity r where r.type=:type and r.targetTcbsId =:targetTcbsid and r.copier.status in :statusList");
    query.setParameter(typeString, type);
    query.setParameter(targetTcbsidString, targetTcbsid);
    query.setParameterList(statusListString, statusList);
    List<AccountRelationEntity> res = query.getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();
    return res;
  }

  public static List<AccountRelationEntity> getRelationInfoByTargetTcbsIdAndTypeAndStatus(String targetTcbsid,
                                                                                          Constants.AccountRelationType type,
                                                                                          Constants.AccountRelationStatus status,
                                                                                          PagingParam pagingParam) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();

    Query<AccountRelationEntity> query = session.createQuery(
      "from AccountRelationEntity r where r.status = :status and r.type = :type and r.targetTcbsId = :targetTcbsid ");
    query.setParameter(statusString, status);
    query.setParameter(typeString, type);
    query.setParameter(targetTcbsidString, targetTcbsid);
    if (pagingParam != null) {
      query.setFirstResult(pagingParam.getPage() * pagingParam.getSize());
      query.setMaxResults(pagingParam.getSize());
    }
    List<AccountRelationEntity> res = query.getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();
    return res;
  }

  public static AccountRelationEntity getAccountRelationByCopyId(Long copyId) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();

    Query<AccountRelationEntity> query = session.createQuery(
      "from AccountRelationEntity r where r.targetId = :copyId ");
    query.setParameter(copyIdString, copyId);
    List<AccountRelationEntity> res = query.getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();
    if (!CollectionUtils.isEmpty(res)) {
      return res.get(0);
    }
    return null;
  }

  public static AccountRelationEntity getAccountRelationBySourceTcbsIdAndTargetTcbsIdAndCopyStatusIn(String sourceTcbsId, String targetTcbsId, List<Constants.CopierStatus> statusList) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();

    Query<AccountRelationEntity> query = session.createQuery(
      "from AccountRelationEntity r where r.sourceTcbsId = :sourceTcbsId and r.targetTcbsId = :targetTcbsid and r.type = :type and r.copier.status in :statusList");
    query.setParameter(sourceTcbsIdString, sourceTcbsId);
    query.setParameter(targetTcbsidString, targetTcbsId);
    query.setParameter(typeString, Constants.AccountRelationType.COPY);
    query.setParameterList(statusListString, statusList);
    List<AccountRelationEntity> res = query.getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();
    if (!CollectionUtils.isEmpty(res)) {
      return res.get(0);
    }
    return null;
  }

  public static AccountRelationEntity getFollowingAccountRelationBySourceTcbsIdAndTargetTcbsId(String sourceTcbsId, String targetTcbsId) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();

    Query<AccountRelationEntity> query = session.createQuery(
      "from AccountRelationEntity r where r.sourceTcbsId = :sourceTcbsId and r.targetTcbsId = :targetTcbsid and r.type = :type and r.status = :status order by r.createdTime");
    query.setParameter(sourceTcbsIdString, sourceTcbsId);
    query.setParameter(targetTcbsidString, targetTcbsId);
    query.setParameter(typeString, Constants.AccountRelationType.FOLLOW);
    query.setParameter(statusString, Constants.AccountRelationStatus.FOLLOWING);
    query.setMaxResults(1);
    List<AccountRelationEntity> res = query.getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();
    if (!CollectionUtils.isEmpty(res)) {
      return res.get(0);
    }
    return null;
  }

  public static List<Long> getCopiersOfTrader(Long traderId) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();

    Query<Long> query = session.createQuery(
      "select r.targetId from AccountRelationEntity r where r.sourceId = :sourceId and r.type = :type and r.status = :status");
    query.setParameter("sourceId", traderId);
    query.setParameter(typeString, Constants.AccountRelationType.COPY);
    query.setParameter(statusString, Constants.AccountRelationStatus.COPYING);
    List<Long> res = query.getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();
    return res;
  }
}