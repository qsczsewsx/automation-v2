package com.tcbs.automation.iris.fin5;


import com.tcbs.automation.iris.AwsIRis;
import com.tcbs.automation.riskcloud.AwsRiskCloud;
import com.tcbs.automation.staging.AwsStagingDwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IndicatorEntity {
  @Step("Get data")
  public static List<HashMap<String, Object>> getData(String fromDate, String toDate) {

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("  exec iris_get_fin5_indicators @fromDate = :fromDate, @toDate = :toDate   ");
    try {
      return AwsRiskCloud.awsRiskCloudDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Get data")
  public static List<HashMap<String, Object>> getIRis(String fromDate, String toDate) {

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select ETL,round(SASS_STOCK/EQT*100,2) equity_investment_on_equity,round(SDB/SASS*100,2) ST_debt_on_ST_asset,round(DB/EQT *100,2) DE  ");
    queryBuilder.append(" from (  ");
    queryBuilder.append(" select sum(SASS_CASH + SASS_TD + SASS_CORP_BOND + SASS_GOV_BOND + SASS_STOCK + SASS_MARGIN +   ");
    queryBuilder.append(" SASS_OTHER)                                                      SASS,  ");
    queryBuilder.append(" sum(LASS_TD + LASS_OTHER)                                            LASS,  ");
    queryBuilder.append(" sum(SDB_LENDING + SDB_LQ_BOND + SDB_AC_BOND + SDB_ISAVE + SDB_OTHER) SDB,  ");
    queryBuilder.append(" sum(LDB_LENDING + LDB_LQ_BOND + LDB_AC_BOND + LDB_OTHER)             LDB,  ");
    queryBuilder.append(" sum(EQT)                                                             EQT,  ");
    queryBuilder.append(" ETL,  ");
    queryBuilder.append(" sum(SASS_STOCK)                                                      SASS_STOCK,  ");
    queryBuilder.append(" sum(SDB_LENDING + SDB_LQ_BOND + SDB_AC_BOND + SDB_ISAVE + SDB_OTHER + LDB_LENDING + LDB_LQ_BOND + LDB_AC_BOND + LDB_OTHER) DB  ");
    queryBuilder.append(" from RISK_CREDIT_INFO  ");
    queryBuilder.append(" where ETL >= :fromDate  ");
    queryBuilder.append(" and ETL <= :toDate  ");
    queryBuilder.append(" group by ETL  ");
    queryBuilder.append(" )  ");
    try {
      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("fromDate", fromDate)
        .setParameter("toDate", toDate)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }
}
