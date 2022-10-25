package com.automation.cas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "xxxx_CONTRACT_TRUST")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class xxxxContractTrust {

  private static Logger logger = LoggerFactory.getLogger(xxxxContractTrust.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @NotNull
  @Column(name = "xxxxID")
  private String xxxxid;
  @Column(name = "FULL_NAME")
  private String fullName;
  @Column(name = "BIRTHDAY")
  private String birthday;
  @Column(name = "NATIONNALITY")
  private String nationnality;
  @Column(name = "PASSPORT_NO")
  private String passportNo;
  @Column(name = "ISSUE_DATE")
  private String issueDate;
  @Column(name = "ISSUE_PLACE")
  private String issuePlace;
  @Column(name = "TYPE")
  private String type;
  @Column(name = "SHORT_NAME")
  private String shortName;
  @Column(name = "HEADQUARTERS")
  private String headquarters;
  @Column(name = "ENTERPRISE_CODE")
  private String enterpriseCode;

  @Step
  public static xxxxContractTrust getByxxxxId(String xxxxid) {
    CAS.casConnection.getSession().clear();
    Query<xxxxContractTrust> query = CAS.casConnection.getSession().createQuery(
      "from xxxxContractTrust a where a.xxxxid=:xxxxid", xxxxContractTrust.class);
    query.setParameter("xxxxid", xxxxid);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new xxxxContractTrust();
    }
  }

}
