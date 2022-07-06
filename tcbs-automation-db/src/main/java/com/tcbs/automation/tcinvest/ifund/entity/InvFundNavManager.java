package com.tcbs.automation.tcinvest.ifund.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("unchecked")
@Table(name = "INV_FUND_NAV_MANAGER")
public class InvFundNavManager {
  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;
  @Column(name = "NAV")
  private String nav;
  @Column(name = "PRODUCT_ID")
  private String productId;
  @Column(name = "CREATED_TIMESTAMP")
  private String createdTimestamp;
  @Column(name = "UPDATED_TIMESTAMP")
  private String updatedTimestamp;
}
