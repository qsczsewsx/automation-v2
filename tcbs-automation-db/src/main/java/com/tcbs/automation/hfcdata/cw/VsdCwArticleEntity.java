package com.tcbs.automation.hfcdata.cw;

import com.tcbs.automation.hfcdata.HfcData;
import lombok.*;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "VSD_CW_ARTICLE")
public class VsdCwArticleEntity {

  private String cwSymbol;
  private Timestamp publishTime;
  private String refLink;
  private String articleTitle;
  private String articleContent;
  private Boolean isVietstockUpdated;
  private Timestamp createdDate;
  private Timestamp updatedDate;
  private String outputPublishTime;
  private String lang;
  @Id
  private String id;
  private String publishType;

  public static java.util.List<HashMap<String, Object>> getContent(String lang, String cw, String id, String type) {
    StringBuilder queryStringBuilder = new StringBuilder();
    if (lang.equalsIgnoreCase("vi")) {
      queryStringBuilder.append(" select CW_SYMBOL , TO_CHAR(ARTICLE_CONTENT) ARTICLE_CONTENT  from VW_CW_ARTICLE");
    } else if (lang.equalsIgnoreCase("en")) {
      queryStringBuilder.append("  select CW_SYMBOL, ARTICLE_CONTENT from ( ");
      queryStringBuilder.append(" select CW_SYMBOL, TO_CHAR(ARTICLE_CONTENT)  ARTICLE_CONTENT, ");
      queryStringBuilder.append(" SUBSTR(a.REF_LINK, INSTR(a.REF_LINK, '/', -1) + 1, LENGTH(a.REF_LINK)) ID, ");
      queryStringBuilder.append(" 'LISTED' AS  PUBLISH_TYPE ");
      queryStringBuilder.append(" from VSD_CW_ARTICLE_ENG a ) ");
    }
    queryStringBuilder.append("  where CW_SYMBOL = :cw and  id = :id and PUBLISH_TYPE = :type ");
    try {
      return HfcData.hfcDataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("cw", cw)
        .setParameter("id", id)
        .setParameter("type", type)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  public static List<HashMap<String, Object>> getByLang(String lang) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append("  SELECT * FROM VW_CW_ARTICLE ");
    queryStringBuilder.append("  WHERE PUBLISH_DATE >= trunc(add_weeks(sysdate,-2)) ");
    queryStringBuilder.append("  ORDER BY PUBLISH_DATE ASC ");

    try {
      return HfcData.hfcDataDbConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }


  @Step("insert data")
  public static void insertData(VsdCwArticleEntity entity) {
    Session session = HfcData.hfcDataDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    StringBuilder queryStringBuilder = new StringBuilder();

    if (entity.getLang().equalsIgnoreCase("vi")) {
      queryStringBuilder.append(" INSERT INTO VSD_CW_ARTICLE");
    } else {
      queryStringBuilder.append(" INSERT INTO VSD_CW_ARTICLE_ENG");
    }
    queryStringBuilder.append("(CW_SYMBOL, PUBLISH_TIME, REF_LINK, ARTICLE_TITLE, ARTICLE_CONTENT, IS_VIETSTOCK_UPDATED, CREATED_DATE, UPDATED_DATE) ");
    queryStringBuilder.append("values (?, ?, ?, ?, ?, ?, ?, ?)");

    Query<?> query = session.createNativeQuery(queryStringBuilder.toString());
    query.setParameter(1, entity.getCwSymbol());
    query.setParameter(2, entity.getPublishTime());
    query.setParameter(3, entity.getRefLink());
    query.setParameter(4, entity.getArticleTitle());
    query.setParameter(5, entity.getArticleContent());
    query.setParameter(6, entity.getVietstockUpdated());
    query.setParameter(7, entity.getCreatedDate());
    query.setParameter(8, entity.getUpdatedDate());
    query.executeUpdate();
    trans.commit();
  }

  @Step("delete data by object")
  public static void deleteData(VsdCwArticleEntity entity) {
    Session session = HfcData.hfcDataDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query;

    if (entity.getLang().equalsIgnoreCase("vi")) {
      query = session.createNativeQuery(" DELETE from VSD_CW_ARTICLE where CW_SYMBOL = :cw");
    } else {
      query = session.createNativeQuery(" DELETE from VSD_CW_ARTICLE_ENG where CW_SYMBOL = :cw");
    }
    query.setParameter("cw", entity.getCwSymbol());
    query.executeUpdate();
    trans.commit();
  }

  @Basic
  @Column(name = "CW_SYMBOL")
  public String getCwSymbol() {
    return cwSymbol;
  }

  public void setCwSymbol(String cwSymbol) {
    this.cwSymbol = cwSymbol;
  }

  @Basic
  @Column(name = "PUBLISH_TIME")
  public Timestamp getPublishTime() {
    return publishTime;
  }

  public void setPublishTime(Timestamp publishTime) {
    this.publishTime = publishTime;
  }

  @Basic
  @Column(name = "REF_LINK")
  public String getRefLink() {
    return refLink;
  }

  public void setRefLink(String refLink) {
    this.refLink = refLink;
  }

  @Basic
  @Column(name = "ARTICLE_TITLE")
  public String getArticleTitle() {
    return articleTitle;
  }

  public void setArticleTitle(String articleTitle) {
    this.articleTitle = articleTitle;
  }

  @Basic
  @Column(name = "ARTICLE_CONTENT")
  public String getArticleContent() {
    return articleContent;
  }

  public void setArticleContent(String articleContent) {
    this.articleContent = articleContent;
  }

  @Basic
  @Column(name = "IS_VIETSTOCK_UPDATED")
  public Boolean getVietstockUpdated() {
    return isVietstockUpdated;
  }

  public void setVietstockUpdated(Boolean vietstockUpdated) {
    isVietstockUpdated = vietstockUpdated;
  }

  @Basic
  @Column(name = "CREATED_DATE")
  public Timestamp getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Timestamp createdDate) {
    this.createdDate = createdDate;
  }

  @Basic
  @Column(name = "UPDATED_DATE")
  public Timestamp getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Timestamp updatedDate) {
    this.updatedDate = updatedDate;
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  public String getOutputPublishTime() {
    return outputPublishTime;
  }

  public void setOutputPublishTime(String outputPublishTime) {
    this.outputPublishTime = outputPublishTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VsdCwArticleEntity that = (VsdCwArticleEntity) o;
    return Objects.equals(cwSymbol, that.cwSymbol) &&
      Objects.equals(publishTime, that.publishTime) &&
      Objects.equals(refLink, that.refLink) &&
      Objects.equals(articleTitle, that.articleTitle) &&
      Objects.equals(articleContent, that.articleContent) &&
      Objects.equals(isVietstockUpdated, that.isVietstockUpdated) &&
      Objects.equals(createdDate, that.createdDate) &&
      Objects.equals(updatedDate, that.updatedDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cwSymbol, publishTime, refLink, articleTitle, articleContent, isVietstockUpdated, createdDate, updatedDate);
  }

}
