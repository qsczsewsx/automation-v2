package com.tcbs.automation.fundstation.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "FEE")
public class Fee {
  public static Session session;
  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "TYPE")
  private Integer type;

  @Column(name = "CALCULATE_BY")
  private Integer calculateBy;

  @Column(name = "PERIOD")
  private String period;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  public static List<Fee> getAllFeeByFromDb(List<String> listStatus, String orderFiled, String orderBy) {
    StringBuilder sql = new StringBuilder("from Fee where status in :listStatus order by ").append(orderFiled).append(" ").append(orderBy).append(" , id ").append(orderBy);
    Query<Fee> query = session.createQuery(sql.toString());
    query.setParameter("listStatus", listStatus);
    return query.getResultList();
  }

  public static List<Fee> getAllFeeFromDb() {
    session.clear();
    Query<Fee> query = session.createQuery("from Fee order by id desc ");
    return query.getResultList();
  }

  public static Fee getFeeLatest() {
    session.clear();
    Query<Fee> query = session.createQuery("from Fee order by id desc ").setFirstResult(0);
    return query.getResultList().get(0);
  }

  public static Fee getFeeById(Integer id) {
    Query<Fee> query = session.createQuery("from Fee where id =:id");
    query.setParameter("id", id);
    List<Fee> listFee = query.getResultList();
    if (listFee.size() > 0) {
      return listFee.get(0);
    }
    return new Fee();
  }

  public static void deleteListFeeId(List<Integer> listId) {
    if (!listId.isEmpty()) {
      Query<Fee> query = session.createQuery("delete Fee where id in :listId");
      query.setParameter("listId", listId);
      query.executeUpdate();
    }
  }
}
