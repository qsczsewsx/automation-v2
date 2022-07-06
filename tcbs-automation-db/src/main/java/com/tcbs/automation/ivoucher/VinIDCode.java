package com.tcbs.automation.ivoucher;

import lombok.*;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.ivoucher.IVoucher.ivoucherDbConnection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "VIN_ID_CODE")
@ToString
public class VinIDCode {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @NotNull
  @Column(name = "CODE")
  private String code;
  @NotNull
  @Column(name = "CAMPAIGN_CODE")
  private String campaignCode;
  @Column(name = "END_DATE")
  private Timestamp endDate;
  @NotNull
  @Column(name = "START_DATE")
  private Timestamp startDate;
  @NotNull
  @Column(name = "STATUS")
  private String status;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "LAST_MODIFIED_DATE")
  private Timestamp lastModifiedDate;
  @Column(name = "TCBS_ID")
  private String tcbsId;

  @Step
  public static void create(VinIDCode entity) {
    ivoucherDbConnection.getSession().clear();
    ivoucherDbConnection.getSession().beginTransaction();
    ivoucherDbConnection.getSession().save(entity);
    ivoucherDbConnection.getSession().getTransaction().commit();
  }

  @Step
  public static void deleteByCampaignCode(String campaignCode) {
    ivoucherDbConnection.getSession().clear();
    ivoucherDbConnection.getSession().beginTransaction();
    Query query = ivoucherDbConnection.getSession().createQuery(
      "delete from VinIDCode a where a.campaignCode =:campaignCode");
    query.setParameter("campaignCode", campaignCode);
    query.executeUpdate();
    ivoucherDbConnection.getSession().getTransaction().commit();
  }

  @Step
  public static List<VinIDCode> find(String vinidcode) {
    ivoucherDbConnection.getSession().clear();
    Query<VinIDCode> query = ivoucherDbConnection.getSession().createQuery(
      "from VinIDCode a where a.code = :code ");
    query.setParameter("code", vinidcode);
    return query.getResultList();
  }
}