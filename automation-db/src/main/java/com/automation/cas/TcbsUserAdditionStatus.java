package com.automation.cas;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "xxxx_USER_ADDITION_STATUS")
@Getter
@Setter
public class xxxxUserAdditionStatus {

  private static Logger logger = LoggerFactory.getLogger(xxxxProInvestorDocument.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "xxxx_ID")
  private String xxxxId;
  @Column(name = "VIEW_ASSET_BOND_STATUS")
  private BigDecimal viewAssetBondStatus;
  @Column(name = "VIEW_ASSET_FUND_STATUS")
  private BigDecimal viewAssetFundStatus;
  @Column(name = "VABS_UPDATED_DATE")
  private Timestamp vabsUpdatedDate;
  @Column(name = "VAFS_UPDATED_DATE")
  private Timestamp vafsUpdatedDate;
  @Column(name = "RS_UPDATED_DATE")
  private Timestamp rsUpdatedDate;

  @Step
  public static xxxxUserAdditionStatus getUserByxxxxId(String xxxxId) {
    CAS.casConnection.getSession().clear();
    Query<xxxxUserAdditionStatus> query = CAS.casConnection.getSession().createQuery(
      "from xxxxUserAdditionStatus a where a.xxxxId =: xxxxId", xxxxUserAdditionStatus.class);
    query.setParameter("xxxxId", xxxxId);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new xxxxUserAdditionStatus();
    }
  }

  @Step
  public static List<xxxxUserAdditionStatus> getListUserByFundStatus(int status) {
    CAS.casConnection.getSession().clear();
    Query<xxxxUserAdditionStatus> query = CAS.casConnection.getSession().createQuery(
      "from xxxxUserAdditionStatus a where a.viewAssetFundStatus =: status", xxxxUserAdditionStatus.class);
    query.setParameter("status", new BigDecimal(status));
    return query.getResultList();
  }

}
