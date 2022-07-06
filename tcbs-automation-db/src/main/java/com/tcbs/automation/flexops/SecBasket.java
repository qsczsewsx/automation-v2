package com.tcbs.automation.flexops;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "SECBASKET")
public class SecBasket {
  private static final Logger logger = LoggerFactory.getLogger(SecBasket.class);
  private static final String BASKET_ID = "basketId";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "AUTOID")
  private Long autoId;
  @Column(name = "BASKETID")
  private String basketId;
  @Column(name = "SYMBOL")
  private String symbol;
  @Column(name = "MRRATIORATE")
  private Double mrRatioRate;
  @Column(name = "MRRATIOLOAN")
  private Double mrRatioLoan;
  @Column(name = "MRPRICERATE")
  private Double mrPriceRate;
  @Column(name = "MRPRICELOAN")
  private Double mrPriceLoan;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "IMPORTDT")
  private String importDt;
  @Column(name = "MAKERID")
  private String makerId;
  @Column(name = "STATUS")
  private String status;

  @Step("Inset sec basket")
  public static SecBasket insertSecBasket(SecBasket secBasket) {
    FlexOps.flexOps.getSession().clear();
    FlexOps.flexOps.getSession().beginTransaction();
    FlexOps.flexOps.getSession().save(secBasket);
    FlexOps.flexOps.getSession().getTransaction().commit();
    return secBasket;
  }

  @Step("Get sec basket")
  public static List<SecBasket> getSecBasketById(List<String> basketId) {
    FlexOps.flexOps.getSession().clear();
    String sql = "select * from SECBASKET where BASKETID in (:basketId)";
    try {
      Query<SecBasket> query = FlexOps.flexOps.getSession().createNativeQuery(sql, SecBasket.class);
      query.setParameter(BASKET_ID, basketId);
      return query.getResultList();
    } catch (Exception ex) {
      logger.warn("[getSecBasketById]Exception!{}", ex);
      return Lists.newArrayList();
    }
  }


  @Step("Get sec basket")
  public static List<SecBasket> getSecBasketById(List<String> basketId, Integer pageIndex, Integer size) {
    FlexOps.flexOps.getSession().clear();
    StringBuilder queryStringBuilder = new StringBuilder("FROM SecBasket ");

    if (CollectionUtils.isNotEmpty(basketId)) {
      queryStringBuilder.append(" where basketId in (:basketId)");
    }

    try {
      Query<SecBasket> query = FlexOps.flexOps.getSession().createQuery(queryStringBuilder.toString(), SecBasket.class);
      if (CollectionUtils.isNotEmpty(basketId)) {
        query.setParameter(BASKET_ID, basketId);
      }
      if (pageIndex == null) {
        pageIndex = 0;
      }
      if (size == null) {
        size = 20;
      }
      query.setMaxResults(size);
      query.setFirstResult((pageIndex) * size);
      return query.getResultList();
    } catch (Exception ex) {
      logger.warn("[getSecBasketById]Exception!{}", ex);
      return Lists.newArrayList();
    }
  }


  @Step("delete sec basket")
  public static void removeSecBasket(List<String> basketId) {
    FlexOps.flexOps.getSession().clear();
    FlexOps.flexOps.getSession().beginTransaction();
    StringBuilder queryStringBuilder = new StringBuilder("delete from SecBasket where basketId in (:basketId)");
    try {
      Query query = FlexOps.flexOps.getSession().createQuery(queryStringBuilder.toString());
      query.setParameter(BASKET_ID, basketId);
      query.executeUpdate();
    } catch (Exception ex) {
      logger.warn("[removeSecBasket]Exception!{}", ex);
    } finally {
      FlexOps.flexOps.getSession().getTransaction().commit();
    }
  }

}
