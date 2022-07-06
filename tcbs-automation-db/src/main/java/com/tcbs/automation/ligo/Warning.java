package com.tcbs.automation.ligo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "warning")
public class Warning {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "id")
  private Long id;
  @NotNull
  @Column(name = "tcbsid")
  private String tcbsid;
  @NotNull
  @Column(name = "code105c")
  private String code105C;
  @NotNull
  @Column(name = "name")
  private String name;
  @NotNull
  @Column(name = "conditions")
  private String conditions;
  @NotNull
  @Column(name = "object_type")
  private String objectType;
  @Column(name = "object_data")
  private String objectData;
  @Column(name = "additional_info")
  private String additionalInfo;
  @Column(name = "enable")
  private boolean enable;
  @Column(name = "send_inbox")
  private boolean sendInbox;
  @NotNull
  @Column(name = "created_on")
  private Long createdOn;
  @Column(name = "modified_on")
  private Long modifiedOn;


  public static void deleteByName(String name) {
    Session session = LigoRepository.ligoDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<?> query = session.createQuery(
      "DELETE FROM Warning ib WHERE ib.name = :name"
    );
    query.setParameter("name", name);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  public static Warning getWarningById(Long id) {
    Session session = LigoRepository.ligoDbConnection.getSession();
    session.clear();
    Query<Warning> query = session.createQuery("from Warning w where w.id =:id");
    query.setParameter("id", id);
    Warning result = query.getSingleResult();
    return result;
  }

  public static List<Warning> getListWarningById(Long id) {
    Session session = LigoRepository.ligoDbConnection.getSession();
    session.clear();
    Query<Warning> query = session.createQuery("from Warning w where w.id =: id");
    query.setParameter("id", id);
    List<Warning> result = query.getResultList();
    return result;
  }

  public static List<Warning> getListWarningByTcbsid(String tcbsid) {
    Session session = LigoRepository.ligoDbConnection.getSession();
    session.clear();
    Query<Warning> query = session.createQuery("from Warning w where w.tcbsid =: tcbsid order by id desc");
    query.setParameter("tcbsid", tcbsid);
    List<Warning> result = query.getResultList();
    return result;
  }

  public static List<Warning> getListWarningByTcbsidAndParam(String tcbsid, String objectType) {
    Session session = LigoRepository.ligoDbConnection.getSession();
    session.clear();
    Query<Warning> query = session.createQuery("from Warning w where w.tcbsid =: tcbsid and w.objectType =: objectType order by id desc");
    query.setParameter("tcbsid", tcbsid);
    query.setParameter("objectType", objectType);
    List<Warning> result = query.getResultList();
    return result;
  }
}
