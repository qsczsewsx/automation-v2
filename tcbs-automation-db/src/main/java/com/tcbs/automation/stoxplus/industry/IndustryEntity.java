package com.tcbs.automation.stoxplus.industry;

import com.tcbs.automation.stockmarket.Stockmarket;
import com.tcbs.automation.tca.TcAnalysis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IndustryEntity {

  @Step("get all idlevel2 from stox v2")
  public static List<HashMap<String, Object>> getIndustryLv2() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT DISTINCT IdLevel2, Namel2, NameEnl2 ");
    queryStringBuilder.append("FROM view_idata_industry t ");
    queryStringBuilder.append("where IdLevel2 is not NULL  ");

    try {
      return TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

//  @Step("get all idlevel2 from db")
//  public static List<HashMap<String, Object>> getIndustryLv2() {
//    StringBuilder queryStringBuilder = new StringBuilder();
//    queryStringBuilder.append("SELECT DISTINCT IdLevel2   ");
//    queryStringBuilder.append("FROM idata_index_industry_exchange iiie ");
//    queryStringBuilder.append("where IdLevel2 is not NULL  ");
//
//    try {
//      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
//        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
//        .getResultList();
//    } catch (Exception ex) {
//      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
//    }
//    return new ArrayList<>();
//  }
}
