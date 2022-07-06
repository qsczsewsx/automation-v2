package com.tcbs.automation.ligo;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "watchlist")
public class Watchlist {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;
  @NotNull
  @Column(name = "tcbs_id")
  private String tcbsId;
  @NotNull
  @Column(name = "name")
  private String name;
  @NotNull
  @Column(name = "filters")
  private String filters;
  @Column(name = "num_of_ticker")
  private Long numOfTicker;
  @NotNull
  @Column(name = "created_on")
  private Long createdOn;
  @Column(name = "modified_on")
  private Long modifiedOn;
  @Column(name = "poseidon_id")
  private Long poseidonId;

  public static void deleteByName(String watchlistName) {
    Session session = LigoRepository.ligoDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createQuery(
      "DELETE FROM Watchlist ib WHERE ib.name = :watchlistName"
    );
    query.setParameter("watchlistName", watchlistName);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static List<Watchlist> getListByTcbsId(String tcbsId) {
    Session session = LigoRepository.ligoDbConnection.getSession();
    session.clear();
    Query<Watchlist> query = session.createQuery("from Watchlist w where w.tcbsId =: tcbsId");
    query.setParameter("tcbsId", tcbsId);
    List<Watchlist> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }

  public static void deleteById(Long id) {
    Session session = LigoRepository.ligoDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createQuery(
      "DELETE FROM Watchlist ib WHERE ib.id = :id"
    );
    query.setParameter("id", id);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static Watchlist getWatchListById(Long id) {
    Session session = LigoRepository.ligoDbConnection.getSession();
    session.clear();
    Query<Watchlist> query = session.createQuery("from Watchlist w where w.id = :id");
    query.setParameter("id", id);
    return query.getSingleResult();
  }
}
