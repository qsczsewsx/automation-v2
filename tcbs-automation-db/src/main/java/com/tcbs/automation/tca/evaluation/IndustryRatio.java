package com.tcbs.automation.tca.evaluation;

import com.beust.jcommander.internal.Nullable;
import com.tcbs.automation.tca.TcAnalysis;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.List;
import java.util.Map;

import static com.tcbs.automation.stoxplus.stock.FormatUtils.*;

@Getter
@RequiredArgsConstructor
@Setter
public class IndustryRatio {
  private String ticker;
  @Nullable
  private Double industryPe;
  @Nullable
  private Double industryPb;
  @Nullable
  private Double industryEvEbitda;
  @Nullable
  private Double indexPe;
  @Nullable
  private Double indexPb;
  @Nullable
  private Double indexEvEbitda;

  @Step
  public static IndustryRatio getByIndustryL2Id(String industryL2Id) {
    String indexQuerySql = "SELECT [P/E] AS [IndexPoE], [P/B] AS [IndexPoB], [EV/EBITDA] AS [IndexEVoEBITDA]"
            + " FROM tbl_idata_evaluation_index_ratio tb1"
            + " WHERE ReportDate = (SELECT MAX(tb2.ReportDate) FROM tbl_idata_evaluation_index_ratio tb2)";

    String industrySql = "SELECT [P/E] AS [PoE], [P/B] AS [PoB], [EV/EBITDA] AS [EVoEBITDA]"
            + " FROM tbl_idata_evaluation_industry_ratio industry"
            + " WHERE IndustryId = :industryL2Id"
            + " AND ReportDate = (SELECT MAX(industry2.ReportDate)"
            + " FROM tbl_idata_evaluation_industry_ratio industry2 "
            + " WHERE industry2.IndustryId = :industryL2Id)";
    try {
      List<Map<String, Object>> indexResultList = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(indexQuerySql)
              .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
              .getResultList();
      List<Map<String, Object>> industryResultList = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(industrySql)
              .setParameter("industryL2Id", industryL2Id)
              .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
              .getResultList();
      IndustryRatio industryRatio = new IndustryRatio();
      if (indexResultList.size() > 0) {
        industryRatio.setIndexPe(parseDouble(indexResultList.get(0).get("IndexPoE")));
        industryRatio.setIndexPb(parseDouble(indexResultList.get(0).get("IndexPoB")));
        industryRatio.setIndexEvEbitda(parseDouble(indexResultList.get(0).get("IndexEVoEBITDA")));
      }
      if (industryResultList.size() > 0) {
        industryRatio.setIndustryPe(parseDouble(industryResultList.get(0).get("PoE")));
        industryRatio.setIndustryPb(parseDouble(industryResultList.get(0).get("PoB")));
        industryRatio.setIndustryEvEbitda(parseDouble(industryResultList.get(0).get("EVoEBITDA")));
      }

      return industryRatio;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    TcAnalysis.tcaDbConnection.closeSession();

    return new IndustryRatio();
  }
}
