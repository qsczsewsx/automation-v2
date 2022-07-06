package com.tcbs.automation.fundstation.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@Entity
@Table(name = "PORTFOLIO")
public class Portfolio {
  public static Session session;
  private static Map<String, Portfolio> mapFundAccount = new HashMap<>();
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;
  @Column(name = "CODE")
  private String code;
  @Column(name = "NAME")
  private String name;
  @Column(name = "PORTFOLIO_TYPE_CODE")
  private String portfolioTypeCode;
  @Column(name = "PORTFOLIO_CLASS_ID")
  private Integer portfolioClassId;
  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;
  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "PERMISSION")
  private String permission;
  @Column(name = "NAME_EN")
  private String nameEn;
  @Column(name = "OWNER_ID")
  private Integer ownerId;

  public static List<Portfolio> getAllPortfolioByType(List<String> portfolioType) {
    session.clear();
    Query<Portfolio> query = session.createQuery("from Portfolio where portfolioTypeCode in :portfolioType order by code asc");
    query.setParameter("portfolioType", portfolioType);
    return query.getResultList();
  }

  public static List<Portfolio> allPortfolio() {
    session.clear();
    Query<Portfolio> query = session.createQuery("from Portfolio where code not like ('0001%')");
    return query.getResultList();
  }

  public static Portfolio getFundInfo(String fundCode) {
    if (mapFundAccount == null || mapFundAccount.size() == 0 || fundCode == null) {
      List<Portfolio> listFund = allPortfolio();
      for (Portfolio acc : listFund) {
        mapFundAccount.put(acc.getCode(), acc);
      }
    }
    return mapFundAccount.get(fundCode);
  }

  public static Portfolio getPortfolioByCode(String code) {
    session.clear();
    Query<Portfolio> query = session.createQuery("from Portfolio where code =:code");
    query.setParameter("code", code);
    List<Portfolio> list = query.getResultList();
    if (list.size() > 0) {
      return query.getResultList().get(0);
    } else {
      return new Portfolio();
    }
  }

  public static Portfolio getPortfolioById(Integer id) {
    session.clear();
    Query<Portfolio> query = session.createQuery("from Portfolio where id =:id");
    query.setParameter("id", id);
    List<Portfolio> list = query.getResultList();
    if (list.size() > 0) {
      return query.getResultList().get(0);
    } else {
      return new Portfolio();
    }
  }

  public static List<Portfolio> getListPortfolioWithListType(List<String> listType) {
    Query<Portfolio> query = session.createQuery("from Portfolio where portfolioTypeCode in :listType");
    query.setParameter("listType", listType);
    return query.getResultList();
  }

  public static void deleteListPortfolioByCode(List<String> listCode) {
    if (!listCode.isEmpty()) {
      Query<Portfolio> query = session.createQuery("delete Portfolio where code in :listCode");
      query.setParameter("listCode", listCode);
      query.executeUpdate();
    }
  }

  public static void deleteListPortfolioById(List<Integer> listId) {
    if (!listId.isEmpty()) {
      Query<Portfolio> query = session.createQuery("delete Portfolio where id in :listId");
      query.setParameter("listId", listId);
      query.executeUpdate();
    }
  }
}
