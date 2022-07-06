package com.tcbs.automation.coman;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "INTEREST")
public class Interest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "REFERENCE_RATE_ID")
  private String referenceRateId;
  @Column(name = "INTEREST")
  private String interest;
  @Column(name = "DEFINED_DATE")
  private String definedDate;
  @Column(name = "TYPE")
  private String type;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "CREATED_AT")
  private Timestamp createdAt;
  @Column(name = "UPDATED_AT")
  private Timestamp updatedAt;

  @Step
  public static Interest getInterestById(String id) {
    Query<Interest> query = Connection.comanDbConnection.getSession()
      .createQuery("from Interest a where a.id=:id and a.type = 'LSTC'", Interest.class);
    query.setParameter("id", id);

    return query.getSingleResult();
  }

  public static Interest getInterestLSGDById(String id) {
    Query<Interest> query = Connection.comanDbConnection.getSession()
      .createQuery("from Interest a where a.id=:id and a.type = 'LSGD'", Interest.class);
    query.setParameter("id", id);

    return query.getSingleResult();
  }

  public static List<Interest> getInterestLSGDByRefId(String referenceRateId) {
    Query<Interest> query = Connection.comanDbConnection.getSession()
      .createQuery("from Interest a where a.referenceRateId=:referenceRateId and a.type = 'LSGD' order by id desc ", Interest.class);
    query.setParameter("referenceRateId", referenceRateId);

    return query.getResultList();
  }

  public static void deleteInterestById(String interestId) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<Interest> query = session.createQuery(
      "DELETE FROM Interest a WHERE a.id = :interestId"
    );
    query.setParameter("interestId", interestId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void deleteInterestByRateId(String referenceRateId) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<Interest> query = session.createQuery(
      "DELETE FROM Interest a WHERE a.referenceRateId = :referenceRateId"
    );
    query.setParameter("referenceRateId", referenceRateId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void addLsgd(Interest data) throws ParseException {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    String sql = "INSERT INTO INTEREST(REFERENCE_RATE_ID, INTEREST, DEFINED_DATE, TYPE, STATUS, CREATED_AT, UPDATED_AT) VALUES (:a, :b, :c, :d, :e, :f, :g)";
    Date definedDate = new SimpleDateFormat("yyyy-MM-dd").parse(data.getDefinedDate());
    session.createSQLQuery(sql).setParameter("a", data.getReferenceRateId())
      .setParameter("a", data.getReferenceRateId())
      .setParameter("b", Float.parseFloat(data.getInterest()))
      .setParameter("c", new Timestamp(definedDate.getTime()))
      .setParameter("d", data.getType())
      .setParameter("e", data.getStatus())
      .setParameter("f", data.getCreatedAt())
      .setParameter("g", data.getUpdatedAt()).executeUpdate();
    session.getTransaction().commit();
  }

  public static List<Interest> getAllLSGD() {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<Interest> query = session.createQuery("FROM Interest WHERE type = 'LSGD' order by id desc ", Interest.class);
    return query.getResultList();
  }
}
