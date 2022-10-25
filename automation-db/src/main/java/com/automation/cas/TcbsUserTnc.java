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

@Entity
@SuppressWarnings("unchecked")
@Table(name = "xxxx_USER_TNC")
@Getter
@Setter
public class xxxxUserTnc {
  private static Logger logger = LoggerFactory.getLogger(xxxxUserTnc.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "USER_ID")
  private BigDecimal userId;
  @Column(name = "TNC_TCB")
  private String tncTcb;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "UPDATED_DATE")
  private Timestamp updatedDate;

  @Step

  public static xxxxUserTnc getByUserId(String userId) {
    Query<xxxxUserTnc> query = CAS.casConnection.getSession().createQuery(
      "from xxxxUserTnc a where a.userId=:userId", xxxxUserTnc.class);
    query.setParameter("userId", new BigDecimal(userId));
    return query.getSingleResult();
  }

}