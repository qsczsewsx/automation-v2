package com.tcbs.automation.tcinvest;

import lombok.Getter;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "INV_BOND_CONTRACT_SNAPSHOT")
public class InvBondContractSnapshot {
  public static final Session session = TcInvest.tcInvestDbConnection.getSession();
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "CONTRACT_SNAPSHOT_ID")
  private String contractSnapshotId;
  @Column(name = "ORDER_ID")
  private String orderId;
  @Column(name = "TEMPLATE_ID")
  private String templateId;
  @Column(name = "PARAMS")
  private String params;
  @Column(name = "CREATED_DATE")
  private Date createdDate;
  @Column(name = "SIGNED_DATE")
  private Date signedDate;
  @Column(name = "TYPE")
  private String type;

  @Step("Get trading by orderId")
  public static InvBondContractSnapshot getTradingByOrderId(String orderId) {
    session.clear();
    Query<InvBondContractSnapshot> query = session.createQuery("from InvBondContractSnapshot where orderId=:orderId");
    query.setParameter("orderId", orderId);
    List<InvBondContractSnapshot> result = query.getResultList();

    if (result.size() > 0) {
      return result.get(0);
    } else {
      return null;
    }
  }
}