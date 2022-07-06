package com.tcbs.automation.bondlifecycle;

import lombok.*;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "BOND_TIMELINE_ENGINE_INSTANCE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BondTimelineEngineInstance {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID", updatable = false, nullable = false)
  private Integer id;

  @Column(name = "TIMELINE_ID")
  private Integer timelineId;

  @Column(name = "TIMELINE_ENGINE_INSTANCE_ID")
  private String timelineEngineInstanceId;

  @Column(name = "BOND_ID")
  private Integer bondId;

  @Column(name = "DATA")
  private String data;

  @Step
  public static BondTimelineEngineInstance getById(Integer timelineId, Integer bondId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<BondTimelineEngineInstance> query = session.createNativeQuery("select * from BOND_TIMELINE_ENGINE_INSTANCE where TIMELINE_ID = :timelineId and BOND_ID = :bondId",
      BondTimelineEngineInstance.class);
    query.setParameter("bondId", bondId);
    query.setParameter("timelineId", timelineId);
    List<BondTimelineEngineInstance> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }

    return results.get(0);
  }

  @Step
  public static void deleteById(Integer timelineId, Integer bondId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    Query<?> query = session.createNativeQuery("delete BOND_TIMELINE_ENGINE_INSTANCE where TIMELINE_ID = :timelineId and BOND_ID = :bondId");
    query.setParameter("bondId", bondId);
    query.setParameter("timelineId", timelineId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void deleteByTimelineId(Integer timelineId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();

    Query<?> query = session.createNativeQuery("delete BOND_TIMELINE_ENGINE_INSTANCE where TIMELINE_ID = :timelineId");
    query.setParameter("timelineId", timelineId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void insert(BondTimelineEngineInstance engineInstance) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.save(engineInstance);
    trans.commit();
  }

  @Step
  public void insert() {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.save(this);
    trans.commit();
  }
}
