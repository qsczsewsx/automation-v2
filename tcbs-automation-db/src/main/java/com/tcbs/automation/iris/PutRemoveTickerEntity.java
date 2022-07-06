package com.tcbs.automation.iris;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor

public class PutRemoveTickerEntity {
  @Id
  private String ticker;
  private String isClean0012;
  private String statusTicker;

  public static List<HashMap<String, Object>> getListTickerRemove(List<String> ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT  * ");
    queryBuilder.append(" FROM RISK_LIST_TICKER_REMOVE  ");
    queryBuilder.append(" where ticker in :ticker and STATUS_TICKER = 'SYNCED' and TYPE_TICKER = 'EXCHANGE_REMOVE'  ");
    try {

      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Delete data by processId")
  public static void deleteData(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();


    Session session = AwsIRis.AwsIRisDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    queryBuilder.append("DELETE FROM RISK_LIST_TICKER_REMOVE where ticker = :ticker ");
    Query query = session.createNativeQuery(queryBuilder.toString()).setParameter("ticker", ticker);
    query.executeUpdate();
    session.getTransaction().commit();


  }

  @Step("Insert data by processId")
  public static void insertData(Integer id, String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    Session session = AwsIRis.AwsIRisDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    queryBuilder.append("Insert into RISK_LIST_TICKER_REMOVE(ID,TICKER,LOAN_TYPE, LOAN_PRICE, LOAN_RATIO,ROOM_FINAL,IS_CLEAN_0012,UPDATED_DATE,UPDATED_BY,STATUS_TICKER, TYPE_TICKER )\n" +
      "values (:ID,:TICKER ,'BLACK_LIST', '0','0','0','0',sysdate,'test','SYNCING', 'EXCHANGE_REMOVE' ) ");
    Query query = session.createNativeQuery(queryBuilder.toString());
    query.setParameter("TICKER", ticker);
    query.setParameter("ID", id);
    query.executeUpdate();
    session.getTransaction().commit();


  }

}