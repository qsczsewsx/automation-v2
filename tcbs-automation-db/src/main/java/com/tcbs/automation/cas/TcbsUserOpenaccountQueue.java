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
import java.util.Date;

import static com.tcbs.automation.cas.CAS.casConnection;

@Entity
@Table(name = "TCBS_USER_OPENACCOUNT_QUEUE")
@Getter
@Setter
public class TcbsUserOpenaccountQueue {
  private static Logger logger = LoggerFactory.getLogger(TcbsReferralData.class);
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private Double id;
  @Column(name = "FIRSTNAME")
  private String firstname;
  @Column(name = "LASTNAME")
  private String lastname;
  @Column(name = "BIRTHDAY")
  private Date birthday;
  @Column(name = "IDNUMBER")
  private String idnumber;
  @Column(name = "IDDATE")
  private Date iddate;
  @Column(name = "IDPLACE")
  private String idplace;
  @Column(name = "EMAIL")
  private String email;
  @Column(name = "PHONE")
  private String phone;
  @Column(name = "CREATED_DATE")
  private Date createddate;
  @Column(name = "OPENACCOUNTDATA")
  private String openaccountdata;
  @Column(name = "REFERENCEID")
  private String referenceid;
  @Column(name = "REFERER")
  private Double referer;
  @Column(name = "STATUS")
  private Double status;
  @Column(name = "GENDER")
  private Double gender;
  @Column(name = "ADDRESS")
  private String address;
  @Column(name = "PROVINCE")
  private String province;
  @Column(name = "BANKACCOUNTNUMBER")
  private String bankaccountnumber;
  @Column(name = "BANKACCOUNTNAME")
  private String bankaccountname;
  @Column(name = "BANKNAME")
  private String bankname;
  @Column(name = "BANKBRANCH")
  private String bankbranch;
  @Column(name = "BANKCODE")
  private String bankcode;
  @Column(name = "PASSWORD")
  private String password;
  @Column(name = "PASSWORDMD5")
  private String passwordmd5;
  @Column(name = "PASSWORDBLANK")
  private String passwordblank;
  @Column(name = "UPLOADED")
  private String uploaded;
  @Column(name = "USER_ID")
  private String userid;
  @Column(name = "BANKPROVINCE")
  private String bankprovince;
  @Column(name = "ORIGINAL_ID")
  private Double originalid;
  @Column(name = "INTENTION")
  private String intention;
  @Column(name = "CAMPAIGN_CODE")
  private String campaigncode;
  @Column(name = "CITIZENSHIP")
  private String citizenship;
  @Column(name = "JOB")
  private String job;
  @Column(name = "POSITION")
  private String position;
  @Column(name = "PERMANENTADDRESS")
  private String permanentaddress;
  @Column(name = "UPDATED_DATE")
  private Date updateddate;
  @Column(name = "UPLOADED_DATE")
  private Date uploadeddate;
  @Column(name = "REFERERPERSON")
  private Double refererperson;
  @Column(name = "UPLOADUSER")
  private Double uploaduser;
  @Column(name = "DOWNLOADED_DATE")
  private Date downloadeddate;
  @Column(name = "DOWNLOADED_USER")
  private Double downloadeduser;
  @Column(name = "STATUS_PROCSS")
  private Double statusprocss;
  @Column(name = "NAME_ACRONYN")
  private String nameacronyn;
  @Column(name = "MST")
  private String mst;
  @Column(name = "CUST_TYPE")
  private Double custtype;
  @Column(name = "FLOW_OPEN_ACCOUNT")
  private Double flowopenaccount;
  @Column(name = "DOCUMENT_ECM_ID")
  private String documentecmid;
  @Column(name = "SCAN_ID_FILES")
  private String scanidfiles;
  @Column(name = "WEALTH_PARTNER_TCBS_ID")
  private String wealthpartnertcbsid;
  @Column(name = "ONBOARDING_DATA")
  private String onboardingdata;
  @Column(name = "HOME_PHONE")
  private String homephone;
  @Column(name = "FAX")
  private String fax;
  @Column(name = "WEALTH_PARTNER_CODE")
  private String wealthpartnercode;
  @Column(name = "WEALTH_PARTNER_CHANNEL")
  private String wealthpartnerchannel;

  @Step
  public static TcbsUserOpenaccountQueue getByPhone(String phone) {
    Query<TcbsUserOpenaccountQueue> query = CAS.casConnection.getSession().createQuery(
      "from TcbsUserOpenaccountQueue a where a.phone=:phone", TcbsUserOpenaccountQueue.class);
    query.setParameter("phone", phone);
    return query.getSingleResult();
  }

  @Step
  public static TcbsUserOpenaccountQueue getByTcbsUserOpenaccountQueue(String userId) {
    Query<TcbsUserOpenaccountQueue> query = CAS.casConnection.getSession().createQuery(
      "from TcbsUserOpenaccountQueue a where a.userid=:userId", TcbsUserOpenaccountQueue.class);
    query.setParameter("userId", userId);
    return query.getSingleResult();
  }

  public static void updateUserId(String phone, String userId) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    org.hibernate.query.Query query = casConnection.getSession().createQuery(
      "Update TcbsUserOpenaccountQueue a set a.userid =:userId where a.phone=:phone");
    query.setParameter("userId", userId);
    query.setParameter("phone", phone);
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  public static void deleteByPhone(String phone) {
    try {
      Session session = CAS.casConnection.getSession();
      Transaction trans = session.beginTransaction();

      Query<?> query = session.createQuery("DELETE TcbsUserOpenaccountQueue WHERE phone=:phone");
      query.setParameter("phone", phone);
      query.executeUpdate();
      trans.commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

}
