package com.tcbs.automation.otp_wt;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "SMS_EXCEPTION_INFO")
public class SmsExceptionInfoWT {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @NotNull
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "START_DATE")
  private String startDate;
  @Column(name = "END_DATE")
  private String endDate;
  @Column(name = "RANGE_VALUE")
  private String rangeValue;
  @NotNull
  @Column(name = "RANGE_TYPE")
  private String rangeType;
  @NotNull
  @Column(name = "NOTE")
  private String note;
  @NotNull
  @Column(name = "CREATED")
  private String created;


}
