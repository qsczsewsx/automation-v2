package com.automation.cas;

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

import static com.automation.cas.CAS.casConnection;

@Entity
@Table(name = "xxxx_RELATION")
@Getter
@Setter
public class xxxxRelation {

  private static Logger logger = LoggerFactory.getLogger(xxxxRelation.class);

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "xxxxID")
  private String xxxxId;
  @Column(name = "CUSTODYCD")
  private String custodyCd;
  @Column(name = "VIP_TYPE")
  private String vipType;
  @Column(name = "IDENTIFY_TYPE")
  private String identifyType;
  @Column(name = "IDENTIFY_CUSTODYCD")
  private String identifyCustodyCd;
  @Column(name = "SYCN_DATETIME")
  private Timestamp sycnDatetime;
  @Column(name = "UPDATED_DATETIME")
  private Timestamp updatedDatetime;
  @Column(name = "STATUS")
  private String status;

  @Step
  public static List<xxxxRelation> getByStatus(int status) {
    CAS.casConnection.getSession().clear();
    Query<xxxxRelation> query = CAS.casConnection.getSession().createQuery(
      "from xxxxRelation a where a.status=:status ", xxxxRelation.class);
    query.setParameter("status", String.valueOf(status));
    return query.getResultList();
  }

  public static xxxxRelation getByCustodyCd(String custodyCd) {
    casConnection.getSession().clear();
    Query<xxxxRelation> query = CAS.casConnection.getSession().createQuery(
      "from xxxxRelation a where a.custodyCd=:custodyCd ", xxxxRelation.class);
    query.setParameter("custodyCd", custodyCd);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new xxxxRelation();
    }
  }

  public static List<xxxxRelation> getByIdentifyCustodyCd(String identifyCustodyCd) {
    casConnection.getSession().clear();
    Query<xxxxRelation> query = CAS.casConnection.getSession().createQuery(
      "from xxxxRelation a where a.identifyCustodyCd=:identifyCustodyCd ", xxxxRelation.class);
    query.setParameter("identifyCustodyCd", identifyCustodyCd);
    return query.getResultList();
  }

}
