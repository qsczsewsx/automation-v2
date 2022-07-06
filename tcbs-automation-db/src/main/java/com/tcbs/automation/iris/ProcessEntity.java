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


public class ProcessEntity {
  @Id
  private String bpmProcessId;
  private String createdBy;
  private String createdDate;
  private String note;
  private String processType;
  private String status;

  @Step("Get data by ProcessId")
  public static List<HashMap<String, Object>> getProcessBpm(String bpmProcessId, String createdBy, String note, String processType, String status) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT  PROCESS_ID, CREATED_BY,CREATED_DATE, STATUS, NOTE, PROCESS_TYPE ");
    queryBuilder.append(" FROM RISK_BPM_PROCESS ");
    queryBuilder.append(" where PROCESS_ID = :bpmProcessId");
    if (createdBy.equalsIgnoreCase("")) {
      queryBuilder.append(" and CREATED_BY is null ");
    } else {
      queryBuilder.append(" and CREATED_BY= :createdBy ");
    }
    if (note.equalsIgnoreCase("")) {
      queryBuilder.append(" and note is null ");
    } else {
      queryBuilder.append(" and NOTE = :note ");
    }
    queryBuilder.append(" and PROCESS_TYPE = :processType ");
    queryBuilder.append(" and STATUS = :status ");

    try {
      Query query = AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString());
      query.setParameter("bpmProcessId", bpmProcessId)
        .setParameter("processType", processType)
        .setParameter("status", status);
      if (!note.equalsIgnoreCase("")) {
        query.setParameter("note", note);
      }
      if (!createdBy.equalsIgnoreCase("")) {
        query.setParameter("createdBy", createdBy);
      }
      return query.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }


    return new ArrayList<>();
  }

  @Step("Get lastest data")
  public static List<HashMap<String, Object>> getLastestProcess() {
    StringBuilder queryBuilder = new StringBuilder();

    queryBuilder.append(" SELECT  PROCESS_ID, CREATED_BY, STATUS, NOTE, PROCESS_TYPE ");
    queryBuilder.append(" FROM RISK_BPM_PROCESS ");
    queryBuilder.append(" where PROCESS_TYPE = 'QUARTER' AND CREATED_DATE = (select max(created_date) from RISK_BPM_PROCESS  where PROCESS_TYPE = 'QUARTER')  ");

    try {

      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  @Step("Delete data by processId")
  public static void deleteData(String bpmProcessId) {
    StringBuilder queryBuilder = new StringBuilder();


    Session session = AwsIRis.AwsIRisDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    queryBuilder.append("DELETE FROM RISK_BPM_PROCESS where PROCESS_ID = :processId ");
    Query query = session.createNativeQuery(queryBuilder.toString()).setParameter("processId", bpmProcessId);
    query.executeUpdate();
    session.getTransaction().commit();


  }

  @Step("Insert data by processId")
  public static void insertData(String bpmProcessId) {
    StringBuilder queryBuilder = new StringBuilder();


    Session session = AwsIRis.AwsIRisDbConnection.getSession();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    queryBuilder.append("Insert into RISK_BPM_PROCESS(PROCESS_ID,STATUS, PROCESS_TYPE, CREATED_DATE) values (:processId ,'DONE', 'QUARTER',sysdate ) ");
    Query query = session.createNativeQuery(queryBuilder.toString()).setParameter("processId", bpmProcessId);
    query.executeUpdate();
    session.getTransaction().commit();


  }
}