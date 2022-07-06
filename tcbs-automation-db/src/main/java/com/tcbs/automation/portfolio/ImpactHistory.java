package com.tcbs.automation.portfolio;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.*;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "IMPACT_HISTORY")
public class ImpactHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "HISTORY_ID")
  private String historyId;
  @Column(name = "SOURCE")
  private String source;
  @Column(name = "ACCOUNT_IMPACT")
  private String accountImpact;
  @Column(name = "ACTION")
  private String action;
  @Column(name = "VOLUME")
  private String volume;
  @Column(name = "NOTE")
  private String note;
  @Column(name = "CREATE_DATE")
  private Date createDate;
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;

  public static List<ImpactHistory> getBuyHistIdAccSour(String historyId, String accountImpact, String source) {
    Session session = PortfolioSit.porfolioIsailConnection.getSession();
    session.clear();
    Query<ImpactHistory> query = session.createQuery("from ImpactHistory ib where ib.historyId =: historyId and ib.accountImpact =:accountImpact and ib.source =:source order by id desc");
    query.setParameter("historyId", historyId);
    query.setParameter("accountImpact", accountImpact);
    query.setParameter("source", source);
    List<ImpactHistory> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }
}
