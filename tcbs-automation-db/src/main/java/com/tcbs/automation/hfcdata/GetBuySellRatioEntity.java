package com.tcbs.automation.hfcdata;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor


public class GetBuySellRatioEntity {
  private static final Logger logger = LoggerFactory.getLogger(GetBuySellRatioEntity.class);
  @Id
  private String ticker;
  private String tickerVi;
  private Integer bull;
  private Integer bear;
  private String tradingDate;
  private String end;

  @Step("Get data")
  public static HashMap<String, Object> getBuySellRatio(String scale, String ticker, String fromDate, String toDate) {
    StringBuilder queryBuilder = new StringBuilder();

    if (scale.equalsIgnoreCase("1")) {

      queryBuilder.append(
        " select SYMBOL, SYMBOLVI, round(sum(NoBuyOrder)/sum(NoBuyOrder + NoSellOrder)*100, 0) as BULL, 100 - round(sum(NoBuyOrder)/sum(NoBuyOrder + NoSellOrder)*100, 0) as BEAR, TRADINGDATE, TRADINGDATETO ");
      queryBuilder.append(" from ( ");
      queryBuilder.append(
        " select SYMBOL, SYMBOL  as SYMBOLVI, NoBuyOrder, NoSellOrder, :fromDate as TRADINGDATE , :toDate as TRADINGDATETO  ");
      queryBuilder.append(" FROM SMY_DWH_FLX_TCBS_SENTIMENT ");
      queryBuilder.append(" WHERE SYMBOL = :ticker ");
      queryBuilder.append(" AND to_char(\"DATE\", 'yyyy-mm-dd') >= :fromDate and to_char(\"DATE\", 'yyyy-mm-dd') <=  :toDate ) ");
      queryBuilder.append(" group by SYMBOL, SYMBOLVI, TRADINGDATE, TRADINGDATETO ");

    } else if (scale.equalsIgnoreCase("2")) {

      queryBuilder.append(
        "  select SYMBOL, SYMBOLVI, round(sum(NoBuyOrder)/sum(NoBuyOrder + NoSellOrder)*100, 0) as BULL, 100 - round(sum(NoBuyOrder)/sum(NoBuyOrder + NoSellOrder)*100, 0) as BEAR, TRADINGDATE , TRADINGDATETO ");
      queryBuilder.append("  from ( ");
      queryBuilder.append(
        "  select NAMEENLV2 as SYMBOL, NAMELV2  as SYMBOLVI, NoBuyOrder, NoSellOrder, :fromDate as TRADINGDATE , :toDate as TRADINGDATETO ");
      queryBuilder.append("  from SMY_DWH_FLX_TCBS_SENTIMENT a, Smy_dwh_stox_CompanyIndustryRate b ");
      queryBuilder.append("  where a.SYMBOL = b.TICKER ");
      queryBuilder.append("  AND to_char(\"DATE\", 'yyyy-mm-dd') >= :fromDate and to_char(\"DATE\", 'yyyy-mm-dd') <=  :toDate  ");
      queryBuilder.append("  and a.SYMBOL in (select TICKER from Smy_dwh_stox_CompanyIndustryRate ");
      queryBuilder.append("  where IDLEVEL2 in ");
      queryBuilder.append("  (select IDLEVEL2 from Smy_dwh_stox_CompanyIndustryRate where ticker = :ticker))) ");
      queryBuilder.append("  group by SYMBOL, SYMBOLVI,TRADINGDATE, TRADINGDATETO  ");


    } else if (scale.equalsIgnoreCase("3")) {
      queryBuilder.append(
        "   select SYMBOL, SYMBOLVI, round(sum(NoBuyOrder)/sum(NoBuyOrder + NoSellOrder)*100, 0) as BULL, 100 - round(sum(NoBuyOrder)/sum(NoBuyOrder + NoSellOrder)*100, 0) as BEAR, TRADINGDATE ,TRADINGDATETO  ");
      queryBuilder.append("   from ( select 'TCBSINVESTOR' SYMBOL, 'TCBSINVESTOR' as SYMBOLVI, NOBUYORDER, NOSELLORDER,  ");
      queryBuilder.append(
        " :fromDate as TRADINGDATE , :toDate as TRADINGDATETO ");
      queryBuilder.append(" from SMY_DWH_FLX_TCBS_SENTIMENT  ");
      queryBuilder.append(" where to_char(\"DATE\", 'yyyy-mm-dd') >= :fromDate and to_char(\"DATE\", 'yyyy-mm-dd') <=  :toDate  )  ");
      queryBuilder.append(" group by SYMBOL, SYMBOLVI, TRADINGDATE, TRADINGDATETO  ");
    }

    try {
      if (scale.equalsIgnoreCase("3")) {

        List<HashMap<String, Object>> listResult = HfcData.hfcDataDbConnection.getSession().createNativeQuery(queryBuilder.toString())
          .setParameter("fromDate", fromDate)
          .setParameter("toDate", toDate)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
        if (listResult.size() > 0) {
          return listResult.get(0);
        }
      } else {
        List<HashMap<String, Object>> listResult = HfcData.hfcDataDbConnection.getSession().createNativeQuery(queryBuilder.toString())
          .setParameter("ticker", ticker)
          .setParameter("fromDate", fromDate)
          .setParameter("toDate", toDate)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
        if (listResult.size() > 0) {
          return listResult.get(0);
        }

      }


    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new HashMap<>();
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
