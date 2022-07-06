package com.tcbs.automation.dwh.biz;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@Entity
@Table(name = "RT_tcb_ISSUER")
public class RtTcbIssuerEntity {
  @Id
  private int id;
  private String name;
  private String businessNo;
  private Timestamp businessDate;
  private String businessBy;
  private String representUser;
  private String repersentRole;
  private String phone;
  private String fax;
  private String email;
  private String address;
  private String createdDate;
  private String updateDate;
  private Integer active;
  private String type;
  private Integer etlCurDate;
  private Timestamp etlRunDateTime;

  @Step("insert data")
  public static void insertRtIssuer(RtTcbIssuerEntity entity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query = session.createNativeQuery("insert into RT_tcb_ISSUER " +
      "(ID, Name, BusinessNo, BusinessDate, BusinessBy, RepresentUser, RepersentRole, Phone, Fax, Address, " +
      "Email, CreatedDate, UpdateDate, Active, [Type], EtlCurDate, EtlRunDateTime) " +
      "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
    query.setParameter(1, entity.getId());
    query.setParameter(2, entity.getName());
    query.setParameter(3, entity.getBusinessNo());
    query.setParameter(4, entity.getBusinessDate());
    query.setParameter(5, entity.getBusinessBy());
    query.setParameter(6, entity.getRepresentUser());
    query.setParameter(7, entity.getRepersentRole());
    query.setParameter(8, entity.getPhone());
    query.setParameter(9, entity.getFax());
    query.setParameter(10, entity.getAddress());
    query.setParameter(11, entity.getEmail());
    query.setParameter(12, entity.getCreatedDate());
    query.setParameter(13, entity.getUpdateDate());
    query.setParameter(14, entity.getActive());
    query.setParameter(15, entity.getType());
    query.setParameter(16, Integer.valueOf(new SimpleDateFormat("yyyyMMdd").format(new Date())));
    query.setParameter(17, new Date());

    query.executeUpdate();
    trans.commit();
  }

  @Step("delete data by object")
  public static void deleteIssuerByID(RtTcbIssuerEntity entity) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<?> query = session.createNativeQuery("DELETE RT_tcb_ISSUER WHERE ID = :id ");
    query.setParameter("id", entity.getId());
    query.executeUpdate();
    session.getTransaction().commit();
    Dwh.dwhDbConnection.closeSession();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getBusinessNo() {
    return businessNo;
  }

  public void setBusinessNo(String businessNo) {
    this.businessNo = businessNo;
  }

  public Timestamp getBusinessDate() {
    return businessDate;
  }

  public void setBusinessDate(Timestamp businessDate) {
    this.businessDate = businessDate;
  }

  public String getBusinessBy() {
    return businessBy;
  }

  public void setBusinessBy(String businessBy) {
    this.businessBy = businessBy;
  }

  public String getRepresentUser() {
    return representUser;
  }

  public void setRepresentUser(String representUser) {
    this.representUser = representUser;
  }

  public String getRepersentRole() {
    return repersentRole;
  }

  public void setRepersentRole(String repersentRole) {
    this.repersentRole = repersentRole;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getFax() {
    return fax;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(String createdDate) {
    this.createdDate = createdDate;
  }

  public String getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(String updateDate) {
    this.updateDate = updateDate;
  }

  public Integer getActive() {
    return active;
  }

  public void setActive(Integer active) {
    this.active = active;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Integer getEtlCurDate() {
    return etlCurDate;
  }

  public void setEtlCurDate(Integer etlCurDate) {
    this.etlCurDate = etlCurDate;
  }

  public Timestamp getEtlRunDateTime() {
    return etlRunDateTime;
  }

  public void setEtlRunDateTime(Timestamp etlRunDateTime) {
    this.etlRunDateTime = etlRunDateTime;
  }
}
