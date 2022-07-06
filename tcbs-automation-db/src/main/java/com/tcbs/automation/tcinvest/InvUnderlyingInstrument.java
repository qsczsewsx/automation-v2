package com.tcbs.automation.tcinvest;

import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Entity
@Table(name = "INV_UNDERLYING_INSTRUMENT")
public class InvUnderlyingInstrument {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "UNDERLYING_ID")
  private String underlyingId;
  @Column(name = "INSTRUMENT_ID")
  private String instrumentId;
  @Column(name = "INSTRUMENT_TYPE")
  private String instrumentType;
  @Column(name = "EXCHANGE")
  private String exchange;
  @Column(name = "NAME")
  private String name;
  @Column(name = "CODE")
  private String code;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "ACTIVE")
  private String active;
  @Column(name = "CREATED_DATE")
  private Date createdDate;
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;

  public static void deleteProductByCode(String code) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvUnderlyingInstrument> query = session.createQuery(
      "DELETE FROM InvUnderlyingInstrument ib WHERE ib.code like :code"
    );
    query.setParameter("code", "%" + code + "%");
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static List<InvUnderlyingInstrument> getBuyCode(String code) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    Query<InvUnderlyingInstrument> query = session.createQuery("from InvUnderlyingInstrument ib where ib.code =: code");
    query.setParameter("code", code);
    List<InvUnderlyingInstrument> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }
}
