package com.tcbs.automation.portfolio;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
@Table(name = "GOODS_ON_FUTURE")
public class GoodsOnFuture {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @NotNull
  @Column(name = "PRODUCT_CODE")
  private String productCode;
  @NotNull
  @Column(name = "SHELF_ID")
  private String shelfId;
  @NotNull
  @Column(name = "GOODS_DATE")
  private Date goodsDate;
  @NotNull
  @Column(name = "GOODS_IN")
  private Double goodsIn;
  @NotNull
  @Column(name = "GOODS_OUT")
  private Double goodsOut;
  @NotNull
  @Column(name = "BOND_CODE")
  private String bondCode;

  public static void insertShelfFutureInfo(String id, String shelfId, String productCode, String goodsDate, String goodsIn, String goodsOut, String bondCode) {
    Session session = PortfolioSit.goodsOrchestratorConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query query = session.createSQLQuery(
      String.format("INSERT INTO GOODS_ON_FUTURE (ID, SHELF_ID, PRODUCT_CODE, GOODS_DATE, GOODS_IN, GOODS_OUT, BOND_CODE) VALUES ('%s', '%s', '%s', TO_DATE('%s', 'yyyy-MM-dd'), '%s', '%s', '%s')", id, shelfId, productCode, goodsDate,
        goodsIn, goodsOut, bondCode));
    query.executeUpdate();
    trans.commit();
  }

  public static List<GoodsOnFuture> getByGoodsDate(String goodsDate) {
    Session session = PortfolioSit.goodsOrchestratorConnection.getSession();
    session.clear();
    Query<GoodsOnFuture> query = session.createQuery("from GoodsOnFuture gof where gof.goodsDate = TO_DATE(:goodsDate,'yyyy-MM-dd') order by id desc");
    query.setParameter("goodsDate", goodsDate);
    List<GoodsOnFuture> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  public static GoodsOnFuture getById(String id) {
    Session session = PortfolioSit.goodsOrchestratorConnection.getSession();
    session.clear();
    Query<GoodsOnFuture> query = session.createQuery("from GoodsOnFuture gof where gof.id =: id");
    query.setParameter("id", id);
    List<GoodsOnFuture> result = query.getResultList();

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
    Query<GoodsOnFuture> query = session.createQuery(
      "DELETE FROM GoodsOnFuture gof WHERE gof.id =:id"
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
    Query<GoodsOnFuture> query = session.createQuery(
      "DELETE FROM GoodsOnFuture gof WHERE gof.productCode =:productCode"
    );
    query.setParameter("productCode", productCode);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static String sumInFutureInDayByBond(String bondCode, String goodDate, String merchant) {
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
      queryStringBuilder.append(String.format("SELECT SUM(GOODS_IN) AS BALANCE FROM GOODS_ON_FUTURE WHERE BOND_CODE = '%s' and GOODS_DATE = to_date('%s', 'yyyy-mm-dd') and SHELF_ID = '%s'", bondCode, goodDate, shelfId));
    } else {
      queryStringBuilder.append(String.format("SELECT SUM(GOODS_IN) AS BALANCE FROM GOODS_ON_FUTURE WHERE BOND_CODE = '%s' and GOODS_DATE = to_date('%s', 'yyyy-mm-dd')", bondCode, goodDate));
    }

    HashMap<String, Object> result = new HashMap<>();
    result = (HashMap<String, Object>) PortfolioSit.goodsOrchestratorConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getSingleResult();

    if (result.get(BALANCE) == null) {
      return "0";
    }
    return result.get(BALANCE).toString();
  }

  public static String sumOutFutureInDayByBond(String bondCode, String goodDate, String merchant) {
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
      queryStringBuilder.append(String.format("SELECT SUM(GOODS_OUT) AS OUT FROM GOODS_ON_FUTURE WHERE BOND_CODE = '%s' and GOODS_DATE = to_date('%s', 'yyyy-mm-dd') and SHELF_ID = '%s'", bondCode, goodDate, shelfId));
    } else {
      queryStringBuilder.append(String.format("SELECT SUM(GOODS_OUT) AS OUT FROM GOODS_ON_FUTURE WHERE BOND_CODE = '%s' and GOODS_DATE = to_date('%s', 'yyyy-mm-dd')", bondCode, goodDate));
    }

    HashMap<String, Object> result = new HashMap<>();
    result = (HashMap<String, Object>) PortfolioSit.goodsOrchestratorConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getSingleResult();

    if (result.get(OUT) == null) {
      return "0";
    }
    return result.get(OUT).toString();
  }

  public static String sumInFutureInDayByProd(String productCode, String goodDate, String merchant) {
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
      queryStringBuilder.append(String.format("SELECT SUM(GOODS_IN) AS BALANCE FROM GOODS_ON_FUTURE WHERE PRODUCT_CODE = '%s' and GOODS_DATE = to_date('%s', 'yyyy-mm-dd') and SHELF_ID = '%s'", productCode, goodDate, shelfId));
    } else {
      queryStringBuilder.append(String.format("SELECT SUM(GOODS_IN) AS BALANCE FROM GOODS_ON_FUTURE WHERE PRODUCT_CODE = '%s' and GOODS_DATE = to_date('%s', 'yyyy-mm-dd')", productCode, goodDate));
    }

    HashMap<String, Object> result = new HashMap<>();
    result = (HashMap<String, Object>) PortfolioSit.goodsOrchestratorConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getSingleResult();

    if (result.get(BALANCE) == null) {
      return "0";
    }
    return result.get(BALANCE).toString();
  }

  public static String sumOutFutureInDayByProd(String productCode, String goodDate, String merchant) {
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
      queryStringBuilder.append(String.format("SELECT SUM(GOODS_OUT) AS OUT FROM GOODS_ON_FUTURE WHERE PRODUCT_CODE = '%s' and GOODS_DATE = to_date('%s', 'yyyy-mm-dd') and SHELF_ID = '%s'", productCode, goodDate, shelfId));
    } else {
      queryStringBuilder.append(String.format("SELECT SUM(GOODS_OUT) AS OUT FROM GOODS_ON_FUTURE WHERE PRODUCT_CODE = '%s' and GOODS_DATE = to_date('%s', 'yyyy-mm-dd')", productCode, goodDate));
    }

    HashMap<String, Object> result = new HashMap<>();
    result = (HashMap<String, Object>) PortfolioSit.goodsOrchestratorConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getSingleResult();

    if (result.get(OUT) == null) {
      return "0";
    }
    return result.get(OUT).toString();
  }

  public static GoodsOnFuture getByProdCode(String productCode, String goodsDate) {
    Session session = PortfolioSit.goodsOrchestratorConnection.getSession();
    session.clear();
    Query<GoodsOnFuture> query = session.createQuery("from GoodsOnFuture gof where gof.productCode = :productCode and gof.goodsDate = TO_DATE(:goodsDate,'yyyy-MM-dd') order by id desc");
    query.setParameter("productCode", productCode);
    query.setParameter("goodsDate", goodsDate);
    List<GoodsOnFuture> result = query.getResultList();

    if (result.size() > 0) {
      return result.get(0);
    } else {
      return null;
    }
  }
}
