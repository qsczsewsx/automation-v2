package com.tcbs.automation.stoxplus.stock;

import com.tcbs.automation.stoxplus.Stoxplus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@Setter
public class CashFlowEntity {
  static final Logger logger = LoggerFactory.getLogger(CashFlowEntity.class);
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

  @Step("get ticker info from db")
  public static List<HashMap<String, Object>> getTickerInfo(String ticker) {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append("SELECT stc.Ticker, stc.ShortName , stc.EnglishName , vii.NameEnl2, vii.Namel2, tiilc.Color ");
    queryStringBuilder.append("from stox_tb_Company stc ");
    queryStringBuilder.append("left join view_idata_industry vii on stc.IndustryID = vii.IdLevel4 ");
    queryStringBuilder.append("left join stox_tb_Industry sti on sti.ID = vii.IdLevel2 ");
    queryStringBuilder.append("left join tbl_idata_industry_level2_color tiilc on tiilc.IndustryID = sti.Code ");
    queryStringBuilder.append("where Ticker = :ticker ");

    try {
      return Stoxplus.stoxDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter(TICKER_STR, ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step
  public List<Map<String, Object>> getByStockCode(String stockCode, Integer yearly, Boolean isAll) {
    String quantity = "";
    String oderBy = "";
    String quantity4 = "";
    String oderBy4 = "";

    if (isAll == true) {
      quantity = " SELECT Ticker, YearReport, LengthReport, ";
      oderBy = "";
      quantity4 = " SELECT * ";
      oderBy4 = "";
    } else {
      quantity = " SELECT top (:quantity) Ticker, YearReport, LengthReport, ";
      oderBy = " ORDER BY YearReport desc, LengthReport desc ";
      quantity4 = " SELECT top(:quantity*4) * ";
      oderBy4 = " ORDER BY YearReport_Cal desc, LengthReport_Cal desc ";
    }

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("  SELECT *    ");
    queryBuilder.append("    FROM    ");
    queryBuilder.append("      (    ");
    queryBuilder.append(quantity);
    queryBuilder.append("    F2_131/1000000000 as [sale],    ");
    queryBuilder.append("    ROUND(F2_139/1000000000, 0) as [invest],    ");
    queryBuilder.append("    F2_146/1000000000 as [finance],    ");
    queryBuilder.append("    F2_132/1000000000 as [investCost]    ");
    queryBuilder.append("  FROM stox_tb_fund_CompanyCashFlowStatement    ");
    queryBuilder.append("  WHERE 	Ticker = :ticker    ");
    queryBuilder.append("    AND ( ( :yearly = 0 and LengthReport <> 5 ) or ( :yearly = 1 and LengthReport = 5 ))    ");
    queryBuilder.append(oderBy);
    queryBuilder.append("  ) t2    ");
    queryBuilder.append("  LEFT JOIN    ");
    queryBuilder.append("  (    ");
    queryBuilder.append(quantity4);
    queryBuilder.append("  FROM (    ");
    queryBuilder.append("    SELECT row_number() over (partition by Ticker,  YearReport_Cal, LengthReport_Cal order by UpdateDate desc) as rn,    ");
    queryBuilder.append("    YearReport_Cal , LengthReport_Cal,    ");
    queryBuilder.append("    F5_9/1000000000  as [freeCashFlow]    ");
    queryBuilder.append("    FROM	stox_tb_Ratio    ");
    queryBuilder.append("    WHERE  Ticker = :ticker      ");
    queryBuilder.append("  	) t    ");
    queryBuilder.append("  WHERE rn = 1    ");
    queryBuilder.append(oderBy4);
    queryBuilder.append("  ) t    ");
    queryBuilder.append("  ON t2.YearReport = t.YearReport_Cal and (t.LengthReport_Cal = t2.LengthReport    ");
    queryBuilder.append("    or (t.LengthReport_Cal = 5 and  t2.LengthReport = 4))    ");
    queryBuilder.append("  ORDER BY YearReport desc, LengthReport desc    ");

    try {
      if (isAll == true) {
        return Stoxplus.stoxDbConnection.getSession().createNativeQuery(queryBuilder.toString())
          .setParameter(TICKER_STR, stockCode)
          .setParameter(YEARLY_STR, yearly)
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      } else {
        return Stoxplus.stoxDbConnection.getSession().createNativeQuery(queryBuilder.toString())
          .setParameter(TICKER_STR, stockCode)
          .setParameter(YEARLY_STR, yearly)
          .setParameter("quantity", ((yearly == 1) ? 5 : 10))
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      }
    } catch (Exception ex) {
      logger.error(ex.getMessage(), ex.getStackTrace());
    }

    return new ArrayList();
  }

}