package com.tcbs.automation.iris;

import com.tcbs.automation.staging.StgTcbCustomer;
import lombok.Getter;
import lombok.Setter;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static com.tcbs.automation.DataBaseUtils.beginTransaction;

@Getter
@Setter
@Entity
@Table(name = "RISK_COMP_ALLOWED")
public class RiskCompAllowed {
  static final Logger logger = LoggerFactory.getLogger(RiskCompAllowed.class);
  @Column(name = "NCOMALLOW")
  private Integer num;

  @Id
  @Column(name = "TICKER")
  private String ticker;

  @Step("get num of nCompAllow")
  public static Integer countAllRiskComAlow() {
    StringBuilder query = new StringBuilder();
    query.append("select * from RISK_COMP_ALLOWED ");
    try {
      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList().size();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    AwsIRis.AwsIRisDbConnection.getSession().close();
    return 0;
  }

  @Step("get num of nCompAllow by ticker")
  public static Integer countRiskComAllowByTicker(String ticker) {
    StringBuilder query = new StringBuilder();
    query.append("select * from RISK_COMP_ALLOWED where TICKER = :ticker ");
    try {
      return AwsIRis.AwsIRisDbConnection.getSession().createNativeQuery(query.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList().size();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    AwsIRis.AwsIRisDbConnection.getSession().close();
    return 0;
  }

  @Step("insert or update data")
  public boolean saveOrUpdateRiskCompAllowed(RiskCompAllowed riskCompAllowed) {
    try {
      Session session = AwsIRis.AwsIRisDbConnection.getSession();
      session.beginTransaction();
      session.save(riskCompAllowed);
      session.getTransaction().commit();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
      return false;
    }
    return true;
  }

  @Step("delete data by key")
  public void deleteByTicker(String ticker) {
    Session session = AwsIRis.AwsIRisDbConnection.getSession();
    session.clear();
    beginTransaction(session);
    Query<StgTcbCustomer> query = session.createQuery(
      "DELETE FROM RiskCompAllowed i WHERE i.ticker=:ticker"
    );
    query.setParameter("ticker", ticker);
    query.executeUpdate();
    session.getTransaction().commit();
  }

}
