package com.tcbs.automation.silkgate;

import com.tcbs.automation.hth.CollectionTrans;
import com.tcbs.automation.hth.HthDb;
import lombok.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static com.tcbs.automation.silkgate.SilkgateDb.silkgateDbConnection;

@Entity
@Table(name = "CONFIG")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Config {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "CREATED_TIME")
  private Timestamp createdTime;
  @Column(name = "UPDATED_TIME")
  private Timestamp updatedTime;

  public static List<Config> getListConfig(String type, String label, String key) {
    Query<Config> query;
    query = silkgateDbConnection.getSession().createQuery(

            "FROM Config WHERE id is not null " +
                    (StringUtils.isNotEmpty(type) ? " and type =:type " : "") +
                    (StringUtils.isNotEmpty(label) ? " and label =:label " : "") +
                    (StringUtils.isNotEmpty(key) ? " and key =:key " : "") +
                    " ORDER BY id ASC",
            Config.class
    );
    setParameter(query, type, label, key);
    return query.getResultList();
  }

  private static void setParameter(Query query, String type, String label, String key) {

    if (StringUtils.isNotEmpty(type)) {
      query.setParameter("type", type);
    }
    if (StringUtils.isNotEmpty(label)) {
      query.setParameter("label", label);
    }
    if (StringUtils.isNotEmpty(key)) {
      query.setParameter("key", key);
    }
  }

  public static void addNewConfig(String type, String label, String key, String value,String description) {
    Session session = silkgateDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    String sql = "INSERT INTO CONFIG (ID, TYPE, LABEL, KEY, VALUE , DESCRIPTION,CREATED_TIME,UPDATED_TIME) VALUES (CONFIG_ID_SEQUENCE.nextval, ?, ?, ?, ?, ?, sysdate, sysdate)";

    Query<?> query = session.createNativeQuery(sql);
    query.setParameter(1, type);
    query.setParameter(2, label);
    query.setParameter(3, key);
    query.setParameter(4, value);
    query.setParameter(5, description);

    query.executeUpdate();
    trans.commit();
  }

  public static Config getByMaxId() {
    silkgateDbConnection.getSession().clear();
    Query<Config> query = silkgateDbConnection.getSession().createQuery(
            " FROM Config ORDER BY id DESC",
            Config.class
    );
    query.setMaxResults(1).setFirstResult(0);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  public static Config getByUpdatedTime() {
    silkgateDbConnection.getSession().clear();
    Query<Config> query = silkgateDbConnection.getSession().createQuery(
            " FROM Config ORDER BY updatedTime DESC",
            Config.class
    );
    query.setMaxResults(1).setFirstResult(0);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return null;
    }
  }

  public static void deleteLatestData() {
    Session session = silkgateDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query = session.createNativeQuery(" DELETE FROM CONFIG WHERE ID = (select max(ID) from CONFIG) ");

    query.executeUpdate();
    trans.commit();
  }

}