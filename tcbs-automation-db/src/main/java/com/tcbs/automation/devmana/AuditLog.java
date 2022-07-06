package com.tcbs.automation.devmana;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "audit_log")
public class AuditLog {
  static final Logger logger = LoggerFactory.getLogger(AuditLog.class);
  @Id
  @Column(name = "id")
  private Integer id;
  @Column(name = "action_type")
  private String actionType;
  @Column(name = "device_token")
  private String deviceToken;
  @Column(name = "device_uuid")
  private String deviceUuid;
  @Column(name = "content")
  private String content;
  @Column(name = "tcbs_id")
  private String tcbsID;
  @Column(name = "group_token")
  private String groupToken;
  @Column(name = "topic")
  private String topic;
  @Column(name = "status")
  private Integer status;
  @Column(name = "created_at")
  private Timestamp createdAt;
  @Column(name = "last_updated_at")
  private Timestamp lastUpdatedAt;
  @Column(name = "description")
  private String description;

  @Step("get log")
  public List<AuditLog> get(HashMap<String, Object> params) {
    StringBuilder sql = new StringBuilder("SELECT id, topic, tcbs_id, created_at, status FROM (");
    sql.append(" select id, topic, tcbs_id, created_at, status, row_number() over (order by id desc) rownum from audit_log where 1 =1 ");

    for (Map.Entry<String, Object> param : params.entrySet()) {
      switch (param.getKey()) {
        case "fromDate":
          sql.append(" and created_at >= :fromDate");
          break;
        case "toDate":
          sql.append(" and created_at <= :toDate");
          break;
        default:
          sql.append(" and " + param.getKey() + " = :" + param.getKey());
      }
    }
    sql.append(") tb where tb.rownum  >= 1 and tb.rownum <= 10");
    List<AuditLog> resultList = new ArrayList<>();
    try {
      Query query = DevmanaService.devmanaConnection.getSession().createSQLQuery(sql.toString());
      for (Map.Entry<String, Object> param : params.entrySet()) {
        query.setParameter(param.getKey(), param.getValue());
      }
      List<Object[]> userList = query.getResultList();
      for (Object[] obj : userList) {
        AuditLog result = new AuditLog();
        result.setId((Integer) obj[0]);
        result.setTopic((String) obj[1]);
        result.setTcbsID((String) obj[2]);
        result.setCreatedAt((Timestamp) obj[3]);
        result.setStatus((Integer) obj[4]);
        resultList.add(result);
      }
      return resultList;
    } catch (Exception e) {
      logger.error(e.getMessage(), e.getStackTrace());
      return new ArrayList<>();
    }
  }

}
