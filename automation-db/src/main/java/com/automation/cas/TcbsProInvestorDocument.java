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
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
@Table(name = "xxxx_PROINVESTOR_DOCUMENT")
@Entity
public class xxxxProInvestorDocument {
  private static Logger logger = LoggerFactory.getLogger(xxxxProInvestorDocument.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "USER_ID")
  private BigDecimal userId;
  @Column(name = "DOCUMENT_TYPE")
  private String documentType;
  @Column(name = "START_DATE")
  private Timestamp startDate;
  @Column(name = "END_DATE")
  private Timestamp endDate;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "UPDATED_DATE")
  private Timestamp updatedDate;
  @Column(name = "PAPER_TYPE")
  private String paperType;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "REASON")
  private String reason;
  @Column(name = "ACTOR")
  private String actor;
  @Column(name = "IN_ACTIVE_DATE")
  private Timestamp inActiveDate;

  private static final String DATA_USERID = "userId";
  private static final String DATA_STATUS = "status";

  @Step
  public static xxxxProInvestorDocument getProinvestorById(BigDecimal id) {
    Query<xxxxProInvestorDocument> query = CAS.casConnection.getSession().createQuery(
      "from xxxxProInvestorDocument a where a.id =: id", xxxxProInvestorDocument.class
    );
    query.setParameter("id", id);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new xxxxProInvestorDocument();
    }
  }

  public static xxxxProInvestorDocument getProinvestorByUserId(String userId) {
    Query<xxxxProInvestorDocument> query = CAS.casConnection.getSession().createQuery(
      "from xxxxProInvestorDocument a where a.userId =: userId", xxxxProInvestorDocument.class
    );
    query.setParameter(DATA_USERID, new BigDecimal(userId));
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new xxxxProInvestorDocument();
    }
  }

  public static xxxxProInvestorDocument getProInvestorByUserIdAndStatus(String userId, String status) {
    Query<xxxxProInvestorDocument> query = CAS.casConnection.getSession().createQuery(
      "from xxxxProInvestorDocument a where a.userId =: userId and a.status =: status", xxxxProInvestorDocument.class);
    query.setParameter(DATA_USERID, new BigDecimal(userId));
    query.setParameter(DATA_STATUS, status);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new xxxxProInvestorDocument();
    }
  }

  public static List<xxxxProInvestorDocument> getListOfProInvestorByStatus(String status) {
    Query<xxxxProInvestorDocument> query = CAS.casConnection.getSession().createQuery(
      "from xxxxProInvestorDocument a where a.status =: status and a.userId!=null", xxxxProInvestorDocument.class
    );

    query.setParameter(DATA_STATUS, status);
    return query.getResultList();
  }

  public static void updateEndDateAndStatusByUserId(String endDate, String status, String userId) {
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    Query <?> query = CAS.casConnection.getSession().createQuery(
      "Update xxxxProInvestorDocument a set a.endDate =:endDate, a.status=:status where a.userId=:userId");
    query.setParameter("endDate", Timestamp.valueOf(LocalDateTime.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
    query.setParameter(DATA_STATUS, status);
    query.setParameter(DATA_USERID, new BigDecimal(userId));
    query.executeUpdate();
    CAS.casConnection.getSession().getTransaction().commit();
  }

  public static void deleteProInvestorByUserIdAndStatus(String userId, String status) {
    Session session = CAS.casConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createQuery("DELETE xxxxProInvestorDocument WHERE userId=:userId and status=:status");
    query.setParameter(DATA_USERID, new BigDecimal(userId));
    query.setParameter(DATA_STATUS, status);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
