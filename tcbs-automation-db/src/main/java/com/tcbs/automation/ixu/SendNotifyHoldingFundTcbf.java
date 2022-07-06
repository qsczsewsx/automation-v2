package com.tcbs.automation.ixu;

import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.query.Query;

import java.sql.*;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static com.tcbs.automation.ixu.TcXu.ixuDbConnection;

@Entity
@Table(name = "SEND_NOTIFY_HOLDING_FUND_TCBF")
@Getter
@Setter
public class SendNotifyHoldingFundTcbf {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "ISSUED_DATE")
  private Timestamp issuedDate;
  @Column(name = "ORDER_ID")
  private String orderId;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;


  @Step
  public static void deleteSendNotify(List<String> tcbsIds) {
    ixuDbConnection.getSession().clear();
    ixuDbConnection.getSession().beginTransaction();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from SendNotifyHoldingFundTcbf a where a.tcbsId in :tcbsIds");
    query.setParameter("tcbsIds", tcbsIds);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
  }

  @Step
  public static List<SendNotifyHoldingFundTcbf> find(List<String> tcbsIds) {
    ixuDbConnection.getSession().clear();
    Query<SendNotifyHoldingFundTcbf> query = ixuDbConnection.getSession().createQuery(
      "from SendNotifyHoldingFundTcbf a where a.tcbsId in :tcbsIds"
    );
    query.setParameter("tcbsIds", tcbsIds);
    return query.getResultList();
  }

}
