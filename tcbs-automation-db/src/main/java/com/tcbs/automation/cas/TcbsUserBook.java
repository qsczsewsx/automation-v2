package com.tcbs.automation.cas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.cas.CAS.casConnection;

@Entity
@Table(name = "TCBS_USER_BOOK")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TcbsUserBook {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @NotNull
  @Column(name = "USER_ID")
  private BigDecimal userId;
  @Column(name = "NO_NUMBER")
  private String noNumber;
  @Column(name = "STATUS")
  private BigDecimal status;
  @Column(name = "RECEIVED_DATE")
  private Date receivedDate;
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;
  @Column(name = "MANAGER_GROUP")
  private String managerGroup;
  @Column(name = "MANAGER_USER")
  private String managerUser;
  @Column(name = "NOTE")
  private String note;
  @Column(name = "TYPE")
  private String type;
  @Column(name = "NEW_INFO")
  private String newInfo;
  @Column(name = "CREATED_DATE")
  private Date createdDate;
  @Column(name = "FILE_PATH")
  private String filePath;
  @Column(name = "OLD_INFO")
  private String oldInfo;

  @Step
  public static TcbsUserBook getById(String id) {
    CAS.casConnection.getSession().clear();
    Query<TcbsUserBook> query = CAS.casConnection.getSession().createQuery(
      "from TcbsUserBook a where a.id=:id", TcbsUserBook.class);
    query.setParameter("id", new BigDecimal(id));
    return query.getSingleResult();
  }

  @Step
  public static List<TcbsUserBook> getByUserIdAndType(String userId, String type) {
    Query<TcbsUserBook> query = CAS.casConnection.getSession().createQuery(
      "from TcbsUserBook a where a.userId=:userId and a.type=:type order by id desc", TcbsUserBook.class);
    query.setParameter("userId", new BigDecimal(userId));
    query.setParameter("type", type);
    return query.getResultList();
  }

  @Step
  public static void deleteByUserId(String userId, String type) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    org.hibernate.query.Query query = casConnection.getSession().createQuery(
      "Delete from TcbsUserBook a where a.userId=:userId and a.type=:type");
    query.setParameter("userId", new BigDecimal(userId));
    query.setParameter("type", type);
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }
}