package com.automation.cas;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

import static com.automation.cas.CAS.casConnection;

@Getter
@Setter
@Entity
@Table(name = "xxxx_ID_PLACE")
public class xxxxIdPlace {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "NAME")
  private String name;
  @Column(name = "PROVINCE_CODE")
  private String provinceCode;

  @Step
  public static xxxxIdPlace getByName(String name) {
    Query<xxxxIdPlace> query = CAS.casConnection.getSession().createQuery(
      "from xxxxIdPlace a where a.name=:name", xxxxIdPlace.class);
    query.setParameter("name", name);
    return query.getSingleResult();
  }

  public static xxxxIdPlace getByProvinceCode(String provinceCode) {
    Query<xxxxIdPlace> query = CAS.casConnection.getSession().createQuery(
      "from xxxxIdPlace a where a.provinceCode=:provinceCode", xxxxIdPlace.class);
    query.setParameter("provinceCode", provinceCode);
    return query.getSingleResult();
  }

  public static List<xxxxIdPlace> getListByProvinceCode(String provinceCode) {
    Query<xxxxIdPlace> query = CAS.casConnection.getSession().createQuery(
      "from xxxxIdPlace a where a.provinceCode=:provinceCode", xxxxIdPlace.class);
    query.setParameter("provinceCode", provinceCode);
    return query.getResultList();
  }

  public static List<xxxxIdPlace> getListData() {
    Query<xxxxIdPlace> query = CAS.casConnection.getSession().createQuery(
      "from xxxxIdPlace a ", xxxxIdPlace.class);
    return query.getResultList();
  }

  public void insert() {
    Session session = casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    try {
      session.save(this);
      session.getTransaction().commit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}

