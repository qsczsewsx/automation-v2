package com.tcbs.automation.portfolio;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "HISTORY")
public class History {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "ORDER_ID")
  private String orderId;
  @Column(name = "CONTRACT_CODE")
  private String contractCode;
  @Column(name = "UNDERLYING_CODE")
  private String underlyingCode;
  @Column(name = "PRODUCT_CODE")
  private String productCode;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "MESSAGE_STATUS")
  private String messageStatus;
  @Column(name = "PARTY")
  private String party;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "COUNTER_PARTY")
  private String counterParty;
  @Column(name = "CREATE_DATE")
  private Date createDate;
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;
  @Column(name = "APPLY_TIME")
  private Date applyTime;
  @Column(name = "ORDER_MESSAGE")
  private String orderMessage;
  @Column(name = "ERROR_MESSAGE")
  private String errorMessage;
//  @Column(name = "EVENT_HASH_STRING")
//  private String eventHashString;

  public static List<History> getBuyCodeMessageStatus(String contractCode, String messageStatus) {
    Session session = PortfolioSit.porfolioIsailConnection.getSession();
    session.clear();
    Query<History> query = session.createQuery("from History ib where ib.contractCode =: contractCode and ib.messageStatus = :messageStatus order by id desc");
    query.setParameter("contractCode", contractCode);
    query.setParameter("messageStatus", messageStatus);
    List<History> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }
}
