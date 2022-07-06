package com.tcbs.automation.cas;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

import static com.tcbs.automation.cas.CAS.casConnection;

@Getter
@Setter
@Entity
@Table(name = "TCBS_ID_PLACE")
public class TcbsIdPlace {
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
  public static TcbsIdPlace getByName(String name) {
    Query<TcbsIdPlace> query = CAS.casConnection.getSession().createQuery(
      "from TcbsIdPlace a where a.name=:name", TcbsIdPlace.class);
    query.setParameter("name", name);
    return query.getSingleResult();
  }

  public static TcbsIdPlace getByProvinceCode(String provinceCode) {
    Query<TcbsIdPlace> query = CAS.casConnection.getSession().createQuery(
      "from TcbsIdPlace a where a.provinceCode=:provinceCode", TcbsIdPlace.class);
    query.setParameter("provinceCode", provinceCode);
    return query.getSingleResult();
  }

  public static List<TcbsIdPlace> getListByProvinceCode(String provinceCode) {
    Query<TcbsIdPlace> query = CAS.casConnection.getSession().createQuery(
      "from TcbsIdPlace a where a.provinceCode=:provinceCode", TcbsIdPlace.class);
    query.setParameter("provinceCode", provinceCode);
    return query.getResultList();
  }

  public static List<TcbsIdPlace> getListData() {
    Query<TcbsIdPlace> query = CAS.casConnection.getSession().createQuery(
      "from TcbsIdPlace a ", TcbsIdPlace.class);
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

