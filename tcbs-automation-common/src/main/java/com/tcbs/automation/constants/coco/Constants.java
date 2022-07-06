package com.tcbs.automation.constants.coco;

import java.util.*;
import java.util.stream.Collectors;

public class Constants {

  public enum CopierStopCopyAction {
    STOP,
    CANCEL_STOP
  }

  public enum CopierStopCopyType {
    SELL,
    TAKE
  }

  public enum FlexAccType {
    NORMAL("normal"),
    COPYER("copyer"),
    TRADER("trader");

    private String stringValue;

    FlexAccType(String stringValue) {
      this.stringValue = stringValue;
    }

    public String getStringValue() {
      return this.stringValue;
    }

    private static final List<FlexAccType> icopyAccTypes = Arrays.asList(TRADER, COPYER);

    public static boolean isICopyAccount(FlexAccType type) {
      return icopyAccTypes.contains(type);
    }

    public static FlexAccType fromString(String value) {
      for (FlexAccType status : values()) {
        if (status.stringValue.equalsIgnoreCase(value)) {
          return status;
        }
      }
      return null;
    }

  }

  public enum NumeratorRange {
    LT_10M,
    GTE_10M_LT_50M,
    GTE_50M_LT_100M,
    GTE_100M_LT_300M,
    GTE_300M_LT_1B,
    GTE_1B_LT_5B,
    GTE_5B_LT_10B,
    GTE_10B
  }

  public enum TraderChangeType {
    CREATE,
    DELETE
  }

  public enum RatingRole {
    SUBSCRIBER,
    COPIER,
  }

  public enum CopierCompleteActionType {
    STOP_COPY,
    TAKE_PROFIT,
    STOP_LOSS,
    TRANSFER_CASH, // stop-copy
    TRANSFER_STOCK, // stop-copy
    FORCE_SELL, // stop-copy
  }

  public enum CopierCompleteActionStatus {
    COMPLETED,
    FAILED,
    CANCELLED,
  }

  public enum TraderStatus {
    PROCESS_TRANS("PROCESS_TRANS"),
    ACTIVE("ACTIVE"),
    TRANS_ERROR("TRANS_ERROR"),
    STOPPED("STOPPED"),
    INACTIVE("INACTIVE"),
    INACTIVE_TO_STOP("INACTIVE_TO_STOP"),
    PENDING_STOP("PENDING_STOP");

    private final String stringValue;

    TraderStatus(String stringValue) {
      this.stringValue = stringValue;
    }

    public static TraderStatus fromString(String value) {
      for (TraderStatus status : values()) {
        if (status.stringValue.equalsIgnoreCase(value)) {
          return status;
        }
      }
      return null;
    }

    public String getStringValue() {
      return this.stringValue;
    }
  }

  public enum TraderQualify {
    QUALIFY,
    DISQUALIFY
  }

  public enum CopierStatus {
    STARTING,
    PROCESS_ERROR,
    ACTIVE,
    PENDING_STOP,
    STOPPED,
    PENDING_CANCEL_STOP,
    PENDING_TAKE_PROFIT,
    PENDING_CUT_LOSS;
  }

  public enum AccountRelationType {
    FOLLOW,
    COPY;
  }

  public enum AccountRelationStatus {
    STARTING, // on creating
    FOLLOWING,
    COPYING, // on copying
    STOPPED, // stopped
  }

  public enum CopierAction {
    INITIALIZE,
    OPEN_TRADE,
    TOPUP,
    WITHDRAW,
    STOP_COPY,
    BUY,
    SELL,
    CANCEL_STOP,
    TRANSFER,
    M_SELL,
  }

  public enum TraderRankCode {
    NEWBIE,
    ROOKIE,
    MASTER,
    GURU;
  }

  public enum RatingCode {
    VERY_GOOD("VERY_GOOD"),
    GOOD("GOOD"),
    NORMAL("NORMAL"),
    WEAK("WEAK"),
    NONE("NONE");

    private final String stringValue;

    RatingCode(String stringValue) {
      this.stringValue = stringValue;
    }

    public static RatingCode fromString(String value) {
      for (RatingCode code : RatingCode.values()) {
        if (code.stringValue.equalsIgnoreCase(value)) {
          return code;
        }
      }
      return null;
    }

    public String getStringValue() {
      return this.stringValue;
    }
  }

  public enum OrderSide {
    B("B"),
    S("S"),
    O("O");

    public final String strValue;

    private OrderSide(String strValue) {
      this.strValue = strValue;
    }

    public static String stringValue(OrderSide os) {
      return os == null ? null : os.strValue;
    }
  }

  public enum CriteriaEnum {
    PNLLAST30DAYS,
    PNL6MONTH,
    PNL12MONTH,
    RISKINMONTH,
    SECTORNUMBER,
    MAXDRAWDOWN,
    NOFOLLOWER,
    VIEWIN30DAYS,
    NOCOPIER,
    AVGTOTALAUM,
    AVGEQUITY,
    ;

  }

  public enum WhitelistAction {
    NONE,
    ADD,
    REMOVE
  }

  public enum WhitelistStatus {
    ACTIVE("ACTIVE"), INACTIVE("INACTIVE");

    private String stringValue;

    WhitelistStatus(String stringValue) {
      this.stringValue = stringValue;
    }

    public String getStringValue() {
      return this.stringValue;
    }

    public static WhitelistStatus fromString(String value) {
      for (WhitelistStatus status : values()) {
        if (status.stringValue.equalsIgnoreCase(value)) {
          return status;
        }
      }
      return null;
    }
  }

  public enum FeeConfigType {
    AUM("ICOPY_MGMT_AUM_FEE"),
    PNL("ICOPY_MGMT_PNL_FEE"),
    AUM_PNL("ICOPY_MGMT_AUM_PNL_FEE"),
    ;
    private final String templateName;

    FeeConfigType(String templateName) {
      this.templateName = templateName;
    }

    public String getTemplateName() {
      return templateName;
    }

    public static FeeConfigType fromTemplateName(String templateName) {
      if(templateName == null) return null;
      return Arrays.stream(FeeConfigType.values())
        .filter(t -> t.getTemplateName().equals(templateName))
        .findAny()
        .orElseThrow(() -> new EnumConstantNotPresentException(FeeConfigType.class, templateName));
    }
  }

  public enum TopFilterCriteria {
    EDITOR_CHOICE,
    MOST_COPY,
    MOST_POPULAR,
    MOST_PROFIT,
    MOST_PROSPECTS,
    MOST_COPIER_GROWTH,
    SMALL_BUDGET,
  }

  public static final long MIN_EQUITY = 50000000;
  public static final String X_API_KEY = "x-api-key";
}
