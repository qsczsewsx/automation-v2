package com.tcbs.automation.evoting;

import com.tcbs.automation.functions.PublicConstant;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ntp.TimeStamp;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "user_vote")
public class UserVoteTbl {

  private final static Logger logger = LoggerFactory.getLogger(UserVoteTbl.class);

  @Id
  @Column(name = "id")
  private Integer id;

  @Column(name = "voteid")
  private String voteid;

  @Column(name = "tcbsid")
  private String tcbsId;

  @Column(name = "status")
  private String status;

  @Column(name = "raw_response")
  private String rawResponse;

  @Column(name = "created_at")
  private TimeStamp createdAt;

  @Column(name = "updated_at")
  private TimeStamp updatedAt;

  public List<UserVoteResponse> getUser(String campaignCode, String status) {
    StringBuilder sql = new StringBuilder("select uv.id, v.campaign_code, v.name, uv.tcbsid, ua.fund_amount, uv.status, uv.created_at, uv.updated_at");
    sql.append(" from user_vote uv join vote v on  uv.voteid = v.id left join user_attribute ua on (uv.voteid = ua.voteid and uv.tcbsid = ua.tcbsid) where 1 = 1");
    HashMap<String, Object> params = new HashMap<>();
    if (StringUtils.isNotEmpty(campaignCode)) {
      sql.append(" and v.campaign_code = :code");
      params.put("code", campaignCode);
    }

    if (StringUtils.isNotEmpty(status)) {
      sql.append(" and uv.status = :status");
      params.put("status", status);
    }
    sql.append(" order by id desc");
    List<UserVoteResponse> resultList = new ArrayList<>();
    try {
      Query query = Vote.voteDbConnection.getSession().createSQLQuery(sql.toString());
      for (Map.Entry<String, Object> param : params.entrySet()) {
        query.setParameter(param.getKey(), param.getValue());
      }
      List<Object[]> userList = query.getResultList();
      for (Object[] obj : userList) {
        UserVoteResponse result = new UserVoteResponse();
        result.setId((Integer) obj[0]);
        result.setCampaignCode((String) obj[1]);
        result.setName((String) obj[2]);
        result.setFundAmount(obj[4] != null ? BigDecimal.valueOf((Double) obj[4]).setScale(6).toString() : "0.0");
        result.setStatus((String) obj[5]);
        result.setCreatedAt(PublicConstant.isoDateFormat.format(obj[6]));
        result.setUpdatedAt(PublicConstant.isoDateFormat.format(obj[7]));
        resultList.add(result);
      }
      return resultList;
    } catch (Exception e) {
      logger.warn("Exception!{}", e);
      return new ArrayList<>();
    }
  }

  public void deleteDataImport(Integer voteid) {
    Session session = Vote.voteDbConnection.getSession();
    String sql1 = "delete from user_vote where voteid = :voteid";
    String sql2 = "delete from user_attribute where voteid = :voteid";
    Transaction trans = session.beginTransaction();
    try {
      Query query1 = session.createSQLQuery(sql1);
      query1.setParameter("voteid", voteid);
      Query query2 = session.createSQLQuery(sql2);
      query2.setParameter("voteid", voteid);
      query1.executeUpdate();
      query2.executeUpdate();
      trans.commit();
      session.clear();
    } catch (Exception e) {
      trans.rollback();
      logger.warn("Exception!{}", e);
    }


  }

}
