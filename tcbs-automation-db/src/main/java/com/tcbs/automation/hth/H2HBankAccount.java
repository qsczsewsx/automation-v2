package com.tcbs.automation.hth;

import lombok.*;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static com.tcbs.automation.hth.HthDb.h2hConnection;

@Entity
@Table(name = "H2H_BANK_ACCOUNT")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class H2HBankAccount {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "ACCOUNT_NUMBER")
  private String accountNumber;
  @Column(name = "ACCOUNT_NAME")
  private String accountName;
  @Column(name = "ACCOUNT_TYPE")
  private String accountType;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;
  @Column(name = "PARTNER")
  private String partner;
  @Column(name = "CODE")
  private String code;

  /****
   *** Author Thuynt53
   **/

  public static List<H2HBankAccount> getListSourceAccountNumber(String accountNumber, String accountName, String accountType,
                                                                String status, String partner, String code) {
    org.hibernate.query.Query<H2HBankAccount> query;
    query = h2hConnection.getSession().createQuery(
      "FROM H2HBankAccount WHERE id is not null " +
        (StringUtils.isNotEmpty(accountNumber) ? " and accountNumber =:accountNumber " : "") +
        (StringUtils.isNotEmpty(accountName) ? " and accountName=:accountName " : "") +
        (StringUtils.isNotEmpty(accountType) ? " and accountType=:accountType " : "") +
        (StringUtils.isNotEmpty(status) ? " and status=:status " : "") +
        (StringUtils.isNotEmpty(partner) ? " and partner=:partner " : "") +
        (StringUtils.isNotEmpty(code) ? " and code=:code " : "") +
        " ORDER BY id ASC ",
      H2HBankAccount.class
    );
    setParameter(query, accountNumber, accountName, accountType, status,
      partner, code);
    return query.getResultList();
  }

  private static void setParameter(Query query, String accountNumber, String accountName, String accountType, String status,
                                   String partner, String code) {

    if (StringUtils.isNotEmpty(accountNumber)) {
      query.setParameter("accountNumber", accountNumber);
    }
    if (StringUtils.isNotEmpty(accountName)) {
      query.setParameter("accountName", accountName);
    }
    if (StringUtils.isNotEmpty(accountType)) {
      query.setParameter("accountType", accountType);
    }
    if (StringUtils.isNotEmpty(status)) {
      query.setParameter("status", status);
    }
    if (StringUtils.isNotEmpty(partner)) {
      query.setParameter("partner", partner);
    }
    if (StringUtils.isNotEmpty(code)) {
      query.setParameter("code", code);
    }
  }
}
