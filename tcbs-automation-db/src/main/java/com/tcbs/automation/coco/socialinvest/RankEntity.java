package com.tcbs.automation.coco.socialinvest;

import com.tcbs.automation.coco.CocoConnBridge;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "RANK")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class RankEntity {
  @Id
  @NotNull
  @Column(name = "RANK_CODE")
  private String rankCode;

  @Column(name = "EQUITY")
  private Double equity;

  @Column(name = "AUM_MIN")
  private Double aumMin;

  @Column(name = "AUM_MAX")
  private Double aumMax;

  @Column(name = "RANK_LEVEL")
  private Integer level;

  @Column(name = "MAX_COPY_FEE")
  private Double maxCopyFee;

  @Column(name = "MIN_COPY_FEE")
  private Double minCopyFee;

  @Column(name = "COMMITTED_PROFIT")
  private Double committedProfit;

  @Column(name = "MIN_EXCESS_FEE")
  private Double minExcessFee;

  @Column(name = "MAX_EXCESS_FEE")
  private Double maxExcessFee;

  @Column(name = "NEXT_MAX_COPY_FEE")
  private Double nextMaxCopyFee;

  @Column(name = "NEXT_MIN_COPY_FEE")
  private Double nextMinCopyFee;

  @Column(name = "NEXT_COMMITTED_PROFIT")
  private Double nextCommittedProfit;

  @Column(name = "NEXT_MIN_EXCESS_FEE")
  private Double nextMinExcessFee;

  @Column(name = "NEXT_MAX_EXCESS_FEE")
  private Double nextMaxExcessFee;

  @Column(name = "SHARE_MANAGEMENT_FEE")
  private Double shareManagementFee;

  @Column(name = "SHARE_PERFORMANCE_FEE")
  private Double sharePerformanceFee;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "APPLIED_TIME")
  private Date appliedTime;

  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "CREATED_TIME")
  private Date createdTime;

  @UpdateTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "UPDATED_TIME")
  private Date updatedTime;

  public static List<Rank> getRankList() {
    Session session = CocoConnBridge.socialInvestConnection.getSession();
    Query<RankEntity> query = session.createQuery(
      "from RankEntity t"
    );
    List<RankEntity> res = query.getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();
    return res.stream().map(RankEntity::toRankModel).collect(Collectors.toList());
  }

  public static Rank getRank(String rankCode) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();
    Query<RankEntity> query = session.createQuery(
      "from RankEntity t where t.id = :rankCode"
    );
    query.setParameter("rankCode", rankCode);
    try {
      return toRankModel(query.getSingleResult());
    } catch (Exception ex) {
      log.error("error when get rank: ", ex);
      return null;
    } finally {
      CocoConnBridge.socialInvestConnection.closeSession();
    }
  }

  public static Rank toRankModel(RankEntity e) {
    if (e == null) {
      return null;
    }
    return Rank.builder()
      .rankCode(e.getRankCode())
      .equity(e.getEquity())
      .aumMin(e.getAumMin())
      .aumMax(e.getAumMax())
      .level(e.getLevel())
      .createdTime(e.getCreatedTime())
      .updatedTime(e.getUpdatedTime())
      .maxCopyFee(e.getMaxCopyFee())
      .minCopyFee(e.getMinCopyFee())
      .committedProfit(e.getCommittedProfit())
      .minExcessFee(e.getMinExcessFee())
      .maxExcessFee(e.getMaxExcessFee())
      .nextMaxCopyFee(e.getNextMaxCopyFee())
      .nextMinCopyFee(e.getNextMinCopyFee())
      .nextCommittedProfit(e.getNextCommittedProfit())
      .nextMaxExcessFee(e.getNextMaxExcessFee())
      .nextMinExcessFee(e.getNextMinExcessFee())
      .appliedDate(e.getAppliedTime())
      .shareManagementFee(e.getShareManagementFee())
      .sharePerformanceFee(e.getSharePerformanceFee())
      .build();
  }
}
