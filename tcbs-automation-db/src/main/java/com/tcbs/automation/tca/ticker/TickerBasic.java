package com.tcbs.automation.tca.ticker;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.tcbs.automation.tca.TcAnalysis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TickerBasic {
  String organCode;
  String ticker;
  String icbCode;
  String comTypeCode;
  Integer industryId;
  String comGroupCode;  

  @Step
  public static TickerBasic getTickerBasic(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("   SELECT ComGroupCode , ComTypeCode , Ticker , OrganCode , I.IcbCode , CAST(IndustryID AS INT) AS IndustryID ");
    queryBuilder.append("  FROM stx_cpf_Organization O  ");
    queryBuilder.append("  LEFT JOIN stx_mst_IcbIndustry I ");
    queryBuilder.append("      ON O.IcbCode = I.IcbCode  ");
    queryBuilder.append("  WHERE Ticker = :ticker  ");
    try{
      List<Map<String, Object>> listResult = TcAnalysis.tcaDbConnection
        .getSession()
        .createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList(); 
      return listResult.stream()
              .map(TickerBasic::transform)
              .collect(Collectors.toList()).get(0);
    } catch(Exception e) {
      e.printStackTrace(System.out);
    }
    return TickerBasic.builder().build();
  }

  public static TickerBasic transform(Map<String, Object> record) {
    return TickerBasic.builder()
      .comGroupCode((String)record.get("ComGroupCode"))
      .comTypeCode((String)record.get("ComTypeCode"))
      .organCode((String)record.get("OrganCode"))
      .ticker((String)record.get("Ticker"))
      .icbCode((String)record.get("IcbCode"))
      .industryId((Integer)record.get("IndustryID"))
      .build();
  }

  public static String getIndustryName(String ticker, String lang) {
    String queryBuilder = "SELECT CASE WHEN :LANG = 'vi' then VII.NAMEL2 ELSE VII.NAMEENL2 END  AS NameLevel2 "
                        + " FROM stx_cpf_Organization stc "
                        + " JOIN view_idata_industry vii "
                        + "   ON vii.IdLevel4 = stc.IcbCode "
                        + " WHERE STC.Ticker  = :TICKER";
    try{
      List<Map<String, Object>> listResult = TcAnalysis.tcaDbConnection
        .getSession()
        .createNativeQuery(queryBuilder)
        .setParameter("TICKER", ticker)
        .setParameter("LANG", lang)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList(); 
      return listResult.get(0).get("NameLevel2").toString();
    } catch(Exception e) {
      e.printStackTrace(System.out);
    }
    return "";
  }

  public static String getIndustryLv2(String ticker) {
    String queryBuilder = "SELECT VII.IdLevel2 "
                        + " FROM stx_cpf_Organization stc "
                        + " JOIN view_idata_industry vii "
                        + "   ON vii.IdLevel4 = stc.IcbCode "
                        + " WHERE STC.Ticker  = :TICKER";
    try{
      List<Map<String, Object>> listResult = TcAnalysis.tcaDbConnection
        .getSession()
        .createNativeQuery(queryBuilder)
        .setParameter("TICKER", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList(); 
      return listResult.get(0).get("IdLevel2").toString();
    } catch(Exception e) {
      e.printStackTrace(System.out);
    }
    return "";
  }
}
