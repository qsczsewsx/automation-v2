package com.automation.cas;

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
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Timestamp;
import java.util.List;

import static com.automation.cas.CAS.casConnection;

@Entity
@SuppressWarnings("unchecked")
@Table(name = "xxxx_USER")
@Getter
@Setter
public class xxxxUser {
  private static Logger logger = LoggerFactory.getLogger(xxxxUser.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "USERNAME")
  private String username;
  @Column(name = "PASSWORD")
  private String password;
  @Column(name = "EMAIL")
  private String email;
  @Column(name = "FIRSTNAME")
  private String firstname;
  @Column(name = "LASTNAME")
  private String lastname;
  @Column(name = "GENDER")
  private BigDecimal gender;
  @Column(name = "BIRTHDAY")
  private Timestamp birthday;
  @Column(name = "RELATIONSHIP")
  private BigDecimal relationship;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "UPDATED_DATE")
  private Timestamp updatedDate;
  @Column(name = "STATUS")
  private BigDecimal status;
  @Column(name = "PROFILE_PICTURE")
  private String profilePicture;
  @Column(name = "HONORIFIC")
  private BigDecimal honorific;
  @Column(name = "PHONE")
  private String phone;
  @Column(name = "xxxxID")
  private String xxxxid;
  @Column(name = "VSD_STATUS")
  private BigDecimal vsdStatus;
  @Column(name = "OTP_CHANNEL")
  private BigDecimal otpChannel;
  @Column(name = "CUSTYPE")
  private BigDecimal custype;
  @Column(name = "TRANSFER_STATUS")
  private BigDecimal transferStatus;
  @Column(name = "NAME_ACRONYN")
  private String nameAcronyn;
  @Column(name = "MST")
  private String mst;
  @Column(name = "FLOW_OPEN_ACCOUNT")
  private BigDecimal flowOpenAccount;
  @Column(name = "SYS_USER_TYPE")
  private BigDecimal sysUserType;
  @Column(name = "SYSTEM_USER")
  private BigDecimal systemUser;
  @Column(name = "ENVELOPE_ID")
  private String envelopeId;
  @Column(name = "DOCUSIGN_STATUS")
  private BigDecimal docusignStatus;
  @Column(name = "ACCOUNT_STATUS")
  private BigDecimal accountStatus;
  @Column(name = "AVATAR_DATA")
  private Blob avatarData;
  @Column(name = "AVATAR_HEADER")
  private String avatarHeader;
  @Column(name = "FIRSTTIME_LOGIN")
  private BigDecimal firsttimeLogin;
  @Column(name = "EDITABLE")
  private BigDecimal editable;
  @Column(name = "BPM_EKYC_STATUS")
  private String bpmEkycStatus;
  @Column(name = "BPM_EKYC_DENY_REASON")
  private String bpmEkycDenyReason;
  @Column(name = "BPM_EKYC_DENY_CONTENT")
  private String bpmEkycDenyContent;
  @Column(name = "ONBOARDING_DATA")
  private Clob onboardingData;
  @Column(name = "CLIENTKEY")
  private String clientkey;
  @Column(name = "CONTRACT_PAYLOAD")
  private Clob contractPayload;
  @Column(name = "HAS_PERM_BOND_PT")
  private BigDecimal hasPermBondPt;
  @Column(name = "SUB_FLOW_OPEN_ACCOUNT")
  private BigDecimal subFlowOpenAccount;
  @Column(name = "IS_FOREIGN_PHONE")
  private BigDecimal isForeignPhone;
  @Column(name = "AVATAR_URL")
  private String avatarUrl;
  @Column(name = "SIGN_CLOSE_CONTRACT")
  private BigDecimal signCloseContract;
  @Column(name = "PHONE_CODE")
  private String phoneCode;
  @Column(name = "KYC_LEVEL")
  private BigDecimal kycLevel;
  @Column(name = "OPEN_SOURCE")
  private String openSource;

  private static final String DATA_EMAIL = "email";
  private static final String DATA_xxxxID = "xxxxId";
  private static final String DATA_USERNAME = "username";
  private static final String DATA_PHONE = "phone";
  private static final String QUERY_BY_PHONE = "from xxxxUser a where a.phone=:phone";

  @Step

  public static xxxxUser getById(BigDecimal id) {
    CAS.casConnection.getSession().clear();
    Query<xxxxUser> query = CAS.casConnection.getSession().createQuery(
      "from xxxxUser a where a.id=:id", xxxxUser.class);
    query.setParameter("id", id);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      logger.error(e.getMessage());
      return new xxxxUser();
    }
  }

  public static xxxxUser getByxxxxId(String xxxxId) {
    CAS.casConnection.getSession().clear();
    Query<xxxxUser> query = CAS.casConnection.getSession().createQuery(
      "from xxxxUser a where a.xxxxid=:xxxxId", xxxxUser.class);
    query.setParameter(DATA_xxxxID, xxxxId);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new xxxxUser();
    }
  }

  public static xxxxUser getByUserName(String username) {
    CAS.casConnection.getSession().clear();
    Query<xxxxUser> query = CAS.casConnection.getSession().createQuery(
      "from xxxxUser a where a.username=:username", xxxxUser.class);
    query.setParameter(DATA_USERNAME, username);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new xxxxUser();
    }
  }

  public static List<xxxxUser> getListByUserName(String username) {
    Query<xxxxUser> query = CAS.casConnection.getSession().createQuery(
      "from xxxxUser a where a.username=:username", xxxxUser.class);
    query.setParameter(DATA_USERNAME, username);
    return query.getResultList();

  }

  public static xxxxUser getByEmail(String email) {
    Query<xxxxUser> query = CAS.casConnection.getSession().createQuery(
      "from xxxxUser a where a.email=:email", xxxxUser.class);
    query.setParameter(DATA_EMAIL, email);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new xxxxUser();
    }
  }

  public static List<xxxxUser> getListByEmail(String email) {
    Query<xxxxUser> query = CAS.casConnection.getSession().createQuery(
      "from xxxxUser a where a.email=:email", xxxxUser.class);
    query.setParameter(DATA_EMAIL, email);
    return query.getResultList();
  }

  public static xxxxUser getByPhoneNumber(String phone) {
    Query<xxxxUser> query = CAS.casConnection.getSession().createQuery(
      QUERY_BY_PHONE, xxxxUser.class);
    query.setParameter(DATA_PHONE, phone);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new xxxxUser();
    }

  }

  public static List<xxxxUser> getListByPhoneNumber(String phone) {
    Query<xxxxUser> query = CAS.casConnection.getSession().createQuery(
      QUERY_BY_PHONE, xxxxUser.class);
    query.setParameter(DATA_PHONE, phone);
    return query.getResultList();

  }

  public static void deleteByPhone(String phone) {
    try {
      Session session = CAS.casConnection.getSession();
      Transaction trans = session.beginTransaction();

      Query<?> query = session.createQuery("DELETE xxxxUser WHERE phone=:phone");
      query.setParameter(DATA_PHONE, phone);
      query.executeUpdate();
      trans.commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public static void updateDocCusSign(String xxxxId, String docusignStatus) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    org.hibernate.query.Query query = casConnection.getSession().createQuery(
      "Update xxxxUser a set a.docusignStatus =:docusignStatus where a.xxxxid=:xxxxId");
    query.setParameter(DATA_xxxxID, xxxxId);
    query.setParameter("docusignStatus", new BigDecimal(docusignStatus));
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  public static void updateNameGender(String xxxxId, String firstName, String lastName, String gender) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    org.hibernate.query.Query query = casConnection.getSession().createQuery(
      "Update xxxxUser a set a.firstname =:firstName, a.lastname =:lastName, a.gender =:gender where a.xxxxid=:xxxxId");
    query.setParameter(DATA_xxxxID, xxxxId);
    query.setParameter("firstName", firstName);
    query.setParameter("lastName", lastName);
    query.setParameter("gender", new BigDecimal(gender));
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  public static xxxxUser getUserByUsernameAndCustype(String username, String custype) {
    Query<xxxxUser> query = CAS.casConnection.getSession().createQuery(
      "from xxxxUser a where a.username=:username and a.custype=:custype", xxxxUser.class);
    query.setParameter(DATA_USERNAME, username);
    query.setParameter("custype", custype);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new xxxxUser();
    }
  }

  public static void updateClientKeyxxxxId(String xxxxId, String clientKey) {
    try {
      Session session = CAS.casConnection.getSession();
      session.getTransaction().begin();

      Query query = session.createQuery("UPDATE xxxxUser a SET a.clientkey=:clientKey WHERE a.xxxxid=:xxxxId");
      query.setParameter("clientKey", clientKey);
      query.setParameter(DATA_xxxxID, xxxxId);
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public void deleteByxxxxId(String xxxxId) {
    try {
      Session session = CAS.casConnection.getSession();
      session.clear();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      Query query = session.createQuery("DELETE xxxxUser WHERE xxxxId=:xxxxId");
      query.setParameter(DATA_xxxxID, xxxxId);
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public void dmlPrepareData(String queryString) {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query query = CAS.casConnection.getSession().createSQLQuery(queryString);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public void setPhoneNoForxxxxId(String username, String phoneNo) {
    try {
      Session session = CAS.casConnection.getSession();
      session.getTransaction().begin();

      Query query = session.createQuery("UPDATE xxxxUser a SET a.phone=:phoneNo WHERE a.username=:username");
      query.setParameter(DATA_USERNAME, username);
      query.setParameter("phoneNo", phoneNo);
      query.executeUpdate();
      session.getTransaction().commit();

    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public xxxxUser getByPhoneNumberOrUsername(String phoneOrUsername, String type) {
    if (type.equals("phoneNo")) {
      Query<xxxxUser> query = CAS.casConnection.getSession().createQuery(
        QUERY_BY_PHONE, xxxxUser.class);
      query.setParameter(DATA_PHONE, phoneOrUsername);
      try {
        return query.getSingleResult();
      } catch (Exception e) {
        return new xxxxUser();
      }
    } else {
      Query<xxxxUser> query = CAS.casConnection.getSession().createQuery(
        "from xxxxUser a where a.username=:userName", xxxxUser.class);
      query.setParameter(DATA_USERNAME, phoneOrUsername);
      try {
        return query.getSingleResult();
      } catch (Exception e) {
        return new xxxxUser();
      }
    }
  }

  public void insert() {
    Session session = CAS.casConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    try {
      session.save(this);
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public static void updateTransferStatusById(String id, String transferStatus) {
    try {
      Session session = CAS.casConnection.getSession();
      session.getTransaction().begin();

      Query query = session.createQuery("UPDATE xxxxUser a SET a.transferStatus=:transferStatus WHERE a.id=:id");
      query.setParameter("transferStatus", new BigDecimal(transferStatus));
      query.setParameter("id", new BigDecimal(id));
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public static void updateEmailByxxxxid(String email, String xxxxid) {
    try {
      Session session = CAS.casConnection.getSession();
      session.getTransaction().begin();

      Query query = session.createQuery("UPDATE xxxxUser a SET a.email =:email WHERE a.xxxxid =:xxxxid");
      query.setParameter(DATA_EMAIL, email);
      query.setParameter(DATA_xxxxID, xxxxid);
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public static void updateEmailByUsername(String email, String username) {
    try {
      Session session = CAS.casConnection.getSession();
      session.getTransaction().begin();

      Query query = session.createQuery("UPDATE xxxxUser a SET a.email =:email WHERE a.username =:username");
      query.setParameter(DATA_EMAIL, email);
      query.setParameter(DATA_USERNAME, username);
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public static void updateCusType(String custype, String username) {
    try {
      Session session = CAS.casConnection.getSession();
      session.clear();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }

      Query<?> query = session.createQuery("UPDATE xxxxUser a SET a.custype=:custype WHERE a.username=:username");
      query.setParameter("custype", new BigDecimal(custype));
      query.setParameter(DATA_USERNAME, username);
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public static void updateAccountStatus(String username, String accountStatus) {
    try {
      Session session = CAS.casConnection.getSession();
      session.clear();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }

      Query<?> query = session.createQuery("UPDATE xxxxUser a SET a.accountStatus=:accountStatus WHERE a.username=:username");
      query.setParameter("accountStatus", new BigDecimal(accountStatus));
      query.setParameter(DATA_USERNAME, username);
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }


  public static void updateSignCloseContract(String username, String signCloseContract) {
    try {
      Session session = CAS.casConnection.getSession();
      session.clear();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }

      Query<?> query = session.createQuery("UPDATE xxxxUser a SET a.signCloseContract=:signCloseContract WHERE a.username=:username");
      query.setParameter("signCloseContract", new BigDecimal(signCloseContract));
      query.setParameter(DATA_USERNAME, username);
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public String getFullName() {
    return lastname + " " + firstname;
  }



}