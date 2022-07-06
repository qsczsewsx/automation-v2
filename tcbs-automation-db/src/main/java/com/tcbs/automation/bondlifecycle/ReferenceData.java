package com.tcbs.automation.bondlifecycle;

import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "REFERENCE_DATA")
public class ReferenceData extends BaseEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "CODE", updatable = false)
  private String code;

  @Column(name = "NAME")
  private String name;

  @Column(name = "NAME_NO_ACCENT")
  private String nameNoAccent;

  @Column(name = "CATEGORY_TYPE")
  private String categoryType;

  @Column(name = "VALUE")
  private String value;

  @Column(name = "PARENTS")
  private String parents;

  @Column(name = "ORDER_NUMBER")
  private String orderNumber;

  @Column(name = "IS_ACTIVE")
  private Integer isActiveDB;

  @Step
  public static List<ReferenceData> findByCategory(String category) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<ReferenceData> query = session.createNativeQuery("SELECT * FROM REFERENCE_DATA where IS_ACTIVE = 1 and CATEGORY_TYPE = :category", ReferenceData.class);
    query.setParameter("category", category);
    return query.getResultList();
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNameNoAccent() {
    return nameNoAccent;
  }

  public void setNameNoAccent(String nameNoAccent) {
    this.nameNoAccent = nameNoAccent;
  }

  public String getCategoryType() {
    return categoryType;
  }

  public void setCategoryType(String categoryType) {
    this.categoryType = categoryType;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getParents() {
    return parents;
  }

  public void setParents(String parents) {
    this.parents = parents;
  }

  public String getOrderNumber() {
    return orderNumber;
  }

  public void setOrderNumber(String orderNumber) {
    this.orderNumber = orderNumber;
  }

  public Integer getIsActiveDB() {
    return isActiveDB;
  }

  public void setIsActiveDB(Integer isActiveDB) {
    this.isActiveDB = isActiveDB;
  }
}
