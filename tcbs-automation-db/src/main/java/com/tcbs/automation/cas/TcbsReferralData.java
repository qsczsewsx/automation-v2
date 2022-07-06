package com.tcbs.automation.cas;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "TCBS_REFERRAL_DATA")
public class TcbsReferralData {
  private static Logger logger = LoggerFactory.getLogger(TcbsReferralData.class);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @NotNull
  @Column(name = "DATA_REFERRAL_ID")
  private String dataReferralId;
  @NotNull
  @Column(name = "REFERRAL_TYPE")
  private String referralType;
  @Column(name = "PAYLOAD")
  private String payload;
  @Column(name = "CREATED_DATE")
  private Date createdDate;
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;

  public TcbsReferralData getByDataReferralId(String dataReferralId) {
    Query<TcbsReferralData> query = CAS.casConnection.getSession().createQuery(
      "from TcbsReferralData a where a.dataReferralId=:dataReferralId", TcbsReferralData.class);
    query.setParameter("dataReferralId", dataReferralId);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  public void deleteByDataReferralId(String dataReferralId) {
    try {
      Session session = CAS.casConnection.getSession();
      Transaction trans = session.beginTransaction();

      Query<?> query = session.createQuery("DELETE TcbsReferralData WHERE dataReferralId=:dataReferralId and referralType=:referralType");
      query.setParameter("dataReferralId", dataReferralId);
      query.setParameter("referralType", "4");
      query.executeUpdate();
      trans.commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }
}
