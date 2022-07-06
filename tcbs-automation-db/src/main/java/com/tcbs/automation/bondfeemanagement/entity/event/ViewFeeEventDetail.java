package com.tcbs.automation.bondfeemanagement.entity.event;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "VIEW_FEE_EVENT_DETAIL")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViewFeeEventDetail {
  @Id
  @Column(name = "EVENT_ENGINE_INSTANCE_ID", updatable = false, nullable = false)
  private String eventEngineInstanceId;

  @Column(name = "TIMELINE_ENGINE_INSTANCE_ID")
  @JsonIgnore
  private String timelineEngineInstanceId;

  @Column(name = "FEE_ID")
  @JsonIgnore
  private Integer feeId;

  @Column(name = "FEE_TIMELINE_ID")
  @JsonIgnore
  private Integer feeTimelineId;

  @Column(name = "FEE_TIMELINE_TYPE")
  @JsonIgnore
  private String feeTimelineType;

  @Column(name = "CASE_ID")
  private Integer caseId;

  @Column(name = "CASE_NAME")
  private String caseName;

  @Column(name = "BONDTEMPLATE_CODE")
  private String bondTemplateCode;

  @Column(name = "BONDTEMPLATE_NAME")
  private String bondTemplateName;

  @Column(name = "GROUP_ID")
  @JsonIgnore
  private String groupId;

  @Column(name = "GROUP_NAME")
  private String groupName;

  @Column(name = "BOND_ID")
  private Integer bondId;

  @Column(name = "BOND_CODE")
  private String bondCode;

  @Column(name = "BOND_NAME")
  private String bondName;

  @Column(name = "ISSUER_ID")
  private Integer issuerId;

  @Column(name = "ISSUER_NAME")
  private String issuerName;

  @Column(name = "FEE_TYPE_LEVEL1_CODE")
  private String feeTypeLevel1Code;

  @Column(name = "FEE_TYPE_LEVEL1_NAME")
  private String feeTypeLevel1Name;

  @Column(name = "FEE_TYPE_LEVEL2_CODE")
  private String feeTypeLevel2Code;

  @Column(name = "FEE_TYPE_LEVEL2_NAME")
  private String feeTypeLevel2Name;

  @Column(name = "FEE_BASE_CODE")
  private String feeBaseCode;

  @Column(name = "FEE_BASE_NAME")
  private String feeBaseName;

  @Column(name = "REVENUE_RECOGNITION_DATE_CODE")
  private String revenueRecognitionCode;

  @Column(name = "REVENUE_RECOGNITION_DATE_NAME")
  private String revenueRecognitionName;

  @Column(name = "PAYMENT_PERIOD")
  private Integer paymentPeriod;

  @Column(name = "START_DATE")
  private String startDate;

  @Column(name = "END_DATE")
  private String endDate;

  @Column(name = "BILL_DATE")
  private String billDate;

  @Column(name = "TENTATIVE_DATE")
  private String tentativeDate;

  @Column(name = "PAYMENT_DATE")
  private String paymentDate;

  @Column(name = "PAYMENT_EXPIRED_DAY")
  private Integer paymentExpiredDay;

  @Column(name = "FEE_PERCENT")
  private Float feePercent;

  @Column(name = "FEE_AMOUNT")
  private Long feeAmount;

  @Column(name = "FEE_AMOUNT_VAT")
  private Long feeAmountVat;

  @Column(name = "FEE_AMOUNT_FINAL")
  private Long feeAmountFinal;

  @Column(name = "ACTUAL_FEE_AMOUNT")
  private Long actualFeeAmount;

  @Column(name = "EVENT_STATUS_CODE")
  private String eventStatusCode;

  @Column(name = "EVENT_STATUS_NAME")
  private String eventStatusName;

  @Column(name = "OTHER_STATUS")
  private String otherStatusCode;

  @Column(name = "OTHER_STATUS_NAME")
  private String otherStatusName;

  @Column(name = "EVENT_ENGINE_STATUS")
  @JsonIgnore
  private String eventEngineStatus;

  @Column(name = "EVENT_NOTE")
  private String eventNote;

  @Column(name = "EXTENSION_TIME")
  private String extensionTime;

  @Column(name = "BPM_TASK_ID")
  @JsonIgnore
  private Integer taskId;

  @Column(name = "DOC_COUNT")
  private Integer docCount;

  @Column(name = "TOTAL_VOLUME")
  private Long totalVolume;

  @Column(name = "UPDATED_AT")
  private String updatedAt;

  @Column(name = "MIS_RETAIL")
  private Boolean misRetail;
}
