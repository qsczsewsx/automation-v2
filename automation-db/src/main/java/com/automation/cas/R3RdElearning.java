package com.automation.cas;

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
  @Column(name = "xxxx_ID")
  private String xxxxId;
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


  private static final String PP_xxxxID = "xxxxId";

  @Step
  public static List<R3RdElearning> getByxxxxId(String xxxxId) {
    CAS.casConnection.getSession().clear();
    Query<R3RdElearning> query = CAS.casConnection.getSession().createQuery(
      "from R3RdElearning a WHERE a.xxxxId=:xxxxId order by a.createdDate desc", R3RdElearning.class);
    query.setParameter(PP_xxxxID, xxxxId);
    return query.getResultList();
  }

  @Step
  public static void deleteByxxxxId(String xxxxId) {
    try {
      Session session = CAS.casConnection.getSession();
      session.clear();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      Query<?> query = CAS.casConnection.getSession().createQuery(
        "DELETE FROM R3RdElearning WHERE xxxxId=:xxxxId");
      query.setParameter(PP_xxxxID, xxxxId);
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public void insert() {
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    Query query = CAS.casConnection.getSession().createNativeQuery(
      "INSERT INTO R3RD_ELEARNING " +
        "(ID, COURSE_CODE, xxxx_ID, FULL_NAME, COURSE_STATUS, CREATED_DATE, UPDATED_DATE, USERNAME, \"ROLE\", BANK_CODE) " +
        "VALUES(R3RD_ELEARNING_SEQUENCE.nextval, ?1, ?2, ?3, ?4, SYSDATE, SYSDATE, ?5, ?6, ?7)"
    );
    query.setParameter(1, courseCode);
    query.setParameter(2, xxxxId);
    query.setParameter(3, fullName);
    query.setParameter(4, courseStatus);
    query.setParameter(5, username);
    query.setParameter(6, role);
    query.setParameter(7, bankCode);
    query.executeUpdate();
    CAS.casConnection.getSession().getTransaction().commit();
  }

  @Step
  public static R3RdElearning getByxxxxIdAndCourseCode(String xxxxId, String courseCode) {
    CAS.casConnection.getSession().clear();
    Query<R3RdElearning> query = CAS.casConnection.getSession().createQuery(
      "from R3RdElearning a WHERE a.xxxxId=:xxxxId and a.courseCode=:courseCode", R3RdElearning.class);
    query.setParameter(PP_xxxxID, xxxxId);
    query.setParameter("courseCode", courseCode);
    return query.getSingleResult();
  }

  public static void updateStatusByxxxxIdAndCourseCode(String xxxxId, String courseCode, String courseStatus) {
    CAS.casConnection.getSession().clear();
    if (!CAS.casConnection.getSession().getTransaction().isActive()) {
      CAS.casConnection.getSession().beginTransaction();
    }
    CAS.casConnection.getSession().createQuery(
      "Update R3RdElearning a set a.courseStatus =:courseStatus where a.xxxxId=:xxxxId and a.courseCode=:courseCode", R3RdElearning.class).setParameter(PP_xxxxID, xxxxId).setParameter("courseCode",
      courseCode).setParameter("courseStatus", new BigDecimal(courseStatus)).executeUpdate();
    CAS.casConnection.getSession().getTransaction().commit();
  }
}

