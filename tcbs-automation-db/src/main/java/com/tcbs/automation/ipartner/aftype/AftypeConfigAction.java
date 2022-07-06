package com.tcbs.automation.ipartner.aftype;

import com.tcbs.automation.functions.PublicConstant;
import com.tcbs.automation.ipartner.IWpartner;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

@Entity
@Table(name = "aftype_config_action")
public class AftypeConfigAction {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "id")
  private Long id;
  @Column(name = "sub_account_type")
  private String subAccountType;
  @Column(name = "aftype_value")
  private String aftypeValue;
  @Column(name = "aftype_config_id")
  private int aftypeConfigId;
  @Column(name = "created_at")
  private Timestamp createdAt;
  @Column(name = "modified_at")
  private Timestamp modifiedAt;
  @Column(name = "created_by")
  private String createdBy;
  @Column(name = "modified_by")
  private String modifiedBy;


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }


  public String getSubAccountType() {
    return subAccountType;
  }

  public void setSubAccountType(String subAccountType) {
    this.subAccountType = subAccountType;
  }


  public String getAftypeValue() {
    return aftypeValue;
  }

  public void setAftypeValue(String aftypeValue) {
    this.aftypeValue = aftypeValue;
  }


  public int getAftypeConfigId() {
    return aftypeConfigId;
  }

  public void setAftypeConfigId(int aftypeConfigId) {
    this.aftypeConfigId = aftypeConfigId;
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

  @Step
  public List<AftypeConfigAction> getListAfTypeConfigAction() {
    Query<AftypeConfigAction> query = IWpartner.iwpDBConnection.getSession().createQuery("from AftypeConfigAction where aftypeConfigId in (select id from AftypeConfig)", AftypeConfigAction.class);
    List<AftypeConfigAction> result = query.getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
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

  public AftypeConfigAction getLatestAftypeConfigAction() {
    Session session = IWpartner.iwpDBConnection.getSession();
    session.clear();
    Query<AftypeConfigAction> query = session.createQuery("from AftypeConfigAction order by id desc");
    query.setMaxResults(1);
    AftypeConfigAction result = query.getSingleResult();
    return result;
  }
}
