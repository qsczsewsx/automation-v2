package com.tcbs.automation.stockmarket.rrg;

import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RrgIndustryEntity {

  @Step("get rrg industry data")
  public static List<HashMap<String, Object>> getRrgIndustry(String industryID, String fromDate, String toDate, String lang, String type) {
    StringBuilder query = new StringBuilder();
    query.append("select t5.*, RS_Norm, RS_Momen from (  ");
    query.append("	select CONCAT([year] , FORMAT(DATEPART(ISO_WEEK, tradingDate), '00')) as weekYear  ");
    query.append("	 , tradingDate, IdLevel2, idName, sum(mkCap)/1000000000 as mkCap, PE, sum(vol) as vol, sum(val) as val  ");
    query.append("	from (  ");
    query.append("		select t1.TradingDate as tradingDate, t3.IdLevel2 as IdLevel2  ");
    query.append("			, IIF(:lang = 'EN', t3.NameEnl2, t3.NameL2) as idName  ");
    query.append("			, RTD11 as mkCap , t6.[P/E] as PE ");
    query.append("			, TotalVolume as vol, TotalMatchValue as val  ");
    query.append("			, CASE WHEN DATEPART(ISO_WEEK, t1.TradingDate) > 50 AND MONTH(t1.TradingDate) = 1 THEN YEAR(t1.TradingDate) - 1 ");
    query.append("			    WHEN DATEPART(ISO_WEEK, t1.TradingDate) = 1 AND MONTH(t1.TradingDate) = 12 THEN YEAR(t1.TradingDate) + 1 ");
    query.append("			    ELSE YEAR(t1.TradingDate) END as [year] ");
    query.append("		from Smy_dwh_stox_MarketPrices t1  ");
    query.append("		left join stx_rto_RatioTTMDaily t2 on t1.Ticker = t2.Ticker and t1.TradingDate = t2.TradingDate  ");
    query.append("		left join ( select Ticker, IcbCode from [stx_cpf_Organization]) t4 on t1.Ticker = t4.Ticker  ");
    query.append("		left join view_idata_industry t3 on t4.IcbCode = cast(t3.IdLevel4 as varchar )  ");
    query.append("		left join tbl_idata_evaluation_industry_ratio t6 on t1.TradingDate = t6.ReportDate and t3.IdLevel2 = t6.IndustryID ");
    query.append("		where t3.IdLevel2 in (select value from STRING_SPLIT(:industryId,','))  ");
    query.append("		and t1.TradingDate between DATEADD(month, -1, :fromDate )  and :toDate and (t2.Status is null or t2.Status = 1) ");
    query.append("	)tbl group by tradingDate, IdLevel2, idName , PE, [year] ");
    query.append(")t5 ");
    if (StringUtils.equalsIgnoreCase(type, "1Y")) {
      query.append("left join (select DISTINCT * from smy_stox_industry_rrg_weekly ");
      query.append("  where ETLRunDateTime = (select max(ETLRunDateTime) from smy_stox_industry_rrg_weekly) ");
      query.append(") t6 on t5.weekYear = t6.WeekYear and t5.IdLevel2 = t6.IdLevel2 ");
    } else {
      query.append("left join ( select DISTINCT * from smy_stox_industry_rrg ");
      query.append(" where ETLRunDateTime = (select max(ETLRunDateTime) from smy_stox_industry_rrg) ");
      query.append(") t6 on t5.tradingDate = t6.DateReport and t5.IdLevel2 = t6.IDlevel2  ");
    }
    query.append(" order by t5.tradingDate; ");

    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("industryId", industryID)
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setParameter("lang", lang)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception e) {
      StepEventBus.getEventBus().testFailed(new AssertionError(e.getMessage()));
    }
    return new ArrayList<>();
  }
}
