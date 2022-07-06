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
import java.util.List;

@Setter
@Getter
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
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Query<CategoryType> query = session.createNativeQuery("SELECT * FROM CATEGORY_TYPE where IS_ACTIVE = 1", CategoryType.class);
    return query.getResultList();
  }
}
