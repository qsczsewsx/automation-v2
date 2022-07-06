package com.tcbs.automation.caservice;

import com.tcbs.automation.functions.PublicConstant;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;

@Entity
@Table(name = "USERS")
public class Users {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "USER_ID")
  private String userId;
  @Column(name = "SERIAL")
  private String serial;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "LAST_UPDATED_DATE")
  private Timestamp lastUpdatedDate;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "TCBS_ID")
  private String tcbsId;


  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }


  public String getSerial() {
    return serial;
  }

  public void setSerial(String serial) {
    this.serial = serial;
  }


  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }


  public String getLastUpdatedDate() {
    return lastUpdatedDate == null ? null : PublicConstant.dateTimeFormat.format(lastUpdatedDate);
  }

  public void setLastUpdatedDate(String lastUpdatedDate) throws ParseException {
    this.lastUpdatedDate = new Timestamp(PublicConstant.dateTimeFormat.parse(lastUpdatedDate).getTime());
  }


  public String getCreatedDate() {
    return createdDate == null ? null : PublicConstant.dateTimeFormat.format(createdDate);
  }

  public void setCreatedDate(String createdDate) throws ParseException {
    this.createdDate = new Timestamp(PublicConstant.dateTimeFormat.parse(createdDate).getTime());
  }


  public String getTcbsId() {
    return tcbsId;
  }

  public void setTcbsId(String tcbsId) {
    this.tcbsId = tcbsId;
  }

  @Step
  public Users getUserByTcbsIdAndSerial(String tcbsId, String serial) {
    Session session = CAService.caserviceConnection.getSession();
    session.clear();

    Query<Users> query = session.createQuery("from Users a where a.tcbsId=:tcbsId and a.serial=:serial");
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("serial", serial);

    return query.getSingleResult();
  }

}
