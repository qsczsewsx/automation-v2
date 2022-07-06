package com.tcbs.automation.ipartner.aftype;


import com.tcbs.automation.functions.PublicConstant;
import com.tcbs.automation.ipartner.IWpartner;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;

@Entity
@Table(name = "aftype_account_special")
@AllArgsConstructor
@NoArgsConstructor
public class AftypeAccountSpecial {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "id")
  private Long id;
  @Column(name = "tcbsid")
  private String tcbsid;
  @Column(name = "type")
  private String type;
  @Column(name = "description")
  private String description;
  @Column(name = "status")
  private String status;
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


  public String getTcbsid() {
    return tcbsid;
  }

  public void setTcbsid(String tcbsid) {
    this.tcbsid = tcbsid;
  }


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
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
  public List<AftypeAccountSpecial> getAftypeAccountSpecial() {
    Query<AftypeAccountSpecial> query = IWpartner.iwpDBConnection.getSession().createQuery("from AftypeAccountSpecial ", AftypeAccountSpecial.class);
    List<AftypeAccountSpecial> result = query.getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  @Step
  public List<AftypeAccountSpecial> getListAftypeAccountSpecial(String tcbsId) {
    Query<AftypeAccountSpecial> query = IWpartner.iwpDBConnection.getSession().createQuery("from AftypeAccountSpecial where tcbsid= :tcbsId");
    query.setParameter("tcbsId", tcbsId);
    return query.getResultList();
  }

  @Step
  public List<AftypeAccountSpecial> getAftypeAccountSpecialByTcbsId(String tcbsId) {
    Query<AftypeAccountSpecial> query = IWpartner.iwpDBConnection.getSession().createQuery("from AftypeAccountSpecial where tcbsid = :tcbsId order by id desc", AftypeAccountSpecial.class);
    query.setParameter("tcbsId", tcbsId);
    List<AftypeAccountSpecial> result = query.getResultList();
    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  @Step
  public AftypeAccountSpecial getSingleAftypeAccountSpecialByTcbsId(String tcbsId) {
    Query<AftypeAccountSpecial> query = IWpartner.iwpDBConnection.getSession().createQuery("from AftypeAccountSpecial where tcbsid = :tcbsId order by id desc", AftypeAccountSpecial.class);
    query.setParameter("tcbsId", tcbsId);
    query.setMaxResults(1);
    AftypeAccountSpecial result = query.getSingleResult();
    return result;
  }

  public AftypeAccountSpecial getLatestAftypeConfig() {
    Session session = IWpartner.iwpDBConnection.getSession();
    session.clear();
    Query<AftypeAccountSpecial> query = IWpartner.iwpDBConnection.getSession().createQuery("from AftypeAccountSpecial order by id desc");
    query.setMaxResults(1);
    AftypeAccountSpecial result = query.getSingleResult();
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

  public void deleteByTcbsId(String tcbsId) {
    try {
      Session session = IWpartner.iwpDBConnection.getSession();
      session.clear();
      Transaction trans = session.beginTransaction();
      Query<AftypeAccountSpecial> query = session.createQuery("DELETE AftypeAccountSpecial WHERE tcbsid =: tcbsId");
      query.setParameter("tcbsId", tcbsId);
      query.executeUpdate();
      trans.commit();
    } catch (Exception ex) {
      System.out.println("Exception " + ex.toString());
      ;
    }
  }


}
