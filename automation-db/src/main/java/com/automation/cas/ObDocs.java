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
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "OB_DOCS")
@Getter
@Setter
public class ObDocs {
  private static Logger logger = LoggerFactory.getLogger(ObDocs.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "FILE_NAME")
  private String fileName;
  @Column(name = "FILE_TYPE")
  private String fileType;
  @Column(name = "ECM_ID")
  private String ecmId;
  @Column(name = "TASK_ID")
  private BigDecimal taskId;
  @Column(name = "CREATE_DATE")
  private Date createDate;
  @Column(name = "ACTOR")
  private String actor;
  @Column(name = "DOCUMENT_GROUP")
  private String documentGroup;
  @NotNull
  @Column(name = "FILE_SUFFIX")
  private String fileSuffix;
  @Column(name = "DOWNLOAD_ID")
  private String downloadId;
  @Column(name = "USER_ID")
  private BigDecimal userId;

  @Step
  public static ObDocs getByEcmID(String id) {
    CAS.casConnection.getSession().clear();
    Query<ObDocs> query = CAS.casConnection.getSession().createQuery(
      "from ObDocs a where a.ecmId=:id", ObDocs.class);
    query.setParameter("id", id);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new ObDocs();
    }
  }

  public static List<ObDocs> getByTaskID(BigDecimal taskId) {
    CAS.casConnection.getSession().clear();
    Query<ObDocs> query = CAS.casConnection.getSession().createQuery(
      " from ObDocs a where a.taskId=:taskId", ObDocs.class);
    query.setParameter("taskId", taskId);
    return query.getResultList();
  }

  public static List<ObDocs> getListByEcmID(String id) {
    CAS.casConnection.getSession().clear();
    Query<ObDocs> query = CAS.casConnection.getSession().createQuery(
      "from ObDocs a where a.ecmId=:id", ObDocs.class);
    query.setParameter("id", id);
    return query.getResultList();

  }

  public static void deleteNullDocumentGroupByUserId(String userId) {
    try {
      Session session = CAS.casConnection.getSession();
      Transaction trans = session.beginTransaction();

      Query<?> query = session.createQuery("DELETE ObDocs where userId=:userId and documentGroup is null ");
      query.setParameter("userId", new BigDecimal(userId));
      query.executeUpdate();
      trans.commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

}
