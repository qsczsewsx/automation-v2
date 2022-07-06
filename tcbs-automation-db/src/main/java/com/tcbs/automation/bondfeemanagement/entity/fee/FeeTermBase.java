package com.tcbs.automation.bondfeemanagement.entity.fee;

import com.tcbs.automation.bondfeemanagement.BaseEntity;
import com.tcbs.automation.bondfeemanagement.ReferenceData;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@MappedSuperclass
public class FeeTermBase extends BaseEntity {

  @Column(name = "FEE_ID")
  private Integer feeId;

  @Column(name = "NAME")
  private String name;

  @Column(name = "TERM_DAY")
  private String termDay;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "FEE_BASE_CODE")
  private ReferenceData feeBaseCodeRef;

  @Column(name = "FEE_PERCENT")
  private Float feePercent;

  @Column(name = "PERIOD")
  private Integer period;

  public FeeTermBase(FeeTermBase feeTermBase) {
    if (feeTermBase != null) {
      this.feeId = feeTermBase.feeId;
      this.period = feeTermBase.period;
      this.name = feeTermBase.name;
      this.termDay = feeTermBase.termDay;
      this.feeBaseCodeRef = feeTermBase.feeBaseCodeRef;
      this.feePercent = feeTermBase.feePercent;
    }
  }

  public FeeTermBase() {

  }
}

