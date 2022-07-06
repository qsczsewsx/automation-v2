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
@Table(name = "COMPANY_GROUP")
public class CompanyGroup {
  public static Session session;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private int id;

  @Column(name = "CODE")
  private String code;

  @Column(name = "NAME")
  private String name;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  public static List<CompanyGroup> getAllGroupFromDb() {
    return session.createQuery("from CompanyGroup").getResultList();
  }

  public static CompanyGroup getCompanyGroupWithId(Integer id) {
    session.clear();
    Query<CompanyGroup> query = session.createQuery("from CompanyGroup where id =:id");
    query.setParameter("id", id);
    List<CompanyGroup> result = query.getResultList();
    if (result.size() != 0) {
      return result.get(0);
    } else {
      return null;
    }
  }

  public static void insertIntoCompanyGroup(String code, String name, String status) {
    Session session2 = sendSessionDBAssets();
    CompanyGroup companyGroup = new CompanyGroup();
    companyGroup.setCode(code);
    companyGroup.setName(name);
    companyGroup.setStatus(status);
    session2.save(companyGroup);
    session2.getTransaction().commit();
  }

  public static void deleteCompanyGroupWithCode(String code) {
    Session session2 = sendSessionDBAssets();
    Query<CompanyGroup> query = session2.createQuery("delete CompanyGroup where code =:code");
    query.setParameter("code", code);
    query.executeUpdate();
    session2.getTransaction().commit();
  }

  public static CompanyGroup getCompanyGroupWithCode(String code) {
    session.clear();
    Query<CompanyGroup> query = session.createQuery("from CompanyGroup where code =:code");
    query.setParameter("code", code);
    List<CompanyGroup> result = query.getResultList();
    if (result.size() != 0) {
      return result.get(0);
    } else {
      return null;
    }
  }
}
