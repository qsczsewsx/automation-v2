package com.tcbs.automation.tca.company;

import com.tcbs.automation.tca.TcAnalysis;
import com.tcbs.automation.tca.ticker.TickerBasic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DividendPayment {
  @Id // @Id for match
  private Long no;

  private String ticker;

  private String exerciseDate;

  private Integer cashYear;

  private Double cashDividendPercentage;

  private String cashWave;
  private String issueMethod;

  @Step
  public static List<Map<String, Object>> getByTicker(String ticker, int page, int size) {
    try{
      StringBuilder queryBuilder = new StringBuilder();
      queryBuilder.append("   SELECT Row_number() OVER(ORDER BY ISNULL(ExDividendDate, 0) DESC) AS no,  ");
      queryBuilder.append(" 	ticker, ");
      queryBuilder.append(" 	exerciseDate, ");
      queryBuilder.append(" 	cashYear, ");
      queryBuilder.append(" 	cashDividendPercentage, ");
      queryBuilder.append(" 	IssueMethod ");
      queryBuilder.append(" FROM  ");
      queryBuilder.append(" ( ");
      queryBuilder.append(" 	SELECT OrganCode AS ticker,  ");
      queryBuilder.append(" 		CONVERT(VARCHAR, ExrightDate, 3) as exerciseDate,  ");
      queryBuilder.append(" 		DividendYear AS cashYear,  ");
      queryBuilder.append(" 		CAST(ExerciseRate AS FLOAT) as cashDividendPercentage, ");
      queryBuilder.append(" 		'cash' AS IssueMethod, ");
      queryBuilder.append(" 		ExrightDate AS ExDividendDate ");
      queryBuilder.append(" 	FROM stx_cpa_CashDividendPayout ");
      queryBuilder.append(" 	WHERE OrganCode = :ticker and ExrightDate is not null ");
      queryBuilder.append(" 	UNION ALL ");
      queryBuilder.append(" 	select OrganCode AS ticker,  ");
      queryBuilder.append(" 		CONVERT(VARCHAR, ExrightDate, 3),  ");
      queryBuilder.append(" 		IssueYear,  ");
      queryBuilder.append(" 		CAST(ExerciseRatio AS FLOAT) as IssueRate, ");
      queryBuilder.append(" 		'share', ");
      queryBuilder.append(" 		ExrightDate AS ExDividendDate ");
      queryBuilder.append(" 	from stx_cpa_ShareIssue ");
      queryBuilder.append(" 	where IssueMethodCode IN ('DIV', 'Rights') and ExrightDate is not null ");
      queryBuilder.append(" 	and OrganCode  = :ticker ");
      queryBuilder.append(" ) tbl ");
      queryBuilder.append(" ORDER BY ISNULL(ExDividendDate, 0) DESC ");
      List<Map<String, Object>> listResult = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", TickerBasic.getTickerBasic(ticker).getOrganCode())
        .setFirstResult(page * size)
        .setMaxResults(size)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return listResult;
    } catch(Exception e) {
      e.printStackTrace(System.out);
    }
    return null;
  }
}
