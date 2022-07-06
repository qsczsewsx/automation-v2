package com.tcbs.automation.fundstation.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.fundstation.entity.UserAccountAuthorization.deleteConfUserByUserId;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "USER_ACCOUNT")
public class UserAccount {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "FULL_NAME")
  private String fullName;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  @Column(name = "ROLE")
  private String role;

  public static List<UserAccount> getAllUserAccount() {
    session.clear();
    Query<UserAccount> query = session.createQuery("from UserAccount");
    return query.getResultList();
  }

  public static UserAccount getUserByUserName(String userName) {
    Query<UserAccount> query = session.createQuery("from UserAccount where userName=:userName");
    query.setParameter("userName", userName);
    List<UserAccount> result = query.getResultList();
    if (result.size() > 0) {
      return result.get(0);
    } else {
      return new UserAccount();
    }
  }

  public static void deleteUserByUserName(List<String> listUser) {
    if (!listUser.isEmpty()) {
      List<Integer> listId = new ArrayList<>();
      for (String user : listUser) {
        listId.add(getUserByUserName(user).getId());
      }
      Query query = session.createQuery("delete UserAccount where id in :listId");
      query.setParameter("listId", listId);
      query.executeUpdate();
      deleteConfUserByUserId(listId);
    }
  }
}
