package com.tcbs.automation.tca.finance;

import com.tcbs.automation.tca.TcAnalysis;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@Setter
public class CashFlow {
  private static final String YEARLY_STR = "yearly";
  private static final String TICKER_STR = "ticker";

  private String ticker;

  private Long quarter;

  private Long year;

  private Double investCost;

  private Double fromInvest;

  private Double fromFinancial;

  private Double fromSale;

  private Double freeCashFlow;

  @Step
  public static List<Map<String, Object>> getByStockCode(String stockCode, Integer yearly, Boolean isAll) {
    String quantity = "";
    String oderBy = "";
    String quantity4 = "";
    String oderBy4 = "";
 
    if(isAll == true){
      quantity = " SELECT Ticker, YearReport, LengthReport, ";
      oderBy = "";
      quantity4 = " SELECT * ";
      oderBy4 = "";
    } else {
      quantity = " SELECT top(:quantity) Ticker, YearReport, LengthReport, ";
      oderBy = " ORDER BY YearReport desc, LengthReport desc ";
      quantity4 = " SELECT top(:quantity*4) * ";
      oderBy4 = " ORDER BY YearReportCal desc, LengthReportCal desc ";
    }

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("  SELECT *    ");
    queryBuilder.append("    FROM    ");
    queryBuilder.append("      (    ");
    queryBuilder.append(quantity);
    queryBuilder.append("    cast(CFA18 as float)/1000000000 as [sale],    ");
    queryBuilder.append("    cast(CFA26 as float)/1000000000 as [invest],    ");
    queryBuilder.append("    cast(CFA34 as float)/1000000000 as [finance],    ");
    queryBuilder.append("    cast(CFA19 as float)/1000000000 as [investCost]    ");
    queryBuilder.append("  FROM STX_FSC_CASHFLOW    ");
    queryBuilder.append("  WHERE 	Ticker = :ticker    ");
    queryBuilder.append("    AND ( ( :yearly = 0 and LengthReport < 5 ) or ( :yearly = 1 and LengthReport = 5 ))    ");
    queryBuilder.append(oderBy);
    queryBuilder.append("  ) t2    ");
    queryBuilder.append("  LEFT JOIN    ");
    queryBuilder.append("  (    ");
    queryBuilder.append(quantity4);
    queryBuilder.append("  FROM (    ");
    queryBuilder.append("    SELECT row_number() over (partition by Ticker,  YearReportCal, LengthReportCal order by UpdateDate desc) as rn,    ");
    queryBuilder.append("    YearReportCal , LengthReportCal,    ");
    queryBuilder.append("    cast(RTD13 as float)/1000000000  as [freeCashFlow]    ");
    queryBuilder.append("    FROM	stx_rto_RatioTTMDaily    ");
    queryBuilder.append("    WHERE  Ticker = :ticker      ");
    queryBuilder.append("  	) t    ");
    queryBuilder.append("  WHERE rn = 1    ");
    queryBuilder.append(oderBy4);
    queryBuilder.append("  ) t    ");
    queryBuilder.append("  ON t2.YearReport = t.YearReportCal and (t.LengthReportCal = t2.LengthReport    ");
    queryBuilder.append("    or (t.LengthReportCal = 5 and  t2.LengthReport = 4))    ");
    queryBuilder.append("  ORDER BY YearReport desc, LengthReport desc    ");

    try {
      if(isAll == true){
        return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
          .setParameter(TICKER_STR, stockCode)
          .setParameter(YEARLY_STR, yearly)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      } else {
        return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
          .setParameter(TICKER_STR, stockCode)
          .setParameter(YEARLY_STR, yearly)
          .setParameter("quantity", ((yearly == 1) ? 5 : 10))
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

}