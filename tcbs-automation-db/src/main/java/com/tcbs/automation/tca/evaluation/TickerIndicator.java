package com.tcbs.automation.tca.evaluation;

import com.tcbs.automation.tca.TcAnalysis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.List;
import java.util.Map;

import static com.tcbs.automation.stoxplus.stock.FormatUtils.parseDouble;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TickerIndicator {
  public static final String TICKER_STR = "ticker";
  private String ticker;
  private Double eps;
  private Double bvps;
  private Double ebitdaPerStock;
  private Double cash;
  private Double longTermDebt;
  private Double shortTermDebt;
  private Double minorityInterest;
  private Double sharesOutstanding;

  public static TickerIndicator getByTicker(String ticker, String typeId) {
    String fromCompanyBalanceSheetQuery = "SELECT TOP(1) \n" +
            " stfcbs.ticker,\n" +
            " CAST(BSA2 AS FLOAT)  as [Cash],\n" +
            " CAST(BSA56 AS FLOAT) as [ShortTermDebt],\n" +
            " CAST(BSA71 AS FLOAT) as [LongTermDebt],\n" +
            " CAST(BSA95 AS FLOAT) as [MinorityInterest] \n" +
            " FROM STX_FSC_BALANCESHEET stfcbs\n" +
            " where stfcbs.ticker = :ticker and stfcbs.LengthReport < 5 \n" +
            " order by YearReport desc, LengthReport desc, UpdateDate desc";

    String tcDataCompanyQuery = "SELECT TOP(1) bvps as [BVPS], basic_eps as [EPS]\n" +
            " , ebitda_per_share [EBITDA]" +
            "   FROM tcdata_ratio_company trc\n" +
            "   WHERE ticker = :ticker AND LengthReport < 5\n" +
            "   ORDER BY YearReport DESC, LengthReport DESC";

    String shareCirculateQuery = "SELECT TOP(1)\n" +
            "   CAST(OUTSTANDINGSHARE AS FLOAT) as [SharesOutstanding]\n" +
            "   FROM stx_cpf_Organization stc where stc.Ticker = :ticker";

    String tcDataBankQuery = "SELECT TOP(1) bvps as [BVPS], basic_eps as [EPS]\n" +
            "   FROM tcdata_ratio_bank trb\n" +
            "   WHERE ticker = :ticker AND LengthReport < 5\n" +
            "   ORDER BY YearReport DESC, LengthReport DESC";
    try {
      if (typeId.equals("NH")) {
        List<Map<String, Object>> tcDataBankResultList = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(tcDataBankQuery)
                .setParameter(TICKER_STR, ticker).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
                .getResultList();
        TickerIndicator tickerIndicator = new TickerIndicator();
        if (!tcDataBankResultList.isEmpty()) {
          tickerIndicator.setEps(parseDouble(tcDataBankResultList.get(0).get("EPS")));
          tickerIndicator.setBvps(parseDouble(tcDataBankResultList.get(0).get("BVPS")));
        }

        return tickerIndicator;
      }

      TickerIndicator tickerIndicator = new TickerIndicator();
      List<Map<String, Object>> fromCompanyBalanceSheetResultList = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(fromCompanyBalanceSheetQuery)
          .setParameter(TICKER_STR, ticker).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
          .getResultList();
      if (fromCompanyBalanceSheetResultList.isEmpty()) {
        return null;
      }

      Map<String, Object> result = fromCompanyBalanceSheetResultList.get(0);
      tickerIndicator.setCash(parseDouble(result.get("Cash")));
      tickerIndicator.setShortTermDebt(parseDouble(result.get("ShortTermDebt")));
      tickerIndicator.setLongTermDebt(parseDouble(result.get("LongTermDebt")));
      tickerIndicator.setMinorityInterest(parseDouble(result.get("MinorityInterest")));

      List<Map<String, Object>> tcDataCompanyResultList = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(tcDataCompanyQuery)
              .setParameter(TICKER_STR, ticker).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
              .getResultList();

      if (!tcDataCompanyResultList.isEmpty()) {
        Map<String, Object> tcData = tcDataCompanyResultList.get(0);
        tickerIndicator.setEps(parseDouble(tcData.get("EPS")));
        tickerIndicator.setBvps(parseDouble(tcData.get("BVPS")));
        tickerIndicator.setEbitdaPerStock(parseDouble(tcData.get("EBITDA")));
      }

      List<Map<String, Object>> shareCirculateResultList = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(shareCirculateQuery)
              .setParameter(TICKER_STR, ticker).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
              .getResultList();
      if (!shareCirculateResultList.isEmpty()) {
        tickerIndicator.setSharesOutstanding(parseDouble(shareCirculateResultList.get(0).get("SharesOutstanding")));
      }
      return tickerIndicator;
    }  catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    TcAnalysis.tcaDbConnection.closeSession();

    return null;
  }
}
