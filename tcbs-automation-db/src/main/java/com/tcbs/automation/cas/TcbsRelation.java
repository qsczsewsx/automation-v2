package com.tcbs.automation.cas;

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

import static com.tcbs.automation.cas.CAS.casConnection;

@Entity
@Table(name = "TCBS_RELATION")
@Getter
@Setter
public class TcbsRelation {

  private static Logger logger = LoggerFactory.getLogger(TcbsRelation.class);

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "TCBSID")
  private String tcbsId;
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
  public static List<TcbsRelation> getByStatus(int status) {
    CAS.casConnection.getSession().clear();
    Query<TcbsRelation> query = CAS.casConnection.getSession().createQuery(
      "from TcbsRelation a where a.status=:status ", TcbsRelation.class);
    query.setParameter("status", String.valueOf(status));
    return query.getResultList();
  }

  public static TcbsRelation getByCustodyCd(String custodyCd) {
    casConnection.getSession().clear();
    Query<TcbsRelation> query = CAS.casConnection.getSession().createQuery(
      "from TcbsRelation a where a.custodyCd=:custodyCd ", TcbsRelation.class);
    query.setParameter("custodyCd", custodyCd);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new TcbsRelation();
    }
  }

  public static List<TcbsRelation> getByIdentifyCustodyCd(String identifyCustodyCd) {
    casConnection.getSession().clear();
    Query<TcbsRelation> query = CAS.casConnection.getSession().createQuery(
      "from TcbsRelation a where a.identifyCustodyCd=:identifyCustodyCd ", TcbsRelation.class);
    query.setParameter("identifyCustodyCd", identifyCustodyCd);
    return query.getResultList();
  }

}
