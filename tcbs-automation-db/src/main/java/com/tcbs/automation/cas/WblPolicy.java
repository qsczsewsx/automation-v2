package com.tcbs.automation.cas;

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
import java.sql.Timestamp;

@Entity
@Table(name = "WBL_POLICY")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WblPolicy {
  private static Logger logger = LoggerFactory.getLogger(WblPolicy.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "CODE")
  private String code;
  @Column(name = "NAME")
  private String name;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "UPDATED_DATETIME")
  private Timestamp updatedDatetime;

  @Step

  public static WblPolicy getByPolicyCode(String policyCode) {
    Query<WblPolicy> query = CAS.casConnection.getSession().createQuery(
      "from WblPolicy a where a.code=:code", WblPolicy.class);
    query.setParameter("code", policyCode);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new WblPolicy();
    }
  }

}
