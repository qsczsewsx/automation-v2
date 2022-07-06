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
@Table(name = "DERIVATIVE_OPEN_ACCOUNT")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DerivativeOpenAccountEntity {

  private static final String TCBS_ID = "tcbsId";
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;
  @Column(name = "TCBS_ID")
  private String tcbsId;
  @Column(name = "CODE_105C")
  private String code105C;
  @Column(name = "DERIVATIVE_ACCOUNT_CODE")
  private String derivativeAccountCode;
  @Column(name = "ACTIVATED_DATE")
  private ZonedDateTime activatedDate;
  @Column(name = "END_DATE_DERIVATIVE_REFUND")
  private ZonedDateTime endDateDerivativeRefund;
  @Column(name = "RAW_DATA")
  private String rawData;
  @Column(name = "CREATED_DATE")
  private Timestamp createdDate;

  @Step
  public static DerivativeOpenAccountEntity insert(DerivativeOpenAccountEntity entity) {
    ixuDbConnection.getSession().getTransaction().begin();
    ixuDbConnection.getSession().save(entity);
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
    return entity;
  }

  @Step
  public static void delete(String tcbsId) {
    ixuDbConnection.getSession().getTransaction().begin();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete DerivativeOpenAccountEntity a where a.tcbsId = :tcbsId"
    );
    query.setParameter(TCBS_ID, tcbsId);
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }

  @Step
  public static List<DerivativeOpenAccountEntity> findAllByTcbsId(String tcbsId) {
    ixuDbConnection.getSession().clear();
    Query<DerivativeOpenAccountEntity> query = ixuDbConnection.getSession().createQuery("" +
      "from DerivativeOpenAccountEntity a where a.tcbsId = :tcbsId order " +
      "by a.createdDate "
    );
    query.setParameter(TCBS_ID, tcbsId);
    List<DerivativeOpenAccountEntity> derivativeOpenAccountEntityList = query.getResultList();
    ixuDbConnection.closeSession();
    return derivativeOpenAccountEntityList;
  }

  @Step
  public static void clearAll() {
    ixuDbConnection.getSession().getTransaction().begin();
    Query query = ixuDbConnection.getSession().createQuery(
      "delete from DerivativeOpenAccountEntity"
    );
    query.executeUpdate();
    ixuDbConnection.getSession().getTransaction().commit();
    ixuDbConnection.closeSession();
  }
}
