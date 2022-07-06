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
@Table(name = "BOND_TEMPLATE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BondTemplate {

  @Id
  @Column(name = "BOND_TEMPLATE_CODE")
  private String bondTemplateCode;

  @Column(name = "BOND_TEMPLATE_NAME")
  private String bondTemplateName;

  @Column(name = "CASE_ID")
  private Integer caseId;

  @Column(name = "TENOR_YEAR")
  private Integer tenorYear;

  @Column(name = "TENOR_MONTH")
  private Integer tenorMonth;

  @Column(name = "TENOR_DAY")
  private Integer tenorDay;

  @Step
  public static void deleteByCode(String bondTemplateCode) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BOND_TEMPLATE where BOND_TEMPLATE_CODE = :bondTemplateCode");
    query.setParameter("bondTemplateCode", bondTemplateCode);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void insert(BondTemplate bondTemplate) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.saveOrUpdate(bondTemplate);
    trans.commit();
  }
}