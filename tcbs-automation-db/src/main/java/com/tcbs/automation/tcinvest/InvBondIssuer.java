package com.tcbs.automation.tcinvest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.*;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static com.tcbs.automation.config.coman.ComanKey.BOND_STATIC_ID;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "INV_BOND_ISSUER")
public class InvBondIssuer {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "BOND_ISSUER_ID")
  private Integer bondIssuerId;
  @Column(name = "NAME")
  private String name;
  @Column(name = "CODE")
  private String code;
  @Column(name = "BUSINESS_NO")
  private String businessNo;
  @Column(name = "BUSINESS_DATE")
  private Date businessDate;
  @Column(name = "BUSINESS_ISSUER")
  private String businessIssuer;
  @Column(name = "REPERSENT_USER")
  private String repersentUser;
  @Column(name = "REPERSENT_ROLE")
  private String repersentRole;
  @Column(name = "PHONE")
  private String phone;
  @Column(name = "FAX")
  private String fax;
  @Column(name = "ADDRESS")
  private String address;
  @Column(name = "EMAIL")
  private String email;
  @Column(name = "ACTIVE")
  private Integer active;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "UPDATED_DATE")
  private Timestamp updatedDate;

  @Step
  public static InvBondIssuer getBondIssuerIdByBondIssuerId(Integer bondIssuerId) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    Query<InvBondIssuer> query = session.createQuery("from InvBondIssuer a where a.bondIssuerId = :bondIssuerId", InvBondIssuer.class);
    query.setParameter("bondIssuerId", bondIssuerId);

    return query.getSingleResult();
  }


  public static void deleteByIssuerId(Integer bondIssuerId) {
    Session session = TcInvest.tcInvestDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createQuery(
      "DELETE FROM InvBondIssuer ib WHERE ib.bondIssuerId = :bondIssuerId"
    );
    query.setParameter("bondIssuerId", bondIssuerId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

}
