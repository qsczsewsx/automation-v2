package com.automation.cas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "xxxx_ADDRESS_DIVISION")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class xxxxAddressDivision {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "DIVISION_NAME")
  private String divisionName;
  @Column(name = "DIVISION_LEVEL")
  private BigDecimal divisionLevel;
  @Column(name = "PARENT_ID")
  private BigDecimal parentId;
  @Column(name = "DIVISION_CODE")
  private String divisionCode;
  @Column(name = "DIVISION_NAME_EN")
  private String divisionNameEn;
  @Column(name = "RANKING")
  private BigDecimal ranking;

  @Step
  public static xxxxAddressDivision getById(String id) {
    Query<xxxxAddressDivision> query = CAS.casConnection.getSession().createQuery(
      "from xxxxAddressDivision a where a.id=:id", xxxxAddressDivision.class);
    query.setParameter("id", new BigDecimal(id));
    return query.getSingleResult();
  }
}

