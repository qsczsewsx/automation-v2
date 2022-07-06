package com.tcbs.automation.tcinvest.ifund.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("unchecked")
@Entity
@Table(name = "INV_FUND_SCHEDULE")
public class InvFundSchedule {
  public static Session session;
  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;
  @Column(name = "OBJECT_TYPE")
  private String objectType;

  @Column(name = "OBJECT_ID")
  private String objectId;

  @Column(name = "PERIOD")
  private String period;

  @Column(name = "START_DATE")
  private Date startDate;

  @Column(name = "END_DATE")
  private Date endDate;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "ACTION_ID")
  private String actionId;

  @Column(name = "PRODUCT_CODE")
  private String productCode;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "ACTOR")
  private String actor;

  @Column(name = "CREATE_TIME")
  private Date creatTime;

  @Column(name = "UPDATE_TIME")
  private Date updateTime;

  @Column(name = "DELETED")
  private String delete;

  @Column(name = "UPDATE_STATUS")
  private String updateStatus;

  public static InvFundSchedule getInvFundScheduleByObjectID(String objectId) {
    Query<InvFundSchedule> query = session.createQuery("from InvFundSchedule where objectId =:objectId");
    query.setParameter("objectId", objectId);
    List<InvFundSchedule> result = query.getResultList();
    if (!result.isEmpty()) {
      return result.get(0);
    } else {
      return null;
    }
  }

  public static void deleteByObjectId(String objectId) {
    startTransaction();
    Query<InvFundSchedule> query = session.createQuery("DELETE InvFundSchedule WHERE objectId =: objectId");
    query.setParameter("objectId", objectId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void startTransaction() {
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
  }

  public String insert() {
    startTransaction();
    session.save(this);
    session.getTransaction().commit();
    return session.save(this).toString();
  }

}
