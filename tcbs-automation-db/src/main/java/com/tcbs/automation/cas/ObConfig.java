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

@Entity
@Table(name = "OB_CONFIG")
@Getter
@Setter
public class ObConfig {
  private static Logger logger = LoggerFactory.getLogger(ObConfig.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @NotNull
  @Column(name = "TYPE")
  private String type;
  @NotNull
  @Column(name = "CODE")
  private String code;
  @NotNull
  @Column(name = "VALUE")
  private String value;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "STATUS")
  private String status;

  @Step
  public static ObConfig getByCode(String code) {
    CAS.casConnection.getSession().clear();
    Query<ObConfig> query = CAS.casConnection.getSession().createQuery(
      "from ObConfig a where a.code=:code", ObConfig.class);
    query.setParameter("code", code);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new ObConfig();
    }
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
