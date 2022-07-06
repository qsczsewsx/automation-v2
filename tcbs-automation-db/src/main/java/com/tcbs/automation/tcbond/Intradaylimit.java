package com.tcbs.automation.tcbond;

import lombok.Getter;
import lombok.ToString;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Getter
@ToString
@Table(name = "INTRADAYLIMIT")
public class Intradaylimit {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  @NotNull
  @Column(name = "ID")
  private Integer id;
  @Column(name = "INTRADAYDATE")
  @Temporal(TemporalType.DATE)
  private Date intradayDate;
  @Column(name = "CREATEDDATE")
  @Temporal(TemporalType.DATE)
  private Date createdDate;
  @Column(name = "LIMITATIONTYPE")
  private Integer limitationtype;
  @Column(name = "ISSUERID")
  private Integer issuerid;
  @Column(name = "BONDPRODUCTID")
  private Integer bondproductid;
  @Column(name = "ACTUALPRICE")
  private BigDecimal actualprice;
  @Column(name = "\"Comment\"")
  private String comment;


  public Intradaylimit setId(Integer id) {
    this.id = id;
    return this;
  }


  public Intradaylimit setIntradayDate(Date intradayDate) {
    this.intradayDate = intradayDate;
    return this;
  }


  public Intradaylimit setCreatedDate(Date createddate) {
    this.createdDate = createddate;
    return this;
  }


  public Intradaylimit setLimitationtype(Integer limitationtype) {
    this.limitationtype = limitationtype;
    return this;
  }


  public Intradaylimit setIssuerid(Integer issuerid) {
    this.issuerid = issuerid;
    return this;
  }


  public Intradaylimit setBondproductid(Integer bondproductid) {
    this.bondproductid = bondproductid;
    return this;
  }


  public Intradaylimit setActualprice(BigDecimal actualprice) {
    this.actualprice = actualprice;
    return this;
  }


  public Intradaylimit setComment(String comment) {
    this.comment = comment;
    return this;
  }

  public void deleteById() {
    Session session = TcBond.tcBondDbConnection.getSession();

    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<Intradaylimit> query = session.createQuery(
      "DELETE FROM Intradaylimit WHERE id=:id "
    );
    query.setParameter("id", this.getId());
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public void insert() {
    Session session = TcBond.tcBondDbConnection.getSession();
    session.clear();
    session.beginTransaction();
    session.save(this);
    session.getTransaction().commit();
  }

}
