package com.tcbs.automation.tca.message;

import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.annotations.Step;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "stx_msg_newsEN", schema = "dbo")
public class MessagesEn {
  private long messageId;
  private String msgTitle;
  private String msgContent;
  private Byte msgStatus;
  private Date msgPostDate;
  private String ticker;

  @Id
  @Column(name = "newsID")
  public long getMessageId() {
    return messageId;
  }

  public void setMessageId(long messageId) {
    this.messageId = messageId;
  }

  @Basic
  @Column(name = "newsTitle")
  public String getMsgTitle() {
    return msgTitle;
  }

  public void setMsgTitle(String msgTitle) {
    this.msgTitle = msgTitle;
  }

  @Basic
  @Column(name = "NewsFullContent")
  public String getMsgContent() {
    return msgContent;
  }

  public void setMsgContent(String msgContent) {
    this.msgContent = msgContent;
  }

  @Basic
  @Column(name = "Status")
  public Byte getMsgStatus() {
    return msgStatus;
  }

  public void setMsgStatus(Byte msgStatus) {
    this.msgStatus = msgStatus;
  }

  @Basic
  @Column(name = "PublicDate")
//  @Temporal(TemporalType.TIMESTAMP)
  public Date getMsgPostDate() {
    return msgPostDate;
  }

  public void setMsgPostDate(Date msgPostDate) {
    this.msgPostDate = msgPostDate;
  }

  @Basic
  @Column(name = "OrganCode")
  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MessagesEn that = (MessagesEn) o;
    return messageId == that.messageId &&
      Objects.equals(msgTitle, that.msgTitle) &&
      Objects.equals(msgContent, that.msgContent) &&
      Objects.equals(msgStatus, that.msgStatus) &&
      Objects.equals(msgPostDate, that.msgPostDate) &&
      Objects.equals(ticker, that.ticker);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(messageId, msgTitle, msgContent, msgStatus, msgPostDate, ticker);
    return result;
  }

  @Step
  public MessagesEn getNewsDetail(Long newsId) {
    Query<MessagesEn> query = TcAnalysis.tcaDbConnection.getSession().createQuery(
      "select a " +
        " from MessagesEn as a " +
        " where a.messageId = :newsID and a.msgStatus = 1"
      , MessagesEn.class
    );
    query.setParameter("newsID", newsId);
    query.setMaxResults(1);
    List<MessagesEn> results = query.getResultList();
    if (CollectionUtils.isNotEmpty(results)) {
      return results.get(0);
    }
    return null;
  }

}
