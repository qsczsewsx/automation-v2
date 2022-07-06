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
@Table(name = "TS_CHANNEL")
public class Channel {
  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "CODE")
  private String code;

  @Column(name = "NAME")
  private String name;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "USER_NAME")
  private String username;

  @Column(name = "CREATED_TIMESTAMP")
  @Temporal(TemporalType.DATE)
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  @Temporal(TemporalType.DATE)
  private Date updatedTimestamp;


  public static List<Channel> getChannelNotDeleteWithCode(String code, String status) {
    session.clear();
    Query<Channel> query = session.createQuery("from Channel where code =:code and not status =:status");
    query.setParameter("code", code);
    query.setParameter("status", status);
    List<Channel> result = query.getResultList();
    return result;
  }

  public static List<Channel> getChannel_WithCode(String code) {
    session.clear();
    Query<Channel> query = session.createQuery("from Channel where code =:code ");
    query.setParameter("code", code);

    List<Channel> result = query.getResultList();
    return result;
  }

  public static List<Channel> getAllChannel_ASC(String softField) {
    session.clear();
    Query<Channel> query = session.createQuery("from Channel ORDER BY :softField ASC");
    query.setParameter("softField", softField);
    List<Channel> result = query.getResultList();
    return result;
  }

  public static List<Channel> getAllChannel_DESC(String softField) {
    session.clear();
    Query<Channel> query = session.createQuery("from Channel ORDER BY :softField DESC ");
    query.setParameter("softField", softField);
    List<Channel> result = query.getResultList();
    return result;
  }

  public static List<Channel> getListChannelWithNotStatus(String status) {
    session.clear();
    Query<Channel> query = session.createQuery("from Channel where not status =:status");
    query.setParameter("status", status);
    List<Channel> result = query.getResultList();
    return result;
  }

  public static void deleteChannelWithChannelCode(String code) {
    Query<Channel> query = session.createQuery("delete  Channel where code = :code");
    query.setParameter("code", code);
    int row = query.executeUpdate();
    System.out.println(row);
  }
}
