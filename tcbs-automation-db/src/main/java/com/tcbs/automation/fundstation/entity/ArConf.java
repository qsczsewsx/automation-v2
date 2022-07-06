package com.tcbs.automation.fundstation.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "AR_CONF")
public class ArConf {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private int id;

  @Column(name = "VALUE")
  private String value;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  public static List<ArConf> getActiveConF() {
    Query<ArConf> query = session.createQuery("from ArConf where status = 'ACTIVE' ");
    return query.getResultList();
  }

}