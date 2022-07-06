package com.tcbs.automation.ixu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "JOB_HISTORY")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class JobHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "JOB_NAME")
  private String jobName;
  @Column(name = "LAST_UPDATED_DATE")
  private ZonedDateTime lastUpdatedDate;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "CREATED_DATE")
  private ZonedDateTime createdDate;
  @Column(name = "ISSUE_DATE")
  private ZonedDateTime issueDate;
  @Column(name = "DETAIL")
  private String detail;

  @Step
  public static List<JobHistory> findJobHistoryByJobName(String jobName, ZonedDateTime issueDate) {
    ixuDbConnection.getSession().clear();
    Query<JobHistory> query = ixuDbConnection.getSession().createQuery(
      "from JobHistory a where a.jobName=:jobName and a.issueDate=:issueDate ");
    query.setParameter("jobName", jobName);
    query.setParameter("issueDate", issueDate);
    List<JobHistory> jobHistoryList = query.getResultList();
    ixuDbConnection.closeSession();
    return jobHistoryList;
  }

  @Step
  public static void updateJobHistory(ZonedDateTime dateTime, String jobName) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "update JobHistory a set a.issueDate=:issueDate where a.jobName=:jobName");
    query.setParameter("issueDate", dateTime);
    query.setParameter("jobName", jobName);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteJobHistory(String jobName) {
    ixuDbConnection.getSession().clear();
    if (!ixuDbConnection.getSession().getTransaction().isActive()) {
      ixuDbConnection.getSession().beginTransaction();
    }

    Query query = ixuDbConnection.getSession().createQuery(
      "delete JobHistory a where a.jobName=:jobName");
    query.setParameter("jobName", jobName);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void insertJobHistory(JobHistory jobHistory) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    ixuDbConnection.getSession().save(jobHistory);

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static JobHistory getJobHistoryByNameAndIssueDate(String jobName, String issueDate) {
    ixuDbConnection.getSession().clear();
    Query query = ixuDbConnection.getSession().createQuery(
      "from JobHistory a where a.jobName=:jobName and trunc(a.issueDate) = to_date( :issueDate, 'yyyy-MM-dd') ");
    query.setParameter("jobName", jobName);
    query.setParameter("issueDate", issueDate);
    List<JobHistory> jobHistoryList = query.getResultList();
    ixuDbConnection.closeSession();
    return jobHistoryList.get(0);
  }

}
