package com.tcbs.automation.idatastorage;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudyTemplateEntity {
  @Step("Get data from db ")
  public static List<HashMap<String, Object>> getData(String client, String user, String template) {
    StringBuilder queryBuilder = new StringBuilder();
    String templateQuery = "";
    if (template.equalsIgnoreCase("")) {
      templateQuery = " AND :template is not null ";
    }
    else {
      templateQuery = " AND name = :template  ";
    }
    queryBuilder.append(" SELECT * FROM model_studytemplate ");
    queryBuilder.append(" WHERE \"ownerId\"  = 'TCBS_ANALYST_TEMPLATE' ");
    queryBuilder.append(templateQuery);
    queryBuilder.append(" UNION ");
    queryBuilder.append(" SELECT * FROM model_studytemplate ");
    queryBuilder.append(" WHERE \"ownerId\" = :user ");
    queryBuilder.append(" AND \"ownerSource\" = :client ");
    queryBuilder.append(templateQuery);

    try {
      List<HashMap<String, Object>> resultList = Datastorage.DataStorageConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("user", user)
        .setParameter("client", client)
        .setParameter("template", template)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return resultList;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

}
