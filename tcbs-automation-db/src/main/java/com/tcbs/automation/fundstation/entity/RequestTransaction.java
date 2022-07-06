package com.tcbs.automation.fundstation.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "REQUEST_TRANSACTION")
public class RequestTransaction {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "ID")
  private int id;

  @Column(name = "TRANSACTION_DATE")
  private Date transactionDate;

  @Column(name = "TRANSACTION_ACTION")
  private Integer transactionAction;

  @Column(name = "TICKER")
  private String ticker;

  @Column(name = "PRICE")
  private Double price;

  @Column(name = "BROKER_ID")
  private Integer brokerId;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "SUGGESTION")
  private String suggestion;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "PORTFOLIO_CODE")
  private String portfolioCode;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "PAYMENT_DATE")
  private Date paymentDate;

  @Column(name = "EX_DATE")
  private Date exDate;

  @Column(name = "MATURITY_DATE")
  private Date maturityDate;

  @Column(name = "TYPE_APPROVE_TRANSACTION")
  private Integer typeTransaction;

  @Column(name = "CURRENCY_ID")
  private Integer currencyId;

  @Column(name = "REF_CODE")
  private String refCode;

  @Column(name = "REPO_DATE")
  private Date repoDate;

  @Column(name = "REPO_PRICE")
  private Double repoPrice;

  @Column(name = "REF_ID")
  private Integer refId;

  @Column(name = "UNIT")
  private Double unit;

  public static List<RequestTransaction> getListTransGen() {
    Query<RequestTransaction> query = session.createQuery("from RequestTransaction where typeTransaction = 1 order by transactionDate desc, ticker asc, id desc");
    return query.getResultList();
  }

  public static List<RequestTransaction> getListTransProjection(String portfolioCode, List<Integer> listTypeTrans, Date fromDate, Date toDate) {
    Query<RequestTransaction> query = session.createQuery(
      "from RequestTransaction where portfolioCode =:portfolioCode and typeTransaction in :listTypeTrans and transactionDate >=: fromDate and transactionDate <=:toDate and status = 'DRAFT' order by transactionDate desc, ticker asc, id desc");
    query.setParameter("portfolioCode", portfolioCode);
    query.setParameter("listTypeTrans", listTypeTrans);
    query.setParameter("fromDate", fromDate);
    query.setParameter("toDate", toDate);
    return query.getResultList();
  }

  public static List<RequestTransaction> getListTransGenProjectionAmountChange(String portfolioCode, List<Integer> listTypeTrans) {
    Query<RequestTransaction> query = session.createQuery(
      "from RequestTransaction where portfolioCode =:portfolioCode and typeTransaction in :listTypeTrans and status = 'DRAFT' order by transactionDate desc, ticker asc, id desc");
    query.setParameter("portfolioCode", portfolioCode);
    query.setParameter("listTypeTrans", listTypeTrans);
    return query.getResultList();
  }

  public static void deleteTransAutoGenWith(Date fromDate, Date toDate) {
    Query<RequestTransaction> query = session.createQuery("delete RequestTransaction where typeTransaction = 1 and createdTimestamp >=:fromDate and createdTimestamp < :toDate");
    query.setParameter("fromDate", fromDate);
    query.setParameter("toDate", toDate);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
