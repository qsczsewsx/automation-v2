package com.tcbs.automation.fundstation.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
@Table(name = "COMPANY")
public class Company {
  public static Session session;
  private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private int id;

  @Column(name = "COMPANY_GROUP_ID")
  private Integer companyGroupId;

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

  @Column(name = "NAME_EN")
  private String nameEn;

  @Column(name = "INDUSTRY_ID")
  private Integer industryId;

  @Column(name = "TOTAL_VALUE")
  private Double totalValue;

  public static List<Company> getAllCompanyFromDb() {
    return session.createQuery("from Company  where companyGroupId is not null order by code asc").getResultList();
  }

  public static List<Company> getAllCompanyWhichHasGroupFromDb() {
    return session.createQuery("from Company where companyGroupId is not null order by code asc").getResultList();
  }

  public static Company getCompanyWithId(Integer id) {
    session.clear();
    Query<Company> query = session.createQuery("from Company where id =:id");
    query.setParameter("id", id);
    List<Company> result = query.getResultList();
    if (result.size() != 0) {
      return result.get(0);
    } else {
      return null;
    }
  }

  public static void insertIntoCompany(String code, String name, String status, String nameEn, Integer companyGroupId) {
    Session session2 = sendSessionDBAssets();
    Company company = new Company();
    company.setCode(code);
    company.setName(name);
    company.setStatus(status);
    company.setNameEn(nameEn);
    company.setCompanyGroupId(companyGroupId);
    session2.save(company);
    session2.getTransaction().commit();
  }

  public static void deleteCompanyWithCode(String code) {
    Session session2 = sendSessionDBAssets();
    Query<CompanyGroup> query = session2.createQuery("delete Company where code =:code");
    query.setParameter("code", code);
    query.executeUpdate();
    session2.getTransaction().commit();

  }

  public static Company getCompanyWithCode(String code) {
    session.clear();
    Query<Company> query = session.createQuery("from Company where code =:code");
    query.setParameter("code", code);
    List<Company> result = query.getResultList();
    if (result.size() != 0) {
      return result.get(0);
    } else {
      return null;
    }
  }

}
