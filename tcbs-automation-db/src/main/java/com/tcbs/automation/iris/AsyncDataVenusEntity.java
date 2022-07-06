package com.tcbs.automation.iris;


import com.tcbs.automation.riskcloud.AwsRiskCloud;
import com.tcbs.automation.staging.AwsStagingDwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AsyncDataVenusEntity {

  @Step("Get data from AOS")
  public static List<HashMap<String, Object>> getAos() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select(  ");
    queryBuilder.append(" select sum(Value) Value from Risk_Cash where UpdatedDate = (select max(UpdatedDate) from Risk_Cash)  ");
    queryBuilder.append(" ) SASS_CASH,   ");

    queryBuilder.append(" (select sum(abs(Value))*1000000000 Value  ");
    queryBuilder.append(" from Risk_Accounting  ");
    queryBuilder.append(" where  datediff(year,getdate(), LiquidDate ) <= 1  ");
    queryBuilder.append(" and Category = 'TD' and GroupName = 'Deposit' and  LiquidDate >= getdate()  ");
    queryBuilder.append(" and UpdatedDate = (select max(UpdatedDate) from Risk_Accounting)  ");
    queryBuilder.append(" group by UpdatedDate) SASS_TD,  ");

    queryBuilder.append(" (select sum(balance)*1000000000 from Liquidity_DailyFlash  ");
    queryBuilder.append(" where updatedate = (select max(updatedate) from Liquidity_DailyFlash) and [group] = 'Bond' and balance >=0) SASS_CORP_BOND ,");

    queryBuilder.append(" (select sum(balance)*1000000000 from Liquidity_DailyFlash  ");
    queryBuilder.append(" where updatedate = (select max(updatedate) from Liquidity_DailyFlash) and [group] = 'STOCK' and balance >=0 ) SASS_STOCK,  ");

    queryBuilder.append(" (select sum(abs(Value))*1000000000 from Risk_Accounting  ");
    queryBuilder.append(" where UpdatedDate = (select max(UpdatedDate) from Risk_Accounting)  ");
    queryBuilder.append(" and datediff(year, Getdate(),LiquidDate ) <= 1 and LiquidDate >= Getdate()  and Category = 'Other_Items'  ");
    queryBuilder.append(" and GroupName = 'Other assets')  SASS_OTHER,  ");

    queryBuilder.append(" (select sum(abs(Value))*1000000000 Value  ");
    queryBuilder.append(" from Risk_Accounting  ");
    queryBuilder.append(" where  datediff(year, LiquidDate, getdate()) > 1  ");
    queryBuilder.append(" and Category = 'TD' and GroupName = 'Deposit'  ");
    queryBuilder.append(" and UpdatedDate = (select max(UpdatedDate) from Risk_Accounting)  ");
    queryBuilder.append(" group by UpdatedDate) LASS_TD,  ");

    queryBuilder.append(" (select sum(abs(Value))*1000000000 from Risk_Accounting ");
    queryBuilder.append(" where Category = 'Other_Items' and GroupName = 'Other assets' ");
    queryBuilder.append(" and UpdatedDate = (select max(UpdatedDate) from Risk_Accounting) ");
    queryBuilder.append(" and datediff(year, GETDATE(),LiquidDate) > 1) LASS_OTHER,  ");

    queryBuilder.append(" (select sum(abs(Value))*1000000000 from Risk_Accounting   ");
    queryBuilder.append(" where datediff(year,Getdate(), LiquidDate ) <= 1 and LiquidDate >= Getdate()  and Category = 'Loan'   ");
    queryBuilder.append(" and GroupName = 'Lending' and item not like '%TCBSBond%' and updateddate = (select max(updateddate) from Risk_Accounting) ) SDB_LENDING, ");

    queryBuilder.append(" (select sum(abs(Value))*1000000000 from Risk_Accounting ");
    queryBuilder.append(" where datediff(year, Getdate(),LiquidDate ) <= 1 and LiquidDate >= Getdate()  ");
    queryBuilder.append(" and category = 'Liabilities' and GroupName = 'Other Payables' ");
    queryBuilder.append(" and updateddate = (select max(updateddate) from Risk_Accounting)) SDB_OTHER,");

    queryBuilder.append(" (select sum(abs(Value))*1000000000 from Risk_Accounting where datediff(year,Getdate(), LiquidDate ) > 1    ");
    queryBuilder.append(" and Category = 'Loan'   ");
    queryBuilder.append(" and GroupName = 'Lending' and item not like '%TCBSBond%' and updateddate = (select max(updateddate) from Risk_Accounting) ) LDB_LENDING, ");

    queryBuilder.append(" (select sum(abs(Value))*1000000000 from Risk_Accounting ");
    queryBuilder.append(" where datediff(year, Getdate(),LiquidDate ) > 1 ");
    queryBuilder.append(" and category = 'Liabilities' and GroupName = 'Other Payables' ");
    queryBuilder.append(" and updateddate = (select max(updateddate) from Risk_Accounting)) LDB_OTHER, ");

    queryBuilder.append(" ( select top 1 Value*1000000000 from ( ");
    queryBuilder.append("   select distinct top 1 abs([Total]) Value, UpdateDate ");
    queryBuilder.append(" from Liquidity_Static ");
    queryBuilder.append(" where category like '%equity%' ");
    queryBuilder.append(" and updatedate = (select max(updatedate) from Liquidity_Static) ");
    queryBuilder.append(" union all ");
    queryBuilder.append(" select distinct top 1 abs([Total]) Value, UpdateDate ");
    queryBuilder.append(" from Liquidity_Dynamic ");
    queryBuilder.append(" where category like '%equity%' ");
    queryBuilder.append(" and updatedate = (select max(updatedate) from Liquidity_Dynamic) ");
    queryBuilder.append("       ) as a order by UpdateDate desc ) EQT");


    try {
      List<HashMap<String, Object>> resultList = AwsRiskCloud.awsRiskCloudDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data from stg")
  public static List<HashMap<String, Object>> getStg() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select (\n" +
        "\tselect sum(balance)  from  \n" +
        "\t(\n" +
        "\t\tSELECT a.txdate, a.SYMBOL, sum(a.BUYAMT) as 'balance' \n" +
        "\t\tFROM SE0044_StockBal a left join (Select distinct ListedCode from IRIS_CLOUD_SIT.dbo.stg_tcb_bond) b \n" +
        "\t\ton a.symbol = b.ListedCode \n" +
        "\t\twhere a.CUSTODYCD = '105P011809' \n" +
        "\t\tand a.txdate = (select max(txdate) from SE0044_StockBal) \n" +
        "\t\tand a.SYMBOL like 'TD%' and len(Symbol) > 3 \n" +
        "\t\tand b.ListedCode is null \n" +
        "\t\tand a.QUANTITY > 0 \n" +
        "\t\tgroup by a.TXDATE, a.SYMBOL\n" +
        "\t) as a \n" +
        ") SASS_GOV_BOND, \n" +
        "(select sum(LNPRIN) from IRIS_CLOUD_SIT.dbo.stg_flx_mr0013 \n" +
        " where  datereport = (select max(datereport) from IRIS_CLOUD_SIT.dbo.stg_flx_mr0013))\n" +
        " SASS_MARGIN, \n" +
        "(select top 1 BALANCE from IRIS_CLOUD_SIT.dbo.Stg_isave_TCBS_ISAVE_BALANCE \n" +
        "where  CALCULATED_TIME = (select max(CALCULATED_TIME) from IRIS_CLOUD_SIT.dbo.Stg_isave_TCBS_ISAVE_BALANCE)) SDB_ISAVE");

    try {
      List<HashMap<String, Object>> resultList = AwsRiskCloud.awsRiskCloudDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get data from IRISK")
  public static List<HashMap<String, Object>> fromApi(String etl) {
    StringBuilder query = new StringBuilder();
    query.append(" select * from RISK_CREDIT_INFO where ETL = :etl ");
    try {
      List<HashMap<String, Object>> resultList = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("etl",etl)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get loan-limit from IRISK")
  public static List<HashMap<String, Object>> getLoanLimit() {
    StringBuilder query = new StringBuilder();
    query.append(" select * from RISK_LOAN_LIMIT_DAILY where ETL_CREATED_TIME = (select max(ETL_CREATED_TIME) from RISK_LOAN_LIMIT_DAILY) ");
    try {
      List<HashMap<String, Object>> resultList = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
          .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get bond")
  public static List<HashMap<String, Object>> getBond() {
    StringBuilder query = new StringBuilder();

    query.append(" select * from Liquidity_DailyFlash ");
    query.append(" where Balance >0 and updatedate = (select max(updatedate) from Liquidity_DailyFlash) ");
    query.append(" and item like '%TCS%' and [group] in ('Prix','Pro90','Pro180','Pro360')  ");
    try {
      List<HashMap<String, Object>> resultList = AwsRiskCloud.awsRiskCloudDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get ngay dao han short Bond")
  public static List<HashMap<String, Object>> getDateBondShort() {
    StringBuilder query = new StringBuilder();

    query.append(" select * from Stg_tci_INV_BOND_STATIC  ");
    query.append(" where datediff(year, Getdate(),EXPIRED_DATE) <= 1 and EXPIRED_DATE >= Getdate()  ");
    query.append(" and EtlCurDate = (select max(EtlCurDate) from Stg_tci_INV_BOND_STATIC)  ");

    try {
      List<HashMap<String, Object>> resultList = AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get ngay dao han short Bond")
  public static List<HashMap<String, Object>> getDateBondLong() {
    StringBuilder query = new StringBuilder();
    query.append(" select * from Stg_tci_INV_BOND_STATIC  ");
    query.append(" where datediff(year, Getdate(),EXPIRED_DATE) > 1  ");
    query.append(" and EtlCurDate = (select max(EtlCurDate) from Stg_tci_INV_BOND_STATIC)  ");

    try {
      List<HashMap<String, Object>> resultList = AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get ngay dao han short Bond")
  public static List<HashMap<String, Object>> getTCSBondShort() {
    StringBuilder query = new StringBuilder();
    query.append(" select sum(abs(Value))*1000000000  Value from Risk_Accounting ");
    query.append(" where Category = 'Loan' and GroupName = 'Lending' ");
    query.append(" and item like '%TCBSBond%' ");
    query.append(" and updateddate = (select max(updateddate) from Risk_Accounting) ");
    query.append(" and  datediff(year, LiquidDate, Getdate()) <=1 and LiquidDate >= getdate() ");

    try {
      List<HashMap<String, Object>> resultList = AwsRiskCloud.awsRiskCloudDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get ngay dao han short Bond")
  public static List<HashMap<String, Object>> getTCSBondLong() {
    StringBuilder query = new StringBuilder();
    query.append(" select sum(abs(Value))*1000000000 Value from Risk_Accounting ");
    query.append(" where Category = 'Loan' and GroupName = 'Lending' ");
    query.append(" and item like '%TCBSBond%' ");
    query.append(" and updateddate = (select max(updateddate) from Risk_Accounting) ");
    query.append(" and  datediff(year, LiquidDate, Getdate()) > 1  ");

    try {
      List<HashMap<String, Object>> resultList = AwsRiskCloud.awsRiskCloudDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


}

