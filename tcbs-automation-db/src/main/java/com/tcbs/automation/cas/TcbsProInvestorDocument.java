package com.tcbs.automation.cas;

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

import static com.tcbs.automation.cas.CAS.casConnection;

@Getter
@Setter
@Table(name = "TCBS_PROINVESTOR_DOCUMENT")
@Entity
public class TcbsProInvestorDocument {
  private static Logger logger = LoggerFactory.getLogger(TcbsProInvestorDocument.class);
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
  public static TcbsProInvestorDocument getProinvestorById(BigDecimal id) {
    Query<TcbsProInvestorDocument> query = CAS.casConnection.getSession().createQuery(
      "from TcbsProInvestorDocument a where a.id =: id", TcbsProInvestorDocument.class
    );
    query.setParameter("id", id);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new TcbsProInvestorDocument();
    }
  }

  public static TcbsProInvestorDocument getProinvestorByUserId(String userId) {
    Query<TcbsProInvestorDocument> query = CAS.casConnection.getSession().createQuery(
      "from TcbsProInvestorDocument a where a.userId =: userId", TcbsProInvestorDocument.class
    );
    query.setParameter(DATA_USERID, new BigDecimal(userId));
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new TcbsProInvestorDocument();
    }
  }

  public static TcbsProInvestorDocument getProInvestorByUserIdAndStatus(String userId, String status) {
    Query<TcbsProInvestorDocument> query = CAS.casConnection.getSession().createQuery(
      "from TcbsProInvestorDocument a where a.userId =: userId and a.status =: status", TcbsProInvestorDocument.class);
    query.setParameter(DATA_USERID, new BigDecimal(userId));
    query.setParameter(DATA_STATUS, status);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new TcbsProInvestorDocument();
    }
  }

  public static List<TcbsProInvestorDocument> getListOfProInvestorByStatus(String status) {
    Query<TcbsProInvestorDocument> query = CAS.casConnection.getSession().createQuery(
      "from TcbsProInvestorDocument a where a.status =: status and a.userId!=null", TcbsProInvestorDocument.class
    );

    query.setParameter(DATA_STATUS, status);
    return query.getResultList();
  }

  public static void updateEndDateAndStatusByUserId(String endDate, String status, String userId) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    Query <?> query = casConnection.getSession().createQuery(
      "Update TcbsProInvestorDocument a set a.endDate =:endDate, a.status=:status where a.userId=:userId");
    query.setParameter("endDate", Timestamp.valueOf(LocalDateTime.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
    query.setParameter(DATA_STATUS, status);
    query.setParameter(DATA_USERID, new BigDecimal(userId));
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  public static void deleteProInvestorByUserIdAndStatus(String userId, String status) {
    Session session = CAS.casConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createQuery("DELETE TcbsProInvestorDocument WHERE userId=:userId and status=:status");
    query.setParameter(DATA_USERID, new BigDecimal(userId));
    query.setParameter(DATA_STATUS, status);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
