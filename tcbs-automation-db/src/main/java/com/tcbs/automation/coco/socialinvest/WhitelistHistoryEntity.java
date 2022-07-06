package com.tcbs.automation.coco.socialinvest;

import com.tcbs.automation.DataBaseUtils;
import com.tcbs.automation.coco.CocoConnBridge;
import com.tcbs.automation.constants.coco.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "WHITELIST_HISTORY")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WhitelistHistoryEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "TICKER")
  private String ticker;

  @Column(name = "ACTION")
  @Enumerated(EnumType.STRING)
  private Constants.WhitelistAction action;

  @Column(name = "FULL_NAME")
  private String fullName;

  @Column(name = "CREATED_DATE")
  @Temporal(TemporalType.DATE)
  private Date createdDate;

  @Column(name = "CREATED_USER")
  private String createdUser;

  @Column(name = "APPROVED_DATE")
  @Temporal(TemporalType.DATE)
  private Date approvedDate;

  @Column(name = "APPROVED_USER")
  private String approvedUser;

  public static List<WhitelistHistoryEntity> filterWhitelistHistory(WhitelistHistoryParam param) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();
    Query<WhitelistHistoryEntity> query = session.createQuery(buildQuery(param) + "order by t.approvedDate desc");
    addParam(query, param);

    if (param.getPage() != null && param.getSize() != null) {
      query.setFirstResult(param.getPage() * param.getSize());
      query.setMaxResults(param.getSize());
    }
    List<WhitelistHistoryEntity> res = query.getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();
    return res;
  }

  public static long countWhitelist(WhitelistHistoryParam param) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();
    Query<Long> query = session.createQuery(
      "select count(*) " + buildQuery(param)
    );
    addParam(query, param);
    try {
      return query.getSingleResult();
    } catch (Exception ex) {
      return 0;
    } finally {
      CocoConnBridge.socialInvestConnection.closeSession();
    }
  }

  private static void addParam(Query query, WhitelistHistoryParam param) {
    if (param.getTicker() != null) {
      query.setParameter("ticker", DataBaseUtils.containsLowerCase(param.getTicker()));
    }
    if (param.getApprovedFromDate() != null) {
      query.setParameter("approvedFromDate", param.getApprovedFromDate());
    }
    if (param.getApprovedToDate() != null) {
      query.setParameter("approvedToDate", param.getApprovedToDate());
    }

    if (param.getAction() != null) {
      query.setParameter("action", param.getAction());
    }

    if (param.getCreatedUser() != null && !param.getCreatedUser().isEmpty()) {
      query.setParameter("createdUser", DataBaseUtils.containsLowerCase(param.getCreatedUser()));
    }

    if (param.getApprovedUser() != null && !param.getApprovedUser().isEmpty()) {
      query.setParameter("approvedUser", DataBaseUtils.containsLowerCase(param.getApprovedUser()));
    }
  }

  // build exact query to ensure result order
  private static String buildQuery(WhitelistHistoryParam param) {
    List<String> queries = new ArrayList<>();
    WhitelistHistoryEntity t;
    queries.add("(t.approvedDate <= current_timestamp)");
    if (param.getTicker() != null) {
      queries.add("(lower(t.ticker) like :ticker)");
    }
    if (param.getApprovedFromDate() != null) {
      queries.add("(t.approvedDate >= :approvedFromDate)");
    }
    if (param.getApprovedToDate() != null) {
      queries.add("(t.approvedDate <= :approvedToDate)");
    }

    if (param.getAction() != null) {
      queries.add("(t.action = :action)");
    }

    if (param.getCreatedUser() != null && !param.getCreatedUser().isEmpty()) {
      queries.add("(lower(t.createdUser) like :createdUser)");
    }

    if (param.getApprovedUser() != null && !param.getApprovedUser().isEmpty()) {
      queries.add("(lower(t.approvedUser) like :approvedUser)");
    }
    return "from WhitelistHistoryEntity t where " + String.join(" and ", queries);
  }
}
