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
@Table(name = "REFERRAL_FRIEND_DERIVATIVE")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReferralFriendDerivative {

  private static final String TCBS_ID = "tcbsId";
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "MAX_AMOUNT")
  private String maxAmount;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "LAST_UPDATED")
  private Timestamp lastUpdated;

  @Step
  public static ReferralFriendDerivative insert(ReferralFriendDerivative entity) {
    ixuDbConnection.getSession().getTransaction().begin();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
    return entity;
  }

  @Step
  public static void delete(String tcbsId) {
    ixuDbConnection.getSession().getTransaction().begin();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete ReferralFriendDerivative a where a.tcbsId = :tcbsId"
    );
    query.setParameter(TCBS_ID, tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void delete(List<String> tcbsId) {
    ixuDbConnection.getSession().getTransaction().begin();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete ReferralFriendDerivative a where a.tcbsId in :tcbsId"
    );
    query.setParameter(TCBS_ID, tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<ReferralFriendDerivative> findAllByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<ReferralFriendDerivative> query = ixuDbConnection.getSession().createQuery("" +
      "from ReferralFriendDerivative a where a.tcbsId = :tcbsId order " +
      "by a.createdDate "
    );
    query.setParameter(TCBS_ID, tcbsId);
    List<ReferralFriendDerivative> referralFriendDerivativeList = query.getResultList();
    ixuDbConnection.closeSession();
    return referralFriendDerivativeList;
  }

  @Step
  public static void clearAll() {
    ixuDbConnection.getSession().getTransaction().begin();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from ReferralFriendDerivative"
    );
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }


}
