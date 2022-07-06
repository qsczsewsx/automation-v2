package com.tcbs.automation.stockgate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "DRAFT_MARKET_ORDER")
public class DraftMarketOrder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ORDER_ID")
  private String orderId;
  @NotNull
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @NotNull
  @Column(name = "CODE105C")
  private String code105C;
  @NotNull
  @Column(name = "ACCOUNT_TYPE")
  private String accountType;
  @NotNull
  @Column(name = "ACCOUNT_NO")
  private String accountNo;
  @NotNull
  @Column(name = "EXEC_TYPE")
  private String execType;
  @NotNull
  @Column(name = "SYMBOL")
  private String symbol;
  @NotNull
  @Column(name = "PRICE_TYPE")
  private String priceType;
  @Column(name = "PRICE")
  private String price;
  @NotNull
  @Column(name = "QUANTITY")
  private String quantity;
  @NotNull
  @Column(name = "EXPIRED_DATE")
  private Timestamp expiredDate;
  @NotNull
  @Column(name = "CREATED_AT")
  private Timestamp createdAt;
  @Column(name = "UPDATED_AT")
  private Timestamp updatedAt;
  @NotNull
  @Column(name = "STATUS")
  private String status;
  @Column(name = "SESSION_ID")
  private String sessionId;
  @Column(name = "OTP_TYPE")
  private String otpType;
  @Column(name = "OTP_VALUE")
  private String otpValue;
  @Column(name = "FLEX_ORDER_ID")
  private String flexOrderId;

  @Step
  public static DraftMarketOrder getSingleLatestOrder() {
    Query<DraftMarketOrder> query = StockgateConnBridge.stockgateConnection.getSession()
      .createQuery("from DraftMarketOrder a order by a.orderId desc").setMaxResults(1);

    return query.getSingleResult();
  }

  @Step
  public static List<DraftMarketOrder> getOrderById(String orderId) {
    Query<DraftMarketOrder> query = StockgateConnBridge.stockgateConnection.getSession()
      .createQuery("from DraftMarketOrder a where a.orderId=:orderId");
    query.setParameter("orderId", orderId);

    return query.getResultList();
  }

  @Step
  public static List<DraftMarketOrder> getListOrdersFromAcc(String tcbsId, String accountType) {
    Query<DraftMarketOrder> query = StockgateConnBridge.stockgateConnection.getSession()
      .createQuery("from DraftMarketOrder a where a.tcbsId=:tcbsId and a.accountType like :accountType");
    query.setParameter("tcbsId", tcbsId);
    query.setParameter("accountType", accountType);

    return query.getResultList();
  }
}
