package com.tcbs.automation.tca.activitynews;


import com.tcbs.automation.tca.TcAnalysis;
import com.tcbs.automation.tca.ticker.TickerBasic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
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
@Table(name = "stx_msg_newsVI")
public class ActivityNewsInfo {
  private Integer price;
  private Integer priceChange;
  private Double priceChangeRatio;
  private Double priceChangeRatio1W;
  private Double priceChangeRatio1M;
  @Column(name = "newsID")
  @Id
  private Long id;
  @Column(name = "OrganCode")
  private String ticker;
  @Column(name = "newsTitle")
  private String msgTitle;
  @Column(name = "PublicDate")
  private Date msgPostDate;
  @Column(name = "Status")
  private Integer msgStatus;

  public ActivityNewsInfo(Long messageId, String ticker, String msgTitle, Date msgPostDate, Integer msgStatus) {
    this.id = messageId;
    this.ticker = ticker;
    this.msgTitle = msgTitle;
    this.msgPostDate = msgPostDate;
    this.msgStatus = msgStatus;
  }

  @Step
  public List<ActivityNewsInfo> getByTicker(String ticker, Integer size, String lang) {
    List<String> listTicker = new ArrayList<>();
    listTicker.add(TickerBasic.getTickerBasic(ticker).getOrganCode());
    return getByMultipleTicker(listTicker, size, lang);
  }

  @Step
  public List<ActivityNewsInfo> getByMultipleTicker(List<String> tickers, Integer size, String lang) {
    Query<ActivityNewsInfo> query = TcAnalysis.tcaDbConnection.getSession().createQuery(
      "select new ActivityNewsInfo(a.id, a.ticker, a.msgTitle, a.msgPostDate,  a.msgStatus) from "
        + (lang.equals("en") ? "ActivityNewsEnInfo" : "ActivityNewsInfo ")
        + " as a where a.ticker in :tickers and a.msgStatus=1 order by msgPostDate desc"
      , ActivityNewsInfo.class
    );
    query.setParameter("tickers", tickers);
    query.setMaxResults(size);
    List<ActivityNewsInfo> results = query.getResultList();
    return results;

  }

  public List<ActivityNewsInfo> getByIndustryId(List<String> listIndustryIdLevel4, Integer size, String lang) {
    Query<ActivityNewsInfo> query = TcAnalysis.tcaDbConnection.getSession().createQuery(
      "select new ActivityNewsInfo(a.id,  a.ticker, a.msgTitle, a.msgPostDate, a.msgStatus) from "
        + (lang.equals("en") ? "ActivityNewsEnInfo " : "ActivityNewsInfo")
        + " as a join com.tcbs.automation.tca.industry.StoxTbCompany c on a.ticker = c.ticker"
        + " where c.industryId  in :listIndustryId and a.msgStatus=1 order by a.msgPostDate desc"
      , ActivityNewsInfo.class
    );
    query.setParameter("listIndustryId", listIndustryIdLevel4);
    query.setMaxResults(size);
    List<ActivityNewsInfo> results = query.getResultList();
    return results;

  }

  public List<ActivityNewsInfo> getAll(Integer size, String lang) {
    Query<ActivityNewsInfo> query = TcAnalysis.tcaDbConnection.getSession().createQuery(
      "select new ActivityNewsInfo(a.id, a.ticker, a.msgTitle,  a.msgPostDate, a.msgStatus) from "
        + (lang.equals("en") ? "ActivityNewsEnInfo" : " ActivityNewsInfo")
        + " a where a.msgStatus=1 order by a.msgPostDate desc"
      , ActivityNewsInfo.class
    );
    query.setMaxResults(size);
    List<ActivityNewsInfo> results = query.getResultList();
    return results;

  }

  @Step("insert or update data")
  public boolean saveOrUpdateMessages(ActivityNewsInfo activityNewsInfo) {
    try {
      Session session = TcAnalysis.tcaDbConnection.getSession();
      session.beginTransaction();
      session.save(activityNewsInfo);
      session.getTransaction().commit();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
      return false;
    }
    return true;
  }


}

