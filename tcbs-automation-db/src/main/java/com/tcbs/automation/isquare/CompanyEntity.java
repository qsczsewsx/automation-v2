package com.tcbs.automation.isquare;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "COMPANY")
public class CompanyEntity implements Serializable {

  public static final long DEFAULT_COMPANY_ID = 8447L;
  public static final long DEFAULT_GA_ID = 10000016510L;
  private static final String ACTIVE = "ACTIVE";

  @Id
  @Column(name = "ID")
  private Long id;
  @Column(name = "GA_ID")
  private Long gaId;
  @Column(name = "NAME")
  private String name;
  @Column(name = "MST_STC")
  private String mstStc;
  @Column(name = "EMAIL")
  private String email;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "IS_VIETNAMESE_COMPANY")
  private String isVietnameseCompany;
  @Column(name = "IS_MST")
  private String isMst;
  @Column(name = "CREATED_DATETIME")
  private Date createdDatetime;
  @Column(name = "LAST_MODIFIED_DATE")
  private Date lastModifiedDate;
  @Column(name = "TICKER")
  private String ticker;

  public static Optional<CompanyEntity> getCompanyById(Long companyId) {
    Session session = ISquare.iSquareDbConnection.getSession();
    session.clear();

    Query<CompanyEntity> query = session.createQuery("FROM CompanyEntity WHERE id = :companyId");
    query.setParameter("companyId", companyId);

    try {
      CompanyEntity result = query.getSingleResult();
      ISquare.iSquareDbConnection.closeSession();
      return Optional.of(result);
    } catch (NoResultException e) {
      ISquare.iSquareDbConnection.closeSession();
      return Optional.empty();
    }
  }

  public static void insertDefaultCompany() {
    try {
      Optional<CompanyEntity> optional = getCompanyById(DEFAULT_COMPANY_ID);
      CompanyEntity companyEntity;

      Session session = ISquare.iSquareDbConnection.getSession();
      session.clear();

      Transaction transaction = session.beginTransaction();

      if (optional.isPresent()) {
        companyEntity = optional.get();
        if (!ACTIVE.equals(companyEntity.getStatus())) {
          companyEntity.setStatus(ACTIVE);
        }
      } else {
        companyEntity = CompanyEntity.builder()
          .id(DEFAULT_COMPANY_ID)
          .gaId(DEFAULT_GA_ID)
          .name("autotest")
          .email("autotest@gmail.com")
          .mstStc("mstautotest")
          .status(ACTIVE)
          .build();
      }

      session.saveOrUpdate(companyEntity);
      transaction.commit();

    } catch (Exception e) {
      throw e;
    } finally {
      ISquare.iSquareDbConnection.closeSession();
    }
  }
}
