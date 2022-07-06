package com.tcbs.automation.bondfeemanagement.entity.bond;

import com.tcbs.automation.bondfeemanagement.BaseEntity;
import com.tcbs.automation.bondfeemanagement.BondFeeManagementConnection;
import lombok.*;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "BOND_CONTRACT_PAYMENT_DATE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BondContractPaymentDate extends BaseEntity {

  @Id
  @Column(name = "ID")
  private Integer id;


  @Column(name = "BOND_ID")
  private Integer bondId;

  @Column(name = "PAYMENT_DATE")
  private Date paymentDateDB;


  @Step
  public static void deleteById(Integer id) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();

    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BOND_CONTRACT_PAYMENT_DATE where ID = :id");
    query.setParameter("id", id);
    query.executeUpdate();
    trans.commit();
  }


  @Step
  public static void deleteByBondId(Integer bondId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();

    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BOND_CONTRACT_PAYMENT_DATE where BOND_ID = :bondId");
    query.setParameter("bondId", bondId);
    query.executeUpdate();
    trans.commit();
  }


  @Step
  public static void insert(BondContractPaymentDate bondContractPaymentDate) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.saveOrUpdate(bondContractPaymentDate);
    trans.commit();
  }

}
