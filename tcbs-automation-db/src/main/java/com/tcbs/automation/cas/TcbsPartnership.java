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

import static com.tcbs.automation.cas.CAS.casConnection;

@Entity
@Table(name = "TCBS_PARTNERSHIP")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TcbsPartnership {
  private static Logger logger = LoggerFactory.getLogger(TcbsPartnership.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "USER_ID")
  private BigDecimal userId;
  @Column(name = "PARTNER_ID")
  private String partnerId;
  @Column(name = "PARTNER_ACCOUNT_ID")
  private String partnerAccountId;
  @Column(name = "LINK_ACCOUNT_STATUS")
  private String linkAccountStatus;
  @Column(name = "LINK_ACCOUNT_TNC")
  private String linkAccountTnc;
  @Column(name = "LINK_ACCOUNT_DATE")
  private Timestamp linkAccountDate;
  @Column(name = "LINK_IA_STATUS")
  private String linkIaStatus;
  @Column(name = "LINK_IA_TNC")
  private String linkIaTnc;
  @Column(name = "LINK_IA_DATE")
  private Timestamp linkIaDate;
  @Column(name = "UPDATED_DATE")
  private Timestamp updatedDate;
  @Column(name = "CONFIRM_ID")
  private String confirmId;

  @Step
  public static TcbsPartnership getByUserId(BigDecimal userId) {
    CAS.casConnection.getSession().clear();
    Query<TcbsPartnership> query = casConnection.getSession().createQuery(
      "from TcbsPartnership a where a.userId =: userId", TcbsPartnership.class);
    query.setParameter("userId", userId);
    return query.getSingleResult();
  }

}