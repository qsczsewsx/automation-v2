package com.tcbs.automation.fundstation.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.fundstation.entity.TcAssets.sendSessionDBAssets;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "TS_CHANNEL")
public class TSChannel {
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

  @Column(name = "WARE_HOUSE")
  private Integer wareHouse;
  @Column(name = "FAVORITE")
  private Integer favorite;

  @Column(name = "DESCRIPTION")
  private String description;

  @Column(name = "USER_NAME")
  private String userName;

  @Column(name = "DEPART_ID")
  private Integer departId;

  @Column(name = "PARENT_ID")
  private Integer parentId;

  @Column(name = "TYPE")
  private String type;

  @Column(name = "CREATED_TIMESTAMP")
  private Date createdTimestamp;

  @Column(name = "UPDATED_TIMESTAMP")
  private Date updatedTimestamp;


  public static List<TSChannel> getChannelWithCode(String code) {
    session.clear();
    Query<TSChannel> query = session.createQuery("from TSChannel where code =:code");
    query.setParameter("code", code);
    return query.getResultList();
  }

  public static TSChannel getChannelWithId(Integer id) {
    session.clear();
    Query<TSChannel> query = session.createQuery("from TSChannel where id =:id");
    query.setParameter("id", id);
    List<TSChannel> list = query.getResultList();
    TSChannel result = null;
    if (list.size() != 0) {
      result = list.get(0);
    }
    return result;
  }

  public static List<TSChannel> getChannelNotDeleted(String status) {
    session.clear();
    Query<TSChannel> query = session.createQuery("from TSChannel where not status =:status");
    query.setParameter("status", status);
    return query.getResultList();
  }

  public static List<TSChannel> getChannelNotDeletedByType(String type, String name, String code) {
    session.clear();
    Query<TSChannel> query = session.createQuery("from TSChannel where type =:type and code like :code and name like :name and not status ='DELETED' order by id");
    query.setParameter("type", type);
    query.setParameter("code", "%" + code + "%");
    query.setParameter("name", "%" + name + "%");
    return query.getResultList();
  }

  public static void deleteChannelWithCode(String code) {
    Session session2 = sendSessionDBAssets();

    Query<TSChannel> query = session2.createQuery("delete TSChannel where code =:code");
    query.setParameter("code", code);
    query.executeUpdate();
    session2.getTransaction().commit();
  }

  public static List<TSChannel> getChannelLevel1NotDeleted(String status) {
    session.clear();
    Query<TSChannel> query = session.createQuery("from TSChannel where not status =:status and itemLevel =1");
    query.setParameter("status", status);
    return query.getResultList();
  }

  public static List<TSChannel> getChannelLevel1WithStatusAndFavorite(String status, Integer favorite, String type) {
    session.clear();
    Query<TSChannel> query = session.createQuery("from TSChannel where status =:status and favorite =:favorite and type =:type and itemLevel=1");
    query.setParameter("favorite", favorite);
    query.setParameter("status", status);
    query.setParameter("type", type);
    return query.getResultList();
  }

  public static List<TSChannel> getListChannelWithWareHouse(String status, Integer wareHouse, String type) {
    session.clear();
    Query<TSChannel> query = session.createQuery("from TSChannel where status =:status and wareHouse =:wareHouse and type =:type ");
    query.setParameter("wareHouse", wareHouse);
    query.setParameter("status", status);
    query.setParameter("type", type);
    return query.getResultList();
  }

  public static List<TSChannel> getChannelLevel1WithStatusAndFavoriteLikeChannelCode(String status, Integer favorite, String type, String code) {
    session.clear();
    Query<TSChannel> query = session.createQuery("from TSChannel where status =:status and favorite =:favorite and type =:type and code like :code and itemLevel=1");
    query.setParameter("favorite", favorite);
    query.setParameter("status", status);
    query.setParameter("type", type);
    query.setParameter("code", code + "%");
    return query.getResultList();
  }

  public static TSChannel getAChannelWithCode(String code) {
    session.clear();
    Query<TSChannel> query = session.createQuery("from TSChannel where code =:code and status='ACTIVE'");
    query.setParameter("code", code);
    List<TSChannel> list = query.getResultList();
    if (list.size() != 0) {
      return list.get(0);
    } else {
      return null;
    }
  }

  public static List<TSChannel> getChannelWithParentId(Integer parentId) {
    session.clear();
    Query<TSChannel> query = session.createQuery("from TSChannel where parentId =:parentId and status='ACTIVE'");
    query.setParameter("parentId", parentId);
    return query.getResultList();
  }

  public static List<Integer> getListIdAccountOfChannel(Integer parentId, String type) {
    session.clear();
    Query<TSChannel> query = session.createQuery("from TSChannel where parentId =:parentId and type =:type and not status='DELETED'");
    query.setParameter("parentId", parentId);
    query.setParameter("type", type);
    List<Integer> result = new ArrayList<>();
    for (TSChannel tsChannel : query.getResultList()) {
      result.add(tsChannel.getId());
    }
    return result;
  }

  public static void deleteChannelWithListCode(List<String> listCode) {
    Session session2 = sendSessionDBAssets();
    Query<TSChannel> query = session2.createQuery("delete TSChannel where code in :listCode");
    query.setParameter("listCode", listCode);
    int row = query.executeUpdate();
    session2.getTransaction().commit();
  }

  public static void deleteChannelWithItemLevel(Integer itemLevel) {
    Query<TSChannel> query = session.createQuery("delete TSChannel where itemLevel =:itemLevel");
    query.setParameter("itemLevel", itemLevel);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static void insertIntoChannel(String code, String name, Integer wareHouse, Integer favorite, Integer itemLevel, Integer parentChannelId, String username, String status, String type) {
    Session session2 = sendSessionDBAssets();
    TSChannel tsChannel = new TSChannel();
    tsChannel.setCode(code);
    tsChannel.setName(name);
    tsChannel.setStatus(status);
    tsChannel.setUserName(username);
    tsChannel.setFavorite(favorite);
    tsChannel.setWareHouse(wareHouse);
//    tsChannel.setParentId(parentChannelId);
    tsChannel.setType(type);

    session2.save(tsChannel);
    session2.getTransaction().commit();
  }

  public static List<TSChannel> getAllChannelWithStatusAndType(String status, String type) {
    Session session2 = sendSessionDBAssets();
    Query<TSChannel> query = session2.createQuery("from TSChannel where status=: status and type =:type");
    query.setParameter("status", status);
    query.setParameter("type", type);
    return query.getResultList();
  }

  public static List<TSChannel> getChannelLikeChannelCodeAndType(String status, String code, String type) {
    Query<TSChannel> query = session.createQuery("from TSChannel where status=: status and type =:type and code like :code");
    query.setParameter("status", status);
    query.setParameter("type", type);
    query.setParameter("code", code + "%");
    return query.getResultList();
  }

  public static void deleteIssueDateOfBondTempInTsChannel(String bondTemp, String type) {
    Session session2 = sendSessionDBAssets();
    Query<TSChannel> query = session2.createQuery("delete TSChannel where type =:type and code like :bondTemp");
    query.setParameter("type", type);
    query.setParameter("bondTemp", "%" + bondTemp);
    int row = query.executeUpdate();
    session2.getTransaction().commit();
  }

  public static List<TSChannel> getIssueDateOfBondTemp(String bondTemp, String type, String status) {
    session.clear();
    Query<TSChannel> query = session.createQuery("from TSChannel where type =:type and not status=:status and code like :bondTemp");
    query.setParameter("type", type);
    query.setParameter("status", status);
    query.setParameter("bondTemp", bondTemp + "%");
    query.setParameter("bondTemp", "%" + bondTemp);
    return query.getResultList();
  }
}
