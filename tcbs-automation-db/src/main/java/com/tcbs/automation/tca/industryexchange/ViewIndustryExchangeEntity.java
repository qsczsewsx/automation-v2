package com.tcbs.automation.tca.industryexchange;

import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ViewIndustryExchangeEntity {

  @Step("get data from view")
  public static List<HashMap<String, Object>> fromView(){
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT * FROM vw_idata_index_industry_exchange_v2  ");
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("get data from raw")
  public static List<HashMap<String, Object>> fromDataRaw(){
    String query = "select c.ticker, case c.ComGroupCode " +
      "WHEN 'VNINDEX' THEN 0 " +
      "WHEN 'OTC' THEN 2 " +
      "WHEN 'HNXIndex' THEN 1 " +
      "WHEN 'UpcomIndex' THEN 3 " +
      "WHEN 'STOP' THEN 4 " +
      "ELSE NULL " +
      "END " +
      "exchangeId " +
      ", case c.ComGroupCode " +
      "WHEN 'VNINDEX' THEN 'HOSE' " +
      "WHEN 'OTC' THEN 'OTC' " +
      "WHEN 'HNXIndex' THEN 'HNX' " +
      "WHEN 'UpcomIndex' THEN 'UpCom' " +
      "WHEN 'STOP' THEN N'Ngừng GD' " +
      "ELSE 'Other' " +
      "END AS exchangeName " +
      ", ti.indexNumber as indexNumber " +
      ", i.stx1_IdLevel2 as IdLevel2 " +
      ", i.Namel2 " +
      ", i.NameEnl2 " +
      ", len(c.Ticker) lenTicker " +
      ", i.IdLevel2 as icbCodeL2 " +
      ", i.IdLevel3 as icbCodeL3 " +
      ", i.IdLevel4 as icbCodeL4 " +
      "from stx_cpf_Organization c " +
      "join view_idata_industry i on c.IcbCode = i.IdLevel4 " +
      "left join ticker_index ti " +
      "on c.Ticker = ti.ticker " +
      "where len(c.ticker) > 0 and c.Status = 1 ";
    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(query)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
