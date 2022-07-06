package com.tcbs.automation.fundstation.entity;

import lombok.Data;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.fundstation.entity.TcAssets.sendSessionDBAssets;

@Data
@Entity
@Table(name = "TS_PLAN_DETAIL")
public class TSPlanDetail {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "PLAN_ID")
  private Integer planId;

  @Column(name = "PRODUCT_CODE")
  private String productCode;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  public static List<TSPlanDetail> getListBondTempByPlanId(Integer planId) {
    Query<TSPlanDetail> query = session.createQuery("from TSPlanDetail where planId =:planId");
    query.setParameter("planId", planId);
    return query.getResultList();
  }

  public static void deleteListPlanDetail(List<Integer> listPlanId) {
    Session session2 = sendSessionDBAssets();
    if (!listPlanId.isEmpty()) {
      Query<TSPlanDetail> query = session2.createQuery("delete TSPlanDetail where planId  in :listPlanId");
      query.setParameter("listPlanId", listPlanId);
      query.executeUpdate();
      session2.getTransaction().commit();
    }
  }

  public static void insertPlanDetail(Integer planId, String productCode, String status) {
    Session session2 = sendSessionDBAssets();
    TSPlanDetail tsPlanDetail = new TSPlanDetail();
    tsPlanDetail.setPlanId(planId);
    tsPlanDetail.setProductCode(productCode);
    tsPlanDetail.setStatus(status);
    session2.save(tsPlanDetail);
    session2.getTransaction().commit();
  }

  public static void deletePlanDetail(String productCode) {
    Session session2 = sendSessionDBAssets();
    Query<TSPlanDetail> query = session2.createQuery("delete TSPlanDetail where productCode =:productCode ");
    query.setParameter("productCode", productCode);
    int row = query.executeUpdate();
    session2.getTransaction().commit();
  }

  public static List<TSPlanDetail> getTSPlanDetailWithPlanId(Integer planId, String status) {
    session.clear();
    Query<TSPlanDetail> query = session.createQuery("from TSPlanDetail where planId =:planId and status=:status");
    query.setParameter("planId", planId);
    query.setParameter("status", status);
    List<TSPlanDetail> result = query.getResultList();
    return result;
  }
}
