package com.tcbs.automation.fundstation.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Data
@Table(name = "TS_CHANNEL_ACCOUNT")

public class ChannelAccount {

  public static Session session;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)

  @Column(name = "ID")
  private Integer id;

  @Column(name = "CODE")
  private String code;

  @Column(name = "NAME")
  private String name;

  @Column(name = "STATUS")
  private String status;

  @Column(name = "CHANNEL_ID")
  private Integer channelId;

  @Column(name = "CREATED_TIMESTAMP")
  @Temporal(TemporalType.DATE)
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  @Temporal(TemporalType.DATE)
  private Date updatedTimestamp;

  public ChannelAccount(Integer id, String code, String name, String status, Integer channelId, Date createdTimestamp, Date updatedTimestamp) {
    this.id = id;
    this.code = code;
    this.name = name;
    this.status = status;
    this.channelId = channelId;
    this.createdTimestamp = createdTimestamp;
    this.updatedTimestamp = updatedTimestamp;

  }

  public static List<ChannelAccount> getListChannelAcountWithChannelId(Integer channelId) {
    session.clear();
    Query<ChannelAccount> query = session.createQuery("from ChannelAccount where channelId =:channelId");
    query.setParameter("channelId", channelId);
    List<ChannelAccount> result = query.getResultList();
    return result;
  }

  public static List<ChannelAccount> getListChannelAcountWithChannelIdAndNotDeleted(Integer channelId, String status) {
    session.clear();
    Query<ChannelAccount> query = session.createQuery("from ChannelAccount where channelId =:channelId and not status =:status");
    query.setParameter("channelId", channelId);
    query.setParameter("status", status);
    List<ChannelAccount> result = query.getResultList();
    return result;
  }

  public static List<ChannelAccount> getListChannelAcountWithChannelCode(String code) {
    session.clear();
    Query<ChannelAccount> query = session.createQuery("from ChannelAccount where code =:code");
    query.setParameter("code", code);
    List<ChannelAccount> result = query.getResultList();
    return result;
  }

  public static List<ChannelAccount> getListgetActiveChannelAccount(String status) {
    session.clear();
    Query<ChannelAccount> query = session.createQuery("from ChannelAccount where status =:status");
    query.setParameter("status", status);
    List<ChannelAccount> result = query.getResultList();
    return result;
  }

  public static List<ChannelAccount> getListgetChannelAccountActiveAndChannelIdNull(String status) {
    session.clear();
    Query<ChannelAccount> query = session.createQuery("from ChannelAccount where status =:status and  channelId is null");
    query.setParameter("status", status);

    List<ChannelAccount> queryResult = query.getResultList();

    return queryResult;
  }

  public static List<Integer> getListgetChannelAccountToAddToChannel(String status, Integer channelId) {
    session.clear();
    Query<ChannelAccount> query = session.createQuery("from ChannelAccount where status =:status and  channelId is null");
    query.setParameter("status", status);

    List<ChannelAccount> queryResult = query.getResultList();
    List<Integer> channelAccountIdList = new ArrayList<>();
    for (ChannelAccount channelAccount : queryResult) {
      channelAccountIdList.add(channelAccount.getId());
    }
    return channelAccountIdList;
  }

  public static void deleteListChannelAccount(List<String> listCode) {

    Query<Portfolio> query = session.createQuery("from ChannelAccount where code in :listCode");

    query.setParameter("listCode", listCode);
    int row = query.executeUpdate();
    session.flush();
    session.getTransaction().commit();
    System.out.println(row);
  }

  public static void deleteChannelAccountWithCode(String code) {
    session.clear();
    Query<ChannelAccount> query = session.createQuery("delete  ChannelAccount where code = :code");
    query.setParameter("code", code);
    int row = query.executeUpdate();
    System.out.println(row);

  }

  public static void insertIntoChannelAccount(ChannelAccount channelAccount) {
    String code = channelAccount.getCode();
    String name = channelAccount.getName();
    String status = channelAccount.getStatus();
    Query query = session.createSQLQuery(String.format("INSERT INTO TS_CHANNEL_ACCOUNT (CODE, NAME, STATUS) VALUES ('%s','%s','%s)", code, name, status));
    session.save(channelAccount);

  }

  public static List<ChannelAccount> getChannelAccountWithId(List<Integer> listId) {
    session.clear();
    Query<ChannelAccount> query = session.createQuery("from  ChannelAccount where id in :listId");
    query.setParameter("listId", listId);
    List<ChannelAccount> result = query.getResultList();
    return result;
  }


}
