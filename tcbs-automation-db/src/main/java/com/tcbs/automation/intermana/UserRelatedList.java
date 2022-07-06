package com.tcbs.automation.intermana;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "USER_RELATED_LIST")
public class UserRelatedList {
  static final Logger logger = LoggerFactory.getLogger(UserRelatedList.class);
  @Id
  @Column(name = "ID")
  private Integer id;
  @Column(name = "USER_ID")
  private Integer userId;
  @Column(name = "ORGANIZATION_ID")
  private Integer organizationId;
  @Column(name = "RELATED_LIST_ID")
  private Integer relatedListId;
  @Column(name = "STATUS")
  private Integer status;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "FROM_DATE")
  private Timestamp fromDate;
  @Column(name = "TO_DATE")
  private Timestamp toDate;
  @Column(name = "CREATED_AT")
  private Timestamp createdAt;
  @Column(name = "UPDATED_AT")
  private Timestamp updatedAt;
  @Transient
  private String listType;
  @Transient
  private String fullName;

  @Step("get userRelatedList")
  public UserRelatedList get(Integer userId, Integer relatedListId) {
    Session session = IntermanaService.intermanaConnection.getSession();
    StringBuilder sql = new StringBuilder(" FROM UserRelatedList where userId = :userId and relatedListId = :relatedListId");
    Query<UserRelatedList> query = session.createQuery(sql.toString());
    query.setParameter("relatedListId", relatedListId);
    query.setParameter("userId", userId);
    return query.uniqueResult();
  }

  @Step("delete userRelatedList")
  public void delete(HashMap<String, Object> params) {
    Session session = IntermanaService.intermanaConnection.getSession();
    try {
      session.clear();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      StringBuilder sql = new StringBuilder("delete from UserRelatedList where 1 = 1");
      for (Map.Entry<String, Object> param : params.entrySet()) {
        sql.append(" and " + param.getKey() + " = :" + param.getKey());
      }
      Query<UserInfo> query = session.createQuery(sql.toString());
      for (Map.Entry<String, Object> param : params.entrySet()) {
        query.setParameter(param.getKey(), param.getValue());
      }
      query.executeUpdate();
      session.getTransaction().commit();
    } catch (Exception e) {
      session.getTransaction().rollback();
    }
  }

  @Step("get detail")
  public List<UserRelatedList> getDetail(Integer userId) {
    StringBuilder sql = new StringBuilder("select ur.id, ur.related_list_id,rl.list_type, ui.full_name, ur.status, ur.description, ur.from_date, ur.to_date");
    sql.append(" from user_related_list ur left join user_info ui on ui.id = ur.related_user_id");
    sql.append(" left join related_list rl on rl.id = ur.related_list_id");
    sql.append(" where user_id = :userId and status = 1");

    Query query = IntermanaService.intermanaConnection.getSession().createSQLQuery(sql.toString());
    query.setParameter("userId", userId);

    List<Object[]> objList = query.getResultList();
    List<UserRelatedList> rs = new ArrayList<>();
    for (Object[] obj : objList) {
      UserRelatedList url = new UserRelatedList();
      url.setId(((BigDecimal) obj[0]).intValue());
      url.setRelatedListId(((BigDecimal) obj[1]).intValue());
      url.setListType((String) obj[2]);
      url.setFullName((String) obj[3]);
      url.setStatus(((BigDecimal) obj[4]).intValue());
      url.setDescription((String) obj[5]);
      url.setFromDate(obj[6] != null ? (Timestamp) obj[6] : null);
      url.setToDate(obj[7] != null ? (Timestamp) obj[7] : null);
      rs.add(url);
    }
    return rs;

  }

}
