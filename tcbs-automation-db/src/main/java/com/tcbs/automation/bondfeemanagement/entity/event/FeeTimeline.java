package com.tcbs.automation.bondfeemanagement.entity.event;

import com.tcbs.automation.bondfeemanagement.BondFeeManagementConnection;
import com.tcbs.automation.bondfeemanagement.entity.fee.FeeBaseEntity;
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
@Table(name = "FEE_TIMELINE")
@AllArgsConstructor
@NoArgsConstructor
public class FeeTimeline extends FeeBaseEntity {

  @Id
  @Column(name = "FEE_ID", updatable = false, nullable = false)
  private Integer feeId;

  @OneToMany(fetch = FetchType.LAZY)
  @JoinColumn(name = "FEE_ID")
  private List<FeeTimelineTerm> feeTerms = new ArrayList<>();

  public FeeTimeline(FeeBaseEntity feeBase) {
    super(feeBase);
  }

  public static String getTableName() {
    Table table = FeeTimeline.class.getAnnotation(Table.class);
    return table.name();
  }

  @Step
  public static void truncateData() {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("truncate table FEE_TIMELINE");
    query.executeUpdate();
    trans.commit();
  }


  @Step
  public static void deleteById(Integer feeId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("delete FEE_TIMELINE where FEE_ID = :feeId");
    query.setParameter("feeId", feeId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void insert(FeeTimeline feeTimeline) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.saveOrUpdate(feeTimeline);
    trans.commit();
  }

  @Step
  public static FeeTimeline getById(Integer feeId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    org.hibernate.query.Query<FeeTimeline> query = session.createNativeQuery("select * from FEE_TIMELINE where FEE_ID = :feeId", FeeTimeline.class);
    query.setParameter("feeId", feeId);
    List<FeeTimeline> results = query.getResultList();
    if (results == null || results.isEmpty()) {
      return null;
    }
    return results.get(0);
  }

}
