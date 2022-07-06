package com.tcbs.automation.dwh.iwealthpartner;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Table(name = "iWP_Incentive")
public class IWpIncentiveEntity {
  private Long id;
  private Timestamp reportDate;
  private String referTcbsid;
  private String tcbsid;
  private String instrumentType;
  private String tradingCode;
  private Double valueBased;
  private Double incentive;
  private Integer campaignId;

  @Step
  public static HashMap<String, Object> getFundBondStockTrans(String tcbsId, String fromDate, String toDate, String type) {
    StringBuilder queryBuilder = new StringBuilder();
    if (type.equalsIgnoreCase("bond")) {
      queryBuilder.append("  select sub.partner_id, sum(IIF(bd.[Action] = 5, bd.principal, 0)) totalBondBuy,  ");
      queryBuilder.append("  sum(IIF(bd.[Action] = 7, bd.principal, 0)) totalBondSell , count(bd.[Action]) totalTransBond ");
      queryBuilder.append(" from (	select partner_id, subscriber_id, wp.created_date, ineffective_date, uv.CUSTODYCD as subCustodyCd ");
      queryBuilder.append("           from [tcbs-dwh].dbo.Stg_ipartner_wp_relation wp left join [tcbs-dwh].dbo.smy_dwh_cas_alluserview uv on wp.subscriber_id = uv.TCBSID ");
      queryBuilder.append("           where uv.EtlCurDate = (select max(etlcurdate) from [tcbs-dwh].dbo.smy_dwh_cas_alluserview) ");
      queryBuilder.append("           and (wp.status = 'ACTIVE' or ineffective_date is not NULL) ");
//      queryBuilder.append("           and cast(wp.created_date as date) between :fromDate and :toDate ");
      queryBuilder.append("           and partner_id = :tcbsId) sub ");
      queryBuilder.append("  left join [tcbs-dwh].dbo.vw_Bond_Trading_Details bd on sub.subscriber_id = bd.CustomerTcbsId ");
      queryBuilder.append("  where bd.TradingDate >= sub.created_date and ((bd.TradingDate <= sub.ineffective_date) or (sub.ineffective_date is null)) ");
      queryBuilder.append("  and bd.TradingDate between :fromDate and :toDate ");
      queryBuilder.append("  group by sub.partner_id ");
    } else if (type.equalsIgnoreCase("fund")) {
      queryBuilder.append("  select partner_id, sum(IIF(fd.ACTION_ID = 1,fd.TRANSACTION_VALUE,0)) totalFundBuy,  ");
      queryBuilder.append("  sum(IIF(fd.ACTION_ID = 2,fd.TRANSACTION_VALUE,0)) totalFundSell , count(fd.ACTION_ID) totalTransFund ");
      queryBuilder.append(" from (	select partner_id, subscriber_id, wp.created_date, ineffective_date, uv.CUSTODYCD as subCustodyCd  ");
      queryBuilder.append("           from [tcbs-dwh].dbo.Stg_ipartner_wp_relation wp ");
      queryBuilder.append("           left join [tcbs-dwh].dbo.smy_dwh_cas_alluserview uv on wp.subscriber_id = uv.TCBSID ");
      queryBuilder.append("           where uv.EtlCurDate = (select max(etlcurdate) from [tcbs-dwh].dbo.smy_dwh_cas_alluserview) ");
      queryBuilder.append("           and (wp.status = 'ACTIVE' or ineffective_date is not NULL) and partner_id = :tcbsId ");
      queryBuilder.append("        ) sub  ");
      queryBuilder.append("  left join [tcbs-dwh].dbo.smy_dwh_tci_fund_trading_details fd on sub.subscriber_id = fd.ACCOUNT_ID ");
      queryBuilder.append("  where EtlCurDate = (select max(etlcurdate) from [tcbs-dwh].dbo.smy_dwh_tci_fund_trading_details) and STATUS = 'MATCHED' ");
      queryBuilder.append("  and fd.MATCHED_TIMESTAMP >= sub.created_date and ((fd.MATCHED_TIMESTAMP <= sub.ineffective_date) or (sub.ineffective_date is null)) ");
      queryBuilder.append("  and fd.MATCHED_TIMESTAMP between :fromDate and :toDate ");
      queryBuilder.append("  group by partner_id ");
    } else if (type.equalsIgnoreCase("stock")) {
      queryBuilder.append("  select partner_id, sum(iif(st.Field = 'Mua', st.buyamt, 0)) totalStockBuy,  ");
      queryBuilder.append("  sum(iif(st.Field = 'Bán', st.SellAmt, 0)) totalStockSell , count(st.BusDate) totalDateStockTrans ");
      queryBuilder.append("  from (	select partner_id, subscriber_id, wp.created_date, ineffective_date, uv.CUSTODYCD as subCustodyCd   ");
      queryBuilder.append("           from [tcbs-dwh].dbo.Stg_ipartner_wp_relation wp ");
      queryBuilder.append("           left join [tcbs-dwh].dbo.smy_dwh_cas_alluserview uv on wp.subscriber_id = uv.TCBSID ");
      queryBuilder.append("           where uv.EtlCurDate = (select max(etlcurdate) from [tcbs-dwh].dbo.smy_dwh_cas_alluserview)   ");
      queryBuilder.append("           and (wp.status = 'ACTIVE' or ineffective_date is not NULL) ");
//      queryBuilder.append("           and cast(wp.created_date as date) between :fromDate and :toDate ");
      queryBuilder.append("           and partner_id = :tcbsId) sub ");
      queryBuilder.append("  left join [tcbs-dwh].dbo.smy_dwh_flx_allstocktxn st on sub.subCustodyCd = st.CustodyCD and st.Field in ('Mua','Bán') and st.Sectype in ( 'Stock', 'Fund') ");
      queryBuilder.append("  where EtlCurDate = (select max(etlcurdate) from [tcbs-dwh].dbo.smy_dwh_flx_allstocktxn) ");
      queryBuilder.append("  and st.BusDate >= sub.created_date and ((st.BusDate <= sub.ineffective_date) or (sub.ineffective_date is null)) ");
      queryBuilder.append("  and st.BusDate between :fromDate and :toDate ");
      queryBuilder.append("  group by partner_id; ");
    }
    try {
      List<HashMap<String, Object>> result = Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("tcbsId", tcbsId)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      if (!result.isEmpty()) {
        return result.get(0);
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new HashMap<>();
  }

  @Step
  public static List<HashMap<String, Object>> getSubIWealthPartner(List<String> tcbsIds, String fromDate, String toDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("   select partner_id, COUNT(distinct subscriber_id) as sub  ");
    queryBuilder.append("   from [tcbs-dwh].dbo.Stg_ipartner_wp_relation wp ");
    queryBuilder.append("   left join [tcbs-dwh].dbo.smy_dwh_cas_alluserview uv on wp.subscriber_id = uv.TCBSID ");
    queryBuilder.append("   where uv.EtlCurDate = (select max(etlcurdate) from [tcbs-dwh].dbo.smy_dwh_cas_alluserview) ");
    queryBuilder.append("   and wp.status in ('ACTIVE') ");
    queryBuilder.append("   and partner_id in :tcbsIds and partner_id is not null ");
    queryBuilder.append("   GROUP by partner_id  ");

    try {
      return Dwh.dwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("tcbsIds", tcbsIds)
//        .setParameter("fromDate", fromDate)
//        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Id
  @GeneratedValue
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Basic
  @Column(name = "ReportDate")
  public Timestamp getReportDate() {
    return reportDate;
  }

  public void setReportDate(Timestamp reportDate) {
    this.reportDate = reportDate;
  }

  @Basic
  @Column(name = "Refer_TCBSID")
  public String getReferTcbsid() {
    return referTcbsid;
  }

  public void setReferTcbsid(String referTcbsid) {
    this.referTcbsid = referTcbsid;
  }

  @Basic
  @Column(name = "TCBSID")
  public String getTcbsid() {
    return tcbsid;
  }

  public void setTcbsid(String tcbsid) {
    this.tcbsid = tcbsid;
  }

  @Basic
  @Column(name = "INSTRUMENT_TYPE")
  public String getInstrumentType() {
    return instrumentType;
  }

  public void setInstrumentType(String instrumentType) {
    this.instrumentType = instrumentType;
  }

  @Basic
  @Column(name = "TradingCode")
  public String getTradingCode() {
    return tradingCode;
  }

  public void setTradingCode(String tradingCode) {
    this.tradingCode = tradingCode;
  }

  @Basic
  @Column(name = "Value_Based")
  public Double getValueBased() {
    return valueBased;
  }

  public void setValueBased(Double valueBased) {
    this.valueBased = valueBased;
  }

  @Basic
  @Column(name = "Incentive")
  public Double getIncentive() {
    return incentive;
  }

  public void setIncentive(Double incentive) {
    this.incentive = incentive;
  }

  @Basic
  @Column(name = "CampaignID")
  public Integer getCampaignId() {
    return campaignId;
  }

  public void setCampaignId(Integer campaignId) {
    this.campaignId = campaignId;
  }

}
