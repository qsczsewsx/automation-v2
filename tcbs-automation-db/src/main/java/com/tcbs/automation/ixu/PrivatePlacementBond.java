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
@Table(name = "PRIVATE_PLACEMENT_BOND")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PrivatePlacementBond {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;

  @Column(name = "ORIGIN_ID")
  private String originId;
  @Column(name = "BOND_CODE")
  private String bondCode;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "APPLY_DATE")
  private Timestamp applyDate;
  @Column(name = "EXPIRED_DATE")
  private Timestamp expiredDate;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "LAST_UPDATED_DATE")
  private Timestamp lastUpdatedTime;


  @Step
  public static void deleteByBondCode(String bondCode) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    org.hibernate.query.Query query = ixuDbConnection.getSession().createQuery(
      "delete from PrivatePlacementBond s where s.bondCode = :bondCode");
    query.setParameter("bondCode", bondCode);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void createNewEntity(PrivatePlacementBond entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }


  @Step
  public static void clearDataTable() {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    org.hibernate.query.Query query = ixuDbConnection.getSession().createQuery(
      "delete from PrivatePlacementBond s where s.id is not null ");
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }


  public static List<PrivatePlacementBond> findAll(List<String> originIds) {
    ixuDbConnection.getSession().clear();
    Query<PrivatePlacementBond> query = ixuDbConnection.getSession().createQuery(
      "from PrivatePlacementBond s where s.originId in (:originId) order by s.originId DESC,  s.bondCode DESC ", PrivatePlacementBond.class);
    query.setParameter("originId", originIds);
    List<PrivatePlacementBond> privatePlacementBondList = query.getResultList();
    ixuDbConnection.closeSession();
    return privatePlacementBondList;
  }
}
