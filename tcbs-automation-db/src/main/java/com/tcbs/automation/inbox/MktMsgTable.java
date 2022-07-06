package com.tcbs.automation.inbox;
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
@Table(name = "mkt_msg_table")
public class MktMsgTable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "id")
  private Long id;
  @Column(name = "content")
  private String content;
  @Column(name = "content_type")
  private String contentType;
  @Column(name = "metadata")
  private String metadata;
  @Column(name = "extra")
  private String extra;
  @Column(name = "brief")
  private String brief;
  @Column(name = "created_at")
  private Timestamp createdAt;
  @Column(name = "updated_at")
  private Timestamp updatedAt;
  @Column(name = "status")
  private String status;
  @Column(name = "reason")
  private String reason;

  public static MktMsgTable getMKTMsgId(Long id) {
    Session session = Inbox.inboxDbConnection.getSession();
    session.clear();
    Query<MktMsgTable> query = session.createQuery("from MktMsgTable where id=:id", MktMsgTable.class);
    query.setParameter("id", id);
    return query.getSingleResult();
  }
  public static List<MktMsgTable> getListMKTMsgId() {
    Session session = Inbox.inboxDbConnection.getSession();
    session.clear();
    Query<MktMsgTable> query = session.createQuery("from MktMsgTable where status = '1' order by id desc", MktMsgTable.class);
    return query.getResultList();
  }



}
