package com.tcbs.automation.bondfeemanagement.entity.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tcbs.automation.bondfeemanagement.BondFeeManagementConnection;
import lombok.*;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "FEE_EVENT_EXTEND")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeEventExtend {

  @Id
  @Column(name = "ID", nullable = false)
  private Integer id;

  @Column(name = "EVENT_ENGINE_INSTANCE_ID")
  private String eventEngineInstanceId;

  @Column(name = "EXTENDED_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date extendedDateDB;

  @Column(name = "NOTE")
  private String note;

  @Column(name = "CREATED_AT", updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  @CreationTimestamp
  @JsonIgnore
  private Date createdAtDate = null;

  @Column(name = "CREATED_BY", updatable = false)
  private String createdBy = null;

  @Step
  public static void deleteByFeeEventId(String eventEngineId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("delete FEE_EVENT_EXTEND where EVENT_ENGINE_INSTANCE_ID = :id");
    query.setParameter("id", eventEngineId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void deleteById(Integer id) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("delete FEE_EVENT_EXTEND where ID = :id");
    query.setParameter("id", id);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void insert(FeeEventExtend feeEventExtend) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.saveOrUpdate(feeEventExtend);
    trans.commit();
  }
}
