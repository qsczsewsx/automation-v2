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
import java.sql.Timestamp;

@Entity
@Table(name = "xxxx_PARTNERSHIP_CONFIRM")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class xxxxPartnerShipConfirm {
  private static Logger logger = LoggerFactory.getLogger(xxxxPartnerShipConfirm.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "PARTNERSHIP_ID")
  private BigDecimal partnershipId;
  @Column(name = "TYPE")
  private String type;
  @Column(name = "METHOD")
  private String method;
  @Column(name = "VALUE")
  private String value;
  @Column(name = "STATUS")
  private BigDecimal status;
  @Column(name = "NOTE")
  private String note;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "UPDATED_DATE")
  private Timestamp updatedDate;

  @Step
  public static xxxxPartnerShipConfirm getConfirmIdByPartnerAndType(BigDecimal partnershipId, String type) {
    CAS.casConnection.getSession().clear();
    Query<xxxxPartnerShipConfirm> query = CAS.casConnection.getSession().createQuery(
      "from xxxxPartnerShipConfirm a where a.partnershipId=:partnershipId and a.type=:type", xxxxPartnerShipConfirm.class);
    query.setParameter("partnershipId", partnershipId);
    query.setParameter("type", type);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new xxxxPartnerShipConfirm();
    }
  }

}
