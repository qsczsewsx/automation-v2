package com.tcbs.automation.fundstation.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "USER_ACCOUNT_AUTHORIZATION")
public class UserAccountAuthorization {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "ID")
  private int id;

  @Column(name = "USER_ACCOUNT_ID")
  private Integer userAccountId;

  @Column(name = "PORTFOLIO_ID")
  private Integer portfolioId;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  @Column(name = "ACCESS_TYPE")
  private String accessType;

  public static List<UserAccountAuthorization> getListPortfolioManagements(Integer userAccountId) {
    session.clear();
    Query<UserAccountAuthorization> query = session.createQuery("from UserAccountAuthorization where userAccountId =:userAccountId and status = 'ACTIVE'");
    query.setParameter("userAccountId", userAccountId);
    return query.getResultList();
  }

  public static List<UserAccountAuthorization> getListUserManagement(Object portfolioId) {
    session.clear();
    Query<UserAccountAuthorization> query = session.createQuery("from UserAccountAuthorization where portfolioId =:portfolioId and status = 'ACTIVE'");
    query.setParameter("portfolioId", portfolioId);
    return query.getResultList();
  }

  public static void deleteConfUserByUserId(List<Integer> userId) {
    if (!userId.isEmpty()) {
      Query<UserAccountAuthorization> query = session.createQuery("delete UserAccountAuthorization where userAccountId in :userId");
      query.setParameter("userId", userId);
      query.executeUpdate();
    }
  }
}
