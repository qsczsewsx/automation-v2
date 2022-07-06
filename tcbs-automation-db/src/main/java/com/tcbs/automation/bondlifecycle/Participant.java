package com.tcbs.automation.bondlifecycle;


import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "PARTICIPANT")
public class Participant extends ApprovalEntity {
  @Id
  @Column(name = "ID", updatable = false)
  private Integer id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "NAME_NO_ACCENT")
  private String nameNoAccent;

  @Step
  public static Participant getParticipantInfoById(Integer id) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<Participant> query = session.createNativeQuery("select * from PARTICIPANT where  id =:participantId", Participant.class);
    query.setParameter("participantId", id);
    List<Participant> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results.get(0);
  }

  @Step
  public static void deleteParticipantById(Integer id) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    Query<?> query = session.createNativeQuery("delete PARTICIPANT a where a.id =:participantId");
    query.setParameter("participantId", id);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void deleteParticipantByName(String name) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    Query<?> query = session.createNativeQuery("delete PARTICIPANT a where a.name =:name");
    query.setParameter("name", name);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static List<Participant> findAllByKey(String findKey) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<Participant> query = session.createNativeQuery(
      "SELECT c.* FROM PARTICIPANT c LEFT JOIN REFERENCE_DATA ref on c.APPROVAL_STATUS  = ref.CODE  WHERE c.STATUS <> 'INACTIVE' and (UPPER(c.NAME_NO_ACCENT) like '%' || :findKey || '%' or UPPER(ref.NAME_NO_ACCENT) like '%' || :findKey || '%')",
      Participant.class);
    query.setParameter("findKey", findKey);
    return query.getResultList();
  }

  @Step
  public static List<Participant> findAllActive() {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<Participant> query = session.createNativeQuery("SELECT * FROM PARTICIPANT WHERE STATUS = 'ACTIVE'", Participant.class);
    return query.getResultList();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getNameNoAccent() {
    return nameNoAccent;
  }

  public void setNameNoAccent(String nameNoAccent) {
    this.nameNoAccent = nameNoAccent;
  }

  @Step
  public void insert() {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.saveOrUpdate(this);
    trans.commit();
  }
}
