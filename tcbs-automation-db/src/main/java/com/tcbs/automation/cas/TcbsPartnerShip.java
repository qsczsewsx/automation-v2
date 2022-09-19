package com.tcbs.automation.cas;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
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
public class TcbsPartnerShip {
  private static Logger logger = LoggerFactory.getLogger(TcbsPartnerShip.class);
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

  private static final String PARTNER_ACCOUNT_ID = "partnerAccountId";

  @Step
  public static TcbsPartnerShip getPartnerShip(String partnerAccountId) {
    CAS.casConnection.getSession().clear();
    Query<TcbsPartnerShip> query = CAS.casConnection.getSession().createQuery(
      "from TcbsPartnerShip a where a.partnerAccountId=:partnerAccountId", TcbsPartnerShip.class);
    query.setParameter(PARTNER_ACCOUNT_ID, partnerAccountId);
    return query.getSingleResult();
  }

  public static TcbsPartnerShip getByUserId(BigDecimal userId) {
    CAS.casConnection.getSession().clear();
    Query<TcbsPartnerShip> query = CAS.casConnection.getSession().createQuery(
      "from TcbsPartnerShip a where a.userId=:userId", TcbsPartnerShip.class);
    query.setParameter("userId", userId);
    return query.getSingleResult();
  }

  public static void updatePartnerStatusLinkAcc(String partnerAccountId, String linkAccountStatus) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    org.hibernate.query.Query query = casConnection.getSession().createNativeQuery(
      "Update TCBS_PARTNERSHIP set LINK_ACCOUNT_STATUS =:linkAccountStatus where PARTNER_ACCOUNT_ID=:partnerAccountId");
    query.setParameter(PARTNER_ACCOUNT_ID, partnerAccountId);
    query.setParameter("linkAccountStatus", linkAccountStatus);
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  public static void deleteByPartnerAccountId(String partnerAccountId) {
    try {
      Session session = CAS.casConnection.getSession();
      Transaction trans = session.beginTransaction();

      Query<?> query = session.createQuery("DELETE TcbsPartnerShip WHERE partnerAccountId=:partnerAccountId");
      query.setParameter(PARTNER_ACCOUNT_ID, partnerAccountId);
      query.executeUpdate();
      trans.commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  @Step
  public static TcbsPartnerShip getByPartnerAccountIdAndPartnerId(String partnerId,String partnerAccountId) {
    CAS.casConnection.getSession().clear();
    Query<TcbsPartnerShip> query = CAS.casConnection.getSession().createQuery(
      "from TcbsPartnerShip a where a.partnerId=:partnerId and a.partnerAccountId=:partnerAccountId", TcbsPartnerShip.class);
    query.setParameter(PARTNER_ACCOUNT_ID, partnerAccountId);
    query.setParameter("partnerId", partnerId);
    return query.getSingleResult();
  }

}
