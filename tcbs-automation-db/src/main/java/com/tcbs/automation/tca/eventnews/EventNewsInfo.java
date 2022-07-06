package com.tcbs.automation.tca.eventnews;

import com.tcbs.automation.tca.TcAnalysis;
import com.tcbs.automation.tca.ticker.TickerBasic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stx_cpa_Event")
public class EventNewsInfo {
  @Id
  // @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "EVENTID")
  private Long id;

  @Column(name = "PUBLICDATE")
  private Date anDate;
  @Column(name = "OrganCode")
  private String ticker;
  @Column(name = "EventLISTCode")
  private String eventCode;
  @Column(name = "EventDescription")
  private String eventDesc;
  @Column(name = "EventTitle")
  private String eventName;
  @Column(name = "Note")
  private String note;
  @Column(name = "en_EventDescription")
  private String eventDescEng;
  @Column(name = "en_EventTitle")
  private String eventNameEng;
  @Column(name = "en_Note")
  private String noteEng;
  @Column(name = "ExRightDate")
  private Date exRigthDate;
  @Column(name = "IssueDate")
  private Date exDate;
  @Column(name = "RecordDate")
  private Date regFinalDate;

  @Step
  public List<EventNewsInfo> getByTicker(String ticker, Integer page, Integer size) {
    List<String> listTicker = new ArrayList<>();
    listTicker.add(TickerBasic.getTickerBasic(ticker).getOrganCode());
    return getByMultipleTicker(listTicker, page, size);
  }

  @Step
  public List<EventNewsInfo> getByMultipleTicker(List<String> tickers, Integer page, Integer size) {
    Query<EventNewsInfo> query = TcAnalysis.tcaDbConnection.getSession().createQuery(
      "from EventNewsInfo a where a.ticker in :tickers order by anDate desc", EventNewsInfo.class);
    query.setParameter("tickers", tickers);
    query.setFirstResult(page * size);
    query.setMaxResults(size);
    List<EventNewsInfo> result = query.getResultList();
    return result;
  }

  @Step
  public List<EventNewsInfo> getByIndustryIdLevel4(List<String> listIdLevel4, Integer page, Integer size) {
    if (CollectionUtils.isEmpty(listIdLevel4)) {
      return new ArrayList<>();
    }
    Query<EventNewsInfo> query = TcAnalysis.tcaDbConnection.getSession().createQuery(
      "select e from EventNewsInfo e join com.tcbs.automation.tca.industry.StoxTbCompany c on e.ticker = c.ticker where c.industryId in :listIdLevel4 order by e.anDate desc",
      EventNewsInfo.class);
    query.setParameter("listIdLevel4", listIdLevel4);
    query.setFirstResult(page * size);
    query.setMaxResults(size);
    return query.getResultList();
  }

  @Step
  public List<EventNewsInfo> getAll(Integer page, Integer size) {
    Query<EventNewsInfo> query = TcAnalysis.tcaDbConnection.getSession()
      .createQuery("from EventNewsInfo a order by anDate desc", EventNewsInfo.class);
    query.setFirstResult(page * size);
    query.setMaxResults(size);
    return query.getResultList();
  }
}
