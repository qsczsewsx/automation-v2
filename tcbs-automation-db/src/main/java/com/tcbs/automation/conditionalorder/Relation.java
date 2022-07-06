package com.tcbs.automation.conditionalorder;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "RELATION")
public class Relation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @NotNull
  @Column(name = "PARENT_ID")
  private String parentId;
  @NotNull
  @Column(name = "CHILD_ID")
  private String childId;
  @NotNull
  @Column(name = "CREATED_ON")
  private String createdOn;

  public static List<Relation> getByParentId(String parentId) {
    Session session = TheConditionalOrder.anattaDbConnection.getSession();
    session.clear();
    Query<Relation> query = session.createQuery("from Relation ib where ib.parentId =:parentId and ib.childId != :parentId");
    query.setParameter("parentId", parentId);
    List<Relation> result = query.getResultList();
    return result;
  }
}
