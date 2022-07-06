package com.tcbs.automation.ixu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "BLACK_LIST_ALL_CAMPAIGN")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BlacklistAllCampaigns {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;

  @Column(name = "CODE_105C")
  private String code105C;

  @Column(name = "TCBS_ID")
  private String tcbsId;

  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Step
  public static void deleteByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    if (!ixuDbConnection.getSession().getTransaction().isActive()) {
      ixuDbConnection.getSession().beginTransaction();
    }
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from BlacklistAllCampaigns a where a.tcbsId = :tcbsId");
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
  }

  @Step
  public static void saveOrUpdate(BlacklistAllCampaigns entity) {
    ixuDbConnection.getSession().clear();
    if (!ixuDbConnection.getSession().getTransaction().isActive()) {
      ixuDbConnection.getSession().beginTransaction();
    }
    ixuDbConnection.getSession().saveOrUpdate(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<BlacklistAllCampaigns> getAll() {
    org.hibernate.query.Query<BlacklistAllCampaigns> query = ixuDbConnection.getSession().createQuery(
      "from BlacklistAllCampaigns", BlacklistAllCampaigns.class);
    List<BlacklistAllCampaigns> result = query.getResultList();
    ixuDbConnection.closeSession();
    return result;
  }
}
