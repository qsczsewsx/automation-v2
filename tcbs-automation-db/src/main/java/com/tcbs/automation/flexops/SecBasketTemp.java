package com.tcbs.automation.flexops;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "SECBASKETTEMP")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SecBasketTemp {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SECBASKETTEMP")
  @SequenceGenerator(name = "SEQ_SECBASKETTEMP", sequenceName = "SEQ_SECBASKETTEMP", allocationSize = 1)
  @Column(name = "AUTO_ID")
  private Long id;
  @Column(name = "BASKETID")
  private String basketId;
  @Column(name = "SYMBOL")
  private String symbol;
  @Column(name = "MRRATIORATE")
  private Double mrRatioRate;
  @Column(name = "MRRATIOLOAN")
  private Double mrRatioLoan;
  @Column(name = "MRPRICERATE")
  private Double mrPriceRate;
  @Column(name = "MRPRICELOAN")
  private Double mrPriceLoan;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "APPROVED")
  private String approved;
  @Column(name = "TELLERID")
  private String tellerId;
  @Column(name = "APRID")
  private String aprId;
  @Column(name = "DELTD")
  private String delId;
}
