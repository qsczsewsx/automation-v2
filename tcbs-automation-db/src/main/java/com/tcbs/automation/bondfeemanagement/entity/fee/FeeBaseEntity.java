package com.tcbs.automation.bondfeemanagement.entity.fee;

import com.tcbs.automation.bondfeemanagement.ApprovalEntity;
import com.tcbs.automation.bondfeemanagement.ReferenceData;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@MappedSuperclass
public class FeeBaseEntity extends ApprovalEntity {
  @Column(name = "GROUP_ID")
  private Integer groupId;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "FEE_TYPE_LEVEL1_CODE")
  private ReferenceData feeTypeLevel1CodeRef;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "FEE_TYPE_LEVEL2_CODE")
  private ReferenceData feeTypeLevel2CodeRef;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "FEE_BASE_CODE")
  private ReferenceData feeBaseCodeRef;

  @Column(name = "FEE_PERCENT")
  private Float feePercent;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "REVENUE_RECOGNITION_DATE_CODE")
  private ReferenceData revenueRecognitionCodeRef;

  //Thời đểm xuất hóa đơn - ngay cu the
  @Column(name = "REVENUE_RECOGNITION_DATE")
  private Date revenueRecognitionDateDB;

  //Loại Điều chỉnh thời điểm xuất hóa đơn - cach chuyen ngay
  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "REVENUE_RECOGNITION_DATE_SHIFT_TYPE_CODE")
  private ReferenceData revenueRecognitionDateShiftTypeCodeRef;

  //Điều chỉnh thời điểm xuất hóa đơn - Số ngày/tháng/năm
  @Column(name = "REVENUE_RECOGNITION_DATE_SHIFT_DATE")
  private Integer revenueRecognitionDateShiftDate;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "TERM_RULE_CODE")
  private ReferenceData termRuleCodeRef;

  @Column(name = "TERM_SLIDE_DAY")
  private Integer termSlideDay;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "TERM_DAY_TYPE_CODE")
  private ReferenceData termDayTypeCodeRef;

  @Column(name = "TERM_NOTE")
  private String termNote;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "EVENT_TYPE_CODE")
  private ReferenceData eventTypeCodeRef;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "START_DATE_CODE")
  private ReferenceData startDateCodeRef;

  //Ngày hiệu lực (ngày cụ thể )
  @Column(name = "START_DATE_SPECIFIC")
  private Date startDateSpecificDB;

  //Điều chỉnh ngày hiệu lực (+ tháng)
  @Column(name = "START_DATE_SHIFT_MONTH")
  private Integer startDateShiftMonth;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "END_DATE_CODE")
  private ReferenceData endDateCodeRef;

  //Ngày hết hiệu lực (ngày cụ thể )
  @Column(name = "END_DATE_SPECIFIC")
  private Date endDateSpecificDB;

  //Điều chỉnh ngày hết hiệu lực (+ tháng)
  @Column(name = "END_DATE_SHIFT_MONTH")
  private Integer endDateShiftMonth;

  @Column(name = "PERIOD_MONTH")
  private Integer periodMonth;

  @Column(name = "IS_SAME_PERIODS")
  private Integer isSamePeriodsDB;

  @Column(name = "IS_GROUP_LAST_TERM")
  private Integer isGroupLastTermDB;

  //Thời gian tính phí (tháng), tenorMonth = tenorYear * 12 + tenorMonth of BondTemplate
  @Column(name = "TENOR_MONTH")
  private Integer tenorMonth;

  //Thời gian tính phí (ngày)
  @Column(name = "TENOR_DAY")
  private Integer tenorDay;

  public FeeBaseEntity(FeeBaseEntity feeBase) {
    if (feeBase != null) {
      this.groupId = feeBase.getGroupId();
      this.feeTypeLevel1CodeRef = feeBase.getFeeTypeLevel1CodeRef();
      this.feeTypeLevel2CodeRef = feeBase.getFeeTypeLevel2CodeRef();
      this.feeBaseCodeRef = feeBase.getFeeBaseCodeRef();
      this.feePercent = feeBase.getFeePercent();
      this.revenueRecognitionCodeRef = feeBase.getRevenueRecognitionCodeRef();
      this.termRuleCodeRef = feeBase.getTermRuleCodeRef();
      this.termSlideDay = feeBase.getTermSlideDay();
      this.termDayTypeCodeRef = feeBase.getTermDayTypeCodeRef();
      this.termNote = feeBase.getTermNote();
      this.eventTypeCodeRef = feeBase.getEventTypeCodeRef();
      this.startDateCodeRef = feeBase.getStartDateCodeRef();
      this.endDateCodeRef = feeBase.getEndDateCodeRef();
      this.periodMonth = feeBase.getPeriodMonth();
      this.isSamePeriodsDB = feeBase.getIsSamePeriodsDB();

      this.setStatus(feeBase.getStatus());
      this.setApprovalStatus(feeBase.getApprovalStatus());
      this.setAction(feeBase.getAction());
    }
  }
}
