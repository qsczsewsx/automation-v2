package com.tcbs.automation.cas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;


@Entity
@Table(name = "WBL_POLICY_USER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WblPolicyUser {
  private static Logger logger = LoggerFactory.getLogger(WblPolicyUser.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "USER_ID")
  private BigDecimal userId;
  @Column(name = "POLICY_ID")
  private BigDecimal policyId;
  @Column(name = "TYPE")
  private String type;
  @Column(name = "CREATED_BY")
  private String createdBy;
  @Column(name = "CREATED_DATETIME")
  private Timestamp createdDatetime;
  @Column(name = "APPROVED_BY")
  private String approvedBy;
  @Column(name = "APPROVED_DATETIME")
  private Timestamp approvedDatetime;
  @Column(name = "UPDATED_DATETIME")
  private Timestamp updatedDatetime;
  @Column(name = "START_DATETIME")
  private Timestamp startDatetime;
  @Column(name = "END_DATETIME")
  private Timestamp endDatetime;
  @Column(name = "REF_USER_ID")
  private BigDecimal refUserId;
  @Column(name = "STATES")
  private String states;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "RELATION")
  private String relation;
  @Column(name = "NOTE")
  private String note;
  @Column(name = "BUSINESS_TITLE")
  private String businessTitle;
  @Column(name = "CAPITAL_RATIO")
  private String capitalRatio;
  @Column(name = "WBLUSER_ID")
  private BigDecimal wbluserId;
  @Column(name = "REF_WBLUSER_ID")
  private BigDecimal refWbluserId;

  @Step
  public static List<WblPolicyUser> getByWblUserIdAndPolicyId(BigDecimal wbluserId, BigDecimal policyId) {
    Query<WblPolicyUser> query = CAS.casConnection.getSession().createQuery(
      "from WblPolicyUser a where a.wbluserId=:wbluserId and a.policyId=:policyId", WblPolicyUser.class);
    query.setParameter("wbluserId", wbluserId);
    query.setParameter("policyId", policyId);
    return query.getResultList();
  }
}
