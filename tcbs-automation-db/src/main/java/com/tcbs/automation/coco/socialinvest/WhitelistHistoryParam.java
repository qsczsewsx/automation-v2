package com.tcbs.automation.coco.socialinvest;

import com.tcbs.automation.constants.coco.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WhitelistHistoryParam {
  private String ticker;
  private Date approvedFromDate;
  private Date approvedToDate;
  private Constants.WhitelistAction action;
  private String createdUser;
  private String approvedUser;

  private Integer page;
  private Integer size;
}
