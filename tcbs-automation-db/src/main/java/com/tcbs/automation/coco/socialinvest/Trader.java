package com.tcbs.automation.coco.socialinvest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tcbs.automation.constants.coco.Constants;
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
public class Trader {
  private Long traderId;
  private Long accountId;
  private String accountNo;
  private String regularAccountNo;
  private String tcbsId;
  private String custodyId;
  private Constants.TraderStatus status;
  private String strategy;
  private Rank rank;
  private Constants.TraderQualify qualify;
  private Date lastStoppedTime;
  private String sentMsg;

  private Date createdDate;
  private Date updatedDate;
  private Date stoppedDate;

  public boolean isActive() {
    return Constants.TraderStatus.ACTIVE == status;
  }
}
