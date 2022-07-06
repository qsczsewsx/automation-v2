package com.tcbs.automation.iris.bond;

import com.tcbs.automation.staging.AwsStagingDwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author minhnv8
 * @date 07/06/2022 14:54
 */
public class PostUpdateCorpBondOnGoingEntity {
  @Step("Get data from iRis")
  public static List<HashMap<String, Object>> getResultCorpBondOnGoingUpdated(List<Integer> listIdIn) {
    String queryBuilder = " select * from Stg_risk_InvestmentLimit_CorpBond_OnGoing where updateDate = (select max(updateDate) from Stg_risk_InvestmentLimit_CorpBond_OnGoing) and id IN :id  ";
    HashMap<String, Object> params = new HashMap<>();
    params.put("id", listIdIn);
    return executeQuery(AwsStagingDwh.awsStagingDwhDbConnection.getSession(), queryBuilder, params);
  }

  public static void deleteCorpBondOnGoingUpdated(List<Integer> listIdIn) {
    Session session = AwsStagingDwh.awsStagingDwhDbConnection.getSession();
    Transaction trans = session.beginTransaction();
    Query<?> query;
    query = session.createNativeQuery("delete Stg_risk_InvestmentLimit_CorpBond_OnGoing where id IN :id ");

    HashMap<String, Object> params = new HashMap<>();
    params.put("id", listIdIn);
    for (String key : params.keySet()) {
      query = query.setParameter(key, params.get(key));
    }
    query.executeUpdate();
    trans.commit();
    session.clear();
  }

  public static List<HashMap<String, Object>> getResultCorpBondFinal() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select Bond_Code            as bondCode, ");
    queryBuilder.append(" Bond_Temp_Code       as bondTempCode,Bond_Case_Name       as bondCaseName, Issuer_Ga_Id         as issuerGaId,");
    queryBuilder.append(" Issuer_Ga_Group_Id   as issuerGaGroupId,Bond_Temp_Limit      as bondTempLimit,Bond_Case_Limit      as bondCaseLimit,");
    queryBuilder.append(" Issuer_Limit         as issuerLimit,Issuer_Group_Limit   as issuerGroupLimit,Bond_Balance         as bondBalance,");
    queryBuilder.append(" Bond_Temp_Balance    as bondTempBalance,Bond_Case_Balance    as bondCaseBalance,Issuer_Balance       as issuerBalance,");
    queryBuilder.append(" Issuer_Group_Balance as issuerGroupBalance,Bond_Temp_Remain     as bondTempRemain,Bond_Case_Remain     as bondCaseRemain,");
    queryBuilder.append(" Issuer_Remain        as issuerRemain,Issuer_Group_Remain  as issuerGroupRemain,Remain               as remain");
    queryBuilder.append(" from Stg_risk_InvestmentLimit_CorpBond_Final");
    queryBuilder.append(" where Updated_Date = (select max(Updated_Date) from Stg_risk_InvestmentLimit_CorpBond_Final)  order by Bond_Code ");
    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString()).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getResultOnGoing() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("select bondCode, sum(IIF(side = 'SELL', -totalAmount, totalAmount)) as totalAmount ");
    queryBuilder.append(" from Stg_risk_InvestmentLimit_CorpBond_OnGoing ");
    queryBuilder.append(" where updateDate = (select max(updateDate) from Stg_risk_InvestmentLimit_CorpBond_OnGoing) ");
    queryBuilder.append(" and CONVERT(VARCHAR, tradingDate, 112) = CONVERT(VARCHAR, getdate(), 112) group by bondCode ");
    try {
      return AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(queryBuilder.toString()).setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  private static List<HashMap<String, Object>> executeQuery(Session ss, String query, HashMap<String, Object> params) {
    try {
      NativeQuery qr = ss.createNativeQuery(query);
      for (String key : params.keySet()) {
        qr = qr.setParameter(key, params.get(key));
      }
      return qr.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
