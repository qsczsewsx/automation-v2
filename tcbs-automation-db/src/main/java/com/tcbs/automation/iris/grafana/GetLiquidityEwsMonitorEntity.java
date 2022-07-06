package com.tcbs.automation.iris.grafana;


import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetLiquidityEwsMonitorEntity {

  private static final String PCT_LOSS_SELL_VOLUME = "Pct_LossSellVolume";
  private static final String PCT_SELL_VOLUME_OUTSTANDING = "Pct_SellVolume_Outstanding";
  private static final String PCT_AVG_DAILY_SALES = "Pct_AvgDailySales";
  private static final String PCT_SELL_ORDER = "Pct_SellOrder";
  private static final String PCT_SELL_VOLUME = "Pct_SellVolume";
  private static final String CONDITION_QUERY = "on 1 = 1";

  private static final String PCT_LOSS_SELL_VOLUME_QUERY = "with Outstanding as (" +
    " select UpdateDate " +
    "                       ,BondCategory " +
    "                       ,sum(Value) Outstanding " +
    "                       from DailyPort_risk_Liquidity_iBondOustanding " +
    "                       where custshortname = 'TotalRetail' " +
    "                       and cast(updatedate as date) = (select max(updatedate) from DailyPort_risk_Liquidity_iBondOustanding) " +
    "                       and BondCategory = 'iBondPrix' " +
    "                       group by UpdateDate, BondCategory " +
    "), " +
    " TotalSell_UnderPrincipal as (" +
    " select cast(getdate() as date) date " +
    "                ,isnull(sum(cast(a.VOLUME as bigint))*100000,0) SELLVOL_UnderPrincipal " +
    "        from RT_tcb_ORDER_MARKET a " +
    "                                      " +
    "        left join (" +
    " select distinct  cast(a.datereport as date) date  " +
    " ,b.tcbsid ACCOUNT_ID  " +
    " ,a.bondcode  " +
    " ,sum(principal) PRINCIPAL  " +
    " ,sum(balancequantity) QUANTITY  " +
    " from dailyport_bondbal a  " +
    " left join Dtm_dwh_Customer_Dim b  " +
    " on a.CustodyCD = b.CustodyCode  " +
    " where 1=1  " +
    " and a.datereport = (select max(datereport) from dailyport_bondbal)-1  " +
    " group by cast(a.datereport as date) , a.BondCode, b.tcbsid  " +
    "        ) b  " +
    "        on a.ACCOUNT_ID = b.ACCOUNT_ID  " +
    "        and a.BOND_CODE = b.BondCode  " +
    "        where 1=1  " +
    "        and a.ACTION_ID = 2  " +
    "        and a.STATUS = 'MATCHING'  " +
    "        and (cast(a.START_DATE as date)<= cast(getdate() as date) and cast(getdate() as date)<= cast(a.EXPIRED_DATE as date))                                       " +
    "        and (a.PRICE/a.VOLUME) < (b.PRINCIPAL/b.QUANTITY)   " +
    ")  " +
    "select case when ISNULL(Outstanding.Outstanding, 0) = 0 then 0  " +
    " else CAST(ISNULL(TotalSell_UnderPrincipal.SELLVOL_UnderPrincipal, 0) AS FLOAT) / (ISNULL(Outstanding.Outstanding, 0))  " +
    " end as Pct_LossSellVolume  " +
    "from TotalSell_UnderPrincipal  " +
    "full outer join Outstanding  " + CONDITION_QUERY;

  private static final String SELL_VOLUME_OUTSTANDING_QUERY = "with Outstanding as (  " +
    "  select  UpdateDate   " +
    "            ,BondCategory  " +
    "            ,sum(Value) Outstanding  " +
    "    from DailyPort_risk_Liquidity_iBondOustanding  " +
    "    where custshortname = 'TotalRetail'  " +
    "    and cast(updatedate as date) = (select max(updatedate) from DailyPort_risk_Liquidity_iBondOustanding)  " +
    "    and BondCategory = 'iBondPrix'  " +
    "    group by UpdateDate, BondCategory  " +
    "),   " +
    "Matched as (  " +
    "  select  cast(a.TradingDate  as date) date  " +
    "            , sum(isnull(a.quantity, 0)*100000) total_vol   " +
    "    from rt_tcb_trading a  " +
    "    where a.ACTION = 5  " +
    "    and a.MARKETSTATUS = 1  " +
    "    and a.WAITINGSTATUS = 3  " +
    "    and a.CUSTOMERSTATUS = 1  " +
    "    and a.OPERATIONSTATUS = 1  " +
    "    and cast(a.TRADINGDATE as date) = cast(getdate() as date)  " +
    "    group by cast(a.TRADINGDATE as date)  " +
    "),   " +
    "SellUnmatched as (  " +
    "  select  cast(getdate() as date) date  " +
    "            ,ISNULL(SUM(cast(a.REMAIN_VOLUME as bigint)) * 100000,0) total_vol   " +
    "    from RT_tcb_ORDER_MARKET a  " +
    "    where 1=1  " +
    "    and a.ACTION_ID = 2  " +
    "    and a.STATUS = 'MATCHING'  " +
    "    and (cast(a.START_DATE as date)<= cast(getdate() as date) and cast(getdate() as date)<= cast(a.EXPIRED_DATE as date))  " +
    ")  " +
    "select  case   " +
    "      when ISNULL(Outstanding.Outstanding, 0) = 0 then 0  " +
    "      else (CAST(ISNULL(Matched.total_vol, 0) AS FLOAT) + ISNULL(SellUnmatched.total_vol, 0)) / (ISNULL(Outstanding.Outstanding, 0))  " +
    "    end as Pct_SellVolume_Outstanding  " +
    "from Matched  " +
    " full outer join SellUnmatched  " + CONDITION_QUERY +
    " full outer join Outstanding  " + CONDITION_QUERY;

  private static final String AVG_DAILY_SALES_QUERY = "with dt_table as (  " +
    "  select  distinct dt.date  " +
    "  from dtm_dwh_date_dim dt  " +
    "  left join INV_HOLIDAY ih  " +
    "  on dt.date = ih.timestamp  " +
    "  where ih.TIMESTAMP is null  " +
    "  and dt.date > DATEADD(DAY, -30, CAST(GETDATE() AS DATE))  " +
    "  and dt.date <= CAST(GETDATE() AS DATE)  " +
    ")  " +
    ",TCBS_GrossSales as (  " +
    "  select  a.date as TradingDate  " +
    "      , ISNULL(TotalDailySales, 0) as TotalDailySales   " +
    "  from dt_table a  " +
    "  left join (  " +
    "    select  cast(t.tradingDate as date) TradingDate  " +
    "        ,sum(isnull(Quantity, 0))/10000 TotalDailySales  " +
    "    from RT_tcb_Trading t  " +
    "    where 1=1  " +
    "    and t.action = 5  " +
    "    and t.AgencyStatus = 1  " +
    "    and t.WaitingStatus = 3  " +
    "    and t.CounterParty in (3926)  " +
    "    and t.CustomerID not in (3926)  " +
    "    and cast(t.tradingDate as date) > dateadd(DAY,-30,cast(getdate() as date))  " +
    "    group by cast(t.tradingDate as date)  " +
    "  ) b  " +
    "  on a.date = cast(b.tradingDate as date)  " +
    ")  " +
    "select  top 1 dt_table.date  " +
    "    ,case   " +
    "      when b.month_moving_average = 0 then 0  " +
    "      else a.week_moving_average / b.month_moving_average  " +
    "    end as Pct_AvgDailySales  " +
    "from dt_table  " +
    "  " +
    "full outer join (  " +
    "  select isnull(avg(TotalDailySales), 0) as week_moving_average  " +
    "  from TCBS_GrossSales  " +
    "  where TradingDate > dateadd(WEEK,-1,cast(getdate() as date))  " +
    ") a  " +
    "on 1=1  " +
    "  " +
    "full outer join (  " +
    "  select isnull(avg(TotalDailySales), 0) as month_moving_average  " +
    "  from TCBS_GrossSales  " +
    ") b  " +
    "on 1 = 1  " +
    "  " +
    "order by dt_table.date desc  " +
    "  ";

  private static final String SELL_ORDER_QUERY = "with Matched as (        " +
    "         select         cast(a.TradingDate  as date) date        " +
    "            ,COUNT(a.customerid) total_orders        " +
    "    from rt_tcb_trading a        " +
    "    where a.ACTION = 5        " +
    "    and a.MARKETSTATUS = 1        " +
    "    and a.WAITINGSTATUS = 3        " +
    "    and a.CUSTOMERSTATUS = 1        " +
    "    and a.OPERATIONSTATUS = 1        " +
    "    and cast(a.TRADINGDATE as date) = cast(getdate() as date)        " +
    "    group by cast(a.TRADINGDATE as date)        " +
    "),         " +
    "SellUnmatched as (        " +
    "         select         cast(getdate() as date) date        " +
    "            ,COUNT(a.ACCOUNT_ID) total_orders        " +
    "    from RT_tcb_ORDER_MARKET a        " +
    "    where 1=1        " +
    "    and a.ACTION_ID = 2        " +
    "    and a.STATUS = 'MATCHING'        " +
    "    and (cast(a.START_DATE as date)<= cast(getdate() as date) and cast(getdate() as date)<= cast(a.EXPIRED_DATE as date))        " +
    "),        " +
    "TCBS_Matched as (        " +
    "         select         cast(a.TradingDate  as date) date        " +
    "            , count(a.ID) total_orders         " +
    "    from rt_tcb_trading a        " +
    "    where a.ACTION = 5        " +
    "    and a.MARKETSTATUS = 1        " +
    "    and a.WAITINGSTATUS = 3        " +
    "    and a.CUSTOMERSTATUS = 1        " +
    "    and a.OPERATIONSTATUS = 1        " +
    "    and cast(a.TradingDate  as date) = cast(getdate() as date)        " +
    "    and a.customerID = 3926                  " +
    "    group by cast(a.TradingDate  as date)        " +
    ")        " +
    "select         case        " +
    "  when ISNULL(Matched.total_orders, 0) + ISNULL(SellUnmatched.total_orders, 0) = 0 then 1        " +
    "  else (CAST(ISNULL(Matched.total_orders, 0) AS FLOAT) - ISNULL(TCBS_Matched.total_orders, 0)) / (ISNULL(Matched.total_orders, 0) + ISNULL(SellUnmatched.total_orders, 0))" +
    "                  end as Pct_SellOrder        " +
    "from Matched        " +
    "full outer join SellUnmatched        " +
    "on 1 = 1        " +
    "full outer join TCBS_Matched        " +
    "on 1 = 1 ";

  private static final String  SELL_VOLUME_QUERY = "with Matched as (          " +
    "       select       cast(a.TradingDate  as date) date          " +
    "            , sum(isnull(a.quantity, 0)*100000) total_vol           " +
    "            , COUNT(a.customerid) total_orders          " +
    "    from rt_tcb_trading a          " +
    "    where a.ACTION = 5          " +
    "    and a.MARKETSTATUS = 1          " +
    "    and a.WAITINGSTATUS = 3          " +
    "    and a.CUSTOMERSTATUS = 1          " +
    "    and a.OPERATIONSTATUS = 1          " +
    "    and cast(a.TRADINGDATE as date) = cast(getdate() as date)          " +
    "    group by cast(a.TRADINGDATE as date)          " +
    "),           " +
    "SellUnmatched as (          " +
    "       select       cast(getdate() as date) date          " +
    "            ,ISNULL(SUM(cast(a.REMAIN_VOLUME as bigint)) * 100000,0) total_vol            " +
    "            ,COUNT(a.ACCOUNT_ID) total_orders          " +
    "    from RT_tcb_ORDER_MARKET a          " +
    "    where 1=1          " +
    "    and a.ACTION_ID = 2          " +
    "    and a.STATUS = 'MATCHING'          " +
    "    and (cast(a.START_DATE as date)<= cast(getdate() as date) and cast(getdate() as date)<= cast(a.EXPIRED_DATE as date))          " +
    "),          " +
    "TCBS_Matched as (          " +
    "       select       cast(a.TradingDate  as date) date          " +
    "            , sum(isnull(a.quantity, 0)*100000) total_vol           " +
    "            , count(a.ID) total_orders           " +
    "    from rt_tcb_trading a          " +
    "    where a.ACTION = 5          " +
    "    and a.MARKETSTATUS = 1          " +
    "    and a.WAITINGSTATUS = 3          " +
    "    and a.CUSTOMERSTATUS = 1          " +
    "    and a.OPERATIONSTATUS = 1          " +
    "    and cast(a.TradingDate  as date) = cast(getdate() as date)          " +
    "    and a.customerID = 3926          " +
    "    group by cast(a.TradingDate  as date)          " +
    ")          " +
    "          " +
    "select case           " +
    "                     when ISNULL(Matched.total_vol, 0) + ISNULL(SellUnmatched.total_vol, 0) = 0 then 1          " +
    "                     else (ISNULL(Matched.total_vol, 0) - ISNULL(TCBS_Matched.total_vol, 0)) / (ISNULL(Matched.total_vol, 0) + ISNULL(SellUnmatched.total_vol, 0))          " +
    "              end as Pct_SellVolume          " +
    "from Matched          " +
    "full outer join SellUnmatched          " +
    "on 1 = 1          " +
    "full outer join TCBS_Matched          " +
    "on 1 = 1 ";

  @Step("Get data from iRis")
  public static List<HashMap<String, Object>> calculateDataFromDWH() {
    HashMap<String, Object> monitor = new HashMap<>();
    monitor.put(PCT_SELL_VOLUME, getData(SELL_VOLUME_QUERY, PCT_SELL_VOLUME));
    monitor.put(PCT_SELL_ORDER, getData(SELL_ORDER_QUERY, PCT_SELL_ORDER));
    monitor.put(PCT_SELL_VOLUME_OUTSTANDING, getData(SELL_VOLUME_OUTSTANDING_QUERY, PCT_SELL_VOLUME_OUTSTANDING));
    monitor.put(PCT_AVG_DAILY_SALES, getData(AVG_DAILY_SALES_QUERY, PCT_AVG_DAILY_SALES));
    monitor.put(PCT_LOSS_SELL_VOLUME, getData(PCT_LOSS_SELL_VOLUME_QUERY, PCT_LOSS_SELL_VOLUME));
    List<HashMap<String, Object>> listResult = new ArrayList<>();
    listResult.add(monitor);
    return listResult;
  }
  @SuppressWarnings("unchecked")
  @Step("Get data from iRis")
  public static List<HashMap<String, Object>> getStoringDataFromDWH() {
    StringBuilder query = new StringBuilder();
    query.append(" select *  from Risk_Liquidity_EWS_Monitor where updated_time = (select max(updated_time) from Risk_Liquidity_EWS_Monitor)  ");
    try {
      List<HashMap<String, Object>> result =  Dwh.dwhDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      List<HashMap<String, Object>> resultFinal = new ArrayList<>();
      result.forEach(v -> {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(PCT_SELL_VOLUME, Double.parseDouble(v.get(PCT_SELL_VOLUME).toString()));
        hashMap.put(PCT_SELL_ORDER, Double.parseDouble(v.get(PCT_SELL_ORDER).toString()));
        hashMap.put(PCT_SELL_VOLUME_OUTSTANDING, Double.parseDouble(v.get(PCT_SELL_VOLUME_OUTSTANDING).toString()));
        hashMap.put(PCT_AVG_DAILY_SALES, Double.parseDouble(v.get(PCT_AVG_DAILY_SALES).toString()));
        hashMap.put(PCT_LOSS_SELL_VOLUME, Double.parseDouble(v.get(PCT_LOSS_SELL_VOLUME).toString()));
        resultFinal.add(hashMap);
      });
      return resultFinal;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @SuppressWarnings("unchecked")
  public static Double getData(String queryString, String field) {
    StringBuilder query = new StringBuilder();
    query.append(queryString);
    try {
      HashMap<String, Object> result = (HashMap<String, Object>)  Dwh.dwhDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList().get(0);
      if(result.get(field)!=null) {
        Double value = Double.parseDouble(result.get(field).toString());
        return (double) Math.round(value * 10000 * 100) / 10000;
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }
}
