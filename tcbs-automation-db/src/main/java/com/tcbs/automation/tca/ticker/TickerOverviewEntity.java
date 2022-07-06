package com.tcbs.automation.tca.ticker;

import com.tcbs.automation.dwh.Dwh;
import com.tcbs.automation.tca.TcAnalysis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TickerOverviewEntity {
  private String ticker;
  private String exchange;
  private String shortName;
  private Integer industryID;
  private String industry;
  private String industryEn;
  private String establishedYear;
  private Integer noEmployees;
  private Integer noShareholders;
  private Float foreignPercent;
  private String website;
  private Float deltaInWeek;
  private Float deltaInMonth;
  private Float deltaInYear;
  private Float outstandingShare;
  private Float issueShare;
  private String comTypeCode;

  private static boolean isVI(String lang) {
    return "vi".equalsIgnoreCase(lang);
  }

  public static String toExchangeString(String exchangeId, String lang) {
    switch (exchangeId) {
      case "VNINDEX":
        return "HOSE";
      case "HNXINDEX":
        return "HNX";
      case "OTC":
        return "OTC";
      case "UPCOMINDEX":
        return "UPCOM";
      default:
        return isVI(lang) ? "NgungGiaoDich" : "Suspended";
    }
  }

  public static Map<String, Object> getByTicker(String ticker, String lang) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("   SELECT c.Ticker  ");
    queryStringBuilder.append("      , c.OrganShortName AS ShortName");
    queryStringBuilder.append("      , c.en_OrganShortName  as ShortEnglishName");
    queryStringBuilder.append("      , vii.Namel2");
    queryStringBuilder.append("      , vii.NameEnl2");
    queryStringBuilder.append("      , CASE ");
    queryStringBuilder.append("           WHEN c.comGroupCode = 'vnindex' then 'HOSE' ");
    queryStringBuilder.append("           WHEN c.comGroupCode = 'hnxindex' then 'HNX' ");
    queryStringBuilder.append("           WHEN c.comGroupCode = 'otc' then 'OTC' ");
    queryStringBuilder.append("           WHEN c.comGroupCode = 'upcomindex' then 'UPCOM' ");
    queryStringBuilder.append("           ELSE  (CASE ");
    queryStringBuilder.append("             WHEN :lang = 'vi' then 'NgungGiaoDich' ");
    queryStringBuilder.append("             ELSE 'Suspended' ");
    queryStringBuilder.append("       END) END as ExchangeID  ");
    queryStringBuilder.append("       , vii.stx1_IdLevel4 AS IndustryID,");
    queryStringBuilder.append("       CAST(YEAR(c.CreateDate) AS VARCHAR(4))  AS CreateDate,");
    queryStringBuilder.append("       W.NumberOfEmployee AS NumberOfEmployees,");
    queryStringBuilder.append("       CAST(c.NumberOfShareHolder AS INT) AS NumberOfCDPT,");
    queryStringBuilder.append("       W.Website AS WebsiteHomepage,");
    queryStringBuilder.append("       o.Psh1 as TyLeNcNgoaiSoHuu,");
    queryStringBuilder.append("       CONVERT(float, vni.VNI1W) as DeltaInWeek,");
    queryStringBuilder.append("       CONVERT(float, vni.VNI1M) as DeltaInMonth,");
    queryStringBuilder.append("       CONVERT(float, vni.VNI1Y) as DeltaInYear, ");
    queryStringBuilder.append("       round(c.OutstandingShare/1000000 , 1) OutstandingShare, ");
    queryStringBuilder.append("       round(c.IssueShare /1000000, 1) IssueShare ");
    queryStringBuilder.append("       ,c.ComTypeCode AS ComTypeCode ");
    queryStringBuilder.append(" FROM stx_cpf_Organization c");
    queryStringBuilder.append(" LEFT JOIN stx_cpf_CompanyInformation W");
    queryStringBuilder.append(" ON C.OrganCode = W.OrganCode ");
    queryStringBuilder.append(" LEFT JOIN view_idata_industry vii on vii.IdLevel4 = c.IcbCode ");
    queryStringBuilder.append(" LEFT JOIN (");
    queryStringBuilder.append(" SELECT organCode, CAST(foreignerPercentage AS FLOAT) as psh1");
    queryStringBuilder.append(" FROM stx_cpa_Ownership");
    queryStringBuilder.append(" WHERE organCode = :ticker");
    queryStringBuilder.append("  AND PublicDate = (SELECT max(PublicDate) FROM stx_cpa_Ownership WHERE organCode = :ticker)");
    queryStringBuilder.append(" ) o ");
    queryStringBuilder.append(" ON o.organCode = c.organCode");
    queryStringBuilder.append(" LEFT JOIN vw_stox_VNI_StockPrice_Change vni ON vni.Ticker = c.Ticker ");
    queryStringBuilder.append(" WHERE c.organCode = :ticker	  ");

    try {
      List<Map<String, Object>> resultList = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("ticker", TickerBasic.getTickerBasic(ticker).getOrganCode())
        .setParameter("lang", lang)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList.get(0);
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  public static String getExchangeByTicker(String ticker, String lang) {
    if (ticker.startsWith("VN30F") || ticker.startsWith("GB10F") || ticker.startsWith("GB05F")) {
      return "HNX";
    }
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append("  SELECT CASE ");
    queryStringBuilder.append("      WHEN c.comGroupCode = 'vnindex' then 'HOSE' ");
    queryStringBuilder.append("      WHEN c.comGroupCode = 'hnxindex' then 'HNX' ");
    queryStringBuilder.append("      WHEN c.comGroupCode = 'otc' then 'OTC' ");
    queryStringBuilder.append("      WHEN c.comGroupCode = 'upcomindex' then 'UPCOM' ");
    queryStringBuilder.append("      ELSE  (CASE ");
    queryStringBuilder.append("         WHEN :lang = 'vi' then 'NgungGiaoDich' ");
    queryStringBuilder.append("          ELSE 'Suspended' ");
    queryStringBuilder.append("       END) END as ExchangeID  ");
    queryStringBuilder.append("  FROM stx_cpf_Organization c  ");
    queryStringBuilder.append("  WHERE Ticker = :ticker  ");

    try {
      List<Map<String, Object>> query = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryStringBuilder.toString()) //TODO change v2
        .setParameter("ticker", ticker)
        .setParameter("lang", lang)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return query.get(0).get("ExchangeID").toString().toUpperCase();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

}
