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
@Table(name = "INV_WORKFLOW")
public class InvWorkflow {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "WORKFLOW_ID")
  private String workflowId;
  @Column(name = "WORKFLOW_TEMPLATE_ID")
  private String workflowTemplateId;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "CREATED_BY")
  private String createdBy;
  @Column(name = "APPROVER")
  private String approver;
  @Column(name = "OWNER")
  private String owner;
  @Column(name = "ACTION")
  private String action;
  @Column(name = "CATEGORY")
  private String category;
  @Column(name = "SERVICE_NAME")
  private String serviceName;
  @Column(name = "SERVICE_SUB_PATH")
  private String serviceSubPath;
  @Column(name = "REASON")
  private String reason;
  @Column(name = "HASH_ID")
  private String hashId;
  @Column(name = "PAYLOAD_IDENTIFIER")
  private String payloadIdentifier;
  @Column(name = "PAYLOAD")
  private String payload;
  @Column(name = "PREVIOUS_PAYLOAD")
  private String previousPayload;
  @Column(name = "CREATED_DATE")
  private Date createdDate;
  @Column(name = "UPDATED_DATE")
  private Date updatedDate;

  public static void deleteInvWorkflowById(String workflowId) {
    Session session = FourEyes.foureyesConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<InvWorkflow> query = session.createQuery(
      "DELETE FROM InvWorkflow iw WHERE iw.workflowId =: workflowId"
    );
    query.setParameter("workflowId", workflowId);
    query.executeUpdate();
    session.getTransaction().commit();
  }
}
