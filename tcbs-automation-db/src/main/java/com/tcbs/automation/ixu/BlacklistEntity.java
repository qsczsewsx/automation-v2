package com.tcbs.automation.ixu;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.thucydides.core.annotations.Step;
import org.assertj.core.util.Strings;
import org.hibernate.Session;
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
@Table(name = "BLACKLIST")
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class BlacklistEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  @NonNull
  private Long id;

  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "CODE_105C")
  private String code105c;
  @Column(name = "CAMPAIGN_ID")
  private String campaignId;
  @Column(name = "DELETED")
  private int deleted;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "UPDATED_DATE")
  private Timestamp updatedDate;

  @Step
  public static void update(String tcbsId, String code105c, String campaignId) {

    Session session = ixuDbConnection.getSession();
    session.clear();
    session.beginTransaction();

    String sql = "update BlacklistEntity a set a.deleted = 1 " +
      "where a.campaignId=:campaignId AND deleted = 0 ";

    boolean addSpecial = false;
    if (!Strings.isNullOrEmpty(code105c) || !Strings.isNullOrEmpty(tcbsId)) {
      sql += " AND (a.code105c = :code105c OR a.tcbsId = :tcbsId)";
      addSpecial = true;
    }

    Query query = session.createQuery(sql);
    query.setParameter("campaignId", campaignId);
    if (addSpecial) {
      query.setParameter("code105c", code105c);
      query.setParameter("tcbsId", tcbsId);
    }
    query.executeUpdate();

    session.getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void delete(String campaignId) {

    Session session = ixuDbConnection.getSession();
    session.clear();
    session.beginTransaction();

    String sql = "DELETE FROM BlacklistEntity a " +
      "where a.campaignId=:campaignId ";

    Query query = session.createQuery(sql);
    query.setParameter("campaignId", campaignId);

    query.executeUpdate();

    session.getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void create(List<BlacklistEntity> entities) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    session.beginTransaction();
    entities.forEach(session::save);
    session.getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<BlacklistEntity> getBlacklistByCampaignIdAndCode105cOrTcbsId(String tcbsId,
                                                                                  String code105c,
                                                                                  String campaignId) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    Query query = session.createQuery(
      "from BlacklistEntity where campaignId = :campaignId AND deleted = 0 " +
        " AND (code105c = :code105c OR tcbsId = :tcbsId)", BlacklistEntity.class);

    query.setParameter("campaignId", campaignId);
    query.setParameter("code105c", code105c);
    query.setParameter("tcbsId", tcbsId);
    List<BlacklistEntity> blacklistEntities = query.getResultList();
    ixuDbConnection.closeSession();
    return blacklistEntities;
  }
}
