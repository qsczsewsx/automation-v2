package com.tcbs.automation.portfolio;

import lombok.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TCPRICE_RECENTLY_SEARCH")
public class TcPriceRecentlySearch {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TCPRICE_RECENTLY_SEARCH_SEQ")
  @SequenceGenerator(sequenceName = "TCPRICE_RECENTLY_SEARCH_SEQ", allocationSize = 1, name = "TCPRICE_RECENTLY_SEARCH_SEQ")
  private Integer id;

  @Column(name = "TCBS_ID")
  private String tcbsId;

  @Column(name = "TICKERS")
  private String tickers;

  @Column(name = "CREATED_DATE")
  @Temporal(TemporalType.DATE)
  private Date createDate;

  @Column(name = "UPDATED_DATE")
  @Temporal(TemporalType.DATE)
  private Date updatedDate;

  public static void deleteByTcbsID(String tcbsId) {
    PortfolioSit.porConnection.getSession().clear();
    PortfolioSit.porConnection.getSession().beginTransaction();

    Query query = PortfolioSit.porConnection.getSession().createQuery(
      "delete from TcPriceRecentlySearch c where c.tcbsId = :tcbsId");

    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();

    PortfolioSit.porConnection.getSession().getTransaction().commit();
  }

  public static List<String> getByTcbsID(String tcbsId) {
    PortfolioSit.porConnection.getSession().clear();
    List<String> tickers = new ArrayList<>();
    Query<TcPriceRecentlySearch> query = PortfolioSit.porConnection.getSession().createQuery(
      "select c from TcPriceRecentlySearch c where c.tcbsId = :tcbsId",
      TcPriceRecentlySearch.class
    );
    query.setParameter("tcbsId", tcbsId);
    TcPriceRecentlySearch res = query.getSingleResult();
    if (res != null && res.tickers != null && !res.tickers.isEmpty()) {
      tickers = new ArrayList<>(Arrays.asList(res.tickers.split(",")));
    }

    return tickers;
  }

  public static Integer save(TcPriceRecentlySearch contract) {
    Session session = PortfolioSit.porConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Integer generatedId = (Integer) session.save(contract);
    session.getTransaction().commit();

    return generatedId;
  }
}
