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

@Entity
@SuppressWarnings("unchecked")
@Table(name = "TCBS_USER_TNC")
@Getter
@Setter
public class TcbsUserTnc {
  private static Logger logger = LoggerFactory.getLogger(TcbsUserTnc.class);
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

  public static TcbsUserTnc getByUserId(String userId) {
    Query<TcbsUserTnc> query = CAS.casConnection.getSession().createQuery(
      "from TcbsUserTnc a where a.userId=:userId", TcbsUserTnc.class);
    query.setParameter("userId", new BigDecimal(userId));
    return query.getSingleResult();
  }

}