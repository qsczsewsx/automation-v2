package com.tcbs.automation.tcbond;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "INV_BOND_YIELD_TO_MATURITY")
public class InvBondYieldToMaturity {

  @Id
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "TYPE")
  private String type;
  @Column(name = "INVESTMENTRATE")
  private String investmentrate;
  @Column(name = "INVESTMENTTIMEBYMONTH")
  private String investmenttimebymonth;
  @Column(name = "INVESTMENTRATEWITHRECOUPON")
  private String investmentratewithrecoupon;
  @Column(name = "PRODUCTCODE")
  private String productcode;
  @Column(name = "CREATEDDATE")
  private java.sql.Timestamp createddate;
  @Column(name = "UPDATEDDATE")
  private java.sql.Timestamp updateddate;
  @Column(name = "INVESTMENTRATERECOUPONCOMMIT")
  private String investmentraterecouponcommit;
  @Column(name = "BUYUNITPRICE")
  private String buyunitprice;
  @Column(name = "UNITPRICECLEAN")
  private String unitpriceclean;
  @Column(name = "RATEPRIMARYFIX")
  private String rateprimaryfix;
  @Column(name = "SPREAD")
  private String spread;
  @Column(name = "INVESTMENTRATETWELVEMONTH")
  private String investmentratetwelvemonth;
  @Column(name = "FIRSTINVESTMENTRATE")
  private String firstinvestmentrate;

  @Step
  public InvBondYieldToMaturity getYieldsForCusByProductCode(String productcode) {
    Query<InvBondYieldToMaturity> query = TcBond.tcBondDbConnection.getSession().createQuery(
      "from InvBondYieldToMaturity where type = 'Self-served' and investmenttimebymonth is not null  and investmentratewithrecoupon is not null and productcode = '" + productcode + "' ",
      InvBondYieldToMaturity.class);
    return query.getSingleResult();
  }


}

