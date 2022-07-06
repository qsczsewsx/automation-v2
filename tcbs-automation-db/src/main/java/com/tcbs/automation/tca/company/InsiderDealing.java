package com.tcbs.automation.tca.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Id;
import com.tcbs.automation.tca.TcAnalysis;
import com.tcbs.automation.tca.ticker.TickerBasic;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsiderDealing {
  @Id // @Id for match
  private Long no;

  private String ticker;

  private String anDate;

  private Short dealingMethod;

  private String dealingAction;

  private Double quantity;
  private Double price;
  private Double ratio;

  @Step
  public static List<Map<String, Object>> getByTicker(String ticker, int page, int size) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("     SELECT no, ");
    queryBuilder.append("     ticker, ");
    queryBuilder.append("     anDate, ");
    queryBuilder.append("     AnDateJoiner, ");
    queryBuilder.append("     dealingMethod, ");
    queryBuilder.append("     dealingAction, ");
    queryBuilder.append("     CAST(quantity AS FLOAT) AS Quantity, ");
    queryBuilder.append("     CAST(ClosePrice AS float) AS ClosePrice ");
    queryBuilder.append(" FROM ");
    queryBuilder.append(" ( ");
    queryBuilder.append("     SELECT tbl.*,  ");
    queryBuilder.append("         IIF(mpri.ClosePriceAdjusted is null,  ");
    queryBuilder.append("                 (SELECT TOP 1 ClosePriceAdjusted  ");
    queryBuilder.append("                     FROM Smy_dwh_stox_MarketPrices  ");
    queryBuilder.append("                     WHERE TICKER = :ticker AND tradingDate < AnDateJoiner  ");
    queryBuilder.append("                     ORDER BY tradingDate DESC)  ");
    queryBuilder.append("                 , mpri.ClosePriceAdjusted) as ClosePrice ");
    queryBuilder.append("     FROM  ");
    queryBuilder.append("     ( ");
    queryBuilder.append("         SELECT Row_number() OVER(ORDER BY ISNULL(AnDate, 0) DESC) AS no,  ");
    queryBuilder.append("             :ticker as ticker, ");
    queryBuilder.append("             CONVERT(VARCHAR, AnDate, 3) AS anDate,  ");
    queryBuilder.append("             CONVERT(DATE, AnDate, 101) AnDateJoiner, ");
    queryBuilder.append("             DealMethod AS dealingMethod,  ");
    queryBuilder.append("             Action AS dealingAction,  ");
    queryBuilder.append("             quantity ");
    queryBuilder.append("         FROM   ( ");
    queryBuilder.append("             SELECT CASE  ");
    queryBuilder.append("                     WHEN DealTypeCode = 'MT' THEN 1 ");
    queryBuilder.append("                     WHEN DealTypeCode = 'IT' THEN 0 ");
    queryBuilder.append("                     WHEN DealTypeCode = 'RT' THEN 2 ");
    queryBuilder.append("                 END as DealMethod ");
    queryBuilder.append("                 , PublicDate as AnDate ");
    queryBuilder.append("                 , CASE  ");
    queryBuilder.append("                     WHEN ActionTypeCode = 'B' THEN '0' ");
    queryBuilder.append("                     WHEN ActionTypeCode = 'S' THEN '1' ");
    queryBuilder.append("                     ELSE ActionTypeCode ");
    queryBuilder.append("                 END AS Action ");
    queryBuilder.append("                 , ShareAcquire as quantity ");
    queryBuilder.append("             FROM stx_cpa_CorporateDeal ");
    queryBuilder.append("             WHERE  organCode = :ticker ");
    queryBuilder.append("             UNION ");
    queryBuilder.append("             SELECT CASE  ");
    queryBuilder.append("                     WHEN DealTypeCode = 'MT' THEN 1 ");
    queryBuilder.append("                     WHEN DealTypeCode = 'IT' THEN 0 ");
    queryBuilder.append("                     WHEN DealTypeCode = 'RT' THEN 2  ");
    queryBuilder.append("                 END as DealMethod ");
    queryBuilder.append("                 , PublicDate as AnDate ");
    queryBuilder.append("                 , CASE  ");
    queryBuilder.append("                     WHEN ActionTypeCode = 'B' THEN '0' ");
    queryBuilder.append("                     WHEN ActionTypeCode = 'S' THEN '1' ");
    queryBuilder.append("                     ELSE ActionTypeCode ");
    queryBuilder.append("                 END AS Action ");
    queryBuilder.append("                 , ShareAcquire as quantity ");
    queryBuilder.append("             FROM stx_cpa_PersonDeal ");
    queryBuilder.append("             WHERE  OrganCode = :ticker	 ");
    queryBuilder.append("         ) D  ");
    queryBuilder.append("         ORDER BY ISNULL(AnDate, 0) DESC ");
    queryBuilder.append("         OFFSET :size * :page ROWS FETCH NEXT :size ROWS ONLY   ");
    queryBuilder.append("     ) tbl ");
    queryBuilder.append("     LEFT JOIN Smy_dwh_stox_MarketPrices mpri ");
    queryBuilder.append("     ON tbl.Ticker = mpri.TICKER and tbl.AnDateJoiner = mpri.tradingDate 		 ");
    queryBuilder.append(" ) t ");
    queryBuilder.append(" ORDER BY no ASC ");

    List<Map<String, Object>> result = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
      .setParameter("ticker", TickerBasic.getTickerBasic(ticker).getOrganCode())
      .setParameter("size", size)
      .setParameter("page", page)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    return result;
  }

  public static Double getLatestClosePriceByTicker(String ticker) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("   SELECT TOP 1 ClosePriceAdjusted   ");
    queryStringBuilder.append("   FROM Smy_dwh_stox_MarketPrices   ");
    queryStringBuilder.append("   WHERE ticker = :ticker   ");
    queryStringBuilder.append("   ORDER BY tradingDate  desc ");

    List<Map<String, Object>> resultList = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setParameter("ticker", TickerBasic.getTickerBasic(ticker).getOrganCode())
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    if (resultList.size() > 0) {
      return ((BigDecimal) resultList.get(0).get("ClosePriceAdjusted")).doubleValue();
    } else {
      return null;
    }
  }

  public static Double getClosePriceByTicker(String ticker, Date anDate) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append(" SELECT TOP 1 ClosePriceAdjusted ");
    queryStringBuilder.append(" FROM Smy_dwh_stox_MarketPrices ");
    queryStringBuilder.append(" WHERE ticker = :tick AND tradingDate < :anDate ");
    queryStringBuilder.append(" ORDER BY tradingDate DESC ");

    List<Map<String, Object>> resultList = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setParameter("tick", TickerBasic.getTickerBasic(ticker).getOrganCode())
      .setParameter("anDate", anDate)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    if (resultList.size() > 0) {
      return ((BigDecimal) resultList.get(0).get("ClosePriceAdjusted")).doubleValue();
    } else {
      return null;
    }
  }
}
