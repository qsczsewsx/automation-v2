package com.tcbs.automation.fundstation.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "POLICY")
public class Policy {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "ID")
  private int id;

  @Column(name = "PORTFOLIO_CODE")
  private String portfolioCode;

  @Column(name = "NAME")
  private String name;

  @Column(name = "OBJECT_CODE")
  private String objectCode;

  @Column(name = "OBJECT_REF_ID")
  private Integer objectRefId;

  @Column(name = "VALUE")
  private Double targetValue;

  @Column(name = "OPERATOR")
  private String operator;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  @Column(name = "EFFECT_DATE")
  private Date effectDate;

  @Column(name = "FUNCTION_TYPE")
  private String functionType;

  @Column(name = "CALCULATION_BY")
  private String calculationBy;

  @Column(name = "LISTED")
  private String listed;

  @Column(name = "INDEX_DISPLAY")
  private Integer index;

  @Column(name = "PRIORITY")
  private Integer priority;

  public static List<Policy> getAllPolicyActiveBy(String portfolioCode, Date reportDate) {
    Query<Policy> query = session.createQuery("from Policy where portfolioCode =:portfolioCode and effectDate <=:reportDate and status = 'ACTIVE' order by effectDate desc");
    query.setParameter("portfolioCode", portfolioCode);
    query.setParameter("reportDate", reportDate);
    return query.getResultList();
  }
}
