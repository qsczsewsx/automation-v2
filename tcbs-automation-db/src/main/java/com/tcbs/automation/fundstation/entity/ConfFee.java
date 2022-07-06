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
@Table(name = "CONF_FEE")
public class ConfFee {
  public static Session session;
  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "PORTFOLIO")
  private String portfolio;

  @Column(name = "FEE_ID")
  private Integer feeId;

  @Column(name = "VALUE")
  private Double value;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  public static List<ConfFee> getListFeeByPortfolioActive(String portfolio) {
    Query<ConfFee> query = session.createQuery("from ConfFee where portfolio =:portfolio and status = 'ACTIVE' order by id desc ");
    query.setParameter("portfolio", portfolio);
    return query.getResultList();
  }

  public static ConfFee getFeeLatest() {
    session.clear();
    Query<ConfFee> query = session.createQuery("from ConfFee order by id desc ").setFirstResult(0);
    return query.getResultList().get(0);
  }

  public static ConfFee getFeeById(Integer id) {
    Query<ConfFee> query = session.createQuery("from ConfFee where id =:id");
    query.setParameter("id", id);
    List<ConfFee> listFee = query.getResultList();
    if (listFee.size() > 0) {
      return listFee.get(0);
    }
    return null;
  }

  public static void deleteListFeeId(List<Integer> listId) {
    if (!listId.isEmpty()) {
      Query<ConfFee> query = session.createQuery("delete ConfFee where id in :listId");
      query.setParameter("listId", listId);
      query.executeUpdate();
    }
  }
}
