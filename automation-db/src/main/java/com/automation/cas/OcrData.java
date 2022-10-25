package com.automation.cas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "OCR_DATA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OcrData {
  private static Logger logger = LoggerFactory.getLogger(OcrData.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "REF_ID")
  private String refId;
  @Column(name = "CREATED_DATE")
  private Date createdDate;
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;
  @Column(name = "USER_NAME")
  private String userName;
  @Column(name = "USER_PHONE")
  private String userPhone;
  @Column(name = "DOC_TYPE")
  private BigDecimal docType;
  @Column(name = "JSON_RESULT")
  private String jsonResult;
  @Column(name = "JSON_POINT")
  private String jsonPoint;
  @Column(name = "USER_ID")
  private BigDecimal userId;
  @Column(name = "DOB")
  private String dob;
  @Column(name = "GENDER")
  private BigDecimal gender;
  @Column(name = "ID_NUMBER")
  private String idNumber;
  @Column(name = "IDENTITY_TYPE")
  private String identityType;
  @Column(name = "ISSUE_DATE")
  private String issueDate;
  @Column(name = "ISSUE_PLACE")
  private String issuePlace;
  @Column(name = "NATIONALITY")
  private String nationality;
  @Column(name = "EXPIRE_DATE")
  private String expireDate;
  @Column(name = "PERMANENT_ADDRESS")
  private String permanentAddress;
  @Column(name = "HOMETOWN_ADDRESS")
  private String hometownAddress;
  @Column(name = "FULL_NAME")
  private String fullName;
  @Column(name = "STATUS")
  private BigDecimal status;
  @Column(name = "ISSUE_PLACE_CODE")
  private String issuePlaceCode;
  @Column(name = "RETRY_COUNT")
  private BigDecimal retryCount;
  @Column(name = "TUOQ_ID")
  private BigDecimal tuoqId;

  @Step
  public static void updateStatus(String refId, String status) {
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    Query<?> query = CAS.casConnection.getSession().createQuery(
      "Update OcrData a set a.status=:status where a.refId=:refId");
    query.setParameter("refId", refId);
    query.setParameter("status", status);
    query.executeUpdate();
    CAS.casConnection.getSession().getTransaction().commit();
  }

  public static List<OcrData> getByTuoqId(String tuoqId) {
    CAS.casConnection.getSession().clear();
    Query<OcrData> query = CAS.casConnection.getSession().createQuery(
      "from OcrData a where a.tuoqId =: tuoqId order by id desc", OcrData.class);
    query.setParameter("tuoqId", new BigDecimal(tuoqId));
    return query.getResultList();
  }

  public static void deleteByTuoqId(BigDecimal tuoqId) {
    try {
      Session session = CAS.casConnection.getSession();
      Transaction trans = session.beginTransaction();

      Query<?> query = session.createQuery("DELETE OcrData WHERE tuoqId=:tuoqId");
      query.setParameter("tuoqId", tuoqId);
      query.executeUpdate();
      trans.commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }


}
