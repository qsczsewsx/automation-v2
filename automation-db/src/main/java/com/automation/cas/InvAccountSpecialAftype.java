package com.automation.cas;

import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Table(name = "INV_ACCOUNT_SPECIAL_AFTYPE")
public class InvAccountSpecialAftype {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "USERNAME")
  private String username;
  @NotNull
  @Column(name = "CREATED_DATE")
  private Date createdDate;
  @NotNull
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;
  @Column(name = "NOTE")
  private String note;
  @Column(name = "STATUS")
  private String status;

  public static void updateStatusByUserName(String username, String newStatus) {
    Session session = CAS.casConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvAccountSpecialAftype> query = session.createQuery(
      "update InvAccountSpecialAftype set status=:status where username=:username", InvAccountSpecialAftype.class);
    query.setParameter("status", newStatus)
      .setParameter("username", username)
      .executeUpdate();
    session.getTransaction().commit();
  }

  public static InvAccountSpecialAftype getDataByUsername(String username) {
    Session session = CAS.casConnection.getSession();
    Query<InvAccountSpecialAftype> query = session.createQuery(
      "from InvAccountSpecialAftype where username=:username", InvAccountSpecialAftype.class);
    return query.setParameter("username", username).getSingleResult();
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public Date getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Date updatedDate) {
    this.updatedDate = updatedDate;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
