package com.tcbs.automation.coco.mongodb.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcbs.automation.constants.coco.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity("copierActionInfo")
public class CopierActionInfo {
  private String id;
  private String tcbsId;
  private Long copierId;
  private TraderListingItem.AccountInfo traderInfo;

  private Date createdDate;
  private Date updatedDate;

  private Constants.CopierAction actionType;
  private Date actionDate;
  private String symbol;
  private Double invested;
  private Double costPrice;
  private Double sellPrice;
  private Double returnValue;
  private Double returnPct;

  private Constants.CopierAction parentActionType; // STOP_COPY
  private String traceId;
  private String accountNo;
  private Boolean completed;
  @JsonIgnore
  private String orderId;
  private Long volume;
  private List<ChildAction> details;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ChildAction {
    private Constants.CopierAction actionType;
    private Date actionDate;
    private String symbol;
    private Double invested;
    private Double costPrice;
    private Double sellPrice;
    private Double returnValue;
    private Double returnPct;
    private Long volume;
    private Boolean completed;
  }
}
