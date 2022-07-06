package com.tcbs.automation.bondfeemanagement.entity.fee;


import com.tcbs.automation.bondfeemanagement.BondFeeManagementConnection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BOND_FEE")
public class BondFee extends FeeBaseEntity {
  @Id
  @Column(name = "FEE_ID", updatable = false, nullable = false)
  private Integer feeId;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "FEE_ID")
  private List<BondFeeTerm> bondFeeTerms = new ArrayList<>();

  public BondFee(FeeBaseEntity feeBase) {
    super(feeBase);
  }


  @Step
  public static void truncateData() {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("truncate table BOND_FEE");
    query.executeUpdate();
    trans.commit();
  }


  @Step
  public static void deleteById(Integer feeId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();

    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BOND_FEE where FEE_ID = :feeId", BondFee.class);
    query.setParameter("feeId", feeId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void deleteByGroupId(Integer groupId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();

    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BOND_FEE where GROUP_ID = :groupId", BondFee.class);
    query.setParameter("groupId", groupId);
    query.executeUpdate();
    trans.commit();
  }


  @Step
  public static void insert(BondFee bondFee) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.saveOrUpdate(bondFee);
    trans.commit();
  }

  @Step
  public static BondFee getById(Integer feeId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<BondFee> query = session.createNativeQuery("select * from BOND_FEE where FEE_ID = :feeId", BondFee.class);
    query.setParameter("feeId", feeId);
    List<BondFee> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }

    return results.get(0);
  }

}

