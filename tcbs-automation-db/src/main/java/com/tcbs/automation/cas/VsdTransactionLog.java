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

import static com.tcbs.automation.cas.CAS.casConnection;

@Entity
@Table(name = "VSD_TRANSACTION_LOG")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VsdTransactionLog {
  private static Logger logger = LoggerFactory.getLogger(VsdTransactionLog.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "ORDER_ID")
  private BigDecimal orderId;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "SEND_STATUS")
  private String sendStatus;

  @Step
  public static VsdTransactionLog getByTransactionCode(String transactionCode) {
    CAS.casConnection.getSession().clear();
    Query<VsdTransactionLog> query = casConnection.getSession().createNativeQuery(
      "SELECT ID, ORDER_ID, STATUS, SEND_STATUS FROM VSD_TRANSACTION_LOG WHERE TRANSACTION_CODE = :transactionCode", VsdTransactionLog.class);
    query.setParameter("transactionCode", transactionCode);
    System.out.println(query.getQueryString());
    System.out.println(transactionCode);
    return query.getSingleResult();
  }
}
