package com.tcbs.automation.evoting;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "vote")
public class VoteTbl {

  private final static Logger logger = LoggerFactory.getLogger(VoteTbl.class);

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", unique = true, nullable = false)
  private Integer id;
  @Column(name = "name")
  private String name;
  @Column(name = "description")
  private String description;
  @Column(name = "campaign_code")
  private String campaignCode;
  @Column(name = "start_date")
  private Timestamp startDate;
  @Column(name = "end_date")
  private Timestamp endDate;
  @Column(name = "created_at")
  private Timestamp createdAt;
  @Column(name = "updated_at")
  private Timestamp updatedAt;

  @Step("get vote")
  public List<VoteTbl> getVote(int page, int size) {
    StringBuilder queryStringBuilder = new StringBuilder("FROM VoteTbl ");
    queryStringBuilder.append("ORDER BY id DESC");
    try {
      Query<VoteTbl> query = Vote.voteDbConnection.getSession().createQuery(queryStringBuilder.toString(), VoteTbl.class);
      query.setMaxResults(size);
      query.setFirstResult((page - 1) * size);
      return query.getResultList();
    } catch (Exception ex) {
      logger.warn("Exception!{}", ex);
      return new ArrayList<>();
    }
  }

  @Step("filter vote")
  public List<VoteTbl> filterVote(String campaignCode) {
    StringBuilder queryStringBuilder = new StringBuilder("FROM VoteTbl WHERE 1=1");
    Map<String, Object> paramsMap = new HashMap<>();
    if (StringUtils.isNotEmpty(campaignCode)) {
      queryStringBuilder.append(" AND campaignCode = :campaignCode");
      paramsMap.put("campaignCode", campaignCode);
    }
    try {
      Query<VoteTbl> query = Vote.voteDbConnection.getSession().createQuery(queryStringBuilder.toString(), VoteTbl.class);
      for (Map.Entry<String, Object> entry : paramsMap.entrySet()) {
        query.setParameter(entry.getKey(), entry.getValue());
      }
      return query.getResultList();
    } catch (Exception ex) {
      logger.warn("Exception!{}", ex);
      return new ArrayList<>();
    }

  }

  @Step("add vote")
  public void addVote(VoteTbl voteTbl) {
    Session session = Vote.voteDbConnection.getSession();
    session.beginTransaction();
    try {
      session.save(voteTbl);
      session.getTransaction().commit();
      session.clear();
    } catch (Exception e) {
      logger.warn("Exception!{}", e);
      session.getTransaction().rollback();
    }

  }

  @Step("delete vote")
  public void deleteVote(Integer id) {
    String sql = "delete from vote where id = :id";
    Session session = Vote.voteDbConnection.getSession();
    session.beginTransaction();
    try {
      Query query = session.createSQLQuery(sql);
      query.setParameter("id", id);
      query.executeUpdate();
      session.getTransaction().commit();
      session.clear();
    } catch (Exception e) {
      logger.warn("Exception!{}", e);
    }

  }
}
