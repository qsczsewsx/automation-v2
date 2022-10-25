package com.automation.cas;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@SuppressWarnings("unchecked")
@Table(name = "xxxx_USER_CHECK")
@Getter
@Setter
public class xxxxUserCheck {
  private static Logger logger = LoggerFactory.getLogger(xxxxUserCheck.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @NotNull
  @Column(name = "USERNAME")
  private String username;
  @Column(name = "CONTRACT_OK")
  private BigDecimal contractOk;

  @Step

  public static xxxxUserCheck getByUserName(String username, String contractOk) {
    Query<xxxxUserCheck> query = CAS.casConnection.getSession().createQuery(
      "from xxxxUserCheck a where a.username=:username and a.contractOk=:contractOk", xxxxUserCheck.class);
    query.setParameter("username", username);
    query.setParameter("contractOk", new BigDecimal(contractOk));
    return query.getSingleResult();
  }

  public static void deleteByUserName(String username, String contractOk) {
    try {
      Session session = CAS.casConnection.getSession();
      session.clear();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }

      Query<?> query = session.createQuery("DELETE xxxxUserCheck WHERE username=:username and contractOk=:contractOk");
      query.setParameter("username", username);
      query.setParameter("contractOk", new BigDecimal(contractOk));
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      logger.info(e.getMessage());
    }
  }


}