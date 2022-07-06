package com.tcbs.automation.conditionalorder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "CENTRAL_LOG")
public class CentralLog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @NotNull
  @Column(name = "OBJECT_ID")
  private String objectId;
  @NotNull
  @Column(name = "OBJECT_TYPE")
  private String objectType;
  @NotNull
  @Column(name = "ACTION")
  private String action;
  @NotNull
  @Column(name = "ACTOR")
  private String actor;
  @Column(name = "BEFORE_ACTION")
  private String beforeAction;
  @Column(name = "AFTER_ACTION")
  private String afterAction;
  @Column(name = "TRANS_ID")
  private String transId;
  @NotNull
  @Column(name = "CREATED_AT")
  private String createdAt;
  @Column(name = "DESCRIPTION")
  private String description;

  @Step
  public static CentralLog getTransIdByObjectId(String objectId) {
    Query<CentralLog> query = TheConditionalOrder.aniccaDbConnection.getSession()
      .createQuery("from CentralLog a where a.objectId=:objectId order by createdAt asc", CentralLog.class);
    query.setParameter("objectId", objectId);

//    return query.getSingleResult();
    return query.list().get(query.list().size() - 1);
  }

  @Step
  public List<CentralLog> getRecordsByTransId(String transId) {
    Query<CentralLog> query = TheConditionalOrder.aniccaDbConnection.getSession()
      .createQuery("from CentralLog a where a.transId=:transId", CentralLog.class);
    query.setParameter("transId", transId);

    List<CentralLog> results = query.getResultList();
    if (results.size() > 0) {
      return results;
    } else {
      return null;
    }
  }

}
