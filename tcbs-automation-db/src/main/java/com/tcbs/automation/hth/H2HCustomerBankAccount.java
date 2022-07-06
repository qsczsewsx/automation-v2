package com.tcbs.automation.hth;

import com.tcbs.automation.hth.HthDb;
import lombok.*;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.sql.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "H2H_CUSTOMER_BANK_ACCOUNT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class H2HCustomerBankAccount {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "ACCOUNT_NUMBER")
  private String accountNumber;
  @Column(name = "H2H_NAME")
  private String h2HName;
  @Column(name = "T24_NAME")
  private String t24Name;
  @Column(name = "BANK_STATUS_CODE")
  private String bankStatusCode;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;


  public static void deleteAccountNumber(String accountNumber) {
    Session session = HthDb.h2hConnection.getSession();
    session.beginTransaction();
    Query query = session.createQuery("delete from H2HCustomerBankAccount where accountNumber=:accountNumber");
    query.setParameter("accountNumber", accountNumber).executeUpdate();
    session.getTransaction().commit();

  }

  public static void updateStatus(String status, String accountNumber) {
    Session session = HthDb.h2hConnection.getSession();
    session.beginTransaction();
    javax.persistence.Query query = session.createQuery(
      "update H2HCustomerBankAccount set status=:status where accountNumber=:accountNumber"
    );
    query.setParameter("status", status)
      .setParameter("accountNumber", accountNumber)
      .executeUpdate();
    session.getTransaction().commit();
  }

}
