package com.tcbs.automation.ipartner.aftype;

import com.tcbs.automation.ipartner.IWpartner;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.util.List;

@Entity
@Table(name = "aftype_config")
public class AftypeConfig {

  private static Logger logger = LoggerFactory.getLogger(AftypeConfig.class);
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "id")
  private Long id;
  @Column(name = "special_aftype")
  private String specialAftype;
  @Column(name = "no_normal_sub_account")
  private String noNormalSubAccount;
  @Column(name = "no_margin_sub_account")
  private String noMarginSubAccount;
  @Column(name = "status")
  private String status;
  @Column(name = "description")
  private String description;
  @Column(name = "created_at")
  private java.sql.Timestamp createdAt;
  @Column(name = "modified_at")
  private java.sql.Timestamp modifiedAt;
  @Column(name = "created_by")
  private String createdBy;
  @Column(name = "modified_by")
  private String modifiedBy;
  @Column(name = "sequence_action_id")
  private String sequenceActionId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getSpecialAftype() {
    return specialAftype;
  }

  public void setSpecialAftype(String specialAftype) {
    this.specialAftype = specialAftype;
  }

  public String getNoNormalSubAccount() {
    return noNormalSubAccount;
  }

  public void setNoNormalSubAccount(String noNormalSubAccount) {
    this.noNormalSubAccount = noNormalSubAccount;
  }

  public String getNoMarginSubAccount() {
    return noMarginSubAccount;
  }

  public void setNoMarginSubAccount(String noMarginSubAccount) {
    this.noMarginSubAccount = noMarginSubAccount;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public java.sql.Timestamp getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(java.sql.Timestamp createdAt) throws ParseException {
    this.createdAt = createdAt;
  }

  public java.sql.Timestamp getModifiedAt() {
    return modifiedAt;
  }

  public void setModifiedAt(java.sql.Timestamp modifiedAt) throws ParseException {
    this.modifiedAt = modifiedAt;
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

  public String getSequenceActionId() {
    return sequenceActionId;
  }

  public void setSequenceActionId(String sequenceActionId) {
    this.sequenceActionId = sequenceActionId;
  }

  @Step
  public List<AftypeConfig> getAfTypeConfigList() {
    Query<AftypeConfig> query = IWpartner.iwpDBConnection.getSession().createQuery("from AftypeConfig ", AftypeConfig.class);
    List<AftypeConfig> result = query.getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  @Step
  public AftypeConfig getAfTypeConfigById(String id) {
    Session session = IWpartner.iwpDBConnection.getSession();
    session.clear();
    Query<AftypeConfig> query = session.createQuery("from AftypeConfig where id = :id", AftypeConfig.class);
    AftypeConfig result = new AftypeConfig();
    try {
      query.setParameter("id", Long.valueOf(id));
      result = query.getSingleResult();
    } catch (Exception ex) {
      logger.info(ex.toString());
    }
    return result;
  }

  public void insert() {
    Session session = IWpartner.iwpDBConnection.getSession();
    session.clear();
    session.beginTransaction();
    session.save(this);
    session.getTransaction().commit();
  }

  public void delete() {
    Session session = IWpartner.iwpDBConnection.getSession();
    session.clear();
    session.beginTransaction();
    session.delete(this);
    session.getTransaction().commit();
  }

  public AftypeConfig getLatestAftypeConfig() {
    Session session = IWpartner.iwpDBConnection.getSession();
    session.clear();
    Query<AftypeConfig> query = session.createQuery("from AftypeConfig order by id desc");
    query.setMaxResults(1);
    AftypeConfig result = query.getSingleResult();
    return result;
  }
}
