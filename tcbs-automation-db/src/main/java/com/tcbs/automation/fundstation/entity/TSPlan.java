package com.tcbs.automation.fundstation.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.tcbs.automation.fundstation.entity.TSPlanDetail.deleteListPlanDetail;
import static com.tcbs.automation.fundstation.entity.TcAssets.sendSessionDBAssets;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TS_PLAN")
public class TSPlan {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "PLAN_CODE")
  private String planCode;

  @Column(name = "PLAN_NAME")
  private String planName;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "USER_NAME")
  private String username;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "CREATED_TIMESTAMP")
  @Temporal(TemporalType.DATE)
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  @Temporal(TemporalType.DATE)
  private Date updatedTimestamp;


  public static List<TSPlan> getListPlanNotDeleted(String status) {
    session.clear();
    Query<TSPlan> query = session.createQuery("from TSPlan where not status =:status order by planCode ASC");
    query.setParameter("status", status);

    List<TSPlan> result = query.getResultList();
    return result;
  }

  public static List<TSPlan> getListPlan(String status) {
    session.clear();
    Query<TSPlan> query = session.createQuery("from TSPlan where status =:status order by planCode ASC");
    query.setParameter("status", status);
    List<TSPlan> result = query.getResultList();
    return result;
  }

  public static List<TSPlan> getListPlanOfPlanCode(String planCode) {
    session.clear();
    Query<TSPlan> query = session.createQuery("from TSPlan where planCode =:planCode");
    query.setParameter("planCode", planCode);
    List<TSPlan> result = query.getResultList();
    return result;
  }

  public static TSPlan getPlanWithPlanCode(String planCode) {
    session.clear();
    Query<TSPlan> query = session.createQuery("from TSPlan where planCode =:planCode");
    query.setParameter("planCode", planCode);
    List<TSPlan> result = query.getResultList();
    if (result.size() != 0) {
      return result.get(0);
    } else {
      return null;
    }
  }

  public static TSPlan getPlanOfPlanId(Long id) {
    session.clear();
    Query<TSPlan> query = session.createQuery("from TSPlan where id =:id");
    query.setParameter("id", id);
    List<TSPlan> result = query.getResultList();
    if (result.size() != 0) {
      return result.get(0);
    } else {
      return null;
    }
  }

  public static List<TSPlan> getListPlanOfPlanId(Integer id) {
    session.clear();
    Query<TSPlan> query = session.createQuery("from TSPlan where id =:id");
    query.setParameter("id", id);
    List<TSPlan> result = query.getResultList();
    return result;
  }

  public static void deletePlanHasPlanCode(String planCode) {
    Session session2 = sendSessionDBAssets();
    int row = 0;
    Query<TSPlan> query = session2.createQuery("delete TSPlan where planCode =: planCode ");
    query.setParameter("planCode", planCode);
    row = query.executeUpdate();
    session2.getTransaction().commit();
  }

  public static void deleteActivePlan(String status) {
    Session session2 = sendSessionDBAssets();
    Query<TSPlan> query = session2.createQuery("delete TSPlan where status =: status ");
    query.setParameter("status", status);
    int row = query.executeUpdate();
    session.flush();
    session2.getTransaction().commit();

  }

  public static void deleteAllPlan() {
    Session session2 = sendSessionDBAssets();
    Query<TSPlan> query = session2.createQuery("delete TSPlan ");
    int row = query.executeUpdate();
    session2.flush();
    session2.getTransaction().commit();
  }

  public static void deleteListPlan(List<TSPlan> listPlan) {
    if (listPlan.size() > 0) {
      List<String> listCode = listPlan.stream().map(TSPlan::getPlanCode).collect(Collectors.toList());
      List<Integer> listId = listPlan.stream().map(TSPlan::getId).collect(Collectors.toList());
      deleteListPlanDetail(listId);

      Query<TSPlan> query = session.createQuery("delete TSPlan where planCode in :listCode");
      query.setParameter("listCode", listCode);
      query.executeUpdate();
    }
  }

  public static void insertIntoTsPlan(String planCode, String planName, String description, String username, String status) {
    Session session2 = sendSessionDBAssets();
    TSPlan tsPlan = new TSPlan();
    tsPlan.setPlanCode(planCode);
    tsPlan.setPlanName(planName);
    tsPlan.setDescription(description);
    tsPlan.setUsername(username);
    tsPlan.setStatus(status);
    session2.save(tsPlan);
    session2.getTransaction().commit();
  }
}
