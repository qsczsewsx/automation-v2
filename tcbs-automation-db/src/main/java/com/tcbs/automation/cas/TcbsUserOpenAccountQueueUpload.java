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
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "TCBS_USER_OPENACCOUNT_QUEUE_UPLOAD")
@Getter
@Setter
public class TcbsUserOpenAccountQueueUpload {
  private static Logger logger = LoggerFactory.getLogger(TcbsUserOpenAccountQueueUpload.class);
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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

  @Step
  public static List<TcbsUserOpenAccountQueueUpload> getFileUploadIdentify(BigDecimal tuoqId) {
    Query<TcbsUserOpenAccountQueueUpload> query = CAS.casConnection.getSession().createQuery(
      "from TcbsUserOpenAccountQueueUpload a where a.tuoqId=:tuoqId", TcbsUserOpenAccountQueueUpload.class);
    query.setParameter("tuoqId", tuoqId);
    return query.getResultList();
  }

  public static void deleteByTuoqID(BigDecimal tuoqId) {
    try {
      Session session = CAS.casConnection.getSession();
      Transaction trans = session.beginTransaction();

      Query<?> query = session.createQuery("DELETE TcbsUserOpenAccountQueueUpload WHERE tuoqId=:tuoqId");
      query.setParameter("tuoqId", tuoqId);
      query.executeUpdate();
      trans.commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }

}
