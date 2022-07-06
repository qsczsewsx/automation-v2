package com.tcbs.automation.inbox;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "mkt_config_table")
public class MktConfigTable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "id")
  private String id;
  @Column(name = "frequency")
  private String frequency;
  @Column(name = "created_at")
  private Timestamp createdAt;
  @Column(name = "updated_at")
  private Timestamp updatedAt;
  @Column(name = "status")
  private String status;
  @Column(name = "reason")
  private String reason;

  public static MktConfigTable getConfig() {
    Session session = Inbox.inboxDbConnection.getSession();
    session.clear();
    Query<MktConfigTable> query = session.createQuery("from MktConfigTable where status = '1'", MktConfigTable.class);
    return query.getSingleResult();
  }

}
