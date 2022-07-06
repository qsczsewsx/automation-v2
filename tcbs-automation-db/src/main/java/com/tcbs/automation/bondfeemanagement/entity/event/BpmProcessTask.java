package com.tcbs.automation.bondfeemanagement.entity.event;

import com.tcbs.automation.bondfeemanagement.BondFeeManagementConnection;
import lombok.*;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "BPM_PROCESS_TASK")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BpmProcessTask {
  @Id
  @Column(name = "BPM_TASK_ID")
  private Integer taskId;

  @Column(name = "BPM_INSTANCE_ID")
  private String bpmInstanceId;

  @Column(name = "CASE_ID")
  private Integer caseId;

  @Column(name = "FEE_TYPE_LEVEL2_CODE")
  private String feeTypeLevel2Code;

  @Column(name = "BILL_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date billDateDB = null;

  @Column(name = "CREATED_AT", updatable = false, nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  private Date createdAtDate = null;

  @Column(name = "CLAIMED_AT")
  @Temporal(TemporalType.TIMESTAMP)
  private Date claimedAtDB = null;

  @Column(name = "CLAIMED_BY")
  private String claimedBy;

  @Step
  public static void deleteById(Integer taskId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BPM_PROCESS_TASK where BPM_TASK_ID = :taskId");
    query.setParameter("taskId", taskId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void insert(BpmProcessTask bpmProcessTask) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.saveOrUpdate(bpmProcessTask);
    trans.commit();
  }
}
