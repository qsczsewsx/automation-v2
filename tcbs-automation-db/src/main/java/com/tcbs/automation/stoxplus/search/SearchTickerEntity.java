package com.tcbs.automation.stoxplus.search;

import com.tcbs.automation.stoxplus.StoxplusV2;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.HashMap;
import java.util.List;

public class SearchTickerEntity {
  public static List<HashMap<String, Object>> getStockTicker() {
    StringBuilder queryStringBuilder = new StringBuilder();

    queryStringBuilder.append(" with stock as( ");
    queryStringBuilder.append("   SELECT Ticker, 'stock' as [type], IcbCode, ");
    queryStringBuilder.append("   OrganShortName, en_OrganShortName, ");
    queryStringBuilder.append("   CASE ComGroupCode WHEN 'VNINDEX' THEN '0' ");
    queryStringBuilder.append(" WHEN 'HNXIndex' THEN '1' ");
    queryStringBuilder.append(" WHEN 'UpcomIndex' THEN '3' ");
    queryStringBuilder.append(" ELSE NULL END as exchange ");
    queryStringBuilder.append(" FROM stx_cpf_Organization ");
    queryStringBuilder.append(" where ComGroupCode  in ('VNINDEX', 'HNXIndex', 'UpcomIndex') ");
    queryStringBuilder.append(" and Ticker is not NULL) ");
    queryStringBuilder.append(" SELECT stock.*, ");
    queryStringBuilder.append(" Namel2, NameEnl2 ");
    queryStringBuilder.append(" from stock ");
    queryStringBuilder.append(" left join view_idata_industry b ");
    queryStringBuilder.append(" on stock.IcbCode = b.IdLevel4 ");


    try {
      return StoxplusV2.stoxV2DbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return null;
  }

  public static List<HashMap<String, Object>> getIndexCode() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT * FROM idata_index_tickers ");

    try {
      return StoxplusV2.stoxV2DbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return null;
  }
}
