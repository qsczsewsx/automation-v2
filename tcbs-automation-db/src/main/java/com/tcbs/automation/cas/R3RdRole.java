package com.tcbs.automation.cas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "R3RD_ROLE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class R3RdRole {
  private static Logger logger = LoggerFactory.getLogger(R3RdRole.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;

  @Column(name = "TITLE")
  private String title;

  @Column(name = "ROLE")
  private String role;

  @Column(name = "SALE_CODE")
  private String saleCode;

  @Column(name = "STATUS")
  private BigDecimal status;

  @Column(name = "BANK_CODE")
  private String bankCode;

  public static List<String> getAllPartner() {
    CAS.casConnection.getSession().clear();
    Query<String> query = CAS.casConnection.getSession().createNativeQuery(
      "SELECT BANK_CODE FROM R3RD_ROLE rrr GROUP BY BANK_CODE ");
    return query.getResultList();
  }

  public static List<R3RdRole> getRoleByPartner(String partner) {
    CAS.casConnection.getSession().clear();
    Query<R3RdRole> query = CAS.casConnection.getSession().createNativeQuery(
      "SELECT ID, TITLE, STATUS, ROLE, SALE_CODE, BANK_CODE FROM R3RD_ROLE WHERE BANK_CODE = ?1 ORDER BY ID ASC", R3RdRole.class);
    query.setParameter(1, partner);
    return query.getResultList();
  }

  public static List<R3RdRole> getRoleByName(String role) {
    CAS.casConnection.getSession().clear();
    Query<R3RdRole> query = CAS.casConnection.getSession().createNativeQuery(
      "SELECT ID, TITLE, STATUS, ROLE, SALE_CODE, BANK_CODE FROM R3RD_ROLE WHERE ROLE = ?1", R3RdRole.class);
    query.setParameter(1, role);
    return query.getResultList();
  }

  public static R3RdRole getRoleByNameAndBankCode(String role, String bankCode) {
    CAS.casConnection.getSession().clear();
    Query<R3RdRole> query = CAS.casConnection.getSession().createNativeQuery(
      "SELECT ID, TITLE, STATUS, ROLE, SALE_CODE, BANK_CODE FROM R3RD_ROLE WHERE ROLE = ?1 AND BANK_CODE = ?2", R3RdRole.class);
    query.setParameter(1, role);
    query.setParameter(2, bankCode);
    return query.getSingleResult();
  }
}
