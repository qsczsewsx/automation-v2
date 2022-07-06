package com.tcbs.automation.inbox;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.tcbs.automation.config.inbox.InboxKey.INBOX_SELECT_MSG_TBL;

@Getter
@Setter
@Entity
@Table(name = "msg_tbl")
public class MsgTbl {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;
  @Column(name = "msg_type")
  private String msgType;
  @Column(name = "content")
  private String content;
  @Column(name = "brief")
  private String brief;
  @Column(name = "content_type")
  private String contentType;
  @Column(name = "is_prompt")
  private Boolean isPrompt;
  @Column(name = "priority")
  private String priority;
  @Column(name = "created_at")
  private Timestamp createdAt;
  @Column(name = "updated_at")
  private Timestamp updatedAt;
  @Column(name = "title")
  private String title;
  @Column(name = "metadata")
  private String metadata;
  @Column(name = "show_outbox")
  private Boolean showOutBox;
  @Column(name = "timeout_outbox")
  private Timestamp timeOutOutBox;
  @Column(name = "expired")
  private Timestamp expired;
  @Column(name = "extra")
  private String extra;

  @Step("get msgId popup, banner, normal - showOutbox == true, status = new")
  public static MsgTbl getMsgId(Long id) {
    Session session = Inbox.inboxDbConnection.getSession();
    session.clear();
    Query<MsgTbl> query = session.createQuery("from MsgTbl where id=:id", MsgTbl.class);
    query.setParameter("id", id);
    return query.getSingleResult();
  }

  public static List<Long> getIdMessageByFromDate(String date) {
    Session session = Inbox.inboxDbConnection.getSession();
    session.clear();
    StringBuilder querySql = new StringBuilder(INBOX_SELECT_MSG_TBL);
    querySql.append(String.format("where created_at > '%s' order by id desc", date));
    Query query = session.createSQLQuery(querySql.toString());
    List rs = query.getResultList();
    return query.getResultList();
  }

////DB Oracle
//  @Step("get top message")
//  public List<Map<String, Object>> getTopMessage(String tcbsid, String status, String msgType, String contentType, int limit, int page) {
//    StringBuilder queryStringBuilder = new StringBuilder();
//    queryStringBuilder.append(
//      "SELECT msg_tbl.id, msg_type, content, brief, content_type,show_outbox, timeout_outbox, expired, extra, is_prompt, priority, created_at, updated_at, status, msg_tbl.title, metadata \r\n");
//    queryStringBuilder.append("FROM msg_tbl \r\n");
//    queryStringBuilder.append("INNER JOIN user_msg_tbl ON msg_tbl.id = user_msg_tbl.msg_id \r\n");
//
//    if (StringUtils.isEmpty(status)) {
//      queryStringBuilder.append(String.format("WHERE user_msg_tbl.tcbsid = '%s' AND user_msg_tbl.is_deleted = false AND (expired > SYSDATE or expired is null) \r\n", tcbsid));
//    } else if (StringUtils.equals(status, "deleted")) {
//      queryStringBuilder.append(String.format("WHERE user_msg_tbl.tcbsid = '%s' AND user_msg_tbl.is_deleted = true \r\n", tcbsid));
//    } else {
//      queryStringBuilder.append(
//        String.format("WHERE user_msg_tbl.tcbsid = '%s' AND user_msg_tbl.status = '%s' AND user_msg_tbl.is_deleted = 0 AND (expired > SYSDATE or expired is null) \r\n", tcbsid, status));
//    }
//
//    if (!StringUtils.isEmpty(msgType)) {
//      queryStringBuilder.append(String.format(" AND msg_tbl.msg_type = '%s'", msgType));
//    }
//
//    if (!StringUtils.isEmpty(contentType)) {
//      queryStringBuilder.append(String.format(" AND msg_tbl.content_type = '%s'", contentType));
//    }
//
//    queryStringBuilder.append(" ORDER BY created_at DESC \r\n");
//    queryStringBuilder.append(String.format(" OFFSET %d ROWS ", page));
//    queryStringBuilder.append(String.format(" FETCH NEXT %d ROWS ONLY \r\n", limit));
//
//    List<Map<String, Object>> result = new ArrayList<>();
//    try {
//      result = Inbox.inboxDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
//        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
//    } catch (Exception ex) {
//      ex.printStackTrace();
//    }
//
//    return result;
//  }

  public static List<Long> getIdMessageByFromDateAndToDate(String fromDate, String toDate) {
    Session session = Inbox.inboxDbConnection.getSession();
    session.clear();
    StringBuilder querySql = new StringBuilder(INBOX_SELECT_MSG_TBL);
    querySql.append(String.format("where created_at > '%s' and created_at < '%s' order by id desc", fromDate, toDate));
    Query query = session.createNativeQuery(querySql.toString());
    return query.getResultList();
  }

  //db Oracle
//  public static List<Long> getIdMessageByFromDate(String date) {
//    Session session = Inbox.inboxDbConnection.getSession();
//    session.clear();
//    StringBuilder querySql = new StringBuilder(INBOX_SELECT_MSG_TBL);
//    querySql.append(String.format("WHERE created_at > to_date('%s','yyyy-mm-dd hh24:mi:ss') order by id desc", date));
//    Query query = session.createSQLQuery(querySql.toString());
//    return query.getResultList();
//  }

  public static List<Long> getIdMessageByFromDateAndTitle(String date, String title) {
    Session session = Inbox.inboxDbConnection.getSession();
    session.clear();
    StringBuilder querySql = new StringBuilder(INBOX_SELECT_MSG_TBL);
    querySql.append(String.format("where created_at > '%s' and REPLACE(LOWER(title), ' ', '') = '%s' order by id desc", date, title));
    Query query = session.createSQLQuery(querySql.toString());
    return query.getResultList();
  }

  //DB oracle
//  public static List<Long> getIdMessageByFromDateAndToDate(String date, String toDate) {
//    Session session = Inbox.inboxDbConnection.getSession();
//    session.clear();
//    StringBuilder querySql = new StringBuilder(INBOX_SELECT_MSG_TBL);
//    querySql.append(String.format("WHERE created_at > to_date('%s','yyyy-mm-dd hh24:mi:ss') and created_at < to_date('%s','yyyy-mm-dd hh24:mi:ss') order by id desc", date, toDate));
//    Query query = session.createSQLQuery(querySql.toString());
//    return query.getResultList();

  public static List<Long> getIdMessageByFromDateAndContent(String date, String content) {
    Session session = Inbox.inboxDbConnection.getSession();
    session.clear();
    StringBuilder querySql = new StringBuilder(INBOX_SELECT_MSG_TBL);
    querySql.append(String.format("WHERE created_at > to_date('%s','yyyy-mm-dd hh24:mi:ss') and REPLACE(LOWER(title), ' ', '') like LOWER('%s') order by id desc", date, content + "%"));
    Query query = session.createSQLQuery(querySql.toString());
    return query.getResultList();
  }

  public static List<Long> getIdMessage3MonthAgo(String date) {
    Session session = Inbox.inboxDbConnection.getSession();
    session.clear();
    StringBuilder querySql = new StringBuilder(INBOX_SELECT_MSG_TBL);
    //DB oracle
    //querySql.append(String.format("where CREATED_AT <= ADD_MONTHS(trunc(sysdate), -3) -1"));
    querySql.append(String.format("WHERE created_at <= '%s'", date));
    Query query = session.createSQLQuery(querySql.toString());
    return query.getResultList();
  }

  @Step("get top message")
  public List<Map<String, Object>> getTopMessage(String tcbsid, String status, String msgType, String contentType, int limit, int page) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(
      "SELECT msg_tbl.id, msg_type, content, brief, content_type,show_outbox, timeout_outbox, expired, extra, is_prompt, priority, created_at, updated_at, status, msg_tbl.title, metadata \r\n");
    queryStringBuilder.append("FROM msg_tbl \r\n");
    queryStringBuilder.append("INNER JOIN user_msg_tbl ON msg_tbl.id = user_msg_tbl.msg_id \r\n");

    if (StringUtils.isEmpty(status)) {
      queryStringBuilder.append(String.format("WHERE user_msg_tbl.tcbsid = '%s' AND user_msg_tbl.is_deleted = false AND (expired > CURRENT_DATE or expired is null) \r\n", tcbsid));
    } else if (StringUtils.equals(status, "deleted")) {
      queryStringBuilder.append(String.format("WHERE user_msg_tbl.tcbsid = '%s' AND user_msg_tbl.is_deleted = true \r\n", tcbsid));
    } else {
      queryStringBuilder.append(
        String.format("WHERE user_msg_tbl.tcbsid = '%s' AND user_msg_tbl.status = '%s' AND user_msg_tbl.is_deleted = false AND (expired > CURRENT_DATE or expired is null) \r\n", tcbsid, status));
    }

    if (!StringUtils.isEmpty(msgType)) {
      queryStringBuilder.append(String.format(" AND msg_tbl.msg_type = '%s'", msgType));
    }

    if (!StringUtils.isEmpty(contentType)) {
      queryStringBuilder.append(String.format(" AND msg_tbl.content_type = '%s'", contentType));
    }

    queryStringBuilder.append(" ORDER BY created_at DESC \r\n");
    queryStringBuilder.append(String.format(" OFFSET %d ROWS ", page));
    queryStringBuilder.append(String.format(" FETCH NEXT %d ROWS ONLY \r\n", limit));

    List<Map<String, Object>> result = new ArrayList<>();
    try {
      Session session = Inbox.inboxDbConnection.getSession();
      session.clear();
      result = session.createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return result;
  }

  @Step
  public MsgTbl getNoti(int i) {
    MsgTbl noti = null;
    try {
      Query<MsgTbl> result = Inbox.inboxDbConnection.getSession().createQuery("FROM MsgTbl ORDER BY createdAt DESC", MsgTbl.class)
        .setMaxResults(i + 1);
      noti = result.getResultList().get(i);
    } catch (ArrayIndexOutOfBoundsException e) {
      e.printStackTrace();
    }
    return noti;
  }


}