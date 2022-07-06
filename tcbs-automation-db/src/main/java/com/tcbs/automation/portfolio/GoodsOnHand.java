package com.tcbs.automation.portfolio;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;

import static com.tcbs.automation.config.portfolio.PortfolioHandler.*;

@Getter
@Setter
@Entity
@Table(name = "GOODS_ON_HAND")
public class GoodsOnHand {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @NotNull
  @Column(name = "SHELF_ID")
  private String shelfId;
  @NotNull
  @Column(name = "PRODUCT_CODE")
  private String productCode;
  @NotNull
  @Column(name = "GOODS_DATE")
  private Date goodsDate;
  @NotNull
  @Column(name = "BALANCE")
  private Double balance;
  @NotNull
  @Column(name = "OUT")
  private Double out;
  @NotNull
  @Column(name = "BOND_CODE")
  private String bondCode;

  public static void insertShelfOnhandInfo(String id, String shelfId, String productCode, String goodsDate, String balance, String out, String bondCode) {
    Session session = PortfolioSit.goodsOrchestratorConnection.getSession();
    session.clear();

    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = session.createNativeQuery(
      String.format("INSERT INTO GOODS_ON_HAND (ID, SHELF_ID, PRODUCT_CODE, GOODS_DATE, BALANCE, OUT, BOND_CODE) VALUES ('%s', '%s', '%s', TO_DATE('%s','yyyy-MM-dd'), '%s', '%s', '%s')", id, shelfId,
        productCode, goodsDate, balance, out, bondCode));
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static List<GoodsOnHand> getByGoodsDate(String goodsDate) {
    Session session = PortfolioSit.goodsOrchestratorConnection.getSession();
    session.clear();
    Query<GoodsOnHand> query = session.createQuery("from GoodsOnHand goh where goh.goodsDate = TO_DATE(:goodsDate,'yyyy-MM-dd') order by id desc");
    query.setParameter("goodsDate", goodsDate);
    List<GoodsOnHand> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  public static GoodsOnHand getById(String id) {
    Session session = PortfolioSit.goodsOrchestratorConnection.getSession();
    session.clear();
    Query<GoodsOnHand> query = session.createQuery("from GoodsOnHand goh where goh.id =: id");
    query.setParameter("id", id);
    List<GoodsOnHand> result = query.getResultList();

    if (result.size() > 0) {
      return result.get(0);
    } else {
      return null;
    }
  }

  public static void deleteById(String id) {
    Session session = PortfolioSit.goodsOrchestratorConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<GoodsOnHand> query = session.createQuery(
      "DELETE FROM GoodsOnHand goh WHERE goh.id =:id"
    );
    query.setParameter("id", id);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void deleteByProductCode(String productCode) {
    Session session = PortfolioSit.goodsOrchestratorConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<GoodsOnHand> query = session.createQuery(
      "DELETE FROM GoodsOnHand goh WHERE goh.productCode =:productCode"
    );
    query.setParameter("productCode", productCode);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static List<GoodsOnHand> getByGoodsDateAndBond(String goodsDate, String bondCode) {
    Session session = PortfolioSit.goodsOrchestratorConnection.getSession();
    session.clear();
    Query<GoodsOnHand> query = session.createQuery("from GoodsOnHand goh where goh.goodsDate =: goodsDate and goh.bondCode = :bondCode order by id desc");
    query.setParameter("goodsDate", goodsDate);
    query.setParameter("bondCode", bondCode);
    List<GoodsOnHand> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  public static List<GoodsOnHand> getByProductVsGoodsDate(String goodsDate, String productCode) {
    Session session = PortfolioSit.goodsOrchestratorConnection.getSession();
    session.clear();
    Query<GoodsOnHand> query = session.createQuery("from GoodsOnHand goh where goh.goodsDate = TO_DATE(:goodsDate,'yyyy-MM-dd') and goh.productCode = :productCode order by id desc");
    query.setParameter("goodsDate", goodsDate);
    query.setParameter("productCode", productCode);
    List<GoodsOnHand> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  public static String sumBalOnHandLatestByProd(String prod, String goodDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(String.format("SELECT SUM(BALANCE) AS BALANCE FROM GOODS_ON_HAND WHERE PRODUCT_CODE = '%s' and GOODS_DATE >= to_date('%s', 'yyyy-mm-dd')", prod, goodDate));

    HashMap<String, Object> result = new HashMap<>();
    result = (HashMap<String, Object>) PortfolioSit.goodsOrchestratorConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getSingleResult();

    if (result.get(BALANCE) == null) {
      return "0";
    }
    return result.get(BALANCE).toString();
  }

  public static String sumBalOnHandInDayByBond(String bondCode, String goodDate, String merchant) {
    StringBuilder queryStringBuilder = new StringBuilder();

    if (!merchant.equals("")) {
      String shelfId = SHELF;
      switch (merchant) {
        case TCS:
          shelfId = SHELF;
          break;
        case HB:
          shelfId = HB_SHELF;
          break;
      }
      queryStringBuilder.append(
        String.format("SELECT SUM(BALANCE) AS BALANCE FROM GOODS_ON_HAND WHERE BOND_CODE = '%s' and GOODS_DATE = to_date('%s', 'yyyy-mm-dd') and SHELF_ID = '%s'", bondCode, goodDate, shelfId));
    } else {
      queryStringBuilder.append(String.format("SELECT SUM(BALANCE) AS BALANCE FROM GOODS_ON_HAND WHERE BOND_CODE = '%s' and GOODS_DATE = to_date('%s', 'yyyy-mm-dd')", bondCode, goodDate));
    }

    HashMap<String, Object> result = new HashMap<>();
    result = (HashMap<String, Object>) PortfolioSit.goodsOrchestratorConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getSingleResult();

    if (result.get(BALANCE) == null) {
      return "0";
    }
    return result.get(BALANCE).toString();
  }

  public static String sumBalOnHandLatestByBond(String bondCode, String goodDate) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(String.format("SELECT SUM(BALANCE) AS BALANCE FROM GOODS_ON_HAND WHERE BOND_CODE = '%s' and GOODS_DATE >= to_date('%s', 'yyyy-mm-dd')", bondCode, goodDate));

    HashMap<String, Object> result = new HashMap<>();
    result = (HashMap<String, Object>) PortfolioSit.goodsOrchestratorConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getSingleResult();

    if (result.get(BALANCE) == null) {
      return "0";
    }
    return result.get(BALANCE).toString();
  }

  public static String sumOutOnHandIndayByBond(String bondCode, String goodDate, String merchant) {
    StringBuilder queryStringBuilder = new StringBuilder();

    if (!merchant.equals("")) {
      String shelfId = SHELF;
      switch (merchant) {
        case TCS:
          shelfId = SHELF;
          break;
        case HB:
          shelfId = HB_SHELF;
          break;
      }
      queryStringBuilder.append(String.format("SELECT SUM(OUT) AS OUT FROM GOODS_ON_HAND WHERE BOND_CODE = '%s' and GOODS_DATE = to_date('%s', 'yyyy-mm-dd') and SHELF_ID = '%s'", bondCode, goodDate, shelfId));
    } else {
      queryStringBuilder.append(String.format("SELECT SUM(OUT) AS OUT FROM GOODS_ON_HAND WHERE BOND_CODE = '%s' and GOODS_DATE = to_date('%s', 'yyyy-mm-dd')", bondCode, goodDate));
    }

    HashMap<String, Object> result = new HashMap<>();
    result = (HashMap<String, Object>) PortfolioSit.goodsOrchestratorConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getSingleResult();

    if (result.get(OUT) == null) {
      return "0";
    }
    return result.get(OUT).toString();
  }

  public static String sumBalOnHandInDayByBondMerchent(String bondCode, String goodDate, String merchant) {
    StringBuilder queryStringBuilder = new StringBuilder();
    if (merchant.equals(HB)) {
      merchant = HB_SHELF;
      queryStringBuilder.append(
        String.format("SELECT SUM(BALANCE) AS BALANCE FROM GOODS_ON_HAND WHERE BOND_CODE = '%s' and GOODS_DATE = to_date('%s', 'yyyy-mm-dd') and SHELF_ID = '%s'", bondCode, goodDate, merchant));
    }
    if (merchant.equals(TCS)) {
      merchant = SHELF;
      queryStringBuilder.append(
        String.format("SELECT SUM(BALANCE) AS BALANCE FROM GOODS_ON_HAND WHERE BOND_CODE = '%s' and GOODS_DATE = to_date('%s', 'yyyy-mm-dd') and SHELF_ID = '%s'", bondCode, goodDate, merchant));
    }
    if (merchant.equals("")) {
      queryStringBuilder.append(String.format("SELECT SUM(BALANCE) AS BALANCE FROM GOODS_ON_HAND WHERE BOND_CODE = '%s' and GOODS_DATE = to_date('%s', 'yyyy-mm-dd')", bondCode, goodDate));
    }

    HashMap<String, Object> result = new HashMap<>();
    result = (HashMap<String, Object>) PortfolioSit.goodsOrchestratorConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getSingleResult();

    if (result.get(BALANCE) == null) {
      return "0";
    }
    return result.get(BALANCE).toString();
  }

  public static String sumBalOnHandLatestByBondMerchant(String bondCode, String goodDate, String merchant) {
    StringBuilder queryStringBuilder = new StringBuilder();

    String shelfId = SHELF;
    if (!merchant.equals("")) {
      if (merchant.equals(HB)) {
        shelfId = HB_SHELF;
      }
      queryStringBuilder.append(
        String.format("SELECT SUM(BALANCE) AS BALANCE FROM GOODS_ON_HAND WHERE BOND_CODE = '%s' and GOODS_DATE >= to_date('%s', 'yyyy-mm-dd') and SHELF_ID = '%s'", bondCode, goodDate, shelfId));
    } else {
      queryStringBuilder.append(String.format("SELECT SUM(BALANCE) AS BALANCE FROM GOODS_ON_HAND WHERE BOND_CODE = '%s' and GOODS_DATE >= to_date('%s', 'yyyy-mm-dd')", bondCode, goodDate));
    }

    HashMap<String, Object> result = new HashMap<>();
    result = (HashMap<String, Object>) PortfolioSit.goodsOrchestratorConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getSingleResult();

    if (result.get(BALANCE) == null) {
      return "0";
    }
    return result.get(BALANCE).toString();
  }

  public static String sumBalOnHandInDayByProdMerchent(String productCode, String goodDate, String merchant) {
    StringBuilder queryStringBuilder = new StringBuilder();
    if (merchant.equals(HB)) {
      merchant = HB_SHELF;
      queryStringBuilder.append(
        String.format("SELECT SUM(BALANCE) AS BALANCE FROM GOODS_ON_HAND WHERE PRODUCT_CODE = '%s' and GOODS_DATE = to_date('%s', 'yyyy-mm-dd') and SHELF_ID = '%s'", productCode, goodDate, merchant));
    }
    if (merchant.equals(TCS)) {
      merchant = SHELF;
      queryStringBuilder.append(
        String.format("SELECT SUM(BALANCE) AS BALANCE FROM GOODS_ON_HAND WHERE PRODUCT_CODE = '%s' and GOODS_DATE = to_date('%s', 'yyyy-mm-dd') and SHELF_ID = '%s'", productCode, goodDate, merchant));
    }
    if (merchant.equals("")) {
      queryStringBuilder.append(String.format("SELECT SUM(BALANCE) AS BALANCE FROM GOODS_ON_HAND WHERE PRODUCT_CODE = '%s' and GOODS_DATE = to_date('%s', 'yyyy-mm-dd')", productCode, goodDate));
    }

    HashMap<String, Object> result = new HashMap<>();
    result = (HashMap<String, Object>) PortfolioSit.goodsOrchestratorConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getSingleResult();

    if (result.get(BALANCE) == null) {
      return "0";
    }
    return result.get(BALANCE).toString();
  }

  public static String sumOutOnHandInDayByProdMerchent(String productCode, String goodDate, String merchant) {
    StringBuilder queryStringBuilder = new StringBuilder();
    if (merchant.equals(HB)) {
      merchant = HB_SHELF;
      queryStringBuilder.append(
        String.format("SELECT SUM(OUT) AS OUT FROM GOODS_ON_HAND WHERE PRODUCT_CODE = '%s' and GOODS_DATE = to_date('%s', 'yyyy-mm-dd') and SHELF_ID = '%s'", productCode, goodDate, merchant));
    }
    if (merchant.equals(TCS)) {
      merchant = SHELF;
      queryStringBuilder.append(
        String.format("SELECT SUM(OUT) AS OUT FROM GOODS_ON_HAND WHERE PRODUCT_CODE = '%s' and GOODS_DATE = to_date('%s', 'yyyy-mm-dd') and SHELF_ID = '%s'", productCode, goodDate, merchant));
    }
    if (merchant.equals("")) {
      queryStringBuilder.append(String.format("SELECT SUM(OUT) AS OUT FROM GOODS_ON_HAND WHERE PRODUCT_CODE = '%s' and GOODS_DATE = to_date('%s', 'yyyy-mm-dd')", productCode, goodDate));
    }

    HashMap<String, Object> result = new HashMap<>();
    result = (HashMap<String, Object>) PortfolioSit.goodsOrchestratorConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getSingleResult();

    if (result.get(OUT) == null) {
      return "0";
    }
    return result.get(OUT).toString();
  }
}
