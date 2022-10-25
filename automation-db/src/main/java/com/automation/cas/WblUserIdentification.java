package com.automation.cas;

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
import java.util.List;

@Entity
@Table(name = "WBL_USER_IDENTIFICATION")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WblUserIdentification {

  private static Logger logger = LoggerFactory.getLogger(WblUserIdentification.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "WBLUSER_ID")
  private BigDecimal wbluserId;
  @Column(name = "ID_NUMBER")
  private String idNumber;
  @Column(name = "CREATED_DATETIME")
  private Date createdDatetime;
  @Column(name = "STATUS")
  private String status;

  @Step
  public static List<WblUserIdentification> getByWblUserId(BigDecimal wbluserId) {
    Query<WblUserIdentification> query = CAS.casConnection.getSession().createQuery(
      "from WblUserIdentification a where a.wbluserId=:wbluserId", WblUserIdentification.class);
    query.setParameter("wbluserId", wbluserId);
    return query.getResultList();
  }

  @Step
  public static WblUserIdentification getByIdNumber(String idNumber) {
    Query<WblUserIdentification> query = CAS.casConnection.getSession().createQuery(
      "from WblUserIdentification a where a.idNumber=:idNumber", WblUserIdentification.class);
    query.setParameter("idNumber", idNumber);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new WblUserIdentification();
    }
  }
}
