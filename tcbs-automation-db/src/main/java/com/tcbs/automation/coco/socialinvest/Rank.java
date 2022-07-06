package com.tcbs.automation.coco.socialinvest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Rank {
  private String rankCode;
  private Double equity;
  private Double aumMin;
  private Double aumMax;
  private Integer level;
  private Date createdTime;
  private Date updatedTime;

  private Double maxCopyFee;
  private Double minCopyFee;
  private Double committedProfit;
  private Double minExcessFee;
  private Double maxExcessFee;

  private Double nextMaxCopyFee;
  private Double nextMinCopyFee;
  private Double nextCommittedProfit;
  private Double nextMinExcessFee;
  private Double nextMaxExcessFee;
  private Date appliedDate;

  private Double shareManagementFee;
  private Double sharePerformanceFee;
}
