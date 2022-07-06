package com.tcbs.automation.coco.mongodb.model;

import com.tcbs.automation.constants.coco.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity("traderListingItem")
public class TraderListingItem {
  private ICopyInfo iCopyInfo;
  private InvestmentInfo investmentInfo;
  private AccountInfo accountInfo;
  private RelationInfo relationInfo;
  private SummaryInfo summaryInfo;
  private NextRank nextRank;
  private RankingInfo rankingInfo;
  private FeeConfigurationInfo feeConfigurationInfo;
  private Date createdDate;
  private Date updatedDate;
  private String id;
  private String tcbsID;
  private Long traderID;
  private Float ratingScore;

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof TraderListingItem)) {
      return false;
    }

    TraderListingItem a = (TraderListingItem) o;

    return Objects.equals(a.iCopyInfo, this.iCopyInfo) &&
      Objects.equals(a.investmentInfo, this.investmentInfo) &&
      Objects.equals(a.accountInfo, this.accountInfo) &&
      Objects.equals(a.relationInfo, this.relationInfo) &&
      Objects.equals(a.nextRank, this.nextRank) &&
      Objects.equals(a.tcbsID, this.tcbsID);
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ICopyInfo {
    private Integer editorChoice;
    @Builder.Default
    private Long noCopier = 0L;          // -> db social-invest // ACCOUNT_RELATION
    @Builder.Default
    private Long noFollower = 0L;          // -> db social-invest // ACCOUNT_RELATION
    @Builder.Default
    private Long viewIn30Days = 0L;
    private Long changeInMonth;     // -> db social-invest // ACCOUNT_RELATION
    private Double totalAUM;        // -> db social-invest // AccountAssetManager.getTotalAssetsValue
    private Double copierGrowth;
    private Double equity;

//    @Override
//    public boolean equals(Object o) {
//      if (o == this) {
//        return true;
//      }
//
//      if (!(o instanceof ICopyInfo)) {
//        return false;
//      }
//
//      ICopyInfo a = (ICopyInfo) o;
//
//      return Objects.equals(a.editorChoice, this.editorChoice) &&
//        Objects.equals(a.noCopier, this.noCopier) &&
//        Objects.equals(a.noFollower, this.noFollower) &&
//        Objects.equals(a.viewIn30Days, this.viewIn30Days) &&
//        Objects.equals(a.changeInMonth, this.changeInMonth) &&
//        Objects.equals(a.totalAUM, this.totalAUM);
//    }
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class InvestmentInfo { // perf-ngin-api
    private Double pnlLast30Days;
    private Double pnl12Month;
    private Double riskInMonth;
    private Long maxNoTicker;
    private Double maxDrawdown;
    private Date firstStockOrderDate;
    private Long seniority;
    private Long transactions;
    private Long avgHoldingPeriod;
    private Double profitableTrade;
    private Constants.NumeratorRange avgNumerator30Day;

//    @Override
//    public boolean equals(Object o) {
//      if (o == this) {
//        return true;
//      }
//
//      if (!(o instanceof InvestmentInfo)) {
//        return false;
//      }
//
//      InvestmentInfo a = (InvestmentInfo) o;
//
//      return Objects.equals(a.pnlLast30Days, this.pnlLast30Days) &&
//        Objects.equals(a.pnl12Month, this.pnl12Month) &&
//        Objects.equals(a.riskInMonth, this.riskInMonth);
//    }
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class AccountInfo { // -> profile
    private String tcbsID;
    private Long traderID;
    private String accountNo;
    private String fullname;
    private String avatar;
    private String custodyID;
    private Constants.TraderRankCode rank;
    private Constants.TraderStatus status;
    private Date birthday;
    private String ageGroup;
    private Boolean blacklist;

//    @Override
//    public boolean equals(Object o) {
//      if (o == this) {
//        return true;
//      }
//
//      if (!(o instanceof AccountInfo)) {
//        return false;
//      }
//
//      AccountInfo a = (AccountInfo) o;
//
//      return Objects.equals(a.tcbsID, this.tcbsID) &&
//        Objects.equals(a.traderID, this.traderID) &&
//        Objects.equals(a.accountNo, this.accountNo) &&
//        Objects.equals(a.fullname, this.fullname) &&
//        Objects.equals(a.avatar, this.avatar) &&
//        Objects.equals(a.rank, this.rank);
//    }
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class NextRank {
    private Constants.TraderRankCode rank;
    private Double equityAmount;
    private Double aumAmount;

    @Override
    public boolean equals(Object o) {
      if (o == this) {
        return true;
      }

      if (!(o instanceof NextRank)) {
        return false;
      }

      NextRank a = (NextRank) o;

      return Objects.equals(a.rank, this.rank) &&
        Objects.equals(a.equityAmount, this.equityAmount) &&
        Objects.equals(a.aumAmount, this.aumAmount);
    }
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class RelationInfo {
    private Long copyID;
    private boolean isFollowing; // -> db social-invest // ACCOUNT_RELATION
    private boolean isCopying;   // -> db social-invest // ACCOUNT_RELATION
    private Double pnlAllTime;   // -> pnl cua thang copier dang xem trader, perf-ngin-api
    private Constants.CopierStatus copyStatus;
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class SummaryInfo {
    private Date reportDate;
    private Double avgAUM;
    private Double avgCopierTime;
    private Double avgPnl;
    private Double aumChange30Days;
    private Double successCopiedOrderRate;
    private Double matchedPriceDelta;
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class RankingInfo {
    @Builder.Default
    private Long generalRankScore = 0L;
    @Builder.Default
    private Long returnRankScore = 0L;
    @Builder.Default
    private Long riskRankScore = 0L;
    @Builder.Default
    private Long impactRankScore = 0L;
    private Double dailyReturn;
    private Double maxDD;
    private Double peak;
    private ReturnInfo returnInfo;
    private RiskInfo riskInfo;
    private ImpactInfo impactInfo;
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ReturnInfo {
    @Builder.Default
    private Double pnlLast30DaysPercent = 0D;
    @Builder.Default
    private Double pnl6MonthPercent = 0D;
    @Builder.Default
    private Double pnl12MonthPercent = 0D;
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ImpactInfo {
    @Builder.Default
    private Double noFollowerPercent = 0D;
    @Builder.Default
    private Double noCopierPercent = 0D;
    @Builder.Default
    private Double viewIn30DaysPercent = 0D;
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class RiskInfo {
    @Builder.Default
    private Double riskInMonthPercent = 0D;
    @Builder.Default
    private Double noSectorPercent = 0D;
    @Builder.Default
    private Double maxDDPercent = 0D;
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class FeeConfigurationInfo {
    FeeConfigurationItem currentConf;
    FeeConfigurationItem nextConf;
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class FeeConfigurationItem {
    private Constants.FeeConfigType copyFeeType;
    private Double managementRate;
    private Double committedProfit;
    private Double performanceRate;
    private Date appliedDate;
  }
}