package com.tcbs.automation.survey;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "survey")
public class SurveyTbl {

  private final static Logger logger = LoggerFactory.getLogger(SurveyTbl.class);

  @Id
  @Column(name = "id")
  private long id;
  @Column(name = "url")
  private String url;
  @Column(name = "campaign_code")
  private String campaignCode;
  @Column(name = "is_public")
  private boolean isPublic;
  @Column(name = "is_secured")
  private boolean isSecured;

  @Step("filter survey")
  public List<Map<String, Object>> filterSurvey(String campaignCode, int limit, int page) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("SELECT id, url, name, campaign_code, is_public, is_secured \r\n");
    queryStringBuilder.append("FROM survey \r\n");

    if (!StringUtils.isEmpty(campaignCode)) {
      queryStringBuilder.append(String.format("WHERE campaign_code = '%s' \r\n", campaignCode));
    }

    queryStringBuilder.append("ORDER BY id DESC \r\n");
    queryStringBuilder.append(String.format("LIMIT %d \r\n", limit));
    queryStringBuilder.append(String.format("OFFSET %d", (page == 0 ? 0 : (page - 1))));

    List<Map<String, Object>> result = new ArrayList<>();
    try {
      result = Survey.surveyDbConnection.getSession().createNativeQuery(queryStringBuilder.toString()).getResultList();
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return result;
  }

  @Step("insert survey")
  public void insertSurvey(CreateSurveyRequest createSurveyRequest) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(String.format("INSERT INTO campaign (code) VALUES ('%s'); \r\n", createSurveyRequest.getCampaignCode()));
    queryStringBuilder.append(
      String.format("INSERT INTO survey (url, name, campaign_code, is_public, is_secured) VALUES ('%s', '%s', '%s', %s, %s); \r\n", createSurveyRequest.getUrl(), createSurveyRequest.getName(),
        createSurveyRequest.getCampaignCode(), createSurveyRequest.isPublic(), createSurveyRequest.isSecured()));

    try {
      Survey.surveyDbConnection.getSession().createNativeQuery(queryStringBuilder.toString()).executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Step("delete survey")
  public void deleteSurvey(String campaignCode) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(String.format("DELETE FROM campaign WHERE code = '%s'; \r\n", campaignCode));
    queryStringBuilder.append(String.format("DELETE FROM survey WHERE campaign_code = '%s'; \r\n", campaignCode));

    try {
      Survey.surveyDbConnection.getSession().createNativeQuery(queryStringBuilder.toString()).executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Step("filter survey by campaignCodes")
  public List<SurveyTbl> filterSurveyByCampaignCode(List<String> campaignCodes) {
    StringBuilder queryStringBuilder = new StringBuilder("FROM SurveyTbl ");

    if (campaignCodes.size() > 0) {
      queryStringBuilder.append("WHERE campaignCode IN (:campaignCodes)");
    }
    List<SurveyTbl> result = new ArrayList<>();
    try {
      Query<SurveyTbl> query = Survey.surveyDbConnection.getSession().createQuery(queryStringBuilder.toString(), SurveyTbl.class);
      query.setParameterList("campaignCodes", campaignCodes);
      return query.getResultList();
    } catch (Exception ex) {
      logger.warn("Exception!{}", ex);
      return new ArrayList<>();
    }
  }

}
