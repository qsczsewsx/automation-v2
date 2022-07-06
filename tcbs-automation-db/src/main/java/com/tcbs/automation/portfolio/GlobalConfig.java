package com.tcbs.automation.portfolio;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.*;
import java.text.ParseException;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "GLOBAL_CONFIG")
public class GlobalConfig {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @NotNull
  @Column(name = "TYPE")
  private String type;
  @NotNull
  @Column(name = "NAME")
  private String name;
  @NotNull
  @Column(name = "VALUE")
  private String value;

  public static void updateValueInfo(String name, String value) {
    Session session = PortfolioSit.goodsOrchestratorConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query query = session.createSQLQuery(
      String.format("UPDATE GLOBAL_CONFIG SET VALUE = '%s' WHERE NAME = '%s'", value, name));
    query.executeUpdate();
    trans.commit();
  }
}
