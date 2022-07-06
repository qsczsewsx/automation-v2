package com.tcbs.automation.coco.socialinvest;

import com.tcbs.automation.DataBaseUtils;
import com.tcbs.automation.coco.CocoConnBridge;
import com.tcbs.automation.constants.coco.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.query.Query;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.coco.CocoConnBridge.socialInvestConnection;

@Entity
@Data
@Table(name = "WHITELIST_TICKER")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WhitelistTickerEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Long id;

  @Column(name = "TICKER")
  private String ticker;

  @Column(name = "ACTIVE_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date activeDate;

  @Column(name = "STATUS")
  @Enumerated(EnumType.STRING)
  private Constants.WhitelistStatus status;

  @Column(name = "FULL_NAME")
  private String fullName;

  @Column(name = "INACTIVE_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date inactiveDate;

  @Column(name = "CREATED_TIME")
  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  private Date createdTime;

  @Column(name = "UPDATED_TIME")
  @Temporal(TemporalType.TIMESTAMP)
  @UpdateTimestamp
  private Date updatedTime;

  public static List<WhitelistTickerEntity> filterWhitelist(String ticker, Date activeDate, Integer page, Integer size) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();
    Query<WhitelistTickerEntity> query = session.createQuery(buildQuery(ticker, activeDate));
    if (ticker != null) {
      query.setParameter("ticker", ticker.toLowerCase());
    }
    if (activeDate != null) {
      query.setParameter("activeDate", activeDate);
    }
    if (page != null && size != null) {
      query.setFirstResult(page * size);
      query.setMaxResults(size);
    }
    List<WhitelistTickerEntity> res = query.getResultList();
    CocoConnBridge.socialInvestConnection.closeSession();
    return res;
  }

  public static long countWhitelist(String ticker, Date activeDate) {
    Session session = CocoConnBridge.socialInvestConnection.getSession();
    Query<Long> query = session.createQuery(
      "select count(*) " + buildQuery(ticker, activeDate)
    );
    if (ticker != null) {
      query.setParameter("ticker", DataBaseUtils.containsLowerCase(ticker));
    }
    if (activeDate != null) {
      query.setParameter("activeDate", activeDate);
    }
    try {
      return query.getSingleResult();
    } catch (Exception ex) {
      return 0;
    } finally {
      CocoConnBridge.socialInvestConnection.closeSession();
    }
  }

  // build exact query to ensure result order
  private static String buildQuery(String ticker, Date activeDate) {
    List<String> queries = new ArrayList<>();
    if (ticker != null) {
      queries.add("(lower(t.ticker) like :ticker)");
    }
    if (activeDate == null) {
      queries.add("(t.status = 'ACTIVE')");
    } else {
      queries.add("(t.activeDate <= :activeDate and (t.inactiveDate > :activeDate or t.inactiveDate is null))");
    }
    return "from WhitelistTickerEntity t where " + String.join(" and ", queries);
  }
}
