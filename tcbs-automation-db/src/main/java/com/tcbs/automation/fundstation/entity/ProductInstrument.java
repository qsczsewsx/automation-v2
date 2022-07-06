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
@Table(name = "PRODUCT_INSTRUMENT")
public class ProductInstrument {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "ID")
  private int id;

  @Column(name = "PRODUCT_ID")
  private String productId;

  @Column(name = "INSTRUMENT_ID")
  private String instrumentId;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;
}