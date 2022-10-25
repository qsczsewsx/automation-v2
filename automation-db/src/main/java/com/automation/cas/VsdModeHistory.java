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
@Table(name = "VSD_MODE_HISTORY")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VsdModeHistory {
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

  @Step
  public static VsdModeHistory getLatestModeByProduct(String product) {
    CAS.casConnection.getSession().clear();
    Query<VsdModeHistory> query = CAS.casConnection.getSession().createNativeQuery(
      "SELECT ID, STP_MODE, NOTE, PRODUCT FROM VSD_MODE_HISTORY WHERE PRODUCT = ?1 ORDER BY CREATED_DATE DESC FETCH FIRST 1 ROWS ONLY", VsdModeHistory.class);
    query.setParameter(1, product);
    return query.getSingleResult();
  }
}
