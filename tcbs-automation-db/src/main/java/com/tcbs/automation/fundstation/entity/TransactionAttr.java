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
@Table(name = "TRANSACTION_ATTR")
public class TransactionAttr {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "TRANSACTION_ATTR_ID")
  private int transactionAttrId;

  @Column(name = "TRANSACTION_ID")
  private String transactionId;

  @Column(name = "KEY")
  private String key;
  @Column(name = "VALUE")
  private String value;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;
}
