package com.tcbs.automation.ipartner.aftype;

import com.tcbs.automation.functions.PublicConstant;
import com.tcbs.automation.ipartner.IWpartner;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

@Entity
@Table(name = "sequence_config_action")
public class SequenceConfigAction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "id")
  private Long id;
  @Column(name = "action")
  private String action;
  @Column(name = "sequence_config_id")
  private Long sequenceConfigId;
  @Column(name = "created_at")
  private Timestamp createdAt;
  @Column(name = "modified_at")
  private Timestamp modifiedAt;
  @Column(name = "created_by")
  private String createdBy;
  @Column(name = "modified_by")
  private String modifiedBy;
  @Column(name = "status")
  private String status;
  @Column(name = "description")
  private String description;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }


  public Long getSequenceConfigId() {
    return sequenceConfigId;
  }

  public void setSequenceConfigId(Long sequenceConfigId) {
    this.sequenceConfigId = sequenceConfigId;
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

  @Step
  public List<SequenceConfigAction> getSequenceConfigAction() {
    Query<SequenceConfigAction> query = IWpartner.iwpDBConnection.getSession().createQuery("from SequenceConfigAction ", SequenceConfigAction.class);
    List<SequenceConfigAction> result = query.getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }
}
