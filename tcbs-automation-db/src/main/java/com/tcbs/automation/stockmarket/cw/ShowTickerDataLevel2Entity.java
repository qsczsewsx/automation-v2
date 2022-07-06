package com.tcbs.automation.stockmarket.cw;

import com.tcbs.automation.stockmarket.Stockmarket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.HashMap;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class ShowTickerDataLevel2Entity {
  Integer industryId;
  @Id
  String ticker;
  String exchangeName;

  @Step("Get ticker, ex_name for each industry_id")
  public static java.util.List<HashMap<String, Object>> getListTicker() {
    StringBuilder queryStringBuilder = new StringBuilder();
    queryStringBuilder.append(" select IdLevel2, ticker, exchangeName from tca_vw_idata_index_industry_exchange_v2 ");
    queryStringBuilder.append(" where exchangeId in (0, 1, 3) and IdLevel2 is not null ");
    queryStringBuilder.append(" order by ticker");
    try {
      return Stockmarket.stockMarketConnection.getSession().createNativeQuery(queryStringBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE).getResultList();
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }

    return new ArrayList<>();
  }

  public Integer getIndustryId() {
    return industryId;
  }

  public void setIndustryId(Integer industryId) {
    this.industryId = industryId;
  }

  public String getTicker() {
    return ticker;
  }

  public void setTicker(String ticker) {
    this.ticker = ticker;
  }

  public String getExchangeName() {
    return exchangeName;
  }

  public void setExchangeName(String exchangeName) {
    this.exchangeName = exchangeName;
  }
}

