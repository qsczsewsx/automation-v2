package com.tcbs.automation.iris.impact;

import com.tcbs.automation.iris.AwsIRis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CalculateRttEntity {
  @Step("Get stock ")
  public static List<HashMap<String, Object>> getStockBpm(String eltUpdateDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("  select CUSTODYCD, SYMBOL, MARKET_PRICE ,LOAN_PRICE_AFTER LOAN_RATIO_AFTER,LOAN_PRICE_BEFORE, LOAN_RATIO_BEFORE,ETL_UPDATE_DATE ");
    queryBuilder.append("  from RISK_STOCK ");
    queryBuilder.append("  where ETL_UPDATE_DATE = :etlUpdateDate ");
    try {

      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("etlUpdateDate", eltUpdateDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Get customer")
  public static List<HashMap<String, Object>> getResult(String etlUpdateDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from RISK_RTT_CALCULATE_RESULT where ETL_UPDATE_DATE = :etlUpdateDate   ");
    try {

      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("etlUpdateDate", etlUpdateDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Get customer")
  public static List<HashMap<String, Object>> getData(String etlUpdateDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" WITH temp AS ( ");
    queryBuilder.append(" select a.CUSTODYCD,a.AFACCTNO,FULL_NAME,MRI_RATE,MRM_RATE,MARGIN_RATE,MRL_RATE,TOTAL_VND,SYMBOL,NET_DEBT,ASSET, ");
    queryBuilder.append(" DEBT_BEFORE DEBT, CASH_BEFORE CASH,SEASS, MARKET_PRICE, LOAN_PRICE_BEFORE,LOAN_RATIO_BEFORE,nvl(CAASS_BEFORE,0) CAASS_BEFORE,nvl(CAAMT_BEFORE,0) CAAMT_BEFORE , ");
    queryBuilder.append(" nvl(CAASS_AFTER,0) CAASS_AFTER,nvl(CAAMT_AFTER,0) CAAMT_AFTER, ");
    queryBuilder.append(" case when LOAN_PRICE_BEFORE >= AVG_PRICE then AVG_PRICE else LOAN_PRICE_BEFORE end PRICEBEFORE, ");
    queryBuilder.append(" LOAN_PRICE_AFTER,LOAN_RATIO_AFTER,case when LOAN_PRICE_AFTER >= MARKET_PRICE then MARKET_PRICE else LOAN_PRICE_AFTER end PRICEAFTER ");
    queryBuilder.append(" from RISK_CUSTOMER a ");
    queryBuilder.append(" inner join  RISK_STOCK b on a.CUSTODYCD = b.CUSTODYCD and a.ETL_UPDATE_DATE= b.ETL_UPDATE_DATE ");
    queryBuilder.append(" where a.ETL_UPDATE_DATE = :etlUpdateDate ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" select * from ( ");
    queryBuilder.append(" select * from ( ");
    queryBuilder.append(" select CUSTODYCD, AFACCTNO,FULL_NAME,SYMBOL, MRI_RATE,MRM_RATE,MARGIN_RATE,MRL_RATE,TOTAL_VND,NET_DEBT,DeltaRTT, rn, DEBT, CASH, SEASS,sumDelta,case when RTT_AFTER_SUM_DELTA <=0 then 0 else RTT_AFTER_SUM_DELTA end as RTT_AFTER_SUM_DELTA ,STATUS,TOTAL_ASSETS,case when STATUS <> 'Normal' then CEIL(NET_DEBT - TOTAL_ASSETS / (MRM_RATE/100)) end as MONEY_ADDED_TOTAL,round(TOTAL_ASSETS / NET_DEBT * 100, 2) as RTT_AFTER,round(TOTAL_ASSETS_BEFORE / NET_DEBT * 100, 2) as RTT_BEFORE,round(TOTAL_ASSETS_BEFORE_BONUS / NET_DEBT * 100, 2) as RTT_BEFORE_BONUS ");
    queryBuilder.append(" from ( ");
    queryBuilder.append(" SELECT CUSTODYCD, AFACCTNO,FULL_NAME,MRI_RATE,MRM_RATE, MARGIN_RATE, MRL_RATE,TOTAL_VND,NET_DEBT,SYMBOL, DEBT, CASH,SEASS, ");
    queryBuilder.append(" DeltaRTT, ROW_NUMBER() over (PARTITION BY CUSTODYCD order by DeltaRTT asc, SYMBOL asc) as rn, sumDelta,RTT_AFTER_SUM_DELTA,case when RTT_AFTER_SUM_DELTA < MRL_RATE then 'Force Sell' ");
    queryBuilder.append(" when (MRL_RATE <= RTT_AFTER_SUM_DELTA and RTT_AFTER_SUM_DELTA < MRM_RATE) then 'Margin Call' ");
    queryBuilder.append(" when RTT_AFTER_SUM_DELTA >= MRM_RATE then 'Normal' end   as STATUS,TOTAL_ASSETS,TOTAL_ASSETS_BEFORE,TOTAL_ASSETS_BEFORE_BONUS ");
    queryBuilder.append(" FROM ( ");
    queryBuilder.append(" select CUSTODYCD, AFACCTNO,FULL_NAME,MRI_RATE,MRM_RATE,MARGIN_RATE,MRL_RATE,TOTAL_VND,NET_DEBT,SYMBOL, DEBT, CASH,SEASS,sumDelta,TOTAL_ASSETS,TOTAL_ASSETS_BEFORE,TOTAL_ASSETS_BEFORE_BONUS,case when NET_DEBT <> 0 then round(DeltaRtt/abs(NET_DEBT) *100,3) else 0 end  DeltaRTT, round(sumDelta/NET_DEBT*100 + TOTAL_ASSETS_BEFORE/NET_DEBT*100,2) as  RTT_AFTER_SUM_DELTA,round(sumDelta/NET_DEBT*100 + TOTAL_ASSETS_BEFORE_BONUS/NET_DEBT*100,2) as  RTT_AFTER_SUM_DELTA_BONUS ");
    queryBuilder.append(" from ");
    queryBuilder.append(" (select CUSTODYCD, AFACCTNO,FULL_NAME,MRI_RATE,MRM_RATE,MARGIN_RATE,MRL_RATE,TOTAL_VND,NET_DEBT,SYMBOL, DEBT, CASH,SEASS, ");
    queryBuilder.append(" (PRICEAFTER * LOAN_RATIO_AFTER/100 - PRICEBEFORE * LOAN_RATIO_BEFORE/100) * ASSET  as DeltaRtt ");
    queryBuilder.append(" from temp) a ");
    queryBuilder.append(" left join ");
    queryBuilder.append(" (select CUSTODYCD Cus,sum((PRICEAFTER * LOAN_RATIO_AFTER/100 - PRICEBEFORE * LOAN_RATIO_BEFORE/100) * ASSET)  as sumDelta, sum(PRICEAFTER * LOAN_RATIO_AFTER/100 * ASSET) TOTAL_ASSETS,sum(PRICEBEFORE * LOAN_RATIO_BEFORE/100 * (ASSET+CAASS_BEFORE) + CAAMT_BEFORE) TOTAL_ASSETS_BEFORE_BONUS,sum(PRICEBEFORE * LOAN_RATIO_BEFORE/100 * ASSET ) TOTAL_ASSETS_BEFORE ");
    queryBuilder.append(" from temp ");
    queryBuilder.append(" group by CUSTODYCD) b on a.CUSTODYCD = b.Cus ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" where rn <= 3) ");
    queryBuilder.append(" pivot (max(SYMBOL) TICKER,max(DeltaRtt) as DeltaRTT for rn in (1, 2, 3) ) ) tb1 ");
    queryBuilder.append(" left join ( ");
    queryBuilder.append(" select * from ( ");
    queryBuilder.append(" select CUSTODYCD Cus, SYMBOL,DeltaRTT, rn,sumDelta sumDeltaBonus,case when RTT_AFTER_SUM_DELTA_BONUS <=0 then 0 else RTT_AFTER_SUM_DELTA_BONUS end as RTT_AFTER_SUM_DELTA_BONUS ,STATUS_BONUS,TOTAL_ASSETS_BONUS,case when STATUS_BONUS <> 'Normal' then CEIL(NET_DEBT - TOTAL_ASSETS_BONUS / (MRM_RATE/100)) end as MONEY_ADDED_TOTAL_BONUS,round(TOTAL_ASSETS_BONUS / NET_DEBT * 100, 2) as RTT_AFTER_BONUS ");
    queryBuilder.append(" from ( ");
    queryBuilder.append(" SELECT CUSTODYCD, AFACCTNO,FULL_NAME,MRI_RATE,MRM_RATE, MARGIN_RATE, MRL_RATE,TOTAL_VND,NET_DEBT,SYMBOL, DEBT, CASH,SEASS, ");
    queryBuilder.append(" DeltaRTT, ROW_NUMBER() over (PARTITION BY CUSTODYCD order by DeltaRTT asc, SYMBOL asc) as rn, sumDelta,RTT_AFTER_SUM_DELTA_BONUS,case when RTT_AFTER_SUM_DELTA_BONUS < MRL_RATE then 'Force Sell' ");
    queryBuilder.append(" when (MRL_RATE <= RTT_AFTER_SUM_DELTA_BONUS and RTT_AFTER_SUM_DELTA_BONUS < MRM_RATE) then 'Margin Call' ");
    queryBuilder.append(" when RTT_AFTER_SUM_DELTA_BONUS >= MRM_RATE then 'Normal' end   as STATUS_BONUS,TOTAL_ASSETS_BONUS ");
    queryBuilder.append(" FROM ( ");
    queryBuilder.append(" select CUSTODYCD, AFACCTNO,FULL_NAME,MRI_RATE,MRM_RATE,MARGIN_RATE,MRL_RATE,TOTAL_VND,NET_DEBT,SYMBOL, DEBT, CASH,SEASS,sumDelta,TOTAL_ASSETS_BONUS,case when NET_DEBT <> 0 then round(DeltaRtt/abs(NET_DEBT) *100,3) else 0 end  DeltaRTT, round(sumDelta/NET_DEBT*100 + TOTAL_ASSETS_BEFORE_BONUS/NET_DEBT*100,2) as  RTT_AFTER_SUM_DELTA_BONUS ");
    queryBuilder.append(" from ");
    queryBuilder.append(" (select CUSTODYCD, AFACCTNO,FULL_NAME,MRI_RATE,MRM_RATE,MARGIN_RATE,MRL_RATE,TOTAL_VND,NET_DEBT,SYMBOL, DEBT, CASH,SEASS, ");
    queryBuilder.append(" (PRICEAFTER * LOAN_RATIO_AFTER/100 *(CAASS_AFTER + ASSET) + CAAMT_AFTER) - (PRICEBEFORE * LOAN_RATIO_BEFORE/100* (ASSET+CAASS_BEFORE)+ CAAMT_BEFORE)    as DeltaRtt ");
    queryBuilder.append(" from temp) a ");
    queryBuilder.append(" left join ");
    queryBuilder.append(" (select CUSTODYCD Cus,sum((PRICEAFTER * LOAN_RATIO_AFTER/100*(CAASS_AFTER + ASSET)+ CAAMT_AFTER) - (PRICEBEFORE * LOAN_RATIO_BEFORE/100 * (ASSET+CAASS_BEFORE)+ CAAMT_BEFORE))  as sumDelta, (sum(PRICEAFTER * LOAN_RATIO_AFTER/100 * (ASSET+CAASS_AFTER)) + sum(CAAMT_AFTER)) TOTAL_ASSETS_BONUS, sum(PRICEBEFORE * LOAN_RATIO_BEFORE/100 * (ASSET+CAASS_BEFORE) + CAAMT_BEFORE) TOTAL_ASSETS_BEFORE_BONUS ");
    queryBuilder.append(" from temp ");
    queryBuilder.append(" group by CUSTODYCD) b on a.CUSTODYCD = b.Cus ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" ) ");
    queryBuilder.append(" where rn <= 3) ");
    queryBuilder.append(" pivot (max(SYMBOL) TICKER_BONUS,max(DeltaRtt) as DeltaRTT_BONUS for rn in (1, 2, 3) )) tb2 on tb1.CUSTODYCD = tb2.Cus ");


    try {

      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("etlUpdateDate", etlUpdateDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }
}
