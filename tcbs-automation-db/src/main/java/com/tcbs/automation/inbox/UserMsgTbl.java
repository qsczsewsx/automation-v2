package com.tcbs.automation.inbox;

import lombok.Getter;
import lombok.Setter;
import lombok.val;
import net.thucydides.core.annotations.Step;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.tcbs.automation.config.inbox.InboxKey.INBOX_TCBS_ID;
import static com.tcbs.automation.config.inbox.InboxKey.INBOX_TCBS_ID_MSG;

@Getter
@Setter
@Entity
@Table(name = "user_msg_tbl")
public class UserMsgTbl {
  @Id
  @Column(name = "id")
  private long id;
  @Column(name = "tcbsid")
  private String tcbsId;
  @Column(name = "code_105c")
  private String code105c;
  @Column(name = "msg_id")
  private long msgId;
  @Column(name = "status")
  private String status;
  @Column(name = "is_deleted")
  private Boolean isDeleted;

  @Step("get msgId by tcbsId")
  public static List<Long> getMsgId(String tcbsId) {
    Query<UserMsgTbl> query = Inbox.inboxDbConnection.getSession().createQuery("from UserMsgTbl where tcbsId=:tcbsId and status = 'new' order by id desc", UserMsgTbl.class);
    query.setParameter(INBOX_TCBS_ID, tcbsId);
    List<UserMsgTbl> userMsgId = query.getResultList();
    List<Long> msgIdList = new ArrayList<>();
    for (int i = 0; i < userMsgId.size(); i++) {
      Long msgId = userMsgId.get(i).getMsgId();
      msgIdList.add(msgId);
    }
    return msgIdList;
  }

  public static List<UserMsgTbl> getMsgReceiverOrderId(Long msgId, String status) {
    String queryString = "";
    if (StringUtils.isEmpty(status)) {
      queryString = "from UserMsgTbl where msgId=:msgId order by id desc";
    } else if (StringUtils.equals(status, "deleted")) {
      queryString = "from UserMsgTbl where msgId=:msgId and isDeleted=true order by id desc";
    } else {
      queryString = "from UserMsgTbl where msgId=:msgId and status=:statusRequest order by id desc";
    }
    Session session = Inbox.inboxDbConnection.getSession();
    session.clear();
    Query<UserMsgTbl> query = session.createQuery(queryString);
    query.setParameter("msgId", msgId);
    if (!StringUtils.isEmpty(status) && !StringUtils.equals(status, "deleted")) {
      query.setParameter("statusRequest", status);
    }
    List<UserMsgTbl> rs = query.getResultList();
    return rs;
  }

  @Step("count message")
  public long countMsg(String tcbsId, String status, String msgType, String contentType) {
    String queryString = "SELECT COUNT(um.id) FROM user_msg_tbl um LEFT JOIN msg_tbl m ON um.msg_id = m.id WHERE 1 =1 ";
    if (StringUtils.isEmpty(status)) {
      queryString += " AND um.tcbsId=:tcbsId AND um.is_deleted = false";
    } else if (StringUtils.equals(status, "deleted")) {
      queryString += " AND um.tcbsId=:tcbsId AND um.is_deleted = true ";
    } else {
      queryString += " AND um.tcbsId=:tcbsId AND um.is_deleted = false AND um.status = :status ";
    }
    if (StringUtils.isNotEmpty(msgType)) {
      queryString += " and m.msg_type = :msgType ";
    }
    if (StringUtils.isNotEmpty(contentType)) {
      queryString += " and m.content_type = :contentType ";
    }
    Query<Object> query = Inbox.inboxDbConnection.getSession().createSQLQuery(queryString);
    query.setParameter(INBOX_TCBS_ID, tcbsId);
    if (!StringUtils.isEmpty(status) && !StringUtils.equals(status, "deleted")) {
      query.setParameter("status", status);
    }
    if (StringUtils.isNotEmpty(msgType)) {
      query.setParameter("msgType", msgType);
    }
    if (StringUtils.isNotEmpty(contentType)) {
      query.setParameter("contentType", contentType);
    }
    val rs = Long.parseLong(query.getSingleResult().toString());
    return rs;
  }

  @Step("get by tcbsid")
  public UserMsgTbl getById(String tcbsId) {
    Query<UserMsgTbl> query = Inbox.inboxDbConnection.getSession().createQuery("from UserMsgTbl where tcbsId=:tcbsId", UserMsgTbl.class);
    query.setParameter(INBOX_TCBS_ID, tcbsId);
    val rs = query.getResultStream().findFirst().orElse(null);
    return rs;
  }

  @Step("get status by msgId")
  public UserMsgTbl getMsgStatus(Long msgId, String tcbsId) {
    Query<UserMsgTbl> query = Inbox.inboxDbConnection.getSession().createQuery("from UserMsgTbl where msgId=:msgId and tcbsId=:tcbsId", UserMsgTbl.class);
    query.setParameter("msgId", msgId);
    query.setParameter(INBOX_TCBS_ID, tcbsId);
    val temp = query.getSingleResult();
    return temp;
  }

  public long countMsgReceiver(Long msgId, String status) {
    String queryString;
    if (StringUtils.isEmpty(status)) {
      queryString = "select count(id) from UserMsgTbl where msgId=:msgId";
    } else if (StringUtils.equals(status, "deleted")) {
      //queryString = "select count(id) from UserMsgTbl where msgId=:msgId and isDeleted=1";
      queryString = "select count(id) from UserMsgTbl where msgId=:msgId and isDeleted=true";
    } else {
      queryString = "select count(id) from UserMsgTbl where msgId=:msgId and status=:statusRequest";
    }
    Session session = Inbox.inboxDbConnection.getSession();
    session.clear();
    Query<Long> query = session.createQuery(queryString, Long.class);
    query.setParameter("msgId", msgId);
    if (!StringUtils.isEmpty(status) && !StringUtils.equals(status, "deleted")) {
      query.setParameter("statusRequest", status);
    }
    val rs = query.getResultStream().findFirst().orElse(Long.valueOf(0));
    return rs;
  }

  public static long countMsgBackEnd(Long msgId) {
    String queryString = "select count(id) from UserMsgTbl where msgId=:msgId and isDeleted=false";
    Query<Long> query = Inbox.inboxDbConnection.getSession().createQuery(queryString, Long.class);
    query.setParameter(INBOX_TCBS_ID_MSG, msgId);
    val rs = query.getResultStream().findFirst().orElse(Long.valueOf(0));
    return rs;
  }
}
