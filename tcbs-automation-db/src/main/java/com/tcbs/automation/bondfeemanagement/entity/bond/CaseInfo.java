package com.tcbs.automation.bondfeemanagement.entity.bond;

import com.tcbs.automation.bondfeemanagement.BondFeeManagementConnection;
import lombok.*;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CASE_INFO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CaseInfo {
  @Id
  @Column(name = "CASE_ID")
  private Integer caseId;

  @Column(name = "CASE_CODE")
  private String caseCode;

  @Column(name = "CASE_NAME")
  private String caseName;

  @Step
  public static void deleteById(Integer caseId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("delete CASE_INFO where CASE_ID = :caseId");
    query.setParameter("caseId", caseId);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void insert(CaseInfo caseInfo) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.saveOrUpdate(caseInfo);
    trans.commit();
  }
}