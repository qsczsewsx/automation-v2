package com.tcbs.automation.cas;

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
@Table(name = "TCBS_USER_ADDITION_STATUS")
@Getter
@Setter
public class TcbsUserAdditionStatus {

  private static Logger logger = LoggerFactory.getLogger(TcbsProInvestorDocument.class);
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @NotNull
  @Column(name = "ID")
  private BigDecimal id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
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
  public static TcbsUserAdditionStatus getUserByTcbsId(String tcbsId) {
    CAS.casConnection.getSession().clear();
    Query<TcbsUserAdditionStatus> query = CAS.casConnection.getSession().createQuery(
      "from TcbsUserAdditionStatus a where a.tcbsId =: tcbsId", TcbsUserAdditionStatus.class);
    query.setParameter("tcbsId", tcbsId);
    try {
      return query.getSingleResult();
    } catch (Exception e) {
      return new TcbsUserAdditionStatus();
    }
  }

  @Step
  public static List<TcbsUserAdditionStatus> getListUserByFundStatus(int status) {
    CAS.casConnection.getSession().clear();
    Query<TcbsUserAdditionStatus> query = CAS.casConnection.getSession().createQuery(
      "from TcbsUserAdditionStatus a where a.viewAssetFundStatus =: status", TcbsUserAdditionStatus.class);
    query.setParameter("status", new BigDecimal(status));
    return query.getResultList();
  }

}
