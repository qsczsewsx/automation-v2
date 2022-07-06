package com.tcbs.automation.hth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "H2H_BANK_INFO")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HthBankInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "CITAD")
  private String citad;
  @Column(name = "BANK_CODE")
  private String bankCode;
  @Column(name = "BANK_NAME")
  private String bankName;
  @Column(name = "BRANCH_ID")
  private String branchId;
  @Column(name = "BRANCH_NAME")
  private String branchName;
  @Column(name = "STATE_PROVINCE")
  private String stateProvince;
  @Column(name = "IS_CENTRALIZED_BANK")
  private BigDecimal isCentralizedBank;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;

  public static List<HthBankInfo> getByIsCentralizedBank(BigDecimal isCentralizedBank) {
    return HthDb.h2hConnection.getSession().createQuery(
      "from HthBankInfo where isCentralizedBank=:isCentralizedBank"
    ).setParameter("isCentralizedBank", isCentralizedBank).getResultList();
  }
}
