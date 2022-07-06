package com.tcbs.automation.hth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

import static com.tcbs.automation.hth.HthDb.h2hConnection;


/**
 * Author Trangntt46
 **/
@Entity
@Table(name = "BLOCKADE_BALANCE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlockadeBalance {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "CUSTODY_CODE")
  private String custodyCode;
  @Column(name = "SUB_ACCOUNT_NUMBER")
  private String subAccountNumber;
  @Column(name = "HOLD_KEY")
  private String holdKey;
  @Column(name = "AMOUNT")
  private BigDecimal amount;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;


  public static BlockadeBalance getBalance(String custodyCode, String subAccountNumber, String holdKey) {
    try {
      h2hConnection.getSession().clear();
      Query<BlockadeBalance> query = h2hConnection.getSession().createQuery(
        "from BlockadeBalance where custodyCode =:custodyCode and subAccountNumber =:subAccountNumber and holdKey =:holdKey",
        BlockadeBalance.class
      );
      query.setParameter("custodyCode", custodyCode);
      query.setParameter("subAccountNumber", subAccountNumber);
      query.setParameter("holdKey", holdKey);

      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

}
