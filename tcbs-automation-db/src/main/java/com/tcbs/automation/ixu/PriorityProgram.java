package com.tcbs.automation.ixu;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import net.thucydides.core.annotations.Step;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Table;
import java.util.Date;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "PRIORITY_PROGRAM")
public class PriorityProgram {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;

  @Column(name = "CATEGORY")
  private String category;

  @Column(name = "PROGRAM")
  private String program;

  @Column(name = "CAMPAIGN_ID")
  private String campaignId;

  @Column(name = "PRIORITY")
  private String priority;

  @Column(name = "NOT_UPDATE_CAL")
  private String notUpdateCal;

  @Column(name = "CREATED_DATE")
  private Date createdDate;

  @Column(name = "LAST_MODIFY_DATE")
  private Date lastModifyDate;

  @Step
  public static void insert(PriorityProgram entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void delete(String program, String campaignId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession()
      .createQuery("delete from PriorityProgram a where a.program = :program and a.campaignId = :campaignId");
    query.setParameter("program", program);
    query.setParameter("campaignId", campaignId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void clearAll() {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery("delete from PriorityProgram a where a.id is not null");
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }
}
