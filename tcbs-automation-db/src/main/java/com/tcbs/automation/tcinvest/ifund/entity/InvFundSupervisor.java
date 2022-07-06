package com.tcbs.automation.tcinvest.ifund.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("unchecked")
@Table(name = "INV_FUND_SUPERVISOR")
public class InvFundSupervisor {
  public static Session session;
  @Column(name = "NAME")
  private String name;
  @Id
  @Column(name = "ACCOUNT_NUMBER")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String accountNumber;
  @Column(name = "BRANCH")
  private String branch;
  @Column(name = "BANK_CODE")
  private String bankCode;

  public InvFundSupervisor bankInfo(String bankNumber) {
    Query<InvFundSupervisor> query = session.createQuery("from InvFundSupervisor where accountNumber =: bankNumber");
    query.setParameter("bankNumber", bankNumber);
    List<InvFundSupervisor> result = query.getResultList();
    if (result != null && result.size() > 0) {
      return result.get(0);
    } else {
      return new InvFundSupervisor();
    }
  }
}
