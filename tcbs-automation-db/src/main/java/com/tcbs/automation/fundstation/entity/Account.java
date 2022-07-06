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
@Table(name = "ACCOUNT")
public class Account {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "FULL_NAME")
  private String fullName;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "ACCOUNT_CLASS_ID")
  private Integer accountClassId;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  public static List<Account> getAllAccount() {
    session.clear();
    Query<Account> query = session.createQuery("from Account where  userName not like ('105C%')");
    return query.getResultList();
  }

  public static Account getAccountByUserName(Integer id, String userName) {
    session.clear();
    Query<Account> query = session.createQuery("from Account where userName=:userName or id =:id");
    query.setParameter("userName", userName);
    query.setParameter("id", id);
    List<Account> result = query.getResultList();
    if (result.size() > 0) {
      return result.get(0);
    } else {
      return new Account();
    }
  }

  public static void deleteAccountTest(List<String> listAccount) {
    if (!listAccount.isEmpty()) {
      Query<Account> query = session.createQuery("delete Account where userName in :listAccount");
      query.setParameter("listAccount", listAccount);
      query.executeUpdate();
    }
  }
}
