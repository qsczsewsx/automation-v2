package com.tcbs.automation.stoxplus.stock;

import com.tcbs.automation.dwh.Dwh;
import com.tcbs.automation.stoxplus.Stoxplus;
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
  private Double noEmployees;
  private Double noShareholders;
  private Float foreignPercent;
  private String website;
  private Float deltaInWeek;
  private Float deltaInMonth;
  private Float deltaInYear;

  private static boolean isVI(String lang) {
    return "vi".equalsIgnoreCase(lang);
  }

  public static String toExchangeString(int exchangeId, String lang) {
    switch (exchangeId) {
      case 0:
        return "HOSE";
      case 1:
        return "HNX";
      case 2:
        return "OTC";
      case 3:
        return "UPCOM";
      case 4:
        return isVI(lang) ? "NgungGiaoDich" : "Suspended";
      default:
        return "UNKNOWN";
    }
  }

  public static Map<String, Object> getByTicker(String ticker) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("   SELECT c.Ticker, c.ShortName, c.ShortEnglishName, c.IndustryID, c.exchangeid, c.CreateDate, c.NumberOfEmployees, c.NumberOfCDPT, c.WebsiteHomepage,  ");
    queryStringBuilder.append("     vii.Namel2, vii.NameEnl2, i.Name_En, o.Psh1 / 100 as TyLeNcNgoaiSoHuu,  ");
    queryStringBuilder.append("   vni.VNI1W as DeltaInWeek, vni.VNI1M as DeltaInMonth, vni.VNI1Y as DeltaInYear  ");
    queryStringBuilder.append("   FROM stox_tb_Company c  ");
    queryStringBuilder.append("   LEFT JOIN stox_tb_Industry i on i.id = c.IndustryID  ");
    queryStringBuilder.append("   LEFT JOIN view_idata_industry vii on vii.IdLevel4 = c.IndustryID  ");
    queryStringBuilder.append("   LEFT JOIN  ");
    queryStringBuilder.append("   (  ");
    queryStringBuilder.append("     SELECT Ticker, Psh1  ");
    queryStringBuilder.append("   FROM stox_tb_fund_Ownership_New  ");
    queryStringBuilder.append("   WHERE ticker = :ticker  ");
    queryStringBuilder.append("     AND UpdateDate = (SELECT max(UpdateDate) from stox_tb_fund_Ownership_New where ticker = :ticker)  ");
    queryStringBuilder.append(" ) o  ");
    queryStringBuilder.append("   ON o.ticker = c.ticker  ");
    queryStringBuilder.append("   LEFT JOIN vw_stox_VNI_StockPrice_Change vni ON vni.Ticker = c.Ticker  ");
    queryStringBuilder.append("   WHERE c.Ticker = :ticker  ");

    List<Map<String, Object>> resultList = Stoxplus.stoxDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
      .setParameter("ticker", ticker)
      .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    Dwh.dwhDbConnection.closeSession();
    if (resultList.size() > 0) {
      return resultList.get(0);
    } else {
      return null;
    }
  }

  public static String getExchangeByTicker(String ticker, String lang) {
    if (ticker.startsWith("VN30F") || ticker.startsWith("GB10F") || ticker.startsWith("GB05F")) {
      return "HNX";
    }
    StringBuilder queryStringBuilder = new StringBuilder();

//    queryStringBuilder.append("  SELECT ExchangeID  ");
//    queryStringBuilder.append("  FROM stox_tb_Company stc  ");
//    queryStringBuilder.append("  WHERE ticker = :ticker  ");

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
//      Dwh.dwhDbConnection.closeSession();
      if (!query.isEmpty()) {
//        return toExchangeString((short) query.get(0).get("ExchangeID"), lang);
        return query.get(0).get("ExchangeID").toString().toUpperCase();
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

}
