package com.tcbs.automation.ipartner.aftype;

import com.tcbs.automation.functions.PublicConstant;
import com.tcbs.automation.ipartner.IWpartner;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;

@Entity
@Table(name = "aftype_action_history")
public class AftypeActionHistory {

  private static Logger logger = LoggerFactory.getLogger(AftypeActionHistory.class);
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "id")
  private Long id;
  @Column(name = "tcbsid")
  private String tcbsid;
  @NotNull
  @Column(name = "aftype_action_id")
  private Long aftypeActionId;
  @Column(name = "result")
  private String result;
  @Column(name = "result_detail")
  private String resultDetail;
  @NotNull
  @Column(name = "sequence_action_history_id")
  private Long sequenceActionHistoryId;
  @Column(name = "created_at")
  private Timestamp createdAt;
  @Column(name = "created_by")
  private String createdBy;
  @Column(name = "modified_at")
  private Timestamp modifiedAt;
  @Column(name = "modified_by")
  private String modifiedBy;
  @NotNull
  @Column(name = "sub_account_type")
  private String subAccountType;
  @Column(name = "sub_account_no")
  private String subAccountNo;
  @Column(name = "custody_code")
  private String custodyCode;
  @Column(name = "aftype_value")
  private String aftypeValue;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


  public String getTcbsid() {
    return tcbsid;
  }

  public void setTcbsid(String tcbsid) {
    this.tcbsid = tcbsid;
  }


  public Long getAftypeActionId() {
    return aftypeActionId;
  }

  public void setAftypeActionId(Long aftypeActionId) {
    this.aftypeActionId = aftypeActionId;
  }


  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }


  public String getResultDetail() {
    return resultDetail;
  }

  public void setResultDetail(String resultDetail) {
    this.resultDetail = resultDetail;
  }


  public Long getSequenceActionHistoryId() {
    return sequenceActionHistoryId;
  }

  public void setSequenceActionHistoryId(Long sequenceActionHistoryId) {
    this.sequenceActionHistoryId = sequenceActionHistoryId;
  }


  public String getCreatedAt() {
    return createdAt == null ? null : PublicConstant.dateTimeFormat.format(createdAt);
  }

  public void setCreatedAt(String createdAt) throws ParseException {
    this.createdAt = new Timestamp(PublicConstant.dateTimeFormat.parse(createdAt).getTime());
  }


  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }


  public String getModifiedAt() {
    return modifiedAt == null ? null : PublicConstant.dateTimeFormat.format(modifiedAt);
  }

  public void setModifiedAt(String modifiedAt) throws ParseException {
    this.modifiedAt = new Timestamp(PublicConstant.dateTimeFormat.parse(modifiedAt).getTime());
  }


  public String getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(String modifiedBy) {
    this.modifiedBy = modifiedBy;
  }


  public String getSubAccountType() {
    return subAccountType;
  }

  public void setSubAccountType(String subAccountType) {
    this.subAccountType = subAccountType;
  }


  public String getSubAccountNo() {
    return subAccountNo;
  }

  public void setSubAccountNo(String subAccountNo) {
    this.subAccountNo = subAccountNo;
  }


  public String getCustodyCode() {
    return custodyCode;
  }

  public void setCustodyCode(String custodyCode) {
    this.custodyCode = custodyCode;
  }


  public String getAftypeValue() {
    return aftypeValue;
  }

  public void setAftypeValue(String aftypeValue) {
    this.aftypeValue = aftypeValue;
  }

  public void deleteByTcbsId(String tcbsId) {
    try {
      Session session = IWpartner.iwpDBConnection.getSession();
      session.clear();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      Query query = session.createQuery("DELETE AftypeActionHistory WHERE tcbsid=:tcbsId");
      query.setParameter("tcbsId", tcbsId);
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }
}
