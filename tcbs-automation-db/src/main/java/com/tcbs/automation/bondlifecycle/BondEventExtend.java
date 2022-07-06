package com.tcbs.automation.bondlifecycle;

import lombok.*;
import net.thucydides.core.annotations.Step;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "BOND_EVENT_EXTEND")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BondEventExtend extends BaseEntity {

  @Id
  @Column(name = "ID")
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "EVENT_ID")
  private String eventEngineId;

  @Column(name = "EXTENDED_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date extendedDateDB;

  @Column(name = "NOTE")
  private String note;

  @Step
  public static void insert(BondEventExtend bondEventExtend) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.saveOrUpdate(bondEventExtend);
    trans.commit();
  }

  @Step
  public static void delete(List<Integer> ids) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query = session.createQuery("delete BondEventExtend a where a.id in :ids");
    query.setParameter("ids", ids);
    trans.commit();
  }

  @Step
  public static void deleteByEventEngineIds(List<String> eventEngineIds) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query = session.createQuery("delete BondEventExtend a where a.eventEngineId in :eventEngineIds");
    query.setParameter("eventEngineIds", eventEngineIds);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static BondEventExtend getBondEventExtendByEventEngineId(String eventEngineId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<BondEventExtend> query = session.createQuery("select a from BondEventExtend a where a.eventEngineId =:eventEngineId",
      BondEventExtend.class);
    query.setParameter("eventEngineId", eventEngineId);
    List<BondEventExtend> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results.get(0);
  }

  @Step
  public static BondEventExtend getLastestBondEventExtendByEventEngineId(String eventEngineId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<BondEventExtend> query = session.createQuery("select a from BondEventExtend a where a.eventEngineId =:eventEngineId order by a.extendedDateDB desc",
      BondEventExtend.class);
    query.setParameter("eventEngineId", eventEngineId);
    List<BondEventExtend> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results.get(0);
  }

  @Step
  public static String getLastBondEventExtendDate(String eventId) {
    Session session = BondLifeCycleConnection.CONNECTION.getSession();
    session.clear();
    Query<BondEventExtend> query = session.createNativeQuery("SELECT * FROM BOND_EVENT_EXTEND WHERE EVENT_ID = :eventId order by CREATED_AT desc", BondEventExtend.class);
    query.setParameter("eventId", eventId);
    List<BondEventExtend> bondEvenetExtends = query.getResultList();
    if (bondEvenetExtends != null && !bondEvenetExtends.isEmpty()) {
      return DateFormatUtils.format(bondEvenetExtends.get(0).getExtendedDateDB(), "yyyy-MM-dd");
    }
    return null;
  }

}
