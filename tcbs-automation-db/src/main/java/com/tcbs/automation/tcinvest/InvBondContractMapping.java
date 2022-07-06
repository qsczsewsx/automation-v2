package com.tcbs.automation.tcinvest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.List;

/**
 * @author Nguyen Ngoc Tien
 * @created 16/09/2020 - 11:13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "INV_BOND_CONTRACT_MAPPING")
public class InvBondContractMapping {
  public static final Session session = TcInvest.tcInvestDbConnection.getSession();
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;
  @Column(name = "PREFIX")
  private String prefix;
  @Column(name = "TEMPLATE_VARIABLE")
  private String templateVariable;
  @Column(name = "DESCRIPTION")
  private String description;
  @Column(name = "SOURCE")
  private String source;
  @Column(name = "SOURCE_VARIABLE")
  private String sourceVariable;
  @Column(name = "DATA_TYPE")
  private String dataType;
  @Column(name = "FORMAT")
  private String format;
  @Column(name = "REFERENCE_VARIABLE")
  private String referenceVariable;
  @Column(name = "PARENT_VARIABLE")
  private String parentVariable;
  @Column(name = "THRESHOLD")
  private Double threshold;
  @Column(name = "TRADING_VARIABLE")
  private String tradingVariable;

  @Step("Get inv contract mapping by reference")
  public static List<InvBondContractMapping> getInvContractMappingByReference() {
    session.clear();
    Query<InvBondContractMapping> query = session.createQuery("from InvBondContractMapping where referenceVariable is not null");
    List<InvBondContractMapping> result = query.getResultList();

    if (result.size() > 0) {
      return result;
    } else {
      return null;
    }
  }
}
