package com.tcbs.automation.ixu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "HOLDING_BONDPRO_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HoldingBondProTransaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "CODE_105C")
  private String code105C;
  @Column(name = "BOND_CODE")
  private String bondCode;
  @Column(name = "CATEGORY")
  private String category;
  @Column(name = "TRADING_CODE")
  private String tradingCode;
  @Column(name = "RATE")
  private Double rate;
  @Column(name = "QUANTITY")
  private Double quantity;
  @Column(name = "UNIT_PRICE")
  private Double unitPrice;
  @Column(name = "NUM_PERIOD")
  private Double numPeriod;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "ISSUED_DATE")
  private Timestamp issuedDate;
  @Column(name = "RULE_INPUT")
  private String ruleInput;
  @Column(name = "RULE_OUTPUT")
  private String ruleOutput;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Step
  public static void deleteByTcbsId(List<String> tcbsIds) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from HoldingBondProTransaction a where a.tcbsId in :tcbsId");
    query.setParameter("tcbsId", tcbsIds);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
  }

  @Step
  public static List<HoldingBondProTransaction> getByTcbsIds(List<String> tcbsIds) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query<HoldingBondProTransaction> query = ixuDbConnection.getSession().createQuery(
      "from HoldingBondProTransaction a where a.tcbsId in :tcbsId order by a.tcbsId desc");
    query.setParameter("tcbsId", tcbsIds);
    List<HoldingBondProTransaction> results = query.getResultList();
    ixuDbConnection.closeSession();
    return results;
  }

  @Step
  public static HoldingBondProTransaction insertOrSave(HoldingBondProTransaction entity) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    ixuDbConnection.getSession().saveOrUpdate(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
    return entity;
  }
}
