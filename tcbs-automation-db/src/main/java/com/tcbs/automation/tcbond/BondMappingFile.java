package com.tcbs.automation.tcbond;

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
 * @created 16/09/2020 - 11:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BondMappingFile")
public class BondMappingFile extends TcBond {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;
  @Column(name = "FileId")
  private Integer fileId;
  @Column(name = "ProductId")
  private Integer productId;
  @Column(name = "Version")
  private String version;
  @Column(name = "EcmId")
  private String ecmId;
  @Column(name = "Status")
  private String status;


  @Step("Get BondMappingFile by ProductCode")
  public BondMappingFile getEcmIdByProductCode(String productCode) {
    Query<BondMappingFile> query = tcBondDbConnection.getSession().createQuery(
      "from BondMappingFile bmf join BondProduct bp on bmf.ProductId = bp.ID where bp.Code = :productCode and UPPER(bmf.status) = 'ACTIVE'");
    List<BondMappingFile> result = query
      .setParameter("productCode", productCode)
      .getResultList();
    if (result.size() == 1) {
      return result.get(0);
    } else {
      return null;
    }
  }

  @Step("Get distinct by ecmId active")
  public List<String> findDistinctByEcmIdActive() {
    Session session = tcBondDbConnection.getSession();
    session.clear();
    Query query = session.createNativeQuery(
      "select distinct ecmId from BondMappingFile bmf where UPPER(bmf.status) = 'ACTIVE'");
    return (List<String>) query.getResultList();
  }
}
