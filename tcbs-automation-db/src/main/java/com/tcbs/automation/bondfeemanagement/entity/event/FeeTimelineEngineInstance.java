package com.tcbs.automation.bondfeemanagement.entity.event;

import com.tcbs.automation.bondfeemanagement.BondFeeManagementConnection;
import lombok.*;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.*;
import java.sql.Clob;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "FEE_TIMELINE_ENGINE_INSTANCE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeTimelineEngineInstance {
  @Id
  @Column(name = "ID", updatable = false, nullable = false)
  private Integer id;

  @Column(name = "BOND_ID")
  private Integer bondId;

  @Column(name = "FEE_ID")
  private Integer feeId;

  @Column(name = "FEE_TIMELINE_ID")
  private Integer feeTimelineId;

  @Column(name = "FEE_TIMELINE_TYPE")
  private String feeTimelineType;

  @Column(name = "TIMELINE_ENGINE_INSTANCE_ID")
  private String timelineEngineInstanceId;

  @Column(name = "TIMELINE_ENGINE_STATUS")
  private String timelineEngineStatus;

  @Column(name = "DATA")
  private Clob data;

  @Column(name = "START_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date startDateDB = null;

  @Column(name = "END_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date endDateDB = null;

  @Column(name = "BILL_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date billDateDB = null;

  public FeeTimelineEngineInstance(FeeTimelineEngineInstance feeTimelineEngineInstance) {
    super();
    this.id = feeTimelineEngineInstance.getId();
    this.bondId = feeTimelineEngineInstance.getBondId();
    this.feeId = feeTimelineEngineInstance.getFeeId();
    this.timelineEngineInstanceId = feeTimelineEngineInstance.getTimelineEngineInstanceId();
    this.timelineEngineStatus = feeTimelineEngineInstance.getTimelineEngineStatus();
    this.data = feeTimelineEngineInstance.getData();
  }

  @Step
  public static void truncateData() {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("truncate table FEE_TIMELINE_ENGINE_INSTANCE");
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void deleteById(Integer id) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("delete FEE_TIMELINE_ENGINE_INSTANCE where ID = :id");
    query.setParameter("id", id);
    query.executeUpdate();
    trans.commit();
  }


  @Step
  public static void deleteByFeeId(Integer feeId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("delete FEE_TIMELINE_ENGINE_INSTANCE where FEE_ID = :feeId");
    query.setParameter("feeId", feeId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void insert(FeeTimelineEngineInstance feeTimelineEngineInstance) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.saveOrUpdate(feeTimelineEngineInstance);
    trans.commit();
  }

  @Step
  public static FeeTimelineEngineInstance getById(Integer id) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<FeeTimelineEngineInstance> query = session.createNativeQuery("select * from FEE_TIMELINE_ENGINE_INSTANCE where ID = :id", FeeTimelineEngineInstance.class);
    query.setParameter("id", id);
    List<FeeTimelineEngineInstance> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results.get(0);
  }

  @Step
  public static List<FeeTimelineEngineInstance> getByFeeId(Integer feeId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<FeeTimelineEngineInstance> query = session.createNativeQuery("select * from FEE_TIMELINE_ENGINE_INSTANCE where FEE_ID = :feeId", FeeTimelineEngineInstance.class);
    query.setParameter("feeId", feeId);
    List<FeeTimelineEngineInstance> results = query.getResultList();

    if (results == null || results.isEmpty()) {
      return Collections.emptyList();
    }

    return results;
  }

  @Step
  public static FeeTimelineEngineInstance getByTimelineEngineInstanceId(String id) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<FeeTimelineEngineInstance> query = session.createNativeQuery("select * from FEE_TIMELINE_ENGINE_INSTANCE where TIMELINE_ENGINE_INSTANCE_ID = :id",
      FeeTimelineEngineInstance.class);
    query.setParameter("id", id);
    List<FeeTimelineEngineInstance> results = query.getResultList();

    if (results == null || results.isEmpty()) {
      return null;
    }

    return results.get(0);
  }

}
