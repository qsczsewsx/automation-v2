package com.tcbs.automation.tcbond;

import lombok.Getter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Getter
@Table(name = "BONDPRODUCT")
public class BondProduct {

  @Id
  @Column(name = "ID")
  private Integer id;
  @Column(name = "CATEGORYID")
  private Integer categoryid;
  @Column(name = "BONDID")
  private Integer bondid;
  @Column(name = "CODE")
  private String code;
  @Column(name = "UNITMINIMUM")
  private Integer unitminimum;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "CREATEDDATE")
  private java.sql.Timestamp createddate;
  @Column(name = "UPDATEDDATE")
  private java.sql.Timestamp updateddate;
  @Column(name = "ACTIVE")
  private Integer active;
  @Column(name = "GROUPON")
  private String groupon;
  @Column(name = "LTV")
  private String ltv;
  @Column(name = "PRICINGPERUNIT")
  private String pricingperunit;
  @Column(name = "GROUPCODE")
  private String groupcode;

  public static void deleteProductByCodeOnTCBond(String code) {
    Session session = TcBond.tcBondDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }

    Query<BondProduct> query = session.createQuery(
      "DELETE FROM BondProduct WHERE code like :code"
    );
    query.setParameter("code", "%" + code + "%");
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static List<BondProduct> getByCode(String code) {
    Session session = TcBond.tcBondDbConnection.getSession();
    session.clear();
    Query<BondProduct> query = session.createQuery("from BondProduct where code =: code");
    query.setParameter("code", code);
    List<BondProduct> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  public static Integer getBondIdByProductCode(String productCode) {
    Query<BondProduct> query = TcBond.tcBondDbConnection.getSession().createQuery(
      "from BondProduct a where a.code=:code", BondProduct.class);
    query.setParameter("code", productCode);
    return query.getSingleResult().getBondid();
  }

  @Step("Get BondProduct by {0}")
  public BondProduct getBondProductByProductCode(String productCode) {
    Query<BondProduct> query = TcBond.tcBondDbConnection.getSession().createQuery(
      "from BondProduct a where a.code=:code", BondProduct.class);
    query.setParameter("code", productCode);
    return query.getSingleResult();
  }
}
