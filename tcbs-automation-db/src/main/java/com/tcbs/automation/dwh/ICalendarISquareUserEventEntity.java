package com.tcbs.automation.dwh;

import com.tcbs.automation.config.idatatcanalysis.IData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "icalendar_isquare_user_event")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ICalendarISquareUserEventEntity {
  @Id
  @Column(name = "id")
  public Long id;

  @Column(name = "user_id")
  public Long userId;

  @Column(name = "company_ga_id")
  public Long companyGaId;

  @Column(name = "event_desc")
  public String eventDesc;

  @Column(name = "event_date")
  public Date eventDate;

  @Column(name = "created_date")
  public Date createdDate;

  @Column(name = "last_updated_date")
  public Date lastUpdatedDate;

  public static List<ICalendarISquareUserEventEntity> getFromDateToDate(Date fromDate, Date toDate,
                                                                        int page, int size,
                                                                        Long userId, Long companyGaId) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();

    try {
      Query<ICalendarISquareUserEventEntity> query = session.createQuery(
        "FROM ICalendarISquareUserEventEntity " +
          "WHERE (eventDate BETWEEN :fromDate AND :toDate)" +
          "AND (userId = :userId)" +
          "AND (companyGaId = :companyGaId)" +
          "ORDER BY eventDate"
      );
      query.setParameter("fromDate", fromDate);
      query.setParameter("toDate", toDate);
      query.setParameter("userId", userId);
      query.setParameter("companyGaId", companyGaId);
      query.setFirstResult(page * size);
      query.setMaxResults(size);

      List<ICalendarISquareUserEventEntity> result = query.getResultList();
      IData.idataDbConnection.closeSession();
      return result;
    } catch (Exception e) {
      IData.idataDbConnection.closeSession();
      throw e;
    }
  }

  public static long countFromDateToDate(Date fromDate, Date toDate, Long userId, Long companyGaId) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();

    try {
      Query<Long> query = session.createQuery(
        "SELECT COUNT(*) FROM ICalendarISquareUserEventEntity " +
          "WHERE (eventDate BETWEEN :fromDate AND :toDate)" +
          "AND (userId = :userId)" +
          "AND (companyGaId = :companyGaId)"
      );
      query.setParameter("fromDate", fromDate);
      query.setParameter("toDate", toDate);
      query.setParameter("userId", userId);
      query.setParameter("companyGaId", companyGaId);

      Long result = query.uniqueResult();
      IData.idataDbConnection.closeSession();
      return result;
    } catch (Exception e) {
      IData.idataDbConnection.closeSession();
      throw e;
    }
  }

  public static ICalendarISquareUserEventEntity getById(Long id) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();

    Query<ICalendarISquareUserEventEntity> query = session.createQuery(
      "FROM ICalendarISquareUserEventEntity WHERE id = :id"
    );
    query.setParameter("id", id);

    try {
      ICalendarISquareUserEventEntity result = query.getSingleResult();
      IData.idataDbConnection.closeSession();
      return result;
    } catch (NoResultException e) {
      IData.idataDbConnection.closeSession();
      return null;
    }
  }

  public static ICalendarISquareUserEventEntity getTopOne() {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();

    Query<ICalendarISquareUserEventEntity> query = session.createQuery(
      "FROM ICalendarISquareUserEventEntity"
    );
    query.setMaxResults(1);

    ICalendarISquareUserEventEntity result = query.getSingleResult();
    IData.idataDbConnection.closeSession();
    return result;
  }

  public static void executeQuery(String sql) {
    Session session = Dwh.dwhDbConnection.getSession();
    session.clear();
    String[] sqlRows = sql.split("\n");

    try {
      Transaction transaction = session.getTransaction();
      if(transaction != null && transaction.isActive() && transaction.getStatus().canRollback()){
        transaction.rollback();
      }
      transaction = session.beginTransaction();
      for (String s : sqlRows) {
        session.createNativeQuery(s).executeUpdate();
      }
      transaction.commit();
    } catch (Exception e) {
      throw e;
    }finally {
      IData.idataDbConnection.closeSession();
    }
  }
}
