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

import static com.tcbs.automation.cas.CAS.casConnection;

@Entity
@SuppressWarnings("unchecked")
@Table(name = "TCBS_USER_CAMPAIGN_HOLD105C")
@Getter
@Setter
public class TcbsUserCampaignHold105C {
  private static Logger logger = LoggerFactory.getLogger(TcbsUserCampaignHold105C.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "CREATE_DATE")
  private Timestamp createDate;
  @Column(name = "TOTAL")
  private BigDecimal total;
  @Column(name = "CAMPAIGN_CODE_ID")
  private BigDecimal campaignCodeId;
  @Column(name = "PHONE_NUMBER")
  private String phoneNumber;

  @Step

  public static void updateStatus(String phoneNumber, String status) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    Query<TcbsUserCampaignHold105C> query = casConnection.getSession().createQuery(
      "Update TcbsUserCampaignHold105C a set a.status =:status where a.phoneNumber=:phoneNumber ");
    query.setParameter("phoneNumber", phoneNumber);
    query.setParameter("status", status);
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

}