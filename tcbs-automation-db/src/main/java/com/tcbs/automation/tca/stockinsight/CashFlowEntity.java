package com.tcbs.automation.tca.stockinsight;

import com.tcbs.automation.tca.TcAnalysis;
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

@Getter
@RequiredArgsConstructor
@Setter
public class CashFlowEntity {
  static final Logger logger = LoggerFactory.getLogger(CashFlowEntity.class);
  private static final String TICKER_STR = "ticker";

  @Step("get ticker info from db")
  public static List<HashMap<String, Object>> getTickerInfo(String ticker) {
    StringBuilder query = new StringBuilder();
    query.append(" SELECT tb1.Ticker, ExchangeCode, ShortName, EnglishName, IndustryCode, Namel2, NameEnl2, Color ");
    query.append(" FROM ( ");
    query.append("     SELECT st.Ticker as TICKER, ");
    query.append("       st.OrganShortName AS ShortName, ");
    query.append("       st.en_OrganName AS EnglishName, ");
    query.append("       i2.IcbCode AS IndustryCode, ");
    query.append("       i2.IcbName  AS Namel2, ");
    query.append("       i2.en_IcbName AS NameEnl2, ");
    query.append("       i5.Color AS Color, ");
    query.append("        (CASE  ");
    query.append("          WHEN st.comGroupCode = 'VNINDEX' THEN 'HSX' ");
    query.append("     WHEN st.comGroupCode = 'HNXIndex' THEN 'HNX' ");
    query.append("     WHEN st.comGroupCode = 'UpcomIndex' THEN 'UPC' ");
    query.append("       END) AS ExchangeCode ");
    query.append("     FROM stx_cpf_Organization st ");
    query.append("     LEFT JOIN view_idata_industry i4 ON st.IcbCode = i4.IdLevel4  ");
    query.append("     LEFT JOIN stx_mst_IcbIndustry i2  ");
    query.append("       ON i4.IdLevel2 = i2.IcbCode  ");
    query.append("       AND i2.IcbLevel = 2 ");
    query.append("     LEFT JOIN tbl_idata_industry_level2_color i5 ON i4.IdLevel2 = i5.IndustryID  ");
    query.append("     WHERE st.comGroupCode IN ('HNXIndex','UpcomIndex','VNINDEX') ");
    query.append("       AND st.Ticker = :ticker ");
    query.append("   )tb1 ");
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter(TICKER_STR, ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}