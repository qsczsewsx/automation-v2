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
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "BOND_CONTRACT_DATE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BondContractDate extends BaseEntity implements Serializable {

  @Id
  @Column(name = "ID")
  private Integer id;

  @Column(name = "BOND_ID")
  private Integer bondId;

  @Column(name = "CONTRACT_DATE")
  private Date contactDateDB;


  @Step
  public static void deleteById(Integer id) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();

    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BOND_CONTRACT_DATE where ID = :id");
    query.setParameter("id", id);
    query.executeUpdate();
    trans.commit();
  }

  @Step
  public static void deleteByBondId(Integer bondId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();

    org.hibernate.query.Query<?> query = session.createNativeQuery("delete BOND_CONTRACT_DATE where BOND_ID = :bondId");
    query.setParameter("bondId", bondId);
    query.executeUpdate();
    trans.commit();
  }


  @Step
  public static void insert(BondContractDate bondContractDate) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    session.saveOrUpdate(bondContractDate);
    trans.commit();
  }
}