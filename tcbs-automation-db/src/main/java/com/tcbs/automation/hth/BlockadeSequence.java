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

@Entity
@Table(name = "BLOCKADE_SEQUENCE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlockadeSequence {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @NotNull
  @Column(name = "PARTNER")
  private String partner;
  @NotNull
  @Column(name = "BANK_ACCOUNT_NO")
  private String bankAccountNo;
  @Column(name = "CUSTODY_CODE")
  private String custodyCode;
  @Column(name = "HOLD_SEQUENCE")
  private String holdSequence;
  @Column(name = "AMOUNT")
  private BigDecimal amount;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;

  /**
   * Author Trangntt46
   **/
  public static BlockadeSequence getFromCustodyCodeAndPartner(String custodyCode, String partner, String bankAccountNo) {
    try {
      h2hConnection.getSession().clear();
      Query<BlockadeSequence> query = h2hConnection.getSession().createQuery(
        "from BlockadeSequence where custodyCode=:custodyCode and partner=:partner and bankAccountNo =:bankAccountNo ",
        BlockadeSequence.class
      );
      query.setParameter("custodyCode", custodyCode);
      query.setParameter("partner", partner);
      query.setParameter("bankAccountNo", bankAccountNo);
      return query.getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

}
