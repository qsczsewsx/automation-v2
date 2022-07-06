package com.tcbs.automation.iris.bond;


import com.tcbs.automation.iris.AwsIRis;
import com.tcbs.automation.riskcloud.AwsRiskCloud;
import com.tcbs.automation.staging.AwsStagingDwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class GetCorpBondBalanceEntity {

  private static final String QUANTITY = "QUANTITY";
  private static final String PRICE = "Price";

  @SuppressWarnings("unchecked")
  @Step("Get corp bond listed from Flex")
  public static List<HashMap<String, Object>> getCorpBondFromFlex() {
    List<HashMap<String, Object>> result = new ArrayList<>();
    try {
      StringBuilder queryFromTcbBond = new StringBuilder();
      queryFromTcbBond.append(" SELECT Code, Price FROM Stg_tcb_Bond WHERE EtlCurDate = (SELECT MAX(EtlCurDate) FROM Stg_tcb_Bond) AND ListedStatus = 1 ");
      List<HashMap<String, Object>> listBondFromTcbBond = (List<HashMap<String, Object>>) AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryFromTcbBond.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

      StringBuilder queryFromStockBal = new StringBuilder();
      queryFromStockBal.append(" SELECT SYMBOL, SUM(QUANTITY) as QUANTITY FROM SE0044_StockBal  WHERE CUSTODYCD = '105P011809' AND txdate = ");
      queryFromStockBal.append(" (SELECT max(txdate) FROM SE0044_StockBal) AND LEN(SYMBOL) > 5  GROUP BY SYMBOL ");
      List<HashMap<String, Object>> listBondFromStockBal = (List<HashMap<String, Object>>) AwsRiskCloud.awsRiskCloudDbConnection.getSession().createNativeQuery(queryFromStockBal.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      listBondFromTcbBond.forEach(v -> {
        for (HashMap<String, Object> item : listBondFromStockBal) {
          if (item.get("SYMBOL").toString().equals(v.get("Code").toString())) {
            v.put(QUANTITY, v.get(QUANTITY));
            break;
          }
        }
      });
      listBondFromTcbBond.forEach(x -> {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("CODE", x.get("Code"));
        hashMap.put("PRICE", x.get(PRICE));
        Double quantity = null;
        if (x.get(PRICE) != null && x.get(QUANTITY) != null) {
          quantity = Double.parseDouble(x.get(PRICE).toString()) * Double.parseDouble(x.get(QUANTITY).toString());
        }
        hashMap.put("BALANCE", quantity);
        result.add(hashMap);
      });
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @Step("Get corp bond balance in OTC")
  public static List<HashMap<String, Object>> getCorpBondBalanceInOTC(String type) {
    StringBuilder query = new StringBuilder();
    if (type.equalsIgnoreCase("Treasury")) {
      query.append(" select BondCode , SUM(ParValue_Sum) as ParValue_Sum,  SUM(TradingValue_Sum) as TradingValue_Sum from iris_smy_bond_tre_intraday group by BondCode ");
    } else if (type.equalsIgnoreCase("Retail")) {
      query.append(" select BondCode , SUM(ParValue_Sum) as ParValue_Sum,  SUM(TradingValue_Sum) as TradingValue_Sum from iris_smy_bond_retail_intraday group by BondCode ");
    } else {
      return new ArrayList<>();
    }
    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @SuppressWarnings("unchecked")
  @Step("Get corp bond balance from DB")
  public static List<HashMap<String, Object>> getCorpBondBalanceFromDB() {
    StringBuilder query = new StringBuilder();
    query.append(" select * from Stg_Risk_Bond_Balance where Updated_Date = (select max(Updated_Date) from Stg_Risk_Bond_Balance)  ");
    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @SuppressWarnings("unchecked")
  @Step("Get bond code and balance info from DB")
  public static List<HashMap<String, Object>> getBondCodeAndBalanceFromDB() {
    StringBuilder query = new StringBuilder();
    query.append(" select Bond_Code, Bond_Balance from Stg_Risk_Bond_Balance where Updated_Date = (select max(Updated_Date) from Stg_Risk_Bond_Balance)  ");
    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @SuppressWarnings("unchecked")
  @Step("Get bond code  info from DB")
  public static List<String> getBondCodeFromDB() {
    StringBuilder query = new StringBuilder();
    query.append(" select Bond_Code from Stg_Risk_Bond_Balance where Updated_Date = (select max(Updated_Date) from Stg_Risk_Bond_Balance)  ");
    try {
      List<HashMap<String, Object>> data = AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      List<String> listBondCode = new ArrayList<>();
      data.forEach(v -> {
        listBondCode.add(v.get("Bond_Code").toString());
      });
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @SuppressWarnings("unchecked")
  @Step("Get bond code information from DB")
  public static HashMap<String, Object> getCorpBondInfoFromDB() {
    StringBuilder query = new StringBuilder();
    query.append(" SELECT  BOND_CODE, BOND_TEMP_CODE, BOND_CASE_NAME, GA_ID, GROUP_ISSUER_ID FROM RISK_BOND_INFO WHERE UPDATED_DATE = (SELECT MAX(UPDATED_DATE)  FROM RISK_BOND_INFO)  ");
    try {
      List<HashMap<String, Object>> resultListBondInfo = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      HashMap<String, Object> listCorpBondHashMap = new HashMap<>();
      resultListBondInfo.forEach(v -> {
        listCorpBondHashMap.put(v.get("BOND_CODE").toString(), v.get("BOND_TEMP_CODE") + "/" + v.get("BOND_CASE_NAME") + "/" + v.get("GA_ID") + "/" + v.get("GROUP_ISSUER_ID"));
      });
      return listCorpBondHashMap;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new HashMap<>();
  }

  @SuppressWarnings("unchecked")
  @Step("Get list bond information from DB")
  public static List<String> getCorpBondInfo() {
    StringBuilder query = new StringBuilder();
    List<String> listCorpBond = new ArrayList<>();
    query.append(" SELECT  BOND_CODE FROM RISK_BOND_INFO WHERE UPDATED_DATE = (SELECT MAX(UPDATED_DATE)  FROM RISK_BOND_INFO)  ");
    try {
      List<HashMap<String, Object>> resultListBondInfo = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      resultListBondInfo.forEach(v -> {
        listCorpBond.add(v.get("BOND_CODE").toString());
      });
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return listCorpBond;
  }

  @SuppressWarnings("unchecked")
  @Step("Get bond code  having balance from DB")
  public static List<HashMap<String, Object>> getListBondCodeHavingBalanceFromDB() {
    StringBuilder query = new StringBuilder();
    query.append(" select Bond_Code, Bond_Balance  from Stg_Risk_Bond_Balance where Updated_Date = (select max(Updated_Date) from Stg_Risk_Bond_Balance) ");
    query.append(" and Bond_Balance is not null and Bond_Balance > 0 ");
    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @SuppressWarnings("unchecked")
  @Step("Get bond code  having balance from DB")
  public static ArrayList<String> getListBondCodeNameHavingBalanceFromDB() {
    StringBuilder query = new StringBuilder();
    query.append(" select Bond_Code from Stg_Risk_Bond_Balance where Updated_Date = (select max(Updated_Date) from Stg_Risk_Bond_Balance) ");
    query.append(" and Bond_Balance is not null and Bond_Balance > 0 ");
    ArrayList<String> result = new ArrayList<>();
    try {
      List<HashMap<String, Object>> data = AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      data.forEach(v -> {
        result.add(v.get("Bond_Code").toString());
      });

      return result;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
