package com.tcbs.automation.ipartner.aftype;

import com.tcbs.automation.functions.PublicConstant;
import com.tcbs.automation.ipartner.IWpartner;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

@Entity
@Table(name = "sequence_action_history")
public class SequenceActionHistory {

  private static Logger logger = LoggerFactory.getLogger(SequenceActionHistory.class);
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "id")
  private Long id;
  @Column(name = "tcbsid")
  private String tcbsid;
  @Column(name = "sequence_action_id")
  private Long sequenceActionId;
  @Column(name = "result")
  private String result;
  @Column(name = "created_at")
  private Timestamp createdAt;
  @Column(name = "modified_at")
  private Timestamp modifiedAt;
  @Column(name = "created_by")
  private String createdBy;
  @Column(name = "modified_by")
  private String modifiedBy;
  @Column(name = "result_detail")
  private String resultDetail;

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


  public Long getSequenceActionId() {
    return sequenceActionId;
  }

  public void setSequenceActionId(Long sequenceActionId) {
    this.sequenceActionId = sequenceActionId;
  }


  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }


  public String getCreatedAt() {
    return createdAt == null ? null : PublicConstant.dateTimeFormat.format(createdAt);
  }

  public void setCreatedAt(String createdAt) throws ParseException {
    this.createdAt = new Timestamp(PublicConstant.dateTimeFormat.parse(createdAt).getTime());
  }


  public String getModifiedAt() {
    return modifiedAt == null ? null : PublicConstant.dateTimeFormat.format(modifiedAt);
  }

  public void setModifiedAt(String modifiedAt) throws ParseException {
    this.modifiedAt = new Timestamp(PublicConstant.dateTimeFormat.parse(modifiedAt).getTime());
  }


  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }


  public String getModifiedBy() {
    return modifiedBy;
  }

  public void setModifiedBy(String modifiedBy) {
    this.modifiedBy = modifiedBy;
  }


  public String getResultDetail() {
    return resultDetail;
  }

  public void setResultDetail(String resultDetail) {
    this.resultDetail = resultDetail;
  }

  @Step
  public List<SequenceActionHistory> getSequenceActionHistory() {
    Query<SequenceActionHistory> query = IWpartner.iwpDBConnection.getSession().createQuery("from SequenceActionHistory ", SequenceActionHistory.class);
    List<SequenceActionHistory> result = query.getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  public SequenceActionHistory getSequenceActionHistoryByTcbsId(String tcbsid, String result) {
    Session session = IWpartner.iwpDBConnection.getSession();
    session.clear();
    Query<SequenceActionHistory> query = session.createQuery("from SequenceActionHistory where tcbsid = :tcbsid and result = :result", SequenceActionHistory.class);
    query.setParameter("tcbsid", tcbsid);
    query.setParameter("result", result);
    SequenceActionHistory rs = query.getSingleResult();
    return rs;
  }

  public void delete() {
    Session session = IWpartner.iwpDBConnection.getSession();
    session.clear();
    session.beginTransaction();
    session.delete(this);
    session.getTransaction().commit();
  }

  public void deleteByTcbsId(String tcbsId) {
    try {
      Session session = IWpartner.iwpDBConnection.getSession();
      session.clear();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      Query query = session.createQuery("DELETE SequenceActionHistory WHERE tcbsid=:tcbsId");
      query.setParameter("tcbsId", tcbsId);
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }
}
