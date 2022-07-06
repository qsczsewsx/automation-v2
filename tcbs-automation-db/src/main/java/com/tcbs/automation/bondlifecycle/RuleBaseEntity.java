package com.tcbs.automation.bondlifecycle;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Clob;
import java.util.Date;


@MappedSuperclass
@Setter
@Getter
public class RuleBaseEntity extends ApprovalEntity {
  @Column(name = "PARENT_ID")
  private Integer parentId;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "EVENT_NAME_ID")
  private EventName eventName;

  @Column(name = "DESCRIPTION")
  private Clob descriptionDB;

  @Column(name = "DESC_NO_ACCENT")
  private Clob descNoAccentDB;

  @Column(name = "BUSINESS_ID")
  private Integer businessId;

  @Column(name = "PARTICIPANT_ID")
  private Integer participantId;

  @Column(name = "CONTRACT_REFS")
  private String contractRefs;

  @Column(name = "CONTRACT_REFS_NO_ACCENT")
  private String contractRefsNoAccent;

  @Column(name = "PERIOD_MONTH")
  private Integer periodMonth;

  @Column(name = "PERIOD_START_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date periodStartDateDB = null;

  @Column(name = "SLIDE_DAY")
  private Integer slideDay;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "EVENT_RULE_CODE")
  private ReferenceData eventRuleCodeRef;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "TIME_MARK_CODE")
  private ReferenceData timeMarkCodeRef;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "DAY_TYPE_CODE")
  private ReferenceData dayTypeCodeRef;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "SLIDE_DAY_CODE")
  private ReferenceData slideDayCodeRef;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "START_DATE_CODE")
  private ReferenceData startDateCodeRef;

  @Column(name = "START_DATE_OTHER")
  @Temporal(TemporalType.TIMESTAMP)
  private Date startDateOtherDB = null;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "END_DATE_CODE")
  private ReferenceData endDateCodeRef;

  @Column(name = "END_DATE_OTHER")
  @Temporal(TemporalType.TIMESTAMP)
  private Date endDateOtherDB = null;

  public RuleBaseEntity(RuleBaseEntity ruleBase) {
    if (ruleBase == null) {
      return;
    }

    this.parentId = ruleBase.getParentId();
    this.eventName = ruleBase.getEventName();
    this.descriptionDB = ruleBase.getDescriptionDB();
    this.descNoAccentDB = ruleBase.getDescNoAccentDB();
    this.businessId = ruleBase.getBusinessId();
    this.participantId = ruleBase.getParticipantId();
    this.contractRefs = ruleBase.getContractRefs();
    this.contractRefsNoAccent = ruleBase.getContractRefsNoAccent();
    this.periodMonth = ruleBase.getPeriodMonth();
    this.periodStartDateDB = ruleBase.getPeriodStartDateDB();
    this.slideDay = ruleBase.getSlideDay();
    this.eventRuleCodeRef = ruleBase.getEventRuleCodeRef();
    this.timeMarkCodeRef = ruleBase.getTimeMarkCodeRef();
    this.dayTypeCodeRef = ruleBase.getDayTypeCodeRef();
    this.slideDayCodeRef = ruleBase.getSlideDayCodeRef();
    this.startDateCodeRef = ruleBase.getStartDateCodeRef();
    this.startDateOtherDB = ruleBase.getStartDateOtherDB();
    this.endDateCodeRef = ruleBase.getEndDateCodeRef();
    this.endDateOtherDB = ruleBase.getEndDateOtherDB();
  }
}
