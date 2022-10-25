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
import java.util.List;

@Entity
@Table(name = "xxxx_USER_OPENACCOUNT_QUEUE_UPLOAD")
@Getter
@Setter
public class xxxxUserOpenAccountQueueUpload {
  private static Logger logger = LoggerFactory.getLogger(xxxxUserOpenAccountQueueUpload.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "TUOQ_ID")
  private BigDecimal tuoqId;
  @Column(name = "FILE_TYPE")
  private String fileType;
  @Column(name = "FILE_NAME")
  private String fileName;
  @Column(name = "OBJECT_ID")
  private String objectId;

  private static final String DATA_TUOQID = "tuoqId";

  @Step
  public static List<xxxxUserOpenAccountQueueUpload> getByTuoqIdAndFileType(String tuoqId, String fileType) {
    Query<xxxxUserOpenAccountQueueUpload> query = CAS.casConnection.getSession().createQuery(
      "from xxxxUserOpenAccountQueueUpload a where a.tuoqId=:tuoqId and a.fileType=:fileType", xxxxUserOpenAccountQueueUpload.class);
    query.setParameter(DATA_TUOQID, new BigDecimal(tuoqId));
    query.setParameter("fileType", fileType);
    return query.getResultList();
  }

  public static void deleteByTuoqIdAndFileType(String tuoqId, String fileType) {
    try {
      Session session = CAS.casConnection.getSession();
      session.clear();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      Query<?> query = session.createNativeQuery("DELETE FROM xxxx_USER_OPENACCOUNT_QUEUE_UPLOAD WHERE TUOQ_ID =:tuoqId AND FILE_TYPE =:fileType");
      query.setParameter(DATA_TUOQID, new BigDecimal(tuoqId));
      query.setParameter("fileType", fileType);
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

  public static List<xxxxUserOpenAccountQueueUpload> getFileUploadIdentify(BigDecimal tuoqId) {
    Query<xxxxUserOpenAccountQueueUpload> query = CAS.casConnection.getSession().createQuery(
      "from xxxxUserOpenAccountQueueUpload a where a.tuoqId=:tuoqId", xxxxUserOpenAccountQueueUpload.class);
    query.setParameter(DATA_TUOQID, tuoqId);
    return query.getResultList();
  }

  public static void deleteByTuoqID(BigDecimal tuoqId) {
    try {
      Session session = CAS.casConnection.getSession();
      Transaction trans = session.beginTransaction();

      Query<?> query = session.createQuery("DELETE xxxxUserOpenAccountQueueUpload WHERE tuoqId=:tuoqId");
      query.setParameter(DATA_TUOQID, tuoqId);
      query.executeUpdate();
      trans.commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

}