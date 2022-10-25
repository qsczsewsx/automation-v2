package com.automation.cas;

import lombok.*;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "STP_MODE_HISTORY")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StpModeHistory {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;

  @Column(name = "PRODUCT")
  private String product;

  @Column(name = "STP_MODE")
  private String stpMode;

  @Column(name = "NOTE")
  private String note;

  @Column(name = "ACTOR")
  private String actor;

  @Column(name = "CREATED_DATE")
  private Date createDate;

  @Step
  public static StpModeHistory getLatestModeByProduct(String product) {
    CAS.casConnection.getSession().clear();
    Query<StpModeHistory> query = CAS.casConnection.getSession().createNativeQuery(
      "SELECT * FROM STP_MODE_HISTORY smh WHERE PRODUCT =:product ORDER BY CREATED_DATE DESC FETCH FIRST 1 ROWS ONLY", StpModeHistory.class);
    query.setParameter("product", product);
    return query.getSingleResult();
  }
}
