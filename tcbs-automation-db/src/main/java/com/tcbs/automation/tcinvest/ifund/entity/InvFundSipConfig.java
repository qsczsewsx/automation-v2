package com.tcbs.automation.tcinvest.ifund.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("unchecked")
@Table(name = "INV_FUND_SIP_CONFIG")
public class InvFundSipConfig {
  public static Session session;
  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(name = "CODE")
  private String code;
  @Column(name = "DISP_NAME")
  private String dispName;
  @Column(name = "ACTION_ID")
  private String actionId;
  @Column(name = "SIP_PERIOD_UNIT")
  private String sipPeriodUnit;
  @Column(name = "SIP_PERIOD")
  private String sipPeriod;
  @Column(name = "GEN_DATE_PARAM")
  private String genDateParam;
  @Column(name = "MAX_CONT_FAIL_SIP")
  private String maxContFailSip;
  @Column(name = "MIN_CONT_SIP_DATE_DIFF")
  private String minContSipDateDiff;

  public List<InvFundSipConfig> getAllSipConfig() {
    Query<InvFundSipConfig> query = session.createQuery("from InvFundSipConfig");
    List<InvFundSipConfig> result = query.getResultList();
    return result;
  }
}
