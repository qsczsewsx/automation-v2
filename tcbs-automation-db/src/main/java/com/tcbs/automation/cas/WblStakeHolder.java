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
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "WBL_STAKEHOLDER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WblStakeHolder {

  private static Logger logger = LoggerFactory.getLogger(WblStakeHolder.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "WBLUSER_ID")
  private BigDecimal wbluserId;
  @Column(name = "COMPANY_NAME")
  private String companyName;
  @Column(name = "COMPANY_ID_NUMBER")
  private String companyIdNumber;
  @Column(name = "ADDRESS")
  private String address;
  @Column(name = "CAPITAL_RATIO")
  private String capitalRatio;
  @Column(name = "FROM_DATE")
  private Date fromDate;
  @Column(name = "STATUS")
  private String status;

  @Step
  public static List<WblStakeHolder> getByWblUserId(BigDecimal wbluserId) {
    Query<WblStakeHolder> query = CAS.casConnection.getSession().createQuery(
      "from WblStakeHolder a where a.wbluserId=:wbluserId", WblStakeHolder.class);
    query.setParameter("wbluserId", wbluserId);
    return query.getResultList();
  }
}
