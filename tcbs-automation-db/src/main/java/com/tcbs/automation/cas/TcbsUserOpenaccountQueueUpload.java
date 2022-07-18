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
import java.util.Date;

import static com.tcbs.automation.cas.CAS.casConnection;

@Entity
@Table(name = "TCBS_USER_OPENACCOUNT_QUEUE")
@Getter
@Setter
public class TcbsUserOpenaccountQueueUpload {
  private static Logger logger = LoggerFactory.getLogger(TcbsReferralData.class);
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
  public static TcbsUserOpenaccountQueueUpload getFileUploadIdentify(String tuoqId) {
    Query<TcbsUserOpenaccountQueueUpload> query = CAS.casConnection.getSession().createQuery(
      "from TcbsUserOpenaccountQueueUpload a where a.tuoqId=:tuoqId", TcbsUserOpenaccountQueueUpload.class);
    query.setParameter("tuoqId", tuoqId);
    return query.getSingleResult();
  }

}
