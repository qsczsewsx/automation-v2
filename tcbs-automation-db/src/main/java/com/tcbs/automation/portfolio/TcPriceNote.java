package com.tcbs.automation.portfolio;

import org.hibernate.Session;
import org.hibernate.query.Query;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TCPRICE_NOTE")
public class TcPriceNote {
  private static final long serialVersionUID = 1L;
  private static String tcbsIdKey = "tcbsId";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TCPRICE_NOTE_SEQ")
  @SequenceGenerator(sequenceName = "TCPRICE_NOTE_SEQ", allocationSize = 1, name = "TCPRICE_NOTE_SEQ")
  private Integer id;

  @Column(name = "TCBS_ID")
  private String tcbsId;

  @Column(name = "TICKER")
  private String ticker;

  @Column(name = "CONTENT")
  private String content;

  @Column(name = "CREATED_DATE")
  @Temporal(TemporalType.DATE)
  private Date createDate;

  @Column(name = "UPDATED_DATE")
  @Temporal(TemporalType.DATE)
  private Date updatedDate;

  public static void deleteByTickerAndTcbsID(String ticker, String tcbsId) {
    PortfolioSit.porConnection.getSession().clear();
    PortfolioSit.porConnection.getSession().beginTransaction();

    Query query = PortfolioSit.porConnection.getSession().createQuery(
      "delete from TcPriceNote c where c.ticker=:ticker and c.tcbsId = :tcbsId");

    query.setParameter("ticker", ticker);
    query.setParameter(tcbsIdKey, tcbsId);
    query.executeUpdate();

    PortfolioSit.porConnection.getSession().getTransaction().commit();
  }

  public static void deleteByTcbsID(String tcbsId) {
    PortfolioSit.porConnection.getSession().clear();
    PortfolioSit.porConnection.getSession().beginTransaction();

    Query query = PortfolioSit.porConnection.getSession().createQuery(
      "delete from TcPriceNote c where c.tcbsId = :tcbsId");

    query.setParameter(tcbsIdKey, tcbsId);
    query.executeUpdate();

    PortfolioSit.porConnection.getSession().getTransaction().commit();
  }

  public static List<TcPriceNote> getByTickerAndTcbsID(String ticker, String tcbsId) {
    PortfolioSit.porConnection.getSession().clear();

    Query<TcPriceNote> query = PortfolioSit.porConnection.getSession().createQuery(
      "select c from TcPriceNote c where c.ticker=:ticker and c.tcbsId = :tcbsId",
      TcPriceNote.class
    );
    query.setParameter("ticker", ticker);
    query.setParameter(tcbsIdKey, tcbsId);
    List<TcPriceNote> res = query.getResultList();
    return res;
  }

  public static List<TcPriceNote> getByTcbsID(String tcbsId) {
    PortfolioSit.porConnection.getSession().clear();

    Query<TcPriceNote> query = PortfolioSit.porConnection.getSession().createQuery(
      "select c from TcPriceNote c where c.tcbsId = :tcbsId",
      TcPriceNote.class
    );
    query.setParameter(tcbsIdKey, tcbsId);
    List<TcPriceNote> res = query.getResultList();
    return res;
  }

  public static Integer save(TcPriceNote contract) {
    Session session = PortfolioSit.porConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Integer generatedId = (Integer) session.save(contract);
    session.getTransaction().commit();

    return generatedId;
  }

  public static List<TcPriceNote> saveAll(List<TcPriceNote> tcPriceNotes) {
    Session session = PortfolioSit.porConnection.getSession();
    session.clear();

    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    tcPriceNotes.forEach(tcPriceNote -> tcPriceNote.setId((Integer) session.save(tcPriceNote)));

    session.getTransaction().commit();
    return tcPriceNotes;
  }
}
