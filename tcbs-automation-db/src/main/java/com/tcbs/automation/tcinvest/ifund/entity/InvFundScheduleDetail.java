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
@Entity
@Getter
@Setter
@NoArgsConstructor
@SuppressWarnings("unchecked")
@Table(name = "INV_FUND_SCHEDULE_DETAIL")
public class InvFundScheduleDetail {
  public static Session session;
  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;
  @Column(name = "SCHEDULE_ID")
  private String scheduleId;
  @Column(name = "ORDER_ID")
  private String orderId;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "GEN_DATE")
  private Date genDate;
  @Column(name = "PLAN_DATE")
  private Date planDate;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "ACTOR")
  private String actor;
  @Column(name = "CREATE_TIME")
  private Date createTime;
  @Column(name = "UPDATE_TIME")
  private Date updateTime;
  @Column(name = "DELETED")
  private String delete;
  @Column(name = "NEXT_TRADING_DATE")
  private Date nextTradingDate;

  public static InvFundScheduleDetail getInvFundScheduleDetailByScheduleId(String scheduleId) {
    Query<InvFundScheduleDetail> query = session.createQuery("from InvFundScheduleDetail where scheduleId=:scheduleId");
    query.setParameter("scheduleId", scheduleId);
    List<InvFundScheduleDetail> result = query.getResultList();
    if (!result.isEmpty()) {
      return result.get(0);
    } else {
      return null;
    }
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
