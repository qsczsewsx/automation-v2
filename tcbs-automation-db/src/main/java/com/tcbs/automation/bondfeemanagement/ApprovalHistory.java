package com.tcbs.automation.bondfeemanagement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Clob;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "APPROVAL_HISTORY")
public class ApprovalHistory {
  private static String paramObjectId = "objectId";
  private static String paramObjectType = "objectType";

  @Id
  @Column(name = "ID", updatable = false, nullable = false)
  private Integer id;

  @Column(name = "OBJECT_TYPE")
  private String objectType;

  @Column(name = "OBJECT_ID")
  private String objectId;

  @Column(name = "APPROVAL_STATUS")
  private String approvalStatus;

  @Column(name = "NOTE")
  private String note;

  @Column(name = "CONTENT")
  private Clob contentDB;

  @Column(name = "APPROVAL_TIME", updatable = false, nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  @JsonIgnore
  private Date approvalTimeDB = null;

  @Column(name = "APPROVAL_BY", updatable = false, nullable = false)
  private String approvalBy = null;

  @Step
  public static void deleteById(String objectId, String objectType) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    org.hibernate.query.Query<?> query = session.createNativeQuery("delete APPROVAL_HISTORY where OBJECT_ID = :objectId and OBJECT_TYPE = :objectType");
    query.setParameter(paramObjectId, objectId);
    query.setParameter(paramObjectType, objectType);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void truncateData() {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("truncate table APPROVAL_HISTORY ");
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void deleteById(Integer id) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    org.hibernate.query.Query<?> query = session.createNativeQuery("delete APPROVAL_HISTORY where id = :id ");
    query.setParameter("id", id);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static com.tcbs.automation.bondfeemanagement.ApprovalHistory getLastRecordByObjectId(String objectId, String objectType) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<com.tcbs.automation.bondfeemanagement.ApprovalHistory> query = session.createNativeQuery(
      "select * from APPROVAL_HISTORY where OBJECT_ID = :objectId and OBJECT_TYPE = :objectType and ROWNUM = 1 order by APPROVAL_TIME DESC",
      com.tcbs.automation.bondfeemanagement.ApprovalHistory.class);
    query.setParameter(paramObjectId, objectId);
    query.setParameter(paramObjectType, objectType);
    List<com.tcbs.automation.bondfeemanagement.ApprovalHistory> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results.get(0);
  }

  @Step
  public static void insert(com.tcbs.automation.bondfeemanagement.ApprovalHistory approvalHistory) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.save(approvalHistory);
    trans.commit();
  }

  @Step
  public static void deleteByIdAndObjectType(String objectId, String objectType) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    Query<?> query = session.createNativeQuery("delete APPROVAL_HISTORY where OBJECT_ID = :objectId and OBJECT_TYPE =:objectType");
    query.setParameter(paramObjectId, objectId);
    query.setParameter(paramObjectType, objectType);
    query.executeUpdate();
    trans.commit();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getObjectType() {
    return objectType;
  }

  public void setObjectType(String objectType) {
    this.objectType = objectType;
  }

  public String getObjectId() {
    return objectId;
  }

  public void setObjectId(String objectId) {
    this.objectId = objectId;
  }

  public String getApprovalStatus() {
    return approvalStatus;
  }

  public void setApprovalStatus(String approvalStatus) {
    this.approvalStatus = approvalStatus;
  }

  public Clob getContentDB() {
    return contentDB;
  }

  public void setContentDB(Clob contentDB) {
    this.contentDB = contentDB;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public Date getApprovalTimeDB() {
    return approvalTimeDB;
  }

  public void setApprovalTimeDB(Date approvalTimeDB) {
    this.approvalTimeDB = approvalTimeDB;
  }

  public String getApprovalBy() {
    return approvalBy;
  }

  public void setApprovalBy(String approvalBy) {
    this.approvalBy = approvalBy;
  }

}
