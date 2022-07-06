package com.tcbs.automation.projection.tcanalysis.models;

import com.tcbs.automation.projection.Projection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Column;
import javax.persistence.Id;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NextYearIncome {

  @Id
  @Column(name = "ticker")
  private String ticker;

  @Column(name = "sales_growth")
  private Double salesGrowth;

  @Column(name = "income_growth")
  private Double incomeGrowth;

  public static List<NextYearIncome> getNextYearIncome(String tickers, String fType){
    if(fType.equals("INDUSTRY")) {
      return getNextYearIncome(getTickerSameIndustry(tickers));
    } else {
      return getNextYearIncome(stringToList(tickers));
    }
    
  }

  @Step
  public static List<NextYearIncome> getNextYearIncome(List<String> tickers) {
    StringBuilder queryBuilder = new StringBuilder();
    try {
      queryBuilder.append(" WITH VERSION AS ( ");
      queryBuilder.append(" SELECT MAX(D.id) AS ID , D.ticker, MAX(D.YEAR) AS YEARREPORT ");
      queryBuilder.append(" FROM  db_owner.stox_tb_save_data D ");
      queryBuilder.append(" WHERE D.TICKER IN :TICKERLIST ");
      queryBuilder.append(" AND D.type = 1 ");
      queryBuilder.append(" AND LAST_UPDATE < GETDATE() AT TIME ZONE 'UTC'  AT TIME ZONE 'SE Asia Standard Time'  ");
      queryBuilder.append(" GROUP BY D.TICKER  ");
      queryBuilder.append(" )  ");
      queryBuilder.append(" SELECT B.ticker, B.sales_growth , B.income_growth ");
      queryBuilder.append(" FROM db_owner.tcdata_ratio_bank B  ");
      queryBuilder.append(" INNER JOIN VERSION V ");
      queryBuilder.append("     ON B.YearReport = V.YEARREPORT + 1 ");
      queryBuilder.append("     AND B.version_id = V.ID ");
      queryBuilder.append(" UNION  ");
      queryBuilder.append(" SELECT C.ticker, C.sales_growth , C.income_growth  ");
      queryBuilder.append(" FROM db_owner.tcdata_ratio_company C  ");
      queryBuilder.append(" INNER JOIN VERSION V ");
      queryBuilder.append("     ON C.YearReport = V.YEARREPORT + 1 ");
      queryBuilder.append("     AND C.version_id = V.ID ");

      List<Map<String, Object>> map = Projection.projectionDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("TICKERLIST", tickers)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      if (CollectionUtils.isNotEmpty(map)) {
        return map.stream().map(NextYearIncome::build).collect(Collectors.toList());
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static NextYearIncome build(Map<String, Object> rs) {
    return NextYearIncome.builder()
          .ticker(rs.get("ticker").toString())
          .salesGrowth(tryDouble(rs.get("sales_growth")))
          .incomeGrowth(tryDouble(rs.get("income_growth")))
          .build();
  }   

  public static Double tryDouble(Object x) {
    if(x == null) {
      return null;
    } else {
      return Double.parseDouble(x.toString());
    }
  }

  @Step
  public static List<String> getTickerSameIndustry(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    try {
      queryBuilder.append(" WITH INS AS ( ");
      queryBuilder.append(" 	SELECT S.Ticker AS TICKER, V.IdLevel2 AS ID ");
      queryBuilder.append(" 	FROM db_owner.stox_tb_Company S ");
      queryBuilder.append(" 	INNER JOIN view_idata_industry v ");
      queryBuilder.append(" 		ON S.IndustryID = V.IdLevel4 ");
      queryBuilder.append(" ) ");
      queryBuilder.append(" SELECT TICKER  ");
      queryBuilder.append(" FROM INS  ");
      queryBuilder.append(" WHERE INS.ID = ( ");
      queryBuilder.append(" 	SELECT ID  ");
      queryBuilder.append(" 	FROM INS  ");
      queryBuilder.append(" 	WHERE TICKER = :TICKER ");
      queryBuilder.append(" ) ");

      List<Map<String, Object>> map = Projection.projectionDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("TICKER", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      if (CollectionUtils.isNotEmpty(map)) {
        return map.stream().map(Object::toString).collect(Collectors.toList());
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<String> stringToList(String in) {
    in = in.toUpperCase();
    if (in.trim().equals("")) return new ArrayList<>();
    String delimiter = ",";
    Set<String> validated = new HashSet<>(Arrays.asList(in.replaceAll("\\s+","").split(delimiter)));
    return new ArrayList<>(validated);
  }
}
