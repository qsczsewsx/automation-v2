package com.tcbs.automation.iris.bond;

import com.tcbs.automation.staging.AwsStagingDwh;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@Table(name = "Iris_InvestmentLimit_Bond_Transaction")
public class PostUpdateBondLimitTransactionEntity {
  @Id
  @Column(name = "Id", nullable = false)
  private Double id;
  @Column(name = "Bond_Code")
  private String bondCode;
  @Column(name = "Action")
  private String action;
  @Column(name = "Value")
  private Double value;
  @Column(name = "Trading_Id")
  private Double tradingId;
  @Column(name = "Market_Type")
  private String marketType;
  @Column(name = "Updated_Date")
  private String updatedDate;
  private static final String FORMAT_DATE = "yyyy-MM-dd HH:mm:ss";

  public Double getId() {
    return id;
  }

  public void setId(Double id) {
    this.id = id;
  }

  public String getBondCode() {
    return bondCode;
  }

  public void setBondCode(String bondCode) {
    this.bondCode = bondCode;
  }

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public Double getValue() {
    return value;
  }

  public void setValue(Double value) {
    this.value = value;
  }

  public Double getTradingId() {
    return tradingId;
  }

  public void setTradingId(Double tradingId) {
    this.tradingId = tradingId;
  }

  public String getMarketType() {
    return marketType;
  }

  public void setMarketType(String marketType) {
    this.marketType = marketType;
  }

  public String getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(String updatedDate) {
    this.updatedDate = updatedDate;
  }

  @Step("insert data")
  public static void insertData(PostUpdateBondLimitTransactionEntity entity) {
    Session session = AwsStagingDwh.awsStagingDwhDbConnection.getSession();
    Transaction trans = session.beginTransaction();
    StringBuilder queryStringBuilder = new StringBuilder();
    Query<?> query;
    queryStringBuilder.append(" INSERT INTO Iris_InvestmentLimit_Bond_Transaction(Bond_Code,Action,Value,Trading_Id,Market_Type,Updated_Date) VALUES (?,?,?,?,?,CONVERT(datetime,GETDATE(),111))");
    query = session.createNativeQuery(queryStringBuilder.toString());
    query.setParameter(1, entity.getBondCode());
    query.setParameter(2, entity.getAction());
    query.setParameter(3, entity.getValue());
    query.setParameter(4, entity.getTradingId());
    query.setParameter(5, entity.getMarketType());
    query.executeUpdate();
    trans.commit();
    session.clear();
  }

  @Step("delete data")
  public static void deleteData(PostUpdateBondLimitTransactionEntity entity) {
    Session session = AwsStagingDwh.awsStagingDwhDbConnection.getSession();
    Transaction trans = session.beginTransaction();
    Query<?> query;

    query = session.createNativeQuery(" DELETE from Iris_InvestmentLimit_Bond_Transaction where Bond_Code = :bondCode");
    query.setParameter("bondCode", entity.getBondCode());
    query.executeUpdate();
    trans.commit();
    session.clear();
  }

  @Step("Get data from DB by Trading_Id and Market_Type")
  public static List<HashMap<String, Object>> getDataByTradingIdAndMarketType(String tradingId, String marketType) {
    StringBuilder query = new StringBuilder();
    query.append(" SELECT * FROM Iris_InvestmentLimit_Bond_Transaction where Trading_Id = :tradingId and Market_Type = :marketType ");
    try {
      List<HashMap<String, Object>> data = AwsStagingDwh.awsStagingDwhDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("tradingId", tradingId)
        .setParameter("marketType", marketType)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
      return data;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return new ArrayList<>();
  }
}
