package com.tcbs.automation.tcbsdbapi2;

import com.tcbs.automation.dwh.Dwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "MonthlyPort_Customer")
public class MonthlyPortCustomerEntity {
  private String customerName;
  @Id
  private String custodyCd;
  private String idNumber;
  private String password;
  private String email;
  private Date dateReport;
  private int e;
  private Timestamp e2;

  @Step("Get All customer")
  public static List<HashMap<String, Object>> getAllCustody() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT CustodyCD FROM MonthlyPort_Customer  ");
    queryStringBuilder.append(" ORDER BY CustodyCD  ");

    try {
      List<HashMap<String, Object>> result = DbApi2.dbApiDb2Connection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

      Dwh.dwhDbConnection.closeSession();
      return result;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Step("Get customer info")
  public static List<HashMap<String, Object>> getCustomerInfo(String custodyCd) {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" SELECT customername, custodyCd, idnumber, password, email FROM MonthlyPort_Customer  ");
    queryStringBuilder.append(" where custodyCd = :custodyCd  ");

    try {
      List<HashMap<String, Object>> result = DbApi2.dbApiDb2Connection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setParameter("custodyCd", custodyCd)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();

      Dwh.dwhDbConnection.closeSession();
      return result;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }

  @Basic
  @Column(name = "CustomerName")
  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  @Basic
  @Column(name = "CustodyCD")
  public String getCustodyCd() {
    return custodyCd;
  }

  public void setCustodyCd(String custodyCd) {
    this.custodyCd = custodyCd;
  }

  @Basic
  @Column(name = "IDNumber")
  public String getIdNumber() {
    return idNumber;
  }

  public void setIdNumber(String idNumber) {
    this.idNumber = idNumber;
  }

  @Basic
  @Column(name = "password")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Basic
  @Column(name = "Email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Basic
  @Column(name = "DateReport")
  public Date getDateReport() {
    return dateReport;
  }

  public void setDateReport(Date dateReport) {
    this.dateReport = dateReport;
  }

  @Basic
  @Column(name = "e")
  public int getE() {
    return e;
  }

  public void setE(int e) {
    this.e = e;
  }

  @Basic
  @Column(name = "e2")
  public Timestamp getE2() {
    return e2;
  }

  public void setE2(Timestamp e2) {
    this.e2 = e2;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MonthlyPortCustomerEntity that = (MonthlyPortCustomerEntity) o;
    return e == that.e &&
      Objects.equals(customerName, that.customerName) &&
      Objects.equals(custodyCd, that.custodyCd) &&
      Objects.equals(idNumber, that.idNumber) &&
      Objects.equals(password, that.password) &&
      Objects.equals(email, that.email) &&
      Objects.equals(dateReport, that.dateReport) &&
      Objects.equals(e2, that.e2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(customerName, custodyCd, idNumber, password, email, dateReport, e, e2);
  }
}
