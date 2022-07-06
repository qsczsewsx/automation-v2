package com.tcbs.automation.ixu;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
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
@Table(name = "STOCK_REFERRAL_ACCUMULATION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockReferralAccumulation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  @JsonProperty("id")
  private Long id;

  @JsonProperty("tcbsId")
  @Column(name = "TCBS_ID")
  private String tcbsId;

  @Column(name = "POINT")
  @JsonProperty("point")
  private Double point;

  @Column(name = "AWARD_TYPE")
  @JsonProperty("awardType")
  private String awardType;

  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Column(name = "LAST_UPDATED_DATE")
  private Timestamp lastUpdatedDate;

  @Column(name = "VERSION")
  private String version;

  @Column(name = "CAMPAIGN_ID")
  @JsonProperty("campaignId")
  private String campaignId;

  @Column(name = "TAX_COLLECTION")
  private String taxCollection;

  public StockReferralAccumulation(String awardType, String campaignId, Double point) {
    this.awardType = awardType;
    this.campaignId = campaignId;
    this.point = point;
  }

  @Step
  public static void deleteByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    org.hibernate.query.Query query = ixuDbConnection.getSession().createQuery(
      "delete from StockReferralAccumulation s where s.tcbsId =:tcbsId");
    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static void create(StockReferralAccumulation entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();

    ixuDbConnection.getSession().save(entity);

    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<StockReferralAccumulation> findStockRefAccumulationTransactionByTcbsId(String tcbsId, String taxCollection) {
    Query<StockReferralAccumulation> query = ixuDbConnection.getSession().createQuery(
      "from StockReferralAccumulation a where a.tcbsId=:tcbsId and a.taxCollection=:taxCollection order by a.id desc ");
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("taxCollection", taxCollection);
    List<StockReferralAccumulation> stockReferralAccumulationList = query.getResultList();
    ixuDbConnection.closeSession();
    return stockReferralAccumulationList;
  }

}
