package com.tcbs.automation.iris.venus;

import com.tcbs.automation.iris.AwsIRis;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PostCheckDraftEntity {

  @Step("Get list ticker")
  public static List<HashMap<String, Object>> getData(String etl) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select  (nvl(SASS_CASH,0) + nvl(SASS_TD,0) + nvl(SASS_CORP_BOND,0) + nvl(SASS_GOV_BOND,0) + nvl(SASS_STOCK,0) + nvl(SASS_MARGIN,0) + nvl(SASS_OTHER,0)) SASS   ,  ");
    queryBuilder.append(" nvl(EQT,0) EQT,  ");
    queryBuilder.append(" (nvl(SDB_LENDING,0) + nvl(SDB_LQ_BOND,0) + nvl(SDB_AC_BOND,0) + nvl(SDB_ISAVE,0) + nvl(SDB_OTHER,0)) SDB   ,  ");
    queryBuilder.append(" (nvl(LDB_LENDING,0) + nvl(LDB_LQ_BOND,0) + nvl(LDB_AC_BOND,0) + nvl(LDB_OTHER,0)) LDB  ");
    queryBuilder.append("   ");
    queryBuilder.append(" from RISK_CREDIT_INFO where ETL = :etl  ");
    try {

      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("etl",etl)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
