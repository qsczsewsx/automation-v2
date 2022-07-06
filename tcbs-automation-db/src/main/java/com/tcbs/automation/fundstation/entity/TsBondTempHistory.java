package com.tcbs.automation.fundstation.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.fundstation.entity.TcAssets.sendSessionDBAssets;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TS_BOND_TEMP_HISTORY")
public class TsBondTempHistory {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "BOND_TEMP_CODE")
  private String bondTempCode;

  @Column(name = "ISSUE_DATE")
  private Date issueDate;

  @Column(name = "VALUE")
  private Double value;

  @Column(name = "FROM_DATE")
  private Date fromDate;

  @Column(name = "ACTION")
  private String action;

  @Column(name = "SYNC_DATE")
  private Date syncDate;

  @Column(name = "BOND_TEMP_NAME")
  private String bondTempName;

  @Column(name = "COMPANY_ID")
  private Integer companyId;

  @Column(name = "COMPANY_GROUP_ID")
  private Integer companyGroupId;

  @Column(name = "REF_ID")
  private Integer refId;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  public static List<TsBondTempHistory> getListBondTempHistory() {
    Query<TsBondTempHistory> query = session.createQuery("from TsBondTempHistory order by syncDate DESC");
    return query.getResultList();
  }

  public static void insertIntoTsBondTempHistory(String bondTempCode, Date issueDate, Double value, Date fromDate, String action, Date syncDate, String bondTempName, Integer companyId, Integer companyGroupId, Integer refId) {
    Session session2 = sendSessionDBAssets();
    TsBondTempHistory tsBondTempHistory = new TsBondTempHistory();
    tsBondTempHistory.setBondTempCode(bondTempCode);
    tsBondTempHistory.setIssueDate(issueDate);
    tsBondTempHistory.setValue(value);
    tsBondTempHistory.setFromDate(fromDate);
    tsBondTempHistory.setAction(action);
    tsBondTempHistory.setSyncDate(syncDate);
    tsBondTempHistory.setBondTempName(bondTempName);
    tsBondTempHistory.setCompanyId(companyId);
    tsBondTempHistory.setCompanyId(companyGroupId);
    tsBondTempHistory.setRefId(refId);
    session2.save(tsBondTempHistory);
    session2.getTransaction().commit();
  }
}
