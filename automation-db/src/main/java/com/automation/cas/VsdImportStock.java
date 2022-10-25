package com.automation.cas;

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
@Table(name = "VSD_IMPORT_STOCK")
@Getter
@Setter
public class VsdImportStock {
  private static Logger logger = LoggerFactory.getLogger(VsdImportStock.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "FILE_NAME")
  private String fileName;
  @Column(name = "CREATED_DATETIME")
  private Timestamp createdDatetime;
  @Column(name = "CUSTODY_CODE")
  private String custodyCode;
  @Column(name = "NAME")
  private String name;
  @Column(name = "TYPE")
  private String type;
  @Column(name = "ID_NUMBER1")
  private String idNumber1;
  @Column(name = "ID_NUMBER2")
  private String idNumber2;
  @Column(name = "ID_TYPE")
  private String idType;
  @Column(name = "IDDATE")
  private Timestamp iddate;

  @Step
  public static List<VsdImportStock> getByFileName(String fileName) {
    CAS.casConnection.getSession().clear();
    Query<VsdImportStock> query = CAS.casConnection.getSession().createQuery(
      "from VsdImportStock a where a.fileName=:fileName order by a.id", VsdImportStock.class);
    query.setParameter("fileName", fileName);

    return query.getResultList();

  }


}
