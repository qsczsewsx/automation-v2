package com.tcbs.automation.pluto;

import com.tcbs.automation.ops.OPS;
import lombok.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SVR_VALUATION")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ValuationEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "ID")
  private Integer id;

  @Column(name = "COMPANY_NAME")
  private String companyName;

  @Column(name = "TICKER")
  private String ticker;

  @Column(name = "VALUATION_DATE")
  @Temporal(TemporalType.DATE)
  private Date valuationDate;

  @Column(name = "CREATED_DATE")
  @Temporal(TemporalType.DATE)
  private Date createdDate;

  @Column(name = "UPDATED_DATE")
  @Temporal(TemporalType.DATE)
  private Date updatedDate;

  @Column(name = "VALUE")
  private Double value;

  @Column(name = "UNIT")
  private String unit;

  @Column(name = "REQUEST")
  private String request;

  @Column(name = "CONTRACT_NO")
  private String contractNo;

  @Column(name = "STATUS")
  private String status;


  public static void removeByValuationID(Integer id) {
    Session session = OPS.opsConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();

    ValuationEntity entity = session.get(ValuationEntity.class, id);
    if (entity != null) {
      session.remove(entity);
    }
    trans.commit();
  }

  public static ValuationEntity getValuationByID(Integer id) {
    Session session = OPS.opsConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();

    Query<ValuationEntity> query = session.createQuery(
      "select c from ValuationEntity c where c.id = :id",
      ValuationEntity.class
    );
    query.setParameter("id", id);
    ValuationEntity res = query.getSingleResult();
    trans.commit();
    return res;
  }

  public boolean equalsInsert(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValuationEntity that = (ValuationEntity) o;
    return companyName.equals(that.companyName) && ticker.equals(that.ticker) && value.equals(that.value) && unit.equals(that.unit) && request.equals(that.request) && status.equals(that.status);
  }

  public boolean equalsUpdate(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ValuationEntity that = (ValuationEntity) o;
    return companyName.equals(that.companyName) && ticker.equals(that.ticker) && value.equals(that.value) && unit.equals(that.unit) && request.equals(that.request) && contractNo.equals(
      that.contractNo) && status.equals(that.status);
  }
}
