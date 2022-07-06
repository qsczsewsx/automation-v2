package com.tcbs.automation.coco;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Getter
@Setter
@Table(name = "PE_PORTFOLIO_SNAPSHOT")
public class PePortfolioSnapshot {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ACCOUNT_ID")
  private String accountId;
  @Column(name = "TICKER")
  private String ticker;
  @Column(name = "REPORT_DATE")
  private Date reportDate;
  @Column(name = "OPEN_VOLUME")
  private Double openVolume;
  @Column(name = "OPEN_VALUE")
  private String openValue;
  @Column(name = "BUYING_VOLUME")
  private Double buyingVolume;
  @Column(name = "BUYING_VALUE")
  private String buyingValue;
  @Column(name = "SELLING_VOLUME")
  private Double sellingVolume;
  @Column(name = "SELLING_VALUE")
  private String sellingValue;
  @Column(name = "CLOSE_VOLUME")
  private Double closeVolume;
  @Column(name = "CLOSE_VALUE")
  private String closeValue;
  @Column(name = "COGS_PER_UNIT")
  private Double cogsPerUnit;
  @Column(name = "COGS_PER_UNIT_ADJ")
  private Double cogsPerUnitAdj;
  @Column(name = "REF_PRICE")
  private Double refPrice;
  @Column(name = "CLOSE_PRICE")
  private Double closePrice;
  @Column(name = "USER_ID")
  private String userId;

}
