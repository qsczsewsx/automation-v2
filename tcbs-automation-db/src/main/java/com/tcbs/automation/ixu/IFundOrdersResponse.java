package com.tcbs.automation.ixu;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "IFUND_ORDERS_RESPONSE")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IFundOrdersResponse {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "GT_ID")
  private String gtId;
  @Column(name = "GT_ISSUED_DATE")
  private LocalDateTime gtIssuedDate;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "TO_MATCHED_DATE")
  private LocalDateTime toMatchedDate;
  @Column(name = "FUND_NUMBER_ORDERS")
  private Integer fundNumberOrders;
  @Column(name = "FUND_RESPONSE")
  private String fundResponse;
  @Column(name = "BOND_NUMBER_ORDERS")
  private Integer bondNumberOrders;
  @Column(name = "BOND_RESPONSE")
  private String bondResponse;
  @Column(name = "ICONNECT_NUMBER_ORDERS")
  private Integer iconnectNumberOrders;
  @Column(name = "ICONNECT_RESPONSE")
  private String iconnectResponse;
  @Column(name = "CREATED_DATE")
  private LocalDateTime createdDate;
  @Column(name = "LAST_MODIFIED_DATE")
  private LocalDateTime lastModifiedDate;

  @Step
  public static void deleteAll() {
    Session session = ixuDbConnection.getSession();
    session.clear();
    session.beginTransaction();
    Query query = session.createQuery("DELETE FROM IFundOrdersResponse WHERE 1 = 1");
    query.executeUpdate();
    session.getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<IFundOrdersResponse> getAll() {
    Session session = ixuDbConnection.getSession();
    session.clear();
    Query query = session.createQuery("from IFundOrdersResponse order by tcbsId", IFundOrdersResponse.class);
    List<IFundOrdersResponse> resultList = query.getResultList();
    ixuDbConnection.closeSession();
    return resultList;
  }
}
