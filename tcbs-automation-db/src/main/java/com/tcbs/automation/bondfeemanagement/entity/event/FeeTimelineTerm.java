package com.tcbs.automation.bondfeemanagement.entity.event;

import com.tcbs.automation.bondfeemanagement.BondFeeManagementConnection;
import com.tcbs.automation.bondfeemanagement.entity.fee.FeeTermBase;
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
@Table(name = "FEE_TIMELINE_TERM")
@Getter
@Setter
public class FeeTimelineTerm extends FeeTermBase {
  @Id
  @Column(name = "TERM_ID", updatable = false, nullable = false)
  private Integer termId;

  public FeeTimelineTerm(FeeTimelineTerm feeTermBase) {
    super(feeTermBase);
  }

  public FeeTimelineTerm() {
    super();
  }

  @Step
  public static void truncateData() {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("truncate table FEE_TIMELINE_TERM");
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void deleteByFeeId(Integer feeId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("delete FEE_TIMELINE_TERM where FEE_ID = :feeId");
    query.setParameter("feeId", feeId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void deleteById(Integer termId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("delete FEE_TIMELINE_TERM where TERM_ID = :termId");
    query.setParameter("termId", termId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void insert(FeeTimelineTerm feeTimelineTerm) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.saveOrUpdate(feeTimelineTerm);
    trans.commit();
  }

  @Step
  public static FeeTimelineTerm getById(Integer termId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<FeeTimelineTerm> query = session.createNativeQuery("select * from FEE_TIMELINE_TERM where TERM_ID = :termId", FeeTimelineTerm.class);
    query.setParameter("termId", termId);
    List<FeeTimelineTerm> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }

    return results.get(0);
  }

}