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
@Table(name = "UNDERLYING_DETAIL")
public class UnderlyingDetail {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private int id;

  @Column(name = "UNDERLYING_ID")
  private Integer underlyingId;

  @Column(name = "ISSUE_DATE")
  private Date issueDate;

  @Column(name = "VALUE")
  private Double value;

  @Column(name = "STATUS")
  private String status;

  public static List<UnderlyingDetail> underlyingDetails(Integer underlyingId) {
    Query<UnderlyingDetail> query = session.createQuery("from UnderlyingDetail where underlyingId =:underlyingId and status = 'ACTIVE' order by issueDate desc");
    query.setParameter("underlyingId", underlyingId);
    return query.getResultList();
  }

  public static List<UnderlyingDetail> getUnderlyingWithUnderlyingId(Integer underlyingId) {
    Query<UnderlyingDetail> query = session.createQuery("from UnderlyingDetail where not status ='DELETED' and underlyingId =: underlyingId order by issueDate ASC");
    query.setParameter("underlyingId", underlyingId);
    return query.getResultList();
  }

  public static List<UnderlyingDetail> getActiveUnderlyingWithUnderlyingId(Integer underlyingId) {
    Query<UnderlyingDetail> query = session.createQuery("from UnderlyingDetail where  status ='ACTIVE' and underlyingId =: underlyingId order by issueDate ASC");
    query.setParameter("underlyingId", underlyingId);
    return query.getResultList();
  }

  public static UnderlyingDetail getUnderlyingWithUnderlyingIdAndIssueDate(String code, Date issueDate) {
    session.clear();
    Query<UnderlyingDetail> query = session.createQuery("from Underlying where code =: code and issueDate =:issueDate");
    query.setParameter("code", code);
    query.setParameter("issueDate", issueDate);
    List<UnderlyingDetail> list = query.getResultList();
    if (list.size() != 0) {
      return list.get(0);
    } else {
      return null;
    }
  }
}
