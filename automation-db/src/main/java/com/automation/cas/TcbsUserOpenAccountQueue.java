package com.automation.cas;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "xxxx_USER_OPENACCOUNT_QUEUE")
@Getter
@Setter
public class xxxxUserOpenAccountQueue {
  private static Logger logger = LoggerFactory.getLogger(xxxxUserOpenAccountQueue.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
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
  private Date createdDate;
  @Column(name = "OPENACCOUNTDATA")
  private String openaccountdata;
  @Column(name = "REFERENCEID")
  private String referenceid;
  @Column(name = "REFERER")
  private BigDecimal referer;
  @Column(name = "STATUS")
  private BigDecimal status;
  @Column(name = "GENDER")
  private BigDecimal gender;
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
  private BigDecimal userId;
  @Column(name = "BANKPROVINCE")
  private String bankprovince;
  @Column(name = "ORIGINAL_ID")
  private BigDecimal originalId;
  @Column(name = "INTENTION")
  private String intention;
  @Column(name = "CAMPAIGN_CODE")
  private String campaignCode;
  @Column(name = "CITIZENSHIP")
  private String citizenship;
  @Column(name = "JOB")
  private String job;
  @Column(name = "POSITION")
  private String position;
  @Column(name = "PERMANENTADDRESS")
  private String permanentaddress;
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;
  @Column(name = "UPLOADED_DATE")
  private Date uploadedDate;
  @Column(name = "REFERERPERSON")
  private BigDecimal refererperson;
  @Column(name = "UPLOADUSER")
  private BigDecimal uploaduser;
  @Column(name = "DOWNLOADED_DATE")
  private Date downloadedDate;
  @Column(name = "DOWNLOADED_USER")
  private BigDecimal downloadedUser;
  @Column(name = "STATUS_PROCSS")
  private BigDecimal statusProcss;
  @Column(name = "NAME_ACRONYN")
  private String nameAcronyn;
  @Column(name = "MST")
  private String mst;
  @Column(name = "CUST_TYPE")
  private BigDecimal custType;
  @Column(name = "FLOW_OPEN_ACCOUNT")
  private BigDecimal flowOpenAccount;
  @Column(name = "DOCUMENT_ECM_ID")
  private String documentEcmId;
  @Column(name = "SCAN_ID_FILES")
  private String scanIdFiles;
  @Column(name = "WEALTH_PARTNER_xxxx_ID")
  private String wealthPartnerxxxxId;
  @Column(name = "HOME_PHONE")
  private String homePhone;
  @Column(name = "FAX")
  private String fax;
  @Column(name = "ONBOARDING_DATA")
  private String onboardingData;
  @Column(name = "WEALTH_PARTNER_CHANNEL")
  private String wealthPartnerChannel;
  @Column(name = "WEALTH_PARTNER_CODE")
  private String wealthPartnerCode;
  @Column(name = "RECEIVE_ADVERTISE")
  private String receiveAdvertise;
  @Column(name = "PK_VID")
  private String pkVid;
  @Column(name = "BUSINESS_TYPE")
  private String businessType;
  @Column(name = "WEALTH_PARTNER_CUS_TRANSPARENT")
  private BigDecimal wealthPartnerCusTransparent;
  @Column(name = "REFERRAL_CODE")
  private String referralCode;
  @Column(name = "CODE_105C")
  private String code105C;
  @Column(name = "SUB_FLOW_OPEN_ACCOUNT")
  private BigDecimal subFlowOpenAccount;
  @Column(name = "WEALTH_PARTNER_IS_COMFIRM")
  private BigDecimal wealthPartnerIsComfirm;
  @Column(name = "IS_FOREIGN_PHONE")
  private BigDecimal isForeignPhone;
  @Column(name = "REFER_CODE_HOLD105C")
  private String referCodeHold105C;
  @Column(name = "PHONE_CODE")
  private String phoneCode;

  private static final String DATA_PHONE = "phone";

  @Step
  public static xxxxUserOpenAccountQueue getByPhone(String phone) {
    Query<xxxxUserOpenAccountQueue> query = CAS.casConnection.getSession().createQuery(
      "from xxxxUserOpenAccountQueue a where a.phone=:phone", xxxxUserOpenAccountQueue.class);
    query.setParameter(DATA_PHONE, phone);
    return query.getSingleResult();
  }

  @Step
  public static xxxxUserOpenAccountQueue getByxxxxUserOpenAccountQueue(String userId) {
    Query<xxxxUserOpenAccountQueue> query = CAS.casConnection.getSession().createQuery(
      "from xxxxUserOpenAccountQueue a where a.userid=:userId", xxxxUserOpenAccountQueue.class);
    query.setParameter("userId", userId);
    return query.getSingleResult();
  }

  public static void updateUserId(String phone, String userId) {
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    org.hibernate.query.Query query = CAS.casConnection.getSession().createQuery(
      "Update xxxxUserOpenAccountQueue a set a.userid =:userId where a.phone=:phone");
    query.setParameter("userId", userId);
    query.setParameter(DATA_PHONE, phone);
    query.executeUpdate();
    CAS.casConnection.getSession().getTransaction().commit();
  }

  public static void deleteByPhone(String phone) {
    try {
      Session session = CAS.casConnection.getSession();
      session.clear();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      Query<?> query = session.createQuery("DELETE xxxxUserOpenAccountQueue WHERE phone=:phone");
      query.setParameter(DATA_PHONE, phone);
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public static xxxxUserOpenAccountQueue getByReferenceId(String referenceid) {
    Query<xxxxUserOpenAccountQueue> query = CAS.casConnection.getSession().createQuery(
      "from xxxxUserOpenAccountQueue a where a.referenceid=:referenceid", xxxxUserOpenAccountQueue.class);
    query.setParameter("referenceid", referenceid);
    return query.getSingleResult();
  }

}
