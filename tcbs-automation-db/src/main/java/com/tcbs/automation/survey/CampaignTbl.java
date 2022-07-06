package com.tcbs.automation.survey;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Getter
@Setter
@Entity
@Table(name = "campaign")
public class CampaignTbl {

  private final static Logger logger = LoggerFactory.getLogger(CampaignTbl.class);

  @Id
  @Column(name = "id")
  private Integer id;
  @Column(name = "code")
  private String campaignCode;

  @Step("filter campaign by code")
  public CampaignTbl filterCampaignByCode(String code) {
    Session session = Survey.surveyDbConnection.getSession();
    session.clear();
    try {
      Query<CampaignTbl> query = session.createQuery("from CampaignTbl where campaignCode = :campaignCode", CampaignTbl.class);
      query.setParameter("campaignCode", code);
      return query.getSingleResult();
    } catch (Exception ex) {
      logger.warn("Exception!{}", ex);
      return null;
    }
  }
}
