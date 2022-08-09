package com.tcbs.automation.cas;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

import static com.tcbs.automation.cas.CAS.casConnection;

@Entity
@Table(name = "OCR_DATA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OcrDataHis {
  private static Logger logger = LoggerFactory.getLogger(OcrDataHis.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "USER_DATA")
  private String userData;
  @Column(name = "REF_ID")
  private String refId;
  @Column(name = "STATUS")
  private BigDecimal status;
  @Column(name = "ERROR_CODE")
  private String errorCode;
  @Column(name = "ERROR_MESAGE")
  private String errorMesage;
  @Column(name = "DATE_TIME")
  private Date dateTime;
  @Column(name = "ACTION")
  private String action;
  @Column(name = "USER_DATA_TYPE")
  private String userDataType;

  @Step
  public static OcrDataHis getByTuoqId(String tuoqId) {
    CAS.casConnection.getSession().clear();
    Query<OcrDataHis> query = casConnection.getSession().createQuery(
      "from OcrDataHis a where a.userData =: tuoqId", OcrDataHis.class);
    query.setParameter("tuoqId", new BigDecimal(tuoqId));
    return query.getSingleResult();
  }



}
