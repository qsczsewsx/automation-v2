package com.tcbs.automation.hth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "CONF")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Conf {
  @Id
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "TYPE")
  private String type;
  @Column(name = "LABEL")
  private String label;
  @Column(name = "KEY")
  private String key;
  @Column(name = "VALUE")
  private String value;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;

  public static void updateValueByLabelAndKey(String label, String key, String value) {
    Session session = HthDb.h2hConnection.getSession();
    session.beginTransaction();
    Query query = session.createQuery(
      "update Conf set value=:value where label=:label and key=:key"
    );
    query.setParameter("value", value)
      .setParameter("label", label)
      .setParameter("key", key)
      .executeUpdate();
    session.getTransaction().commit();
  }

  public static Conf getByLabelAndKey(String label, String key) {
    Session session = HthDb.h2hConnection.getSession();
    org.hibernate.query.Query<Conf> query = session.createQuery(
      "from Conf where label=:label and key=:key",
      Conf.class);
    try {
      return query
        .setParameter("label", label)
        .setParameter("key", key)
        .getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  public static void deleteDataByLabelAndKey(String label, String key) {
    Session session = HthDb.h2hConnection.getSession();
    session.beginTransaction();
    Query query = session.createQuery(
      "delete Conf where label=:label and key=:key"
    );
    query
      .setParameter("label", label)
      .setParameter("key", key)
      .executeUpdate();
    session.getTransaction().commit();
  }
}
