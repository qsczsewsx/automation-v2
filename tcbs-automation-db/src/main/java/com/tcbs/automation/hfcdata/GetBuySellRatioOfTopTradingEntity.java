package com.tcbs.automation.hfcdata;

import lombok.*;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class GetBuySellRatioOfTopTradingEntity {
  private static final Logger logger = LoggerFactory.getLogger(GetBuySellRatioOfTopTradingEntity.class);
  @Id
  private String ticker;
  private String tickerVi;
  private Double bull;
  private Double bear;
  private String tradingDate;
  private String end;
  private String timeFrame;

  @Step("Get data")
  public static List<HashMap<String, Object>> getBuySellRatioOfTopTrading(String scale, String ticker, String timeFrame, String fromDate, String toDate) {
    StringBuilder queryBuilder = new StringBuilder();

    if (scale.equalsIgnoreCase("1")) {
      queryBuilder.append(" select SYMBOL, SYMBOLVI, sum(TCBS_BUYAMT) as BULL, sum(TCBS_SELLAMT) as BEAR, TRADINGDATE, \"END\"  ");
      queryBuilder.append(" from ( select SYMBOL,SYMBOL as SYMBOLVI,TCBS_BUYAMT, TCBS_SELLAMT , :fromDate as TRADINGDATE,  :toDate as \"END\" ");
      queryBuilder.append("  from SMY_DWH_FLX_TCBS_SENTIMENT ");
      queryBuilder.append(" where to_char (\"DATE\",'yyyy-mm-dd') >= :fromDate and to_char (\"DATE\",'yyyy-mm-dd') <= :toDate and length(SYMBOL) = 3 ) ");
      queryBuilder.append(" group by TRADINGDATE,SYMBOL,SYMBOLVI, \"END\" ");
      queryBuilder.append(" order by (SUM(TCBS_BUYAMT) + sum(TCBS_SELLAMT)) desc, SYMBOL asc ");
      queryBuilder.append(" FETCH NEXT 20 ROWS ONLY  ");

    } else if (scale.equalsIgnoreCase("2")) {
      queryBuilder.append("  select SYMBOL,SYMBOLVI,sum(TCBS_BUYAMT) as BULL, sum(TCBS_SELLAMT) as BEAR, TRADINGDATE, \"END\" ");
      queryBuilder.append("  from ( select SYMBOL as SYMBOL,SYMBOL as SYMBOLVI, TCBS_BUYAMT,TCBS_SELLAMT,:fromDate as TRADINGDATE,  :toDate as \"END\" ");
      queryBuilder.append("  from SMY_DWH_FLX_TCBS_SENTIMENT a, Smy_dwh_stox_CompanyIndustryRate b ");
      queryBuilder.append("  where a.SYMBOL = b.TICKER and to_char (\"DATE\",'yyyy-mm-dd') >= :fromDate and to_char (\"DATE\",'yyyy-mm-dd') <= :toDate ");
      queryBuilder.append("  and length(SYMBOL) = 3 ");
      queryBuilder.append("  and IDLEVEL2 = (select IDLEVEL2 from Smy_dwh_stox_CompanyIndustryRate where ticker = :ticker )) ");
      queryBuilder.append("  group by TRADINGDATE,SYMBOL,SYMBOLVI ,  \"END\"");
      queryBuilder.append("  order by (sum(TCBS_BUYAMT) + sum(TCBS_SELLAMT)) desc,SYMBOL asc ");
      queryBuilder.append("  FETCH NEXT 20 ROWS ONLY ");


    } else if (scale.equalsIgnoreCase("3")) {
      queryBuilder.append("   select SYMBOL,SYMBOLVI,sum(TCBS_BUYAMT) as BULL, sum(TCBS_SELLAMT) as BEAR, TRADINGDATE, \"END\" ");
      queryBuilder.append("   from( ");
      queryBuilder.append("   select NAMEENLV2 as SYMBOL,NAMELV2 as SYMBOLVI,TCBS_BUYAMT, TCBS_SELLAMT, :fromDate as TRADINGDATE,  :toDate as \"END\" ");
      queryBuilder.append("   from SMY_DWH_FLX_TCBS_SENTIMENT a, Smy_dwh_stox_CompanyIndustryRate b ");
      queryBuilder.append("   where a.SYMBOL = b.TICKER and to_char (\"DATE\",'yyyy-mm-dd') >= :fromDate and to_char (\"DATE\",'yyyy-mm-dd') <= :toDate ) ");
      queryBuilder.append("   group by SYMBOL,TRADINGDATE,SYMBOL,SYMBOLVI, \"END\" ");
      queryBuilder.append("   order by (sum(TCBS_BUYAMT) + sum(TCBS_SELLAMT)) desc, SYMBOL asc ");
      queryBuilder.append("   FETCH NEXT 10 ROWS ONLY ");
    }

    try {

      if (scale.equalsIgnoreCase("2")) {
        return HfcData.hfcDataDbConnection.getSession().createNativeQuery(queryBuilder.toString())
          .setParameter("ticker", ticker)
          .setParameter("fromDate", fromDate)
          .setParameter("toDate", toDate)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      } else {
        return HfcData.hfcDataDbConnection.getSession().createNativeQuery(queryBuilder.toString())
          .setParameter("fromDate", fromDate)
          .setParameter("toDate", toDate)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      }


    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("get ticker latest day from db")
  public static List<HashMap<String, Object>> getLastestDay() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" select max(\"DATE\") as lastest_date ");
    queryStringBuilder.append(" from SMY_DWH_FLX_TCBS_SENTIMENT ");
    try {
      return HfcData.hfcDataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}