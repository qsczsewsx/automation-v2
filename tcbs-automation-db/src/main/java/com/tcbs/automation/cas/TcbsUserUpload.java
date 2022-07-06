package com.tcbs.automation.cas;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@SuppressWarnings("unchecked")
@Table(name = "TCBS_USER_UPLOAD")
@Getter
@Setter
public class TcbsUserUpload {
  private static Logger logger = LoggerFactory.getLogger(TcbsUserUpload.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "USER_ID")
  private BigDecimal userId;
  @Column(name = "FILE_PATH")
  private String filePath;
  @Column(name = "FILE_TYPE")
  private String fileType;
  @Column(name = "FILE_ALIAS")
  private String fileAlias;
  @Column(name = "SINCE")
  private Timestamp since;
  @Column(name = "FILE_OWNER")
  private BigDecimal fileOwner;
  @Column(name = "FILE_UUID")
  private String fileUuid;
  @Column(name = "ORIGINAL_NAME")
  private String originalName;
  @Column(name = "OBJECT_ID")
  private String objectId;

  @Step

  public static List<TcbsUserUpload> getByUserId(String userId) {
    Query<TcbsUserUpload> query = CAS.casConnection.getSession().createQuery(
      "from TcbsUserUpload a where a.userId=:userId order by a.id desc", TcbsUserUpload.class);
    query.setParameter("userId", new BigDecimal(userId));
    return query.getResultList();
  }


}
