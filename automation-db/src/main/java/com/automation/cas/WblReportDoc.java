package com.automation.cas;

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

@Entity
@Table(name = "WBL_REPORT_DOC")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WblReportDoc {
  private static Logger logger = LoggerFactory.getLogger(WblReportDoc.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "DOC_NO")
  private String docNo;
  @Column(name = "ACTION_ID")
  private BigDecimal actionId;
  @Column(name = "ORG_CODE")
  private String orgCode;
  @Column(name = "START_DATETIME")
  private Timestamp startDatetime;
  @Column(name = "END_DATETIME")
  private Timestamp endDatetime;
  @Column(name = "CREATED_DATETIME")
  private Timestamp createdDatetime;
  @Column(name = "META_DATA")
  private String metaData;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "REF_DOC_ID")
  private BigDecimal refDocId;
  @Column(name = "POLICY_ID")
  private BigDecimal policyId;
  @Column(name = "USER_ID")
  private BigDecimal userId;

  @Step
  public static WblReportDoc getByDocNo(String docNo) {
    Query<WblReportDoc> query = CAS.casConnection.getSession().createQuery(
      "from WblReportDoc a where a.docNo=:docNo", WblReportDoc.class);
    query.setParameter("docNo", docNo);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new WblReportDoc();
    }
  }

  @Step
  public static WblReportDoc getById(String id) {
    Query<WblReportDoc> query = CAS.casConnection.getSession().createQuery(
      "from WblReportDoc a where a.id=:id", WblReportDoc.class);
    query.setParameter("id", new BigDecimal(id));
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new WblReportDoc();
    }
  }

  @Step
  public static void deleteByDocNo(String docNo) {
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    org.hibernate.query.Query query = CAS.casConnection.getSession().createQuery(
      "Delete from WblReportDoc a where a.docNo=:docNo");
    query.setParameter("docNo", docNo);
    query.executeUpdate();
    CAS.casConnection.getSession().getTransaction().commit();
  }

}
