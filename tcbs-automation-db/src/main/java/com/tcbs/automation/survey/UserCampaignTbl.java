package com.tcbs.automation.survey;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Getter
@Setter
@Entity
@Table(name = "user_campaign")
public class UserCampaignTbl {
  @Id
  @Column(name = "id")
  private long id;
  @Column(name = "tcbsid")
  private String tcbsId;
  @Column(name = "campaign_code")
  private String campaignCode;
  @Column(name = "status")
  private String status;
  @Column(name = "created_date")
  private Timestamp createdDate;
  @Column(name = "last_modified_date")
  private Timestamp lastModifiedDate;

  @Step("insert user campaign")
  public void insertUserCampaign(String campaignCode, String tcbsId) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(
      String.format("INSERT INTO user_campaign (tcbsid, campaign_code, status, created_date, last_modified_date) VALUES ('%s', '%s', 'PENDING', '%s', '%s');", tcbsId, campaignCode,
        DateTime.now(), DateTime.now()));

    try {
      Survey.surveyDbConnection.getSession().createNativeQuery(queryStringBuilder.toString()).executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Step("delete user campaign")
  public void deleteUserCampaign(String campaignCode, String tcbsId) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(String.format("DELETE FROM user_campaign WHERE campaign_code = '%s' AND tcbsid = '%s'; \r\n", campaignCode, tcbsId));

    try {
      Survey.surveyDbConnection.getSession().createNativeQuery(queryStringBuilder.toString()).executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
