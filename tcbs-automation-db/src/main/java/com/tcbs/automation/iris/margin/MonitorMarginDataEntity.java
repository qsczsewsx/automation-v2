package com.tcbs.automation.iris.margin;

import com.tcbs.automation.staging.AwsStagingDwh;
import com.tcbs.automation.stoxplus.Stoxplus;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MonitorMarginDataEntity {

  @Step("Get list ticker")
  public static List<HashMap<String, Object>> getData(String etlcurdate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select count(*) records , max(CAST(CAST(etlcurdate AS VARCHAR(50)) AS DATE)) maxDate, 'ris_el_bonus_shares' tbl, 'truncate' etlType,  ");
    queryBuilder.append(" (select count(*) from ris_el_bonus_shares where CAST(CAST(etlcurdate AS VARCHAR(50)) AS DATE)  = :etlcurdate )totalDay  ");
    queryBuilder.append(" from ris_el_bonus_shares union all   ");
    queryBuilder.append(" select count(*) records , max(CAST(CAST(etlcurdate AS VARCHAR(50)) AS DATE)) maxDate, 'ris_el_portfolio' tbl, 'append' etlType,  ");
    queryBuilder.append(" (select count(*) from ris_el_portfolio where CAST(CAST(etlcurdate AS VARCHAR(50)) AS DATE) = :etlcurdate ) totalDay  ");
    queryBuilder.append(" from ris_el_portfolio union all ");
    queryBuilder.append(" select count(*) records , MAX(TXDATE_FLEX)  maxDate, 'stg_flx_020012' tbl, 'truncate' etlType,  ");
    queryBuilder.append(" (select count(*) from stg_flx_020012 where TXDATE_FLEX = :etlcurdate ) totalDay  ");
    queryBuilder.append(" from stg_flx_020012 union all ");
    queryBuilder.append(" select count(*) records , max(TXDATE_FLEX) maxDate, 'stg_flx_020023' tbl, 'append' etlType,  ");
    queryBuilder.append(" (select count(*) from stg_flx_020023 where TXDATE_FLEX = :etlcurdate ) totalDay  ");
    queryBuilder.append(" from stg_flx_020023 union all  ");
    queryBuilder.append(" select count(*) records , max(CAST(CAST(EtlCurDate  AS VARCHAR(50)) AS DATE)) maxDate , 'Stg_flx_020201' tbl, 'truncate' etlType,  ");
    queryBuilder.append(" (select count(*) from Stg_flx_020201 where CAST(CAST(EtlCurDate AS VARCHAR(50)) AS DATE)  = :etlcurdate ) totalDay  ");
    queryBuilder.append(" from Stg_flx_020201 union all  ");
    queryBuilder.append(" select count(*) records , MAX(TXDATE) AS maxDate, 'Stg_flx_ADSCHD' tbl, 'append' etlType,  ");
    queryBuilder.append(" (select count(*) from Stg_flx_ADSCHD where TXDATE = :etlcurdate ) totalDay  ");
    queryBuilder.append(" from Stg_flx_ADSCHD union all ");
    queryBuilder.append(" select count(*) records , MAX(CONVERT(date,TXDATE,121))   maxDate, 'stg_flx_ci0015' tbl, 'append' etlType,  ");
    queryBuilder.append(" (select count(*) from stg_flx_ci0015 where CONVERT(date,TXDATE,121) = :etlcurdate ) totalDay  ");
    queryBuilder.append(" from stg_flx_ci0015 union all ");
    queryBuilder.append(" select count(*) records ,  MAX(CONVERT(date,TXDATE,121))  maxDate, 'stg_flx_ci0017' tbl, 'append' etlType,  ");
    queryBuilder.append(" (select count(*) from stg_flx_ci0017 where CONVERT(date,TXDATE,121) = :etlcurdate ) totalDay  ");
    queryBuilder.append(" from stg_flx_ci0017 union all  ");
    queryBuilder.append(" select count(*) records , max(CONVERT(date,DATEREPORT,121)) maxDate, 'Stg_flx_MR0013' tbl, 'append' etlType,  ");
    queryBuilder.append(" (select count(*) from Stg_flx_MR0013 where CONVERT(date,DATEREPORT,121) = :etlcurdate ) totalDay  ");
    queryBuilder.append(" from Stg_flx_MR0013 union all  ");
    queryBuilder.append(" select count(*) records , max(TXDATE_FLEX) maxDate, 'Stg_flx_MR0015' tbl, 'append' etlType,  ");
    queryBuilder.append(" (select count(*) from Stg_flx_MR0015 where TXDATE_FLEX = :etlcurdate ) totalDay  ");
    queryBuilder.append(" from Stg_flx_MR0015 union all  ");
    queryBuilder.append(" select count(*) records ,max(CAST(CAST(EtlCurDate AS VARCHAR(50)) AS DATE)) maxDate, 'Stg_flx_VW_MR0001' tbl, 'truncate' etlType,  ");
    queryBuilder.append(" (select count(*) from Stg_flx_VW_MR0001 where CAST(CAST(EtlCurDate AS VARCHAR(50)) AS DATE) = :etlcurdate ) totalDay  ");
    queryBuilder.append(" from Stg_flx_VW_MR0001  ");
    try {

      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("etlcurdate", etlcurdate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get market price")
  public static List<HashMap<String, Object>> getMarketPrice(String etlUpdateDate) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select  ");
    queryBuilder.append(
      " (select count(*) from Smy_dwh_stox_MarketPrices where CONVERT(NVARCHAR,trading_Date,23) = (select dbo.businessDaysAdd(-1, CAST(:etlUpdateDate AS DATE)))) TOTAL_MARKET_PRICE,  ");
    queryBuilder.append(" (select max(trading_Date) from Smy_dwh_stox_MarketPrices ) TOTAL_MARKET_PRICE_MAX_DATE  ");
    try {

      return Stoxplus.stoxDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("etlUpdateDate", etlUpdateDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get market price")
  public static List<HashMap<String, Object>> getResult() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from iris_margin_check_valid where checkDate = (select max(checkDate) from iris_margin_check_valid) ");
    try {

      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get da5 ")
  public static List<HashMap<String, Object>> getDa5(String fromDate) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" select avg(a.totalDay) da5,a.tbl from (  ");
    queryBuilder.append(" select *, ROW_NUMBER() OVER (   PARTITION BY tbl order by checkDate desc) rn  ");
    queryBuilder.append(" from iris_margin_check_valid  ");
    queryBuilder.append(" where checkDate >= :fromDate  ");
    queryBuilder.append(" and totalDay >0  ");
    queryBuilder.append(" ) a  ");
    queryBuilder.append(" where rn >=1 and rn <=5  ");
    queryBuilder.append(" group by a.tbl  ");
    try {

      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


}