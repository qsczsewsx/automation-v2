package com.tcbs.automation.inbox;

import com.tcbs.automation.ligo.LigoRepository;
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
@Table(name = "MSG_TBL_ARCHIVE")
public class MsgTblArchive {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;
  @NotNull
  @Column(name = "MSG_TYPE")
  private String msgType;
  @NotNull
  @Column(name = "CONTENT")
  private String content;
  @Column(name = "CONTENT_TYPE")
  private String contentType;
  @Column(name = "IS_PROMPT")
  private Boolean isPrompt;
  @Column(name = "PRIORITY")
  private String priority;
  @NotNull
  @Column(name = "CREATED_AT")
  private Timestamp createdAt;
  @Column(name = "UPDATED_AT")
  private Timestamp updatedAt;
  @Column(name = "TITLE")
  private String title;
  @Column(name = "METADATA")
  private String metadata;
  @Column(name = "SHOW_OUTBOX")
  private Boolean showOutbox;
  @Column(name = "TIMEOUT_OUTBOX")
  private Timestamp timeoutOutbox;
  @Column(name = "EXPIRED")
  private Timestamp expired;
  @Column(name = "EXTRA")
  private String extra;
  @Column(name = "BRIEF")
  private String brief;
  @Column(name = "REASON")
  private String reason;

  public static List<Long> getIdMessage3MonthAgo() {
    Session session = Inbox.inboxDbConnection.getSession();
    session.clear();
    StringBuilder querySql = new StringBuilder("SELECT DISTINCT(ID) FROM MSG_TBL_ARCHIVE");
    Query query = session.createSQLQuery(querySql.toString());
    return query.getResultList();
  }

  public static void deleteByid(Long id) {
    Session session = Inbox.inboxDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    StringBuilder querySql = new StringBuilder("DELETE FROM MsgTblArchive ib WHERE ib.id = :id");
    Query<?> query = session.createQuery(querySql.toString());
    query.setParameter("id", id);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
