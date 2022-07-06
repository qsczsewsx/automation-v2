package com.tcbs.automation.coco.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterCriteriaItem {
  public static final String CRI_KEY_FULLNAME = "fullname";
  public static final String CRI_KEY_NO_COPIER = "noCopier";
  public static final String CRI_KEY_NO_FOLLOWER = "noFollower";
  public static final String CRI_KEY_POPULAR = "popular";
  public static final String CRI_KEY_EDITOR_CHOICE = "editorChoice";
  public static final String CRI_KEY_RANKING_INFO = "generalRankScore";
  public static final String CRI_KEY_RISK_IN_MONTH = "riskInMonth";
  public static final String CRI_KEY_RANK = "rank";
  public static final String CRI_KEY_STATUS = "status";
  public static final String CRI_KEY_TOTAL_AUM = "totalAUM";
  public static final String CRI_KEY_PNL_12_MONTH = "pnl12Month";
  public static final String CRI_KEY_PNL_30_DAYS = "pnlLast30Days";
  public static final String CRI_KEY_TCBSID = "tcbsID";
  public static final String CRI_KEY_TRADER_ID = "traderID";
  public static final String CRI_KEY_AVATAR = "avatar";
  public static final String CRI_KEY_EQUITY = "equity";
  public static final String CRI_KEY_BLACKLIST = "blacklist";
  public static final String CRI_CNT = ":"; // contain
  public static final String CRI_EQ = "=";
  public static final String CRI_GT = ">";
  public static final String CRI_LT = "<";
  public static final String CRI_GTE = ">=";
  public static final String CRI_LTE = "<=";
  public static final String CRI_IN = "in";
  private String key;
  private String operator;
  private Object value;
}
