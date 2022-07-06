package com.tcbs.automation.iris.bond;

import com.tcbs.automation.iris.AwsIRis;
import com.tcbs.automation.staging.AwsStagingDwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GetSyncLimitInvestmentVenusEntity {
  @SuppressWarnings("unchecked")
  @Step("Get data from iRis")
  public static List<HashMap<String, Object>> getValue(String table) {
    StringBuilder queryBuilder = new StringBuilder();
    if (table.equals("Stg_risk_InvestmentLimit_CorpBond_Mapping")) {
      queryBuilder.append(" select * from Stg_risk_InvestmentLimit_CorpBond_Mapping  ");
      queryBuilder.append("where updateDate = (select max(updateDate) from Stg_risk_InvestmentLimit_CorpBond_Mapping)");
    }
    if (table.equals("Stg_risk_InvestmentLimit_CorpBond")) {
      queryBuilder.append(" select * from Stg_risk_InvestmentLimit_CorpBond  ");
      queryBuilder.append("where updateDate = (select max(updateDate) from Stg_risk_InvestmentLimit_CorpBond)");
    }
    if (table.equals("Stg_risk_InvestmentLimit_CorpBond_Attr")) {
      queryBuilder.append(" select * from Stg_risk_InvestmentLimit_CorpBond_Attr  ");
      queryBuilder.append("where updateDate = (select max(updateDate) from Stg_risk_InvestmentLimit_CorpBond_Attr)");
    }
    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @SuppressWarnings("unchecked")
  public static List<String> getListBondCode(String type, Double id) {
    StringBuilder queryBuilder = new StringBuilder();
    List<String> listBondCode = new ArrayList<>();
    switch (type) {
      case "bond_code":
        queryBuilder.append(" select BOND_CODE from RISK_BOND_INFO where UPDATED_DATE = (select max(UPDATED_DATE) from RISK_BOND_INFO) and BOND_CODE = :id");
        break;
      case "bond_temp":
        queryBuilder.append(" select BOND_CODE from RISK_BOND_INFO where UPDATED_DATE = (select max(UPDATED_DATE) from RISK_BOND_INFO) and BOND_TEMP_ID = :id");
        break;
      case "bond_case":
        queryBuilder.append(" select BOND_CODE from RISK_BOND_INFO where UPDATED_DATE = (select max(UPDATED_DATE) from RISK_BOND_INFO) and BOND_CASE_ID = :id");
        break;
      case "issuer":
        queryBuilder.append(" select BOND_CODE from RISK_BOND_INFO where UPDATED_DATE = (select max(UPDATED_DATE) from RISK_BOND_INFO) and ISSUER_ID = :id");
        break;
      case "group":
        queryBuilder.append(" select BOND_CODE from RISK_BOND_INFO where UPDATED_DATE = (select max(UPDATED_DATE) from RISK_BOND_INFO) and GROUP_ISSUER_ID = :id");
        break;
      default:
        return listBondCode;
    }
    try {
      List<HashMap<String, Object>> resultList = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("id", id)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

      resultList.forEach(v -> {
        listBondCode.add(v.get("BOND_CODE").toString());
      });

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return listBondCode;
  }
}
