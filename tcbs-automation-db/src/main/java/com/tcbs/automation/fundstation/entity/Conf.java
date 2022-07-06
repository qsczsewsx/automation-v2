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
@Table(name = "CONF")
public class Conf {
  public static Session session;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private int id;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "LABEL")
  private String label;

  @Column(name = "KEY")
  private String key;

  @Column(name = "VALUE")
  private String value;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;

  public static List<Conf> getAllConfig() {
    Query<Conf> query = session.createQuery("from Conf");
    return query.getResultList();
  }

  public static List<Conf> getConfigByLabel(String label) {
    Query<Conf> query = session.createQuery("from Conf where label =:label");
    query.setParameter("label", label);
    List<Conf> result = query.getResultList();
    return result;
  }
}
