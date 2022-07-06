package com.tcbs.automation.tcinvest;

import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Table(name = "INV_BOND")
public class InvBond {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "BOND_ID")
  private String bondId;
  @Column(name = "BOND_CATEGORY_ID")
  private String bondCategoryId;
  @Column(name = "BOND_STATIC_ID")
  private String bondStaticId;
  @Column(name = "CODE")
  private String code;
  @Column(name = "UNIT_MINIMUM")
  private String unitMinimum;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "ACTIVE")
  private String active;
  @Column(name = "LTV")
  private String ltv;
  @Column(name = "PRICING_PER_UNIT")
  private String pricingPerUnit;
  @Column(name = "CREATED_DATE")
  private Date createdDate;
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;
  @Column(name = "PERIOD")
  private String period;
  @Column(name = "PERIOD_TYPE")
  private String periodType;

  public static void deleteProductByCode(String code) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvBond> query = session.createQuery(
      "DELETE FROM InvBond ib WHERE ib.code like :code"
    );
    query.setParameter("code", "%" + code + "%");
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static List<InvBond> getBuyCode(String code) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    Query<InvBond> query = session.createQuery("from InvBond ib where ib.code =: code");
    query.setParameter("code", code);
    List<InvBond> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  public static Long getBondStaticIdByProductCode(String productCode) {
    Query<InvBond> query = TcInvest.tcInvestDbConnection.getSession()
      .createQuery("from InvBond where code =:productCode", InvBond.class)
      .setParameter("productCode", productCode);
    return Long.valueOf(query.getSingleResult().getBondStaticId());
  }

}
