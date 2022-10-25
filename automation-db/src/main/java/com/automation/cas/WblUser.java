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
import java.sql.Timestamp;

@Entity
@Table(name = "WBL_USER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WblUser {
  private static Logger logger = LoggerFactory.getLogger(WblUser.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "FULL_NAME")
  private String fullName;
  @Column(name = "ID_NUMBER")
  private String idNumber;
  @Column(name = "ADDRESS")
  private String address;
  @Column(name = "CREATED_DATETIME")
  private Timestamp createdDatetime;
  @Column(name = "UPDATED_DATETIME")
  private Timestamp updatedDatetime;
  @Column(name = "STATUS")
  private String status;

  @Step

  public static WblUser getByIdNumber(String idNumber) {
    Query<WblUser> query = CAS.casConnection.getSession().createQuery(
      "from WblUser a where a.idNumber=:idNumber", WblUser.class);
    query.setParameter("idNumber", idNumber);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new WblUser();
    }
  }

}
