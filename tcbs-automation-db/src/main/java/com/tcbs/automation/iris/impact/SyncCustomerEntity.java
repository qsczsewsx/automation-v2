package com.tcbs.automation.iris.impact;

import com.tcbs.automation.iris.AwsIRis;
import com.tcbs.automation.staging.AwsStagingDwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SyncCustomerEntity {
  @Step("Get stock from BPM")
  public static List<HashMap<String, Object>> getCustomerBpm() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from RISK_CUSTOMER where UPDATE_DATE = (select max(UPDATE_DATE) from RISK_CUSTOMER)   ");
    try {

      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Get customer from Flex")
  public static List<HashMap<String, Object>> getCustomerStg(List<String> tickers) {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" select distinct a.CUSTODYCD, AFACCTNO, FULLNAME, MARGINAMT, TOTALVND, b.ACTYPE AC_TYPE, b.MARGINRATE MARGIN_RATE,b.MRIRATE MRI_RATE,b.MRMRATE MRM_RATE,b.MRLRATE MRL_RATE, b.TOTALVND TOTAL_VND, SEASS,  ");
    queryBuilder.append(" case when MARGINAMT - TOTALVND > 0 then MARGINAMT - TOTALVND when MARGINAMT - TOTALVND <= 0 then 0 end    as NET_DEBT from (  ");
    queryBuilder.append("   select  CUSTODYCD, afacctno,TXDATE_FLEX  ");
    queryBuilder.append(" from Stg_flx_MR0015 a  ");
    queryBuilder.append(" where TXDATE = (select max(txdate) from Stg_flx_MR0015)  ");
    queryBuilder.append(" and (len(SYMBOL) = 3 )  ");
    queryBuilder.append(" and SYMBOL <> 'TCB' and symbol in :tickers  ");
    queryBuilder.append(") as a  ");
    queryBuilder.append(" left join Stg_flx_VW_MR0001 b  ");
    queryBuilder.append(" on (a.CUSTODYCD  = b.CUSTODYCD and a.afacctno = b.acctno )  ");
    queryBuilder.append("  where MARGINAMT - TOTALVND > 0  and CONVERT(VARCHAR, a.TXDATE_FLEX, 112) = b.EtlCurDate ");


    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("tickers", tickers)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get margin list")
  public static List<HashMap<String, Object>> getTickerChange() {

    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" select * from RISK_RTT_TICKER_CHANGED where UPDATED_DATE= (Select max(UPDATED_DATE) from RISK_RTT_TICKER_CHANGED) ");
    try {
      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }
}