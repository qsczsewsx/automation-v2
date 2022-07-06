package com.tcbs.automation.tca.rating;

import com.tcbs.automation.tca.TcAnalysis;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thucydides.core.steps.StepEventBus;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GeneralRating {
  private static final Logger log = LoggerFactory.getLogger(GeneralRating.class);

  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  @Id
  private String ticker;

  @Column(name = "highestPrice")
  private Double highestPrice;

  @Column(name = "lowestPrice")
  private Double lowestPrice;

  @Column(name = "priceChange3m")
  private Double priceChange3m;

  @Column(name = "priceChange1y")
  private Double priceChange1y;

  @Column(name = "beta")
  private Double beta;

  @Column(name = "alpha")
  private Double alpha;

  public Map<String, Object> getByTicker(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append("SELECT Ticker, Price52WHighest as highestPrice, Price52WLowest as lowestPrice, ");
    queryBuilder.append("PriceChange3m, PriceChange1y ");
    queryBuilder.append("FROM tbl_idata_price_summary ");
    queryBuilder.append("WHERE Ticker  = :ticker ");

    try {
      List<Map<String, Object>> listPrice = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      if (listPrice.size() > 0) {
        return listPrice.get(0);
      } else {
        return null;
      }
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  public List<Map<String, Object>> getListTickerTop200() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT TOP(200) r.Ticker  ");
    queryBuilder.append(" FROM	stx_rto_RatioTTMDaily r  ");
    queryBuilder.append(" WHERE	CAST(UpdateDate AS date) = CAST((select MAX(UpdateDate) from stx_rto_RatioTTMDaily) AS date)  ");
    queryBuilder.append(" ORDER BY RTD11 DESC  ");

    try {
      List<Map<String, Object>> listTickers = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return listTickers;
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  public Map<String, Object> getByTop200() {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT 'TOP200' as Ticker,  ");
    queryBuilder.append(" AVG(Price52WHighest) as highestPrice,  ");
    queryBuilder.append(" AVG(Price52WLowest) as lowestPrice,  ");
    queryBuilder.append(" AVG(PriceChange3m) as PriceChange3m,  ");
    queryBuilder.append(" AVG(PriceChange1y) as PriceChange1y  ");
    queryBuilder.append(" FROM tbl_idata_price_summary  ");
    queryBuilder.append(" WHERE Ticker IN ( SELECT TOP(200) r.Ticker  ");
    queryBuilder.append("                 FROM	stx_rto_RatioTTMDaily r  ");
    queryBuilder.append("                 WHERE	CAST(UpdateDate AS date) = CAST((select MAX(UpdateDate) from stx_rto_RatioTTMDaily) AS date)  ");
    queryBuilder.append("                 ORDER BY RTD11 DESC )  ");

    try {
      List<Map<String, Object>> listPrice = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return listPrice.get(0);
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }

  public Map<String, Object> getByIndustry(String ticker) {
    StringBuilder queryBuilder = new StringBuilder();
    queryBuilder.append(" SELECT 'INDUSTRY' as Ticker,  ");
    queryBuilder.append(" AVG(Price52WHighest) as highestPrice,  ");
    queryBuilder.append(" AVG(Price52WLowest) as lowestPrice,  ");
    queryBuilder.append(" AVG(PriceChange3m) as PriceChange3m,  ");
    queryBuilder.append(" AVG(PriceChange1y) as PriceChange1y  ");
    queryBuilder.append(" FROM tbl_idata_price_summary  ");
    queryBuilder.append(" WHERE Ticker IN ( SELECT comp.OrganCode FROM stx_cpf_Organization comp  ");
    queryBuilder.append("               JOIN view_idata_industry vwind  ");
    queryBuilder.append("               ON comp.IcbCode = vwind.IdLevel4  ");
    queryBuilder.append("               where vwind.IdLevel2 = ( SELECT IdLevel2 FROM stx_cpf_Organization comp  ");
    queryBuilder.append("                                     JOIN view_idata_industry vwind  ");
    queryBuilder.append("                                     ON vwind.IdLevel4 = comp.IcbCode  ");
    queryBuilder.append("                                     WHERE comp.OrganCode = :ticker) )  ");

    try {
      List<Map<String, Object>> listPrice = TcAnalysis.tcaDbConnection.getSession().createNativeQuery(queryBuilder.toString())
        .setParameter("ticker", ticker)
        .setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE)
        .getResultList();
      return listPrice.get(0);
    } catch (Exception ex) {
      StepEventBus.getEventBus().testFailed(new AssertionError(ex.getMessage()));
    }
    return null;
  }
}
