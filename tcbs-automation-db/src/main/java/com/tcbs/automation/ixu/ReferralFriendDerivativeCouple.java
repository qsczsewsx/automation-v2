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
@Table(name = "REFERRAL_FRIEND_DERIVATIVE_COUPLE")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReferralFriendDerivativeCouple {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;
  @Column(name = "TCBS_ID_REFEREE")
  private String tcbsIdReferee;
  @Column(name = "TCBS_ID_REFERRAL")
  private String tcbsIdReferral;
  @Column(name = "STATUS")
  private String status;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "DESCRIPTION")
  private String description;


  @Step
  public static ReferralFriendDerivativeCouple insert(ReferralFriendDerivativeCouple entity) {
    ixuDbConnection.getSession().getTransaction().begin();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
    return entity;
  }

  @Step
  public static void delete(String tcbsIdReferral, String tcbsIdReferee) {
    ixuDbConnection.getSession().getTransaction().begin();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete ReferralFriendDerivativeCouple a " +
        "where a.tcbsIdReferee = :tcbsIdReferee or a.tcbsIdReferral = :tcbsIdReferral"
    );
    query.setParameter("tcbsIdReferral", tcbsIdReferral);
    query.setParameter("tcbsIdReferee", tcbsIdReferee);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<ReferralFriendDerivativeCouple> findAllByTcbsIdReferee(String tcbsIdReferee) {
    ixuDbConnection.getSession().clear();
    Query<ReferralFriendDerivativeCouple> query = ixuDbConnection.getSession().createQuery(
      "from ReferralFriendDerivativeCouple a where a.tcbsIdReferee = :tcbsIdReferee order " +
        "by a.createdDate "
    );
    query.setParameter("tcbsIdReferee", tcbsIdReferee);
    List<ReferralFriendDerivativeCouple> referralFriendDerivativeCoupleList = query.getResultList();
    ixuDbConnection.closeSession();
    return referralFriendDerivativeCoupleList;
  }

  @Step
  public static void clearAll() {
    ixuDbConnection.getSession().getTransaction().begin();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from ReferralFriendDerivativeCouple"
    );
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }
}
