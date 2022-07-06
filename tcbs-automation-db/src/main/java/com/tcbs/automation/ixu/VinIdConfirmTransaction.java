package com.tcbs.automation.ixu;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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
@Table(name = "VIN_ID_CONFIRM_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class VinIdConfirmTransaction {
  private static final String TCBS_ID = "tcbsId";
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "CAMPAIGN_CODE")
  private String campaignCode;
  @Column(name = "VIN_CODE")
  private String vinCode;
  @Column(name = "VIN_CODE_ID")
  private String vinCodeID;
  @Column(name = "START_DATE")
  private Timestamp startDate;
  @Column(name = "END_DATE")
  private Timestamp endDate;
  @Column(name = "IS_PAID_BOND")
  private String isPaidBond;
  @Column(name = "IS_PAID_FUND")
  private String isPaidFund;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "MESSAGE")
  private String message;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "STATUS")
  private String status;

  public static VinIdConfirmTransaction insert(VinIdConfirmTransaction entity) {
    ixuDbConnection.getSession().getTransaction().begin();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
    return entity;
  }

  public static void delete(String tcbsId) {
    ixuDbConnection.getSession().getTransaction().begin();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete VinIdConfirmTransaction a where a.tcbsId = :tcbsId"
    );
    query.setParameter(TCBS_ID, tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  public static VinIdConfirmTransaction find(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<VinIdConfirmTransaction> query = ixuDbConnection.getSession().createQuery("" +
      "from VinIdConfirmTransaction  a where a.tcbsId = :tcbsId"
    );
    query.setParameter(TCBS_ID, tcbsId);
    VinIdConfirmTransaction vinIdConfirmTransaction = query.getSingleResult();
    ixuDbConnection.closeSession();
    if (vinIdConfirmTransaction == null) {
      return null;
    }
    return vinIdConfirmTransaction;
  }

  public static List<VinIdConfirmTransaction> findAll(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<VinIdConfirmTransaction> query = ixuDbConnection.getSession().createQuery("" +
      "from VinIdConfirmTransaction  a where a.tcbsId = :tcbsId order " +
      "by a.createdDate "
    );
    query.setParameter(TCBS_ID, tcbsId);
    List<VinIdConfirmTransaction> vinIdConfirmTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return vinIdConfirmTransactionList;
  }
}
