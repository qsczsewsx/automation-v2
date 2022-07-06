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
public class GetBuySellRatioOverTimeEntity {
  private static final Logger logger = LoggerFactory.getLogger(GetBuySellRatioOverTimeEntity.class);
  private String ticker;
  private String tickerVi;
  private Integer bull;
  private Integer bear;
  @Id
  private String tradingDate;
  private String timeFrame;
  private String fromDate;
  private String toDate;

  @Step("Get data")
  public static List<HashMap<String, Object>> getBuySellRatioOverTime(String scale, String ticker, String fromDate, String toDate, String timeFrame) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("  SELECT * FROM ( ");
    queryBuilder.append("  select SYMBOL, SYMBOLVI, round(sum(NOBUYORDER)/sum(NOBUYORDER + NOSELLORDER)*100, 0) as BULL,  ");
    queryBuilder.append("         100 - round(sum(NoBuyOrder)/sum(NoBuyOrder + NoSellOrder)*100, 0) as BEAR, TRADINGDATE ");
    if (timeFrame.equalsIgnoreCase("1")) {
      if (scale.equalsIgnoreCase("1")) {

        queryBuilder.append("  from ( SELECT SYMBOL,SYMBOL   as SYMBOLVI,NOBUYORDER ,NOSELLORDER,to_char(\"DATE\", 'yyyy-mm-dd')    as TRADINGDATE ");
        queryBuilder.append("  FROM SMY_DWH_FLX_TCBS_SENTIMENT ");
        queryBuilder.append("  WHERE SYMBOL = :ticker ");
        queryBuilder.append("  AND to_char (\"DATE\",'yyyy-mm-dd') >= :fromDate ");
        queryBuilder.append("  AND to_char( \"DATE\",'yyyy-mm-dd') <= :toDate ) ");
        queryBuilder.append("  group by SYMBOL, SYMBOLVI, TRADINGDATE ");

      } else if (scale.equalsIgnoreCase("2")) {

        queryBuilder.append(" from (  select NAMEENLV2 as SYMBOL,NAMELV2 as SYMBOLVI, NOBUYORDER, NOSELLORDER, to_char(\"DATE\", 'yyyy-mm-dd') as TRADINGDATE ");
        queryBuilder.append(" from SMY_DWH_FLX_TCBS_SENTIMENT a, Smy_dwh_stox_CompanyIndustryRate b ");
        queryBuilder.append(" where a.SYMBOL = b.TICKER  and to_char (\"DATE\",'yyyy-mm-dd') >= :fromDate   AND to_char( \"DATE\",'yyyy-mm-dd') <= :toDate ");
        queryBuilder.append(" and a.SYMBOL in (select TICKER    from SMY_DWH_STOX_COMPANYINDUSTRYRATE ");
        queryBuilder.append(" where IDLEVEL2 in (select IDLEVEL2 from Smy_dwh_stox_CompanyIndustryRate where TICKER = :ticker ))) ");
        queryBuilder.append(" group by SYMBOL, SYMBOLVI,TRADINGDATE ");


      } else if (scale.equalsIgnoreCase("3")) {
        queryBuilder.append("  from ( ");
        queryBuilder.append(" select 'TCBSINVESTOR' as SYMBOL,'TCBSINVESTOR' as SYMBOLVI, NOBUYORDER, NOSELLORDER, to_char(\"DATE\", 'yyyy-mm-dd') as TRADINGDATE ");
        queryBuilder.append(" from SMY_DWH_FLX_TCBS_SENTIMENT  ");
        queryBuilder.append(" where  ");
        queryBuilder.append(" to_char (\"DATE\",'yyyy-mm-dd') >= :fromDate  ");
        queryBuilder.append(" AND to_char( \"DATE\",'yyyy-mm-dd') <= :toDate  AND LENGTH(SYMBOL) = 3 ) ");
        queryBuilder.append(" group by SYMBOL,SYMBOLVI, TRADINGDATE ");
      }
    } else if (timeFrame.equalsIgnoreCase("2")) {
      if (scale.equalsIgnoreCase("1")) {
        queryBuilder.append("  from ( SELECT SYMBOL,SYMBOL   as SYMBOLVI,NOBUYORDER ,NOSELLORDER,to_char(trunc( \"DATE\",'MONTH'),'yyyy-mm-dd')   as TRADINGDATE ");
        queryBuilder.append("  FROM SMY_DWH_FLX_TCBS_SENTIMENT ");
        queryBuilder.append("  WHERE SYMBOL = :ticker  AND to_char (\"DATE\",'yyyy-mm-dd') > :fromDate ");
        queryBuilder.append("  AND to_char( \"DATE\",'yyyy-mm-dd') <= :toDate) ");
        queryBuilder.append("  group by SYMBOL, SYMBOLVI, TRADINGDATE order by TRADINGDATE desc ");
      } else if (scale.equalsIgnoreCase("2")) {
        queryBuilder.append(" from ( ");
        queryBuilder.append(" select NAMEENLV2 as SYMBOL,NAMELV2 as SYMBOLVI, NOBUYORDER, NOSELLORDER, to_char(trunc( \"DATE\",'MONTH'),'yyyy-mm-dd') as TRADINGDATE ");
        queryBuilder.append(" from SMY_DWH_FLX_TCBS_SENTIMENT a, Smy_dwh_stox_CompanyIndustryRate b ");
        queryBuilder.append(" where a.SYMBOL = b.TICKER ");
        queryBuilder.append("  AND to_char (\"DATE\",'yyyy-mm-dd') >= :fromDate ");
        queryBuilder.append("  and to_char( \"DATE\",'yyyy-mm-dd') <= :toDate  ");
        queryBuilder.append("  and a.SYMBOL in (select TICKER ");
        queryBuilder.append("   from SMY_DWH_STOX_COMPANYINDUSTRYRATE ");
        queryBuilder.append("    where IDLEVEL2 in (select IDLEVEL2 from Smy_dwh_stox_CompanyIndustryRate where TICKER = :ticker ) AND LENGTH(TICKER) = 3)) ");
        queryBuilder.append(" group by SYMBOL,SYMBOLVI, TRADINGDATE ");
        queryBuilder.append(" order by TRADINGDATE desc ");


      } else if (scale.equalsIgnoreCase("3")) {

        queryBuilder.append("  from ( ");
        queryBuilder.append(" select 'TCBSINVESTOR' as SYMBOL,'TCBSINVESTOR' as SYMBOLVI, NOBUYORDER, NOSELLORDER, to_char(trunc( \"DATE\",'MONTH'),'yyyy-mm-dd') as TRADINGDATE ");
        queryBuilder.append(" from SMY_DWH_FLX_TCBS_SENTIMENT a ");
        queryBuilder.append(" where to_char (\"DATE\",'yyyy-mm-dd') >= :fromDate ");
        queryBuilder.append(" AND to_char( \"DATE\",'yyyy-mm-dd') <= :toDate AND LENGTH(SYMBOL) = 3 ) ");
        queryBuilder.append(" group by SYMBOL, SYMBOLVI, TRADINGDATE ");
        queryBuilder.append(" order by TRADINGDATE desc ");

      }
    }
    queryBuilder.append("  ) a ORDER BY TRADINGDATE DESC");

    try {
      if (scale.equalsIgnoreCase("3")) {
        return HfcData.hfcDataDbConnection.getSession().createNativeQuery(queryBuilder.toString())
          .setParameter("fromDate", fromDate)
          .setParameter("toDate", toDate)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      } else {
        return HfcData.hfcDataDbConnection.getSession().createNativeQuery(queryBuilder.toString())
          .setParameter("ticker", ticker)
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