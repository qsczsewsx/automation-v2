package com.tcbs.automation.ixu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.thucydides.core.annotations.Step;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "FRACTIONAL_SHARE_TRADING_TRANSACTION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class FractionalShareTradingTransaction {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private String id;
  @Column(name = "CODE105C")
  private String code105c;
  @Column(name = "STOCK_SYMBOL")
  private String stockSymbol;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "ISSUE_DATE")
  private Timestamp issueDate;
  @Column(name = "REGISTER_DATE")
  private String registerDate;
  @Column(name = "REGISTER_UNIT_PRICE")
  private String registerUnitPrice;
  @Column(name = "REGISTER_VOLUME")
  private String registerVolume;
  @Column(name = "SELL_UNIT_PRICE")
  private String sellUnitPrice;
  @Column(name = "SELL_VOLUME")
  private String sellVolume;
  @Column(name = "SELL_DATE")
  private String sellDate;
  @Column(name = "POINT")
  private Integer point;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;
  @Column(name = "UPDATED_DATE")
  private Timestamp updatedDate;


  @Step
  public static FractionalShareTradingTransaction insert(FractionalShareTradingTransaction entity) {
    Session session = ixuDbConnection.getSession();
    session.beginTransaction();
    session.save(entity);
    session.getTransaction().commit();
    ixuDbConnection.closeSession();
    return entity;
  }

  @Step
  public static void delete(List<String> tcbsId) {
    Session session = ixuDbConnection.getSession();
    session.beginTransaction();

    Query query = ixuDbConnection.getSession().createQuery(
      "delete from FractionalShareTradingTransaction where tcbsId in :tcbsId");

    query.setParameter("tcbsId", tcbsId);
    query.executeUpdate();

    session.getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  public static FractionalShareTradingTransaction findByUniqueCondition(String code105c, Date issueDate, String symbol) {
    ixuDbConnection.getSession().clear();
    org.hibernate.query.Query<FractionalShareTradingTransaction> query = ixuDbConnection.getSession().createQuery(
      "from FractionalShareTradingTransaction a where a.code105c = :code105c and a.stockSymbol = :symbol" +
        " and trunc(a.issueDate) >= to_date(:issueDate , 'dd/MM/yyyy')");
    query.setParameter("issueDate", DateFormatUtils.format(issueDate, "dd/MM/yyyy"));
    query.setParameter("code105c", code105c);
    query.setParameter("symbol", symbol);
    List<FractionalShareTradingTransaction> res = query.getResultList();
    if (res == null || res.isEmpty()) {
      ixuDbConnection.closeSession();
      return null;
    }
    ixuDbConnection.closeSession();
    return res.get(0);
  }
}
