package com.tcbs.automation.tcinvest;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Entity
@Setter
@Table(name = "INV_INVESTMENT_PRODUCT")
public class InvInvestmentProduct {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "PRODUCT_ID")
  private String productId;
  @Column(name = "PRODUCT_NAME")
  private String productName;
  @Column(name = "PRODUCT_CODE")
  private String productCode;
  @Column(name = "UNDERLYING_INSTRUMENT_ID")
  private String underlyingInstrumentId;
  @Column(name = "UNIT_PAR_VALE")
  private String unitParVale;
  @Column(name = "NUMBER_OF_UNIT")
  private String numberOfUnit;
  @Column(name = "OWNER_ID")
  private String ownerId;
  @Column(name = "PRODUCT_CATEGORY_ID")
  private String productCategoryId;
  @Column(name = "VALUATION_METHOD_ID")
  private String valuationMethodId;
  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;
  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;
  @Column(name = "CONTRACT_CODE")
  private String contractCode;
  @Column(name = "FINANCIAL_ID")
  private String financialId;
  @Column(name = "ACTIVE")
  private String active;

  public static void deleteProductByCode(String productCode) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvInvestmentProduct> query = session.createQuery(
      "DELETE FROM InvInvestmentProduct inv WHERE inv.productCode like :productCode"
    );
    query.setParameter("productCode", "%" + productCode + "%");
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static List<InvInvestmentProduct> getBuyCode(String productCode) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    Query<InvInvestmentProduct> query = session.createQuery("from InvInvestmentProduct ib where ib.productCode =: productCode");
    query.setParameter("productCode", productCode);
    List<InvInvestmentProduct> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  public static Integer getProductIdByProductCode(String productCode) {
    Query<InvInvestmentProduct> query = TcInvest.tcInvestDbConnection.getSession()
      .createQuery("from InvInvestmentProduct ib where ib.productCode =: productCode")
      .setParameter("productCode", productCode);
    return Integer.valueOf(query.getSingleResult().getProductId());
  }

  public static List<InvInvestmentProduct> getListNoPaging() {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    String querySql = "select  p.PRODUCT_ID               as PRODUCT_ID," +
      "       p.PRODUCT_NAME             as PRODUCT_NAME,\n" +
      "       p.PRODUCT_CODE             as PRODUCT_CODE,\n" +
      "       f.CODE                     as F_CODE,\n" +
      "       f.NAME                     as F_NAME,\n" +
      "       f.BROKERAGE                as F_BROKERAGE,\n" +
      "       u.CODE                     as U_CODE,\n" +
      "       u.EXCHANGE                 as U_EXCHANGE,\n" +
      "       u.NAME                     as U_NAME,\n" +
      "       s.CODE                     as S_CODE,\n" +
      "       s.NAME                     as S_NAME,\n" +
      "       s.LISTED_CODE              as S_LISTED_CODE,\n" +
      "       s.LISTED_STATUS            as S_LISTED_STATUS\n" +
      "from INV_INVESTMENT_PRODUCT p\n" +
      "         left outer join INV_FINANCIAL_TERM f on p.FINANCIAL_ID = f.FINANCIAL_ID\n" +
      "         left outer join INV_UNDERLYING_INSTRUMENT u\n" +
      "                         on p.UNDERLYING_INSTRUMENT_ID = u.UNDERLYING_ID\n" +
      "         left outer join INV_BOND b on u.INSTRUMENT_ID = b.BOND_ID\n" +
      "         left outer join INV_BOND_STATIC s on b.BOND_STATIC_ID = s.BOND_STATIC_ID\n" +
      "where u.INSTRUMENT_TYPE = 'BOND' \n" +
      " AND p.ACTIVE = 1 \n" +
      " AND (lower(p.PRODUCT_CODE) like null or null is null) \n" +
      " AND (s.LISTED_STATUS = -1 or -1 = -1 ) \n" +
      " AND (f.CODE = null or null is null ) \n" +
      " AND (b.BOND_STATIC_ID = -1 or -1 = -1 ) \n" +
      " order by p.PRODUCT_ID desc\n";
    Query query = session.createSQLQuery(querySql);
    List<Object[]> objList = query.getResultList();
    List<InvInvestmentProduct> rs = new ArrayList<>();
    for (Object[] obj : objList) {
      InvInvestmentProduct invInvestmentProduct = new InvInvestmentProduct();
      invInvestmentProduct.setProductId(obj[0].toString());
      invInvestmentProduct.setProductName(obj[1].toString());
      invInvestmentProduct.setProductCode(obj[2].toString());
      rs.add(invInvestmentProduct);
    }
    return rs;
  }
}
