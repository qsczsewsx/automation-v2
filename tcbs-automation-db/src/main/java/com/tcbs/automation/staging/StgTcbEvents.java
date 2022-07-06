package com.tcbs.automation.staging;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Stg_stox_tb_Events")
public class StgTcbEvents {
  @Id
  @Column(name = "ID")
  private Integer id;

  @Column(name = "AnDate")
  private Date anDate;
  @Column(name = "Ticker")
  private String ticker;
  @Column(name = "EventCode")
  private String eventCode;
  @Column(name = "EventDesc")
  private String eventDesc;
  @Column(name = "EventName")
  private String eventName;
  @Column(name = "Note")
  private String note;
  @Column(name = "EventDesc_E")
  private String eventDescEng;
  @Column(name = "EventName_E")
  private String eventNameEng;
  @Column(name = "Note_E")
  private String noteEng;
  @Column(name = "ExRigthDate")
  private Date exRigthDate;
  @Column(name = "ExDate")
  private Date exDate;
  @Column(name = "RegFinalDate")
  private Date regFinalDate;


  @Step
  public List<StgTcbEvents> getStoxEvent(String ticker, String eventCode, Date fromDate, Date toDate, Integer page, Integer size) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("FROM StgTcbEvents ");
    queryBuilder.append("WHERE 1 = 1 ");
    if (StringUtils.isNotEmpty(ticker)) {
      queryBuilder.append("AND ticker = :ticker ");
    }
    if (StringUtils.isNotEmpty(eventCode)) {
      queryBuilder.append("AND eventCode = :eventCode ");
    }
    if (fromDate != null) {
      queryBuilder.append("AND CONVERT(DATE, anDate) >= CONVERT(date, :fromDate ) ");
    }
    if (toDate != null) {
      queryBuilder.append("AND CONVERT(DATE, anDate) <= CONVERT(date, :toDate) ");
    }
    queryBuilder.append("ORDER BY AnDate DESC, ID DESC ");

    return execStgTcbEvents(ticker, eventCode, null, fromDate, toDate, page, size, queryBuilder);
  }

  @Step
  public List<StgTcbEvents> getStoxEventByExRightDate(String ticker, List<String> eventCodes, Date fromDate, Date toDate, Integer page, Integer size) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("FROM StgTcbEvents ");
    queryBuilder.append("WHERE ExRigthDate IS NOT NULL ");
    if (StringUtils.isNotEmpty(ticker)) {
      queryBuilder.append("AND ticker = :ticker ");
    }
    if (eventCodes.size() > 0 && !eventCodes.get(0).equals("")) {
      queryBuilder.append("AND eventCode IN :eventCodes ");
    }
    if (fromDate != null) {
      queryBuilder.append("AND CONVERT(DATE, ExRigthDate) >= CONVERT(date, :fromDate ) ");
    }
    if (toDate != null) {
      queryBuilder.append("AND CONVERT(DATE, ExRigthDate) <= CONVERT(date, :toDate) ");
    }
    queryBuilder.append("ORDER BY ExRigthDate DESC, ID DESC ");

    return execStgTcbEvents(ticker, null, eventCodes, fromDate, toDate, page, size, queryBuilder);
  }

  private List<StgTcbEvents> execStgTcbEvents(String ticker, String eventCode, List<String> eventCodes, Date fromDate, Date toDate,
                                              Integer page, Integer size, StringBuilder queryBuilder) {
    String queryStr = queryBuilder.toString();
    Query<StgTcbEvents> query = Staging.stagingDbConnection.getSession()
      .createQuery(queryStr, StgTcbEvents.class);
    if (StringUtils.isNotEmpty(ticker)) {
      query.setParameter("ticker", ticker);
    }
    if (StringUtils.isNotEmpty(eventCode)) {
      query.setParameter("eventCode", eventCode);
    }
    if (eventCodes != null && eventCodes.size() > 0 && !eventCodes.get(0).equals("")) {
      query.setParameter("eventCodes", eventCodes);
    }
    if (fromDate != null) {
      query.setParameter("fromDate", fromDate);
    }
    if (toDate != null) {
      query.setParameter("toDate", toDate);
    }
    query.setFirstResult(page * size);
    query.setMaxResults(size);
    return query.getResultList();
  }
}
