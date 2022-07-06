package com.tcbs.automation.fundstation.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.fundstation.entity.TcAssets.sendSessionDBAssets;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "UNDERLYING")
public class Underlying {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private int id;

  @Column(name = "CODE")
  private String code;

  @Column(name = "NAME")
  private String name;

  @Column(name = "UNDERLYING_TYPE_CODE")
  private String underlyingTypeCode;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  @Column(name = "SUFFIX")
  private String suffix;

  @Column(name = "COMPANY_ID")
  private Integer companyId;

  @Column(name = "FIRST_ISSUE_DATE")
  private Date firstIssueDate;

  @Column(name = "VALUE")
  private Double value;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "STATUS")
  private String status;

  public static List<Underlying> getBondTempActive() {
    Query<Underlying> query = session.createQuery("from Underlying where status = 'ACTIVE' order by firstIssueDate desc ");
    return query.getResultList();
  }

  public static Underlying getUnderlyingWithCode(String code) {
    session.clear();
    Query<Underlying> query = session.createQuery("from Underlying where code =: code");
    query.setParameter("code", code);
    List<Underlying> list = query.getResultList();
    if (list.size() != 0) {
      return list.get(0);
    } else {
      return null;
    }
  }

  public static Underlying getUnderlyingWithCodeAndIssueDate(String code, Date issueDate) {
    session.clear();
    Query<Underlying> query = session.createQuery("from Underlying where code =: code and firstIssueDate =:issueDate");
    query.setParameter("code", code);
    query.setParameter("issueDate", issueDate);
    List<Underlying> list = query.getResultList();
    if (list.size() != 0) {
      return list.get(0);
    } else {
      return null;
    }
  }

  public static Underlying getUnderlyingWithCodeAndIssueDate(String code, Date issueDate, String status) {
    session.clear();
    Query<Underlying> query = session.createQuery("from Underlying where code =: code and firstIssueDate =:issueDate and status=:status");
    query.setParameter("code", code);
    query.setParameter("issueDate", issueDate);
    query.setParameter("status", status);
    List<Underlying> list = query.getResultList();
    if (list.size() != 0) {
      return list.get(0);
    } else {
      return null;
    }
  }

  public static void deleteBondTempWithCode(String code) {
    Session session2 = sendSessionDBAssets();
    Query<TSPlan> query = session2.createQuery("delete Underlying where code =: code ");
    query.setParameter("code", code);
    int row = query.executeUpdate();
    session2.getTransaction().commit();
  }

  public static void insertIntoUnderlying(String code, String name, Integer companyId, Date issueDate, Integer value, String status, String underlyingTypeCode, String description) {
    Session session2 = sendSessionDBAssets();
    Underlying underlying = new Underlying();
    underlying.setCode(code);
    underlying.setName(name);
    underlying.setCompanyId(companyId);
    underlying.setValue(value.doubleValue());
    underlying.setStatus(status);
    underlying.setFirstIssueDate(issueDate);
    underlying.setUnderlyingTypeCode(underlyingTypeCode);
    underlying.setDescription(description);
    session2.save(underlying);
    session2.getTransaction().commit();
  }

  public static List<Underlying> getBondTempNotDeletedLikeCode(String code, String code2) {
    Query<Underlying> query = session.createQuery("from Underlying where code like :code or code like :code2 and not status = 'DELETED' order by firstIssueDate asc ");
    query.setParameter("code", "%" + code + "%");
    query.setParameter("code2", "%" + code2 + "%");
    return query.getResultList();
  }

  public static List<Underlying> getBondTempNotDeleted() {
    Query<Underlying> query = session.createQuery("from Underlying where not status = 'DELETED' order by firstIssueDate asc ");
    return query.getResultList();
  }

  public static Underlying getUnderlyingWithId(Integer id) {
    session.clear();
    Query<Underlying> query = session.createQuery("from Underlying where id =:id");
    query.setParameter("id", id);
    List<Underlying> list = query.getResultList();
    if (list.size() != 0) {
      return list.get(0);
    } else {
      return null;
    }
  }
}