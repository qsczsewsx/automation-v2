package com.tcbs.automation.intermana;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "USER_INFO")
public class UserInfo {
  static final Logger logger = LoggerFactory.getLogger(UserInfo.class);
  @Id
  @Column(name = "ID")
  private Integer id;
  @Column(name = "FULL_NAME")
  private String fullName;
  @Column(name = "IDENTITY_NUMBER")
  private String identityNumber;
  @Column(name = "IDENTITY_DATE")
  private Timestamp identityDate;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "CUSTODY_ID")
  private String custodyId;
  @CreationTimestamp
  @Column(name = "CREATED_AT")
  private Timestamp createdAt;
  @UpdateTimestamp
  @Column(name = "UPDATED_AT")
  private Timestamp updatedAt;

  @Step("get user info")
  public List<UserInfo> get(HashMap<String, Object> params) {
    Session session = IntermanaService.intermanaConnection.getSession();
    StringBuilder sql = new StringBuilder(" FROM UserInfo where 1 = 1");
    for (Map.Entry<String, Object> param : params.entrySet()) {
      sql.append(" and " + param.getKey() + " = :" + param.getKey());
    }
    Query<UserInfo> query = session.createQuery(sql.toString());
    for (Map.Entry<String, Object> param : params.entrySet()) {
      query.setParameter(param.getKey(), param.getValue());
    }
    return query.getResultList();

  }

  @Step("delete user info")
  public void delete(HashMap<String, Object> params) {
    Session session = IntermanaService.intermanaConnection.getSession();
    try {
      session.clear();
      if (!session.getTransaction().isActive()) {
        session.beginTransaction();
      }
      StringBuilder sql = new StringBuilder("delete from UserInfo where 1 = 1");
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

  public List<UserInfo> getListPage(HashMap<String, Object> params) {
    StringBuilder startPagingSql = new StringBuilder(" select id, full_name, identity_number, identity_date, custody_id, created_at, updated_at from (");
    String endPagingSql = " ) tb where tb.ordernum  >= 1 and tb.ordernum <= 10";
    StringBuilder sql = new StringBuilder(
      " select id, full_name, identity_number, identity_date, custody_id, created_at, updated_at, row_number() over (order by id desc) ordernum from user_info where 1 = 1 ");
    StringBuilder subSql = new StringBuilder("select user_id from user_related_list where status = 1");
    for (Map.Entry<String, Object> param : params.entrySet()) {
      switch (param.getKey()) {
        case "full_name":
          sql.append(" and full_name like :full_name");
          break;
        case "related_list_id":
          subSql.append(" and related_list_id = :related_list_id");
          break;
        default:
          sql.append(" and " + param.getKey() + " = :" + param.getKey());
      }
    }
    sql.append(" and id in ( ").append(subSql.toString()).append(" )");
    startPagingSql.append(sql.toString()).append(endPagingSql);
    List<UserInfo> resultList = new ArrayList<>();
    try {
      Query query = IntermanaService.intermanaConnection.getSession().createSQLQuery(startPagingSql.toString());
      for (Map.Entry<String, Object> param : params.entrySet()) {
        query.setParameter(param.getKey(), param.getValue());
      }
      List<Object[]> userList = query.getResultList();
      for (Object[] obj : userList) {
        UserInfo result = new UserInfo();
        result.setId(((BigDecimal) obj[0]).intValue());
        result.setFullName((String) obj[1]);
        result.setIdentityNumber((String) obj[2]);
        if (obj[3] != null) {
          result.setIdentityDate((Timestamp) obj[3]);
        }
        result.setCustodyId((String) obj[4]);
        result.setCreatedAt((Timestamp) obj[5]);
        result.setUpdatedAt((Timestamp) obj[6]);
        resultList.add(result);
      }
      return resultList;
    } catch (Exception e) {
      logger.error(e.getMessage(), e.getStackTrace());
      return new ArrayList<>();
    }
  }


  @Step("insert user info")
  public Integer create(UserInfo user) {
    Session session = IntermanaService.intermanaConnection.getSession();
    String sql = "select user_info_seq.nextval from dual";
    Query query = session.createSQLQuery(sql);
    Integer id = (Integer) query.uniqueResult();
    user.setId(id);

    Transaction transaction = session.getTransaction();
    session.save(user);
    transaction.commit();
    return id;
  }

}
