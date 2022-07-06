package com.tcbs.automation.iris.loanreview;

import com.ibm.icu.text.SimpleDateFormat;
import com.tcbs.automation.iris.Aos;
import lombok.*;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "ELFM_Summaries")
public class ELFMSummariesEntity {
  @Id
  private String rownames;
  private String customerID;
  private Double el;

  @Step("insert data")
  public static void insertData(ELFMSummariesEntity entity) {
    Session session = Aos.aosDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    StringBuilder queryStringBuilder = new StringBuilder();
    Query<?> query;

    queryStringBuilder.append(" INSERT INTO ELFM_Summaries (rownames,CUSTODYCD,EL,DateReport) ");
    queryStringBuilder.append(" values (?, ?, ?, ?)");
    query = session.createNativeQuery(queryStringBuilder.toString());
    query.setParameter(1, entity.getRowNames());
    query.setParameter(2, entity.getCustomerId());
    query.setParameter(3, entity.getEL());
    query.setParameter(4, getMaxDate());
    query.executeUpdate();
    trans.commit();
  }

  @Step("delete data by object")
  public static void deleteData(ELFMSummariesEntity entity) {
    Session session = Aos.aosDbConnection.getSession();
    session.clear();
    Transaction trans = session.beginTransaction();
    Query<?> query;

    query = session.createNativeQuery(" DELETE from ELFM_Summaries where CUSTODYCD = :customerId");
    query.setParameter("customerId", entity.getCustomerId());
    query.executeUpdate();
    trans.commit();
  }

  @Step("get max date data in risk_margin_customer_detail table")
  public static Date getMaxDate() {
    StringBuilder query = new StringBuilder();
    Date maxDate = null;
    query.append(" select  DISTINCT DateReport from ELFM_Summaries where DateReport = (select max(DateReport) from ELFM_Summaries) ");
    try {
      HashMap<String, Object> date = (HashMap<String, Object>) Aos.aosDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList().get(0);
      String dateTime = date.get("DateReport").toString().replace(".0", "");
      maxDate = new SimpleDateFormat("yyyy-MM-dd").parse(dateTime);
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return maxDate;
  }

  @Step("get data in ELFM_Summaries table")
  public static HashMap<String, Object> getELFMSummaries(String customerId) {
    StringBuilder query = new StringBuilder();
    query.append(" select * from ELFM_Summaries where CUSTODYCD = :customerId and DateReport = (select max(DateReport) from ELFM_Summaries) ");
    try {
      HashMap<String, Object> summary = (HashMap<String, Object>) Aos.aosDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("customerId", customerId)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList().get(0);
      return summary;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new HashMap<>();
  }

  @Step("get data in ELFM_Summaries table")
  public static HashMap<String, Object> getEquity() {
    StringBuilder query = new StringBuilder();
    query.append(" select cast(-Total as numeric) 'Equity' from Liquidity_static ");
    query.append(" where updatedate = (select max(UpdateDate) from Liquidity_static) ");
    query.append(" and category = 'Equity' and GroupName = 'Equity' ");
    try {
      HashMap<String, Object> summary = (HashMap<String, Object>) Aos.aosDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList().get(0);
      return summary;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new HashMap<>();
  }

  @Basic
  @Column(name = "CUSTODYCD")
  public String getCustomerId() {
    return customerID;
  }

  public void setCustomerId(String customerID) {
    this.customerID = customerID;
  }

  @Basic
  @Column(name = "rownames")
  public String getRowNames() {
    return rownames;
  }

  public void setRowNames(String rownames) {
    this.rownames = rownames;
  }

  @Basic
  @Column(name = "EL")
  public Double getEL() {
    return el;
  }

  public void setEL(Double el) {
    this.el = el;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ELFMSummariesEntity that = (ELFMSummariesEntity) o;
    return Objects.equals(customerID, that.customerID) && Objects.equals(el, that.el) && Objects.equals(rownames, that.rownames);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerID, el, rownames);
  }
}

