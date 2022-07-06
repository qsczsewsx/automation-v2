package com.tcbs.automation.iris.impact;

import com.tcbs.automation.iris.AwsIRis;
import com.tcbs.automation.staging.AwsStagingDwh;
import com.tcbs.automation.stoxplus.Stoxplus;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SyncStockEntity {
  @Step("Get stock from BPM")
  public static List<HashMap<String, Object>> getStockBpm(String etlUpdateDate) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(
      " select CUSTODYCD, AFACCTNO, SYMBOL, ASSET, MARKET_PRICE, LOAN_PRICE_BEFORE,LOAN_RATIO_BEFORE,LOAN_RATIO_AFTER,LOAN_PRICE_AFTER,nvl(CAASS_BEFORE,0) CAASS_BEFORE, nvl(CAAMT_BEFORE,0) CAAMT_BEFORE, nvl(CAASS_AFTER,0) CAASS_AFTER, nvl(CAAMT_AFTER,0) CAAMT_AFTER, AVG_PRICE ");
    queryBuilder.append("   from RISK_STOCK where ETL_UPDATE_DATE = :etlUpdateDate ORDER BY CUSTODYCD, SYMBOL");
    try {

      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("etlUpdateDate", etlUpdateDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }


  @Step("Get quantity from STG")
  public static List<HashMap<String, Object>> getQuantity() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT distinct b.CUSTODYCD, b.SYMBOL,TRADE + RECEIVING + BUYQTTY as 'ASSET', a.ACTYPE, AFACCTNO ");
    queryBuilder.append(" from Stg_flx_VW_MR0001 a, Stg_flx_MR0015 b   ");
    queryBuilder.append(" where b.TXDATE = (select max(TXDATE) from Stg_flx_MR0015) ");
    queryBuilder.append(" and a.CUSTODYCD = b.CUSTODYCD and b.AFACCTNO = a.ACCTNO and b.SYMBOL <> 'TCB' and len(b.SYMBOL) = 3  ORDER BY CUSTODYCD, SYMBOL");
    try {

      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Get market price")
  public static List<HashMap<String, Object>> getMarketPrice() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("  select a.Ticker 'SYMBOL', a.tradingDate, a.ClosePriceAdjusted  ");
    queryBuilder.append(" from Smy_dwh_stox_MarketPrices a,  ");
    queryBuilder.append(" (select  Ticker, max(tradingDate) trading_date  ");
    queryBuilder.append("  from Smy_dwh_stox_MarketPrices  ");
    queryBuilder.append("  group by Ticker ) b  ");
    queryBuilder.append(" where a.Ticker = b.Ticker and a.tradingDate = b.trading_date and len(a.Ticker) = 3 and a.Ticker <> 'TCB'   ");
    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get basic price")
  public static List<HashMap<String, Object>> getBasicPrice(String effectiveDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("  select a.Ticker 'SYMBOL', a.tradingDate, a.ClosePriceAdjusted   ");
    queryBuilder.append("  from Smy_dwh_stox_MarketPrices a,  ");
    queryBuilder.append("  (select  Ticker, max(tradingDate) trading_date  ");
    queryBuilder.append("  from Smy_dwh_stox_MarketPrices where tradingDate < CONVERT(DATE, :effectiveDate, 112)  ");
    queryBuilder.append("  group by Ticker ) b  ");
    queryBuilder.append("  where a.Ticker = b.Ticker and a.tradingDate = b.trading_date and len(a.Ticker) = 3 and a.Ticker <> 'TCB'  ");
    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("effectiveDate", effectiveDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get tx date flex")
  public static List<HashMap<String, Object>> getTxDateFlex() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("  select max(EtlCurDate) TXDATE_FLEX  from Stg_flx_VW_MR0001 ");
    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get loan and ratio from 0023")
  public static List<HashMap<String, Object>> getLoanRate() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("  select a.SYMBOL,a.MRRATIORATE, b.MRPRICELOAN 'price0012',a.MRPRICELOAN 'price0023', AFTYPE   ");
    queryBuilder.append(" from Stg_flx_020023 a, Stg_flx_020012 b   ");
    queryBuilder.append(" where a.SYMBOL = b.symbol   ");

    try {

      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Get list cus")
  public static List<HashMap<String, Object>> getListCus() {

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("  select * from RISK_CUSTOMER where ETL_UPDATE_DATE = (Select max(ETL_UPDATE_DATE) from RISK_CUSTOMER) ");
    try {
      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Get max Etl")
  public static List<HashMap<String, Object>> getMaxEtlUpdate() {

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("  select max(ETL_UPDATE_DATE) ETL_UPDATE_DATE from RISK_CUSTOMER  ");
    try {
      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Get loanPrice from Full list")
  public static List<HashMap<String, Object>> getFullList() {

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("  select * from RISK_ANALYST_MARGIN_REVIEWED_FULL   ");
    try {
      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Get data mới")
  public static List<HashMap<String, Object>> getData(Integer affectedDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" with Stg_0023 as (  ");
    queryBuilder.append(" select a.SYMBOL, a.MRPRICELOAN as 'Price0023', b.MRPRICELOAN as 'Price0012', a.MRRATIORATE, AFTYPE, a.TXDATE_FLEX  ");
    queryBuilder.append(" from Stg_flx_020023 a,  ");
    queryBuilder.append(" Stg_flx_020012 b  ");
    queryBuilder.append(" where a.SYMBOL = b.SYMBOL  ");
    queryBuilder.append(" and a.TXDATE_FLEX = (select max(TXDATE_FLEX) from Stg_flx_020023)  ");
    queryBuilder.append(" and b.TXDATE_FLEX = (select max(TXDATE_FLEX) from Stg_flx_020012)  ");
    queryBuilder.append(" and a.TXDATE_FLEX = b.TXDATE_FLEX  ");
    queryBuilder.append(" ),  ");
    queryBuilder.append(" temp as (  ");
    queryBuilder.append(" select  AFACCTNO,  ");
    queryBuilder.append(" a.SYMBOL,  ");
    queryBuilder.append(" (TRADE + RECEIVING + BUYQTTY) ASSET,  ");
    queryBuilder.append(" 0        as                   caass_before,  ");
    queryBuilder.append(" 0        as                   caass_after,  ");
    queryBuilder.append(" 'Normal' as                   TYPE,  ");
    queryBuilder.append(" 0        as                   caamt_before,  ");
    queryBuilder.append(" 0        as                   caamt_after,  ");
    queryBuilder.append(" AVGPRICE  ");
    queryBuilder.append(" from Stg_flx_MR0015 a,  ");
    queryBuilder.append(" Stg_flx_VW_MR0001 b  ");
    queryBuilder.append(" where TXDATE = (select max(TXDATE) from Stg_flx_MR0015)  ");
    queryBuilder.append(" and CONVERT(VARCHAR, a.TXDATE_FLEX, 112) = b.EtlCurDate  ");
    queryBuilder.append(" and a.CUSTODYCD = b.CUSTODYCD  ");
    queryBuilder.append(" and a.AFACCTNO = b.ACCTNO  ");
    queryBuilder.append(" and len(a.SYMBOL) = 3  ");
    queryBuilder.append(" and a.SYMBOL != 'TCB'  ");
    queryBuilder.append(" ),  ");
    queryBuilder.append(" uni_temp as (  ");
    queryBuilder.append(" select *  ");
    queryBuilder.append(" from temp  ");
    queryBuilder.append(" union all  ");
    queryBuilder.append(" (  ");
    queryBuilder.append(" select afacctno             AFACCTNO,  ");
    queryBuilder.append(" symbol               SYMBOL,  ");
    queryBuilder.append(" 0                 as ASSET,  ");
    queryBuilder.append(" sum(caass_before) as caass_before,  ");
    queryBuilder.append(" sum(caass_after)  as caass_after,  ");
    queryBuilder.append(" 'Bonus'           as TYPE,  ");
    queryBuilder.append(" sum(caamt_before) as caamt_before,  ");
    queryBuilder.append(" sum(caamt_after)  as caamt_after,  ");
    queryBuilder.append(" 0 as AVGPRICE  ");
    queryBuilder.append(" from (  ");
    queryBuilder.append(" select afacctno,  ");
    queryBuilder.append(" symbol,  ");
    queryBuilder.append(" case  ");
    queryBuilder.append(" when ((:affectedDate < exdateInt) or  ");
    queryBuilder.append(" (TXDATE_FLEX < exdateInt and exdateInt <= :affectedDate)) then 0  ");
    queryBuilder.append(" when exdateInt <= TXDATE_FLEX then caass end as caass_before,  ");
    queryBuilder.append(" case  ");
    queryBuilder.append(" when ((:affectedDate < exdateInt) or  ");
    queryBuilder.append(" (TXDATE_FLEX < exdateInt and exdateInt <= :affectedDate)) then 0  ");
    queryBuilder.append(" when exdateInt <= TXDATE_FLEX then caamt end as caamt_before,  ");
    queryBuilder.append(" case  ");
    queryBuilder.append(" when (:affectedDate < exdateInt) then 0  ");
    queryBuilder.append(" when ((TXDATE_FLEX < exdateInt and exdateInt <= :affectedDate) or  ");
    queryBuilder.append(" exdateInt <= TXDATE_FLEX)  ");
    queryBuilder.append(" then caass end                           as caass_after,  ");
    queryBuilder.append(" case  ");
    queryBuilder.append(" when (:affectedDate < exdateInt) then 0  ");
    queryBuilder.append(" when ((TXDATE_FLEX < exdateInt and exdateInt <= :affectedDate) or  ");
    queryBuilder.append(" exdateInt <= TXDATE_FLEX)  ");
    queryBuilder.append(" then caamt end                           as caamt_after  ");
    queryBuilder.append("   ");
    queryBuilder.append(" from (  ");
    queryBuilder.append(" select *,  ");
    queryBuilder.append(" CONVERT(VARCHAR, exdate, 112)                                 exdateInt,  ");
    queryBuilder.append(" (select max(EtlCurDate) EtlCurDate from Stg_flx_VW_MR0001) as TXDATE_FLEX  ");
    queryBuilder.append(" from ris_el_bonus_shares a  ");
    queryBuilder.append(" where etlcurdate = (select max(etlcurdate) from ris_el_bonus_shares)  ");
    queryBuilder.append(" and cast(enddate as date) >= cast(getdate() as date)  ");
    queryBuilder.append(" and approved = 1  ");
    queryBuilder.append(" ) a  ");
    queryBuilder.append(" ) bonus  ");
    queryBuilder.append(" group by afacctno, symbol  ");
    queryBuilder.append(" )  ");
    queryBuilder.append(" ),  ");
    queryBuilder.append(" uni as (  ");
    queryBuilder.append(" select  AFACCTNO,  ");
    queryBuilder.append(" SYMBOL,  ");
    queryBuilder.append(" sum(ASSET) as     ASSET,  ");
    queryBuilder.append(" sum(caass_before) caass_before,  ");
    queryBuilder.append(" sum(caamt_before) caamt_before,  ");
    queryBuilder.append(" sum(caass_after)  caass_after,  ");
    queryBuilder.append(" sum(caamt_after)  caamt_after,  ");
    queryBuilder.append(" sum(AVGPRICE) AVGPRICE  ");
    queryBuilder.append(" from uni_temp  ");
    queryBuilder.append(" group by AFACCTNO, SYMBOL  ");
    queryBuilder.append(" )  ");
    queryBuilder.append("   ");
    queryBuilder.append(" select MR01.CUSTODYCD,  ");
    queryBuilder.append(" uni.AFACCTNO                                     AFACCTNO,  ");
    queryBuilder.append(" Stg_0023.SYMBOL,  ");
    queryBuilder.append(" ACTYPE,  ");
    queryBuilder.append(" uni.ASSET,  ");
    queryBuilder.append(" uni.caamt_before                                 CAAMT_BEFORE,  ");
    queryBuilder.append(" uni.caass_before                                 CAASS_BEFORE,  ");
    queryBuilder.append(" uni.caamt_after                                  CAAMT_AFTER,  ");
    queryBuilder.append(" uni.caass_after                                  CAASS_AFTER,  ");
    queryBuilder.append(" Price0023,  ");
    queryBuilder.append(" Price0012,  ");
    queryBuilder.append(" MRRATIORATE                                      loanRatio,  ");
    queryBuilder.append(" IIF(Price0012 > Price0023, Price0023, Price0012) loanPrice,  ");
    queryBuilder.append(" AVGPRICE  ");
    queryBuilder.append(" from Stg_flx_VW_MR0001 MR01,  ");
    queryBuilder.append(" Stg_0023,  ");
    queryBuilder.append(" uni  ");
    queryBuilder.append(" where MR01.ACCTNO = uni.AFACCTNO  ");
    queryBuilder.append(" and CONVERT(VARCHAR, Stg_0023.TXDATE_FLEX, 112) = MR01.EtlCurDate  ");
    queryBuilder.append(" and uni.SYMBOL = Stg_0023.SYMBOL  ");
    queryBuilder.append(" and MR01.ACTYPE = Stg_0023.AFTYPE   ");
    queryBuilder.append(" order by MR01.CUSTODYCD, Stg_0023.SYMBOL  ");

    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("affectedDate", affectedDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }


}