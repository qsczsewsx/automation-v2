package com.tcbs.automation.bondfeemanagement.entity.event;

import com.tcbs.automation.bondfeemanagement.BaseEntity;
import com.tcbs.automation.bondfeemanagement.BondFeeManagementConnection;
import lombok.*;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "FEE_EVENT_PAYMENT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeEventPayment extends BaseEntity {

  @Id
  @Column(name = "ID", nullable = false)
  private Integer id;

  @Column(name = "EVENT_ENGINE_INSTANCE_ID")
  private String eventEngineInstanceId;

  @Column(name = "PAYMENT_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date paymentDateDB;

  @Column(name = "PAYMENT_AMOUNT")
  private Long paymentAmount;

  @Column(name = "NOTE")
  private String note;

  @Step
  public static void deleteByFeeEventId(String eventEngineId) {
    Session session = BondFeeManagementConnection.CONNECTION.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    org.hibernate.query.Query<?> query = session.createNativeQuery("delete FEE_EVENT_PAYMENT where EVENT_ENGINE_INSTANCE_ID = :id");
    query.setParameter("id", eventEngineId);
    query.executeUpdate();
    trans.commit();
  }

}
