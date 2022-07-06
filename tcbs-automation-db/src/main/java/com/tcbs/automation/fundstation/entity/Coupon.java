package com.tcbs.automation.fundstation.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "COUPON")
public class Coupon {
  public static Session session;
  public static Map<String, List<Coupon>> mapCoupon = new HashMap<>();
  private static Map<Object, Map<String, Coupon>> mapCouponByDate = new HashMap<>();
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private int id;
  @Column(name = "TICKER_ROOT")
  private String tickerRoot;
  @Column(name = "FROM_DATE")
  private Date fromDate;
  @Column(name = "TO_DATE")
  private Date toDate;
  @Column(name = "EX_DIVIDEND_DATE")
  private Date exDividendDate;
  @Column(name = "COUPON_RATE")
  private Double couponRate;
  @Column(name = "PURCHASING_TIMESTAMP")
  private Date purchasingTimestamp;
  @Column(name = "CREATED_TIMESTAMP")
  private Date createTimestamp;
  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;
  @Column(name = "STATUS")
  private String status;

  public static List<Coupon> getCouponOfTicker(String tickerRoot) {
    if (mapCoupon == null || mapCoupon.size() == 0) {
      mapCoupon = getAllCoupon();
    }
    return mapCoupon.get(tickerRoot);
  }

  public static Map<String, List<Coupon>> getListCouponByRageDate(Date fromDate, Date toDate, List<String> listTicker) {
    Query<Coupon> query = session.createQuery("from Coupon where status = 'ACTIVE' and toDate >=:fromDate and toDate <=:toDate and tickerRoot in :listTicker  order by toDate asc ");
    query.setParameter("fromDate", fromDate);
    query.setParameter("toDate", toDate);
    query.setParameter("listTicker", listTicker);
    List<Coupon> listCoupon = query.getResultList();

    Map<String, List<Coupon>> mapCoupon = new HashMap<>();
    for (Coupon coupon : listCoupon) {
      List<Coupon> list = new ArrayList<>();
      if (mapCoupon.containsKey(coupon.getTickerRoot())) {
        list = mapCoupon.get(coupon.getTickerRoot());
      }
      list.add(coupon);
      mapCoupon.put(coupon.getTickerRoot(), list);
    }
    return mapCoupon;
  }

  public static Map<String, Coupon> getListCouponByDate(Date couponGenDate) {
    if (!mapCouponByDate.containsKey(couponGenDate)) {
      Query<Coupon> query = session.createQuery("from Coupon where status = 'ACTIVE' and (case when exDividendDate is null then toDate else exDividendDate end) =:couponGenDate");
      query.setParameter("couponGenDate", couponGenDate);
      List<Coupon> listCoupon = query.getResultList();
      mapCouponByDate.put(couponGenDate, convertListToMap(listCoupon));
    }
    return mapCouponByDate.get(couponGenDate);
  }

  static Map<String, Coupon> convertListToMap(List<Coupon> listCoupon) {
    Map<String, Coupon> mapCoupon = new HashMap<>();
    for (Coupon coupon : listCoupon) {
      mapCoupon.put(coupon.getTickerRoot(), coupon);
    }
    return mapCoupon;
  }

  public static Map<String, List<Coupon>> getAllCoupon() {
    Map<String, List<Coupon>> map = new HashMap<>();
    Query<Coupon> query = session.createQuery("from Coupon where status = 'ACTIVE' order by toDate asc ");

    List<Coupon> result = query.getResultList();
    for (Coupon item : result) {
      if (map.containsKey(item.getTickerRoot())) {
        List<Coupon> list = map.get(item.getTickerRoot());
        list.add(item);
        map.put(item.getTickerRoot(), list);
      } else {
        List<Coupon> list = new ArrayList<>();
        list.add(item);
        map.put(item.getTickerRoot(), list);
      }
    }
    return map;
  }
}
