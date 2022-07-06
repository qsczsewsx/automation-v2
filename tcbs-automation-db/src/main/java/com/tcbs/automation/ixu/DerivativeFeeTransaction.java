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
import java.time.ZonedDateTime;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "DERIVATIVE_FEE_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DerivativeFeeTransaction {

  private static final String TCBS_ID = "tcbsId";
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "CODE_105C")
  private String code105C;
  @Column(name = "TRADE_DATE")
  private ZonedDateTime tradeDate;
  @Column(name = "TOTAL_VOLUME")
  private Integer totalVolume;
  @Column(name = "TOTAL_FEE")
  private Float totalFee;
  @Column(name = "TOTAL_HNX_FEE")
  private Float totalHnxFee;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Step
  public static DerivativeFeeTransaction insert(DerivativeFeeTransaction entity) {
    ixuDbConnection.getSession().getTransaction().begin();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
    return entity;
  }

  @Step
  public static void deleteByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().getTransaction().begin();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete DerivativeFeeTransaction a where a.tcbsId = :tcbsId"
    );
    query.setParameter(TCBS_ID, tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<DerivativeFeeTransaction> findByTcbsIdAndTradeDate(String tcbsId, String tradeDate) {
    ixuDbConnection.getSession().clear();
    Query<DerivativeFeeTransaction> query = ixuDbConnection.getSession().createQuery("" +
      "from DerivativeFeeTransaction a where a.tcbsId = :tcbsId " +
      "and trunc(a.tradeDate) = to_date(:tradeDate, 'yyyy-mm-dd') "
    );
    query.setParameter(TCBS_ID, tcbsId);
    query.setParameter("tradeDate", tradeDate);
    List<DerivativeFeeTransaction> derivativeFeeTransactionList = query.getResultList();
    ixuDbConnection.closeSession();
    return derivativeFeeTransactionList;
  }
}
