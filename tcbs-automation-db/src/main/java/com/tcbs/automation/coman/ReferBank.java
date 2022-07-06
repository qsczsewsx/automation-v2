package com.tcbs.automation.coman;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "REFER_BANK")
public class ReferBank {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @NotNull
  @Column(name = "ID")
  private String id;
  @Column(name = "REFERENCE_RATE_ID")
  private Integer referenceRateId;
  @Column(name = "BANK_CODE")
  private String bankCode;
  @Column(name = "CITAD")
  private String citad;

  public static void deleteReferenceRateById(Integer referenceRateId) {
    Session session = Connection.comanDbConnection.getSession();
    session.clear();
    if (!session.getTransaction().isActive()) {
      session.beginTransaction();
    }
    Query<ReferBank> query = session.createQuery(
      "DELETE FROM ReferBank a WHERE a.referenceRateId = :referenceRateId"
    );
    query.setParameter("referenceRateId", referenceRateId);
    query.executeUpdate();
    session.getTransaction().commit();
  }

  @Step
  public static List<ReferBank> getRefBankByRefId(Integer referenceRateId) {
    Query<ReferBank> query = Connection.comanDbConnection.getSession()
      .createQuery("from ReferBank a where a.referenceRateId=:referenceRateId order by id asc", ReferBank.class);
    query.setParameter("referenceRateId", referenceRateId);
    return query.getResultList();
  }
}
