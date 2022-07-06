package com.tcbs.automation.cas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.security.Timestamp;
import java.util.List;

@Entity
@Table(name = "R3RD_AGENCY")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class R3RdAgency {
  @Id
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "AGENCYCODE")
  @NotNull
  private String agencyCode;
  @Column(name = "AGENCYNAME")
  @NotNull
  private String agencyName;
  @Column(name = "BUSINESSNO")
  private String businessNo;
  @Column(name = "BUSINESSDATE")
  private Timestamp businessDate;
  @Column(name = "BUSINESSBY")
  private String businessBy;
  @Column(name = "REPRESENTUSER")
  private String representUser;
  @Column(name = "REPERSENTROLE")
  private String representRole;
  @Column(name = "PHONE")
  private String phone;
  @Column(name = "FAX")
  private String fax;
  @Column(name = "ADDRESS")
  private String address;
  @Column(name = "EMAIL")
  private String email;
  @Column(name = "CREATEDDATE")
  private Timestamp createdDate;
  @Column(name = "UPDATEDATE")
  private Timestamp updatedDate;
  @Column(name = "ACTIVE")
  private BigDecimal active;
  @Column(name = "ISCN")
  private BigDecimal iscn;
  @Column(name = "ZONE")
  private String zone;
  @Column(name = "AREA")
  private String area;
  @Column(name = "CITY")
  private String city;
  @Column(name = "EMAIL2")
  private String email2;

  @Step
  public static List<String> getByAgencyCode(String agencyCode) {
    CAS.casConnection.getSession().clear();
    Query<String> query = CAS.casConnection.getSession().createNativeQuery(
      "select a.AGENCYCODE from R3RD_AGENCY a WHERE a.AGENCYCODE = ?1");
    query.setParameter(1, agencyCode);
    return query.getResultList();
  }
}

