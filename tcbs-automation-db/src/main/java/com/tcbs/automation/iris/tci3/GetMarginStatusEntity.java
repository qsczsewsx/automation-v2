package com.tcbs.automation.iris.tci3;

import com.tcbs.automation.staging.AwsStagingDwh;
import com.tcbs.automation.stoxplus.Stoxplus;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetMarginStatusEntity {


  @Step("Get data full ")
  public static List<HashMap<String, Object>> get00120023(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" select *  ");
    queryBuilder.append(" from (select SYMBOL,MRPRICERATE Price0012  from Stg_flx_020012 where SYMBOL = :ticker ) a  ");
    queryBuilder.append(" left join  ");
    queryBuilder.append("   ");
    queryBuilder.append(" (  ");
    queryBuilder.append(" select SYMBOL TICKER,max(MRPRICERATE) Price0023, max (MRRATIOLOAN) Ratio0023 from Stg_flx_020023  ");
    queryBuilder.append(" where SYMBOL = :ticker  ");
    queryBuilder.append(" group by  SYMBOL) b on a.SYMBOL = b.TICKER  ");
    try {

      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Get full 0012")
  public static List<HashMap<String, Object>> getFull0012() {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" select * ");
    queryBuilder.append("  from Stg_flx_020012  ");
    try {

      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Get full 0012")
  public static List<HashMap<String, Object>> getExchange(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select ticker, case when ComGroupCode='VNINDEX' then 'HSX' ");
    queryBuilder.append("  when ComGroupCode = 'HNXIndex' then 'HNX' ");
    queryBuilder.append("   when ComGroupCode = 'UpcomIndex' then 'UPC' ");
    queryBuilder.append("   else 'OTC'   end Exchange  ");
    queryBuilder.append(" from  stx_cpf_Organization where ticker = :ticker ");
    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get full 0012")
  public static List<HashMap<String, Object>> getTickerBonus() {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" SELECT DISTINCT symbol  FROM ris_el_bonus_shares ");
    queryBuilder.append(" WHERE etlcurdate = (SELECT MAX(etlcurdate) FROM ris_el_bonus_shares)  ");
    queryBuilder.append(" AND approved = 1  ");

    try {

      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }


}