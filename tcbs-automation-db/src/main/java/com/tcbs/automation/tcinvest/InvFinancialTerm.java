package com.tcbs.automation.tcinvest;

import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Entity
@Table(name = "INV_FINANCIAL_TERM")
public class InvFinancialTerm {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "FINANCIAL_ID")
  private String financialId;
  @Column(name = "ACTIVE")
  private String active;
  @Column(name = "BROKERAGE")
  private String brokerage;
  @Column(name = "BROKERAGE_TIME")
  private String brokerageTime;
  @Column(name = "BUY_IN_BROKERAGE")
  private String buyInBrokerage;
  @Column(name = "CODE")
  private String code;
  @Column(name = "NAME")
  private String name;
  @Column(name = "CREATED_DATE")
  private Date createdDate;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "UNDERLYING_CLASS")
  private String underlyingClass;
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;

  public static void deleteFinancialTermByCode(String code) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvFinancialTerm> query = session.createQuery(
      "DELETE FROM InvFinancialTerm if WHERE if.code =: code"
    );
    query.setParameter("code", code);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static List<InvFinancialTerm> getBuyCode(String code) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    Query<InvFinancialTerm> query = session.createQuery("from InvFinancialTerm if where if.code =: code");
    query.setParameter("code", code);
    List<InvFinancialTerm> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }
}
