package com.tcbs.automation.cas;

import lombok.*;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.cas.CAS.casConnection;

@Entity
@Table(name = "R3RD_ELEARNING")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class R3RdElearning {
  private static Logger logger = LoggerFactory.getLogger(R3RdElearning.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "COURSE_CODE")
  private String courseCode;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "FULL_NAME")
  private String fullName;
  @Column(name = "COURSE_STATUS")
  private BigDecimal courseStatus;
  @Column(name = "CREATED_DATE")
  private Date createdDate;
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;
  @Column(name = "USERNAME")
  private String username;
  @Column(name = "ROLE")
  private String role;
  @Column(name = "BANK_CODE")
  private String bankCode;


  private static final String PP_TCBSID = "tcbsId";

  @Step
  public static List<R3RdElearning> getByTcbsId(String tcbsId) {
    CAS.casConnection.getSession().clear();
    Query<R3RdElearning> query = CAS.casConnection.getSession().createQuery(
      "from R3RdElearning a WHERE a.tcbsId=:tcbsId order by a.createdDate desc", R3RdElearning.class);
    query.setParameter(PP_TCBSID, tcbsId);
    return query.getResultList();
  }

  @Step
  public static void deleteByTcbsId(String tcbsId) {
    try {
      Session session = CAS.casConnection.getSession();
      session.clear();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      Query<?> query = CAS.casConnection.getSession().createQuery(
        "DELETE FROM R3RdElearning WHERE tcbsId=:tcbsId");
      query.setParameter(PP_TCBSID, tcbsId);
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public void insert() {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    Query query = casConnection.getSession().createNativeQuery(
      "INSERT INTO R3RD_ELEARNING " +
        "(ID, COURSE_CODE, TCBS_ID, FULL_NAME, COURSE_STATUS, CREATED_DATE, UPDATED_DATE, USERNAME, \"ROLE\", BANK_CODE) " +
        "VALUES(R3RD_ELEARNING_SEQUENCE.nextval, ?1, ?2, ?3, ?4, SYSDATE, SYSDATE, ?5, ?6, ?7)"
    );
    query.setParameter(1, courseCode);
    query.setParameter(2, tcbsId);
    query.setParameter(3, fullName);
    query.setParameter(4, courseStatus);
    query.setParameter(5, username);
    query.setParameter(6, role);
    query.setParameter(7, bankCode);
    query.executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }

  @Step
  public static R3RdElearning getByTcbsIdAndCourseCode(String tcbsId, String courseCode) {
    CAS.casConnection.getSession().clear();
    Query<R3RdElearning> query = CAS.casConnection.getSession().createQuery(
      "from R3RdElearning a WHERE a.tcbsId=:tcbsId and a.courseCode=:courseCode", R3RdElearning.class);
    query.setParameter(PP_TCBSID, tcbsId);
    query.setParameter("courseCode", courseCode);
    return query.getSingleResult();
  }

  public static void updateStatusByTcbsIdAndCourseCode(String tcbsId, String courseCode, String courseStatus) {
    casConnection.getSession().clear();
    if (!casConnection.getSession().getTransaction().isActive()) {
      casConnection.getSession().beginTransaction();
    }
    casConnection.getSession().createQuery(
      "Update R3RdElearning a set a.courseStatus =:courseStatus where a.tcbsId=:tcbsId and a.courseCode=:courseCode", R3RdElearning.class).setParameter(PP_TCBSID, tcbsId).setParameter("courseCode",
      courseCode).setParameter("courseStatus", new BigDecimal(courseStatus)).executeUpdate();
    casConnection.getSession().getTransaction().commit();
  }
}

