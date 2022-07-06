package com.tcbs.automation.ixu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "AF_TYPE")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AfType {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "CODE105C")
  private String code105C;
  @Column(name = "NAME")
  private String name;
  @Column(name = "DELETED")
  private Integer deleted;
  @Column(name = "LAST_UPDATED_TIME")
  private Timestamp lastUpdatedTime;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Step
  public static void deleteAfTypeByListTcbsId(List<String> tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete AfType a where a.tcbsId in :tcbsId");
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void insertAfType(AfType afType) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    ixuDbConnection.getSession().save(afType);

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void deleteAllAfTypes() {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete AfType a where a.id is not null");
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<AfType> findAllAfTypeWhichIsNotDeleted() {
    ixuDbConnection.getSession().clear();
    Query<AfType> query = ixuDbConnection.getSession().createQuery(
      "from AfType a where a.deleted=0 order by a.id desc ");
    List<AfType> afTypeList = query.getResultList();
    ixuDbConnection.getSession();
    return afTypeList;
  }
}
