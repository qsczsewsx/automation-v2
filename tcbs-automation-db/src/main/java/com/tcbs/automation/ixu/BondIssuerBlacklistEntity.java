package com.tcbs.automation.ixu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "BOND_ISSUER_BLACKLIST")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BondIssuerBlacklistEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "ISSUER_ID")
  private Integer issuerID;
  @Column(name = "ISSUER_NAME")
  private String issuerName;
  @Column(name = "CAMPAIGN_ID")
  private Integer campaignID;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  public static BondIssuerBlacklistEntity insert(BondIssuerBlacklistEntity entity) {
    ixuDbConnection.getSession().getTransaction().begin();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
    return entity;
  }

  public static void delete(Integer issuerID) {
    ixuDbConnection.getSession().getTransaction().begin();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete BondIssuerBlacklistEntity a where a.issuerID = :issuerID"
    );
    query.setParameter("issuerID", issuerID);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }
}
