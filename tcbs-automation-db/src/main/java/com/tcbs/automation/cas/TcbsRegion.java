package com.tcbs.automation.cas;

import lombok.*;
import net.thucydides.core.annotations.Step;
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
@Table(name = "TCBS_REGION")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TcbsRegion {
  private static Logger logger = LoggerFactory.getLogger(TcbsRegion.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "CODE")
  private String code;
  @Column(name = "NAME")
  private String name;
  @Column(name = "CREATED_DATE")
  private Date createdDate;
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;
  @Column(name = "PHONE_CODE")
  private String phoneCode;
  @Column(name = "NATIONAL_FLAG_PATH")
  private String nationalFlagPath;
  @Column(name = "RANKING")
  private String ranking;

  @Step
  public static List<TcbsRegion> getAllRegion() {
    CAS.casConnection.getSession().clear();
    Query<TcbsRegion> query = casConnection.getSession().createQuery(
      " from TcbsRegion a order by a.ranking asc,a.name asc" , TcbsRegion.class);
    return query.getResultList();
  }

}