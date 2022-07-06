package com.tcbs.automation.bondfeemanagement;

import com.tcbs.automation.bondlifecycle.BaseEntity;
import lombok.Getter;
import lombok.Setter;
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
@Setter
@Getter
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

  @Column(name = "VALUE1")
  private String value1;

  @Column(name = "VALUE2")
  private String value2;

  @Column(name = "PARENTS")
  private String parents;

  @Column(name = "ORDER_NUMBER")
  private String orderNumber;

  @Column(name = "IS_ACTIVE")
  private Integer isActiveDB;

  @Step
  public static List<ReferenceData> findByCategory(String category) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Query<ReferenceData> query = session.createNativeQuery("SELECT * FROM REFERENCE_DATA where IS_ACTIVE = 1 and CATEGORY_TYPE = :category", ReferenceData.class);
    query.setParameter("category", category);
    return query.getResultList();
  }

  @Step
  public static List<ReferenceData> findByParent(String parenCode, Integer[] status) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Query<ReferenceData> query = session.createNativeQuery("select * from REFERENCE_DATA where IS_ACTIVE IN :status and (PARENTS like '%;' || :parenCode || ';%') order by ORDER_NUMBER",
      ReferenceData.class);
    query.setParameter("parenCode", parenCode);
    query.setParameterList("status", status);
    return query.getResultList();
  }
}
