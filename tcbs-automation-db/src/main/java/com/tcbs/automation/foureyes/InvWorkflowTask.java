package com.tcbs.automation.foureyes;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "INV_WORKFLOW_TASK")
public class InvWorkflowTask {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "TASK_ID")
  private String taskId;
  @Column(name = "TASK_TEMPLATE_ID")
  private String taskTemplateId;
  @Column(name = "WORKFLOW_ID")
  private String workflowId;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "CREATED_DATE")
  private Date createdDate;
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;

  public static void deleteInvWorkflowTaskById(String workflowId) {
    Session session = FourEyes.foureyesConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvWorkflowTask> query = session.createQuery(
      "DELETE FROM InvWorkflowTask iw WHERE iw.workflowId =: workflowId"
    );
    query.setParameter("workflowId", workflowId);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
