package com.tcbs.automation.bondlifecycle;

import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "CATEGORY_TYPE")
public class CategoryType extends BaseEntity {
  @Id
  @Column(name = "CATEGORY_TYPE", updatable = false)
  private String cateType;

  @Column(name = "NAME")
  private String name;

  @Column(name = "NAME_NO_ACCENT")
  private String nameNoAccent;

  @Column(name = "IS_ACTIVE")
  private Integer isActiveDB;

  @Step
  public static List<CategoryType> findAllActive() {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<CategoryType> query = session.createNativeQuery("SELECT * FROM CATEGORY_TYPE where IS_ACTIVE = 1", CategoryType.class);
    return query.getResultList();
  }

  public String getCateType() {
    return cateType;
  }

  public void setCateType(String cateType) {
    this.cateType = cateType;
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

  public Integer getIsActiveDB() {
    return isActiveDB;
  }

  public void setIsActiveDB(Integer isActiveDB) {
    this.isActiveDB = isActiveDB;
  }
}
