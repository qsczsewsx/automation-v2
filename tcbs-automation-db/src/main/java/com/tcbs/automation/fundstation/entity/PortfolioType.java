package com.tcbs.automation.fundstation.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "PORTFOLIO_TYPE")
public class PortfolioType {
  public static Session session;
  private static Map<Object, PortfolioType> mapAccountType = new HashMap<>();
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;
  @Column(name = "CODE")
  private String code;
  @Column(name = "NAME")
  private String name;
  @Column(name = "CREATED_TIMESTAMP")
  private Date createTimestamp;
  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  public static PortfolioType getAccountType(Object codeOrId) {
    if (mapAccountType == null || mapAccountType.size() == 0) {
      List<PortfolioType> list = getAllPortfolioType();
      for (PortfolioType portfolioType : list) {
        mapAccountType.put(portfolioType.getId(), portfolioType);
        mapAccountType.put(portfolioType.getCode(), portfolioType);
      }
    }
    if (mapAccountType.containsKey(codeOrId)) {
      return mapAccountType.get(codeOrId);
    } else {
      return new PortfolioType();
    }
  }

  public static List<PortfolioType> getAllPortfolioType() {
    Query<PortfolioType> query = session.createQuery("from PortfolioType");
    return query.getResultList();
  }
}
