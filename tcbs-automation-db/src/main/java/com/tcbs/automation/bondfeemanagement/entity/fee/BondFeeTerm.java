package com.tcbs.automation.bondfeemanagement.entity.fee;

import com.tcbs.automation.bondfeemanagement.BondFeeManagementConnection;
import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "BOND_FEE_TERM")
public class BondFeeTerm extends FeeTermBase {
  @Id
  @Column(name = "TERM_ID", updatable = false, nullable = false)
  private Integer termId;

  @Step
  public static void deleteByFeeId(Integer feeId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BOND_FEE_TERM where FEE_ID = :feeId");
    query.setParameter("feeId", feeId);
    query.executeUpdate();
    trans.commit();
  }


  @Step
  public static void deleteById(Integer termId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BOND_FEE_TERM where TERM_ID = :termId");
    query.setParameter("termId", termId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void insert(BondFeeTerm bondFeeTerm) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.saveOrUpdate(bondFeeTerm);
    trans.commit();
  }

  @Step
  public static BondFeeTerm getById(Integer termId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<BondFeeTerm> query = session.createNativeQuery("select * from BOND_FEE_TERM where TERM_ID = :termId", BondFeeTerm.class);
    query.setParameter("termId", termId);
    List<BondFeeTerm> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }

    return results.get(0);
  }

  @Step
  public static List<BondFeeTerm> getByFeeId(Integer feeId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<BondFeeTerm> query = session.createNativeQuery("select * from BOND_FEE_TERM where FEE_ID = :feeId order by TERM_ID", BondFeeTerm.class);
    query.setParameter("feeId", feeId);
    List<BondFeeTerm> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results;
  }
}

