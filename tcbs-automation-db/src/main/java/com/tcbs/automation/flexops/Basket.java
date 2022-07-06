package com.tcbs.automation.flexops;

import com.tcbs.automation.iris.IRis;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "BASKET")
@Log4j
public class Basket {
  private final static Logger logger = LoggerFactory.getLogger(Basket.class);
  private static final String BASKET_ID = "basketId";
  @Id
  @Column(name = "BASKETID")
  private String basketId;
  @Column(name = "BASKETNAME")
  private String basketName;
  @Column(name = "NOTES")
  private String notes;
  @Column(name = "PSTATUS")
  private String pStatus;
  @Column(name = "STATUS")
  private String status;

  @Step("Inset basket")
  public static Basket insertSecBasket(Basket basket) {
    try {
      FlexOps.flexOps.getSession().clear();
      FlexOps.flexOps.getSession().beginTransaction();
      FlexOps.flexOps.getSession().save(basket);
      FlexOps.flexOps.getSession().getTransaction().commit();
    } catch (Exception e) {
      logger.warn(e.getMessage());
    }
    return basket;
  }

//  @Step("Get all basket")
//  public static List<HashMap<String, Object>> getAllBasket() {
//
//    StringBuilder queryStringBuilder = new StringBuilder();
//    queryStringBuilder.append(" SELECT b.BASKETID, BASKETNAME, NOTES, SYMBOL, MRRATIORATE, MRPRICELOAN FROM Basket b ");
//    queryStringBuilder.append(" INNER JOIN SECBASKET s ON b.BASKETID = s.BASKETID  ");
//    try {
//      return FlexOps.flexOps.getSession().createNativeQuery(queryStringBuilder.toString())
//        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
//    } catch (Exception ex) {
//      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
//    }
//    return new ArrayList<>();
//  }

  @Step("Get basket")
  public static List<Basket> getBasketById(List<String> basketId, Integer pageIndex, Integer size) {

    StringBuilder queryStringBuilder = new StringBuilder("FROM Basket ");

    if (CollectionUtils.isNotEmpty(basketId)) {
      queryStringBuilder.append(" where basketId in (:basketId)");
    }

    try {
      Query<Basket> query = FlexOps.flexOps.getSession().createQuery(queryStringBuilder.toString(), Basket.class);
      if (CollectionUtils.isNotEmpty(basketId)) {
        query.setParameter(BASKET_ID, basketId);
      }
      if (pageIndex == null) {
        pageIndex = 0;
      }
      if (size == null) {
        size = 50;
      }
      query.setMaxResults(size);
      query.setFirstResult((pageIndex) * size);
      return query.getResultList();
    } catch (Exception ex) {
      logger.warn("[getBasketById] Exception!{}", ex);
      return Lists.newArrayList();
    }
  }

  @Step("Get All basket")
  public static List<Basket> getBasketById(List<String> basketId) {

    StringBuilder queryStringBuilder = new StringBuilder("FROM Basket where basketId in (:basketId)");
    try {
      Query<Basket> query = FlexOps.flexOps.getSession().createQuery(queryStringBuilder.toString(), Basket.class);
      query.setParameter(BASKET_ID, basketId);
      return query.getResultList();
    } catch (Exception ex) {
      logger.warn("Exception!{}", ex);
      return Lists.newArrayList();
    }
  }

  @Step("delete basket")
  public static void removeBasket(String basketId) {
    FlexOps.flexOps.getSession().clear();
    FlexOps.flexOps.getSession().beginTransaction();
    StringBuilder queryStringBuilder = new StringBuilder("delete from Basket where basketId = :basketId");
    try {
      Query query = FlexOps.flexOps.getSession().createQuery(queryStringBuilder.toString());
      query.setParameter(BASKET_ID, basketId);
      query.executeUpdate();
    } catch (Exception ex) {
      logger.warn("[removeBasket]Exception!{}", ex);
    } finally {
      FlexOps.flexOps.getSession().getTransaction().commit();
    }
  }


  @Step("delete basket")
  public static void removeBasketList(List<String> basketId) {
    FlexOps.flexOps.getSession().clear();
    FlexOps.flexOps.getSession().beginTransaction();
    StringBuilder queryStringBuilder = new StringBuilder("delete from Basket where basketId in (:basketId)");
    try {
      Query query = FlexOps.flexOps.getSession().createQuery(queryStringBuilder.toString());
      query.setParameter(BASKET_ID, basketId);
      query.executeUpdate();
    } catch (Exception ex) {
      logger.warn("[removeBasketList]Exception!", ex);
    } finally {
      FlexOps.flexOps.getSession().getTransaction().commit();
    }
  }


}
