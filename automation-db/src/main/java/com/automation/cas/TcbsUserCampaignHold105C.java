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

@Entity
@SuppressWarnings("unchecked")
@Table(name = "xxxx_USER_CAMPAIGN_HOLD105C")
@Getter
@Setter
public class xxxxUserCampaignHold105C {
  private static Logger logger = LoggerFactory.getLogger(xxxxUserCampaignHold105C.class);
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
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    Query<xxxxUserCampaignHold105C> query = CAS.casConnection.getSession().createQuery(
      "Update xxxxUserCampaignHold105C a set a.status =:status where a.phoneNumber=:phoneNumber ");
    query.setParameter("phoneNumber", phoneNumber);
    query.setParameter("status", status);
    query.executeUpdate();
    CAS.casConnection.getSession().getTransaction().commit();
  }

}