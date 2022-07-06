package com.tcbs.automation.fundstation.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ASSET_BANK_ACCOUNT")
public class AssetBankAccount {
  public static Session session;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private int id;

  @Column(name = "PORTFOLIO_ID")
  private String accountId;

  @Column(name = "BANK_ACCOUNT_NAME")
  private String bankAccountName;

  @Column(name = "BANK_ACCOUNT_NUMBER")
  private String bankAccountNumber;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;
}
