package com.tcbs.automation.cas;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "VSD_IMPORT_FUND")
@Getter
@Setter
public class VsdImportFund {
  private static Logger logger = LoggerFactory.getLogger(VsdImportFund.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "FILE_NAME")
  private String fileName;
  @Column(name = "CREATED_DATETIME")
  private Timestamp createdDatetime;
  @Column(name = "I_DATE")
  private Timestamp iDate;
  @Column(name = "VSYMBOL")
  private String vsymbol;
  @Column(name = "TRADE")
  private BigDecimal trade;
  @Column(name = "FULLNAME")
  private String fullname;
  @Column(name = "IDCODE")
  private String idcode;
  @Column(name = "IDTYPE")
  private String idtype;
  @Column(name = "IDDATE")
  private Timestamp iddate;
  @Column(name = "IDPLACE")
  private String idplace;
  @Column(name = "SEX")
  private String sex;
  @Column(name = "BIRTHDATE")
  private Timestamp birthdate;
  @Column(name = "FAX")
  private String fax;
  @Column(name = "ADDRESS")
  private String address;
  @Column(name = "PHONE")
  private String phone;
  @Column(name = "EMAIL")
  private String email;
  @Column(name = "COUNTRY")
  private String country;
  @Column(name = "CUSTTYPE")
  private String custtype;
  @Column(name = "DBCODE")
  private String dbcode;
  @Column(name = "TAXNO")
  private String taxno;
  @Column(name = "CUSTODYCD")
  private String custodycd;
  @Column(name = "ACCTYPE")
  private String acctype;
  @Column(name = "BANKACC")
  private String bankacc;
  @Column(name = "BANKNAME")
  private String bankname;
  @Column(name = "CITYBANK")
  private String citybank;
  @Column(name = "REFNAME1")
  private String refname1;
  @Column(name = "REFPOST1")
  private String refpost1;
  @Column(name = "REFMOBILE1")
  private String refmobile1;
  @Column(name = "REFIDCODE1")
  private String refidcode1;
  @Column(name = "REFIDDATE1")
  private String refiddate1;
  @Column(name = "REFIDPLACE1")
  private String refidplace1;
  @Column(name = "REFCOUNTRY1")
  private String refcountry1;
  @Column(name = "REFADDRESS1")
  private String refaddress1;
  @Column(name = "SYMBOL")
  private String symbol;
  @Column(name = "MBNAME")
  private String mbname;
  @Column(name = "CUSTNAME")
  private String custname;
  @Column(name = "CFUIDCODE")
  private String cfuidcode;
  @Column(name = "CFUIDDATE")
  private String cfuiddate;
  @Column(name = "CFUIDPLACE")
  private String cfuidplace;
  @Column(name = "CFUADRESS")
  private String cfuadress;
  @Column(name = "CFUPHONE")
  private String cfuphone;
  @Column(name = "RATE")
  private BigDecimal rate;

  @Step
  public static List<VsdImportFund> getByFileName(String fileName) {
    CAS.casConnection.getSession().clear();
    Query<VsdImportFund> query = CAS.casConnection.getSession().createQuery(
      "from VsdImportFund a where a.fileName=:fileName", VsdImportFund.class);
    query.setParameter("fileName", fileName);
    return query.getResultList();
  }


}
