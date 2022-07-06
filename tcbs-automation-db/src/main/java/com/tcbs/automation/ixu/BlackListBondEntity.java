package com.tcbs.automation.ixu;


import io.cucumber.java.mk_latn.No;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.thucydides.core.annotations.Step;
import org.apache.commons.codec.net.QCodec;
import org.hibernate.Session;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "BLACK_LIST_BOND")
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class BlackListBondEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  @NonNull
  private Long id;
  @Column(name = "CAMPAIGN_ID")
  private String campaignId;
  @Column(name = "BOND_PRODUCT_CODE")
  private String productCode;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;


  @Step
  public static void insert(String campaignId, String productCode) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    session.beginTransaction();
    BlackListBondEntity entity = new BlackListBondEntity();
    entity.setCampaignId(campaignId);
    entity.setProductCode(productCode);
    entity.setCreatedDate(new Timestamp(new Date().getTime()));
    session.saveOrUpdate(entity);
    session.getTransaction().commit();
    ixuDbConnection.closeSession();
    return;
  }

  @Step
  public static void delete(String campaignId, String productCode) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    session.beginTransaction();
    Query query = session.createQuery(
      "delete from BlackListBondEntity where campaignId = :campaignId and productCode = :productCode");
    query.setParameter("campaignId", campaignId);
    query.setParameter("productCode", productCode);
    query.executeUpdate();
    session.getTransaction().commit();
    ixuDbConnection.closeSession();
    return;
  }

  @Step
  public static List<BlackListBondEntity> find(String campaignId, String productCode) {
    Session session = ixuDbConnection.getSession();
    session.clear();
    Query query = session.createQuery(
      "from BlackListBondEntity where campaignId = :campaignId and productCode = :productCode", BlackListBondEntity.class);
    query.setParameter("campaignId", campaignId);
    query.setParameter("productCode", productCode);

    List<BlackListBondEntity> blackListBondEntities = query.getResultList();
    ixuDbConnection.closeSession();
    return blackListBondEntities;
  }
}
